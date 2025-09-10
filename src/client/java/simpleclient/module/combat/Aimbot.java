package simpleclient.module.combat;

import org.lwjgl.glfw.GLFW;

import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.events.TickEvent;
import simpleclient.event.listeners.RenderListener;
import simpleclient.event.listeners.TickListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.BooleanSetting;
import simpleclient.module.settings.NumberSetting;

public class Aimbot extends Mod implements RenderListener, TickListener {
	
	private LivingEntity temp = null;
	
	public NumberSetting radius = new NumberSetting("Radius", 1f, 50f, 10f, 1f);
	public BooleanSetting targetAnimals = new BooleanSetting("Animals", false);
	public BooleanSetting targetPlayers = new BooleanSetting("Plyers", false);
	public BooleanSetting targetMonsters = new BooleanSetting("Mnstrs", true);
	public BooleanSetting legitAiming = new BooleanSetting("legit", false);
	
	public Aimbot() {
		super("Aimbot", "Does what it says", Category.COMBAT);
		addSettings(radius, targetAnimals, targetPlayers, targetMonsters, legitAiming);
		this.setKey(GLFW.GLFW_KEY_KP_7);
	}
	
	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(RenderListener.class, this);
		Client.getInstance().eventManager.AddListener(TickListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		Client.getInstance().eventManager.RemoveListener(TickListener.class, this);
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		Client.getInstance().eventManager.RemoveListener(TickListener.class, this);
		super.onDisable();
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void OnRender(RenderEvent event) {
		if (temp != null) {
			mc.player.lookAt(EntityAnchor.EYES, temp.getEyePos());
		}
	}
	
	@Override
	public void OnUpdate(TickEvent event) {
		if (mc.world != null) {
			if (temp != null) {
				if (mc.player.squaredDistanceTo(temp) > (this.radius.getValue()*this.radius.getValue())) temp = null;
			}
			if (this.targetPlayers.isEnabled()) {
				if (mc.world.getPlayers().size() == 2) {
					PlayerEntity targetPlayer = mc.world.getPlayers().get(1);
					if (mc.player.squaredDistanceTo(targetPlayer) > (this.radius.getValue()*this.radius.getValue()) && targetPlayer == mc.player) return;
					temp = targetPlayer;
				} else if (mc.world.getPlayers().size() > 2) {
					for (int x = 0; x < mc.world.getPlayers().size(); x++) {
						for (int y = 1; y < mc.world.getPlayers().size(); y++) {
							if (mc.world.getPlayers().get(x).distanceTo(mc.player) < mc.world.getPlayers().get(y)
									.distanceTo(mc.player)) {
								PlayerEntity tgPlayer = mc.world.getPlayers().get(x);
								if (tgPlayer == mc.player) return;
								if (mc.player.squaredDistanceTo(tgPlayer) > (this.radius.getValue()*this.radius.getValue())) continue;
								if (legitAiming.isEnabled()) {
									if (mc.player.canSee(tgPlayer)) {
										temp = tgPlayer;
									}
								} else {
									temp = tgPlayer;
								}
							}
						}
					}
				}
			}
			if (this.targetAnimals.isEnabled() || this.targetMonsters.isEnabled()) {
				LivingEntity tempEntity = null;
				for (Entity entity : mc.world.getEntities()) {
					if (mc.player.squaredDistanceTo(entity) > (this.radius.getValue()*this.radius.getValue())) continue;
					if (!(entity instanceof LivingEntity))
						continue;
					if (entity instanceof PlayerEntity)
						continue;
					if (entity instanceof Monster) {
						if (!targetMonsters.isEnabled()) {
							continue;
						}
					}
					if (entity instanceof AnimalEntity) {
						if (!targetAnimals.isEnabled()) {
							continue;
						}
					}
					if (!(entity instanceof PlayerEntity) && !(entity instanceof Monster) && !(entity instanceof AnimalEntity)) continue;
					if (tempEntity == null) {
						if (legitAiming.isEnabled()) {
							if (mc.player.canSee(entity)) {
								tempEntity = (LivingEntity) entity;
							}
						} else {
							tempEntity = (LivingEntity) entity;
						}
					} else {
						if (entity.distanceTo(mc.player) < tempEntity.distanceTo(mc.player)) {
							if (legitAiming.isEnabled()) {
								if (mc.player.canSee(entity)) {
									tempEntity = (LivingEntity) entity;
								}
							} else {
								tempEntity = (LivingEntity) entity;
							}
						}
					}
				}
				temp = tempEntity;
			}
		}
	}
}
