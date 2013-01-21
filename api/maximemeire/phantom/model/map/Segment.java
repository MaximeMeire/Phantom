package maximemeire.phantom.model.map;

import maximemeire.phantom.concurrent.Actor;
import maximemeire.phantom.model.map.message.LoadSegmentMessage;
import maximemeire.phantom.model.map.message.SegmentLoadedMessage;

public class Segment extends Actor {

	protected final int x;
	protected final int y;
	
	protected Tile[][][] tiles;
	
	public Segment(int x, int y) {
		this.x = x;
		this.y = y;
		this.tiles = new Tile[4][8][8];
	}
	
	public void loadSegment(LoadSegmentMessage<Segment> message) {
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					this.tiles[z][x][y] = new Tile(getAbsX() + x, getAbsY() + y, z);
				}
			}
		}		
		message.reply(new SegmentLoadedMessage<Region>(getLink(message.getSenderReference())));
	}
	
	// TODO finish registration entities
	
	@Override
	protected int getActorId() {
		return (x << 16) | y;
	}
	
	/**
	 * Gets the x coordinate in the {@link Region} coordinate system.
	 * @return The x coordinate of the {@link Region} coordinate system.
	 */
	protected int getRegionX() {
		return x >> 3;
	}
	
	/**
	 * Gets the y coordinate in the {@link Region} coordinate system.
	 * @return The y coordinate of the {@link Region} coordinate system.
	 */
	protected int getRegionY() {
		return y >> 3;
	}
	
	/**
	 * Gets the x coordinate in the {@link Tile} coordinate system of
	 * the base of this segment's {@link Region}.
	 * @return The x coordinate in the {@link Tile} coordinate system of
	 * the base of this segment's {@link Region}.
	 */
	protected int getRegionAbsX() {
		return getRegionX() << 6;
	}
	
	/**
	 * Gets the y coordinate in the {@link Tile} coordinate system of
	 * the base of this segment's {@link Region}.
	 * @return The y coordinate in the {@link Tile} coordinate system of
	 * the base of this segment's {@link Region}.
	 */
	protected int getRegionAbsY() {
		return getRegionY() << 6;
	}
	
	/**
	 * Get the x coordinate in the {@link Tile} coordinate system.
	 * This is the base of this segment, being in the south west corner.
	 * @return The x coordinate in the {@link Tile} coordinate system.
	 */
	protected int getAbsX() {
		return x << 3;
	}
	
	/**
	 * Get the y coordinate in the {@link Tile} coordinate system.
	 * This is the base of this segment, being in the south west corner.
	 * @return The y coordinate in the {@link Tile} coordinate system.
	 */
	protected int getAbsY() {
		return y << 3;
	}
	
	/**
	 * Gets the x coordinate in the {@link Segment} coordinate system.
	 * @return The x coordinate the in the {@link Segment} coordinate system.
	 */
	protected int getX() {
		return x;
	}
	
	/**
	 * Gets the y coordinate in the {@link Segment} coordinate system.
	 * @return The y coordinate the in the {@link Segment} coordinate system.
	 */
	protected int getY() {
		return y;
	}

}
