package steve6472.polyground.teleporter;

import steve6472.polyground.entity.player.Player;
import org.joml.AABBf;

import java.util.UUID;

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
	public final UUID uuid;
	private String name;

	public Teleporter(UUID uuid)
	{
		this.uuid = uuid;
		canTeleport = true;
	}

	public Teleporter()
	{
		canTeleport = true;
		uuid = UUID.randomUUID();
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
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
		if (getOther() == null)
			return;

		boolean playerFlag = player.getHitbox().getHitbox().intersectsAABB(aabb);

		if (playerFlag && canTeleport)
		{
			player.setPosition(other.aabb.minX - aabb.minX + player.getX(), other.aabb.minY - aabb.minY + player.getY() + 0.000001f, other.aabb.minZ - aabb.minZ + player.getZ());
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

	public UUID getUuid()
	{
		return uuid;
	}
}
