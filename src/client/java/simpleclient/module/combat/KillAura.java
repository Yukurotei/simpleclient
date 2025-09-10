package simpleclient.module.combat;

import simpleclient.ui.Color;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.events.TickEvent;
import simpleclient.event.listeners.RenderListener;
import simpleclient.event.listeners.TickListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.BooleanSetting;
import simpleclient.module.settings.ModeSetting;
import simpleclient.module.settings.NumberSetting;
import simpleclient.utils.RendererUtil;

public class KillAura extends Mod implements TickListener, RenderListener{
	
	private enum Priority {
		LOWESTHP, CLOSEST
	}
	
	private enum Weapon {
		AXE, SWORD
	}
	
	private Priority priority = Priority.LOWESTHP;
	private Weapon weapon = Weapon.SWORD;
	private LivingEntity currentTarget = null;
	public NumberSetting radius = new NumberSetting("KAR", 0.1f, 10f, 5f, 0.1f);
	public NumberSetting prepRadius = new NumberSetting("PR", 1f, 15f, 7f, 1f);
	public BooleanSetting targetAnimals = new BooleanSetting("Animals", false);
	public BooleanSetting targetMonsters = new BooleanSetting("Mnstrs", true);
	public BooleanSetting targetPlayers = new BooleanSetting("Plyers", true);
	public BooleanSetting legitAttacking = new BooleanSetting("legit", false);
	public ModeSetting prio = new ModeSetting("Prio", "Close", "Close", "lowHP");
	public ModeSetting attackWeapon = new ModeSetting("Wpon", "Sword", "Axe", "Sword");
	public boolean attacking = false;
	
	public KillAura() {
		super("Kill Aura", "Auto hit stuff", Category.COMBAT);
		this.addSettings(radius, prepRadius, targetAnimals, targetMonsters, targetPlayers, legitAttacking, prio, attackWeapon);
		this.setKey(GLFW.GLFW_KEY_KP_9);
	}
	
	@Override
	public void onTick() {
		if (prio.isMode("Close")) {
			this.priority = Priority.CLOSEST;
		} else if (prio.isMode("lowHP")) {
			this.priority = Priority.LOWESTHP;
		}
		if (attackWeapon.isMode("Axe")) {
			weapon = Weapon.AXE;
		} else if (attackWeapon.isMode("Sword")) {
			weapon = Weapon.SWORD;
		}
		super.onTick();
	}
	
	@Override
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(TickListener.class, this);
		Client.getInstance().eventManager.AddListener(RenderListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.getInstance().eventManager.RemoveListener(TickListener.class, this);
		Client.getInstance().eventManager.RemoveListener(TickListener.class, this);
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		super.onDisable();
	}
	
	@Override
	public void OnUpdate(TickEvent event) {
		if (mc.player != null) {
			//Weapon Switch Statement
			for (Entity entity : mc.world.getEntities()) {
				if (!entity.isAlive()) continue;
				if (legitAttacking.isEnabled()) {
					if (!mc.player.canSee(entity)) {
						continue;
					}
				}
				if (entity instanceof PlayerEntity && Client.INSTANCE.doesFriendExist(entity.getName().getString())) continue;
				if (!(mc.player.squaredDistanceTo(entity) > (this.prepRadius.getValue()*this.prepRadius.getValue()))) {
					if (entity instanceof PlayerEntity) {
						PlayerEntity playerEntity = (PlayerEntity) entity;
						if (playerEntity.getName().getString().equals(mc.player.getName().getString())) {
							continue;
						}
					
					}
					if (!(entity instanceof Monster) && !(entity instanceof PlayerEntity) && !(entity instanceof AnimalEntity)) continue;
					//System.out.println("Doing wepon switch");
					ItemStack heldItem = mc.player.getMainHandStack();
					if (this.weapon == Weapon.AXE) {
						if (!(heldItem.getItem() instanceof AxeItem)) {
							PlayerInventory playerInventory = mc.player.getInventory();
							for (int i = 0; i < playerInventory.size(); i++) {
								ItemStack item = playerInventory.getStack(i);
								if (!item.isEmpty() && item.getItem() instanceof AxeItem) {
									if (PlayerInventory.isValidHotbarIndex(i)) {
										playerInventory.selectedSlot = i;
										break;
									} else {
										mc.interactionManager.clickSlot(
												mc.player.playerScreenHandler.syncId,
												i,
												playerInventory.selectedSlot,
												SlotActionType.SWAP,
												mc.player
										);
										break;
									}
								}
							}
						}
					}
					if (this.weapon == Weapon.SWORD) {
						if (!(heldItem.getItem() instanceof SwordItem)) {
							PlayerInventory playerInventory = mc.player.getInventory();
							for (int i = 0; i < playerInventory.size(); i++) {
								ItemStack item = playerInventory.getStack(i);
								if (!item.isEmpty() && item.getItem() instanceof SwordItem) {
									if (PlayerInventory.isValidHotbarIndex(i)) {
										playerInventory.selectedSlot = i;
										break;
									} else {
										mc.interactionManager.clickSlot(
												mc.player.playerScreenHandler.syncId,
												i,
												playerInventory.selectedSlot,
												SlotActionType.SWAP,
												mc.player
										);
										break;
									}
								}
							}
						}
					}
				}
			}
			//Attack statements
			if (mc.player.getAttackCooldownProgress(0) == 1) {
				ArrayList<Entity> hitList = new ArrayList<Entity>();
				LivingEntity entityToAttack = null;
				boolean found = false;
				
				for (Entity entity : mc.world.getEntities()) {
					if (mc.player.squaredDistanceTo(entity) > (this.radius.getValue()*this.radius.getValue())) continue;
					if (legitAttacking.isEnabled()) {
						if (!mc.player.canSee(entity)) {
							continue;
						}
					}
					if ((entity instanceof AnimalEntity && this.targetAnimals.isEnabled()) || (entity instanceof Monster && this.targetMonsters.isEnabled())) {
						hitList.add(entity);
					}
				}
				
				if (this.targetPlayers.isEnabled()) {
					for (PlayerEntity player : mc.world.getPlayers()) {
						if (Client.INSTANCE.doesFriendExist(player.getName().getString())) continue;
						if (player.getName().getString().equals(mc.player.getName().getString())) continue;
						if (legitAttacking.isEnabled()) {
							if (!mc.player.canSee(player)) {
								continue;
							}
						}
						if (player == mc.player || mc.player.squaredDistanceTo(player) > (this.radius.getValue()*this.radius.getValue())) {
							continue;
						}
						hitList.add(player);
					}
				}
				
				for (Entity entity : hitList) {
					LivingEntity le = (LivingEntity) entity;
					if (entityToAttack == null) {
						entityToAttack = le;
						found = true;
					} else {
						if (this.priority == Priority.CLOSEST) {
							if (le.getHealth() <= entityToAttack.getHealth()) {
								entityToAttack = le;
								found = true;
							}
						} else if (this.priority == Priority.CLOSEST) {
							if (mc.player.squaredDistanceTo(le) <= mc.player.squaredDistanceTo(entityToAttack)) {
								entityToAttack = le;
								found = true;
							}
						}
					}
				}
				if (found) {
					if (entityToAttack.getHealth() != 0) {
						this.currentTarget = entityToAttack;
						attacking = true;
						setInfo(": Attacking " + entityToAttack.getName().getString());
						mc.interactionManager.attackEntity(mc.player, entityToAttack);
						mc.player.swingHand(Hand.MAIN_HAND);
					}
				} else {
					setInfo(": Scanning");
					attacking = false;
				}
			}
		}
	}
	
	@Override
	public void OnRender(RenderEvent event) {
		if (this.currentTarget != null && this.currentTarget.getHealth() != 0) {
			if (legitAttacking.isEnabled()) {
				if (!mc.player.canSee(currentTarget)) {
					return;
				}
			}
			Color tracerColor = null;
			if (!(mc.player.squaredDistanceTo(this.currentTarget) > (this.prepRadius.getValue()*this.prepRadius.getValue()))) tracerColor = new Color(255, 165, 0);
			if (!(mc.player.squaredDistanceTo(this.currentTarget) > (this.radius.getValue()*this.radius.getValue()))) tracerColor = new Color(255, 0, 0);
			if (RendererUtil.screenSpaceCoordinateIsVisible(RendererUtil.worldSpaceToScreenSpace(currentTarget.getPos())) && tracerColor != null) {
					this.getRenderUtils().drawLine3D(event.GetMatrixStack(), mc.player.getBoundingBox().getCenter(), this.currentTarget.getPos(), tracerColor);
			}
		}
	}
}
