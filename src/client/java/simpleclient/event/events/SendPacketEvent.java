package simpleclient.event.events;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.network.packet.Packet;
import simpleclient.event.listeners.AbstractListener;
import simpleclient.event.listeners.SendPacketListener;

public class SendPacketEvent extends AbstractEvent {
	
	private Packet<?> packet;
	
	public SendPacketEvent(Packet<?> packet) {
		this.packet = packet;
	}
	
	public Packet<?> GetPacket() {
		return packet;
	}
	
	@Override
	public void Fire(CopyOnWriteArrayList<? extends AbstractListener> listeners) {
		for (AbstractListener listener : listeners) {
			SendPacketListener sendPacketListener = (SendPacketListener) listener;
			sendPacketListener.OnSendPacket(this);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<SendPacketListener> GetListenerClassType() {
		return SendPacketListener.class;
	}

}
