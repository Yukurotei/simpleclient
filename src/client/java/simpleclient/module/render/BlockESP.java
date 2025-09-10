package simpleclient.module.render;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import simpleclient.Client;
import simpleclient.event.events.BlockRenderEvent;
import simpleclient.event.events.RenderEvent;
import simpleclient.event.listeners.BlockRenderListener;
import simpleclient.event.listeners.RenderListener;
import simpleclient.module.Mod;
import simpleclient.module.settings.NumberSetting;
import simpleclient.ui.Color;
import simpleclient.utils.RendererUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BlockESP extends Mod implements RenderListener, BlockRenderListener {

    public Map<String, Color> blockEntries = new HashMap<>();
    private final Map<BlockPos, BlockState> foundBlocks = new ConcurrentHashMap<>();

    public NumberSetting range = new NumberSetting("Range", 0, 500, 200, 10);

    public BlockESP() {
        super("BlockESP", "Highlights blocks. Use .sc blockesp to add blocks", Category.RENDER);
        addSetting(range);
    }

    @Override
    public void onEnable() {
        foundBlocks.clear();
        mc.worldRenderer.reload();
        Client.getInstance().eventManager.AddListener(RenderListener.class, this);
        Client.getInstance().eventManager.AddListener(BlockRenderListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        foundBlocks.clear();
        Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
        Client.getInstance().eventManager.RemoveListener(RenderListener.class, this);
        Client.getInstance().eventManager.RemoveListener(BlockRenderListener.class, this);
        Client.getInstance().eventManager.RemoveListener(BlockRenderListener.class, this);
        super.onDisable();
    }

    @Override
    public void OnBlockRender(BlockRenderEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (foundBlocks.size() > 100000) {
            foundBlocks.clear();
        }

        for (Map.Entry<BlockPos, BlockState> entry : foundBlocks.entrySet()) {
            if (entry.getKey() == event.GetBlockPos() && !Objects.equals(entry.getValue().getBlock().toString(), event.GetBlock().getBlock().toString())) {
                foundBlocks.remove(entry.getKey(), entry.getValue());
            }
            if (entry.getKey() == event.GetBlockPos() && Registries.BLOCK.getId(event.GetBlock().getBlock()).getPath().toLowerCase().equals("air")) {
                foundBlocks.remove(entry.getKey(), entry.getValue());
            }
        }
        if (shouldAdd(event.GetBlock())) {
            if (!foundBlocks.containsKey(event.GetBlockPos())) {
                foundBlocks.put(event.GetBlockPos(), event.GetBlock());
            }
        }
    }

    @Override
    public void OnRender(RenderEvent event) {
        if (mc.world == null || mc.player == null) return;
        for (Map.Entry<BlockPos, BlockState> entry : foundBlocks.entrySet()) {
            if (entry.getKey().getSquaredDistance(mc.player.getPos()) > (range.getValue() * range.getValue()) || !shouldAdd(entry.getValue())) { //shouldAdd also acts as a check for should render
                foundBlocks.remove(entry.getKey());
                continue;
            }

            Box box = new Box(entry.getKey());
            this.getRenderUtils().draw3DBox(event.GetMatrixStack(), box, blockEntries.get(Registries.BLOCK.getId(entry.getValue().getBlock()).getPath().toLowerCase()), 0.2f);
        }

    }

    private boolean shouldAdd(BlockState blockState) {
        return blockEntries.containsKey(Registries.BLOCK.getId(blockState.getBlock()).getPath().toLowerCase());
    }
}
