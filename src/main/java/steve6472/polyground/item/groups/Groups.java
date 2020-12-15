package steve6472.polyground.item.groups;

import steve6472.polyground.block.Block;
import steve6472.polyground.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static steve6472.polyground.item.groups.PreviewType.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Groups
{
	public static final String UNGROUPED = "ungrouped";

	public final record Group(IGroupWheelData wheelData, Group parent, HashMap<String, Group> groups, List<IGroupWheelData> items)
	{
		public void addGroup(Group group)
		{
			groups.put(group.wheelData.id(), group);
		}

		public void addItem(Item item)
		{
			items.add(new ItemPreview(item.name(), item.name(), item.name(), ITEM));
		}

		public void addItem(Block block)
		{
			items.add(new PlacerPreview(block.name, block.name, block.name, BLOCK));
		}
	}

	private final Group mainGroups;

	public Groups()
	{
		mainGroups = GroupBuilder
			.create("Main", "main", "null", BLOCK)
			.addChild(GroupBuilder
				.create("Building", "building", "bricks", BLOCK)
				.addChild(GroupBuilder
					.create("Stone", "stone", "stone", BLOCK)
					.addChild(GroupBuilder.create("Cobblestone", "cobblestone", "cobblestone", BLOCK))
					.addChild(GroupBuilder.create("Stone", "stone", "stone", BLOCK)))
				.addChild(GroupBuilder.create("Bricks", "bricks", "bricks", BLOCK))
				.addChild(GroupBuilder.create("Metal", "metal", "iron_block", BLOCK))
				.addChild(GroupBuilder.create("Statues", "statues", "limestone", BLOCK))
				.addChild(GroupBuilder
					.create("Wood", "wood", "oak_log", BLOCK)
					.addChild(GroupBuilder.create("Oak", "oak", "oak_log", BLOCK))
					.addChild(GroupBuilder.create("Birch", "birch", "birch_log", BLOCK))
					.addChild(GroupBuilder.create("Spruce", "spruce", "spruce_log", BLOCK))
					.addChild(GroupBuilder.create("Dark Oak", "dark_oak", "dark_oak_log", BLOCK))
					.addChild(GroupBuilder.create("Jungle", "jungle", "jungle_log", BLOCK))
					.addChild(GroupBuilder.create("Acacia", "acacia", "acacia_log", BLOCK))))
			.addChild(GroupBuilder
				.create("Nature", "nature", "pebbles", BLOCK)
				.addChild(GroupBuilder.create("Desert", "desert", "sand", BLOCK))
				.addChild(GroupBuilder.create("Forest", "forest", "oak_leaves", BLOCK))
				.addChild(GroupBuilder.create("Taiga", "taiga", "snow_layer", BLOCK))
				.addChild(GroupBuilder.create("Hills", "hills", "gravel", BLOCK))
				.addChild(GroupBuilder.create("Leaves", "leaves", "oak_leaves", BLOCK))
				.addChild(GroupBuilder.create("Cave", "cave", "amethine", BLOCK)))
			.addChild(GroupBuilder.create("Misc", "misc", "wooden_box", BLOCK))
			.addChild(GroupBuilder
				.create("Items", "items", "brush", ITEM)
				.addChild(GroupBuilder.create("Powders", "powders", "cyan_powder", ITEM))
				.addChild(GroupBuilder.create("Logic", "logic", "high_constant", ITEM)
					.addChild(GroupBuilder.create("Gates", "gates", "and_gate", ITEM)))
				.addChild(GroupBuilder.create("B", "b", "stone", BLOCK)))
			.addChild(GroupBuilder.create("Ungrouped", "ungrouped", "null", BLOCK))
			.generate(null);
	}

	private Group findGroup(String groupPath)
	{
		String[] split = groupPath.split("\\.");

		Group current = mainGroups;

		for (String s : split)
		{
			current = current.groups.get(s);
		}
		return current;
	}

	public void add(Item item, String groupPath)
	{
		Group group = findGroup(groupPath);

		if (group == null)
		{
//			throw new NullPointerException("Group '" + groupPath + "' does not exist, skipping item '" + item.name() + "'!");
			System.err.println("Group '" + groupPath + "' does not exist, skipping item '" + item.name() + "'!");
		} else
		{
			group.addItem(item);
		}
	}

	public void add(Block item, String groupPath)
	{
		Group group = findGroup(groupPath);

		if (group == null)
		{
//			throw new NullPointerException("Group '" + groupPath + "' does not exist, skipping blockPlacer '" + item.name + "'!");
			System.err.println("Group '" + groupPath + "' does not exist, skipping blockPlacer '" + item.name + "'!");
		} else
		{
			group.addItem(item);
		}
	}

	public void addUngrouped(Item item)
	{
		mainGroups.groups.get(UNGROUPED).addItem(item);
	}

	public void addUngrouped(Block block)
	{
		mainGroups.groups.get(UNGROUPED).addItem(block);
	}

	public Group getRoot()
	{
		return mainGroups;
	}


	private static class GroupBuilder
	{
		private final String name, preview, id;
		private final PreviewType previewType;
		private final List<GroupBuilder> children;

		private GroupBuilder(String name, String id, String preview, PreviewType previewType)
		{
			children = new ArrayList<>();
			this.name = name;
			this.id = id;
			this.preview = preview;
			this.previewType = previewType;
		}

		public static GroupBuilder create(String name, String id, String preview, PreviewType previewType)
		{
			return new GroupBuilder(name, id, preview, previewType);
		}

		public GroupBuilder addChild(GroupBuilder child)
		{
			children.add(child);
			return this;
		}

		public Group generate(Group parent)
		{
			IGroupWheelData data = switch (previewType)
				{
					case ITEM -> new ItemPreview(name, id, preview, GROUP);
					case BLOCK -> new PlacerPreview(name, id, preview, GROUP);
					default -> throw new IllegalStateException("Unexpected value: " + previewType);
				};

			Group group = new Group(data, parent, new HashMap<>(), new ArrayList<>());

			for (GroupBuilder builder : children)
			{
				group.addGroup(builder.generate(group));
			}

			return group;
		}
	}
}
