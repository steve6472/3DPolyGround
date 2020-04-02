package steve6472.polyground.world.light;

import org.joml.Vector3f;
import steve6472.sge.main.game.mixable.IPosition3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.02.2020
 * Project: SJP
 *
 ***********************/
public class Light implements IPosition3f
{
	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation;

	private boolean updatePosition;
	private boolean updateColor;
	private boolean updateAttenuation;

	private EnumLightSource source;

	/**
	 * Position in array
	 */
	private int index;

	private boolean inactive;
	private long lastUpdate;

	/**
	 * Creates inactive light
	 */
	public Light(int index)
	{
		inactive = true;
		this.index = index;
		position = new Vector3f(0, 0, 0);
		color = new Vector3f(0, 0, 0);
		attenuation = new Vector3f(1, 0, 0);
		updateAttenuation();
	}

	public long getLastUpdate()
	{
		return lastUpdate;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public EnumLightSource getSource()
	{
		return source;
	}

	public void setSource(EnumLightSource source)
	{
		this.source = source;
	}

	public boolean isInactive()
	{
		return inactive;
	}

	public void setInactive(boolean inactive)
	{
		this.inactive = inactive;

		if (inactive)
			setColor(0, 0, 0);
	}

	/* Attenuation */

	public void setUpdateAttenuation(boolean updateAttenuation)
	{
		this.updateAttenuation = updateAttenuation;
	}

	public boolean shouldUpdateAttenuation()
	{
		return updateAttenuation;
	}

	public void updateAttenuation()
	{
		this.updateAttenuation = true;
		lastUpdate = System.currentTimeMillis();
	}

	public void setAttenuation(float constant, float linear, float quadratic)
	{
		attenuation.set(constant, linear, quadratic);
		updateAttenuation();
	}

	public Vector3f getAttenuation()
	{
		return attenuation;
	}

	/* Color */

	public void setUpdateColor(boolean updateColor)
	{
		this.updateColor = updateColor;
	}

	public boolean shouldUpdateColor()
	{
		return updateColor;
	}

	public void updateColor()
	{
		this.updateColor = true;
		lastUpdate = System.currentTimeMillis();
	}

	public void setColor(float r, float g, float b)
	{
		color.set(r, g, b);
		updateColor();
	}

	public Vector3f getColor()
	{
		return color;
	}

	/* Position */

	public void setUpdatePosition(boolean updatePosition)
	{
		this.updatePosition = updatePosition;
	}

	public boolean shouldUpdatePosition()
	{
		return updatePosition;
	}

	public void updatePosition()
	{
		this.updatePosition = true;
		lastUpdate = System.currentTimeMillis();
	}

	@Override
	public Vector3f getPosition()
	{
		return position;
	}

	@Override
	public void setPosition(Vector3f position)
	{
		IPosition3f.super.setPosition(position);
		updatePosition();
	}

	@Override
	public void setPosition(float x, float y, float z)
	{
		IPosition3f.super.setPosition(x, y, z);
		updatePosition();
	}

	@Override
	public void addPosition(Vector3f position)
	{
		IPosition3f.super.addPosition(position);
		updatePosition();
	}

	@Override
	public void addPosition(float x, float y, float z)
	{
		IPosition3f.super.addPosition(x, y, z);
		updatePosition();
	}

	@Override
	public void setX(float x)
	{
		IPosition3f.super.setX(x);
		updatePosition();
	}

	@Override
	public void setY(float y)
	{
		IPosition3f.super.setY(y);
		updatePosition();
	}

	@Override
	public void setZ(float z)
	{
		IPosition3f.super.setZ(z);
		updatePosition();
	}

	@Override
	public String toString()
	{
		return "L{" + " a=" + !inactive + ", u=" + lastUpdate + '}';
	}
}
