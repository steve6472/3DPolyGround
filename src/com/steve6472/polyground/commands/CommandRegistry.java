package com.steve6472.polyground.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.commands.coms.*;
import com.steve6472.polyground.commands.coms.worldEdit.SetCommand;

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
		new SnapCommand(dispatcher);
		new SaveWorldCommand(dispatcher);
		new LoadWorldCommand(dispatcher);
		new SetWorldName(dispatcher);
		new HelpCommand(dispatcher);
		new BlocksCommand(dispatcher);
		new SpawnCommand(dispatcher);
		new AnimateChunkCommand(dispatcher);
		new SetCommand(dispatcher);
		new SensitivityCommand(dispatcher);
		new AICommand(dispatcher);
		new ModelLayerCommand(dispatcher);
		new ParticleCommand(dispatcher);
	}
}
