package com.steve6472.polyground.generator.creator.components;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.generator.creator.BlockCreatorGui;
import com.steve6472.sge.gui.Component;
import com.steve6472.sge.gui.components.RadioGroup;
import com.steve6472.sge.gui.components.ToggleButton;
import com.steve6472.sge.main.MainApp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.10.2019
 * Project: SJP
 *
 ***********************/
public class FaceList extends Component
{
	private FaceListItem up, down, north, east, south, west;
	private BlockCreatorGui creatorGui;

	public FaceList(BlockCreatorGui creatorGui)
	{
		this.creatorGui = creatorGui;
	}

	@Override
	public void init(MainApp main)
	{
		up = createButton(EnumFace.UP, 0);
		down = createButton(EnumFace.DOWN, 25);
		north = createButton(EnumFace.NORTH, 50);
		east = createButton(EnumFace.EAST, 75);
		south = createButton(EnumFace.SOUTH, 100);
		west = createButton(EnumFace.WEST, 125);

		RadioGroup.addToggleButtons(
			up.getToggleButton(),
			down.getToggleButton(),
			north.getToggleButton(),
			east.getToggleButton(),
			south.getToggleButton(),
			west.getToggleButton());
	}

	private FaceListItem createButton(EnumFace face, int y)
	{
		FaceListItem button = new FaceListItem(creatorGui, face);
		button.setRelativeLocation(0, y);
		button.setSize(200, 25);
		addComponent(button);
		button.getToggleButton().addChangeEvent(this::runFaceChangeEvents);
		button.getVisButton().addChangeEvent(this::runVisChangeEvents);

		return button;
	}

	public FaceListItem[] array()
	{
		return new FaceListItem[] {up, down, north, east, south, west};
	}

	public void iterate(Consumer<FaceListItem> c)
	{
		for (FaceListItem fli : array())
			c.accept(fli);
	}

	public FaceListItem get(EnumFace face)
	{
		for (FaceListItem f : array())
		{
			if (f.getFace() == face) return f;
		}

		return null;
	}

	public void hideAll()
	{
		iterate(c -> c.setVisible(false));
	}

	public void setEnabled(boolean flag)
	{
		iterate(c -> c.setEnabled(flag));
		if (!flag) iterate(c ->
		{
			c.setSelected(false);
			c.setVisible(false);
		});
	}

	public FaceListItem getSelectedFace()
	{
		for (FaceListItem f : array())
		{
			if (f.isSelected()) return f;
		}

		return null;
	}

	@Override
	public void tick()
	{
		tickComponents();
	}

	@Override
	public void render()
	{
		renderComponents();
	}

	/* Events */

	private List<Consumer<ToggleButton>> visChangeEvent = new ArrayList<>();
	private List<Consumer<ToggleButton>> faceChangeEvent = new ArrayList<>();

	public void addVisChangeEvent(Consumer<ToggleButton> c)
	{
		visChangeEvent.add(c);
	}

	private void runVisChangeEvents(ToggleButton button)
	{
		visChangeEvent.forEach(c -> c.accept(button));
	}

	public void addFaceChangeEvent(Consumer<ToggleButton> c)
	{
		faceChangeEvent.add(c);
	}

	private void runFaceChangeEvents(ToggleButton button)
	{
		faceChangeEvent.forEach(c -> c.accept(button));
	}
}
