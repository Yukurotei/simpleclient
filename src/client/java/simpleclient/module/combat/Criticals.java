package simpleclient.module.combat;

//import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
//import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
//import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import simpleclient.Client;
import simpleclient.event.events.SendPacketEvent;
import simpleclient.event.listeners.SendPacketListener;
import simpleclient.module.Mod;

public class Criticals extends Mod implements SendPacketListener {
	
	//private HandSwingC2SPacket swingPacket;
	//private PlayerInteractEntityC2SPacket attackPacket;
	//private int sendTimer;
	
	public Criticals() {
		super("Criticals[WIP]", "Makes all your hits criticals", Category.COMBAT);
	}
	
	
	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(SendPacketListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.getInstance().eventManager.RemoveListener(SendPacketListener.class, this);
		Client.getInstance().eventManager.RemoveListener(SendPacketListener.class, this);
		super.onDisable();
	}
	
	@Override
	public void onTick() {
		
	}
	
	@Override
	public void OnSendPacket(SendPacketEvent event) {
		
	}
	
	//private void sendPacket(double height) {
		
	//}
	
	//private boolean skipCrit() {
		//return !mc.player.isOnGround() || mc.player.isSubmergedInWater() || mc.player.isInLava() || mc.player.isClimbing();
 	//}

}
