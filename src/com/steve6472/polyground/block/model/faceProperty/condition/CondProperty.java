package com.steve6472.polyground.block.model.faceProperty.condition;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.model.faceProperty.FaceProperty;
import com.steve6472.polyground.world.World;
import org.json.JSONObject;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class CondProperty extends FaceProperty
{
	private ICheck check;

	private static void print(String s, Object... f)
	{
		if (Boolean.parseBoolean(System.getProperty("debug_conditions", "false")))
			System.out.println(String.format(s, f));
	}

	public CondProperty()
	{
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		String condition = json.getString("condition");

		check = new MainCheck(condition);
	}

	boolean test(int x, int y, int z, World world)
	{
		return check.test(x, y, z, world);
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		System.err.println("Can not save CondProperty yet!");
		System.exit(0);
	}

	@Override
	public String getId()
	{
		return "condition";
	}

	@Override
	public CondProperty createCopy()
	{
		return null;
	}

	private interface ICheck
	{
		boolean test(int x, int y, int z, World world);
	}

	private static boolean hasCheckSign(String s)
	{
		return s.contains("||") || s.contains("&&") || s.contains("!=") || s.contains("==");
	}

	private static class MainCheck implements ICheck
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
				m: for (int i = 0; i < raw.length(); i++)
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
		public boolean test(int x, int y, int z, World world)
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
				return left.test(x, y, z, world);
			}

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

			print(String.format("Main Condition \"%s\" is %b", raw, flag));

			return flag;
		}
	}

	private static class LRCheck implements ICheck
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

			print(String.format("LR Condition \"%s\" is %b", raw, flag));

			return flag;
		}
	}

	private static class BlockCheck implements ICheck
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

		public boolean test(int x, int y, int z, World world)
		{
			boolean f;
			if (block.startsWith("#"))
			{
				f = world.getBlock(x + relX, y + relY, z + relZ).hasTag(block.substring(1));
				print("Checked for block tag \"%s\" with result %b", block.substring(1), f);
			} else
			{
				f = world.getBlock(x + relX, y + relY, z + relZ).getName().equals(block);
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

	private enum Type
	{
		EQUALS("=="), NOT_EQUALS("!="), OR("||"), AND("&&"), NOT_FOUND("");

		String raw;

		Type(String raw)
		{
			this.raw = raw;
		}
	}
}
