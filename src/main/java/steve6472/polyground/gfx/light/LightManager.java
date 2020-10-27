package steve6472.polyground.gfx.light;

import steve6472.polyground.CaveGame;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.shaders.LightUniform;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.gfx.shaders.ILightShader;

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
	public static final int LIGHT_COUNT = 64;

	public static List<Light> lights;

	static
	{
		lights = new ArrayList<>(LIGHT_COUNT);

		for (int i = 0; i < LIGHT_COUNT; i++)
		{
			lights.add(new Light(i));
		}
	}

	public static void init()
	{

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
	public static Light addLight(EnumLightSource source, float x, float y, float z, float r, float g, float b, float constant, float linear, float quadratic, float dirX, float dirY, float dirZ, float cutOff)
	{
		for (Light l : lights)
		{
			if (l.isInactive())
			{
				setLight(l, source, x, y, z, r, g, b, constant, linear, quadratic, dirX, dirY, dirZ, cutOff);
				return l;
			}
		}

		return null;
	}

	public static Light replaceIdeal(EnumLightSource replaceType, float x, float y, float z, float r, float g, float b, float constant, float linear, float quadratic, float dirX, float dirY, float dirZ, float cutOff)
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
			setLight(lights.get(minInactive), replaceType, x, y, z, r, g, b, constant, linear, quadratic, dirX, dirY, dirZ, cutOff);
			return lights.get(minInactive);
		} else
		{
			setLight(lights.get(min), replaceType, x, y, z, r, g, b, constant, linear, quadratic, dirX, dirY, dirZ, cutOff);
			return lights.get(min);
		}
	}

	private static void setLight(Light light, EnumLightSource source, float x, float y, float z, float r, float g, float b, float constant, float linear, float quadratic, float dirX, float dirY, float dirZ, float cutOff)
	{
		light.setSource(source);
		light.setPosition(x, y, z);
		light.setColor(r, g, b);
		light.setAttenuation(constant, linear, quadratic);
		light.setSpotlight(dirX, dirY, dirZ, cutOff);
		light.setInactive(false);
	}

	public static void updateLights(ILightShader<LightUniform> shader, boolean update)
	{
		for (Light light : LightManager.lights)
		{
			if (light.shouldUpdateColor())
			{
				shader.setUniform(shader.getLights()[light.getIndex()].color, light.getColor().x, light.getColor().y, light.getColor().z);
				light.setUpdateColor(update);

				if (light.getSource() == EnumLightSource.SKY)
					shader.setUniform(shader.getLights()[light.getIndex()].color,
						light.getColor().x, light.getColor().y, light.getColor().z);

			}
			if (light.shouldUpdatePosition())
			{
				shader.setUniform(shader.getLights()[light.getIndex()].position, light.getX(), light.getY(), light.getZ());
				light.setUpdatePosition(update);
			}
			if (light.shouldUpdateAttenuation())
			{
				shader.setUniform(shader.getLights()[light.getIndex()].attenuation, light.getAttenuation().x(), light.getAttenuation().y(), light.getAttenuation().z());
				light.setUpdateAttenuation(update);
			}
			if (light.shouldUpdateSpotlight())
			{
				shader.setUniform(shader.getLights()[light.getIndex()].spotlight, light.getSpotlight().x, light.getSpotlight().y, light.getSpotlight().z, light.getSpotlight().w);
				light.setSpotlight(update);
			}
		}
	}

	public static void renderLights()
	{
		//		glDisable(GL_DEPTH_TEST);

		MainRender.shaders.mainShader.bind(CaveGame.getInstance().getCamera().getViewMatrix());
		BasicTessellator tess = CaveGame.getInstance().mainRender.basicTess;
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
