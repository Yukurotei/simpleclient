package simpleclient.module.render;

import java.util.ArrayList;
import java.util.stream.Collectors;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.util.math.Box;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.events.TickEvent;
import simpleclient.event.listeners.RenderListener;
import simpleclient.event.listeners.TickListener;
import simpleclient.misc.ModuleUtils;
import simpleclient.misc.RainbowColor;
import simpleclient.module.Mod;
import simpleclient.module.settings.BooleanSetting;
import simpleclient.module.settings.NumberSetting;
import simpleclient.ui.Color;

public class ChestESP extends Mod implements RenderListener, TickListener{
	private Color currentColor;
	private Color color;
	private RainbowColor rainbowColor;
	
	public NumberSetting hue = new NumberSetting("Hue", 0, 360, 57, 1);
	public BooleanSetting rainbow = new BooleanSetting("Rinbw", false);
	public NumberSetting effectSpeed = new NumberSetting("Efspd", 1, 20, 4, 0.1);
	
	public ChestESP() {
		super("ChestESP", "Does what it says", Category.RENDER);
		color = new Color(hue.getValueFloat(), 1f, 1f);
		currentColor = color;
		rainbowColor = new RainbowColor();
		this.addSettings(hue, rainbow, effectSpeed);
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
	public void onEnable() {
		Client.getInstance().eventManager.AddListener(RenderListener.class, this);
		Client.getInstance().eventManager.AddListener(TickListener.class, this);
		super.onEnable();
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void OnRender(RenderEvent event) {
		ArrayList<BlockEntity> blockEntities = ModuleUtils.getTileEntities().collect(Collectors.toCollection(ArrayList::new));
		for(BlockEntity blockEntity : blockEntities) {
			if(blockEntity instanceof ChestBlockEntity || blockEntity instanceof TrappedChestBlockEntity || blockEntity instanceof ShulkerBoxBlockEntity || blockEntity instanceof EnderChestBlockEntity ||
					blockEntity instanceof BarrelBlockEntity) {
				Box box = new Box(blockEntity.getPos());
				this.getRenderUtils().draw3DBox(event.GetMatrixStack(), box, currentColor, 0.2f);
			}
		}
	}
	
	@Override
	public void OnUpdate(TickEvent event) {
		if(this.rainbow.isEnabled()) {
			this.rainbowColor.update(this.effectSpeed.getValueFloat());
			this.currentColor = this.rainbowColor.getColor();
		}else {
			this.color.setHSV(hue.getValueFloat(), 1f, 1f);
			this.currentColor = color;
		}
	}

}
