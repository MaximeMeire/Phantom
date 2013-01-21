package maximemeire.phantom.concurrent;

public class Link {
	
	protected Actor sender;
	protected Actor receiver;
	
	public Link(Actor sender, Actor receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}

}
