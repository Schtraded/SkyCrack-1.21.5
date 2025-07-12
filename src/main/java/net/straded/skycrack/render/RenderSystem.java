package net.straded.skycrack.render;

import lombok.experimental.UtilityClass;
import net.straded.skycrack.render.buffer.BufferManager;
import net.straded.skycrack.render.shader.ShaderManager;

@UtilityClass
public class RenderSystem {
    public void init() {
        ShaderManager.init();
        BufferManager.init();
    }
}
