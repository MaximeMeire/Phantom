package maximemeire.phantom.worldserver.tcpip;

import org.jboss.netty.channel.ChannelHandler.Sharable;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;

@Sharable
public class DownstreamLoggingEncoder extends SimpleChannelDownstreamHandler {
	
//	private final static Logger LOGGER = Logging.log();
	
	@Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//		LOGGER.info("Written packet to " + ctx.getChannel().getRemoteAddress());
		ctx.sendDownstream(e);
    }

}
