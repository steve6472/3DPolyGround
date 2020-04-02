package steve6472.polyground.entity.parkour;

public class Brain
{
	private static final int instructionCount = 60 * ParkourTest.TEST_TIME;
	int currentInstruction;

	Instruction[] instructions;

	public Brain()
	{
		instructions = new Instruction[instructionCount];

		for (int i = 0; i < instructionCount; i++)
		{
			instructions[i] = new Instruction(i).random();
		}
	}

	public Instruction nextInstruction()
	{
		if (currentInstruction + 1 <= instructions.length)
			return instructions[currentInstruction++];
		else
			return null;
	}

	public Brain(Brain parent, int parentDiedAtStep, boolean goalReached)
	{
		instructions = new Instruction[instructionCount];

		for (int i = 0; i < instructionCount; i++)
		{
			instructions[i] = parent.instructions[i].copy().mutate_(parentDiedAtStep, goalReached);
		}
	}

	public int getPassedInstructions()
	{
		return currentInstruction;
	}
}