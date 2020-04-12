package steve6472.polyground.generator.cond;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.MouseEvent;

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
public class Picker
{
	View view;
	private final DepthFrameBuffer screen;
	private final PboPicker picker;

	public Picker(View view)
	{
		this.view = view;

		screen = new DepthFrameBuffer(View.screenWidth, View.screenHeight);

		view.mainApp.getEventHandler().register(this);
		picker = new PboPicker();
	}

	public void render()
	{
		BasicTessellator basicTess = view.basicTess;


		screen.bindFrameBuffer(View.screenWidth, View.screenHeight);
		DepthFrameBuffer.clearCurrentBuffer();

		view.shader.bind(view.camera.getViewMatrix());
		view.shader.setTransformation(new Matrix4f().scale(0.2f));
		view.shader.setProjection(new Matrix4f().ortho(-1, 1, -1, 1, 0.01f, 64f));

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		List<Cube> cubes = new ArrayList<>(26);

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (i != 1 || j != 1 || k != 1)
					{
						cubes.add(new Cube(view.camera, i * 2 - 2, j * 2 - 2, k * 2 - 2, i / 3f, j / 3f, k / 3f, 1, false));
					}
				}
			}
		}

		Collections.sort(cubes);

		basicTess.begin(12 * 3 * 26);

		for (Cube c : cubes)
		{
			View.renderCube(c, basicTess);
		}

		GL11.glLineWidth(2);
		basicTess.loadPos(0);
		basicTess.loadColor(1);
		basicTess.draw(Tessellator.TRIANGLES);
		basicTess.disable(0, 1);
		GL11.glLineWidth(1f);

		glDisable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
		screen.unbindCurrentFrameBuffer(view.mainApp);

//		SpriteRender.renderSpriteInverted(0, view.mainApp.getHeight() / 2 - View.screenHeight / 2, View.screenWidth, View.screenHeight, screen.texture);
	}

	@Event
	public void clicc(MouseEvent e)
	{
		if (e.getAction() != KeyList.PRESS || e.getButton() == KeyList.MMB)
			return;

		if (e.getX() < 20 || e.getX() > 300 || e.getY() < 50 || e.getY() > 400)
			return;

		int x = e.getX();
		int y = -e.getY() + View.screenHeight + view.mainApp.getHeight() / 2 - View.screenHeight / 2;

		byte[] bytes = picker.getColorFromFrameBuffer(x, y, screen);
		if ((bytes[3] & 0xff) != 0)
		{
			int r = bytes[0] & 0xff;
			int g = bytes[1] & 0xff;
			int b = bytes[2] & 0xff;
			r /= 85;
			g /= 85;
			b /= 85;

			if (e.getButton() == KeyList.LMB)
				view.conditionGenerator.states[r][g][b] = EnumState.cycle(view.conditionGenerator.states[r][g][b]);
			else
				view.conditionGenerator.states[r][g][b] = EnumState.reverseCycle(view.conditionGenerator.states[r][g][b]);
		}
	}
}
