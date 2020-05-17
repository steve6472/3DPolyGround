package steve6472.polyground;

import steve6472.polyground.gfx.MainRender;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.game.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.12.2018
 * Project: PolyGround
 *
 ***********************/
public class PolyUtil
{
	private static final float FAR_PLANE = 1024f;
	private static final double RAD_90 = Math.PI / 2.0;

	public static Matrix4f createProjectionMatrix(MainApp app)
	{
		return createProjectionMatrix(app.getWidth(), app.getHeight(), FAR_PLANE);
	}

	public static Matrix4f createProjectionMatrix(float width, float height)
	{
		return createProjectionMatrix(width, height, FAR_PLANE);
	}

	public static Matrix4f createProjectionMatrix(float width, float height, float farPlane, float fov)
	{
		Matrix4f projectionMatrix = new Matrix4f();
		createProjectionMatrix(projectionMatrix, width, height, farPlane, fov);
		return projectionMatrix;
	}

	public static void createProjectionMatrix(Matrix4f destination, float width, float height, float farPlane, float fov)
	{
		final float NEAR_PLANE = 0.1f;
		float aspectRatio = width / height;

		destination.perspective((float) Math.toRadians(fov), aspectRatio, NEAR_PLANE, farPlane);
	}

	public static void toDirectionalVector(Camera camera, Vector3f dest)
	{
		float xzLen = (float) Math.cos(camera.getPitch());
		float x = (float) (xzLen * Math.cos(camera.getYaw() + RAD_90));
		float y = (float) Math.sin(camera.getPitch());
		float z = (float) (xzLen * -Math.sin(camera.getYaw() + RAD_90));
		dest.set(x, y, z);
	}

	public static Matrix4f createProjectionMatrix(float width, float height, float farPlane)
	{
		return createProjectionMatrix(width, height, farPlane, 80);
	}

	public static void calculateRay(Vector3f ray, MainApp main)
	{
		float mouseX = main.getMouseX();
		float mouseY = main.getHeight() - main.getMouseY();
		float norCordX = (2f * mouseX) / (float) main.getWidth() - 1;
		float norCordY = (2f * mouseY) / (float) main.getHeight() - 1;
		Vector4f clipCoords = new Vector4f(norCordX, norCordY, -1F, 1F);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		toWorldCoords(eyeCoords, ray);
	}

	public static void calculateRay(Vector3f ray, MainApp main, int mx, int my)
	{
		float mouseX = mx;
		float mouseY = main.getHeight() - my;
		float norCordX = (2f * mouseX) / (float) main.getWidth() - 1;
		float norCordY = (2f * mouseY) / (float) main.getHeight() - 1;
		Vector4f clipCoords = new Vector4f(norCordX, norCordY, -1F, 1F);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		toWorldCoords(eyeCoords, ray);
	}

	private static void toWorldCoords(Vector4f eyeCoords, Vector3f ray)
	{
		Matrix4f invertedView = new Matrix4f(CaveGame.getInstance().getCamera().getViewMatrix()).invert();
		Vector4f v = invertedView.transform(eyeCoords);
		ray.set(v.x, v.y, v.z);
		ray.normalize();
	}

	private static Vector4f toEyeCoords(Vector4f clipCoords)
	{
		Matrix4f invertedProjection = new Matrix4f(MainRender.shaders.getProjectionMatrix()).invert();
		Vector4f eyeCoords = invertedProjection.transform(clipCoords);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	public static void updateDirectionRay(Vector3f dir, Camera camera)
	{
		double yaw = camera.getYaw();
		double pitch = camera.getPitch();

		float rx, ry, rz;
		rx = (float) (-Math.sin(yaw) * (Math.cos(pitch)));
		ry = (float) (Math.sin(pitch));
		rz = (float) (-Math.cos(yaw) * (Math.cos(pitch)));

		dir.set(rx, ry, rz);
	}

	public static void toScreenPos(Vector3f worldPos, Vector2f destination)
	{
		Matrix4f viewMatrix = CaveGame.getInstance().getCamera().getViewMatrix();
		Matrix4f projectionMatrix = MainRender.shaders.getProjectionMatrix();

		Vector4f clipSpacePos = new Vector4f(worldPos, 1.0f).mul(viewMatrix).mul(projectionMatrix);
		Vector3f ndcSpacePos = new Vector3f(clipSpacePos.x, clipSpacePos.y, clipSpacePos.z).div(clipSpacePos.w);

		Vector2f viewSize = new Vector2f(CaveGame.getInstance().getWidth(), CaveGame.getInstance().getHeight());

		destination.set(((ndcSpacePos.x + 1.0f) / 2.0f) * viewSize.x, ((1.0f - ndcSpacePos.y) / 2.0f) * viewSize.y);
	}
}
