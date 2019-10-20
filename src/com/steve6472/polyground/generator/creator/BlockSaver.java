package com.steve6472.polyground.generator.creator;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.10.2019
 * Project: SJP
 *
 ***********************/
class BlockSaver
{
	static void save(BlockEntry entry)
	{
		JSONObject mainJson = new JSONObject();

		JSONArray cubeArray = new JSONArray();
		for (Cube c : entry.getModel().getCubes())
		{
			cubeArray.put(cubeJson(c));
		}

		mainJson.put("cubes", cubeArray);

		File compiled = new File("creator\\blocks");
		if (!compiled.exists()) compiled.mkdirs();

		save(new File(compiled, entry.getName() + ".json"), mainJson.toString(4));
	}

	private static JSONObject cubeJson(Cube cube)
	{
		JSONObject json = new JSONObject();

		JSONArray from = new JSONArray();
		from.put(cube.getAabb().minX * 16f).put(cube.getAabb().minY * 16f).put(cube.getAabb().minZ * 16f);

		JSONArray to = new JSONArray();
		to.put(cube.getAabb().maxX * 16f).put(cube.getAabb().maxY * 16f).put(cube.getAabb().maxZ * 16f);

		json.put("from", from);
		json.put("to", to);

		if (!cube.isHitbox()) json.put("isHitbox", cube.isHitbox());
		if (!cube.isCollisionBox()) json.put("isCollisionBox", cube.isCollisionBox());

		json.put("name", ((ICreatorCube) cube).getName());

		JSONObject faces = new JSONObject();

		for (EnumFace f : EnumFace.getFaces())
		{
			if (cube.getFace(f) != null)
			{
				JSONObject face = cubeFaceJson(cube.getFace(f));
				if (face != null)
					faces.put(f.getName(), face);
			}
		}

		if (!faces.isEmpty())
			json.put("faces", faces);

		return json;
	}

	private static JSONObject cubeFaceJson(CubeFace face)
	{
		if (!face.hasProperty(FaceRegistry.texture) || face.getProperty(FaceRegistry.texture).isBlank()) return null;

		JSONObject json = new JSONObject();
		face.saveToJSON(json);

		return json;
	}

	private static void save(File file, String s)
	{
		try (PrintWriter out = new PrintWriter(file))
		{
			out.println(s);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
