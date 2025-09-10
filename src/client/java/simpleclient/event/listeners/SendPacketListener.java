package simpleclient.event.listeners;

import simpleclient.event.events.SendPacketEvent;

public interface SendPacketListener extends AbstractListener{
	public abstract void OnSendPacket(SendPacketEvent event);
}
