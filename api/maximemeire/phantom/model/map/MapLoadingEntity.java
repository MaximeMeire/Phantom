package maximemeire.phantom.model.map;

import java.util.HashSet;

public class MapLoadingEntity {
	
	public final Location location;
	public final HashSet<Integer> regions;
	
	public MapLoadingEntity(Location location) {
		this.location = location;
		this.regions = new HashSet<Integer>();
	}

}
