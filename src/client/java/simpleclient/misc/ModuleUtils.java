package simpleclient.misc;

import java.util.Objects;
import java.util.stream.Stream;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BowItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LingeringPotionItem;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import simpleclient.Client;
import simpleclient.SimpleClient;

public class ModuleUtils {
	public static boolean isThrowable(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof BowItem || item instanceof SnowballItem || item instanceof EggItem
				|| item instanceof EnderPearlItem || item instanceof SplashPotionItem
				|| item instanceof LingeringPotionItem || item instanceof FishingRodItem;
	}
	
	public static double throwableGravity(Item item) {
		if(item instanceof RangedWeaponItem) {
			return 0.05;
		}else if(item instanceof ThrowablePotionItem) {
			return 0.4;
		}else if(item instanceof FishingRodItem) {
			return 0.15;
		}else if(item instanceof TridentItem) {
			return 0.015;
		}else {
			return 0.03;
		}
	}
	
	public static boolean isPlantable(ItemStack stack) {
		Item item = stack.getItem();
		return item == Items.WHEAT_SEEDS ||  item == Items.CARROT || item == Items.POTATO;
	}
	
	public static Stream<BlockEntity> getTileEntities(){
		return getLoadedChunks().flatMap(chunk -> chunk.getBlockEntities().values().stream());
	}

    public static Stream<BlockPos> getAllBlocks() {
        // Get the stream of loaded chunks (assuming this function works as before)
        return getLoadedChunks().flatMap(chunk -> {
            // Get the current world object to determine the height bounds.
            World world = MinecraftClient.getInstance().world;
            if (world == null) {
                return Stream.empty();
            }

            // Get the chunk's position coordinates (x, z)
            ChunkPos chunkPos = chunk.getPos();

            // Define the start and end coordinates of the chunk in world space
            // The BlockPos.stream() method is inclusive of the start and end points.
            BlockPos start = new BlockPos(
                    chunkPos.getStartX(),
                    world.getBottomY(), // Use the world's bottom Y level
                    chunkPos.getStartZ()
            );
            BlockPos end = new BlockPos(
                    chunkPos.getEndX(),
                    world.getTopY() - 1, // Use the world's top Y level
                    chunkPos.getEndZ()
            );

            // Use the BlockPos.stream() method to generate a stream of all block positions in the chunk
            return BlockPos.stream(start, end);
        });
    }


    public static Stream<WorldChunk> getLoadedChunks(){
		Client.getInstance();
		int radius = Math.max(2, SimpleClient.MC.options.getClampedViewDistance()) + 3;
		int diameter = radius * 2 + 1;
		
		Client.getInstance();
		ChunkPos center = SimpleClient.MC.player.getChunkPos();
		ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
		ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);
		
		Client.getInstance();
		Client.getInstance();
		Stream<WorldChunk> stream = Stream.<ChunkPos> iterate(min, pos -> {
			int x = pos.x;
			int z = pos.z;
			x++;
			
			if(x > max.x)
			{
				x = min.x;
				z++;
			}
			
			return new ChunkPos(x, z);

		}).limit(diameter*diameter)
			.filter(c -> SimpleClient.MC.world.isChunkLoaded(c.x, c.z))
			.map(c -> SimpleClient.MC.world.getChunk(c.x, c.z)).filter(Objects::nonNull);
		
		return stream;
	}
}
