package simpleclient.mixins;

import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import simpleclient.Client;
import simpleclient.event.events.BlockRenderEvent;
import simpleclient.module.ModuleManager;
import simpleclient.module.render.XRay;

@Mixin(BlockModelRenderer.class)
public abstract class BlockModelRendererMixin {

	@Inject(at = { @At("HEAD") }, method = {
			"renderSmooth(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLnet/minecraft/util/math/random/Random;JI)V",
			"renderFlat(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLnet/minecraft/util/math/random/Random;JI)V" }, cancellable = true)
	public void onRenderSmoothOrFlat(BlockRenderView world, BakedModel model,
			BlockState state, BlockPos pos, MatrixStack matrices,
			VertexConsumer vertexConsumer, boolean cull, Random random, long seed,
			int overlay, CallbackInfo ci) {
		if (!ModuleManager.INSTANCE.isModuleEnabled("XRay")) return;
		if (ModuleManager.INSTANCE.isModuleEnabled("XRay") && !Client.mc.player.isSpectator()) {
			if (XRay.isXRayBlock(state.getBlock())) {
				ci.cancel();
				return;
			}
		} else {
			return;
		}
	}
}
