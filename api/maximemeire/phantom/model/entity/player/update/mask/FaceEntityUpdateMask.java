package maximemeire.phantom.model.entity.player.update.mask;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateFlags.PlayerUpdateFlag;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateMask;
import maximemeire.phantom.network.DynamicOutboundBuffer;

public class FaceEntityUpdateMask extends PlayerUpdateMask {

	public FaceEntityUpdateMask(Player player) {
		super(player, PlayerUpdateFlag.FACE_ENTITY_UPDATE_MASK, FACE_ENTITY_UPDATE_BIT);
	}

	@Override
	public DynamicOutboundBuffer encode(Player player) {
		return null;
	}

}
