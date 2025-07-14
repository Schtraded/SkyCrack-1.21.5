package net.straded.skycrack.render.shader;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlImportProcessor;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL33.*;

@UtilityClass
public class ShaderManager {
    @Getter
    private static Shader positionColorShader;

    public static void init() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            try {
                loadShader();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void loadShader() throws IOException {
        positionColorShader = new Shader("position_color");
    }

    public static int loadShaderProgram(String name, ShaderType type) throws IOException {
        boolean file_present = true;
        ResourceFactory resourceFactory = MinecraftClient.getInstance().getResourceManager();
        Optional<Resource> resource = resourceFactory.getResource(Identifier.of("renderer", "shader/" + name + type.fileExtension));

        val source = new StringBuilder();

        InputStream inputStream = resource.get().getInputStream();
            /*if (resource.isPresent()) {
                inputStream = resource.get().getInputStream();
            }*/

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int shaderID = glCreateShader(type.glType);

        GlStateManager.glShaderSource(shaderID, source.toString());
        GlStateManager.glCompileShader(shaderID);

        if (GlStateManager.glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {

            String errorLog = StringUtils.trim(GlStateManager.glGetShaderInfoLog(shaderID, 32768));

            throw new IOException("Failed to compile shader " + type.name + " program (" + name + ") : " + errorLog + ". Features that utilise this " +
                    "shader will not work correctly, if at all");
                /*val errorMessage = "Failed to compile shader $fileName${type.extension}. Features that utilise this " +
                        "shader will not work correctly, if at all";
                val errorLog = StringUtils.trim(glGetShaderInfoLog(shaderID, 32768));

                if (inWorld()) {
                    ErrorManager.logErrorWithData(
                            Exception("Shader compilation error."),
                            errorMessage,
                            "GLSL Compilation Error:\n" to errorLog,
                            )
                } else {
                    ChatUtils.consoleLog("$errorMessage $errorLog")
                }

                return -1;*/
        }
        return shaderID;
    }

    public enum ShaderType {
        VERTEX("vertex", ".vsh", GL_VERTEX_SHADER),
        FRAGMENT("fragment", ".fsh", GL_FRAGMENT_SHADER);;
        private final String name;
        private final String fileExtension;
        private final int glType;

        ShaderType(String name, String fileExtension, int glType) {
            this.name = name;
            this.fileExtension = fileExtension;
            this.glType = glType;
        }
    }
}