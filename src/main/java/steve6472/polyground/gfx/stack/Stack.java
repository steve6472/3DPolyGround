package steve6472.polyground.gfx.stack;

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Stack extends Matrix4fStack
{
	private final EntityTess entityTess;
	private final LineTess lineTess;

	public Stack()
	{
		super(16);
		entityTess = new EntityTess(this);
		lineTess = new LineTess(this);
	}

	public void render(Matrix4f view)
	{
		entityTess.render(view);
		lineTess.render(view);
	}

	public void reset()
	{
		entityTess.reset();
		lineTess.reset();
	}

	public EntityTess getEntityTess()
	{
		return entityTess;
	}

	public LineTess getLineTess()
	{
		return lineTess;
	}
}
