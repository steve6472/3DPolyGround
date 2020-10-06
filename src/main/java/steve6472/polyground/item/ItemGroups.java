package steve6472.polyground.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ItemGroups
{
	public final record ItemGroup(String name, String preview, String id, HashMap<String, ItemGroup> groups, List<Item> items)
	{
		public void addGroup(ItemGroup group)
		{
			groups.put(group.id, group);
		}

		public void addItem(Item item)
		{
			items.add(item);
		}
	}

	private final ItemGroup mainGroups;

	public ItemGroups()
	{
		mainGroups = GroupBuilder
			.create("Main", "main", "null")
			.addChild(
				GroupBuilder.create("Building", "building", "bricks")
					.addChild(GroupBuilder.create("Stone", "stone", "stone"))
					.addChild(GroupBuilder.create("Metal", "metal", "iron_block"))
					.addChild(
						GroupBuilder.create("Wood", "wood", "oak_log")
							.addChild(GroupBuilder.create("Oak", "oak", "oak_log"))
							.addChild(GroupBuilder.create("Birch", "birch", "birch_log"))
							.addChild(GroupBuilder.create("Spruce", "spruce", "spruce_log"))
							.addChild(GroupBuilder.create("Dark Oak", "dark_oak", "dark_oak_log"))
							.addChild(GroupBuilder.create("Jungle", "jungle", "jungle_log"))
							.addChild(GroupBuilder.create("Acacia", "acacia", "acacia_log"))
				)
			)
			.addChild(
				GroupBuilder.create("Nature", "nature", "pebbles")
					.addChild(GroupBuilder.create("Desert", "desert", "sand"))
					.addChild(GroupBuilder.create("Forest", "forest", "oak_leaves"))
					.addChild(GroupBuilder.create("Taiga", "taiga", "snow_layer"))
					.addChild(GroupBuilder.create("Hills", "hills", "gravel"))
					.addChild(GroupBuilder.create("Leaves", "leaves", "oak_leaves"))
			)
			.addChild(GroupBuilder.create("Misc", "misc", "wooden_box"))
			.generate();
	}

	public void addItem(Item item, String groupPath)
	{
		String[] split = groupPath.split("\\.");

		ItemGroup current = mainGroups;

		for (String s : split)
		{
			current = current.groups.get(s);
		}

		current.addItem(item);
	}

	public ItemGroup getRoot()
	{
		return mainGroups;
	}

	private static class GroupBuilder
	{
		private final String name, preview, id;
		private final List<GroupBuilder> children;

		private GroupBuilder(String name, String id, String preview)
		{
			children = new ArrayList<>();
			this.name = name;
			this.id = id;
			this.preview = preview;
		}

		public static GroupBuilder create(String name, String id, String preview)
		{
			return new GroupBuilder(name, id, preview);
		}

		public GroupBuilder addChild(GroupBuilder child)
		{
			children.add(child);
			return this;
		}

		public ItemGroup generate()
		{
			ItemGroup group = new ItemGroup(name, preview, id, new HashMap<>(), new ArrayList<>());

			for (GroupBuilder builder : children)
			{
				group.addGroup(builder.generate());
			}

			return group;
		}
	}
}
