package simpleclient.utils;

import simpleclient.Client;
import simpleclient.ui.Color;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RendererUtil extends Utils {
	
	public static final Matrix4f lastProjMat = new Matrix4f();

	public static final Matrix4f lastModMat = new Matrix4f();

	public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();

	private static final MinecraftClient client = mc;
	
	final float ROUND_QUALITY = 10;
	
	public static Vec3d worldSpaceToScreenSpace( Vec3d pos) {
		Camera camera = client.getEntityRenderDispatcher().camera;
		int displayHeight = client.getWindow().getHeight();
		int[] viewport = new int[4];
		GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
		Vector3f target = new Vector3f();

		double deltaX = pos.x - camera.getPos().x;
		double deltaY = pos.y - camera.getPos().y;
		double deltaZ = pos.z - camera.getPos().z;

		Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1.f).mul(
				lastWorldSpaceMatrix);

		Matrix4f matrixProj = new Matrix4f(lastProjMat);
		Matrix4f matrixModel = new Matrix4f(lastModMat);

		matrixProj.mul(matrixModel)
				.project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport,
						target);

		return new Vec3d(target.x / client.getWindow().getScaleFactor(),
				(displayHeight - target.y) / client.getWindow().getScaleFactor(), target.z);
	}
	
	public static boolean screenSpaceCoordinateIsVisible(Vec3d pos) {
		return pos != null && pos.z > -1 && pos.z < 1;
	}
	
	public void drawBox(MatrixStack matrixStack, float x, float y, float width, float height, float r, float g, float b, float alpha) {
		Color c = new Color(r,g, b);
		drawBox(matrixStack, x, y,width, height, c, alpha);
	}
	
	public void drawBox(MatrixStack matrixStack, float x, float y, float width, float height, Color color, float alpha) {
		if (!Client.INSTANCE.renderObjects) return;

		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
		bufferBuilder.vertex(matrix, x, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).next();

		tessellator.draw();
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void drawRoundedBox(MatrixStack matrixStack, float x, float y, float width, float height, float radius, int r, int g, int b, float alpha) {
		if (!Client.INSTANCE.renderObjects) return;
		Color c= new Color(r,g,b);
		drawRoundedBox(matrixStack, x, y, width, height, radius, c, alpha);
	}
	
	public void drawRoundedBox(MatrixStack matrixStack, float x, float y, float width, float height, float radius, Color color, float alpha) {
		if (!Client.INSTANCE.renderObjects) return;
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		
		bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION);
		
		buildFilledArc(bufferBuilder, matrix, x + radius, y + radius, radius, 180.0f, 90.0f);
		buildFilledArc(bufferBuilder, matrix, x + width - radius, y + radius, radius, 270.0f, 90.0f);
		buildFilledArc(bufferBuilder, matrix, x + width - radius, y + height - radius, radius, 0.0f, 90.0f);
		buildFilledArc(bufferBuilder, matrix, x + radius, y + height - radius, radius, 90.0f, 90.0f);
		
		// |---
		bufferBuilder.vertex(matrix, x + radius, y, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius, y, 0).next();
		bufferBuilder.vertex(matrix, x + radius, y + radius, 0).next();
		
		// ---|
		bufferBuilder.vertex(matrix, x + radius, y + radius, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius, y, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius, y + radius, 0).next();
		
		// _||
		bufferBuilder.vertex(matrix, x + width - radius, y + radius, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + radius, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius, y + height - radius, 0).next();
		
		// |||
		bufferBuilder.vertex(matrix, x + width, y + radius, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height - radius, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius, y + height - radius, 0).next();
		
		/// __|
		bufferBuilder.vertex(matrix, x + width - radius, y + height - radius, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius, y + height, 0).next();
		bufferBuilder.vertex(matrix, x + radius, y + height - radius, 0).next();
		
		// |__
		bufferBuilder.vertex(matrix, x + radius, y + height - radius, 0).next();
		bufferBuilder.vertex(matrix, x + radius, y + height, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius, y + height, 0).next();
		
		// |||
		bufferBuilder.vertex(matrix, x + radius, y + height - radius, 0).next();
		bufferBuilder.vertex(matrix, x, y + height - radius, 0).next();
		bufferBuilder.vertex(matrix, x , y + radius, 0).next();
		
		/// ||-
		bufferBuilder.vertex(matrix, x , y + radius, 0).next();
		bufferBuilder.vertex(matrix, x + radius , y + radius, 0).next();
		bufferBuilder.vertex(matrix, x + radius, y + height - radius, 0).next();

		/// |-/
		bufferBuilder.vertex(matrix, x + radius , y + radius, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius , y + radius, 0).next();
		bufferBuilder.vertex(matrix, x + radius , y + height - radius, 0).next();
		
		/// /_|
		bufferBuilder.vertex(matrix, x + radius , y + height - radius, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius , y + height - radius, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius , y + radius, 0).next();
		
		tessellator.draw();
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void drawRoundedOutline(MatrixStack matrixStack, float x, float y, float width, float height, float radius, Color color, float alpha) {
		if (!Client.INSTANCE.renderObjects) return;
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
		
		// Top Left Arc and Top
		buildArc(bufferBuilder, matrix, x + radius, y + radius, radius, 180.0f, 90.0f);
		bufferBuilder.vertex(matrix, x + radius, y, 0).next();
		bufferBuilder.vertex(matrix, x + width - radius, y, 0).next();
		
		// Top Right Arc and Right
		buildArc(bufferBuilder, matrix, x + width - radius, y + radius, radius, 270.0f, 90.0f);
		bufferBuilder.vertex(matrix, x + width, y + radius, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height - radius, 0).next();
		
		// Bottom Right
		buildArc(bufferBuilder, matrix, x + width - radius, y + height - radius, radius, 0.0f, 90.0f);
		bufferBuilder.vertex(matrix, x + width - radius, y + height, 0).next();
		bufferBuilder.vertex(matrix, x + radius, y + height, 0).next();
		
		// Bottom Left
		buildArc(bufferBuilder, matrix, x + radius, y + height - radius, radius, 90.0f, 90.0f);
		bufferBuilder.vertex(matrix, x, y + height - radius, 0).next();
		bufferBuilder.vertex(matrix, x, y + radius, 0).next();
		
		tessellator.draw();
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	public void drawOutlinedBox(MatrixStack matrixStack, float x, float y, float width, float height, int r, int g, int b, float alpha) {
		if (!Client.INSTANCE.renderObjects) return;
		Color c = new Color(r, g, b);
		drawOutlinedBox(matrixStack, x, y, width, height, c, alpha);
	}
	
	public void drawOutlinedBox(MatrixStack matrixStack, float x, float y, float width, float height, Color color,
			float alpha) {

		if (!Client.INSTANCE.renderObjects) return;
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, x, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).next();

		tessellator.draw();
		
		RenderSystem.setShaderColor(0, 0, 0, alpha);
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, x, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y, 0).next();

		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void drawLine(MatrixStack matrixStack, float x1, float y1, float x2, float y2, Color color, float alpha) {
		if (!Client.INSTANCE.renderObjects) return;
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, x1, y1, 0).next();
		bufferBuilder.vertex(matrix, x2, y2, 0).next();

		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void drawOutline(MatrixStack matrixStack, float x, float y, float width, float height) {
		if (!Client.INSTANCE.renderObjects) return;

		RenderSystem.setShaderColor(0, 0, 0, 1);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, x, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y, 0).next();
		bufferBuilder.vertex(matrix, x + width, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y + height, 0).next();
		bufferBuilder.vertex(matrix, x, y, 0).next();

		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void draw3DBox(MatrixStack matrixStack, Box box, Color color, float alpha) {
		if (!Client.INSTANCE.renderObjects) return;
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), 1.0f);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();

		tessellator.draw();
		
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), alpha);
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();

		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();

		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ).next();
		bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ).next();
		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void drawLine3D(MatrixStack matrixStack, Vec3d pos, Vec3d pos2, Color color) {
		if (!Client.INSTANCE.renderObjects) return;
		RenderSystem.setShaderColor(color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), 1.0f);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

		bufferBuilder.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z).next();
		bufferBuilder.vertex(matrix, (float) pos2.x, (float) pos2.y, (float) pos2.z).next();

		tessellator.draw();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void drawString(DrawContext drawContext, String text, float x, float y, Color color) {
		if (!Client.INSTANCE.renderObjects) return;
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale(2.0f, 2.0f, 1.0f);
		matrixStack.translate(-x / 2, -y / 2, 0.0f);
		drawContext.drawText(mc.textRenderer, text, (int)x, (int)y, color.getColorAsInt(), false);
		matrixStack.pop();
	}
	
	public void drawString(DrawContext drawContext, String text, float x, float y, int color) {
		if (!Client.INSTANCE.renderObjects) return;
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale(2.0f, 2.0f, 1.0f);
		matrixStack.translate(-x / 2, -y / 2, 0.0f);
		drawContext.drawText(mc.textRenderer, text, (int)x, (int)y, color, false);
		matrixStack.pop();
	}
	
	public void drawStringWithScale(DrawContext drawContext, String text, float x, float y, Color color, float scale) {
		if (!Client.INSTANCE.renderObjects) return;
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale(scale, scale, 1.0f);
		if (scale > 1.0f) {
			matrixStack.translate(-x / scale, -y / scale, 0.0f);
		} else {
			matrixStack.translate((x / scale) - x, (y * scale) - y, 0.0f);
		}
		drawContext.drawText(mc.textRenderer, text, (int)x, (int)y, color.getColorAsInt(), false);
		matrixStack.pop();
	}
	
	public void drawStringWithScale(DrawContext drawContext, String text, float x, float y, int color, float scale) {
		if (!Client.INSTANCE.renderObjects) return;
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale(scale, scale, 1.0f);
		if (scale > 1.0f) {
			matrixStack.translate(-x / scale, -y / scale, 0.0f);
		} else {
			matrixStack.translate(x / scale, y * scale, 0.0f);
		}
		drawContext.drawText(mc.textRenderer, text, (int)x, (int)y, color, false);
		matrixStack.pop();
	}
	
	private void buildFilledArc(BufferBuilder bufferBuilder, Matrix4f matrix, float x, float y, float radius, float startAngle, float sweepAngle) {
		if (!Client.INSTANCE.renderObjects) return;
		double roundedInterval = (sweepAngle / ROUND_QUALITY);
		
				for(int i = 0; i < ROUND_QUALITY; i++) {
					double angle = Math.toRadians(startAngle + (i * roundedInterval));
					double angle2 = Math.toRadians(startAngle + ((i + 1) * roundedInterval));
					float radiusX1 = (float)(Math.cos(angle) * radius);
					float radiusY1 = (float)Math.sin(angle) * radius;
					float radiusX2 = (float)Math.cos(angle2) * radius;
					float radiusY2 = (float)Math.sin(angle2) * radius;
					
					bufferBuilder.vertex(matrix, x, y, 0).next();
					bufferBuilder.vertex(matrix, x + radiusX1, y + radiusY1, 0).next();
					bufferBuilder.vertex(matrix, x + radiusX2, y + radiusY2, 0).next();
				}
		}
	
	private void buildArc(BufferBuilder bufferBuilder, Matrix4f matrix, float x, float y, float radius, float startAngle, float sweepAngle) {
		if (!Client.INSTANCE.renderObjects) return;
		double roundedInterval = (sweepAngle / ROUND_QUALITY);
		
		for(int i = 0; i < ROUND_QUALITY; i++) {
			double angle = Math.toRadians(startAngle + (i * roundedInterval));
			float radiusX1 = (float) (Math.cos(angle) * radius);
			float radiusY1 = (float)Math.sin(angle) * radius;

			bufferBuilder.vertex(matrix, x + radiusX1, y + radiusY1, 0).next();
		}
	}
	
	public static void applyRenderOffset(MatrixStack matrixStack) {
		if (!Client.INSTANCE.renderObjects) return;
		Vec3d camPos = client.getBlockEntityRenderDispatcher().camera.getPos();
		matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
	}

	public void applyRegionalRenderOffset(MatrixStack matrixStack) {
		if (!Client.INSTANCE.renderObjects) return;
		Vec3d camPos = client.getBlockEntityRenderDispatcher().camera.getPos();
		BlockPos camBlockPos = client.getBlockEntityRenderDispatcher().camera.getBlockPos();

		int regionX = (camBlockPos.getX() >> 9) * 512;
		int regionZ = (camBlockPos.getZ() >> 9) * 512;

		matrixStack.translate(regionX - camPos.x, -camPos.y, regionZ - camPos.z);
	}
}
