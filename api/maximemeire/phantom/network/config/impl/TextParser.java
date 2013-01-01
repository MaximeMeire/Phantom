package maximemeire.phantom.network.config.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import maximemeire.phantom.network.config.GlobalServerConfigurationFile;
import maximemeire.phantom.network.config.ServerConfiguration;
import maximemeire.phantom.network.config.ServerConfigurationParser;
import maximemeire.phantom.util.AttributeMap;

/**
 * The ServerConfigurationParser implementation to parse text based configuration files.
 * There is an example in data/configuration/globalserverconfiguration.txt
 * @author Maxime Meire
 *
 */
public class TextParser extends ServerConfigurationParser {
	
	/**
	 * Tokens at the beginning of a line that indicate to skip the line.
	 */
	private static final String[] LINE_SKIPS = new String[] { "#", "" };
	/**
	 * Tokens randomly spread in the file to be skipped.
	 */
	private static final String[] TOKEN_SKIPS = new String[] { "=" };

	/**
	 * Creates a new TextParser instance.
	 * @param file The file to be parsed.
	 */
	public TextParser(File file) {
		super(file);
	}
	
	@Override
	public GlobalServerConfigurationFile parse() throws FileNotFoundException, DataFormatException {
		Scanner scanner = new Scanner(new FileReader(file));
		LinkedList<ServerConfiguration> configurations = new LinkedList<ServerConfiguration>();
		LinkedList<AttributeMap> attributes = parseServers(scanner, skip(scanner));
		for (AttributeMap attribute : attributes) {
			ServerConfiguration configuration = null;
			try {
				configuration = new ServerConfiguration(attribute);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			configurations.add(configuration);
		}
		scanner.close();
		return new GlobalServerConfigurationFile(configurations);
	}
	
	/**
	 * Parse the defined servers in the file.
	 * @param scanner The Scanner to read the file.
	 * @param token The current token.
	 * @return A list of mapped parameters.
	 * @throws DataFormatException If the file to be parsed is not found.
	 */
	private LinkedList<AttributeMap> parseServers(Scanner scanner, String token) throws DataFormatException {
		LinkedList<AttributeMap> attributes = new LinkedList<AttributeMap>();
		while (scanner.hasNext()) {
			AttributeMap list = parseAttributes(scanner, token);
			if (!list.isEmpty())
				attributes.add(list);
		}
		return attributes;
	}
	
	/**
	 * Parse the defined parameters in the file.
	 * @param scanner The Scanner to read the file.
	 * @param token The current token.
	 * @return A map of parameters.
	 */
	private AttributeMap parseAttributes(Scanner scanner, String token) {
		AttributeMap attributes = new AttributeMap();
		do {				
			for (String parameter : PARAMETERS) {
				if (token == null)
					break;
				String value = "";
				if (token.equalsIgnoreCase(parameter)) {
					value = skip(scanner);
					attributes.put(parameter, value);
					token = skip(scanner);
				}
			}				
		} while (token != null && !token.equalsIgnoreCase("type"));
		return attributes;
	}
	
	/**
	 * Checks whether a certain token can be skipped.
	 * @param token The token to check.
	 * @return Whether the token can be skipped.
	 */
	private boolean skipToken(String token) {
		for (String s : TOKEN_SKIPS)
			if (s.equalsIgnoreCase(token)) 
				return true;
		return false;
	}
	
	/**
	 * Checks whether a certain line can be skipped.
	 * @param token The token somewhere in the line.
	 * @return Whether the remainer of the line can be skipped.
	 */
	private boolean skipLine(String token) {
		for (String s : LINE_SKIPS)
			if (s.equalsIgnoreCase(token)) 
				return true;
		return false;
	}
	
	/**
	 * Skips a token or line if it is to be skipped.
	 * @param scanner The Scanner to read the file.
	 * @param token The token the scanner 
	 * @return Whether the token or line was skipped.
	 */
	private boolean skip(Scanner scanner, String token) {
		if (skipToken(token)) 
			return true;
		if (skipLine(token)) {
			scanner.nextLine();
			return true;
		}
		return false;
	}
	
	/**
	 * Skips tokens and lines until a token is found not to be skipped
	 * and returns the token not to be skipped.
	 * @param scanner The Scanner to read the file.
	 * @return The token not to be skipped.
	 */
	private String skip(Scanner scanner) {
		String token;
		while (scanner.hasNext()) {
			if (!skip(scanner, token = scanner.next()))
				return token;
		}
		return null;
	}

}
