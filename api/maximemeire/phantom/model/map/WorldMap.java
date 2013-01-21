package maximemeire.phantom.model.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import maximemeire.phantom.concurrent.Actor;
import maximemeire.phantom.model.entity.Entity;
import maximemeire.phantom.model.map.handler.RegionMessageDispatcher;
import maximemeire.phantom.model.map.message.FinishRegistrationMessage;
import maximemeire.phantom.model.map.message.LoadRegionMessage;
import maximemeire.phantom.model.map.message.RegionLoadedMessage;
import maximemeire.phantom.model.map.message.RegisterEntityMessage;
import maximemeire.phantom.util.Logging;

import org.apache.log4j.Logger;

public class WorldMap extends Actor {
	
	private final static Logger LOGGER = Logging.log();
	
	private static int counter = 0;
	
	/**
	 * This map contains the {@link Region} objects that are finished loaded. Loading
	 * {@link Region} objects is done on demand. Key values are build in this
	 * format: (x << 8) | y
	 */
	private final Map<Integer, Region> loadedRegions = new HashMap<Integer, Region>();
	
	private final Map<Integer, Segment> loadedSegments = new HashMap<Integer, Segment>();
	/**
	 * This map contains the {@link Region} objects that are still loading. Loading
	 * {@link Region} objects is done on demand. Key values are build in this
	 * format: (x << 8) | y
	 */
	private final Map<Integer, Region> loadingRegions = new HashMap<Integer, Region>();
	/**
	 * This map contains entities that are queued to be registered but need to wait for one
	 * or more regions to finish loading.
	 */	
	private final Map<Entity, MapLoadingEntity> loadingViewports = new HashMap<Entity, MapLoadingEntity>();
	
	public void registerEntity(RegisterEntityMessage<WorldMap> message) {
		try {
			Location location = message.location;
			HashSet<Integer> keys = getViewportRegionKeys(location);
			boolean ready = true;
			for (int regionKey : keys) {
				int regionX = regionKey >> 8;
				int regionY = regionKey & 0xff;
				if (!loadedRegions.containsKey(regionKey) && !loadingRegions.containsKey(regionKey)) {
					LOGGER.info("Loading and queue'ing entity registration for region [" + regionX + ", " + regionY + "]");
					ready = false;
					storeLoadingRegionKey(message, regionKey);
					Region region = new Region(this, regionX, regionY);
					universe.setupActor(region, new RegionMessageDispatcher<Region>(region));
					loadingRegions.put(regionKey, region);
					region.sendMessage(new LoadRegionMessage<Region>(getLink(region)));
				} else if (!loadedRegions.containsKey(regionKey) && loadingRegions.containsKey(regionKey)) {
					LOGGER.info("Queue'ing entity registration for region [" + regionX + ", " + regionY + "]");
					ready = false;
					storeLoadingRegionKey(message, regionKey);
				} else if (loadedRegions.containsKey(regionKey) && !loadingRegions.containsKey(regionKey)){
					LOGGER.info("Registering entity for region[" + regionX + ", " + regionY + "]");
					Region region = loadedRegions.get(regionKey);
					if (region == null)
						throw new Exception("The region you're trying to add an entity to isn't registered");
				} else {
					throw new Exception("Region [" + regionX + ", " + regionY + "] is on both loading and loaded maps");
				}
			}
			if (ready) {
				final Entity entity = message.getSenderReference();
				register(entity, location);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void storeLoadingRegionKey(RegisterEntityMessage<WorldMap> message, int regionKey) {
		Entity entity = message.getSenderReference();
		if (!loadingViewports.containsKey(entity)) {
			MapLoadingEntity loadingEntity = new MapLoadingEntity(message.location);
			loadingEntity.regions.add(regionKey);
			loadingViewports.put(entity, loadingEntity);
		} else {
			MapLoadingEntity loadingEntity = loadingViewports.get(entity);
			loadingEntity.regions.add(regionKey);
		}
	}
	
	public void regionRegistered(RegionLoadedMessage<WorldMap> message) {
		try {
			Region region = message.getSenderReference();
			/*
			 * This can only be done because the data accessed are
			 * final primitives. It is bad practice to pass information
			 * like this unless you're on top of your game and know
			 * which data is read-only and only access this data.
			 */
			int regionKey = region.regionKey;
			if (loadingRegions.containsKey(regionKey) && !loadedRegions.containsKey(regionKey)) {
				if (region != loadingRegions.remove(regionKey)) {
					throw new Exception("Error in loading regions [" + region.x + "," + region.y + "]");
				}
				unqueueLoadingEntities(regionKey);
				loadedRegions.put(regionKey, region);
				addLoadedSegments(region.segments);
				LOGGER.info("Finished loading region [" + region.x + ", " + region.y + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void unqueueLoadingEntities(int regionKey) throws Exception {
		Set<Entity> keySet = loadingViewports.keySet();
		Iterator<Entity> it = keySet.iterator();
		Entity entity;
		while (it.hasNext()) {
			entity = it.next();
			MapLoadingEntity loadingEntity = loadingViewports.get(entity);
			loadingEntity.regions.remove(regionKey);
			if (loadingEntity.regions.isEmpty()) {
				register(entity, loadingEntity.location);
				it.remove();
			}
		}
	}
	
	private void register(Entity entity, Location location) {
		int segmentKey = (location.getSegmentX() << 16) | location.getSegmentY();
		Segment segment = loadedSegments.get(segmentKey);
		segment.sendMessage(new FinishRegistrationMessage<Segment>(entity, location, getLink(segment)));
	}
	
	private void addLoadedSegments(Segment[][] segments) {
		for (int y = 0; y < segments.length; y++) {
			for (int x = 0; x < segments[y].length; x++) {
				Segment segment = segments[x][y];
				int segmentKey = (segment.x << 16) | segment.y;
				loadedSegments.put(segmentKey, segment);
			}
		}
	}
	
	private HashSet<Integer> getViewportRegionKeys(Location location) {
		HashSet<Integer> keys = new HashSet<Integer>();
		int regionX = location.getRegionX();
		int regionY = location.getRegionY();
		int regionKey = (regionX << 8) | regionY;
		keys.add(regionKey);
		int baseSegmentX = location.getRegionBaseSegmentX();
		int baseSegmentY = location.getRegionBaseSegmentY();
		int segmentX = location.getSegmentX();
		int segmentY = location.getSegmentY();
		int offsetX = segmentX - baseSegmentX;
		int offsetY = segmentY - baseSegmentY;
		if (offsetX <= 1) {
			if (offsetY <= 1) {
				keys.add(((regionX - 1) << 8) | regionY);
				keys.add((regionX << 8) | (regionY - 1));
				keys.add(((regionX - 1) << 8) | (regionY - 1));
			} else if (offsetY >= 6) {
				keys.add(((regionX - 1) << 8) | regionY);
				keys.add((regionX << 8) | (regionY + 1));
				keys.add(((regionX - 1) << 8) | (regionY + 1));
			} else {
				keys.add(((regionX - 1) << 8) | regionY);
			}
		} else if (offsetX >= 6) {
			if (offsetY <= 1) {
				keys.add(((regionX + 1) << 8) | regionY);
				keys.add((regionX << 8) | (regionY - 1));
				keys.add(((regionX + 1) << 8) | (regionY - 1));
			} else if (offsetY >= 6) {
				keys.add(((regionX + 1) << 8) | regionY);
				keys.add((regionX << 8) | (regionY + 1));
				keys.add(((regionX + 1) << 8) | (regionY + 1));
			} else {
				keys.add(((regionX + 1) << 8) | regionY);
			}
		} else {
			if (offsetY <= 1) {
				keys.add((regionX << 8) | (regionY - 1));
			} else if (offsetY >= 6) {
				keys.add((regionX << 8) | (regionY + 1));
			}
		}
		return keys;
	}

	@Override
	protected int getActorId() {
		return counter++;
	}	

}
