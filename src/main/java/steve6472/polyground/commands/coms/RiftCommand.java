package steve6472.polyground.commands.coms;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.CaveGame;
import steve6472.polyground.commands.Command;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.arguments.RiftArgument;
import steve6472.polyground.commands.arguments.Vec3fArgument;
import steve6472.polyground.rift.Rift;
import steve6472.polyground.rift.RiftModel;
import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.11.2019
 * Project: SJP
 *
 ***********************/
public class RiftCommand extends Command
{
	public RiftCommand(CommandDispatcher<CommandSource> dispatcher)
	{
		super(dispatcher);
	}

	public static Rift rift;

	@Override
	protected void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(literal("rift").then(literal("print").executes(c ->
		{

			for (Rift r : CaveGame.getInstance().getRifts().getRifts())
			{
				c.getSource().sendFeedback(r.getName());
			}

			return 1;
		})).then(literal("start").then(argument("name", string()).executes(c ->
		{
			if (rift == null)
			{
				rift = new Rift(getString(c, "name"), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 0, 0, new RiftModel());
				rift.getModel().setupModel();
				CaveGame.getInstance().getRifts().addRift(rift);
				return 1;
			} else
			{
				c.getSource().sendFeedback("[#FF5555]", "Rift is already being created!");
				return 0;
			}
		}))).then(literal("finish").executes(c ->
		{

			if (rift == null)
			{
				c.getSource().sendFeedback("[#FF5555]", "No rift is being created!");
				return 0;
			} else
			{
				rift.getModel().updateModel();
				rift.setFinished(true);
				rift = null;
				return 1;
			}
		})).then(literal("delete").then(argument("rift", RiftArgument.rift()).executes(c ->
		{

			if (rift != null)
			{
				c.getSource().sendFeedback("[#FF5555]", "Rift is being created!");
				return 0;
			} else
			{
				Rift r = RiftArgument.getRift(c, "rift");

				boolean removed = CaveGame.getInstance().getRifts().removeRift(r.getName());
				if (removed)
				{
					c.getSource().sendFeedback("[#55ff55]", "Succesfully removed rift '", "[#ffffff]", r.getName(), "[#55ff55]", "'");
					return 1;
				} else
				{
					c.getSource().sendFeedback("[#FF5555]", "Can not find rift '", "[#ffffff]", r.getName(), "[#ff5555]", "'");
					return 0;
				}
			}
		}))).then(literal("cancel").executes(c ->
		{

			if (rift == null)
			{
				c.getSource().sendFeedback("[#FF5555]", "No rift is being created!");
				return 0;
			} else if (rift.isFinished())
			{
				c.getSource().sendFeedback("[#FF5555]", "Selected rift has been finished. To delete pelase use \"rift delete <name>\"");
				return 0;
			} else
			{
				String name = rift.getName();
				CaveGame.getInstance().getRifts().removeRift(name);
				rift = null;
				c.getSource().sendFeedback("[#55ff55]", "Succesfully cancalled rift '", "[#ffffff]", name, "[#55ff55]", "'");
				return 1;
			}
		})).then(literal("select").then(argument("rift", RiftArgument.rift()).executes(c ->
		{

			if (rift != null)
			{
				c.getSource().sendFeedback("[#FF5555]", "Rift is being created!");
				return 0;
			} else
			{
				Rift r = RiftArgument.getRift(c, "rift");

				if (r == null)
				{
					c.getSource().sendFeedback("[#FF5555]", "Can not find rift '", "[#ffffff]", "null", "[#ff5555]", "'");
					return 0;
				} else
				{
					rift = r;
					c.getSource().sendFeedback("[#55ff55]", "Selected rift '", "[#ffffff]", r.getName(), "[#55ff55]", "'");
					return 1;
				}
			}
		}))).then(literal("setrot").then(argument("yaw", integer(0, 360)).then(argument("pitch", integer(0, 360)).executes(c ->
		{

			if (rift == null)
			{
				c.getSource().sendFeedback("[#FF5555]", "Rift is selected!");
				return 0;
			} else
			{
				rift.setYaw((float) Math.toRadians(getInteger(c, "yaw")));
				rift.setPitch((float) Math.toRadians(getInteger(c, "pitch")));
				return 1;
			}
		})))).then(literal("print").then(literal("cam").executes(c ->
		{

			if (rift == null)
			{
				c.getSource().sendFeedback("[#FF5555]", "No rift is being created!");
				return 0;
			} else
			{
				c.getSource().sendFeedback(String.format("Camera is at %.2f, %.2f, %.2f", rift.getX(), rift.getY(), rift.getZ()));
				return 1;
			}
		})).then(literal("corr").executes(c ->
		{

			if (rift == null)
			{
				c.getSource().sendFeedback("[#FF5555]", "No rift is being created!");
				return 0;
			} else
			{
				c.getSource().sendFeedback(String.format("Correction is at %.2f, %.2f, %.2f", rift.getCorrection().x, rift.getCorrection().y, rift.getCorrection().z));
				return 1;
			}
		})).then(literal("code").executes(c ->
		{

			if (rift == null)
			{
				c.getSource().sendFeedback("[#FF5555]", "No rift is selected!");
				return 0;
			} else
			{
				System.out.println("{");
				System.out.println("\tList<Vector3f> vertices = new ArrayList<>();");
				for (Vector3f v : rift.getModel().getVertices())
					System.out.println(String.format("\tvertices.add(new Vector3f(%.2ff, %.2ff, %.2ff));", v.x, v.y, v.z));
				System.out.println(String.format("\tRift portal = new Rift(\"%s\", new Vector3f(%.2ff, %.2ff, %.2ff), new Vector3f(%.2ff, %.2ff, %.2ff), 0, 0, new RiftModel(vertices));", rift.getName(), rift.getX(), rift.getY(), rift.getZ(), rift.getCorrection().x, rift.getCorrection().y, rift.getCorrection().z));
				System.out.println("\tportal.setFinished(true);");
				System.out.println("\tgetRifts().addRift(portal);");
				System.out.println("}");
				return 1;
			}
		}))).then(literal("set").then(literal("cam").then(argument("position", Vec3fArgument.vec3()).executes(c ->
		{

			if (rift == null)
			{
				c.getSource().sendFeedback("[#FF5555]", "No rift is being created!");
				return 0;
			} else
			{
				rift.setPosition(Vec3fArgument.getCoords(c, "position"));
				c.getSource().sendFeedback(String.format("Camera is now at %.2f, %.2f, %.2f", rift.getX(), rift.getY(), rift.getZ()));
				return 1;
			}

		}))).then(literal("corr").then(argument("position", Vec3fArgument.vec3()).executes(c ->
		{

			if (rift == null)
			{
				c.getSource().sendFeedback("[#FF5555]", "No rift is being created!");
				return 0;
			} else
			{
				rift.getCorrection().set(Vec3fArgument.getCoords(c, "position"));
				c.getSource().sendFeedback(String.format("Correction is now at %.2f, %.2f, %.2f", rift.getCorrection().x, rift.getCorrection().y, rift.getCorrection().z));
				return 1;
			}

		})))));
	}
}
