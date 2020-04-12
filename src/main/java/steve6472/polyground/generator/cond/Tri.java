package steve6472.polyground.generator.cond;

class Tri
{
	float x0, x1, x2, y0, y1, y2, z0, z1, z2;
	float r, g, b, a;

	float cx, cy, cz;

	public Tri(float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a)
	{
		this.x0 = x0;
		this.y0 = y0;
		this.z0 = z0;
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Tri mul(float m)
	{
		r *= m;
		g *= m;
		b *= m;
		return this;
	}

	public void setPos(float x, float y, float z)
	{
		this.x0 = x0 + x;
		this.y0 = y0 + y;
		this.z0 = z0 + z;
		this.x1 = x1 + x;
		this.y1 = y1 + y;
		this.z1 = z1 + z;
		this.x2 = x2 + x;
		this.y2 = y2 + y;
		this.z2 = z2 + z;

		cx = (x0 + x1 + x2) / 3f;
		cy = (y0 + y1 + y2) / 3f;
		cz = (z0 + z1 + z2) / 3f;
	}
}