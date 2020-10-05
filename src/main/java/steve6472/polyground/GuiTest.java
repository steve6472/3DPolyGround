package steve6472.polyground;

import org.joml.Matrix4f;
import steve6472.polyground.block.Block;
import steve6472.polyground.entity.StaticEntityModel;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.shaders.EntityShader;
import steve6472.sge.main.MainApp;

import static org.lwjgl.opengl.GL11.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.09.2020
 * Project: CaveGame
 *
 ***********************/
public class GuiTest
{
	public static void renderBlock(Block block, MainApp main, float rotX, float rotY, float rotZ, float scale)
	{
		StaticEntityModel model = block.getDefaultState().getBlockModels()[0].getModel();

		EntityShader shader = MainRender.shaders.entityShader;

		float w = main.getWidth();
		float h = main.getHeight();

		shader.bind();
		shader.setProjection(new Matrix4f().ortho(-w / h, w / h, -1, 1, -1, 1).scale(1f / (h / 2f)));

		glEnable(GL_DEPTH_TEST);

		model.render(
			new Matrix4f()
				.translate(0f, 0, 0),
			new Matrix4f()
				.scale(16f)
				.scale(scale)
				.rotate((float) Math.toRadians(rotX), 1, 0, 0)
				.rotate((float) Math.toRadians(rotY), 0, 1, 0)
				.rotate((float) Math.toRadians(rotZ), 0, 0, 1)
				.translate(-0.5f, -0.5f, -0.5f)
		);

		glDisable(GL_DEPTH_TEST);

		shader.setProjection(MainRender.shaders.getProjectionMatrix());
	}
}
