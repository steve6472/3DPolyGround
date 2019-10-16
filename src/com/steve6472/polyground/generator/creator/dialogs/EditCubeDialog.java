package com.steve6472.polyground.generator.creator.dialogs;

import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.polyground.generator.creator.BlockCreatorGui;
import com.steve6472.polyground.generator.creator.ICreatorCube;
import com.steve6472.polyground.generator.creator.components.SomeSlider;
import com.steve6472.sge.gfx.SpriteRender;
import com.steve6472.sge.gfx.font.CustomChar;
import com.steve6472.sge.gui.components.Button;
import com.steve6472.sge.gui.components.NamedCheckBox;
import com.steve6472.sge.gui.components.Slider;
import com.steve6472.sge.gui.components.TextField;
import com.steve6472.sge.gui.components.dialog.OkDialog;
import com.steve6472.sge.gui.components.dialog.YesNoDialog;
import com.steve6472.sge.main.MainApp;

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
		height = 227;
		width = 272;

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

		(fromX = addSlider(0, 0,  c -> cube.getCube().getAabb().minX = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().minX * 16f));
		(fromY = addSlider(0, 25, c -> cube.getCube().getAabb().minY = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().minY * 16f));
		(fromZ = addSlider(0, 50, c -> cube.getCube().getAabb().minZ = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().minZ * 16f));

		(toX = addSlider(130, 0,  c -> cube.getCube().getAabb().maxX = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().maxX * 16f));
		(toY = addSlider(130, 25, c -> cube.getCube().getAabb().maxY = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().maxY * 16f));
		(toZ = addSlider(130, 50, c -> cube.getCube().getAabb().maxZ = c.getIValue() / 16f)).setValue((int) (cube.getCube().getAabb().maxZ * 16f));

		hitbox = new NamedCheckBox();
		hitbox.setRelativeLocation(12, 124);
		hitbox.setSize(120, 25);
		hitbox.setText("Hitbox");
		hitbox.setBoxSize(14, 14);
		hitbox.setSelectedChar(CustomChar.CROSS);
		hitbox.setBoxPadding(5, 5);
		hitbox.setToggled(cube.getCube().isHitbox());
		hitbox.addChangeEvent(c -> cube.getCube().setHitbox(c.isToggled()));
		addComponent(hitbox);

		collisionBox = new NamedCheckBox();
		collisionBox.setRelativeLocation(142, 124);
		collisionBox.setSize(120, 25);
		collisionBox.setText("Collision Box");
		collisionBox.setBoxSize(14, 14);
		collisionBox.setSelectedChar(CustomChar.CROSS);
		collisionBox.setBoxPadding(5, 5);
		collisionBox.setToggled(cube.getCube().isCollisionBox());
		collisionBox.addChangeEvent(c -> cube.getCube().setCollisionBox(c.isToggled()));
		addComponent(collisionBox);

		delete = new Button("Delete");
		delete.setRelativeLocation(12, 154);
		delete.setSize(120, 25);
		delete.addClickEvent(c ->
		{
			delete.setEnabled(false);
			ok.setEnabled(false);

			YesNoDialog yn = new YesNoDialog("Do you really want to delete this nice Cube ?\nWhat did it do to you?\nIt never EVER hurt you!\n\nYou Monster", "Murderer");
			getMain().showDialog(yn).center();
			yn.addYesClickEvent(y -> {
				creatorGui.getSelectedBlock().getModel().removeCube(cube.getCube());
				creatorGui.runOnBlockChange();

				close();
			});
			yn.addNoClickEvent(n -> {

				delete.setEnabled(true);
				ok.setEnabled(true);

			});
		});
		addComponent(delete);

		name = new TextField();
		name.setText(cube.getName());
		name.endCarret();
		name.setRelativeLocation(142, 154);
		name.setSize(120, 25);
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

			creatorGui.runOnBlockChange();
			creatorGui.cubeList.select(cube.getIndex());
			creatorGui.runOnCubeListChange();
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
					if (AutoUVFaceProperty.check(f)) f.getProperty(FaceRegistry.uv).autoUV(f.getParent(), f.getFace());
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
