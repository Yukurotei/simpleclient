package simpleclient.event.events;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.network.packet.Packet;
import simpleclient.event.listeners.AbstractListener;
import simpleclient.event.listeners.ReceivePacketListener;

public class ReceivePacketEvent extends AbstractEvent {

	private Packet<?> packet;
	
	public Packet<?> GetPacket(){
		return packet;
	}
	
	public ReceivePacketEvent(Packet<?> packet) {
		this.packet = packet;
	}
	
	@Override
	public void Fire(CopyOnWriteArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : listeners) {
			ReceivePacketListener readPacketListener = (ReceivePacketListener) listener;
			readPacketListener.OnReceivePacket(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<ReceivePacketListener> GetListenerClassType() {
		return ReceivePacketListener.class;
	}
}