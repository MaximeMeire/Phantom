package maximemeire.phantom.worldserver;

import maximemeire.phantom.model.entity.World;
import maximemeire.phantom.network.tcpip.Server;
import maximemeire.phantom.util.Logging;
import maximemeire.phantom.worldserver.tcpip.WSChannelPipelineFactory;

public class WorldServer {
	
	private static World world;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Logging.setup("317 Actor RSPS");
		world = new World();
		Server server = new Server(43594, new WSChannelPipelineFactory(world.getWorldUniverse()));
		server.initiate();
	}

}
