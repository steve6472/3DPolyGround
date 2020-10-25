package steve6472.polyground.entity.model;

import steve6472.polyground.gfx.model.Element;
import steve6472.polyground.gfx.model.Loader;
import steve6472.polyground.gfx.model.Outliner;
import steve6472.polyground.gfx.model.OutlinerElement;
import steve6472.polyground.gfx.stack.Stack;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Model
{
	private static final float RAD_90 = (float) (-Math.PI / 2.0);

	private OutlinerElement[] elements;
	private HashMap<String, OutlinerElement> animElements;
	private final String name;

	public Model(String name)
	{
		this.name = name;
		reload();
	}

	public void reload()
	{
		elements = Loader.load(name);
		animElements = Loader.assignElements(elements);
	}

	public void render(Stack stack)
	{
		stack.scale(1f / 16f);
		stack.rotateY(RAD_90);
		for (OutlinerElement el : elements)
		{
			if (el instanceof Outliner outliner)
			{
				render(stack, outliner);
			} else if (el instanceof Element element)
			{
				rect(stack, element);
			}
		}
	}

	private void render(Stack stack, OutlinerElement el)
	{
		stack.pushMatrix();
		stack.translate(-el.positionX, el.positionY, el.positionZ);
		stack.translate(el.originX, el.originY, el.originZ);
		stack.rotateXYZ(el.rotationX, el.rotationY, el.rotationZ);
		stack.translate(-el.originX, -el.originY, -el.originZ);
		stack.scale(el.scaleX, el.scaleY, el.scaleZ);
		if (el instanceof Outliner outliner)
		{
			for (OutlinerElement child : outliner.children)
			{
				render(stack, child);
			}
		} else if (el instanceof Element element)
		{
			rect(stack, element);
		}
		stack.popMatrix();
	}

	private void rect(Stack stack, Element element)
	{
		float x = element.fromX;
		float y = element.fromY;
		float z = element.fromZ;

		float w = element.toX - x;
		float h = element.toY - y;
		float d = element.toZ - z;

		if (element.up != null)
		{
			stack.normal(0, 1, 0);

			stack
				.pos(x + w, y + h, z)
				.uv(element.up.getU1(), element.up.getV0()) // 3
				.endVertex();
			stack
				.pos(x, y + h, z)
				.uv(element.up.getU0(), element.up.getV0()) // 0
				.endVertex();
			stack
				.pos(x, y + h, z + d)
				.uv(element.up.getU0(), element.up.getV1()) // 1
				.endVertex();

			stack
				.pos(x, y + h, z + d)
				.uv(element.up.getU0(), element.up.getV1()) // 1
				.endVertex();
			stack
				.pos(x + w, y + h, z + d)
				.uv(element.up.getU1(), element.up.getV1()) // 2
				.endVertex();
			stack
				.pos(x + w, y + h, z)
				.uv(element.up.getU1(), element.up.getV0()) // 3
				.endVertex();
		}

		if (element.down != null)
		{
			stack.normal(0, -1, 0);

			stack
				.pos(x, y, z + d)
				.uv(element.down.getU0(), element.down.getV0()) // 0
				.endVertex();
			stack
				.pos(x, y, z)
				.uv(element.down.getU0(), element.down.getV1()) // 1
				.endVertex();
			stack
				.pos(x + w, y, z)
				.uv(element.down.getU1(), element.down.getV1()) // 2
				.endVertex();

			stack
				.pos(x + w, y, z)
				.uv(element.down.getU1(), element.down.getV1()) // 2
				.endVertex();
			stack
				.pos(x + w, y, z + d)
				.uv(element.down.getU1(), element.down.getV0()) // 3
				.endVertex();
			stack
				.pos(x, y, z + d)
				.uv(element.down.getU0(), element.down.getV0()) // 0
				.endVertex();
		}


		if (element.north != null)
		{
			stack.normal(1, 0, 0);

			stack
				.pos(x + w, y + h, z)
				.uv(element.north.getU0(), element.north.getV0())
				.endVertex();
			stack
				.pos(x + w, y, z)
				.uv(element.north.getU0(), element.north.getV1())
				.endVertex();
			stack
				.pos(x, y, z)
				.uv(element.north.getU1(), element.north.getV1())
				.endVertex();


			stack
				.pos(x, y, z)
				.uv(element.north.getU1(), element.north.getV1())
				.endVertex();
			stack
				.pos(x, y + h, z)
				.uv(element.north.getU1(), element.north.getV0())
				.endVertex();
			stack
				.pos(x + w, y + h, z)
				.uv(element.north.getU0(), element.north.getV0())
				.endVertex();
		}


		if (element.east != null)
		{
			stack.normal(0, 0, 1);

			stack
				.pos(x + w, y + h, z + d)
				.uv(element.east.getU0(), element.east.getV0())
				.endVertex();
			stack
				.pos(x + w, y, z + d)
				.uv(element.east.getU0(), element.east.getV1())
				.endVertex();
			stack
				.pos(x + w, y, z)
				.uv(element.east.getU1(), element.east.getV1())
				.endVertex();


			stack
				.pos(x + w, y, z)
				.uv(element.east.getU1(), element.east.getV1())
				.endVertex();
			stack
				.pos(x + w, y + h, z)
				.uv(element.east.getU1(), element.east.getV0())
				.endVertex();
			stack
				.pos(x + w, y + h, z + d)
				.uv(element.east.getU0(), element.east.getV0())
				.endVertex();
		}


		if (element.south != null)
		{
			stack.normal(-1, 0, 0);

			stack
				.pos(x, y + h, z + d)
				.uv(element.south.getU0(), element.south.getV0())
				.endVertex();
			stack
				.pos(x, y, z + d)
				.uv(element.south.getU0(), element.south.getV1())
				.endVertex();
			stack
				.pos(x + w, y, z + d)
				.uv(element.south.getU1(), element.south.getV1())
				.endVertex();

			stack
				.pos(x + w, y, z + d)
				.uv(element.south.getU1(), element.south.getV1())
				.endVertex();
			stack
				.pos(x + w, y + h, z + d)
				.uv(element.south.getU1(), element.south.getV0())
				.endVertex();
			stack
				.pos(x, y + h, z + d)
				.uv(element.south.getU0(), element.south.getV0())
				.endVertex();
		}


		if (element.west != null)
		{
			stack.normal(0, 0, -1);

			stack
				.pos(x, y + h, z)
				.uv(element.west.getU0(), element.west.getV0()) // 0
				.endVertex();
			stack
				.pos(x, y, z)
				.uv(element.west.getU0(), element.west.getV1()) // 1
				.endVertex();
			stack
				.pos(x, y, z + d)
				.uv(element.west.getU1(), element.west.getV1()) // 2
				.endVertex();

			stack
				.pos(x, y, z + d)
				.uv(element.west.getU1(), element.west.getV1()) // 2
				.endVertex();
			stack
				.pos(x, y + h, z + d)
				.uv(element.west.getU1(), element.west.getV0()) // 3
				.endVertex();
			stack
				.pos(x, y + h, z)
				.uv(element.west.getU0(), element.west.getV0()) // 0
				.endVertex();
		}

	}

	public String getName()
	{
		return name;
	}

	public OutlinerElement[] getElements()
	{
		return elements;
	}

	public HashMap<String, OutlinerElement> getAnimElements()
	{
		return animElements;
	}
}