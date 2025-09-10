package simpleclient.event.events;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import simpleclient.event.listeners.AbstractListener;
import simpleclient.event.listeners.MouseMoveListener;

public class MouseMoveEvent extends AbstractEvent{
	private double horizontal;
	private double vertical;
	
	public MouseMoveEvent(double x, double y) {
		super();
		this.horizontal = x;
		this.vertical = y;
	}
	
	public double GetVertical() {
		return vertical;
	}
	
	public double GetHorizontal() {
		return horizontal;
	}

	@Override
	public void Fire(CopyOnWriteArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : listeners) {
			MouseMoveListener mouseMoveListener = (MouseMoveListener) listener;
			mouseMoveListener.OnMouseMove(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<MouseMoveListener> GetListenerClassType() {
		return MouseMoveListener.class;
	}
}