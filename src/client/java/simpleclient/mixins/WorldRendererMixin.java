package simpleclient.mixins;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import simpleclient.Client;
import simpleclient.module.ModuleManager;
import simpleclient.module.render.EntityESP;
import simpleclient.module.render.ItemESP;
import simpleclient.module.render.PlayerESP;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Inject(at = { @At("RETURN") }, method = { "render" })
	private void onRenderWorld(MatrixStack matrixStack, float tickDelta, long limitTime, boolean renderBlockOutline,
			Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f,
			CallbackInfo info) {
		
		// TODO: Per Module rendering.
		//RenderEvent event = new RenderEvent(matrixStack, tickDelta);
		//Client.getInstance().eventManager.Fire(event);
		
		Client.getInstance().moduleManager.render(matrixStack);
	}
	
	@Inject(method="renderEntity", at=@At("HEAD"))
	private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
		if (vertexConsumers instanceof OutlineVertexConsumerProvider) {
            OutlineVertexConsumerProvider outlineVertexConsumerProvider = (OutlineVertexConsumerProvider)vertexConsumers;
			if (ModuleManager.INSTANCE.isModuleEnabled("EntityESP") && ((EntityESP) ModuleManager.INSTANCE.getModule("EntityESP")).renderMode.isMode("Glow")) {
				if (entity instanceof Monster) {
					outlineVertexConsumerProvider.setColor(255, 0, 0, 255);
				} else if (entity instanceof AnimalEntity) {
					outlineVertexConsumerProvider.setColor(0, 255, 0, 255);
				} else if (!(entity instanceof AnimalEntity) && !(entity instanceof Monster) && !(entity instanceof PlayerEntity) && !(entity instanceof ItemEntity)) {
					outlineVertexConsumerProvider.setColor(0, 0, 255, 255);
				}
			}
			if (ModuleManager.INSTANCE.isModuleEnabled("ItemESP") && ((ItemESP) ModuleManager.INSTANCE.getModule("ItemESP")).renderMode.isMode("Glow")) {
				if (entity instanceof ItemEntity) {
					outlineVertexConsumerProvider.setColor(235, 52, 232, 255);
				}
			}
			if (ModuleManager.INSTANCE.isModuleEnabled("PlayerESP") && ((PlayerESP) ModuleManager.INSTANCE.getModule("PlayerESP")).renderMode.isMode("Glow")) {
				if (entity instanceof PlayerEntity) {
					if (entity != Client.mc.player) {
						if (Client.INSTANCE.doesFriendExist(entity.getName().getString())) {
							outlineVertexConsumerProvider.setColor(5, 247, 243, 255);
						} else {
							outlineVertexConsumerProvider.setColor(255, 255, 255, 255);
						}
					}
				}
			}
		}
	}
}