package steve6472.polyground.block.tree;

import steve6472.polyground.EnumFace;

import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.10.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockPos
{
	private int x;
	private int y;
	private int z;

	public BlockPos(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockPos()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public BlockPos(BlockPos other)
	{
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	public BlockPos offset(EnumFace face)
	{
		north(face.getXOffset());
		up(face.getYOffset());
		east(face.getZOffset());
		return this;
	}

	public BlockPos up(int n)
	{
		this.y += n;
		return this;
	}

	public BlockPos up()
	{
		return up(1);
	}

	public BlockPos down(int n)
	{
		this.y -= n;
		return this;
	}

	public BlockPos down()
	{
		return down(1);
	}

	public BlockPos north(int n)
	{
		this.x += n;
		return this;
	}

	public BlockPos north()
	{
		return north(1);
	}

	public BlockPos east(int n)
	{
		this.z += n;
		return this;
	}

	public BlockPos east()
	{
		return east(1);
	}

	public BlockPos south(int n)
	{
		this.x -= n;
		return this;
	}

	public BlockPos south()
	{
		return south(1);
	}

	public BlockPos west(int n)
	{
		this.z -= n;
		return this;
	}

	public BlockPos west()
	{
		return west(1);
	}

	public void setPos(int x, int y, int z)
	{
		setX(x);
		setY(y);
		setZ(z);
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getZ()
	{
		return z;
	}

	public void setZ(int z)
	{
		this.z = z;
	}

	public boolean equals(int x, int y, int z)
	{
		return this.x == x && this.y == y && this.z == z;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		BlockPos blockPos = (BlockPos) o;
		return x == blockPos.x && y == blockPos.y && z == blockPos.z;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(x, y, z);
	}
}
