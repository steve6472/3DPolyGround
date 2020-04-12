package steve6472.polyground.block.model.faceProperty.condition;

import steve6472.polyground.CaveGame;
import steve6472.polyground.world.chunk.SubChunk;
import java.util.HashMap;

class MainCheck implements ICheck
{
	HashMap<Integer, ICheck> map;
	Type type;

	String raw;

	MainCheck(String raw)
	{
		print("Raw: \"%s\"", raw);
		map = new HashMap<>();
		this.raw = raw;
		int it = 0;
		int replaceCount = 0;
		while (raw.indexOf(')') != -1 || raw.indexOf('(') != -1)
		{
			m:
			for (int i = 0; i < raw.length(); i++)
			{
				if (raw.charAt(i) == ')')
				{
					for (int j = i; j >= 0; j--)
					{
						if (raw.charAt(j) == '(')
						{
							String cond = raw.substring(j + 1, i);
							if (cond.startsWith("[") && cond.contains("]") && cond.indexOf(']') == cond.lastIndexOf(']'))
							{
								map.put(replaceCount, new BlockCheck(cond));
							} else if (hasCheckSign(cond))
							{
								String num0 = "";
								String num1;
								StringBuilder temp = new StringBuilder();
								boolean start = false;
								for (int k = 0; k < cond.length(); k++)
								{
									char c = cond.charAt(k);
									if (c == '§')
									{
										start = true;
										continue;
									}
									if (c == ' ' && start)
									{
										num0 = temp.toString();
										temp = new StringBuilder();
										start = false;
										continue;
									}
									if (start)
									{
										temp.append(c);
									}
								}
								num1 = temp.toString();
								print("\"%s\" \"%s\"", num0, num1);
								int zero = Integer.parseInt(num0);
								int one = Integer.parseInt(num1);
								map.put(replaceCount, new LRCheck(map.get(zero), map.get(one), cond));
								map.remove(zero);
								map.remove(one);
							} else
							{
								System.err.println("Error! Quitting game!");
								CaveGame.getInstance().exit();
								System.exit(-2);
							}
							print("Found check: " + cond);
							raw = raw.replace(raw.substring(j, i + 1), "§" + replaceCount);
							replaceCount++;
							break m;
						}
					}
				}
			}

			it++;
			if (it >= 64)
			{
				System.err.println("Model had more than 64 conditions!");
				return;
			}
		}

		print("Result: \"" + raw + "\"");

		for (Integer integer : map.keySet())
		{
			print("%d: %s", integer, map.get(integer));
		}

		print("-".repeat(64));

		if (raw.contains("||"))
			type = Type.OR;
		else if (raw.contains("&&"))
			type = Type.AND;
		else if (raw.contains("=="))
			type = Type.EQUALS;
		else if (raw.contains("!="))
			type = Type.NOT_EQUALS;
		else
			type = Type.NOT_FOUND;
	}

	@Override
	public boolean test(int x, int y, int z, SubChunk subChunk)
	{
		print("-+".repeat(32));
		ICheck left = null, right = null;

		int i = 0;
		for (Integer integer : map.keySet())
		{
			if (left == null)
				left = map.get(integer);
			else
				right = map.get(integer);
			i++;
		}

		if (i == 0)
		{
			throw new IllegalStateException("There are no conditions!");
		}

		if (i > 2)
		{
			throw new IllegalStateException("Condition count is bigger than two!");
		}

		if (right == null)
		{
			return left.test(x, y, z, subChunk);
		}

		boolean l = left.test(x, y, z, subChunk);
		boolean r = right.test(x, y, z, subChunk);

		boolean flag = switch (type)
			{
				case EQUALS -> l == r;
				case NOT_EQUALS -> l != r;
				case AND -> l && r;
				case OR -> l || r;
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};

		print(String.format("Main Condition \"%s\" is %b", raw, flag));

		return flag;
	}
}