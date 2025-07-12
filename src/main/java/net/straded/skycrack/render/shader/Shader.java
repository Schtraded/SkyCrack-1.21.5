package net.straded.skycrack.render.shader;

import lombok.Getter;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL33.glCreateProgram;
import static org.lwjgl.opengl.GL33.glUseProgram;

public class Shader {
    @Getter
    private final int id;

    public Shader(String name) {
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
        glUniformMatrix4fv(glGetUniformLocation(id, name), false, matrix);
        unbind();
    }

    public void uniformValue2f(String name, float value1, float value2) {
        bind();
        glUniform2f(glGetUniformLocation(id, name), value1, value2);
        unbind();
    }
}
