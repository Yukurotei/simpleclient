package simpleclient.event.events;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import simpleclient.event.listeners.AbstractListener;
import simpleclient.event.listeners.BlockRenderListener;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockRenderEvent extends AbstractEvent {
    BlockState blockState;
    BlockPos pos;

    public BlockState GetBlock() {
        return blockState;
    }

    public BlockPos GetBlockPos() {
        return pos;
    }

    public BlockRenderEvent(BlockState blockState, BlockPos pos) {
        this.blockState = blockState;
        this.pos = pos;
    }

    @Override
    public void Fire(CopyOnWriteArrayList<? extends AbstractListener> listeners) {
        for (AbstractListener listener : listeners) {
            BlockRenderListener blockRenderListener = (BlockRenderListener) listener;
            blockRenderListener.OnBlockRender(this);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<BlockRenderListener> GetListenerClassType() {
        return BlockRenderListener.class;
    }
}
