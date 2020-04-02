package steve6472.polyground.generator.creator.dialogs;

import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import steve6472.polyground.block.model.registry.Cube;
import steve6472.polyground.block.model.registry.face.FaceRegistry;
import steve6472.polyground.generator.creator.BlockCreatorGui;
import steve6472.polyground.generator.creator.ICreatorCube;
import steve6472.polyground.generator.creator.components.SomeSlider;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.NamedCheckBox;
import steve6472.sge.gui.components.Slider;
import steve6472.sge.gui.components.TextField;
import steve6472.sge.gui.components.dialog.OkDialog;
import steve6472.sge.main.MainApp;

import java.util.function.Consumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.10.2019
 * Project: SJP
 *
 ***********************/
public class EditCubeDialog extends OkDialog
{
	private SomeSlider fromX, fromY, fromZ, toX, toY, toZ;
	private NamedCheckBox hitbox, collisionBox;
	private Button delete;
	private TextField name;

	private ICreatorCube cube;
	private BlockCreatorGui creatorGui;

	public EditCubeDialog(Cube cube, BlockCreatorGui creatorGui)
	{
		super(" ", "Edit Cube");
		this.creatorGui = creatorGui;
		height = 202;
		width = 274;

		if (!(cube instanceof ICreatorCube))
		{
			throw new IllegalArgumentException(cube.getClass().getName() + " is not instace of ICreatorCube\n" + cube);
		} else
		{
			this.cube = ((ICreatorCube) cube);
		}

	}

	@Override
	public void init(MainApp main)
	{
		super.init(main);

		(fromX = addSlider(0, 0, c -> cube.getCube().getAabb().minX = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().minX * 16f));
		(fromY = addSlider(0, 25, c -> cube.getCube().getAabb().minY = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().minY * 16f));
		(fromZ = addSlider(0, 50, c -> cube.getCube().getAabb().minZ = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().minZ * 16f));

		(toX = addSlider(130, 0, c -> cube.getCube().getAabb().maxX = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().maxX * 16f));
		(toY = addSlider(130, 25, c -> cube.getCube().getAabb().maxY = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().maxY * 16f));
		(toZ = addSlider(130, 50, c -> cube.getCube().getAabb().maxZ = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().maxZ * 16f));

		name = new TextField();
		name.setText(cube.getName());
		name.endCarret();
		name.setRelativeLocation(12, 129);
		name.setSize(250, 25);
		addComponent(name);

		ok.addClickEvent(c ->
		{
			if (!name.getText().isBlank())
			{
				cube.setName(name.getText());
			} else
			{
				cube.setName("Unnamed Cube");
			}
		});
	}

	private SomeSlider addSlider(int x, int y, Consumer<Slider> accept)
	{
		SomeSlider ni = new SomeSlider();
		ni.setMaxValue(16);
		ni.setRelativeLocation(19 + x, 39 + y);
		ni.addChangeEvent(accept);
		ni.addChangeEvent(c ->
		{

			for (CubeFace f : cube.getCube().getFaces())
			{
				if (f != null)
				{
					if (AutoUVFaceProperty.check(f))
						f.getProperty(FaceRegistry.uv).autoUV(f.getParent(), f.getFace());
				}
			}

		});
		ni.snap = true;
		addComponent(ni);

		return ni;
	}

	@Override
	public void tick()
	{
		super.tick();
	}

	@Override
	public void render()
	{
		super.render();

		SpriteRender.renderFrame(this, "From", 12, 29, 120, 90);

		SpriteRender.renderFrame(this, "To", 142, 29, 120, 90);
	}
}
