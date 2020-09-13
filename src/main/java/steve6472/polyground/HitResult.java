package steve6472.polyground;

import org.joml.AABBf;
import steve6472.polyground.block.states.BlockState;

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
	private BlockState state;
	private AABBf aabb;
	private boolean hit;

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

	public void setHit(boolean hit)
	{
		this.hit = hit;
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

	public void setState(BlockState state)
	{
		this.state = state;
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

	public int getCx()
	{
		return Math.floorMod(x, 16);
	}

	public int getCy()
	{
		return Math.floorMod(y, 16);
	}

	public int getCz()
	{
		return Math.floorMod(z, 16);
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

	public BlockState getState()
	{
		return state;
	}

	public AABBf getAabb()
	{
		return aabb;
	}

	public boolean isHit()
	{
		return hit;
	}
}
