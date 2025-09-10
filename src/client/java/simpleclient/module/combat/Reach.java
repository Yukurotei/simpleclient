package simpleclient.module.combat;

import simpleclient.Client;
import simpleclient.module.Mod;
import simpleclient.module.settings.NumberSetting;

public class Reach extends Mod {
	
	public NumberSetting distance = new NumberSetting("Distance", 1f, 15f, 6f, 1f);
	
	public Reach() {
		super("Reach", "GIVES YOU LONG ARMS", Category.COMBAT);
		addSetting(distance);
	}
	
	public float getReach() {
		return Client.INSTANCE.reach;
	}
	
	@Override
	public void onDisable() {
		Client.INSTANCE.extendedReach = false;
		super.onDisable();
	}
	
	@Override
	public void onEnable() {
		Client.INSTANCE.extendedReach = true;
		super.onEnable();
	}
	
	@Override
	public void onTick() {
		Client.INSTANCE.reach = this.distance.getValueFloat();
		super.onTick();
	}
	
	public void setReachLength(float reach) {
		this.distance.setValue((double)reach);
	}
}
