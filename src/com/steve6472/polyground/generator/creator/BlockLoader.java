package com.steve6472.polyground.generator.creator;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.BlockModel;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.JsonHelper;
import com.steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.VisibleFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import org.joml.AABBf;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.10.2019
 * Project: SJP
 *
 ***********************/
class BlockLoader
{
	static BlockModel load(File f, BlockEntry entry)
	{
		JSONObject mainJson = new JSONObject(read(f));
		entry.setParent(mainJson.optBoolean("isParent", false));

		List<Cube> cubeList = new ArrayList<>();

		int cubeCount = 0;

		JSONArray array = mainJson.getJSONArray("cubes");
		for (int i = 0; i < array.length(); i++)
		{
			JSONObject c = array.getJSONObject(i);
			AABBf aabb = JsonHelper.createAABB(c);
			Cube cube = new CreatorCube(aabb);
			ICreatorCube creatorCube = ((ICreatorCube) cube);

			if (c.has("name"))
				creatorCube.setName(c.getString("name"));
			else
				creatorCube.setName("Unnamed Cube");

			creatorCube.setIndex(cubeCount++);

			if (c.has("faces"))
			{
				JSONObject faces = c.getJSONObject("faces");
				for (EnumFace ef : EnumFace.getFaces())
				{
					face(faces, ef, cube);
				}
			} else
			{
				for (EnumFace ef : EnumFace.getFaces())
				{
					face(null, ef, cube);
				}
			}

			cube.loadFromJson(c);
			cubeList.add(cube);
		}

		return new BlockModel(cubeList);
	}

	private static void fillMissingProperties(CubeFace face)
	{
		for (String key : FaceRegistry.getKeys())
		{
			if (!face.hasProperty(key))
			{
				face.addProperty(FaceRegistry.createProperty(key));
			}
		}
	}

	private static void printData(CubeFace face)
	{
		System.out.println("Loaded face " + face.getFace().getName());

		for (String key : FaceRegistry.getKeys())
		{
			System.out.println(face.getProperty(FaceRegistry.getEntry(key)).toString());
		}

		System.out.println("\n");
	}

	private static void face(JSONObject json, EnumFace face, Cube cube)
	{
		if (json != null && json.has(face.getName()))
		{
			JSONObject faceJson = json.getJSONObject(face.getName());
			CubeFace cf = new CubeFace(cube, face);
			cf.loadFromJSON(faceJson);
			fillMissingProperties(cf);
			if (AutoUVFaceProperty.check(cf))
			{
				cf.getProperty(FaceRegistry.uv).autoUV(cube, face);
			}
			if (cf.getProperty(FaceRegistry.texture).isReference())
			{
				cf.getProperty(FaceRegistry.texture).setTextureId(-1);
			}
			cube.setFace(face, cf);
		} else
		{
			CubeFace cf = new CubeFace(cube, face);
			cf.addProperty(new VisibleFaceProperty(false));
			fillMissingProperties(cf);
			cube.setFace(face, cf);
		}
	}


	private static String read(File f)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(f));

			boolean endOfTheFile = false;
			while (!endOfTheFile)
			{
				String line = br.readLine();

				if (line == null)
				{
					endOfTheFile = true;
				} else
				{
					sb.append(line);
				}
			}

			br.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
}








