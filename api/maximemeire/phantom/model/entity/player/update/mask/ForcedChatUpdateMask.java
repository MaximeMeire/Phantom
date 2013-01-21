package maximemeire.phantom.model.entity.player.update.mask;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateFlags.PlayerUpdateFlag;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateMask;
import maximemeire.phantom.network.DynamicOutboundBuffer;

public class ForcedChatUpdateMask extends PlayerUpdateMask {

	public ForcedChatUpdateMask(Player player) {
		super(player, PlayerUpdateFlag.FORCED_CHAT_UPDATE_MASK, FORCED_CHAT_UPDATE_BIT);
	}

	@Override
	public DynamicOutboundBuffer encode(Player player) {
		return null;
	}

}
