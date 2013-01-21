package maximemeire.phantom.model.entity.player.update;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import maximemeire.phantom.model.entity.MovementNode;
import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateFlags.PlayerUpdateFlag;
import maximemeire.phantom.network.DynamicOutboundBuffer;

/**
 * @author Maxime Meire
 *
 */
public class PlayerUpdate {
	
	protected PlayerUpdateNode updateNode;
	protected Set<Integer> localList = new LinkedHashSet<Integer>();
	protected Set<Integer> additionList = new LinkedHashSet<Integer>();
	protected Set<Integer> removalList = new LinkedHashSet<Integer>();
	protected Set<Integer> cachedList = new LinkedHashSet<Integer>();
	protected Map<Integer, PlayerUpdateNode> otherUpdates = new HashMap<Integer, PlayerUpdateNode>();
	private int movementSize = 512;
	private int generalSize = 256;

	protected static DynamicOutboundBuffer encodePayload(Player player) {
		try {
			PlayerUpdate update = player.getUpdate();
			int movementSize = update.movementSize + (1 / 4) * update.movementSize;
			DynamicOutboundBuffer payload = new DynamicOutboundBuffer(movementSize);
			int generalSize = update.generalSize + (1 / 2) * update.generalSize;
			DynamicOutboundBuffer masks = new DynamicOutboundBuffer(generalSize);
			payload.start_bit_access();
			encodeThisMovement(player, payload);
			encodeGeneralUpdate(update.updateNode, masks, false);
			payload.put_bits(8, update.localList.size());
			encodeOtherMovementUpdates(player, payload, masks);
			encodeAddFOVPlayer(player, payload, masks);
			if (masks.readable()) {
				payload.put_bits(11, 2047);
				payload.end_bit_access();
				payload.writeBytes(masks);
			} else {
				payload.end_bit_access();
			}
			return payload;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected static void encodeThisMovement(Player player, DynamicOutboundBuffer movement) {
		PlayerUpdate update = player.getUpdate();
		if (update.updateNode.teleported || update.updateNode.changedRegion) {
			movement.put_bits(1, 1);
			movement.put_bits(2, 3);
			movement.put_bits(2, player.getLocation().getZ());
			movement.put_bits(1, update.updateNode.teleported ? 1 : 0);
			movement.put_bits(1, update.updateNode.requireUpdate() ? 1 : 0);
			movement.put_bits(7, player.getClientLoadedBase().deltaY(player.getLocation()));
			movement.put_bits(7, player.getClientLoadedBase().deltaX(player.getLocation()));
		} else {
			if (update.updateNode.movement.notMoving()) {
				if (update.updateNode.requireUpdate()) {
					movement.put_bits(1, 1);
					movement.put_bits(2, 0);
				} else {
					movement.put_bits(1, 0);
				}
			} else if (update.updateNode.movement.walking()) {
				movement.put_bits(1, 1);
				movement.put_bits(2, 1);
				movement.put_bits(3, update.updateNode.movement.getFirst());
				movement.put_bits(1, update.updateNode.requireUpdate() ? 1 : 0);
			} else {
				movement.put_bits(1, 1);
				movement.put_bits(2, 2);
				movement.put_bits(3, update.updateNode.movement.getFirst());
				movement.put_bits(3, update.updateNode.movement.getSecond());
				movement.put_bits(1, update.updateNode.requireUpdate() ? 1 : 0);
			}
		}
	}
	
	protected static void encodeOtherMovementUpdates(Player player, DynamicOutboundBuffer movement, DynamicOutboundBuffer general) throws Exception {
		PlayerUpdate update = player.getUpdate();
		for (int id : update.localList) {
			if (update.additionList.contains(id))
				throw new Exception("Player is on the addition list but already on the local list");
			if (update.removalList.contains(id)) {
				movement.put_bits(1, 1);
				movement.put_bits(2, 3);
				update.localList.remove(id);
				update.otherUpdates.remove(id);
				update.cachedList.remove(id);
				continue;
			} else if (player.getLocation().absDeltaX(update.updateNode.location) > 15 
					&& player.getLocation().absDeltaX(update.updateNode.location) > 15
					&& player.getLocation().getZ() == update.updateNode.location.getZ()) {
				movement.put_bits(1, 0);
				update.cachedList.add(id);
				continue;
			} else {
				PlayerUpdateNode updateNode = update.otherUpdates.get(id);
				MovementNode movementNode = updateNode.movement;
				if (movementNode.notMoving()) {
					if (updateNode.requireUpdate()) {
						movement.put_bits(1, 1); 
						movement.put_bits(2, 0); 
					} else 
						movement.put_bits(1, 0);
				} else if (movementNode.walking()) {
					movement.put_bits(1, 1); 
					movement.put_bits(2, 1); 
					movement.put_bits(3, movementNode.getFirst());
					movement.put_bits(1, updateNode.requireUpdate() ? 1 : 0);
				} else {
					movement.put_bits(1, 1);
					movement.put_bits(2, 2);
					movement.put_bits(3, movementNode.getFirst());
					movement.put_bits(3, movementNode.getSecond());
					movement.put_bits(1, updateNode.requireUpdate() ? 1 : 0);
				}
				if (updateNode.requireUpdate()) {
					encodeGeneralUpdate(updateNode, general, false); 
				}
			}
		}
	}
	
	protected static void encodeGeneralUpdate(PlayerUpdateNode updateNode, DynamicOutboundBuffer general, boolean forceAppearance) {
		if (!updateNode.requireUpdate() && !forceAppearance) {
			return;
		}
		if (updateNode.hasCachedMaskBlock() && !forceAppearance) {
			general.writeBytes(updateNode.cachedMaskBlock);
			return;
		}
		DynamicOutboundBuffer cacheBlock = new DynamicOutboundBuffer(100);
		int mask = updateNode.mask;
		if (mask >= 0x100) {
			mask |= 0x40;
			cacheBlock.put8(mask & 0xff);
			cacheBlock.put8(mask >> 8);
		} else
			cacheBlock.put8(mask);
		if (updateNode.requireGraphicsUpdate()) {
			cacheBlock.writeBytes(updateNode.masks[PlayerUpdateFlag.GRAPHICS_UPDATE_MASK.ordinal()].payload);
		}
		if (updateNode.requireAnimationUpdate()) {
			cacheBlock.writeBytes(updateNode.masks[PlayerUpdateFlag.ANIMATION_UPDATE_MASK.ordinal()].payload);
		}
		if (updateNode.requireForcedChatUpdate()) {
			cacheBlock.writeBytes(updateNode.masks[PlayerUpdateFlag.FORCED_CHAT_UPDATE_MASK.ordinal()].payload);
		}
		if (updateNode.requireChatUpdate()) {
			cacheBlock.writeBytes(updateNode.masks[PlayerUpdateFlag.CHAT_UPDATE_MASK.ordinal()].payload);
		}
		if (updateNode.requireFaceEntityUpdate()) {
			cacheBlock.writeBytes(updateNode.masks[PlayerUpdateFlag.FACE_ENTITY_UPDATE_MASK.ordinal()].payload);
		}
		if (updateNode.requireAppearanceUpdate()) {
			cacheBlock.writeBytes(updateNode.masks[PlayerUpdateFlag.APPEARANCE_UPDATE_MASK.ordinal()].payload);
		}
		if (updateNode.requireFaceCoordinateUpdate()) {
			cacheBlock.writeBytes(updateNode.masks[PlayerUpdateFlag.FACE_COORDINATE_UPDATE_MASK.ordinal()].payload);
		}
		if (updateNode.requireHitUpdate()) {
			cacheBlock.writeBytes(updateNode.masks[PlayerUpdateFlag.HIT_UPDATE_MASK.ordinal()].payload);
		}
		if (updateNode.requireHit2Update()) {
			cacheBlock.writeBytes(updateNode.masks[PlayerUpdateFlag.HIT2_UPDATE_MASK.ordinal()].payload);
		}
		if (!forceAppearance) {
			updateNode.cachedMaskBlock = cacheBlock;
		}
		general.writeBytes(cacheBlock);
	}
	
	protected static void encodeAddFOVPlayer(Player player, DynamicOutboundBuffer movement, DynamicOutboundBuffer general) throws Exception {
		PlayerUpdate update = player.getUpdate();
		for (int id : update.additionList) {
			if (update.localList.contains(id))
				throw new Exception("Player is already on the local list although scheduled to be added to it");
			if (update.localList.size() >= 255) 
				return;
			if (update.cachedList.contains(id))
				continue;
			if (player.getLocation().absDeltaX(update.updateNode.location) <= 15 
					&& player.getLocation().absDeltaX(update.updateNode.location) <= 15
					&& player.getLocation().getZ() == update.updateNode.location.getZ()) {
				PlayerUpdateNode updateNode = update.otherUpdates.get(id);
				movement.put_bits(11, updateNode.index);
				movement.put_bits(1, 1);
				movement.put_bits(1, 1);
				int deltaX = updateNode.location.deltaX(player.getLocation());
				int deltaY = updateNode.location.deltaY(player.getLocation());
				movement.put_bits(5, deltaY);
				movement.put_bits(5, deltaX);
				encodeGeneralUpdate(updateNode, general, true);
				update.localList.add(id);
				update.additionList.remove(id);
			} else {
				continue;
			}
		}
		for (int id : update.cachedList) {
			if (update.localList.contains(id))
				throw new Exception("Player is already on the local list although also cached.");
			if (update.localList.size() >= 255)
				return;
			if (player.getLocation().absDeltaX(update.updateNode.location) <= 15 
					&& player.getLocation().absDeltaX(update.updateNode.location) <= 15
					&& player.getLocation().getZ() == update.updateNode.location.getZ()) {
				PlayerUpdateNode updateNode = update.otherUpdates.get(id);
				movement.put_bits(11, updateNode.index);
				movement.put_bits(1, 1); // force appearance until having the logic in place to check for it
				movement.put_bits(1, 0);
				int deltaX = updateNode.location.deltaX(player.getLocation());
				int deltaY = updateNode.location.deltaY(player.getLocation());
				movement.put_bits(5, deltaY);
				movement.put_bits(5, deltaX);
				encodeGeneralUpdate(updateNode, general, true);
				update.localList.add(id);
				update.cachedList.remove(id);
			}
		}
	}
	
}
