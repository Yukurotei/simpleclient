package simpleclient.mixins;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Codec;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import simpleclient.interfaces.ISimpleOption;

@Mixin(SimpleOption.class)
public class SimpleOptionMixin<T> implements ISimpleOption<T> {
	
	@Shadow
    @Final
    Text text;
	private Consumer<T> changeCallback;

    @Shadow
    T value;

    @Inject(method = "getCodec", at = @At("HEAD"), cancellable = true)
    private void fullbright$returnFakeCodec(CallbackInfoReturnable<Codec<Double>> ci) {
        if (text.getString().equals(I18n.translate("options.gamma")))
            ci.setReturnValue(Codec.DOUBLE);
    }

    @Inject(method = "setValue", at = @At("HEAD"), cancellable = true)
    private void fullbright$setGammaValue(T value, CallbackInfo ci) {
        if (text.getString().equals(I18n.translate("options.gamma"))) {
            this.value = value;
            ci.cancel();
        }
    }
    
    @Override
    public void forceSetValue(T newValue) {
    	if (!MinecraftClient.getInstance().isRunning()) {
    		value = newValue;
    		return;
    	}
    	
    	if (!value.equals(newValue)) {
    		value = newValue;
    		changeCallback.accept(value);
    	}
    }

}