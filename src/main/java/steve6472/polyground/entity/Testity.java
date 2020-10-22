package steve6472.polyground.entity;

import org.joml.Vector3f;
import steve6472.polyground.entity.interfaces.IAdvancedRender;
import steve6472.polyground.entity.interfaces.ITickable;
import steve6472.polyground.entity.model.ClaySolderModel;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.sge.main.util.RandomUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Testity extends EntityBase implements IAdvancedRender, ITickable
{
	public Testity(Vector3f pos)
	{
		setPosition(pos);
	}

	private static final ClaySolderModel MODEL = new ClaySolderModel();

	@Override
	public void tick()
	{
		if (RandomUtil.decide(60 * 5))
		{
			getMotion().add(RandomUtil.randomFloat(-0.1f, 0.1f), 0, RandomUtil.randomFloat(-0.1f, 0.1f));

			setYaw((float) Math.atan2(getMotionX(), getMotionZ()));
		}

		addPosition(getMotion());
		getMotion().mul(0.95f);
	}

	@Override
	public void render(Stack stack)
	{
		stack.scale(1f / 16f);

		MODEL.update(this);
		MODEL.render(stack);
	}

	@Override
	public boolean isDead()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "Testity";
	}

	public static void main(String[] args) throws IOException
	{
		char a, b, c;

		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String line;
		while (!(line = reader.readLine()).isEmpty())
		{
			if (line.length() != 3)
				continue;

			a = line.charAt(0);
			b = line.charAt(1);
			c = line.charAt(2);

			builder.append("stack.pos(");

			if (a == '-')
				builder.append('-');
			builder.append(".5f, ");
			if (b == '-')
				builder.append('-');
			builder.append(".5f, ");
			if (c == '-')
				builder.append('-');
			builder.append(".5f).endVertex();\n");
		}
		System.out.println(builder.toString());
	}
}
