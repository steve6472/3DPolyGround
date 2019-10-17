package com.steve6472.polyground.block.model;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.BlockLoader;
import com.steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.CubeRegistry;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.sge.main.MainApp;
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
 * On date: 15.09.2019
 * Project: SJP
 *
 ***********************/
public class BlockModelLoader
{
	public List<Cube> loadModel(String name)
	{
		JSONObject json = null;

		try
		{
			json = new JSONObject(read(new File(MainApp.class.getResource("/models/" + name + ".json").getFile())));
		} catch (Exception e)
		{
			System.err.println("Could not load " + name);
			e.printStackTrace();
			CaveGame.getInstance().exit();
			System.exit(0);
		}

		if (json.has("parent"))
		{
			return createFromParent(json);
		}

		List<Cube> cubeList = new ArrayList<>();

		for (String cubeType : CubeRegistry.getKeys())
		{
			if (!json.has(cubeType)) continue;

			JSONArray array = json.getJSONArray(cubeType);
			for (int i = 0; i < array.length(); i++)
			{
				JSONObject c = array.getJSONObject(i);
				AABBf aabb = JsonHelper.createAABB(c);
				Cube cube = CubeRegistry.createCube(cubeType, aabb);

				JSONObject faces = c.getJSONObject("faces");
				for (EnumFace ef : EnumFace.getFaces())
				{
					face(faces, ef, cube);
				}

				cube.loadFromJson(c);
				cubeList.add(cube);
			}
		}

		return cubeList;
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

	private static void face(JSONObject json, EnumFace face, Cube cube)
	{
		if (json.has(face.getName()))
		{
			JSONObject faceJson = json.getJSONObject(face.getName());
			CubeFace cf = new CubeFace(cube, face);
			cf.loadFromJSON(faceJson);
			fillMissingProperties(cf);
			if (AutoUVFaceProperty.check(cf))
			{
				cf.getProperty(FaceRegistry.uv).autoUV(cube, face);
			}

			TextureFaceProperty texture = cf.getProperty(FaceRegistry.texture);
			if (!texture.isReference())
			{
				BlockLoader.putTexture(texture.getTexture());
				texture.setTextureId(BlockLoader.getTextureId(texture.getTexture()));
			}

			cube.setFace(face, cf);
		}
	}

	private List<Cube> createFromParent(JSONObject json)
	{
		List<Cube> parents = loadModel(json.getString("parent"));

		for (Cube cube : parents)
		{
			cube.loadFromParent(json, cube);
		}

		return parents;
	}

	private String read(File f)
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
