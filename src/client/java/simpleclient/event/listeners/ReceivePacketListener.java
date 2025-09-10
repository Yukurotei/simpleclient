package simpleclient.event.listeners;

import simpleclient.event.events.ReceivePacketEvent;

public interface ReceivePacketListener extends AbstractListener {
	public abstract void OnReceivePacket(ReceivePacketEvent readPacketEvent);
}