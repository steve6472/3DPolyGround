package steve6472.polyground.block;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.block.model.BlockModelLoader;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2020
 * Project: CaveGame
 *
 ***********************/
public class CubeImporter
{
	public static void main(String[] args)
	{
		StringBuilder builder = new StringBuilder();
		final String texture = "stick";
		final boolean useUv = true;

		builder.append(",\n(BlockModelBuilder.create(\"stick_1\").modelPath(\"stick\")\n");

		JSONObject json = new JSONObject(BlockModelLoader.read(new File("model.json")));
		JSONArray elements = json.getJSONArray("elements");
		for (int i = 0; i < elements.length(); i++)
		{
			JSONObject element = elements.getJSONObject(i);
			JSONArray from = element.getJSONArray("from");
			JSONArray to = element.getJSONArray("to");
			if (!useUv)
			{
				builder
					.append(".addCube(CubeBuilder.create().size(")
					.append(num(from.getFloat(0))).append(", ")
					.append(num(from.getFloat(1))).append(", ")
					.append(num(from.getFloat(2))).append(", ")
					.append(num(to.getFloat(0) - from.getFloat(0))).append(", ")
					.append(num(to.getFloat(1) - from.getFloat(1))).append(", ")
					.append(num(to.getFloat(2) - from.getFloat(2)))
					.append(").face(FaceBuilder.create().texture(\"")
					.append(texture).append("\").autoUv()))\n");
			} else
			{
				AABBf box = new AABBf(from.getFloat(0), from.getFloat(1), from.getFloat(2), to.getFloat(0), to.getFloat(1), to.getFloat(2));

				Matrix4f rotMat = new Matrix4f();
				rotMat.translate(8f, 0, 8f);
				rotMat.rotate((float) Math.toRadians(90), 0, 1, 0);
				rotMat.translate(-8f, 0, -8f);
				box.transform(rotMat);

				builder
					.append(".addCube(CubeBuilder.create().min(")
					.append(num(box.minX)).append(", ")
					.append(num(box.minY)).append(", ")
					.append(num(box.minZ)).append(").max(")
					.append(num(box.maxX)).append(", ")
					.append(num(box.maxY)).append(", ")
					.append(num(box.maxZ))
					.append(")");

				if (element.has("faces"))
				{
					for (String s : element.getJSONObject("faces").keySet())
					{
						JSONObject face = element.getJSONObject("faces").getJSONObject(s);
						builder.append(".face(FaceBuilder.create().texture(\"").append(face.getString("texture").substring(1)).append("\")");
						if (face.has("uv"))
						{
							JSONArray uv = face.getJSONArray("uv");
							builder
								.append(".uv(")
								.append(num(uv.getFloat(0))).append(", ")
								.append(num(uv.getFloat(1))).append(", ")
								.append(num(uv.getFloat(2))).append(", ")
								.append(num(uv.getFloat(3))).append(")");
						}
						builder.append(", EnumFace.").append(s.toUpperCase()
						);
						builder.append(")");
					}
				}

					builder.append(")\n");
			}
		}
		builder.append(")");
		System.out.println(builder.toString());
	}

	private static String num(float f)
	{
		if (f == Math.floor(f))
			return Float.toString(f).substring(0, Float.toString(f).length() - 2);
		else
			return f + "f";
	}
}
