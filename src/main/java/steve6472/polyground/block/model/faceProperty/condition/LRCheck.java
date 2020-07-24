package steve6472.polyground.block.model.faceProperty.condition;

import steve6472.polyground.world.World;

class LRCheck implements ICheck
{
	ICheck left, right;
	Type type;

	String raw;

	LRCheck(ICheck left, ICheck right, String cond)
	{
		this.raw = cond;
		this.left = left;
		this.right = right;
		if (cond.contains("||"))
			type = Type.OR;
		else if (cond.contains("&&"))
			type = Type.AND;
		else if (cond.contains("=="))
			type = Type.EQUALS;
		else if (cond.contains("!="))
			type = Type.NOT_EQUALS;
		else
			type = Type.NOT_FOUND;

		print(type.name());
	}

	@Override
	public void fixBlockId()
	{
		left.fixBlockId();
		right.fixBlockId();
	}

	@Override
	public boolean test(int x, int y, int z, World world)
	{
		boolean l = left.test(x, y, z, world);
		boolean r = right.test(x, y, z, world);

		boolean flag = switch (type)
			{
				case EQUALS -> l == r;
				case NOT_EQUALS -> l != r;
				case AND -> l && r;
				case OR -> l || r;
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};

//		print(String.format("LR Condition \"%s\" is %b", raw, flag));

		return flag;
	}
}