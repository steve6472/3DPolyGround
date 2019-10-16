package com.steve6472.polyground;

import org.joml.AABBf;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 5.9.2018
 * Project: 3DTest
 *
 ***********************/
public class Frustum
{
	private final Matrix4f prjViewMatrix;

	private FrustumIntersection frustumInt;

	public Frustum()
	{
		prjViewMatrix = new Matrix4f();
		frustumInt = new FrustumIntersection();
	}

	public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix)
	{
		prjViewMatrix.set(projMatrix);
		prjViewMatrix.mul(viewMatrix);

		frustumInt.set(prjViewMatrix);
	}

	public boolean insideFrsutum(float x, float y, float z, float radius)
	{
		return frustumInt.testSphere(x, y, z, radius);
	}

	public boolean insideFrsutum(float sx, float sy, float sz, float ex, float ey, float ez)
	{
		return frustumInt.testAab(sx, sy, sz, ex, ey, ez);
	}

	public boolean insideFrsutum(AABBf aabb)
	{
		return frustumInt.testAab(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
	}
}
