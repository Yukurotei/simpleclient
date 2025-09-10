package simpleclient.module.world;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import simpleclient.Client;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.events.TickEvent;
import simpleclient.event.listeners.RenderListener;
import simpleclient.event.listeners.TickListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.ModeSetting;
import simpleclient.module.settings.NumberSetting;
import simpleclient.ui.Color;

public class Nuker extends Mod implements RenderListener, TickListener {
	
	private enum digMode {
		AROUND, CANCELY
	}
	private digMode dgMode = digMode.AROUND;
	private MinecraftClient mc;
	
	private int clock_cap = 2;
	
	public NumberSetting radius = new NumberSetting("Radius", 0f, 15f, 5f, 1f);
	public NumberSetting clockPause = new NumberSetting("clock", 1f, 20f, 2f, 1f);
	public ModeSetting nukerMode = new ModeSetting("dig", "Arnd", "Arnd", "noY");
	private int clock = 0;
	private boolean canBreak = false;
	
	public Nuker() {
		super("Nuker", "Destroys blocks around the player", Category.WORLD);
		addSettings(radius, clockPause, nukerMode);
		mc = MinecraftClient.getInstance();
	}
	
	public void setRadius(int radius) {
		this.radius.setValue((double)radius);
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
		clock_cap = clockPause.getValueInt();
		if (nukerMode.getMode().equals("Arnd")) {
			dgMode = digMode.AROUND;
		} else if (nukerMode.getMode().equals("noY")) {
			dgMode = digMode.CANCELY;
		}
		if (canBreak) {
			canBreak = false;
		}
		clock += 1;
		if (clock >= clock_cap) {
			clock = 0;
			canBreak = true;
		}
		super.onTick();
	}
	
	@Override
	public void OnUpdate(TickEvent event) {
		if (mc.player != null && mc.world != null && canBreak) {
			int rad = radius.getValueInt();
			for (int x = -rad; x < rad; x++) {
				for (int y = rad; y > -rad; y--) {
					for (int z = -rad; z < rad; z++) {
						if (dgMode == digMode.CANCELY && mc.player.getBlockY() + y < mc.player.getBlockY()) {
							continue;
						}
						BlockPos blockpos = new BlockPos(mc.player.getBlockX() + x, (int) mc.player.getBlockY() + y,
								(int) mc.player.getBlockZ() + z);
						Block block = mc.world.getBlockState(blockpos).getBlock();
						if (block == Blocks.AIR)
							continue;
						
						mc.player.networkHandler.sendPacket(
								new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, blockpos, mc.player.getHorizontalFacing()));
						mc.player.networkHandler
								.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockpos, mc.player.getHorizontalFacing()));
					}
				}
			}
		}
	}
	
	@Override
	public void OnRender(RenderEvent event) {
		if (mc.player != null && mc.world != null) {
			int rad = radius.getValueInt();
			for (int x = -rad; x < rad; x++) {
				for (int y = rad; y > -rad; y--) {
					for (int z = -rad; z < rad; z++) {
						if (dgMode == digMode.CANCELY && mc.player.getBlockY() + y < mc.player.getBlockY()) {
							continue;
						}
						BlockPos blockpos = new BlockPos(mc.player.getBlockX()+ x, mc.player.getBlockY() + y,
								mc.player.getBlockZ()+ z);
						Block block = mc.world.getBlockState(blockpos).getBlock();
	
						if (block == Blocks.AIR || block == Blocks.WATER || block == Blocks.LAVA)
							continue;
	
						this.getRenderUtils().draw3DBox(event.GetMatrixStack(), new Box(blockpos), new Color(255,0,0), 0.2f);
					}
				}
			}
		}
	}
}
