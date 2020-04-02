package steve6472.polyground.entity.parkour;

import steve6472.sge.main.Util;
import steve6472.sge.main.util.RandomUtil;

public class Instruction
{
	double yaw;
	double forward, backwards, left, right, jump, sprint;
	//TODO: add sprint

	int index;

	public Instruction(int index)
	{
		this.index = index;
	}

	public Instruction copy()
	{
		Instruction instruction = new Instruction(index);
		instruction.yaw = yaw;
		instruction.forward = forward;
		instruction.backwards = backwards;
		instruction.left = left;
		instruction.right = right;
		instruction.jump = jump;
		return instruction;
	}

	public Instruction mutate()
	{
		yaw = Util.clamp(0, Math.PI * 2, yaw + mutation());
		forward = Util.clamp(0, 1, forward + mutation());
		backwards = Util.clamp(0, 1, backwards + mutation());
		left = Util.clamp(0, 1, left + mutation());
		right = Util.clamp(0, 1, right + mutation());
		jump = Util.clamp(0, 1, jump + mutation());
		sprint = Util.clamp(0, 1, sprint + mutation());
		return this;
	}

	public Instruction mutate_(int parentDiedAtStep, boolean goalReached)
	{
		double mutationRate = ParkourTest.MUTATION_RATE;

		if (goalReached)
			mutationRate /= 50d;

		if (index >= parentDiedAtStep)
			mutationRate *= 5d;

		if (RandomUtil.randomDouble(0, 1) < mutationRate) yaw = RandomUtil.randomDouble(0, Math.PI * 2);
		if (RandomUtil.randomDouble(0, 1) < mutationRate) forward = RandomUtil.randomDouble(0, 1);
		if (RandomUtil.randomDouble(0, 1) < mutationRate) backwards = RandomUtil.randomDouble(0, 1);
		if (RandomUtil.randomDouble(0, 1) < mutationRate) left = RandomUtil.randomDouble(0, 1);
		if (RandomUtil.randomDouble(0, 1) < mutationRate) right = RandomUtil.randomDouble(0, 1);
		if (RandomUtil.randomDouble(0, 1) < mutationRate) jump = RandomUtil.randomDouble(0, 1);
		if (RandomUtil.randomDouble(0, 1) < mutationRate) sprint = RandomUtil.randomDouble(0, 1);
		return this;
	}

	public Instruction random()
	{
		yaw = RandomUtil.randomDouble(0, Math.PI * 2);
		forward = RandomUtil.randomDouble(0, 1);
		backwards = RandomUtil.randomDouble(0, 1);
		left = RandomUtil.randomDouble(0, 1);
		right = RandomUtil.randomDouble(0, 1);
		jump = RandomUtil.randomDouble(0, 1);
		sprint = RandomUtil.randomDouble(0, 1);
		return this;
	}

	private static double mutation()
	{
		return RandomUtil.randomDouble(-ParkourTest.MUTATION_RATE, ParkourTest.MUTATION_RATE);
	}
}