package simpleclient.event.listeners;

import simpleclient.event.events.RenderEvent;

public interface RenderListener extends AbstractListener {
	public abstract void OnRender(RenderEvent event);
}