package simpleclient.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import simpleclient.module.ModuleManager;

@Mixin(AbstractBlockState.class)
public class AbstractBlockStateMixin extends State<Block, BlockState> {
	
	private AbstractBlockStateMixin(Block object, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
		super(object, immutableMap, mapCodec);
	}
	
	@Inject(at = @At("TAIL"), method = {"getAmbientOcclusionLightLevel(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"}, cancellable=true)
	private void onGetAmbietOcclusionLightLevel(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
		if (!ModuleManager.INSTANCE.isModuleEnabled("XRay")) {
			return;
		}
		cir.setReturnValue(1F);
	}
}
