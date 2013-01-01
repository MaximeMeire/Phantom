package maximemeire.phantom.network.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

/**
 * This class is an abstract representation of a parser that parses
 * server configurations. The parser is used in the GlobalServerConfigurationFactory
 * to create a certain configuration.
 * @author Maxime Meire
 *
 */
public abstract class ServerConfigurationParser {
	
	/**
	 * Possible parameters in a configuration file.
	 */
	protected static final String[] PARAMETERS = 
		new String[] { 
		"type", 
		"ip", 
		"port", 
		"blocking", 
		"keepalive", 
		"reuseaddr", 
		"tcpnodelay", 
		"sndbuf",
		"rcvbuf",
		"linger"
		};
	
	/**
	 * The file this configuration is found in.
	 * This could be a converted file from for example an SQL database,
	 * or just some xml or text file.
	 */
	protected File file;
	
	public ServerConfigurationParser(File file) {
		this.file = file;
	}
	
	/**
	 * Parse the file to an intermediate form GlobalServerConfigurationFile.
	 * @return The GlobalServerConfigurationFile.
	 * @throws FileNotFoundException If the file doesn't exist.
	 * @throws DataFormatException If there is an error in the format of the file.
	 */
	public abstract GlobalServerConfigurationFile parse() throws FileNotFoundException, DataFormatException;

}
