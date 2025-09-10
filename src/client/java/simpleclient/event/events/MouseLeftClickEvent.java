package simpleclient.event.events;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import simpleclient.event.listeners.AbstractListener;
import simpleclient.event.listeners.MouseLeftClickListener;

public class MouseLeftClickEvent extends AbstractEvent{
	
	double mouseX;
	double mouseY;
	
	public MouseLeftClickEvent(double mouseX2, double mouseY2) {
		super();
		this.mouseX = mouseX2;
		this.mouseY = mouseY2;
	}

	public double GetMouseX() {
		return mouseX;
	}
	
	public double GetMouseY() {
		return mouseY;
	}
	
	@Override
	public void Fire(CopyOnWriteArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : listeners) {
			MouseLeftClickListener mouseLeftClickListener = (MouseLeftClickListener) listener;
			mouseLeftClickListener.OnMouseLeftClick(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<MouseLeftClickListener> GetListenerClassType() {
		return MouseLeftClickListener.class;
	}
}