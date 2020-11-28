package steve6472.polyground.gfx.stack;

import org.joml.Math;
import org.joml.*;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.shaders.EntityShader;
import steve6472.polyground.tessellators.StackTessellator;
import steve6472.sge.gfx.Tessellator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 14.11.2020
 * Project: CaveGame
 *
 ***********************/
public class EntityTess extends StackTess
{
	private static final int MAX_SIZE = 1024 * 32;

	private final StackTessellator tess;
	private final Vector3f dest3f;
	private final Vector4f lastColor;

	public EntityTess(Stack stack)
	{
		super(stack);

		tess = new StackTessellator(MAX_SIZE);
		tess.begin(MAX_SIZE);
		tess.color(1, 1, 1, 1);

		dest3f = new Vector3f();
		lastColor = new Vector4f();
	}

	public EntityTess pos(float x, float y, float z)
	{
		stack.transformPosition(x, y, z, dest3f);
		tess.pos(dest3f);
		return this;
	}

	public EntityTess color(float r, float g, float b, float a)
	{
		tess.color(r, g, b, a);
		lastColor.set(r, g, b, a);
		return this;
	}

	public EntityTess color(Vector4f color)
	{
		tess.color(color.x, color.y, color.z, color.w);
		lastColor.set(color);
		return this;
	}

	public EntityTess uv(float u, float v)
	{
		tess.uv(u, v);
		return this;
	}

	public EntityTess normal(float nx, float ny, float nz)
	{
		stack.pushMatrix();
		stack.rotateY((float) (Math.PI / 2f));
		stack.setTranslation(0, 0, 0);
		stack.transformPosition(nx, ny, nz, dest3f);
		dest3f.normalize();
		tess.normal(dest3f);
		stack.popMatrix();
		return this;
	}

	public Vector4f getLastColor()
	{
		return lastColor;
	}

	public EntityTess endVertex()
	{
		tess.endVertex();
		return this;
	}

	@Override
	public void render(Matrix4f view)
	{
		MainRender.shaders.entityShader.bind(view);
		MainRender.shaders.entityShader.setTransformation(new Matrix4f());
		MainRender.shaders.entityShader.setUniform(EntityShader.NORMAL_MATRIX, new Matrix3f(new Matrix4f(stack).invert().transpose3x3()));
		MainRender.shaders.entityShader.setUniform(EntityShader.SHADE, CaveGame.getInstance().world.shade);
		BlockAtlas.getAtlas().getSprite().bind();

		tess.loadPos(0);
		tess.loadColor(1);
		tess.loadUv(2);
		tess.loadNormal(3);
		tess.draw(Tessellator.TRIANGLES);
		tess.disable(0, 1, 2, 3);
	}

	@Override
	public void reset()
	{
		tess.clear();
		tess.begin(MAX_SIZE);
		tess.color(1, 1, 1, 1);
	}

	public void rect(float x, float y, float z, float w, float h, float d)
	{
		rect(x, y, z, w, h, d, 1, 1, 1, 1, 1, 1);
	}

	public void rectShade(float x, float y, float z, float w, float h, float d)
	{
		rect(x, y, z, w, h, d, 0.8f, 0.6f, 0.8f, 0.6f, 1, 0.5f);
	}

	public void rect(float x, float y, float z, float w, float h, float d, float northMul, float eastMul, float southMul, float westMul, float upMul, float downMul)
	{
		EntityTess tess = stack.getEntityTess();
		Vector4f lastColor = new Vector4f(tess.getLastColor());

		tess.color(new Vector4f(lastColor).mul(upMul, upMul, upMul, 1));
		tess.pos(x +w, y +h, z).endVertex();
		tess.pos(x, y +h, z).endVertex();
		tess.pos(x, y +h, z +d).endVertex();

		tess.pos(x, y +h, z +d).endVertex();
		tess.pos(x +w, y +h, z +d).endVertex();
		tess.pos(x +w, y +h, z).endVertex();


		tess.color(new Vector4f(lastColor).mul(downMul, downMul, downMul, 1));
		tess.pos(x, y +h, z).endVertex();
		tess.pos(x, y, z).endVertex();
		tess.pos(x, y, z +d).endVertex();

		tess.pos(x, y, z +d).endVertex();
		tess.pos(x, y +h, z +d).endVertex();
		tess.pos(x, y +h, z).endVertex();


		tess.color(new Vector4f(lastColor).mul(northMul, northMul, northMul, 1));
		tess.pos(x, y +h, z +d).endVertex();
		tess.pos(x, y, z +d).endVertex();
		tess.pos(x +w, y, z +d).endVertex();

		tess.pos(x +w, y, z +d).endVertex();
		tess.pos(x +w, y +h, z +d).endVertex();
		tess.pos(x, y +h, z +d).endVertex();


		tess.color(new Vector4f(lastColor).mul(eastMul, eastMul, eastMul, 1));
		tess.pos(x +w, y +h, z +d).endVertex();
		tess.pos(x +w, y, z +d).endVertex();
		tess.pos(x +w, y, z).endVertex();

		tess.pos(x +w, y, z).endVertex();
		tess.pos(x +w, y +h, z).endVertex();
		tess.pos(x +w, y +h, z +d).endVertex();


		tess.color(new Vector4f(lastColor).mul(southMul, southMul, southMul, 1));
		tess.pos(x +w, y +h, z).endVertex();
		tess.pos(x +w, y, z).endVertex();
		tess.pos(x, y, z).endVertex();

		tess.pos(x, y, z).endVertex();
		tess.pos(x, y +h, z).endVertex();
		tess.pos(x +w, y +h, z).endVertex();


		tess.color(new Vector4f(lastColor).mul(westMul, westMul, westMul, 1));
		tess.pos(x, y, z + d).endVertex();
		tess.pos(x, y, z).endVertex();
		tess.pos(x + w, y, z).endVertex();

		tess.pos(x + w, y, z).endVertex();
		tess.pos(x + w, y, z + d).endVertex();
		tess.pos(x, y, z + d).endVertex();

		tess.color(lastColor);
	}
}
