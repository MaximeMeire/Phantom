package maximemeire.phantom.model.entity.player.update.mask;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateFlags.PlayerUpdateFlag;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateMask;
import maximemeire.phantom.network.DynamicOutboundBuffer;

public class AnimationUpdateMask extends PlayerUpdateMask {

	public AnimationUpdateMask(Player player) {
		super(player, PlayerUpdateFlag.ANIMATION_UPDATE_MASK, ANIMATION_UPDATE_BIT);
	}

	@Override
	public DynamicOutboundBuffer encode(Player player) {
		return null;
	}

}
