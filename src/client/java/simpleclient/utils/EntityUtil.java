package simpleclient.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class EntityUtil extends Utils{
	
	//public static final EntityUtil INSTANCE = new EntityUtil();
	//private MinecraftClient mc = MinecraftClient.getInstance();
	
	private Entity getCrosshairHitEntity() {
	    if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
	    	return ((EntityHitResult)mc.crosshairTarget).getEntity();
	    }
		return null;
	 }
	
	/*
	private BlockPos getCrosshairTargetedBlockPos() {
	    if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
	    	return ((BlockHitResult)mc.crosshairTarget).getBlockPos();
	    }
		return null;
	 }
	 */
	
	public boolean isPlayerHitting(Entity entity) {
		Entity targetedEntity = getCrosshairHitEntity();
		if (targetedEntity != null) {
			if (targetedEntity.getId() == entity.getId()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
