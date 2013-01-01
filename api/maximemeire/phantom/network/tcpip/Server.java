package maximemeire.phantom.network.tcpip;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class Server {
	
	private int port;
	private ChannelPipelineFactory pipelineFactory;
	private ChannelFactory channelFactory;
	private ServerBootstrap bootstrap;
	
	public Server(int port, ChannelPipelineFactory pipelineFactory) {
		this.port = port;
		this.pipelineFactory = pipelineFactory;
	}
	
	public void initiate() {
		try {
			channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), 
					Executors.newCachedThreadPool());
			bootstrap = new ServerBootstrap(channelFactory);
			bootstrap.setPipelineFactory(pipelineFactory);
			bootstrap.setOption("child.tcpNoDelay", true);
			bootstrap.setOption("child.keepAlive", true);
			bootstrap.setOption("child.reuseAddress", true);
			bootstrap.bind(new InetSocketAddress(port));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
