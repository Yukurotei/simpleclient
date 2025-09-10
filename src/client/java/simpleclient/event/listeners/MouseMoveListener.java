package simpleclient.event.listeners;

import simpleclient.event.events.MouseMoveEvent;

public interface MouseMoveListener extends AbstractListener {
	public abstract void OnMouseMove(MouseMoveEvent mouseMoveEvent);
}