package steve6472.polyground.gfx.shaders;

import steve6472.polyground.gfx.light.LightManager;
import steve6472.sge.gfx.shaders.AbstractLightShader3D;
import steve6472.sge.gfx.shaders.Shader;
import steve6472.sge.main.MainApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.09.2020
 * Project: CaveGame
 *
 ***********************/
public class EntityShader extends AbstractLightShader3D<LightUniform>
{
	public static Type ATLAS, NORMAL_MATRIX, SHADE;

	public EntityShader()
	{
		super(Shader.fromSource(readResource(stream("/shaders/entity/entity.vs")), readResource(stream("/shaders/entity/entity.fs"))));
	}

	@Override
	protected void createUniforms()
	{
		addUniform("atlas", ATLAS = new Type(EnumUniformType.INT_1));
		addUniform("normalMatrix", NORMAL_MATRIX = new Type(EnumUniformType.MAT_3));
		addUniform("shade", SHADE = new Type(EnumUniformType.FLOAT_1));
	}

	private static InputStream stream(String name)
	{
		return MainApp.class.getResourceAsStream(name);
	}

	private static String readResource(InputStream stream)
	{
		return read(new BufferedReader(new InputStreamReader(stream)));
	}

	private static String read(BufferedReader reader)
	{
		try
		{
			StringBuilder stringer = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null)
			{
				if (line.equals("const int LIGHT_COUNT = 0;"))
					line = "const int LIGHT_COUNT = " + LightManager.LIGHT_COUNT + ";";

				stringer.append(line).append("\n");
			}

			return stringer.toString();
		} catch (IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}
