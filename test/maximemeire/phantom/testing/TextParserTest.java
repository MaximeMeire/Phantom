package maximemeire.phantom.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import maximemeire.phantom.network.config.impl.TextParser;

public class TextParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		File file = new File("data/globalserverconfiguration.txt");
		TextParser parser = new TextParser(file);
		try {
			parser.parse();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
