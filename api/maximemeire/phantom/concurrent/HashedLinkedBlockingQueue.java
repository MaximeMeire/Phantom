package maximemeire.phantom.concurrent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HashedLinkedBlockingQueue<V> extends Thread {
	
	boolean active = false;
	
	private Lock lock = new ReentrantLock();
	private Map<Integer, V> uniquenessMap;
	private BlockingQueue<V> queue = new LinkedBlockingQueue<V>();
	private BlockingQueue<V> pendingElements = new LinkedBlockingQueue<V>();
	
	public HashedLinkedBlockingQueue(int initialCapacity, float loadFactor) {
		this.uniquenessMap = new HashMap<Integer, V>(initialCapacity, loadFactor);
		this.active = true;
	}
	
	@Override
	public void run() {
		while (active) {
			try {
				if (!pendingElements.isEmpty()) {
					lock.lock();
					Iterator<V> it = pendingElements.iterator();
					V value;
					while (it.hasNext()) {
						value = it.next();
						V v = uniquenessMap.put(value.hashCode(), value);
						if (v == null) 
							queue.offer(value);
						it.remove();
					}
				}
			} finally {
				lock.unlock();
				try {
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public V take() throws InterruptedException {
		lock.lock();
		try {
			V value = queue.take();
			uniquenessMap.remove(value.hashCode());
			return value;
		} finally {
			lock.unlock();
		}
	}
	
	public void offer(V value) {
		pendingElements.offer(value);
	}
	
	public BlockingQueue<V> shutDown() throws InterruptedException {
		active = false;
		this.join();
		uniquenessMap = null;
		for (V value : pendingElements) {
			queue.put(value);
		}
		pendingElements = null;
		return queue;
	}

}
