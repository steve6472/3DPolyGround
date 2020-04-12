package steve6472.polyground.generator.cond;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import steve6472.polyground.gfx.shaders.MainShader;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.game.Camera;
import steve6472.sge.main.util.ColorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.04.2020
 * Project: CaveGame
 *
 ***********************/
public class View
{
	MainShader shader;
	Camera camera;
	MainApp mainApp;
	BasicTessellator basicTess;
	ConditionGenerator conditionGenerator;

	boolean bottom, middle, top;

	private final DepthFrameBuffer screen;
	private final Picker picker;

	static int screenWidth = 320, screenHeight = 320;

	public View(ConditionGenerator conditionGenerator)
	{
		this.conditionGenerator = conditionGenerator;
		this.mainApp = conditionGenerator.getMainApp();
		basicTess = new BasicTessellator();

		shader = new MainShader();
		shader.bind();
		shader.setProjection(new Matrix4f().ortho(-1, 1, -1, 1, 0.01f, 64f));

		camera = new Camera();
		camera.setYaw((float) Math.toRadians(45));
		camera.setPitch((float) Math.toRadians(-45));
		camera.calculateOrbit(0, 0, 0, 16f);
		camera.updateViewMatrix();

		screen = new DepthFrameBuffer(screenWidth, screenHeight);

		picker = new Picker(this);
	}

	private int x, y, px, py;

	public void tick()
	{
		if (mainApp.isMMBHolded())
		{
			if (this.px == -1 && this.py == -1)
			{
				this.px = this.x - mainApp.getMouseX();
				this.py = this.y - mainApp.getMouseY();
			} else
				{
				this.x = mainApp.getMouseX() + this.px;
				this.y = mainApp.getMouseY() + this.py;
			}

			camera.headOrbit(x, y, 0.3f, 0, 0, 0, 16f);
			camera.updateViewMatrix();
		} else
		{
			px = -1;
			py = -1;
		}
	}

	public void render(EnumState[][][] states)
	{
		screen.bindFrameBuffer(screenWidth, screenHeight);
		DepthFrameBuffer.clearCurrentBuffer();

		shader.bind(camera.getViewMatrix());
		shader.setTransformation(new Matrix4f().scale(0.2f));
		shader.setProjection(new Matrix4f().ortho(-1, 1, -1, 1, 0.01f, 64f));

		int count = 0;
		if (top) count++;
		if (middle) count++;
		if (bottom) count++;

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		{
			List<Cube> cubes = new ArrayList<>(count * 9);

			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 3; j++)
				{
					for (int k = 0; k < 3; k++)
					{
						if (i == 1 && j == 1 && k == 1)
							cubes.add(new Cube(camera, i - 1, j - 1, k - 1, 1, 1, 1, 1, true));
						else
						{
							Vector4f c = ColorUtil.getVector4Color(states[i][j][k].color);
							cubes.add(new Cube(camera, i * 2 - 2, j * 2 - 2, k * 2 - 2, c.x, c.y, c.z, c.w, true));
						}
					}
				}
			}

			Collections.sort(cubes);

			basicTess.begin(12 * count * 27);

			for (Cube c : cubes)
			{
				renderCube(c, basicTess);
			}

			GL11.glLineWidth(2);
			basicTess.loadPos(0);
			basicTess.loadColor(1);
			basicTess.draw(Tessellator.TRIANGLES);
			basicTess.disable(0, 1);
			GL11.glLineWidth(1f);
		}

		basicTess.begin(24 * 9 * count - 1);

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (i != 1 || j != 1 || k != 1)
					{
						renderCubeOutline(i * 2 - 2, j * 2 - 2, k * 2 - 2, 1, 1, 1, 1, basicTess);
					}
				}
			}
		}
		shader.setTransformation(new Matrix4f().scale(0.201f));
		GL11.glLineWidth(2);
		basicTess.loadPos(0);
		basicTess.loadColor(1);
		basicTess.draw(Tessellator.LINES);
		basicTess.disable(0, 1);
		GL11.glLineWidth(1f);

		glDisable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
		screen.unbindCurrentFrameBuffer(mainApp);

		SpriteRender.renderSpriteInverted(0, mainApp.getHeight() / 2 - screenHeight / 2, screenWidth, screenHeight, screen.texture);

		picker.render();
	}

	public static void renderCubeOutline(float x, float y, float z, float r, float g, float b, float a, BasicTessellator tess)
	{
		renderOutline(-1f + x, -1f + y, -1f + z, 1f + x, 1f + y, 1f + z, r, g, b, a, tess);
	}

	public static void renderOutline(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float r, float g, float b, float a, BasicTessellator tess)
	{
		tess.color(r, g, b, a);

		tess.pos(minX, minY, minZ).endVertex();
		tess.pos(maxX, minY, minZ).endVertex();
		tess.pos(minX, minY, minZ).endVertex();
		tess.pos(minX, minY, maxZ).endVertex();
		tess.pos(minX, minY, maxZ).endVertex();
		tess.pos(maxX, minY, maxZ).endVertex();
		tess.pos(maxX, minY, minZ).endVertex();
		tess.pos(maxX, minY, maxZ).endVertex();

		tess.pos(minX, minY, minZ).endVertex();
		tess.pos(minX, maxY, minZ).endVertex();
		tess.pos(maxX, minY, minZ).endVertex();
		tess.pos(maxX, maxY, minZ).endVertex();
		tess.pos(minX, minY, maxZ).endVertex();
		tess.pos(minX, maxY, maxZ).endVertex();
		tess.pos(maxX, minY, maxZ).endVertex();
		tess.pos(maxX, maxY, maxZ).endVertex();

		tess.pos(minX, maxY, minZ).endVertex();
		tess.pos(maxX, maxY, minZ).endVertex();
		tess.pos(minX, maxY, minZ).endVertex();
		tess.pos(minX, maxY, maxZ).endVertex();
		tess.pos(minX, maxY, maxZ).endVertex();
		tess.pos(maxX, maxY, maxZ).endVertex();
		tess.pos(maxX, maxY, minZ).endVertex();
		tess.pos(maxX, maxY, maxZ).endVertex();
	}

	public static void renderCube(Cube cube, BasicTessellator tess)
	{
		for (int i = 0; i < cube.tris.size(); i++)
		{
			Tri t = cube.tris.get(i);
			tess.color(t.r, t.g, t.b, t.a);
			tess.pos(t.x0, t.y0, t.z0).endVertex();
			tess.pos(t.x1, t.y1, t.z1).endVertex();
			tess.pos(t.x2, t.y2, t.z2).endVertex();
		}
	}

}
