package steve6472.polyground.gfx.stack;

import org.joml.Vector4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class StackUtil
{
	public static void rect(Stack stack, float x, float y, float z, float w, float h, float d)
	{
		rect(stack, x, y, z, w, h, d, 1, 1, 1, 1, 1, 1);
	}

	public static void rectShade(Stack stack, float x, float y, float z, float w, float h, float d)
	{
		rect(stack, x, y, z, w, h, d, 0.8f, 0.6f, 0.8f, 0.6f, 1, 0.5f);
	}

	public static void rect(Stack stack, float x, float y, float z, float w, float h, float d, float northMul, float eastMul, float southMul, float westMul, float upMul, float downMul)
	{
		Vector4f lastColor = new Vector4f(stack.getLastColor());

		stack.color(new Vector4f(lastColor).mul(upMul, upMul, upMul, 1));
		stack.pos(x +w, y +h, z).endVertex();
		stack.pos(x, y +h, z).endVertex();
		stack.pos(x, y +h, z +d).endVertex();

		stack.pos(x, y +h, z +d).endVertex();
		stack.pos(x +w, y +h, z +d).endVertex();
		stack.pos(x +w, y +h, z).endVertex();


		stack.color(new Vector4f(lastColor).mul(downMul, downMul, downMul, 1));
		stack.pos(x, y +h, z).endVertex();
		stack.pos(x, y, z).endVertex();
		stack.pos(x, y, z +d).endVertex();

		stack.pos(x, y, z +d).endVertex();
		stack.pos(x, y +h, z +d).endVertex();
		stack.pos(x, y +h, z).endVertex();


		stack.color(new Vector4f(lastColor).mul(northMul, northMul, northMul, 1));
		stack.pos(x, y +h, z +d).endVertex();
		stack.pos(x, y, z +d).endVertex();
		stack.pos(x +w, y, z +d).endVertex();

		stack.pos(x +w, y, z +d).endVertex();
		stack.pos(x +w, y +h, z +d).endVertex();
		stack.pos(x, y +h, z +d).endVertex();


		stack.color(new Vector4f(lastColor).mul(eastMul, eastMul, eastMul, 1));
		stack.pos(x +w, y +h, z +d).endVertex();
		stack.pos(x +w, y, z +d).endVertex();
		stack.pos(x +w, y, z).endVertex();

		stack.pos(x +w, y, z).endVertex();
		stack.pos(x +w, y +h, z).endVertex();
		stack.pos(x +w, y +h, z +d).endVertex();


		stack.color(new Vector4f(lastColor).mul(southMul, southMul, southMul, 1));
		stack.pos(x +w, y +h, z).endVertex();
		stack.pos(x +w, y, z).endVertex();
		stack.pos(x, y, z).endVertex();

		stack.pos(x, y, z).endVertex();
		stack.pos(x, y +h, z).endVertex();
		stack.pos(x +w, y +h, z).endVertex();


		stack.color(new Vector4f(lastColor).mul(westMul, westMul, westMul, 1));
		stack.pos(x, y, z + d).endVertex();
		stack.pos(x, y, z).endVertex();
		stack.pos(x + w, y, z).endVertex();

		stack.pos(x + w, y, z).endVertex();
		stack.pos(x + w, y, z + d).endVertex();
		stack.pos(x, y, z + d).endVertex();

		stack.color(lastColor);
	}
}
