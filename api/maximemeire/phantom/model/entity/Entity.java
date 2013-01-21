package maximemeire.phantom.model.entity;

import maximemeire.phantom.concurrent.Actor;
import maximemeire.phantom.model.map.Location;

public abstract class Entity extends Actor {
	
	protected Location location;
	protected Location clientLoadedBaseLocation;
	
	public Entity(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setClientLoadedBaseLocation() {
		this.clientLoadedBaseLocation = new Location(getClientLoadedBaseAbsX(), getClientLoadedBaseAbsY(), 0);
	}
	
	protected int getClientLoadedBaseAbsX() {
		return (this.location.getSegmentX() - 6) << 3;
	}
	
	protected int getClientLoadedBaseAbsY() {
		return (this.location.getSegmentY() - 6) << 3;
	}
	
	public Location getClientLoadedBase() {
		return this.clientLoadedBaseLocation;
	}

}
