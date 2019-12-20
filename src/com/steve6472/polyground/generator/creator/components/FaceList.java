package com.steve6472.polyground.generator.creator.components;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.generator.creator.BlockCreatorGui;
import com.steve6472.polyground.generator.creator.Creator;
import com.steve6472.sge.gfx.SpriteRender;
import com.steve6472.sge.gfx.font.Font;
import com.steve6472.sge.gui.Component;
import com.steve6472.sge.gui.components.RadioGroup;
import com.steve6472.sge.gui.components.context.ContextMenu;
import com.steve6472.sge.gui.components.context.ContextMenuToggleButton;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.10.2019
 * Project: SJP
 *
 ***********************/
public class FaceList extends Component
{
	private BlockCreatorGui creatorGui;

	private boolean hoveringFaceSelect;
	private EnumFace selectedFace;

	public FaceList(BlockCreatorGui creatorGui)
	{
		this.creatorGui = creatorGui;
		selectedFace = EnumFace.NORTH;
	}

	@Override
	public void init(MainApp main)
	{
		setupFaceSelectContextMenu();
	}

	@Override
	public void tick()
	{
		hoveringFaceSelect = isCursorInComponent(x + 12, y + 12, 96, 28);

		onMouseClicked(hoveringFaceSelect, KeyList.LMB, () ->
		{
			creatorGui.setContextMenu("faceSelect");
			creatorGui.getCurrentContextMenu().show(x + 12, y + 40, -1, -1);
		});
	}

	@Override
	public void render()
	{
		renderComponents();

		SpriteRender.renderSingleBorderComponent(this, 0, 0, 0, 1, 1, 1, 1, 1);

		/* Render Face Select Button */
		if (hoveringFaceSelect)
			SpriteRender.renderSpriteFromAtlas(x + 12, y + 12, 128, 32, Creator.BUTTONS.getId(), 0, 4, 2, 8);
		else
			SpriteRender.renderSpriteFromAtlas(x + 12, y + 12, 128, 32, Creator.BUTTONS.getId(), 0, 3, 2, 8);

		Font.render(selectedFace.getFancyName(), x + 20, y + 22);
	}

	private void setupFaceSelectContextMenu()
	{
		ContextMenu menu = new ContextMenu("faceSelect")
		{
			boolean canOpen = true;

			@Override
			public void hide()
			{
				super.hide();

				if (hoveringFaceSelect)
					canOpen = false;
			}

			@Override
			public void show(int x, int y, int mouseX, int mouseY)
			{
				if (canOpen)
				{
					super.show(x, y, mouseX, mouseY);
				} else
				{
					canOpen = true;
				}
			}
		};
		creatorGui.addContextMenu(menu);
		menu.darkenGui = false;
		menu.setSize(96);

		ContextMenuToggleButton north = createFaceButton(EnumFace.NORTH, menu);
		ContextMenuToggleButton east = createFaceButton(EnumFace.EAST, menu);
		ContextMenuToggleButton south = createFaceButton(EnumFace.SOUTH, menu);
		ContextMenuToggleButton west = createFaceButton(EnumFace.WEST, menu);
		ContextMenuToggleButton top = createFaceButton(EnumFace.UP, menu);
		ContextMenuToggleButton bottom = createFaceButton(EnumFace.DOWN, menu);

		north.setToggled(true);

		RadioGroup.addContextMenuToggleButtons(north, east, south, west, top, bottom);

	}

	private ContextMenuToggleButton createFaceButton(EnumFace face, ContextMenu menu)
	{
		ContextMenuToggleButton b = new ContextMenuToggleButton();
		b.addClickEvent(c -> selectedFace = face);
		b.setName(face.getFancyName());

		menu.add(b);
		return b;
	}
}
