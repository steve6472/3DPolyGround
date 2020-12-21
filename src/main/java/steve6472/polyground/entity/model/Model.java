package steve6472.polyground.entity.model;

import steve6472.polyground.gfx.model.Element;
import steve6472.polyground.gfx.model.Loader;
import steve6472.polyground.gfx.model.Outliner;
import steve6472.polyground.gfx.model.OutlinerElement;
import steve6472.polyground.gfx.stack.EntityTess;
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

	private EntityTess vert0(Element.Face face, EntityTess tess)
	{
		switch (face.getRotation())
		{
			case 0 -> tess.uv(face.getU0(), face.getV0());
			case 1 -> tess.uv(face.getU0(), face.getV1());
			case 2 -> tess.uv(face.getU1(), face.getV1());
			case 3 -> tess.uv(face.getU1(), face.getV0());
		}
		tess.endVertex();
		return tess;
	}

	private EntityTess vert1(Element.Face face, EntityTess tess)
	{
//		tess.uv(face.getU0(), face.getV1());
		switch (face.getRotation())
		{
			case 0 -> tess.uv(face.getU0(), face.getV1());
			case 1 -> tess.uv(face.getU1(), face.getV1());
			case 2 -> tess.uv(face.getU1(), face.getV0());
			case 3 -> tess.uv(face.getU0(), face.getV0());
		}
		tess.endVertex();
		return tess;
	}

	private EntityTess vert2(Element.Face face, EntityTess tess)
	{
//		tess.uv(face.getU1(), face.getV1());
		switch (face.getRotation())
		{
			case 0 -> tess.uv(face.getU1(), face.getV1());
			case 1 -> tess.uv(face.getU1(), face.getV0());
			case 2 -> tess.uv(face.getU0(), face.getV0());
			case 3 -> tess.uv(face.getU0(), face.getV1());
		}
		tess.endVertex();
		return tess;
	}

	private EntityTess vert3(Element.Face face, EntityTess tess)
	{
//		tess.uv(face.getU1(), face.getV0());
		switch (face.getRotation())
		{
			case 0 -> tess.uv(face.getU1(), face.getV0());
			case 1 -> tess.uv(face.getU0(), face.getV0());
			case 2 -> tess.uv(face.getU0(), face.getV1());
			case 3 -> tess.uv(face.getU1(), face.getV1());
		}
		tess.endVertex();
		return tess;
	}

	private void rect(Stack stack, Element element)
	{
		float x = element.fromX;
		float y = element.fromY;
		float z = element.fromZ;

		float w = element.toX - x;
		float h = element.toY - y;
		float d = element.toZ - z;

		EntityTess tess = stack.getEntityTess();

		if (element.up != null)
		{
			tess.normal(0, 1, 0);

			vert3(element.up, tess.pos(x + w, y + h, z));
			vert0(element.up, tess.pos(x, y + h, z));
			vert1(element.up, tess.pos(x, y + h, z + d));

			vert1(element.up, tess.pos(x, y + h, z + d));
			vert2(element.up, tess.pos(x + w, y + h, z + d));
			vert3(element.up, tess.pos(x + w, y + h, z));
		}

		if (element.down != null)
		{
			tess.normal(0, -1, 0);

			vert0(element.down, tess.pos(x, y, z + d));
			vert1(element.down, tess.pos(x, y, z));
			vert2(element.down, tess.pos(x + w, y, z));

			vert2(element.down, tess.pos(x + w, y, z));
			vert3(element.down, tess.pos(x + w, y, z + d));
			vert0(element.down, tess.pos(x, y, z + d));
		}


		if (element.north != null)
		{
			tess.normal(1, 0, 0);

			vert0(element.north, tess.pos(x + w, y + h, z));
			vert1(element.north, tess.pos(x + w, y, z));
			vert2(element.north, tess.pos(x, y, z));

			vert2(element.north, tess.pos(x, y, z));
			vert3(element.north, tess.pos(x, y + h, z));
			vert0(element.north, tess.pos(x + w, y + h, z));
		}


		if (element.east != null)
		{
			tess.normal(0, 0, 1);

			vert0(element.east, tess.pos(x + w, y + h, z + d));
			vert1(element.east, tess.pos(x + w, y, z + d));
			vert2(element.east, tess.pos(x + w, y, z));

			vert2(element.east, tess.pos(x + w, y, z));
			vert3(element.east, tess.pos(x + w, y + h, z));
			vert0(element.east, tess.pos(x + w, y + h, z + d));
		}


		if (element.south != null)
		{
			tess.normal(-1, 0, 0);

			vert0(element.south, tess.pos(x, y + h, z + d));
			vert1(element.south, tess.pos(x, y, z + d));
			vert2(element.south, tess.pos(x + w, y, z + d));

			vert2(element.south, tess.pos(x + w, y, z + d));
			vert3(element.south, tess.pos(x + w, y + h, z + d));
			vert0(element.south, tess.pos(x, y + h, z + d));
		}


		if (element.west != null)
		{
			tess.normal(0, 0, -1);

			vert0(element.west, tess.pos(x, y + h, z));
			vert1(element.west, tess.pos(x, y, z));
			vert2(element.west, tess.pos(x, y, z + d));

			vert2(element.west, tess.pos(x, y, z + d));
			vert3(element.west, tess.pos(x, y + h, z + d));
			vert0(element.west, tess.pos(x, y + h, z));
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