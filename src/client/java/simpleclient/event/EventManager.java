package simpleclient.event;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import simpleclient.Client;
import simpleclient.event.events.AbstractEvent;
import simpleclient.event.listeners.AbstractListener;

public class EventManager {
	
	private final HashMap<Class<AbstractListener>, CopyOnWriteArrayList<AbstractListener>> listeners;
    private HashMap<Class<? extends AbstractListener>, AbstractListener> queuedForRemove;
    private HashMap<Class<? extends AbstractListener>, AbstractListener> queuedForAdd;
	
	public EventManager() {
		listeners = new HashMap<>();
        queuedForRemove = new HashMap<>();
        queuedForAdd = new HashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractListener> void AddListener(Class<T> object, AbstractListener listener) {
		try {
			CopyOnWriteArrayList<AbstractListener> listOfListeners = listeners.get(object);
			if(listOfListeners == null)
			{
				listOfListeners = new CopyOnWriteArrayList<>(Collections.singletonList(listener));
				listeners.put((Class<AbstractListener>) object, listOfListeners);
			}
			listOfListeners.add(listener);
		}catch(Exception e) {
			System.out.println("Issue adding listener: " + object.getTypeName() + "...");
			e.printStackTrace();
		}
	}
	
	public <T extends AbstractListener> void RemoveListener(Class<T> object, AbstractListener listener) {
		try {
			CopyOnWriteArrayList<AbstractListener> listOfListeners = listeners.get(object);
			if(listOfListeners != null)
			{
				listOfListeners.remove(listener);
			}
		}catch(Exception e) {
			System.out.println("Issue removing listener: " + object.getTypeName() + "...");
			e.printStackTrace();
		}
	}

    public <T extends AbstractListener> void QueueForRemove(Class<T> object, AbstractListener listener) {
        Client.INSTANCE.logger.info("{} queued removal for listener {}", object.getName(), listener.getClass().getName());
        queuedForRemove.put(object, listener);
    }

    public <T extends AbstractListener> void QueueForAdd(Class<T> object, AbstractListener listener) {
        Client.INSTANCE.logger.info("{} queued add for listener {}", object.getName(), listener.getClass().getName());
        queuedForAdd.put(object, listener);
    }

    public HashMap<Class<? extends AbstractListener>, AbstractListener> GetQueuedRemovals() {
        return queuedForRemove;
    }
	
	public void Fire(AbstractEvent event) {
        for (Map.Entry<Class<? extends AbstractListener>, AbstractListener> entry: queuedForRemove.entrySet()) {
            Client.INSTANCE.logger.info("{} queued removing listener {}", entry.getKey().getName(), entry.getValue().getClass().getName());
            RemoveListener(entry.getKey(), entry.getValue());
            queuedForRemove.remove(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Class<? extends AbstractListener>, AbstractListener> entry: queuedForAdd.entrySet()) {
            Client.INSTANCE.logger.info("{} queued adding listener {}", entry.getKey().getName(), entry.getValue().getClass().getName());
            AddListener(entry.getKey(), entry.getValue());
            queuedForAdd.remove(entry.getKey(), entry.getValue());
        }
		CopyOnWriteArrayList<? extends AbstractListener> listOfListeners = listeners.get(event.GetListenerClassType());
		
		if(listOfListeners == null) {
			return;
		}
		event.Fire(listOfListeners);
	}
}
