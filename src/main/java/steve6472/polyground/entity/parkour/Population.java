package steve6472.polyground.entity.parkour;

import steve6472.polyground.AABBUtil;
import steve6472.polyground.CaveGame;
import steve6472.polyground.gfx.MainRender;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.03.2020
 * Project: CaveGame
 *
 ***********************/
public class Population
{
	private final List<ParkourEntity> parkourEntities;

	private double best = 0;
	private final int index;

	public Population(int index)
	{
		this.index = index;
		parkourEntities = new ArrayList<>();

		for (int i = 0; i < ParkourTest.SAMPLE; i++)
		{
			parkourEntities.add(new ParkourEntity());
		}
	}

	public void render()
	{
		for (ParkourEntity parkourEntity : parkourEntities)
		{
			if (!parkourEntity.isDead() || parkourEntity.goalReached())
				AABBUtil.renderAABBf(parkourEntity.getHitbox().getHitbox(), CaveGame.getInstance().mainRender.basicTess, 1, MainRender.shaders.mainShader);
		}
	}

	public void tick()
	{
		for (int i = 0; i < ParkourTest.TEST_SPEED; i++)
		{
			tickTest();
		}
	}

	private void tickTest()
	{
		boolean allDead = true;
		for (ParkourEntity parkourEntity : parkourEntities)
		{
			if (!parkourEntity.isDead())
			{
				parkourEntity.tick();
				if (!parkourEntity.isDead())
					allDead = false;
			}
		}

		if (allDead)
		{
			double total = 0;
			double lastProbability = 0;
			int min = 0;

			boolean reachedGoal = false;

			for (int i = 0; i < ParkourTest.SAMPLE; i++)
			{
				ParkourEntity e = parkourEntities.get(i);

				double survivalProbability = survivalProbability(e);
				total += survivalProbability;

				if (survivalProbability > lastProbability)
				{
					min = i;
					lastProbability = survivalProbability;
					if (e.goalReached())
						reachedGoal = true;
				}
			}

			best = Math.max(best, lastProbability);
			ParkourEntity entity = parkourEntities.get(min);
			Brain best = entity.getBrain();
			//			parkourEntities.set(0, new ParkourEntity(new Brain(best, entity.getDiedAtStep(), entity.goalReached())));

			System.out.println(String.format("#%d bestNow: %.3f best: %.3f avg: %.3f steps: %d reachedChectpoint: %d lastDistance: %.3f" ,
				index, lastProbability, this.best, total / (double) ParkourTest.SAMPLE, best.getPassedInstructions(), entity.currentCheckpoint, entity.distanceToLastCheckpoint));

			for (int i = 0; i < ParkourTest.SAMPLE - 1; i++)
			{
				parkourEntities.set(i, new ParkourEntity(new Brain(best, entity.getDiedAtStep(), entity.goalReached())));
			}

			/*
			for (int i = 1; i < SAMPLE; i++)
			{
				ParkourEntity e = parkourEntities.get(i);
				Brain current = e.getBrain();
				parkourEntities.set(i, new ParkourEntity(new Brain(current, e.getDiedAtStep(), e.goalReached())));
			}*/

//			parkourEntities.set(ParkourTest.SAMPLE - 1, new ParkourEntity());

			if (reachedGoal && ParkourTest.goalDistance >= 0.002)
				ParkourTest.goalDistance *= ParkourTest.GOAL_MULTIPLIER;
		}
	}

	private double survivalProbability(ParkourEntity e)
	{
		int ip = e.getDiedAtStep();
		ip = 60 * ParkourTest.TEST_TIME - ip;

		if (e.getDiedAtStep() < 10)
			return 0;
		if (e.getBrain().getPassedInstructions() == ParkourTest.TEST_TIME * 60 && e.currentCheckpoint == 0)
			return 0;
		if (e.goalReached())
			return 100d + ip + e.currentCheckpoint * e.currentCheckpoint * 5 + 1d / e.distanceToLastCheckpoint;
		else
			return e.currentCheckpoint * e.currentCheckpoint * 5 + 1d / e.distanceToLastCheckpoint;
	}
}
