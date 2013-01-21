package maximemeire.phantom.model.entity.player.update.mask;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateFlags.PlayerUpdateFlag;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateMask;
import maximemeire.phantom.network.DynamicOutboundBuffer;

public class Hit2UpdateMask extends PlayerUpdateMask {

	public Hit2UpdateMask(Player player) {
		super(player, PlayerUpdateFlag.HIT2_UPDATE_MASK, HIT2_UPDATE_BIT);
	}

	@Override
	public DynamicOutboundBuffer encode(Player player) {
		return null;
	}

}
