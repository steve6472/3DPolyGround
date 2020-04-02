package steve6472.polyground.entity;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2019
 * Project: SJP
 *
 ***********************/
public class Interpolator3D
{
	private Interpolator x, y, z;

	public Interpolator3D()
	{
		x = new Interpolator();
		y = new Interpolator();
		z = new Interpolator();
	}

	public void set(float x, float y, float z)
	{
		setX(x);
		setY(y);
		setZ(z);
	}

	public void set(float x, float y, float z, long time)
	{
		setX(x, time);
		setY(y, time);
		setZ(z, time);
	}

	public void setX(float x)
	{
		this.x.set(x);
	}

	public void setY(float y)
	{
		this.y.set(y);
	}

	public void setZ(float z)
	{
		this.z.set(z);
	}

	public void setX(float x, long time)
	{
		this.x.set(x, time);
	}

	public void setY(float y, long time)
	{
		this.y.set(y, time);
	}

	public void setZ(float z, long time)
	{
		this.z.set(z, time);
	}

	public boolean isXFinished()
	{
		return x.finished();
	}

	public boolean isYFinished()
	{
		return y.finished();
	}

	public boolean isZFinished()
	{
		return z.finished();
	}

	public boolean isAllFinished()
	{
		return isXFinished() && isYFinished() && isZFinished();
	}

	public float getX()
	{
		return this.x.getValue();
	}

	public float getY()
	{
		return this.y.getValue();
	}

	public float getZ()
	{
		return this.z.getValue();
	}

	@Override
	public String toString()
	{
		return "Interpolator3D{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
	}
}
