package maximemeire.phantom.model.entity;

import maximemeire.phantom.concurrent.ActorUniverse;

public class World {
	
	private final ActorUniverse worldUniverse;
	
	public World() {
		this.worldUniverse = new ActorUniverse("world", Runtime.getRuntime().availableProcessors() * 4);
	}

	public ActorUniverse getWorldUniverse() {
		return worldUniverse;
	}

}
