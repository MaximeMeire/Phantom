package maximemeire.phantom.model.map;

import maximemeire.phantom.concurrent.Actor;

public class Segment extends Actor {
	
	private static int counter = 0;

	private int x;
	private int y;
	
	private Tile[][][] tiles;
	
	
	
	@Override
	protected int getActorIdCounter() {
		return counter++;
	}
	
	/**
	 * Gets the x coordinate in the {@link Region} coordinate system.
	 * @return The x coordinate of the {@link Region} coordinate system.
	 */
	private int getRegionX() {
		return x >> 3;
	}
	
	/**
	 * Gets the y coordinate in the {@link Region} coordinate system.
	 * @return The y coordinate of the {@link Region} coordinate system.
	 */
	private int getRegionY() {
		return y >> 3;
	}
	
	/**
	 * Gets the x coordinate in the {@link Tile} coordinate system of
	 * the base of this segment's {@link Region}.
	 * @return The x coordinate in the {@link Tile} coordinate system of
	 * the base of this segment's {@link Region}.
	 */
	private int getRegionAbsX() {
		return getRegionX() << 6;
	}
	
	/**
	 * Gets the y coordinate in the {@link Tile} coordinate system of
	 * the base of this segment's {@link Region}.
	 * @return The y coordinate in the {@link Tile} coordinate system of
	 * the base of this segment's {@link Region}.
	 */
	private int getRegionAbsY() {
		return getRegionY() << 6;
	}
	
	/**
	 * Get the x coordinate in the {@link Tile} coordinate system.
	 * This is the base of this segment, being in the south west corner.
	 * @return The x coordinate in the {@link Tile} coordinate system.
	 */
	private int getAbsX() {
		return x << 3;
	}
	
	/**
	 * Get the y coordinate in the {@link Tile} coordinate system.
	 * This is the base of this segment, being in the south west corner.
	 * @return The y coordinate in the {@link Tile} coordinate system.
	 */
	private int getAbsY() {
		return y << 3;
	}
	
	/**
	 * Gets the x coordinate in the {@link Segment} coordinate system.
	 * @return The x coordinate the in the {@link Segment} coordinate system.
	 */
	private int getX() {
		return x;
	}
	
	/**
	 * Gets the y coordinate in the {@link Segment} coordinate system.
	 * @return The y coordinate the in the {@link Segment} coordinate system.
	 */
	private int getY() {
		return y;
	}

}
