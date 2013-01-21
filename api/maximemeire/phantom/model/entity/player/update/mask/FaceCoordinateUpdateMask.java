package maximemeire.phantom.model.entity.player.update.mask;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateFlags.PlayerUpdateFlag;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateMask;
import maximemeire.phantom.network.DynamicOutboundBuffer;

public class FaceCoordinateUpdateMask extends PlayerUpdateMask {

	public FaceCoordinateUpdateMask(Player player) {
		super(player, PlayerUpdateFlag.FACE_COORDINATE_UPDATE_MASK, FACE_COORDINATE_UPDATE_BIT);
	}

	@Override
	public DynamicOutboundBuffer encode(Player player) {
		return null;
	}

}
