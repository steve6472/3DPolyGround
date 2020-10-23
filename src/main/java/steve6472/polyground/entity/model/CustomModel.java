package steve6472.polyground.entity.model;

import steve6472.polyground.entity.model.loader.Element;
import steve6472.polyground.entity.model.loader.Loader;
import steve6472.polyground.entity.model.loader.Outliner;
import steve6472.polyground.entity.model.loader.OutlinerElement;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.gfx.stack.StackUtil;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class CustomModel
{
	private final OutlinerElement[] elements;
	private final HashMap<String, OutlinerElement> assignElements;

	public CustomModel(String name)
	{
		elements = Loader.load(name);
		assignElements = Loader.assignElements(elements);
	}

	public void update()
	{

	}

	public void render(Stack stack)
	{
		stack.pushMatrix();
		stack.scale(1f / 16f);
		for (OutlinerElement el : elements)
		{
			if (el instanceof Outliner outliner)
			{
				render(stack, outliner);
			} else if (el instanceof Element element)
			{
				render(stack, element);
			}
		}
		stack.popMatrix();
	}

	private void render(Stack stack, OutlinerElement el)
	{
		stack.pushMatrix();
		stack.translate(el.positionX, el.positionY, el.positionZ);
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
			render(stack, element);
		}
		stack.popMatrix();
	}

	private void render(Stack stack, Element element)
	{
		StackUtil.rectShade(stack, element.fromX, element.fromY, element.fromZ, element.toX - element.fromX, element.toY - element.fromY, element.toZ - element.fromZ);
	}

	public OutlinerElement[] getElements()
	{
		return elements;
	}

	public HashMap<String, OutlinerElement> getAnimElements()
	{
		return assignElements;
	}
}