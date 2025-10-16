package simpleclient.module.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.listeners.RenderListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.BooleanSetting;
import simpleclient.module.settings.NumberSetting;
import simpleclient.ui.Color;

public class Tracer extends Mod implements RenderListener {

    public BooleanSetting renderPlayer = new BooleanSetting("Players", true);
    public BooleanSetting renderHostile = new BooleanSetting("Mnstr", false);
    public BooleanSetting renderAnimal = new BooleanSetting("Animal", false);
    public BooleanSetting renderOther = new BooleanSetting("Other", false);
    public NumberSetting crosshairExtend = new NumberSetting("CExtend", 5, 100, 20, 0.5);
	
	public Tracer() {
		super("Tracer", "Traces ESPs", Category.RENDER);
        addSettings(renderPlayer, renderHostile, renderAnimal, renderOther, crosshairExtend);
	}
	
	@Override
	public void onDisable() {
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
        Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
	}
	
	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(RenderListener.class, this);
	}
	
	@Override
	public void OnRender(RenderEvent event) {
		MatrixStack matrixStack = event.GetMatrixStack();
		
		matrixStack.push();
		//Why is it so hard to get the line of sight of the player :(
		for (Entity entity : mc.world.getEntities()) {
			if (entity instanceof LivingEntity) {
				
				//Vec3d screenCenter = new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ());
                Vec3d playerPos = mc.player.getEyePos();
                Vec3d lookVec = mc.player.getRotationVec(1.0f);

                // Extend the line 50 blocks in the direction the player is looking
                Vec3d screenCenter = playerPos.add(lookVec.multiply(crosshairExtend.getValue()));
				//Vec3d eyePos = mc.player.getEyePos();
				//double crossX = (double)-(1 * (Math.cos((mc.player.getYaw() - 90) * 3.14 / 180)));
				//double crossZ = (double)-(1 * (Math.sin((mc.player.getYaw() - 90) * 3.14 / 180)));
				//Vec3d screenCenter = new Vec3d(eyePos.getX() + crossX, eyePos.getY(), eyePos.getZ() + crossZ);
				
				
				if (entity instanceof AnimalEntity && renderAnimal.isEnabled()) {
					this.getRenderUtils().drawLine3D(matrixStack, screenCenter, entity.getPos(), new Color(0, 255, 0));
				} else if (entity instanceof Monster && renderHostile.isEnabled()) {
					this.getRenderUtils().drawLine3D(matrixStack, screenCenter, entity.getPos(), new Color(255, 0, 0));
				} else if (entity instanceof PlayerEntity && renderPlayer.isEnabled()) {
					if (entity != mc.player) {
						if (Client.INSTANCE.doesFriendExist(entity.getName().getString())) {
							this.getRenderUtils().drawLine3D(matrixStack, screenCenter, entity.getPos(), new Color(5, 247, 243));
						} else {
							this.getRenderUtils().drawLine3D(matrixStack, screenCenter, entity.getPos(), new Color(255, 255, 255));
						}
					}
				} else if (!(entity instanceof AnimalEntity) && !(entity instanceof Monster) && !(entity instanceof PlayerEntity) && renderOther.isEnabled()){
					this.getRenderUtils().drawLine3D(matrixStack, screenCenter, entity.getPos(), new Color(0, 0, 255));
				}
			}
		}
		matrixStack.pop();
	}

}
