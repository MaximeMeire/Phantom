package maximemeire.phantom.model.entity.player.update;

import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.entity.player.update.PlayerUpdateFlags.PlayerUpdateFlag;
import maximemeire.phantom.network.DynamicOutboundBuffer;

public abstract class PlayerUpdateMask {
	
	public static final int GRAPHICS_UPDATE_BIT = 0x100;
	public static final int ANIMATION_UPDATE_BIT = 0x8;
	public static final int FORCED_CHAT_UPDATE_BIT = 0x4;
	public static final int CHAT_UPDATE_BIT = 0x80;
	public static final int FACE_ENTITY_UPDATE_BIT = 0x1;
	public static final int APPEARANCE_UPDATE_BIT = 0x10;
	public static final int FACE_COORDINATE_UPDATE_BIT = 0x2;
	public static final int HIT_UPDATE_BIT = 0x20;
	public static final int HIT2_UPDATE_BIT = 0x200;
	
	protected final PlayerUpdateFlag type;
	protected final int bit;
	protected DynamicOutboundBuffer payload;
	
	public PlayerUpdateMask(Player player, PlayerUpdateFlag type, int bit) {
		this.type = type;
		this.bit = bit;
		this.payload = encode(player);
	}
	
	public abstract DynamicOutboundBuffer encode(Player player);

}
