package steve6472.polyground.entity.parkour;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.gfx.stack.LineTess;
import steve6472.polyground.gfx.stack.Stack;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.03.2020
 * Project: CaveGame
 *
 ***********************/
public class ParkourTest
{
	private final List<Population> populations;

	public static final List<Vector4f> checkpoints;

	static
	{
		checkpoints = new ArrayList<>();
		checkpoints.add(new Vector4f(3.5f, 1f, 1.5f, 0.5f));
		checkpoints.add(new Vector4f(6.5f, 1f, 3.5f, 0.5f));
		checkpoints.add(new Vector4f(8.5f, 1f, 6.5f, 0.5f));
		checkpoints.add(new Vector4f(8.5f, 1f, 8.5f, 0.5f));
	}

	public static final int SAMPLE = 16;
	public static final int POPULATION_COUNT = 8;
	public static int TEST_SPEED = 1;
	public static final int TEST_TIME = 20;
	public static final double PRESS_THRESHOLD = 0.3;
	public static final double JUMP_PRESS_THRESHOLD = 0.01;
	public static final double MUTATION_RATE = 0.035;
	public static final Vector3f START_POSITION = new Vector3f(1.5f, 1.1f, 1.5f);
	public static float goalDistance = 0.4f;
	public static final float GOAL_MULTIPLIER = 0.98f;

	public ParkourTest()
	{
		populations = new ArrayList<>();

		for (int i = 0; i < POPULATION_COUNT; i++)
		{
			populations.add(new Population(i));
		}
	}

	public void render()
	{
		final Stack stack = CaveGame.getInstance().mainRender.stack;
		final LineTess lineTess = stack.getLineTess();
		lineTess.debugBox(
			START_POSITION.x() - 0.5f,
			START_POSITION.y() - 0.5f,
			START_POSITION.z() - 0.5f,
			START_POSITION.x() + 0.5f,
			START_POSITION.y() + 0.5f,
			START_POSITION.z() + 0.5f);

		lineTess.debugBox(
			checkpoints.get(checkpoints.size() - 1).x() - 0.5f,
			checkpoints.get(checkpoints.size() - 1).y() - 0.5f,
			checkpoints.get(checkpoints.size() - 1).z() - 0.5f,
			checkpoints.get(checkpoints.size() - 1).x() + 0.5f,
			checkpoints.get(checkpoints.size() - 1).y() + 0.5f,
			checkpoints.get(checkpoints.size() - 1).z() + 0.5f);

		populations.forEach(Population::render);

		for (Vector4f v : checkpoints)
		{
			lineTess.debugBox(
				v.x() - v.w(),
				v.y() - v.w(),
				v.z() - v.w(),
				v.x() + v.w(),
				v.y() + v.w(),
				v.z() + v.w());
		}
	}

	public void tick()
	{
		populations.forEach(population ->
		{
			population.tick();
			render();
		});
	}
}
