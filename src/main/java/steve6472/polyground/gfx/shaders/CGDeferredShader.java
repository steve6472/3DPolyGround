package steve6472.polyground.gfx.shaders;

import steve6472.polyground.world.light.LightManager;
import steve6472.sge.gfx.shaders.GenericDeferredShader;
import steve6472.sge.gfx.shaders.Shader;
import steve6472.sge.main.MainApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.03.2020
 * Project: SJP
 *
 ***********************/
public class CGDeferredShader extends GenericDeferredShader<LightUniform>
{
	public static Type emission, emissionPos;

	public CGDeferredShader()
	{
		super(Shader.fromSource(readResource(MainApp.class.getResourceAsStream("/shaders/deferred/deferred.vs")), readResource(MainApp.class.getResourceAsStream("/shaders/deferred/deferred.fs"))));
	}

	@Override
	protected void createUniforms()
	{
		super.createUniforms();
		this.addUniform("gEmission", emission = new Type(EnumUniformType.INT_1));
		this.addUniform("gEmissionPos", emissionPos = new Type(EnumUniformType.INT_1));
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
