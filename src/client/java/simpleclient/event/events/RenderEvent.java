package simpleclient.event.events;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.util.math.MatrixStack;
import simpleclient.event.listeners.AbstractListener;
import simpleclient.event.listeners.RenderListener;

public class RenderEvent extends AbstractEvent {
	MatrixStack matrixStack; 
	float partialTicks;
	
	public MatrixStack GetMatrixStack() {
		return matrixStack;
	}
	public float GetPartialTicks() {
		return partialTicks;
	}
	
	public RenderEvent(MatrixStack matrixStack, float partialTicks) {
		this.matrixStack = matrixStack;
		this.partialTicks = partialTicks;
	}
	
	@Override
	public void Fire(CopyOnWriteArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : listeners) {
			RenderListener renderListener = (RenderListener) listener;
			renderListener.OnRender(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<RenderListener> GetListenerClassType() {
		return RenderListener.class;
	}
}