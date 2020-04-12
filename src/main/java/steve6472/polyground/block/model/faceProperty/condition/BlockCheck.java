package steve6472.polyground.block.model.faceProperty.condition;

import steve6472.polyground.world.chunk.SubChunk;

class BlockCheck implements ICheck
{
	int relX, relY, relZ;
	String block;
	Type type;

	String raw;

	BlockCheck(String raw)
	{
		this.raw = raw;
		int end = 0;
		for (int i = raw.length() - 1; i > 0; i--)
		{
			if (raw.charAt(i) == ' ')
			{
				block = raw.substring(i + 1);
				end = i;
				break;
			}
		}

		for (int i = end - 1; i > 0; i--)
		{
			if (raw.charAt(i) == ' ')
			{
				String t = raw.substring(i + 1, end);
				for (Type typ : Type.values())
				{
					if (typ.raw.equals(t))
					{
						type = typ;
						break;
					}
				}
				break;
			}
		}

		String[] arr = raw.substring(raw.indexOf('[') + 1, raw.indexOf(']')).split(",");
		relX = Integer.parseInt(arr[0].trim());
		relY = Integer.parseInt(arr[1].trim());
		relZ = Integer.parseInt(arr[2].trim());
	}

	public boolean test(int x, int y, int z, SubChunk subChunk)
	{
		boolean f;
		if (block.startsWith("#"))
		{
			f = subChunk.getBlockEfficiently(relX + Math.floorMod(x, 16), relY + Math.floorMod(y, 16), relZ + Math.floorMod(z, 16)).hasTag(block.substring(1));
			print("Checked for block tag \"%s\" with result %b", block.substring(1), f);
		} else
		{
			f = subChunk.getBlockEfficiently(relX + Math.floorMod(x, 16), relY + Math.floorMod(y, 16), relZ + Math.floorMod(z, 16)).getName().equals(block);
		}
		boolean flag = switch (type)
			{
				case EQUALS -> f;
				case NOT_EQUALS -> !f;
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};

		print(String.format("Block Condition \"%s\" is %b", raw, flag));

		return flag;
	}
}