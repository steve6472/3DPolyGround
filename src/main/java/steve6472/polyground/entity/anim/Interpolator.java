package steve6472.polyground.entity.anim;

public class Interpolator
{
	private float valueStart, valueEnd;
	private long startTime, endTime;

	public void set(float valueEnd, long time)
	{
		this.valueStart = this.valueEnd;
		this.valueEnd = valueEnd;
		this.startTime = System.currentTimeMillis();
		this.endTime = time;
	}

	public void set(float value)
	{
		this.valueEnd = value;
	}

	public float getValue()
	{
		if (!finished())
		{
			return calculateValue(System.currentTimeMillis() - startTime, endTime, valueStart, valueEnd);
		} else
		{
			endTime = 0;
			return valueEnd;
		}
	}

	private float calculateValue(float time, float endTime, float valueStart, float valueEnd)
	{
		return (time / endTime) * valueEnd - (time / endTime) * valueStart + valueStart;
	}

	public boolean finished()
	{
		return endTime < (System.currentTimeMillis() - startTime);
	}

	@Override
	public String toString()
	{
		return "Interpolator{" + "value=" + getValue() + '}';
	}
}