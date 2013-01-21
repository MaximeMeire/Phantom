package maximemeire.phantom.model.entity.player.update;

import maximemeire.phantom.model.entity.MovementNode;
import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateFlags.PlayerUpdateFlag;
import maximemeire.phantom.model.map.Location;
import maximemeire.phantom.model.map.Segment;
import maximemeire.phantom.network.DynamicOutboundBuffer;

public class PlayerUpdateNode {
	
	protected int index;
	protected Location location;
	protected Segment segment;
	protected MovementNode movement;
	protected PlayerUpdateMask[] masks = new PlayerUpdateMask[PlayerUpdateFlag.values().length];
	protected PlayerUpdateFlags flags = new PlayerUpdateFlags();
	protected int mask = 0;
	protected DynamicOutboundBuffer cachedMaskBlock = null;
	protected boolean teleported = false; // TODO implement logic
	protected boolean changedRegion = false; // TODO implement logic
	
	public void appendUpdateMask(Player player, PlayerUpdateMask mask) {
		mask.encode(player);
		this.masks[mask.type.ordinal()] = mask;
		this.mask |= mask.bit;
	}
	
	public void clearFlags() {
		this.mask = 0;
		for (int i = 0; i < masks.length; i++) {
			this.flags.clear();
		}
	}
	
	public void resetFlag(PlayerUpdateFlag flag) {
		this.flags.unflag(flag);
	}
	
	public void setUpdateMaskPayload(PlayerUpdateFlag mask, DynamicOutboundBuffer payload) {
		this.masks[mask.ordinal()].payload = payload;
		this.cachedMaskBlock = null;
	}
	
	public void resetUpdateMaskPayload(int id) {
		this.masks[id].payload = null;
	}
	
	public void resetUpdateMaskPayload(PlayerUpdateFlag flag) {
		this.masks[flag.ordinal()].payload = null;
	}
	
	public void setCachedMaskBlock(DynamicOutboundBuffer payload) {
		this.cachedMaskBlock = payload;
	}
	
	public boolean hasCachedMaskBlock() {
		return cachedMaskBlock != null;
	}
	
	public boolean requireUpdate() {
		return flags.requireUpdate();
	}
	
	public boolean requireGraphicsUpdate() {
		return flags.isFlagged(PlayerUpdateFlag.GRAPHICS_UPDATE_MASK); 
	}
	
	public boolean requireAnimationUpdate() {
		return flags.isFlagged(PlayerUpdateFlag.ANIMATION_UPDATE_MASK);
	}
	
	public boolean requireForcedChatUpdate() {
		return flags.isFlagged(PlayerUpdateFlag.FORCED_CHAT_UPDATE_MASK);
	}
	
	public boolean requireChatUpdate() {
		return flags.isFlagged(PlayerUpdateFlag.CHAT_UPDATE_MASK);
	}
	
	public boolean requireFaceEntityUpdate() {
		return flags.isFlagged(PlayerUpdateFlag.FACE_ENTITY_UPDATE_MASK);
	}
	
	public boolean requireAppearanceUpdate() {
		return flags.isFlagged(PlayerUpdateFlag.APPEARANCE_UPDATE_MASK);
	}
	
	public boolean requireFaceCoordinateUpdate() {
		return flags.isFlagged(PlayerUpdateFlag.FACE_COORDINATE_UPDATE_MASK);
	}
	
	public boolean requireHitUpdate() {
		return flags.isFlagged(PlayerUpdateFlag.HIT_UPDATE_MASK);
	}
	
	public boolean requireHit2Update() {
		return flags.isFlagged(PlayerUpdateFlag.HIT2_UPDATE_MASK);
	}

}
