package maximemeire.phantom.model.entity;

public class MovementNode {
	
	protected int first = -1;
	protected int second = -1;
	
	public MovementNode(int first, int second) {
		this.first = first;
		this.second = second;
	}
	
	public boolean notMoving() {
		return first == -1 && second == -1;
	}
	
	public boolean walking() {
		return first != -1 && second == -1;
	}
	
	public boolean running() {
		return !notMoving() && !walking();
	}
	
	public int getFirst() {
		return first;
	}
	
	public int getSecond() {
		return second;
	}

}
