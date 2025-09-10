package simpleclient.event.events;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import simpleclient.event.listeners.AbstractListener;
import simpleclient.event.listeners.MouseScrollListener;

public class MouseScrollEvent extends AbstractEvent {
	private float horizontal;
	private float vertical;
	
	public MouseScrollEvent(float horizontal, float vertical) {
		super();
		this.horizontal = horizontal;
		this.vertical = vertical;
	}
	
	public float GetVertical() {
		return vertical;
	}
	
	public float GetHorizontal() {
		return horizontal;
	}

	@Override
	public void Fire(CopyOnWriteArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : listeners) {
			MouseScrollListener mouseScrollListener = (MouseScrollListener) listener;
			mouseScrollListener.OnMouseScroll(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<MouseScrollListener> GetListenerClassType() {
		return MouseScrollListener.class;
	}
}