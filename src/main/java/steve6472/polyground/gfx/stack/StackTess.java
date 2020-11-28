package steve6472.polyground.gfx.stack;

import org.joml.Matrix4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 14.11.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class StackTess
{
	protected final Stack stack;

	public StackTess(Stack stack)
	{
		this.stack = stack;
	}

	public abstract StackTess endVertex();

	public abstract void render(Matrix4f view);

	public abstract void reset();
}
