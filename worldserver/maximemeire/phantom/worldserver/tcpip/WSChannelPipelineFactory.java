package maximemeire.phantom.worldserver.tcpip;

import maximemeire.phantom.concurrent.ActorUniverse;
import maximemeire.phantom.worldserver.tcpip.handler.ConnectionStateHandler;
import maximemeire.phantom.worldserver.tcpip.handler.UpstreamDispatcher;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class WSChannelPipelineFactory implements
		ChannelPipelineFactory {
	
	private ActorUniverse worldUniverse;
	
	public WSChannelPipelineFactory(ActorUniverse worldUniverse) {
		this.worldUniverse = worldUniverse;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("PLP_UPSTREAM_DISPATCHER", new UpstreamDispatcher());
		pipeline.addLast("DOWNSTREAM_LOGGER", new DownstreamLoggingEncoder());
		pipeline.addLast("CONNECTION_HANDLER", new ConnectionStateHandler(worldUniverse));
		return pipeline;
	}

}
