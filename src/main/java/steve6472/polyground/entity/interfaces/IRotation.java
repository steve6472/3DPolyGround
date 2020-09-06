package steve6472.polyground.entity.interfaces;

import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.09.2020
 * Project: CaveGame
 *
 ***********************/
public interface IRotation
{
	Vector3f getRotations();

	Vector3f getPivotPoint();

	default void setPitch(float pitch)
	{
		getRotations().x = pitch;
	}

	default void setYaw(float yaw)
	{
		getRotations().y = yaw;
	}

	default void setRoll(float roll)
	{
		getRotations().z = roll;
	}

	default void setRotations(float pitch, float yaw, float roll)
	{
		getRotations().set(pitch, yaw, roll);
	}

	default void setPitchAng(float pitchAng)
	{
		getRotations().x = (float) Math.toRadians(pitchAng);
	}

	default void setYawAng(float yawAng)
	{
		getRotations().y = (float) Math.toRadians(yawAng);
	}

	default void setRollAng(float rollAng)
	{
		getRotations().z = (float) Math.toRadians(rollAng);
	}

	default void setPivotPoint(float x, float y, float z)
	{
		getPivotPoint().set(x, y, z);
	}
}
