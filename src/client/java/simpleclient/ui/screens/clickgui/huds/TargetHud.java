package simpleclient.ui.screens.clickgui.huds;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import simpleclient.Client;
import simpleclient.module.ModuleManager;
import simpleclient.module.setting.TargetHUDSettings;
import simpleclient.ui.screens.HudElement;
import simpleclient.ui.screens.clickgui.Frame;
import simpleclient.utils.StringUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class TargetHud extends HudElement {

    public int range;
    public boolean displayHPAsHearts;
    public boolean noDisplayWhenNoPlayer;
    public boolean ignoreFriendly;

    public TargetHud(Frame parentFrame) {
        super(parentFrame, "targethud", "Target HUD", "Displays information about nearest player");
    }

    public void drawPlayer(DrawContext context, PlayerEntity player, int x, int y, int size, float mouseX, float mouseY) {
        if (player == null) {
            return;
        }

        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();

        // Set up the transformations
        matrixStack.translate((double)x, (double)y, 1050.0);
        matrixStack.scale(1.0F, 1.0F, -1.0F);
        matrixStack.scale((float)size, (float)size, (float)size);

        context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));

        // Calculate the rotations
        float yaw = -45.0F; // Starting rotation
        float pitch = 0.0F;

        float yawMouseDelta = (float)Math.atan((double)(mouseX / 40.0F));
        float pitchMouseDelta = (float)Math.atan((double)(mouseY / 40.0F));

        float combinedYaw = player.headYaw + yawMouseDelta * 20.0F;
        float combinedPitch = player.getPitch() + pitchMouseDelta * 20.0F;

        Quaternionf quaternion = RotationAxis.POSITIVE_X.rotationDegrees(combinedPitch);
        quaternion.mul(RotationAxis.POSITIVE_Y.rotationDegrees(combinedYaw));

        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));

        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitchMouseDelta * 20.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yawMouseDelta * 20.0F));

        EntityRenderDispatcher entityRenderDispatcher = mc.getEntityRenderDispatcher();
        entityRenderDispatcher.setRotation(quaternion);

        VertexConsumerProvider.Immediate immediate = context.getVertexConsumers();
        entityRenderDispatcher.render(player, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack, immediate, 15728880);

        immediate.draw();
        matrixStack.pop();
    }


    private boolean isValidEntity(Entity e) {
        if (!(e instanceof PlayerEntity)) return false;
        else {
            if (e != mc.player) {
                assert mc.player != null;
                return !Objects.equals(e.getName().getString(), mc.player.getName().getString());
            }
            return false;
        }
        //return true;
    }

    private static Color getHealthColor(int health) {
        if (health > 36) {
            health = 36;
        }
        if (health < 0) {
            health = 0;
        }

        int red = (int) (255 - (health * 7.0833));
        int green = 255 - red;

        return new Color(red, green, 0, 255);
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }

    @Override
    public void render(DrawContext context) {
        super.render(context);
        this.range = ((TargetHUDSettings) ModuleManager.INSTANCE.getModule("TargetHUD")).range.getValueInt();
        this.displayHPAsHearts = ((TargetHUDSettings) ModuleManager.INSTANCE.getModule("TargetHUD")).displayAsHearts.isEnabled();
        this.noDisplayWhenNoPlayer = ((TargetHUDSettings) ModuleManager.INSTANCE.getModule("TargetHUD")).noDisplayWhenNoPlayer.isEnabled();
        this.ignoreFriendly = ((TargetHUDSettings) ModuleManager.INSTANCE.getModule("TargetHUD")).ignoreFriendly.isEnabled();
        //context.drawText(mc.textRenderer, "This is a test message", originX, originY, Client.INSTANCE.specialHudCordsColor, true);
        int contentX = originX + 7;
        int contentY = originY + 7;
        int contentWidth = (originX + 105) - (contentX + 30);
        if (mc.world != null && Objects.requireNonNull(mc.player).age >= 10) {
            ArrayList<PlayerEntity> availablePlayerEntities = new ArrayList<PlayerEntity>();
            for (Entity entity : mc.world.getEntities()) {
                if (isValidEntity(entity)) {
                    availablePlayerEntities.add((PlayerEntity) entity);
                }
            }
            availablePlayerEntities.sort(Comparator.comparing(c -> mc.player.distanceTo(c)));

            if (!noDisplayWhenNoPlayer) {
                context.fill(originX, originY, originX + 110, originY + 50, new Color(79, 124, 196, 200).getRGB());
                context.fill(originX + 5, originY + 5, originX + 105, originY + 45, new Color(66, 135, 245, 200).getRGB());
            }
            if (availablePlayerEntities.isEmpty()) return;
            PlayerEntity playerEntity = availablePlayerEntities.get(0);
            if (noDisplayWhenNoPlayer) {
                if (playerEntity.distanceTo(mc.player) <= range) {
                    if (!(ignoreFriendly && Client.INSTANCE.doesFriendExist(playerEntity.getName().getString()))) {
                        context.fill(originX, originY, originX + 110, originY + 50, new Color(79, 124, 196, 200).getRGB());
                        context.fill(originX + 5, originY + 5, originX + 105, originY + 45, new Color(66, 135, 245, 200).getRGB());
                    }
                }
            }
            if (playerEntity.distanceTo(mc.player) <= range) {
                if (ignoreFriendly && Client.INSTANCE.doesFriendExist(playerEntity.getName().getString())) return;
                drawPlayer(context, playerEntity, contentX + 10, contentY + 37, 20, 0, 0);

                String playerName = playerEntity.getName().getString();
                Color playerColor = new Color(255, 255, 255);
                if (Client.INSTANCE.doesFriendExist(playerName)) {
                    playerColor = new Color(5, 247, 243);
                }
                //playerName = Client.INSTANCE.testValue;
                StringUtil.shrinkTextToWidthAndDraw(context, contentWidth, mc.textRenderer, playerName, contentX + 30, contentY, playerColor.getRGB(), true);

                int playerHealth = (int) (playerEntity.getHealth() + playerEntity.getAbsorptionAmount());
                Color healthColor = getHealthColor(playerHealth);
                if (displayHPAsHearts) {
                    StringUtil.shrinkTextToWidthAndDraw(context, contentWidth, mc.textRenderer, "Hearts: " + playerHealth / 2, contentX + 30, contentY + 10, healthColor.getRGB(), true);
                } else {
                    StringUtil.shrinkTextToWidthAndDraw(context, contentWidth, mc.textRenderer, "Health: " + playerHealth, contentX + 30, contentY + 10, healthColor.getRGB(), true);
                }

                StringUtil.shrinkTextToWidthAndDraw(context, contentWidth, mc.textRenderer, "Distance: " + (int) playerEntity.distanceTo(mc.player) + "b", contentX + 30, contentY + 20, Color.WHITE.getRGB(), true);

                if (Client.INSTANCE.doesFriendExist(playerName)) {
                    context.drawText(mc.textRenderer, "Friendly", contentX + 30, contentY + 30, playerColor.getRGB(), true);
                } else {
                    context.drawText(mc.textRenderer, "Not Friendly", contentX + 30, contentY + 30, Color.RED.getRGB(), true);
                }
            }
        }
    }
}
