package steve6472.polyground.commands;

import steve6472.polyground.entity.Player;
import steve6472.polyground.gui.GameChat;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class CommandSource
{
	private Player player;
	private World world;
	private GameChat chat;

	public CommandSource(Player player, World world, GameChat chat)
	{
		this.player = player;
		this.world = world;
		this.chat = chat;
	}

	public World getWorld()
	{
		return world;
	}

	public Player getPlayer()
	{
		return player;
	}

	public GameChat getChat()
	{
		return chat;
	}

	public void sendFeedback(Object... text)
	{
		chat.addText(text);
	}
}
