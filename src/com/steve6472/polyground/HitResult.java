package com.steve6472.polyground;

import com.steve6472.polyground.block.Block;
import org.joml.AABBf;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.08.2019
 * Project: SJP
 *
 ***********************/
public class HitResult
{
	private int x, y, z;
	private float px, py, pz;
	private float distance;
	private EnumFace face;
	private Block block;
	private AABBf aabb;

	public HitResult()
	{
		face = EnumFace.NONE;
	}

	public void setBlockCoords(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setPreciseCoords(float px, float py, float pz)
	{
		this.px = px;
		this.py = py;
		this.pz = pz;
	}

	public EnumFace getFace()
	{
		return face;
	}

	public void setFace(EnumFace face)
	{
		this.face = face;
	}

	public void setDistance(float distance)
	{
		this.distance = distance;
	}

	public void setBlock(Block block)
	{
		this.block = block;
	}

	public void setAabb(AABBf aabb)
	{
		this.aabb = aabb;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getZ()
	{
		return z;
	}

	public float getPx()
	{
		return px;
	}

	public float getPy()
	{
		return py;
	}

	public float getPz()
	{
		return pz;
	}

	public float getDistance()
	{
		return distance;
	}

	public Block getBlock()
	{
		return block;
	}

	public AABBf getAabb()
	{
		return aabb;
	}
}
