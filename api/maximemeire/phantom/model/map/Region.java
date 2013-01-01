package maximemeire.phantom.model.map;

public class Region {
	
	private int x;
	private int y;
	
	private Segment[][] segments;
	
	private Segment getSegment(int x, int y) {
		int segmentX = x >> 3;
		int segmentY = y >> 3;
		return segments[segmentX - getBaseSegmentX()][segmentY - getBaseSegmentY()];
	}
	
	private int getAbsX() {
		return x << 6;
	}
	
	private int getAbsY() {
		return y << 6;
	}
	
	private int getBaseSegmentX() {
		return x << 3;
	}
	
	private int getBaseSegmentY() {
		return y << 3;
	}

}
