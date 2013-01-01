package maximemeire.phantom.network.config;

import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

/**
 * A factory class to create the appropriate GlobalServerConfiguration setup
 * to use in setting up a server.
 * @author Maxime
 *
 */
public class GlobalServerConfigurationFactory {
	
	/**
	 * Based on the given wanted implementation this factory will create and setup
	 * a GlobalServerConfiguration used in a server to be setup.
	 * @param configuration The wanted implementation of the GlobalServerConfiguration object.
	 * @return The GlobalServerConfiguration object.
	 * @throws FileNotFoundException If the file to be parsed is not found.
	 * @throws DataFormatException If the file to be parsed has an incorrect format.
	 */
	public static final GlobalServerConfiguration createConfiguration(GlobalServerConfiguration configuration) throws FileNotFoundException, DataFormatException {
		return configuration.configure();
	}

}
