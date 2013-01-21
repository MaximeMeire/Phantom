package maximemeire.phantom.model.entity.player.update;

import java.util.BitSet;

public class PlayerUpdateFlags {
	
	protected BitSet flags = new BitSet();
	
	public static enum PlayerUpdateFlag {
		GRAPHICS_UPDATE_MASK,
		ANIMATION_UPDATE_MASK,
		FORCED_CHAT_UPDATE_MASK,
		CHAT_UPDATE_MASK,
		FACE_ENTITY_UPDATE_MASK,
		APPEARANCE_UPDATE_MASK,
		FACE_COORDINATE_UPDATE_MASK,
		HIT_UPDATE_MASK,
		HIT2_UPDATE_MASK;
	}
	
	public boolean requireUpdate() {
		return !flags.isEmpty();
	}
	
	public void flag(PlayerUpdateFlag flag) {
		flags.set(flag.ordinal(), true);
	}
	
	public void unflag(PlayerUpdateFlag flag) {
		flags.set(flag.ordinal(), false);
	}
	
	public void set(PlayerUpdateFlag flag, boolean value) {
		flags.set(flag.ordinal(), value);
	}
	
	public boolean isFlagged(PlayerUpdateFlag flag) {
		return flags.get(flag.ordinal());
	}
	
	public void clear() {
		flags.clear();
	}
	
}
