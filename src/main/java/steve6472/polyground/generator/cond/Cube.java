package steve6472.polyground.generator.cond;

import steve6472.sge.main.game.Camera;

import java.util.ArrayList;
import java.util.List;

class Cube implements Comparable<Cube>
{
	List<Tri> tris;
	float x, y, z;
	private Camera camera;

	public Cube(Camera camera, float x, float y, float z, float r, float g, float b, float a, boolean shade)
	{
		this.camera = camera;
		this.x = x;
		this.y = y;
		this.z = z;

		tris = new ArrayList<>(12);
		tris.add(new Tri(-1, +1, -1, -1, -1, -1, -1, -1, +1, r, g, b, a).mul(shade ? 0.8f : 1f));
		tris.add(new Tri(-1, -1, +1, -1, +1, +1, -1, +1, -1, r, g, b, a).mul(shade ? 0.8f : 1f));

		tris.add(new Tri(-1, +1, +1, -1, -1, +1, +1, -1, +1, r, g, b, a).mul(shade ? 0.5f : 1f));
		tris.add(new Tri(+1, -1, +1, +1, +1, +1, -1, +1, +1, r, g, b, a).mul(shade ? 0.5f : 1f));

		tris.add(new Tri(+1, +1, +1, +1, -1, +1, +1, -1, -1, r, g, b, a).mul(shade ? 0.8f : 1f));
		tris.add(new Tri(+1, -1, -1, +1, +1, -1, +1, +1, +1, r, g, b, a).mul(shade ? 0.8f : 1f));

		tris.add(new Tri(+1, +1, -1, +1, -1, -1, -1, -1, -1, r, g, b, a).mul(shade ? 0.5f : 1f));
		tris.add(new Tri(-1, -1, -1, -1, +1, -1, +1, +1, -1, r, g, b, a).mul(shade ? 0.5f : 1f));

		tris.add(new Tri(+1, +1, -1, -1, +1, -1, -1, +1, +1, r, g, b, a));
		tris.add(new Tri(-1, +1, +1, +1, +1, +1, +1, +1, -1, r, g, b, a));

		tris.add(new Tri(-1, -1, +1, -1, -1, -1, +1, -1, -1, r, g, b, a).mul(shade ? 0.2f : 1f));
		tris.add(new Tri(+1, -1, -1, +1, -1, +1, -1, -1, +1, r, g, b, a).mul(shade ? 0.2f : 1f));

		for (Tri t : tris)
		{
			t.setPos(x, y, z);
		}
	}

	@Override
	public int compareTo(Cube o)
	{
		float d0 = camera.getPosition().distance(x, y, z);
		float d1 = camera.getPosition().distance(o.x, o.y, o.z);
		return Float.compare(d1, d0);
	}
}