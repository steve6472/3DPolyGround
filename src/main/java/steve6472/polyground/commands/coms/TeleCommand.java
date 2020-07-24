package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import org.joml.AABBf;
import org.joml.Vector3f;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.arguments.TeleporterArgument;
import steve6472.polyground.commands.arguments.Vec3fArgument;
import steve6472.polyground.teleporter.Teleporter;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.05.2020
 * Project: CaveGame
 *
 ***********************/
public class TeleCommand extends Command
{
	public TeleCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public static Teleporter tele;

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("tele")
			.then(literal("start")
				.then(argument("name", string())
					.executes(c -> {

						if (tele == null)
						{
							tele = new Teleporter();
							tele.setName(getString(c, "name"));
							tele.setAabb(new AABBf());
							c.getSource().getWorld().teleporters.addTeleporter(tele);
							c.getSource().sendFeedback("[#55ff55]", "Left/Right click with the item to place corners (Hold C to corner-snap)");
							return 1;
						} else
						{
							c.getSource().sendFeedback("[#FF5555]", "Teleporter is already being created!");
							return 0;
						}
					})
				)
			).then(literal("pos")
				.then(literal("min")
					.then(argument("pos", Vec3fArgument.vec3())
						.executes(c -> {
							if (tele == null)
							{
								c.getSource().sendFeedback("[#FF5555]", "No rift is being created!");
								return 0;
							}

							Vector3f v = Vec3fArgument.getCoords(c, "pos");
							tele.setAabb(tele.getAabb().setMin(v));

							return 1;
						})
					)
				)
				.then(literal("max")
					.then(argument("pos", Vec3fArgument.vec3())
						.executes(c -> {
							if (tele == null)
							{
								c.getSource().sendFeedback("[#FF5555]", "No rift is being created!");
								return 0;
							}

							Vector3f v = Vec3fArgument.getCoords(c, "pos");
							tele.setAabb(tele.getAabb().setMax(v));

							return 1;
						})
					)
				)
			).then(literal("finish")
				.executes(c -> {
					if (tele == null)
					{
						c.getSource().sendFeedback("[#FF5555]", "No rift is being created!");
						return 0;
					}

					tele = null;
					return 1;
				})
			).then(literal("bind")
				.then(argument("teleporterA", TeleporterArgument.teleporter())
					.then(argument("teleporterB", TeleporterArgument.teleporter())
						.executes(c -> {
							Teleporter a = TeleporterArgument.getTeleporter(c, "teleporterA");
							Teleporter b = TeleporterArgument.getTeleporter(c, "teleporterB");
							a.setOther(b);
							b.setOther(a);

							return 1;
						})
					).executes(c -> {
						Teleporter other = TeleporterArgument.getTeleporter(c, "teleporterA");
						tele.setOther(other);
						other.setOther(tele);

						return 1;
					})
				)
			).then(literal("delete")
				.then(argument("teleporter", TeleporterArgument.teleporter())
					.executes(c -> {
						Teleporter t = TeleporterArgument.getTeleporter(c, "teleporter");
						if (t.getOther() != null)
							t.getOther().setOther(null);
						c.getSource().getWorld().teleporters.removeTemeporter(t);

						return 1;
					})
				)
			).then(literal("edit")
				.then(argument("teleporter", TeleporterArgument.teleporter())
					.executes(c -> {
						tele = TeleporterArgument.getTeleporter(c, "teleporter");
						return 1;
					})
				)
			)




			.then(literal("print")
				.executes(c -> {

					int i = 0;
					List<Teleporter> teleporters = c.getSource().getWorld().teleporters.getTeleporters();
					for (; i < teleporters.size(); i++)
					{
						Teleporter t = teleporters.get(i);
						System.out.printf("Uuid: %s, Other: %s, AABB: %s\n", t.uuid, t.getOther().getUuid(), t.getAabb());
					}
					return i;
				})
			)
		);
	}
}
