package simpleclient.mixins;

import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import simpleclient.Client;
import simpleclient.event.events.BlockRenderEvent;
import simpleclient.event.listeners.BlockRenderListener;
import simpleclient.module.ModuleManager;
import simpleclient.module.render.XRay;

@Mixin(TerrainRenderContext.class)
public class TerrainRenderContextMixin {
	@Inject(at = { @At("HEAD") }, method = { "tessellateBlock" }, cancellable = true, remap = false)
	private void tesselateBlock(BlockState blockState, BlockPos blockPos, final BakedModel model,
			MatrixStack matrixStack, CallbackInfo ci) {
        BlockPos origin = BlockPos.ORIGIN;

        BlockPos worldPos = origin.add(blockPos);

        BlockRenderEvent event = new BlockRenderEvent(blockState, worldPos);
        Client.getInstance().eventManager.Fire(event);
		if (!ModuleManager.INSTANCE.isModuleEnabled("XRay")) return;
		if (ModuleManager.INSTANCE.isModuleEnabled("XRay") && !Client.mc.player.isSpectator()) {
			if (!XRay.isXRayBlock(blockState.getBlock())) {
				ci.cancel();
				return;
			}
		} else {
			return;
		}
	}
}
