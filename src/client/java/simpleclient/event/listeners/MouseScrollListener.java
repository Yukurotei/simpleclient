package simpleclient.event.listeners;

import simpleclient.event.events.MouseScrollEvent;

public interface MouseScrollListener extends AbstractListener {
	public abstract void OnMouseScroll(MouseScrollEvent event);
}