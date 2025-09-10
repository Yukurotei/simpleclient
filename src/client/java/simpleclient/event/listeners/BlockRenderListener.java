package simpleclient.event.listeners;

import simpleclient.event.events.BlockRenderEvent;

public interface BlockRenderListener extends AbstractListener {
    public abstract void OnBlockRender(BlockRenderEvent event);
}
