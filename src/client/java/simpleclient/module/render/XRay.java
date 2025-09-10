package simpleclient.module.render;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
//import simpleclient.interfaces.ISimpleOption;
import simpleclient.module.Mod;

public class XRay extends Mod {
	public static ArrayList<Block> blocks = new ArrayList<Block>();
	
	public XRay() {
		super("XRay", "Allows you to xray without putting on a texture pack", Category.RENDER);
		initXRay();
	}
	
	@Override
	public void onDisable() {
		//OUTDATED METHOD
		//USE WITH FULL BRIGHT
		/*
		@SuppressWarnings("unchecked")
		ISimpleOption<Double> gamma = 
						(ISimpleOption<Double>)(Object)mc.options.getGamma();
		gamma.forceSetValue(1.0);
		*/
		mc.worldRenderer.reload();
		super.onDisable();
	}
	
	@Override
	public void onEnable() {
		mc.worldRenderer.reload();
		//OUTDATED METHOD
		//USE WITH FULL BRIGHT
		/*
		@SuppressWarnings("unchecked")
		ISimpleOption<Double> gamma = 
						(ISimpleOption<Double>)(Object)mc.options.getGamma();
		gamma.forceSetValue(10000.0);
		*/
		super.onEnable();
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	public void initXRay() {
		blocks.add(Blocks.EMERALD_ORE);
		blocks.add(Blocks.EMERALD_BLOCK);
		blocks.add(Blocks.DIAMOND_ORE);
		blocks.add(Blocks.DIAMOND_BLOCK);
		blocks.add(Blocks.GOLD_ORE);
		blocks.add(Blocks.GOLD_BLOCK);
		blocks.add(Blocks.IRON_ORE);
		blocks.add(Blocks.IRON_BLOCK);
		blocks.add(Blocks.COAL_ORE);
		blocks.add(Blocks.COAL_BLOCK);
		blocks.add(Blocks.REDSTONE_BLOCK);
		blocks.add(Blocks.REDSTONE_ORE);
		blocks.add(Blocks.LAPIS_ORE);
		blocks.add(Blocks.LAPIS_BLOCK);
		blocks.add(Blocks.NETHER_QUARTZ_ORE);
		blocks.add(Blocks.MOSSY_COBBLESTONE);
		blocks.add(Blocks.COBBLESTONE);
		blocks.add(Blocks.SPAWNER);
		blocks.add(Blocks.STONE_BRICKS);
		blocks.add(Blocks.MOSSY_STONE_BRICKS);
		blocks.add(Blocks.SMOOTH_STONE_SLAB);
		blocks.add(Blocks.CRACKED_STONE_BRICKS);
		blocks.add(Blocks.END_PORTAL_FRAME);
		blocks.add(Blocks.IRON_BARS);
		blocks.add(Blocks.CHEST);
		blocks.add(Blocks.OAK_DOOR);
		blocks.add(Blocks.IRON_DOOR);
		blocks.add(Blocks.BOOKSHELF);
		blocks.add(Blocks.OAK_FENCE);
		blocks.add(Blocks.OAK_PLANKS);
		blocks.add(Blocks.DEEPSLATE_EMERALD_ORE);
		blocks.add(Blocks.DEEPSLATE_DIAMOND_ORE);
		blocks.add(Blocks.DEEPSLATE_GOLD_ORE);
		blocks.add(Blocks.DEEPSLATE_IRON_ORE);
		blocks.add(Blocks.DEEPSLATE_COAL_ORE);
		blocks.add(Blocks.ANCIENT_DEBRIS);
		blocks.add(Blocks.NETHER_GOLD_ORE);
		blocks.add(Blocks.RAW_GOLD_BLOCK);
		blocks.add(Blocks.RAW_IRON_BLOCK);
	}
	
	public static boolean isXRayBlock(Block b) {
		if (XRay.blocks.contains(b)) {
			return true;
		}
		return false;
	}
}
