package maximemeire.phantom.model.map;

public class Location {

	protected int x;
	protected int y;
	protected int z;
	
	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Gets the x coordinate in the {@link Region} coordinate system.
	 * @return The x coordinate in the {@link Region} coordinate system.
	 */
	public int getRegionX() {
		return x >> 6;
	}
	
	/**
	 * Gets the y coordinate in the {@link Region} coordinate system.
	 * @return The y coordinate in the {@link Region} coordinate system.
	 */
	public int getRegionY() {
		return y >> 6;
	}
	
	/**
	 * Gets the x coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Region}. 
	 * @return The x coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Region}. 
	 */
	public int getRegionAbsX() {
		return getRegionX() << 6;
	}
	
	/**
	 * Gets the y coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Region}. 
	 * @return The y coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Region}. 
	 */
	public int getRegionAbsY() {
		return getRegionY() << 6;
	}
	
	/**
	 * Gets the x coordinate in the {@link Segment} coordinate system.
	 * @return The x coordinate in the {@link Segment} coordinate system.
	 */
	public int getSegmentX() {
		return x >> 3;
	}
	
	/**
	 * Gets the y coordinate in the {@link Segment} coordinate system.
	 * @return The y coordinate in the {@link Segment} coordinate system.
	 */
	public int getSegmentY() {
		return y >> 3;
	}
	
	/**
	 * Gets the x coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Segment}.
	 * @return The x coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Segment}.
	 */
	public int getSegmentAbsX() {
		return getSegmentX() << 3;
	}
	
	/**
	 * Gets the y coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Segment}.
	 * @return The y coordinate in the {@link Tile} coordinate system.
	 * This is the base of the tile's {@link Segment}.
	 */
	public int getSegmentAbsY() {
		return getSegmentY() << 3;
	}
	
	/**
	 * Gets the x coordinate relative to the base of the {@link Segment}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 * @return The x coordinate relative to the base of the {@link Segment}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 */
	public int getSegmentLocalX() {
		return x - getSegmentAbsX();
	}
	
	/**
	 * Gets the y coordinate relative to the base of the {@link Segment}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 * @return The y coordinate relative to the base of the {@link Segment}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 */
	public int getSegmentLocalY() {
		return y - getSegmentAbsY();
	}
	
	/**
	 * Gets the x coordinate relative to the base of the {@link Region}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 * @return The x coordinate relative to the base of the {@link Region}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 */
	public int getRegionLocalX() {
		return x - getRegionAbsX();
	}
	
	/**
	 * Gets the x coordinate relative to the base of the {@link Region}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 * @return The x coordinate relative to the base of the {@link Region}
	 * this {@link Tile} sits in the {@link Tile} coordinate system.
	 */
	public int getRegionLocalY() {
		return y - getRegionAbsY();
	}
	
	public int getRegionBaseSegmentX() {
		return getRegionX() << 3;
	}
	
	public int getRegionBaseSegmentY() {
		return getRegionY() << 3;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public int deltaX(Location loc) {
		return this.x - loc.x;
	}
	
	public int deltaY(Location loc) {
		return this.y - loc.y;
	}
	
	public int deltaZ(Location loc) {
		return this.z - loc.z;
	}
	
	public int absDeltaX(Location loc) {
		return Math.abs(this.x - loc.x);
	}
	
	public int absDeltaY(Location loc) {
		return Math.abs(this.y - loc.y);
	}
	
	public int absDeltaZ(Location loc) {
		return Math.abs(this.z - loc.z);
	}
	
}
