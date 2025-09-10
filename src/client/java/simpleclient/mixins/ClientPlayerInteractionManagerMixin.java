package simpleclient.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import simpleclient.Client;
import simpleclient.module.Mod;
import simpleclient.module.combat.Reach;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
	
	@Inject(at= {@At("HEAD")}, method={"getReachDistance()F"}, cancellable=true)
	private void onGetReachDistance(CallbackInfoReturnable<Float> ci) {
		for (Mod module : Client.getInstance().moduleManager.getModules()) {
			if (module.getName().equals("Reach")) {
				Reach reachModule = (Reach) module;
				if (Client.INSTANCE.extendedReach) {
					ci.setReturnValue((float)reachModule.getReach());
					break;
				}
			}
		}
	}
	
	@Inject(at={ @At("HEAD") }, method={"hasExtendedReach()Z"}, cancellable=true)
	private void hasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
		for (Mod module : Client.getInstance().moduleManager.getModules()) {
			if (module.getName().equals("Reach")) {
				//Reach reachModule = (Reach) module;
				if (Client.INSTANCE.extendedReach) {
					cir.setReturnValue(true);
					break;
				}
			}
		}
	}
}
