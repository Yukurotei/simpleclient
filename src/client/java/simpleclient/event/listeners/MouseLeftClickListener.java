package simpleclient.event.listeners;

import simpleclient.event.events.MouseLeftClickEvent;

public interface MouseLeftClickListener extends AbstractListener {
	public abstract void OnMouseLeftClick(MouseLeftClickEvent event);
}