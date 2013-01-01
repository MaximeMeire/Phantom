package maximemeire.phantom.network.config;

import maximemeire.phantom.util.AttributeMap;

/**
 * Represents a collection of settings to setup a server
 * and/or socket.
 * @author Maxime Meire
 *
 */
public class ServerConfiguration {
	
	private final ServerType serverType;
	private final int id;
	private final int port;
	private final String ip;
	private final boolean blocking;
	private final boolean keepAlive;
	private final boolean reuseAddress;
	private final boolean tcpNoDelay;
	private final int sendBuffer;
	private final int receiveBuffer;
	private final int linger;
	
	public ServerConfiguration(AttributeMap attributes) throws Exception {
		this.serverType = ServerType.getServerType((String) attributes.get("type"));
		this.port = Integer.parseInt((String) attributes.get("port"));
		this.id = this.port - this.serverType.getIdOffset();
		this.ip = attributes.get("ip");
		this.blocking = Boolean.parseBoolean((String) attributes.get("blocking"));
		this.keepAlive = Boolean.parseBoolean((String) attributes.get("keepalive"));
		this.reuseAddress = Boolean.parseBoolean((String) attributes.get("reuseaddr"));
		this.tcpNoDelay = Boolean.parseBoolean((String) attributes.get("tcpnodelay"));
		this.sendBuffer = Integer.parseInt((String) attributes.get("sndbuf"));
		this.receiveBuffer = Integer.parseInt((String) attributes.get("rcvbuf"));
		this.linger = Integer.parseInt((String) attributes.get("linger"));
	}

	public final ServerType getServerType() {
		return serverType;
	}

	public final int getPort() {
		return port;
	}

	public final String getIp() {
		return ip;
	}

	public final boolean isBlocking() {
		return blocking;
	}

	public final boolean keepAlive() {
		return keepAlive;
	}

	public final boolean reuseAddress() {
		return reuseAddress;
	}

	public final boolean tcpNoDelay() {
		return tcpNoDelay;
	}

	public final int getSendBuffer() {
		return sendBuffer;
	}

	public final int getReceiveBuffer() {
		return receiveBuffer;
	}

	public final int getLinger() {
		return linger;
	}
	
	public final int getId() {
		return id;
	}
	
}
