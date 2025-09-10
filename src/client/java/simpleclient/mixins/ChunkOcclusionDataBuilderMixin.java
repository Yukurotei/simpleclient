package simpleclient.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos;
import simpleclient.module.ModuleManager;

@Mixin(ChunkOcclusionDataBuilder.class)
public class ChunkOcclusionDataBuilderMixin {
	@Inject(at = {@At("HEAD")},
			method = {"markClosed(Lnet/minecraft/util/math/BlockPos;)V"},
			cancellable = true)
		private void onMarkClosed(BlockPos pos, CallbackInfo ci)
		{
			if (!ModuleManager.INSTANCE.isModuleEnabled("XRay")) return;
			if(ModuleManager.INSTANCE.isModuleEnabled("XRay")) {
				ci.cancel();
			}
		}
}