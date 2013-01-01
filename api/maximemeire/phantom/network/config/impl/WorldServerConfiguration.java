package maximemeire.phantom.network.config.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.zip.DataFormatException;

import maximemeire.phantom.network.config.GlobalServerConfiguration;
import maximemeire.phantom.network.config.GlobalServerConfigurationFile;
import maximemeire.phantom.network.config.ServerConfiguration;
import maximemeire.phantom.network.config.ServerConfigurationParser;

/**
 * The implementation of configuration of the bindings belonging to this GlobalServerConfiguration.
 * @author Maxime Meire
 *
 */
public class WorldServerConfiguration extends GlobalServerConfiguration {

	/**
	 * Create a new WorldServerConfiguration instance.
	 * @param parser The parser used to parse the configuration file.
	 * @param id The id of the server.
	 */
	public WorldServerConfiguration(ServerConfigurationParser parser, int id) {
		super(parser, id);
		clientBindings = new LinkedList<ServerConfiguration>();
	}
	
	@Override
	public void configureBindings(GlobalServerConfigurationFile file) throws DataFormatException {
		addRoot(file);
		addRootWorld(file);
		addSisterWorlds(file);
	}

	@Override
	protected void addRoot(GlobalServerConfigurationFile file) throws DataFormatException {
		clientBindings.add(getRoot(file));	
	}
	
	/**
	 * Defines the root binding belonging to this configuration.
	 * @param file The GlobalServerConfigurationFile which represents the interface to the parsed data between
	 * the implementation of the parsers and the implementation of how the parsed information should be
	 * interpreted.
	 * @throws DataFormatException If the file to be parsed is not found.
	 */
	private void addRootWorld(GlobalServerConfigurationFile file) throws DataFormatException {
		ServerConfiguration configuration = getWorld(file);
		serverBindings.add(configuration);
		removeWorld(file, id);
	}
	
	/**
	 * Defines the bindings with fellow WorldServer sisters.
	 * @param file The GlobalServerConfigurationFile which represents the interface to the parsed data between
	 * the implementation of the parsers and the implementation of how the parsed information should be
	 * interpreted.
	 * @throws DataFormatException If the file to be parsed is not found.
	 */
	private void addSisterWorlds(GlobalServerConfigurationFile file) throws DataFormatException {
		LinkedList<ServerConfiguration> worlds = getWorlds(file);
		Iterator<ServerConfiguration> it = worlds.iterator();
		ServerConfiguration world;
		while (it.hasNext()) {
			world = it.next();
			clientBindings.add(world);
			it.remove();
		}
	}

}
