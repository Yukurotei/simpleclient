package simpleclient.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import simpleclient.misc.FakePlayerEntity;
import simpleclient.module.ModuleManager;
import simpleclient.module.movement.Flight;
import simpleclient.module.movement.Freecam;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	@Shadow
	private ClientPlayNetworkHandler networkHandler;

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(at = { @At("HEAD") }, method = "tick()V", cancellable = true)
	private void onPlayerTick(CallbackInfo ci) {
		Freecam camModule = (Freecam) ModuleManager.INSTANCE.getModule("Freecam");
		if (camModule.isEnabled()) {
			FakePlayerEntity fakePlayer = camModule.getFakePlayer();
			if(fakePlayer != null) {
				this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(fakePlayer.getX(), fakePlayer.getY(),
						fakePlayer.getZ(), fakePlayer.isOnGround()));
				this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(fakePlayer.getYaw(),
						fakePlayer.getPitch(), fakePlayer.isOnGround()));
			}
		}
	}
	
	@Inject(at = {@At("HEAD") }, method="sendMovementPackets()V", cancellable = true)
	private void sendMovementPackets(CallbackInfo ci) {
		Freecam camModule = (Freecam) ModuleManager.INSTANCE.getModule("Freecam");
		if (camModule.isEnabled()) {
			ci.cancel();
		}
	}
	
	@Override
	public boolean isSpectator() {
		return super.isSpectator() || ((Freecam) ModuleManager.INSTANCE.getModule("Freecam")).isEnabled();
	}
	
	@Override
	protected float getOffGroundSpeed()
	{
		float speed = super.getOffGroundSpeed();
		Flight flyModule = (Flight) ModuleManager.INSTANCE.getModule("Flight");
		if (flyModule == null) {
			Freecam freecamModule = (Freecam) ModuleManager.INSTANCE.getModule("Freecam");
			if (freecamModule == null) return speed;
			if (freecamModule.isEnabled()) {
				return (float)freecamModule.getSpeed();
			}
			return speed;
		};
		if(flyModule.isEnabled()) {
			return (float)flyModule.getSpeed();
		}
		
		Freecam camModule = (Freecam) ModuleManager.INSTANCE.getModule("Freecam");
		if(camModule.isEnabled()) {
			return (float)camModule.getSpeed();
		}
		return speed;
	}

}