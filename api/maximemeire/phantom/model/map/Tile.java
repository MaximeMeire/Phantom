package maximemeire.phantom.model.map;

public class Tile {

	private int x;
	private int y;
	private int z;
	
	public Tile(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Gets the x coordinate in the {@link Region} coordinate system.
	 * @return The x coordinate in the {@link Region} coordinate system.
	 */
	private int getRegionX() {
		return x >> 6;
	}
	
	/**
	 * Gets the y coordinate in the {@link Region} coordinate system.
	 * @return The y coordinate in the {@link Region} coordinate system.
	 */
	private int getRegionY() {
		return y >> 6;
	}
	
	/**
	 * Gets the x coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Region}. 
	 * @return The x coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Region}. 
	 */
	private int getRegionAbsX() {
		return getRegionX() << 6;
	}
	
	/**
	 * Gets the y coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Region}. 
	 * @return The y coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Region}. 
	 */
	private int getRegionAbsY() {
		return getRegionY() << 6;
	}
	
	/**
	 * Gets the x coordinate in the {@link Segment} coordinate system.
	 * @return The x coordinate in the {@link Segment} coordinate system.
	 */
	private int getSegmentX() {
		return x >> 3;
	}
	
	/**
	 * Gets the y coordinate in the {@link Segment} coordinate system.
	 * @return The y coordinate in the {@link Segment} coordinate system.
	 */
	private int getSegmentY() {
		return y >> 3;
	}
	
	/**
	 * Gets the x coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Segment}.
	 * @return The x coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Segment}.
	 */
	private int getSegmentAbsX() {
		return getSegmentX() << 3;
	}
	
	/**
	 * Gets the y coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Segment}.
	 * @return The y coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Segment}.
	 */
	private int getSegmentAbsY() {
		return getSegmentY() << 3;
	}
	
	/**
	 * Gets the x coordinate relative to the base of the {@link Segment}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 * @return The x coordinate relative to the base of the {@link Segment}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 */
	private int getSegmentLocalX() {
		return x - getSegmentAbsX();
	}
	
	/**
	 * Gets the y coordinate relative to the base of the {@link Segment}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 * @return The y coordinate relative to the base of the {@link Segment}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 */
	private int getSegmentLocalY() {
		return y - getSegmentAbsY();
	}
	
	/**
	 * Gets the x coordinate relative to the base of the {@link Region}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 * @return The x coordinate relative to the base of the {@link Region}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 */
	private int getRegionLocalX() {
		return x - getRegionAbsX();
	}
	
	/**
	 * Gets the x coordinate relative to the base of the {@link Region}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 * @return The x coordinate relative to the base of the {@link Region}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 */
	private int getRegionLocalY() {
		return y - getRegionAbsY();
	}
	
	private int getX() {
		return x;
	}
	
	private int getY() {
		return y;
	}
	
	private int getZ() {
		return z;
	}
	
}
