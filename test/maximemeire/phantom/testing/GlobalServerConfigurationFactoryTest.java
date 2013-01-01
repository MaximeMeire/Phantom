package maximemeire.phantom.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import maximemeire.phantom.network.config.GlobalServerConfiguration;
import maximemeire.phantom.network.config.GlobalServerConfigurationFactory;
import maximemeire.phantom.network.config.impl.RootServerConfiguration;
import maximemeire.phantom.network.config.impl.TextParser;
import maximemeire.phantom.network.config.impl.WorldServerConfiguration;

public class GlobalServerConfigurationFactoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("data/globalserverconfiguration.txt");
		try {
			GlobalServerConfiguration rootConf = GlobalServerConfigurationFactory.createConfiguration(new RootServerConfiguration(new TextParser(file)));
			GlobalServerConfiguration worldConf1 = GlobalServerConfigurationFactory.createConfiguration(new WorldServerConfiguration(new TextParser(file), 1));
			GlobalServerConfiguration worldConf2 = GlobalServerConfigurationFactory.createConfiguration(new WorldServerConfiguration(new TextParser(file), 2));
			rootConf.toString();
			worldConf1.toString();
			worldConf2.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
