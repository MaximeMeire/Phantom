package maximemeire.phantom.worldserver.tcpip.handlers;

import maximemeire.phantom.concurrent.ActorUniverse;
import maximemeire.phantom.model.entity.player.Player;
import maximemeire.phantom.model.entity.player.PlayerMessageHandler;
import maximemeire.phantom.util.Logging;
import maximemeire.phantom.worldserver.tcpip.codec.r317.PLPCodec317;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.ChannelHandler.Sharable;

@Sharable
public class ConnectionStateHandler extends SimpleChannelHandler {
	
	private static ActorUniverse worldUniverse;
	
	public ConnectionStateHandler(ActorUniverse worldUniverse) {
		ConnectionStateHandler.worldUniverse = worldUniverse;
	}
	
	private final static Logger LOGGER = Logging.log();

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		LOGGER.info("Channel opened " + ctx.getChannel().getRemoteAddress().toString());
	}
	
	@Override
	public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e) {
		LOGGER.info("Channel bound " + ctx.getChannel().getRemoteAddress().toString());
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		LOGGER.info("Channel connected for " + ctx.getChannel().getRemoteAddress().toString());
		Player player = new Player(ctx.getChannel(), PLPCodec317.getCodec());
		worldUniverse.setupActor(player, new PlayerMessageHandler<Player>(player));
		ctx.getChannel().setAttachment(player);
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		LOGGER.info("Channel disconnected");
	}
	
	@Override
	public void channelUnbound(ChannelHandlerContext ctx, ChannelStateEvent e) {
		LOGGER.info("Channel unbound");
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		LOGGER.info("Channel closed");
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		LOGGER.info("Message received");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)  {
		LOGGER.info("Exception caught " + e.getCause().getMessage());
	}
}
