package steve6472.polyground.block.model.faceProperty.condition;

import steve6472.polyground.world.World;

class AndChain implements ICheck
{
	ICheck[] checks;

	AndChain(String raw)
	{
		String[] blockConditions = raw.split("&&");
		checks = new ICheck[blockConditions.length];

		for (int i = 0; i < blockConditions.length; i++)
		{
			String s = blockConditions[i].trim();
			checks[i] = new BlockCheck(s);
		}
	}

	@Override
	public void fixBlockId()
	{
		for (ICheck c : checks)
			c.fixBlockId();
	}

	@Override
	public boolean test(int x, int y, int z, World world)
	{
		for (ICheck c : checks)
		{
			if (!c.test(x, y, z, world))
				return false;
		}
		return true;
	}
}