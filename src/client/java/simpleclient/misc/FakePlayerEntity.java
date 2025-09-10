package simpleclient.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import simpleclient.Client;

public class FakePlayerEntity extends AbstractClientPlayerEntity {

	public FakePlayerEntity() {
		super(Client.mc.world, Client.mc.player.getGameProfile());
		ClientPlayerEntity player = Client.mc.player;
		this.setPos(player.getPos().x, player.getPos().y, player.getPos().z);
		this.setRotation(player.getYaw(MinecraftClient.getInstance().getTickDelta()),
				player.getPitch(MinecraftClient.getInstance().getTickDelta()));
		//this.inventory = player.getInventory();
	}

	public void despawn() {
		this.remove(RemovalReason.DISCARDED);
	}
}