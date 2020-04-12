package steve6472.polyground.generator.cond;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.main.util.ColorUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.04.2020
 * Project: CaveGame
 *
 ***********************/
public class PboPicker
{
	private int pbo;
	private ByteBuffer buffer;
	private byte[] result;

	public PboPicker()
	{
		buffer = BufferUtils.createByteBuffer(4);
		result = new byte[4];
		pbo = createPbo();
	}

	private int createPbo()
	{
		final int pbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL21.GL_PIXEL_PACK_BUFFER, pbo);
		GL15.glBufferData(GL21.GL_PIXEL_PACK_BUFFER, 4, GL15.GL_STREAM_READ);
		GL15.glBindBuffer(GL21.GL_PIXEL_PACK_BUFFER, 0);
		return pbo;
	}

	public byte[] getColorFromFrameBuffer(int x, int y, DepthFrameBuffer frameBuffer)
	{
		frameBuffer.bindToRead(0);

		GL15.glBindBuffer(GL21.GL_PIXEL_PACK_BUFFER, pbo);
		GL11.glReadPixels(x, y, 1, 1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0L);

		(buffer = GL15.glMapBuffer(GL21.GL_PIXEL_PACK_BUFFER, GL_READ_ONLY, buffer)).get(result);
		GL15.glUnmapBuffer(GL21.GL_PIXEL_PACK_BUFFER);
		buffer.flip();

		GL15.glBindBuffer(GL21.GL_PIXEL_PACK_BUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, 0);

		return result;
	}

	public int decode2(byte[] bytes)
	{
		return ColorUtil.getColor(bytes[0] & 0xff, bytes[1] & 0xff, bytes[2] & 0xff, bytes[3] & 0xff);
	}
}