package simpleclient.module;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.module.Mod.Category;
import simpleclient.module.exploit.*;
import simpleclient.module.movement.*;
import simpleclient.module.render.*;
import simpleclient.module.combat.*;
import simpleclient.module.world.*;
import simpleclient.module.setting.*;
import simpleclient.utils.RendererUtil;

public class ModuleManager {
	
	public static final ModuleManager INSTANCE = new ModuleManager();
	public List<Mod> modules = new ArrayList<>();
	
	public ModuleManager() {
		addModules();
	}
	
	public List<Mod> getModules() {
		return modules;
	}
	
	public List<Mod> getEnabledModules() {
		List<Mod> enabled = new ArrayList<>();
		for (Mod module : modules) {
			if (module.isEnabled()) {
				enabled.add(module);
			}
		}
		
		return enabled;
	}
	
	public List<Mod> getModulesInCategory(Category category) {
		List<Mod> categoryModules = new ArrayList<>();
		
		for (Mod mod : modules) {
			if (mod.getCategory() == category) {
				categoryModules.add(mod);
			}
		}
		
		return categoryModules;
	}
	
	public boolean isModuleEnabled(String moduleName) {
		for (Mod module: getEnabledModules()) {
			if (module.getName() == moduleName) {
				return true;
			}
		}
		return false;
	}
	
	public Mod getModule(String moduleName) {
		for (Mod module: getModules()) {
			if (module.getName().equals(moduleName)) {
				return module;
			}
		}
		return null;
	}
	
	public Mod getModuleRegardOfCases(String moduleName) {
		for (Mod module: getModules()) {
			if (module.getName().toLowerCase().equals(moduleName.toLowerCase())) {
				return module;
			}
		}
		return null;
	}
	
	public void render(MatrixStack matrixStack) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		
		
		matrixStack.push();
		RendererUtil.applyRenderOffset(matrixStack);
		
		RenderEvent renderEvent = new RenderEvent(matrixStack, MinecraftClient.getInstance().getTickDelta());
		Client.getInstance().eventManager.Fire(renderEvent);
		
		matrixStack.pop();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
	
	private void addModules() {
		//combat
		modules.add(new KillAura());
		modules.add(new Criticals());
		modules.add(new Reach());
		modules.add(new Aimbot());
		//movement
		modules.add(new Flight());
		modules.add(new Freecam());
		//modules.add(new Noclip());
		modules.add(new Jesus());
		modules.add(new NoFallDamage());
		modules.add(new Step());
		modules.add(new Sprint());
		//render
		modules.add(new EntityESP());
		modules.add(new PlayerESP());
		modules.add(new ChestESP());
		modules.add(new ItemESP());
        modules.add(new BlockESP());
		modules.add(new Tracer());
		modules.add(new FullBright());
		modules.add(new XRay());
		//exploit
		modules.add(new AutoEat());
		//world
		modules.add(new Nuker());
		//setting
		modules.add(new ShowKeyBind());
		modules.add(new RainbowHUD());
		modules.add(new ModifyHUD());
        modules.add(new TargetHUDSettings());
	}
}
