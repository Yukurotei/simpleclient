package simpleclient.event.events;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import simpleclient.event.listeners.AbstractListener;
import simpleclient.event.listeners.TickListener;

public class TickEvent extends AbstractEvent {
	@Override
	public void Fire(CopyOnWriteArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : listeners) {
			TickListener tickListener = (TickListener) listener;
			tickListener.OnUpdate(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<TickListener> GetListenerClassType() {
		return TickListener.class;
	}
}