package maximemeire.phantom.network.config;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.zip.DataFormatException;

import maximemeire.phantom.network.config.ServerConfiguration;
import maximemeire.phantom.util.MappedAttributeList;

/**
 * This class represents a collection of data used to setup a server with.
 * It contains information about the server setup itself but also possible
 * other servers it needs to connect with acting as a client.
 * @author Maxime Meire
 *
 */
public abstract class GlobalServerConfiguration {
	
	/**
	 * The list of bindings a server needs to make on the local machine.
	 */
	protected LinkedList<ServerConfiguration> serverBindings;
	/**
	 * The list of bindings to an external server.
	 */
	protected LinkedList<ServerConfiguration> clientBindings;
	
	/**
	 * The parser used to parse the configuration file.
	 */
	private ServerConfigurationParser parser;
	/**
	 * The id that belongs to the server.
	 */
	protected int id;
	
	/**
	 * Create a new GlobalServerConfiguration object.
	 * @param parser The ServerConfigurationParser to parse the configuration.
	 * @param id The id that belongs to the server.
	 */
	public GlobalServerConfiguration(ServerConfigurationParser parser, int id) {
		this.parser = parser;
		this.id = id;
		serverBindings = new LinkedList<ServerConfiguration>();
	}
	
	/**
	 * Configures the configuration file by initiating the ServerConfigurationParser
	 * and generate the bindings based on the parsed information.
	 * @return The final GlobalServerConfiguration object.
	 * @throws FileNotFoundException If the file to parse is not found.
	 * @throws DataFormatException If the file contains an incorrect format.
	 */
	public GlobalServerConfiguration configure() throws FileNotFoundException, DataFormatException {
		GlobalServerConfigurationFile configurationFile = parser.parse();
		configureBindings(configurationFile);
		return this;
	}
	
	/**
	 * An abstract class in which is defined how the actual data needs to be interpreted
	 * to generate the bindings.
	 * @param file The GlobalServerConfigurationFile which represents the interface to the parsed data between
	 * the implementation of the parsers and the implementation of how the parsed information should be
	 * interpreted.
	 * @throws DataFormatException
	 */
	protected abstract void configureBindings(GlobalServerConfigurationFile file) throws DataFormatException;
	
	/**
	 * Gets the root server in the configuration. In the current setup there will only
	 * be one root server possible in any configuration.
	 * @param file The GlobalServerConfigurationFile which represents the interface to the parsed data between
	 * the implementation of the parsers and the implementation of how the parsed information should be
	 * interpreted.
	 * @return The ServerConfiguration belonging to a RootServer configuration.
	 * @throws DataFormatException If the file contains an incorrect format.
	 */
	public ServerConfiguration getRoot(GlobalServerConfigurationFile file) throws DataFormatException {
		MappedAttributeList parameters = file.getParameters();
		LinkedList<ServerConfiguration> rootList = parameters.get(ServerType.ROOT.toString());
		if (rootList.size() > 1 || rootList.isEmpty()) 
			throw new DataFormatException("Error in root server configuration format. Either too many (more than 1) or" +
					" none were configured!");
		ServerConfiguration configuration = rootList.getFirst();
		if (configuration != null)
			return configuration;
		else
			throw new DataFormatException("Error in root server configuration format. Root server object is NULL.");
	}
	
	/**
	 * Gets all the ServerConfiguration objects belonging to a WorldServer object. There will be at least one
	 * in every configuration or else a DataFormatException will be thrown.
	 * @param file The GlobalServerConfigurationFile which represents the interface to the parsed data between
	 * the implementation of the parsers and the implementation of how the parsed information should be
	 * interpreted.
	 * @return The ServerConfiguration objects belonging to a WorldServer configuration.
	 * @throws DataFormatException If the file contains an incorrect format.
	 */
	public LinkedList<ServerConfiguration> getWorlds(GlobalServerConfigurationFile file) throws DataFormatException {
		MappedAttributeList parameters = file.getParameters();
		LinkedList<ServerConfiguration> worldList = parameters.get(ServerType.WORLD.toString());
		if (worldList.size() < 1)
			throw new DataFormatException("No world servers were configured. You need to configure at least one!");
		return worldList;
	}
	
	/**
	 * Get the ServerConfiguration belonging to the WorldServer of this GlobalServerConfiguration.
	 * @param file The GlobalServerConfigurationFile which represents the interface to the parsed data between
	 * the implementation of the parsers and the implementation of how the parsed information should be
	 * interpreted.
	 * @return The ServerConfiguration belonging to the WorldServer of this GlobalServerConfiguration.
	 * @throws DataFormatException If the file contains an incorrect format.
	 */
	public ServerConfiguration getWorld(GlobalServerConfigurationFile file) throws DataFormatException {
		return getWorldById(getWorlds(file), this.id);
	}
	
	/**
	 * Get the ServerConfiguration belonging to the WorldServer with the specified id.
	 * @param worlds The ServerConfiguration objects that belong to a WorldServer.
	 * @param id The id of the WorldServer
	 * @return The ServerConfiguration belonging to the WorldServer with the specified id.
	 * @throws DataFormatException If the file contains an incorrect format.
	 */
	private ServerConfiguration getWorldById(LinkedList<ServerConfiguration> worlds, int id) throws DataFormatException {
		for (ServerConfiguration w : worlds) {
			if (w.getPort() - 40000 == id)
				return w;
		}
		throw new DataFormatException("The world with the specified ID was not found! Check your global server configuration file for errors.");
	}
	
	/**
	 * Remove a ServerConfiguration with the specified id from the list of WorldServer ServerConfigurations.
	 * @param file The GlobalServerConfigurationFile which represents the interface to the parsed data between
	 * the implementation of the parsers and the implementation of how the parsed information should be
	 * interpreted.
	 * @param id The id belonging to the ServerConfiguration to remove.
	 * @throws DataFormatException If the file contains an incorrect format.
	 */
	protected void removeWorld(GlobalServerConfigurationFile file, int id) throws DataFormatException {
		LinkedList<ServerConfiguration> worlds = getWorlds(file);
		worlds.remove(getWorldById(worlds, id));
	}
	
	/**
	 * An abstract method which gets the ServerConfiguration belonging to the RootServer
	 * to add it either as a ClientBinding or a ServerBinding.
	 * @param file The GlobalServerConfigurationFile which represents the interface to the parsed data between
	 * the implementation of the parsers and the implementation of how the parsed information should be
	 * interpreted.
	 * @throws DataFormatException If the file contains an incorrect format.
	 */
	protected abstract void addRoot(GlobalServerConfigurationFile file) throws DataFormatException;

}
