package steve6472.polyground.entity.model.loader;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.block.model.ModelLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Loader
{
	private static final float TO_RAD = 0.017453292519943295f;

	public static OutlinerElement[] load(String modelName)
	{
		JSONObject model = new JSONObject(ModelLoader.read(new File("custom_models/entity/" + modelName + ".bbmodel")));
		HashMap<UUID, Element> elements = loadElements(model.getJSONArray("elements"));

		return loadOutliner(model.getJSONArray("outliner"), elements);
	}

	public static HashMap<String, OutlinerElement> assignElements(OutlinerElement[] elements)
	{
		HashMap<String, OutlinerElement> elementMap = new HashMap<>();

		for (OutlinerElement el : elements)
		{
			if (el instanceof Outliner o)
			{
				elementMap.put(o.name, o);
				assignElements(o, elementMap);
			}
		}

		return elementMap;
	}

	private static void assignElements(Outliner outliner, HashMap<String, OutlinerElement> elementMap)
	{
		for (OutlinerElement el : outliner.children)
		{
			if (el instanceof Outliner o)
			{
				elementMap.put(o.name, o);
				assignElements(o, elementMap);
			}
		}
	}

	private static HashMap<UUID, Element> loadElements(JSONArray elements)
	{
		HashMap<UUID, Element> map = new HashMap<>();

		for (Object element : elements)
		{
			if (element instanceof JSONObject jsonElement)
			{
				loadElement(map, jsonElement);
			}
		}

		return map;
	}

	private static void loadElement(HashMap<UUID, Element> map, JSONObject json)
	{
		Element element = new Element();
		element.uuid = loadCommon(element, json);

		JSONArray from = json.getJSONArray("from");
		JSONArray to = json.getJSONArray("to");

		element.fromX = from.getFloat(0);
		element.fromY = from.getFloat(1);
		element.fromZ = from.getFloat(2);

		element.toX = to.getFloat(0);
		element.toY = to.getFloat(1);
		element.toZ = to.getFloat(2);

		if (json.has("north")) element.north = loadFace(json.getJSONObject("north"));
		if (json.has("east")) element.east = loadFace(json.getJSONObject("east"));
		if (json.has("south")) element.south = loadFace(json.getJSONObject("south"));
		if (json.has("west")) element.west = loadFace(json.getJSONObject("west"));
		if (json.has("up")) element.up = loadFace(json.getJSONObject("up"));
		if (json.has("down")) element.down = loadFace(json.getJSONObject("down"));

		map.put(element.uuid, element);
	}

	private static UUID loadCommon(OutlinerElement element, JSONObject json)
	{
		element.name = json.getString("name");
		JSONArray origin = json.getJSONArray("origin");

		element.originX = origin.getFloat(0);
		element.originY = origin.getFloat(1);
		element.originZ = origin.getFloat(2);

		if (json.has("rotation"))
		{
			JSONArray rotation = json.getJSONArray("rotation");
			element.rotationX = rotation.getFloat(0) * TO_RAD;
			element.rotationY = rotation.getFloat(1) * TO_RAD;
			element.rotationZ = rotation.getFloat(2) * TO_RAD;
		}

		element.scaleX = 1;
		element.scaleY = 1;
		element.scaleZ = 1;

		UUID uuid = UUID.fromString(json.getString("uuid"));
		element.uuid = uuid;
		return uuid;
	}

	private static Element.Face loadFace(JSONObject json)
	{
		JSONArray uv = json.getJSONArray("uv");
		return new Element.Face(uv.getFloat(0), uv.getFloat(1), uv.getFloat(2), uv.getFloat(3), json.optInt("texture", -1));
	}


	private static OutlinerElement[] loadOutliner(JSONArray outliner, HashMap<UUID, Element> elements)
	{
		List<OutlinerElement> outlinerElements = new ArrayList<>();
		for (Object o : outliner)
		{
			outlinerElements.add(loadOutlinerElement(o, elements));
		}
		return outlinerElements.toArray(OutlinerElement[]::new);
	}

	private static OutlinerElement loadOutlinerElement(Object obj, HashMap<UUID, Element> elements)
	{
		if (obj instanceof String string)
		{
			UUID uuid = UUID.fromString(string);
			return elements.get(uuid);
		}
		else if (obj instanceof JSONObject jsonObj)
		{
			Outliner outliner = new Outliner();
			loadCommon(outliner, jsonObj);
			outliner.children = loadOutliner(jsonObj.getJSONArray("children"), elements);
			return outliner;
		} else
		{
			throw new IllegalArgumentException(obj.toString());
		}
	}
}
