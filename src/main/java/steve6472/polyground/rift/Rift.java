package steve6472.polyground.rift;

import steve6472.sge.gfx.DepthFrameBuffer;
import steve6472.sge.main.game.mixable.IPosition3f;
import org.joml.Vector3f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.11.2019
 * Project: SJP
 *
 ***********************/
public class Rift implements IPosition3f
{
	private Vector3f cameraPosition, correction;
	private RiftModel model;
	private final DepthFrameBuffer buffer;
	private float yaw, pitch;
	private final String name;
	private boolean finished;

	public Rift(String name, Vector3f cameraPosition, Vector3f correction, float yaw, float pitch, RiftModel model)
	{
		this.name = name;
		this.cameraPosition = cameraPosition;
		this.correction = correction;
		this.yaw = (float) Math.toRadians(yaw);
		this.pitch = (float) Math.toRadians(pitch);
		this.model = model;
		buffer = new DepthFrameBuffer(16 * 70, 9 * 70);
	}

	@Override
	public Vector3f getPosition()
	{
		return cameraPosition;
	}

	public Vector3f getCorrection()
	{
		return correction;
	}

	public float getYaw()
	{
		return yaw;
	}

	public float getPitch()
	{
		return pitch;
	}

	public void setYaw(float yaw)
	{
		this.yaw = yaw;
	}

	public void setPitch(float pitch)
	{
		this.pitch = pitch;
	}

	public RiftModel getModel()
	{
		return model;
	}

	public DepthFrameBuffer getBuffer()
	{
		return buffer;
	}

	public String getName()
	{
		return name;
	}

	public boolean isFinished()
	{
		return finished;
	}

	public void setFinished(boolean finished)
	{
		this.finished = finished;
	}
}
