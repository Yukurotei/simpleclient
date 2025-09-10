package simpleclient.event.listeners;

import simpleclient.event.events.TickEvent;

public interface TickListener extends AbstractListener {
	public abstract void OnUpdate(TickEvent event);
}