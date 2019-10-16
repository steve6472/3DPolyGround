package com.steve6472.polyground.entity;

import com.steve6472.polyground.AABBUtil;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.world.interaction.PlayerCollider;
import com.steve6472.sge.main.game.mixable.IMotion3f;
import com.steve6472.sge.main.game.mixable.IPosition3f;
import org.joml.AABBf;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class EntityHitbox
{
	private AABBf hitbox, expandedHitbox;
	private float w, h, d;
	private List<AABBf> worldCollisionCheck;

	public EntityHitbox(float w, float h, float d)
	{
		hitbox = new AABBf();
		expandedHitbox = new AABBf();
		worldCollisionCheck = new ArrayList<>();
		this.w = w;
		this.h = h;
		this.d = d;
	}

	public AABBf getHitbox()
	{
		return hitbox;
	}

	public AABBf getExpandedHitbox()
	{
		return expandedHitbox;
	}

	public void setHitbox(float x, float y, float z)
	{
		hitbox.setMax(w + x, h + y, d + z);
		hitbox.setMin(-w + x, -h + y, -d + z);
	}

	public void moveHitbox(float x, float y, float z)
	{
		hitbox.minX += x;
		hitbox.maxX += x;
		hitbox.minY += y;
		hitbox.maxY += y;
		hitbox.minZ += z;
		hitbox.maxZ += z;
	}

	public void expand(float x, float y, float z)
	{
		expandedHitbox.minX = x < 0.0f ? hitbox.minX + x : hitbox.minX;
		expandedHitbox.maxX = x > 0.0f ? hitbox.maxX + x : hitbox.maxX;
		expandedHitbox.minY = y < 0.0f ? hitbox.minY + y : hitbox.minY;
		expandedHitbox.maxY = y > 0.0f ? hitbox.maxY + y : hitbox.maxY;
		expandedHitbox.minZ = z < 0.0f ? hitbox.minZ + z : hitbox.minZ;
		expandedHitbox.maxZ = z > 0.0f ? hitbox.maxZ + z : hitbox.maxZ;
	}

	/**
	 *
	 * @param motion Motion
	 * @param position Position
	 * @return isOnGround
	 */
	public boolean collideWithWorld(IMotion3f motion, IPosition3f position)
	{
		expand(motion.getMotionX(), motion.getMotionY(), motion.getMotionZ());

//		for (ParticleHitbox particleHitbox : pg.hitboxList)
//		{
//			if (particleHitbox.getHitbox().testAABB(getExpandedHitbox()))
//			{
//				worldCollisionCheck.add(particleHitbox.getHitbox());
//			}
//		}

		PlayerCollider.checkPlayerBlockCollisions(CaveGame.getInstance().world, worldCollisionCheck, getExpandedHitbox(), position.getX(), position.getY(), position.getZ());

		boolean isOnGround = false;

		if (worldCollisionCheck.size() != 0)
		{
			isOnGround = move(worldCollisionCheck, motion.getMotionX(), motion.getMotionY(), motion.getMotionZ(), motion, position);
		}

		worldCollisionCheck.clear();

		return isOnGround;
	}

	/**
	 *
	 * @param blocks List of blocks the Expanded Hitbox is colliding with
	 * @param xa X Motion
	 * @param ya Y Motion
	 * @param za Z Motion
	 * @param motion Motion
	 * @param position Position
	 *
	 * @return isOnGround
	 */
	public boolean move(List<AABBf> blocks, float xa, float ya, float za, IMotion3f motion, IPosition3f position)
	{
		float xaOrg = xa;
		float yaOrg = ya;
		float zaOrg = za;

		int i;
		for (i = 0; i < blocks.size(); i++)
			ya = AABBUtil.clipYCollide(getHitbox(), blocks.get(i), ya);
		moveHitbox(0.0f, ya, 0.0f);

		for (i = 0; i < blocks.size(); i++)
			xa = AABBUtil.clipXCollide(getHitbox(), blocks.get(i), xa);
		moveHitbox(xa, 0.0F, 0.0F);

		for (i = 0; i < blocks.size(); i++)
			za = AABBUtil.clipZCollide(getHitbox(), blocks.get(i), za);
		moveHitbox(0.0F, 0.0F, za);

		if (xaOrg != xa)
		{
			motion.setMotionX(0.0f);
		}

		if (yaOrg != ya)
		{
			motion.setMotionY(0.0f);
		}

		if (zaOrg != za)
		{
			motion.setMotionZ(0.0f);
		}

		position.setX((getHitbox().minX + getHitbox().maxX) / 2.0F);
		position.setY(getHitbox().minY + 0.0F);
		position.setZ((getHitbox().minZ + getHitbox().maxZ) / 2.0F);

		return yaOrg != ya && yaOrg < 0.0F;
	}
}
