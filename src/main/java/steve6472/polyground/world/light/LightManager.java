package steve6472.polyground.world.light;

import steve6472.polyground.CaveGame;
import steve6472.polyground.gfx.shaders.LightUniform;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.gfx.shaders.GenericDeferredShader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glPointSize;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.03.2020
 * Project: SJP
 *
 ***********************/
public class LightManager
{
	public static final int LIGHT_COUNT = 256;

	public static List<Light> lights;

	static
	{
		lights = new ArrayList<>(LIGHT_COUNT);

		for (int i = 0; i < LIGHT_COUNT; i++)
		{
			lights.add(new Light(i));
		}

		addLight(EnumLightSource.SKY, -16, 48, 32, 1, 1, 1, 1, 0, 0);
		addLight(EnumLightSource.SKY, 32, -48, -16, 0.2f, 0.2f, 0.2f, 1, 0, 0);
	}

	public static boolean removeLight(EnumLightSource source, float x, float y, float z)
	{
		for (Light l : lights)
		{
			if (!l.isInactive())
			{
				if (l.getSource() == source && l.getX() == x && l.getY() == y && l.getZ() == z)
				{
					l.setInactive(true);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @return new Light
	 */
	public static Light addLight(EnumLightSource source, float x, float y, float z, float r, float g, float b, float constant, float linear, float quadratic)
	{
		for (Light l : lights)
		{
			if (l.isInactive())
			{
				setLight(l, source, x, y, z, r, g, b, constant, linear, quadratic);
				return l;
			}
		}

		return null;
	}

	public static Light replaceIdeal(EnumLightSource replaceType, float x, float y, float z, float r, float g, float b, float constant, float linear, float quadratic)
	{
		long last = System.currentTimeMillis();
		long lastInactive = System.currentTimeMillis();
		int min = -1;
		int minInactive = -1;
		boolean foundInactive = false;

		for (int i = 0; i < lights.size(); i++)
		{
			Light l = lights.get(i);

			if (l.getSource() != replaceType && !l.isInactive())
				continue;

			if (l.getLastUpdate() < last)
			{
				last = l.getLastUpdate();
				min = i;
			}

			if (l.getLastUpdate() < lastInactive && l.isInactive())
			{
				lastInactive = l.getLastUpdate();
				minInactive = i;
				foundInactive = true;
			}
		}

		if (min == -1 && minInactive == -1)
		{
			return null;
		}

		if (foundInactive)
		{
			setLight(lights.get(minInactive), replaceType, x, y, z, r, g, b, constant, linear, quadratic);
			return lights.get(minInactive);
		} else
		{
			setLight(lights.get(min), replaceType, x, y, z, r, g, b, constant, linear, quadratic);
			return lights.get(min);
		}
	}

	private static void setLight(Light light, EnumLightSource source, float x, float y, float z, float r, float g, float b, float constant, float linear, float quadratic)
	{
		light.setSource(source);
		light.setPosition(x, y, z);
		light.setColor(r, g, b);
		light.setAttenuation(constant, linear, quadratic);
		light.setInactive(false);
	}

	private static float lastShade = 0;

	public static void updateLights(GenericDeferredShader<LightUniform> shader)
	{
		boolean updateSky = CaveGame.getInstance().getWorld().shade != lastShade;
		lastShade = CaveGame.getInstance().getWorld().shade;

		for (Light light : lights)
		{
			if (light.shouldUpdateColor())
			{
				shader.setUniform(shader.lights[light.getIndex()].color, light.getColor().x, light.getColor().y, light.getColor().z);
				light.setUpdateColor(false);

				if (light.getSource() == EnumLightSource.SKY)
					shader.setUniform(shader.lights[light.getIndex()].color,
						light.getColor().x * lastShade, light.getColor().y * lastShade, light.getColor().z * lastShade);

			}
			if (light.shouldUpdatePosition())
			{
				shader.setUniform(shader.lights[light.getIndex()].position, light.getX(), light.getY(), light.getZ());
				light.setUpdatePosition(false);
			}
			if (light.shouldUpdateAttenuation())
			{
				shader.setUniform(shader.lights[light.getIndex()].attenuation, light.getAttenuation().x(), light.getAttenuation().y(), light.getAttenuation().z());
				light.setUpdateAttenuation(false);
			}

			if (updateSky && light.getSource() == EnumLightSource.SKY)
				shader.setUniform(shader.lights[light.getIndex()].color,
					light.getColor().x * lastShade, light.getColor().y * lastShade, light.getColor().z * lastShade);
		}
	}



	public static void renderLights()
	{
		//		glDisable(GL_DEPTH_TEST);

		CaveGame.shaders.mainShader.bind(CaveGame.getInstance().getCamera().getViewMatrix());
		BasicTessellator tess = CaveGame.getInstance().basicTess;
		tess.begin(LightManager.LIGHT_COUNT);

		for (Light light : LightManager.lights)
		{
			tess.pos(light.getPosition()).color(light.getColor().x, light.getColor().y, light.getColor().z, light.isInactive() ? 0.0f : 1.0f).endVertex();
		}

		glPointSize(4f);
		tess.loadPos(0);
		tess.loadColor(1);
		tess.draw(Tessellator.POINTS);
		tess.disable(0, 1);
		glPointSize(1f);

		//		glEnable(GL_DEPTH_TEST);
	}
}
