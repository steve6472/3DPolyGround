package steve6472.polyground.entity.interfaces;

import steve6472.polyground.entity.EntityManager;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public interface IAdvancedRender
{
	void render(Stack stack);

	default void position(Stack stack)
	{
		if (this instanceof IPosition3f p)
		{
			stack.translate(p.getPosition());
		}
	}

	default void rotation(Stack stack)
	{
		if (this instanceof IRotation rot)
		{
			EntityManager.QUATERNIONF.rotateXYZ(rot.getRotations().x, rot.getRotations().y, rot.getRotations().z);
			stack.rotate(EntityManager.QUATERNIONF);
		}
	}
}
