package net.straded.skycrack;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class SkyCrackClient implements ClientModInitializer {
    private static final Identifier EXAMPLE_LAYER = Identifier.of(SkyCrack.MOD_ID, "hud-example-layer");

    @Override
    public void onInitializeClient() {
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerBefore(IdentifiedLayer.CHAT, EXAMPLE_LAYER, SkyCrackClient::render));
        //HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerBefore(IdentifiedLayer.CHAT, MAP_LAYER, SkyCrackClient::render));
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        Text text = Text.translatable("info_hud");
        context.drawText(MinecraftClient.getInstance().textRenderer, text, 0, 0, 0xFFFFFF, false);
        int rectangleX = 0;
        int rectangleY = 0;
        int rectangleWidth = 100;
        int rectangleHeight = 100;

        context.fill(rectangleX, rectangleY, rectangleX + rectangleWidth, rectangleY + rectangleHeight, 0xFF0000FF);
        context.drawVerticalLine(rectangleX + rectangleWidth / 2, rectangleY, rectangleY + rectangleHeight, 0xFF00FF00);
        context.drawHorizontalLine(rectangleX, rectangleX + rectangleWidth - 1, rectangleX + rectangleWidth / 2, 0xFF00FF00);
        context.drawBorder(rectangleX, rectangleY, rectangleWidth, rectangleHeight, 0xFFFF0000);
        // Starting and ending points of the diagonal line
        //Vec3d cameraPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();

    }
}

