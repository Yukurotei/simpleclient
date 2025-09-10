package simpleclient.mixins;

import com.mojang.blaze3d.systems.RenderSystem;

import simpleclient.module.ModuleManager;
import simpleclient.module.combat.Reach;
import simpleclient.utils.RendererUtil;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0), method = "renderWorld")
	void renderer_postWorldRender(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
		//RenderProfiler.begin("World");

		RendererUtil.lastProjMat.set(RenderSystem.getProjectionMatrix());
		RendererUtil.lastModMat.set(RenderSystem.getModelViewMatrix());
		RendererUtil.lastWorldSpaceMatrix.set(matrix.peek().getPositionMatrix());
		//RenderEvents.WORLD.invoker().rendered(matrix);
		//Renderer3d.renderFadingBlocks(matrix);

		//RenderProfiler.pop();
	}
	
	@ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3))
    private double updateTargetedEntityModifySurvivalReach(double d) {
		if (ModuleManager.INSTANCE.getModule("Reach") == null) {
			return (double)3;
		}
		if (ModuleManager.INSTANCE.getModule("Reach").isEnabled()) {
			return (double)((Reach)ModuleManager.INSTANCE.getModule("Reach")).getReach();
		} else {
			return (double)3;
		}
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9))
    private double updateTargetedEntityModifySquaredMaxReach(double d) {
		if (ModuleManager.INSTANCE.getModule("Reach") == null) {
			return (double)3;
		}
    	if (ModuleManager.INSTANCE.getModule("Reach").isEnabled()) {
    		double reachDis = (double)((Reach)ModuleManager.INSTANCE.getModule("Reach")).getReach();
            return reachDis * reachDis;
    	} else {
    		return (double)3 * 3;
    	}
    }
}