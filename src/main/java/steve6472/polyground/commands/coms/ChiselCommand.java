package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import org.joml.Vector3f;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.itemdata.IItemData;
import steve6472.polyground.item.itemdata.ItemData;
import steve6472.polyground.registry.Items;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ChiselCommand extends Command
{
	public ChiselCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("chisel")
		.executes(c -> {

			World world = c.getSource().getWorld();
			Vector3f position = c.getSource().getPlayer().getPosition();

			Item brushItem = Items.getItemByName("chisel_tool");
			if (brushItem == null)
				return 0;

			ItemData data = null;

			if (brushItem instanceof IItemData id)
				data = id.createNewItemData();

			ItemEntity itemEntity = new ItemEntity(brushItem, data, position.x, position.y, position.z);

			world.getEntityManager().addEntity(itemEntity);

			return 1;
		}));
	}
}
