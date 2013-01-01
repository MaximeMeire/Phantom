package maximemeire.phantom.network.config;

/**
 * This enum represents a type for a server.
 * @author Maxime Meire
 *
 */
public enum ServerType {
	ROOT("root"),
	WORLD("world");
	
	/**
	 * The type in string form.
	 */
	private String type;
	/**
	 * The port offset to retrieve the id belonging to a server.
	 */
	private int idOffset;
	
	/**
	 * Create a new ServerType instance.
	 * @param type The type in string form.
	 */
	private ServerType(String type) {
		this.type = type;
		this.idOffset = setIdOffset(type); 
	}
	
	/**
	 * Get a ServerType instance for the given string.
	 * @param type The type in string form.
	 * @return The ServerType instance.
	 * @throws Exception If a non-existing type was requested.
	 */
	public static ServerType getServerType(String type) throws Exception {
		if (type.equalsIgnoreCase("root"))
			return ServerType.ROOT;
		else if (type.equalsIgnoreCase("world"))
			return ServerType.WORLD;
		else
			throw new Exception("Invalid ServerType requested!");
	}
	
	/**
	 * Sets the id offset.
	 * @param type The type in string form.
	 * @return The offset.
	 */
	private int setIdOffset(String type) {
		if (type.equalsIgnoreCase("root"))
			return 50000;
		else if (type.equalsIgnoreCase("world"))
			return 40000;
		else
			return 0;
	}
	
	/**
	 * Get the port offset to retrieve the id belonging to a server.
	 * @return
	 */
	public int getIdOffset() {
		return idOffset;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
