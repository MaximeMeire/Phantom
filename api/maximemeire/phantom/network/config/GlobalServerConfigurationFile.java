package maximemeire.phantom.network.config;

import java.util.LinkedList;

import maximemeire.phantom.util.MappedAttributeList;

/**
 * A general intermediate form to which any kind of configuration file or resource
 * is parsed to, making sure the implementation of interpreting the information
 * of the configuration is not subject to change.
 * @author Maxime Meire
 *
 */
public class GlobalServerConfigurationFile {
	
	/**
	 * The map of lists containing the parameters belonging to its server.
	 */
	private final MappedAttributeList parameters = new MappedAttributeList();
	
	/**
	 * Create a new GlobalServerConfigurationFile based on parsed ServerConfiguration objects.
	 * @param serverConfigurations
	 */
	public GlobalServerConfigurationFile(LinkedList<ServerConfiguration> serverConfigurations) {
		populateConfiguration(serverConfigurations);
	}
	
	/**
	 * Populate the parameters field with the ServerConfiguration objects, sorted
	 * into their appropriate list based on their type.
	 * @param serverConfigurations The list of ServerConfiguration objects.
	 */
	private void populateConfiguration(LinkedList<ServerConfiguration> serverConfigurations) {
		for (ServerConfiguration configuration : serverConfigurations) {
			parameters.put(configuration.getServerType().toString(), configuration);
		}
	}
	
	/**
	 * The map of lists containing the parameters belonging to its server.
	 * @return The parameters list.
	 */
	public final MappedAttributeList getParameters() {
		return parameters;
	}
	
}
