/*package net.straded.skycrack.render.shader

import com.mojang.blaze3d.opengl.GlStateManager
import com.mojang.blaze3d.platform.TextureUtil
import lombok.Getter
import lombok.SneakyThrows
import lombok.experimental.UtilityClass
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStarted
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.GlImportProcessor
import net.minecraft.resource.ResourceFactory
import net.minecraft.util.Identifier
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.lwjgl.opengl.GL20
import org.lwjgl.system.MemoryUtil
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

@UtilityClass
object ShaderManager {
    @Getter
    private var positionColorShader: Shader? = null

    fun init() {
        ClientLifecycleEvents.CLIENT_STARTED.register(ClientStarted { client: MinecraftClient? -> loadShader() })
    }

    private fun loadShader() {
        positionColorShader = Shader("position_color")
    }

    fun loadShaderProgram(name: String?, type: ShaderType): Int {
        try {
            var file_present = true
            val resourceFactory: ResourceFactory = MinecraftClient.getInstance().getResourceManager()
            val resource = resourceFactory.getResource(Identifier.of("renderer", "shader/" + name + type.fileExtension))

            val source = StringBuilder()

            val i = GL20.glCreateShader(type.glType)

            val inputStream = resource.get().getInputStream()

            /*if (resource.isPresent()) {
                inputStream = resource.get().getInputStream();
            }*/
            BufferedReader(InputStreamReader(inputStream)).forEachLine
            run {
                source.append(it).append("\n")
            }

            if (resource.isPresent()) {
                GlStateManager.glShaderSource(i, object : GlImportProcessor() {
                    @SneakyThrows
                    override fun loadImport(inline: Boolean, name: String?): String? {
                        return IOUtils.toString(resource.get().getInputStream(), StandardCharsets.UTF_8)
                    }
                }.readSource(readResourceAsString(resource.get().getInputStream())).toString())
            } else file_present = false
            GL20.glCompileShader(i)
            if (GL20.glGetShaderi(i, GL20.GL_COMPILE_STATUS) == 0 || !file_present) {
                val shaderInfo = StringUtils.trim(GL20.glGetShaderInfoLog(i, 32768))
                throw IOException("Couldn't compile " + type.name + " program (" + name + ") : " + shaderInfo)
            }
            return i
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return 0
    }

    private fun readResourceAsString(inputStream: InputStream): String? {
        var byteBuffer: ByteBuffer? = null
        try {
            byteBuffer = TextureUtil.readResource(inputStream)
            val i = byteBuffer.position()
            byteBuffer.rewind()
            return MemoryUtil.memASCII(byteBuffer, i)
        } catch (ignored: IOException) {
        } finally {
            if (byteBuffer != null) {
                MemoryUtil.memFree(byteBuffer)
            }
        }
        return null
    }

    enum class ShaderType(name: String, fileExtension: String, glType: Int) {
        VERTEX("vertex", ".vsh", GL20.GL_VERTEX_SHADER),
        FRAGMENT("fragment", ".fsh", GL20.GL_FRAGMENT_SHADER);

        private val name: String?
        private val fileExtension: String?
        private val glType: Int

        init {
            this.name = name
            this.fileExtension = fileExtension
            this.glType = glType
        }
    }
}*/