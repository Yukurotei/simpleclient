package simpleclient.mixins;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.Session;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import simpleclient.Client;
import simpleclient.event.events.MouseLeftClickEvent;
import simpleclient.event.events.TickEvent;
import simpleclient.interfaces.IMinecraftClient;
import simpleclient.module.ModuleManager;
import simpleclient.module.render.EntityESP;
import simpleclient.module.render.ItemESP;
import simpleclient.module.render.PlayerESP;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements IMinecraftClient {
	
	@Shadow
	private int itemUseCooldown;
	@Shadow
	@Final
	private Session session;
	
	@Shadow
	@Final
	private Mouse mouse;
	
	@Shadow
	public ClientWorld world;

	private Session clientSession;
	
	public MinecraftClientMixin(String string) {
		super(string);
	}
	
	@Override
	public void setSession(Session session) {
		clientSession = session;
	}
	
	@Inject(at = {@At("HEAD")}, method = {"getSession()Lnet/minecraft/client/util/Session;"}, cancellable = true)
	private void onGetSession(CallbackInfoReturnable<Session> cir)
	{
		if(clientSession == null) return;
		cir.setReturnValue(clientSession);
	}
	
	@Redirect(at = @At(value = "FIELD",target = "Lnet/minecraft/client/MinecraftClient;session:Lnet/minecraft/client/util/Session;",opcode = Opcodes.GETFIELD,ordinal = 0),method = {"getSessionProperties()Lcom/mojang/authlib/properties/PropertyMap;"})
	private Session getSessionForSessionProperties(MinecraftClient mc)
	{
		if(clientSession != null)return clientSession;
		return session;
	}
	
	@Inject(method = "tick()V", at = @At("TAIL"))
	public void onTick(CallbackInfo ci) {
		Client.INSTANCE.onTick();
		TickEvent updateEvent = new TickEvent();
		Client.getInstance().eventManager.Fire(updateEvent);
	}
	
	@Inject(at = {@At(value="HEAD")}, method = {"close()V"})
	private void onClose(CallbackInfo ci) {
		try {
			Client.getInstance().endClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Inject(at = {@At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;", ordinal = 0)}, method = {"doAttack()Z"}, cancellable = true)
	private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
		double mouseX = Math.ceil(mouse.getX());
		double mouseY = Math.ceil(mouse.getY());
		
		//System.out.println("DOuble Click?");
		MouseLeftClickEvent event = new MouseLeftClickEvent(mouseX, mouseY);
		
		Client.getInstance().eventManager.Fire(event);
		
		if(event.IsCancelled()) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
	
	@Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEntities(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (ModuleManager.INSTANCE.isModuleEnabled("EntityESP") && ((EntityESP) ModuleManager.INSTANCE.getModule("EntityESP")).renderMode.isMode("Glow") && !(entity instanceof PlayerEntity) && !(entity instanceof ItemEntity)) {
        	ci.setReturnValue(true);
        }
        if (ModuleManager.INSTANCE.isModuleEnabled("PlayerESP") && ((PlayerESP) ModuleManager.INSTANCE.getModule("PlayerESP")).renderMode.isMode("Glow") && !(entity instanceof ItemEntity) && entity instanceof PlayerEntity) {
        	ci.setReturnValue(true);
        }
        if (ModuleManager.INSTANCE.isModuleEnabled("ItemESP") && ((ItemESP) ModuleManager.INSTANCE.getModule("ItemESP")).renderMode.isMode("Glow") && !(entity instanceof PlayerEntity) && entity instanceof ItemEntity) {
        	ci.setReturnValue(true);
        }
    }
	
	@Override
	public int getItemUseCooldown()
	{
		return itemUseCooldown;
	}
	
	@Override
	public void setItemUseCooldown(int delay)
	{
		this.itemUseCooldown = delay;
	}
	
}
