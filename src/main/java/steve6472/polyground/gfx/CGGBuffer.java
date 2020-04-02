package steve6472.polyground.gfx;

import org.lwjgl.opengl.GL30;
import steve6472.sge.gfx.GBuffer;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.WindowSizeEvent;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.03.2020
 * Project: CaveGame
 *
 ***********************/
public class CGGBuffer extends GBuffer
{
	public int emission, emissionPos;

	public CGGBuffer(MainApp main)
	{
		super(main);
	}

	public CGGBuffer(int width, int height)
	{
		super(width, height);
		bindFrameBuffer();

		this.emission = createTextureAttachment(3, GL_RGBA, GL_UNSIGNED_BYTE);
		this.emissionPos = createTextureAttachment(4, GL_RGB16F, GL_FLOAT);

		unbindCurrentFrameBuffer();
	}

	@Event
	public void resize(WindowSizeEvent e)
	{
		this.width = e.getWidth();
		this.height = e.getHeight();

		resize(texture, GL_RGBA, GL_UNSIGNED_BYTE);
		resize(position, GL_RGB16F, GL_FLOAT);
		resize(normal, GL_RGB16F, GL_FLOAT);
		resize(emission, GL_RGBA, GL_UNSIGNED_BYTE);
		resize(emissionPos, GL_RGB16F, GL_FLOAT);
		glBindTexture(GL_TEXTURE_2D, 0);

		glBindRenderbuffer(GL_RENDERBUFFER, depth);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
	}

	private void resize(int texture, int internalFormat, int type)
	{
		GL30.glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, GL_RGBA, type, (ByteBuffer) null);
	}

	@Override
	protected int getDrawBufferCount()
	{
		return 5;
	}
}
