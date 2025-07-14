package net.straded.skycrack;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.straded.skycrack.render.RenderHelper;
import net.straded.skycrack.render.RenderSystem;
import net.straded.skycrack.render.buffer.WorldBuffer;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class SkyCrackClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RenderSystem.init();
        WorldRenderEvents.LAST.register(context -> {
            WorldBuffer buffer = RenderHelper.startTri(context);
            RenderHelper.drawTri(buffer, 0, 100, 0, 0, 101, 0, 0, 100, 1, new Color(255, 255, 0, 100));
            RenderHelper.endTri(buffer);
        });
    }
}

