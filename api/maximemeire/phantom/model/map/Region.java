package maximemeire.phantom.model.map;

import maximemeire.phantom.concurrent.Actor;
import maximemeire.phantom.model.map.handler.SegmentMessageDispatcher;
import maximemeire.phantom.model.map.message.LoadRegionMessage;
import maximemeire.phantom.model.map.message.LoadSegmentMessage;
import maximemeire.phantom.model.map.message.RegionLoadedMessage;
import maximemeire.phantom.model.map.message.SegmentLoadedMessage;

public class Region extends Actor {
	
	protected WorldMap worldMap;
	protected final int x;
	protected final int y;
	protected final int regionKey;
	
	private boolean loaded = false;
	private int loadedSegments = 0;
	
	protected Segment[][] segments;
	
	public Region(WorldMap worldMap, int x, int y) {
		this.worldMap = worldMap;
		this.x = x;
		this.y = y;
		this.regionKey = (x << 8) | y;
		this.segments = new Segment[8][8];
	}
	
	public void loadRegion(LoadRegionMessage<Region> message) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Segment segment = new Segment(getBaseSegmentX() + x, getBaseSegmentY() + y);
				universe.setupActor(segment, new SegmentMessageDispatcher<Segment>(segment));
				segment.sendMessage(new LoadSegmentMessage<Segment>(getLink(segment)));
			}
		}
	}
	
	public void segmentLoaded(SegmentLoadedMessage<Region> message) {
		if (!loaded) {
			Segment segment = message.getSenderReference();
			/*
			 * This can only be done because segment.x and segment.y are
			 * final primitives. It is bad practice to pass information
			 * like this unless you're on top of your game and know
			 * which data is readonly and only access this data.
			 */
			int segmentOffsetX = segment.x - this.getBaseSegmentX();
			int segmentOffsetY = segment.y - this.getBaseSegmentY();
			this.segments[segmentOffsetX][segmentOffsetY] = segment;
			if (++loadedSegments >= 8) {
				loaded = true;
				worldMap.sendMessage(new RegionLoadedMessage<WorldMap>(getLink(worldMap)));
			}
		} else {
			try {
				throw new Exception("Too many segments were tried to be loaded!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected Segment getSegment(int x, int y) {
		int segmentX = x >> 3;
		int segmentY = y >> 3;
		return segments[segmentX - getBaseSegmentX()][segmentY - getBaseSegmentY()];
	}
	
	protected int getAbsX() {
		return x << 6;
	}
	
	protected int getAbsY() {
		return y << 6;
	}
	
	protected int getBaseSegmentX() {
		return x << 3;
	}
	
	protected int getBaseSegmentY() {
		return y << 3;
	}

	@Override
	protected int getActorId() {
		return (x << 8) | y;
	}

}
