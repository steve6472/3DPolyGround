package com.steve6472.polyground.world;

import com.steve6472.polyground.EnumFace;

class Vec6
{
	private float x0, y0, z0, x1, y1, z1;

	public void set(float x0, float y0, float z0, float x1, float y1, float z1)
	{
		this.x0 = x0;
		this.y0 = y0;
		this.z0 = z0;
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
	}

	public boolean compare(Vec6 o, boolean x, boolean y, boolean z, EnumFace face)
	{
		boolean f = true;
		if (x) f = f && (x0 == o.x0 && x1 == o.x1); else f = f && (x0 == o.x0 - face.getXOffset() || x1 == o.x1 - face.getXOffset());
		if (y) f = f && (y0 == o.y0 && y1 == o.y1); else f = f && (y0 == o.y0 - face.getXOffset() || y1 == o.y1 - face.getXOffset());
		if (z) f = f && (z0 == o.z0 && z1 == o.z1); else f = f && (z0 == o.z0 - face.getXOffset() || z1 == o.z1 - face.getXOffset());
		return f;
	}

	@Override
	public String toString()
	{
		return "Vec6{" + "x0=" + x0 + ", y0=" + y0 + ", z0=" + z0 + ", x1=" + x1 + ", y1=" + y1 + ", z1=" + z1 + '}';
	}
}