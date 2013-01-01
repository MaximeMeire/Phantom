package maximemeire.phantom.worldserver.tcpip;

import maximemeire.phantom.util.Logging;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelHandler.Sharable;

@Sharable
public class DownstreamLoggingEncoder extends SimpleChannelDownstreamHandler {
	
	private final static Logger LOGGER = Logging.log();
	
	@Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		LOGGER.info("Written packet to " + ctx.getChannel().getRemoteAddress());
		ctx.sendDownstream(e);
    }

}
