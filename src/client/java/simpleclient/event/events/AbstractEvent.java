package simpleclient.event.events;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import simpleclient.event.listeners.AbstractListener;

public abstract class AbstractEvent {
	boolean isCancelled;
	
	public AbstractEvent() {
		isCancelled = false;
	}
	
	public boolean IsCancelled() {
		return isCancelled;
	}
	
	public void SetCancelled(boolean state) {
		this.isCancelled = state;
	}
	
	public abstract void Fire(CopyOnWriteArrayList<? extends AbstractListener> listeners);
	public abstract <T extends AbstractListener> Class<T> GetListenerClassType();
}