package simpleclient.module.render;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.listeners.RenderListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.BooleanSetting;
import simpleclient.module.settings.ModeSetting;
import simpleclient.ui.Color;

public class EntityESP extends Mod implements RenderListener {

	public BooleanSetting renderHostile = new BooleanSetting("Mnstr", false);
	public BooleanSetting renderAnimal = new BooleanSetting("Animal", false);
	public BooleanSetting renderOther = new BooleanSetting("Other", false);
	public ModeSetting renderMode = new ModeSetting("Mode", "Box", "Box", "Glow");
	
	public EntityESP() {
		super("EntityESP", "Does what it says", Category.RENDER);
		this.setKey(GLFW.GLFW_KEY_KP_2);
		addSettings(renderHostile, renderAnimal, renderOther, renderMode);
	}

	@Override
	public void onDisable() {
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		super.onDisable();
	}

	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(RenderListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}

	@Override
	public void OnRender(RenderEvent event) {
		MatrixStack matrixStack = event.GetMatrixStack();
		float partialTicks = event.GetPartialTicks();
		
		if (renderMode.isMode("Box")) {
			this.setInfo(": Box");
			matrixStack.push();
			
			for (Entity entity : mc.world.getEntities()) {
				if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
					
					Box boundingBox = entity.getBoundingBox(); 
					
					Vec3d entityVelocity = entity.getVelocity();
					Vec3d velocityPartial = new Vec3d(entityVelocity.x * partialTicks, 0, entityVelocity.z * partialTicks);
					
					boundingBox = boundingBox.offset(velocityPartial);
					
					if (entity instanceof AnimalEntity && renderAnimal.isEnabled()) {
						this.getRenderUtils().draw3DBox(matrixStack, boundingBox, new Color(0, 255, 0), 0.2f);
					} else if (entity instanceof Monster && renderHostile.isEnabled()) {
						this.getRenderUtils().draw3DBox(matrixStack, boundingBox, new Color(255, 0, 0), 0.2f);
					} else if (!(entity instanceof AnimalEntity) && !(entity instanceof Monster) && !(entity instanceof PlayerEntity) && renderOther.isEnabled()){
						this.getRenderUtils().draw3DBox(matrixStack, boundingBox, new Color(0, 0, 255), 0.2f);
					}
				}
			}
			matrixStack.pop();
		} else {
			this.setInfo(": Glow");
			//Everything moved to mixin
		}
	}
}
