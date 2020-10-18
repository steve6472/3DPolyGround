package steve6472.polyground.registry;

import com.mojang.brigadier.CommandDispatcher;
import steve6472.polyground.CaveGame;
import steve6472.polyground.commands.CommandSource;
import steve6472.polyground.commands.coms.worldEdit.SetCommand;
import steve6472.polyground.commands.coms.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.09.2019
 * Project: SJP
 *
 ***********************/
public class CommandRegistry
{
	public CommandDispatcher<CommandSource> dispatcher;
	public CommandSource commandSource;

	public CommandRegistry()
	{
		init();
	}

	private void init()
	{
		dispatcher = new CommandDispatcher<>();
		commandSource = new CommandSource(CaveGame.getInstance().getPlayer(), CaveGame.getInstance().world, CaveGame.getInstance().inGameGui.chat);

		new TpCommand(dispatcher);
		new ChunkCommand(dispatcher);
		new FillCommand(dispatcher);
		new SpeedCommand(dispatcher);
		new SaveWorldCommand(dispatcher);
		new LoadWorldCommand(dispatcher);
		new SetWorldName(dispatcher);
		new HelpCommand(dispatcher);
		new DumpCommand(dispatcher);
		new SetCommand(dispatcher);
		new SensitivityCommand(dispatcher);
		new ModelLayerCommand(dispatcher);
		new ParticleCommand(dispatcher);
		new RiftCommand(dispatcher);
		new TestSpeedCommand(dispatcher);
		new TeleCommand(dispatcher);
		new RandomTicksCommand(dispatcher);
		new LightCommand(dispatcher);
		new GamemodeCommand(dispatcher);
		new BrushCommand(dispatcher);
		new ChiselCommand(dispatcher);
		new PrintNBTCommand(dispatcher);
	}
}
