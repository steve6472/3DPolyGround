package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import org.joml.Vector3f;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.arguments.ItemArgument;
import steve6472.polyground.commands.arguments.Vec3fArgument;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.item.Item;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ItemCommand extends Command
{
	public ItemCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("item").then(argument("item", ItemArgument.item()).then(argument("position", Vec3fArgument
			.vec3()).executes(c ->
		{
			Item item = ItemArgument.getItem(c, "item");
			Vector3f pos = Vec3fArgument.getCoords(c, "position");

			ItemEntity entity = new ItemEntity(item, null, pos.x, pos.y, pos.z);
			c.getSource().getWorld().getEntityManager().addEntity(entity);
			return 0;
		}))));
	}
}
