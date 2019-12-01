package com.steve6472.polyground.teleporter;

import com.steve6472.polyground.entity.Player;
import org.joml.AABBf;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.11.2019
 * Project: SJP
 *
 ***********************/
public class Teleporter
{
	private Teleporter other;
	private AABBf aabb;
	private boolean canTeleport;

	public Teleporter()
	{
		canTeleport = true;
	}

	public void setOther(Teleporter other)
	{
		this.other = other;
	}

	public void setAabb(AABBf aabb)
	{
		this.aabb = aabb;
	}

	public AABBf getAabb()
	{
		return aabb;
	}

	public void test(Player player)
	{
		boolean playerFlag = player.getHitbox().getHitbox().testAABB(aabb);

		if (playerFlag && canTeleport)
		{
			player.setPosition(
				other.aabb.minX - aabb.minX + player.getX(),
				other.aabb.minY - aabb.minY + player.getY() + 0.000001f,
				other.aabb.minZ - aabb.minZ + player.getZ());
			player.updateHitbox();
			other.canTeleport = false;
		}

		if (!canTeleport && !playerFlag)
		{
			canTeleport = true;
		}
	}

	public Teleporter getOther()
	{
		return other;
	}
}
