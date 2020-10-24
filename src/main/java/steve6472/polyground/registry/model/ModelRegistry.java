package steve6472.polyground.registry.model;

import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.entity.model.Model;
import steve6472.polyground.gfx.model.Element;
import steve6472.polyground.gfx.model.Outliner;
import steve6472.polyground.gfx.model.OutlinerElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class ModelRegistry
{
	private static final List<Model> models = new ArrayList<>();

	public static final Model CHEST = register("chest");
	public static final Model DOOR = register("door");
	public static final Model AMETHINE_CORE = register("amethine_core");
	public static final Model PLAYER = register("player");
	public static final Model TEST = register("test");

	public static Model register(String path)
	{
		Model model = new Model(path);
		models.add(model);
		return model;
	}

	public static void init()
	{

	}

	public static void assignTextures()
	{
		for (Model model : models)
		{
			for (OutlinerElement element : model.getElements())
			{
				recursiveAssignTextures(element);
			}
		}
	}

	private static void recursiveAssignTextures(OutlinerElement e)
	{
		if (e instanceof Outliner outliner)
		{
			for (OutlinerElement child : outliner.children)
			{
				recursiveAssignTextures(child);
			}
		} else if (e instanceof Element element)
		{
			faces(element);
		}
	}

	private static void faces(Element element)
	{
		if (element.north != null) face(element.north);
		if (element.east != null) face(element.east);
		if (element.south != null) face(element.south);
		if (element.west != null) face(element.west);
		if (element.up != null) face(element.up);
		if (element.down != null) face(element.down);
	}

	private static void face(Element.Face face)
	{
		Rectangle r = BlockAtlas.getTexture(face.texture());
		float x = r.x;
		float y = r.y;
		float w = r.width;
		float h = r.height;
		float texel = BlockAtlas.getAtlas().getTexel();
		face.setU0((x + w * face.getU0()) * texel);
		face.setV0((y + h * face.getV0()) * texel);
		face.setU1((x + w * face.getU1()) * texel);
		face.setV1((y + h * face.getV1()) * texel);
	}

	public static void reload()
	{
		models.forEach(Model::reload);
	}
}
