package com.steve6472.polyground.block.model.faceProperty.condition;

import com.steve6472.polyground.block.model.faceProperty.FaceProperty;
import com.steve6472.polyground.world.World;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class CondProperty extends FaceProperty
{
	private ICheck check;

	public CondProperty()
	{
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		String condition = json.getString("condition");

		if (condition.contains("{") && condition.contains("}"))
		{
			check = new NestedCheck(condition);
		} else if (condition.contains("(") && condition.contains(")"))
		{
			check = new LRCheck(condition);
		} else
		{
			check = new BlockCheck(condition);
		}
	}

	public boolean test(int x, int y, int z, World world)
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
/*
	public static void main(String[] args)
	{
//		String toTest = "[1, -1, 0] != model_test";
//		String toTest = "([1, -1, 0] != model_test) && ([1, 0, 0] == air)";
		String toTest = "{([1, -1, 0] != model_test) && ([1, 0, 0] == air)} || {([1, -1, 0] != test) && ([1, 0, 0] == #transparent)}";

		ICheck check;

		World world = new World();

		if (toTest.contains("{") && toTest.contains("}"))
		{
			check = new NestedCheck(toTest);
		} else if (toTest.contains("(") && toTest.contains(")"))
		{
			check = new LRCheck(toTest);
		} else
		{
			check = new BlockCheck(toTest);
		}

		System.out.println(check.test(0, 0, 0, world));
	}*/

	private interface ICheck
	{
		boolean test(int x, int y, int z, World world);
	}

	private static class NestedCheck implements ICheck
	{
		List<ICheck> checks;
		List<Type> types;

		public NestedCheck(String raw)
		{
			checks = new ArrayList<>();
			types = new ArrayList<>();
			int it = 0;
			int replaceCount = 0;
//			System.out.println(raw);
//			System.out.println("-".repeat(raw.length()));
			while (raw.indexOf('}') != -1 || raw.indexOf('{') != -1)
			{
				for (int i = 0; i < raw.length(); i++)
				{
					if (raw.charAt(i) == '}')
					{
						for (int j = i; j >= 0; j--)
						{
							if (raw.charAt(j) == '{')
							{
								checks.add(new LRCheck(raw.substring(j + 1, i)));
								raw = raw.replace(raw.substring(j, i + 1), "§" + replaceCount);
//								System.out.println(raw);
								replaceCount++;
//								System.out.println("-----");
								break;
							}
						}
					}
				}

				it++;
				if (it >= 50)
				{
					System.err.println("ERROR");
					return;
				}
			}


			while (raw.contains("||") || raw.contains("&&") || raw.contains("!=") || raw.contains("=="))
			{
				for (Type typ : Type.values())
				{
					if (raw.contains(typ.raw))
					{
						types.add(typ);
						raw = raw.replaceFirst(Pattern.quote(typ.raw), "&");
						break;
					}
				}
			}
		}

		@Override
		public boolean test(int x, int y, int z, World world)
		{
			boolean flag = checks.get(0).test(x, y, z, world);
			for (int i = 1; i < checks.size() - 1; i++)
			{
				Type t = types.get(i - 1);

				switch (t)
				{
					case EQUALS -> flag = flag == checks.get(i).test(x, y, z, world);
					case NOT_EQUALS -> flag = flag != checks.get(i).test(x, y, z, world);
					case AND -> flag = flag && checks.get(i).test(x, y, z, world);
					case OR -> flag = flag || checks.get(i).test(x, y, z, world);
				}
			}

			switch (types.get(types.size() - 1))
			{
				case EQUALS -> flag = flag == checks.get(checks.size() - 1).test(x, y, z, world);
				case NOT_EQUALS -> flag = flag != checks.get(checks.size() - 1).test(x, y, z, world);
				case AND -> flag = flag && checks.get(checks.size() - 1).test(x, y, z, world);
				case OR -> flag = flag || checks.get(checks.size() - 1).test(x, y, z, world);
			}

			return flag;
		}
	}

	private static class LRCheck implements ICheck
	{
		ICheck left, right;
		Type type;

		public LRCheck(String raw)
		{
			int it = 0;
			int replaceCount = 0;
//			System.out.println(raw);
//			System.out.println("-".repeat(raw.length()));
			while (raw.indexOf(')') != -1 || raw.indexOf('(') != -1)
			{
				for (int i = 0; i < raw.length(); i++)
				{
					if (raw.charAt(i) == ')')
					{
						for (int j = i; j >= 0; j--)
						{
							if (raw.charAt(j) == '(')
							{
								if (left == null)
									left = new BlockCheck(raw.substring(j + 1, i));
								else
									right = new BlockCheck(raw.substring(j + 1, i));

								raw = raw.replace(raw.substring(j, i + 1), "§" + replaceCount);
//								System.out.println(raw);
								replaceCount++;
//								System.out.println("-----");
								break;
							}
						}
					}
				}

				it++;
				if (it >= 50)
				{
					System.err.println("ERROR");
					return;
				}
			}

			for (Type typ : Type.values())
			{
				if (raw.contains(typ.raw))
				{
					type = typ;
					break;
				}
			}
		}

		@Override
		public boolean test(int x, int y, int z, World world)
		{
			return switch (type)
				{
					case EQUALS -> left.test(x, y, z, world) == right.test(x, y, z, world);
					case NOT_EQUALS -> left.test(x, y, z, world) != right.test(x, y, z, world);
					case AND -> left.test(x, y, z, world) && right.test(x, y, z, world);
					case OR -> left.test(x, y, z, world) || right.test(x, y, z, world);
				};
		}
	}

	private static class BlockCheck implements ICheck
	{
		int relX, relY, relZ;
		String block;
		Type type;

		public BlockCheck(String raw)
		{
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

//			System.out.println(block);
//			System.out.println(type);
//			System.out.println(String.format("%d, %d, %d", relX, relY, relZ));
		}

		public boolean test(int x, int y, int z, World world)
		{
			return switch (type)
			{
				case EQUALS -> world.getBlock(x + relX, y + relY, z + relZ).getName().equals(block);
				case NOT_EQUALS -> !world.getBlock(x + relX, y + relY, z + relZ).getName().equals(block);
				default -> throw new IllegalStateException("Unexpected value: " + type);
			};
		}
	}

	private enum Type
	{
		EQUALS("=="), NOT_EQUALS("!="), OR("||"), AND("&&");

		String raw;

		Type(String raw)
		{
			this.raw = raw;
		}
	}
}
