package net.straded.skycrack.render.shader;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL33.glCreateProgram;
import static org.lwjgl.opengl.GL33.glUseProgram;

public class Shader {
    @Getter
    private final int id;

    public Shader(String name) throws IOException {
        int v = ShaderManager.loadShaderProgram(name, ShaderManager.ShaderType.VERTEX);
        int f = ShaderManager.loadShaderProgram(name, ShaderManager.ShaderType.FRAGMENT);
        this.id = glCreateProgram();
        glAttachShader(id, v);
        glAttachShader(id, f);
        glLinkProgram(id);
    }

    public void bind() {
        glUseProgram(id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void uniformMatrix4f(String name, FloatBuffer matrix) {
        bind();

        int location = glGetUniformLocation(id, name);

        if (location == -1) {
            System.err.println("Uniform '" + name + "' not found or optimized out.");
        } else {
            glUniformMatrix4fv(location, false, matrix);
        }

        //glUniformMatrix4fv(glGetUniformLocation(id, name), false, matrix);
        unbind();
    }

    public void uniformValue2f(String name, float value1, float value2) {
        bind();
        glUniform2f(glGetUniformLocation(id, name), value1, value2);
        unbind();
    }
}
