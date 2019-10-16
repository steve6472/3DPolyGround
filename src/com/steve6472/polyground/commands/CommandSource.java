package com.steve6472.polyground.commands;

import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.world.World;

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

	public CommandSource(Player player, World world)
	{
		this.player = player;
		this.world = world;
	}

	public World getWorld()
	{
		return world;
	}

	public Player getPlayer()
	{
		return player;
	}
}
