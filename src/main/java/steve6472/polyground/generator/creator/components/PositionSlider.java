package steve6472.polyground.generator.creator.components;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import steve6472.modelmaker.components.NumberSliderTriplet;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.model.elements.CubeElement;
import steve6472.polyground.block.model.elements.ElUtil;
import steve6472.polyground.block.model.elements.PlaneElement;
import steve6472.polyground.block.model.elements.TriangleElement;
import steve6472.polyground.generator.creator.CreatorData;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.08.2020
 * Project: CaveGame
 *
 ***********************/
public class PositionSlider extends NumberSliderTriplet
{
	int oldX, oldY, oldZ;

	public IElement element;

	@Override
	public void init(MainApp mainApp)
	{
		super.init(mainApp);

		getSliderX().addChangeEvent(() -> {
			int rv = getSliderX().getRealValue();
			int delta = oldX - rv;
			oldX = rv;

			update(rv, delta, 0, 0);
		});

		getSliderY().addChangeEvent(() -> {
			int rv = getSliderY().getRealValue();
			int delta = oldY - rv;
			oldY = rv;

			update(rv, 0, delta, 0);
		});

		getSliderZ().addChangeEvent(() -> {
			int rv = getSliderZ().getRealValue();
			int delta = oldZ - rv;
			oldZ = rv;

			update(rv, 0, 0, delta);
		});
	}

	public void update(int rv, int dx, int dy, int dz)
	{
		CreatorData data = element.getCreatorData();

		if (dx != 0) data.position.x = rv / 100.0f;
		if (dx != 0) data.position.y = rv / 100.0f;
		if (dx != 0) data.position.z = rv / 100.0f;

		Matrix4f rotMat = ElUtil.rotMat(data.origin.x, data.origin.y, data.origin.z, data.rotation.x, data.rotation.y, data.rotation.z);

		if (element instanceof TriangleElement t)
		{
			t.loadVertices(
				t.v0.add(dx / 100f, dy / 100f, dz / 100f),
				t.v1.add(dx / 100f, dy / 100f, dz / 100f),
				t.v2.add(dx / 100f, dy / 100f, dz / 100f), rotMat);
		}

		if (element instanceof PlaneElement p)
		{
			p.loadVertices(
				p.t0.v0.add(dx / 100f, dy / 100f, dz / 100f),
				p.t0.v1.add(dx / 100f, dy / 100f, dz / 100f),
				p.t0.v2.add(dx / 100f, dy / 100f, dz / 100f),
				p.t1.v1.add(dx / 100f, dy / 100f, dz / 100f), rotMat
			);
		}

		if (element instanceof CubeElement c)
		{
			c.loadVertices(
				new Vector3f(c.down.t0.v0.add(dx / 100f, dy / 100f, dz / 100f)),
				new Vector3f(c.up.t1.v1.add(dx / 100f, dy / 100f, dz / 100f)), rotMat
			);
		}
	}

	public void setElement(IElement element)
	{
		this.element = element;
	}

	@Override
	public void tick()
	{
		super.tick();
	}
}
