package maximemeire.phantom.network.config.impl;

import java.util.zip.DataFormatException;

import maximemeire.phantom.network.config.GlobalServerConfiguration;
import maximemeire.phantom.network.config.GlobalServerConfigurationFile;
import maximemeire.phantom.network.config.ServerConfigurationParser;

/**
 * The implementation of configuration of the bindings belonging to this GlobalServerConfiguration.
 * @author Maxime
 *
 */
public class RootServerConfiguration extends GlobalServerConfiguration {

	/**
	 * Creates a new RootServerConfiguration instance.
	 * @param parser The parser used to parse the configuration file.
	 */
	public RootServerConfiguration(ServerConfigurationParser parser) {
		super(parser, 1);
	}

	@Override
	public void configureBindings(GlobalServerConfigurationFile file) throws DataFormatException {
		addRoot(file);
	}
	
	@Override
	protected void addRoot(GlobalServerConfigurationFile file) throws DataFormatException {
		serverBindings.add(getRoot(file));
	}

}
