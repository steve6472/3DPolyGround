package com.steve6472.polyground.generator.creator.dialogs;

import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.sge.gfx.Atlas;
import com.steve6472.sge.gfx.SpriteRender;
import com.steve6472.sge.gfx.font.CustomChar;
import com.steve6472.sge.gui.components.Button;
import com.steve6472.sge.gui.components.NamedCheckBox;
import com.steve6472.sge.gui.components.TextField;
import com.steve6472.sge.gui.components.dialog.OkDialog;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.10.2019
 * Project: SJP
 *
 ***********************/
public class TextureDialog extends OkDialog
{
	private Atlas atlas;
	private int res, selX, selY;
	private CubeFace face;
	private NamedCheckBox isReference;
	private TextField reference;

	public TextureDialog(Atlas atlas, CubeFace face)
	{
		super(" ", "Select Texture");
		this.atlas = atlas;
		this.face = face;
		height += atlas.getTotalSize() * 2 - 8;
		width = Math.max(atlas.getTotalSize() * 2 + 32, 128) + 130;

		selX = face.getProperty(FaceRegistry.texture).getTextureId() % atlas.getTileCount();
		selY = face.getProperty(FaceRegistry.texture).getTextureId() / atlas.getTileCount();
		res = selX + selY * atlas.getTileCount();
	}

	@Override
	public void init(MainApp main)
	{
		super.init(main);

		Button clear = new Button("Clear Texture");
		clear.setRelativeLocation(getWidth() - 130, 34);
		clear.setSize(120, 25);
		clear.addClickEvent(c -> res = -1);
		addComponent(clear);

		reference = new TextField();
		reference.setSize(120, 25);
		reference.setRelativeLocation(getWidth() - 130, 104);
		reference.setEditable(face.getProperty(FaceRegistry.texture).isReference());
		addComponent(reference);

		isReference = new NamedCheckBox();
		isReference.setText("Is Reference");
		isReference.setRelativeLocation(getWidth() - 130, 69 /* Nice */);
		isReference.setToggled(face.getProperty(FaceRegistry.texture).isReference());
		isReference.setSize(120, 25);
		isReference.setBoxSize(14, 14);
		isReference.setSelectedChar(CustomChar.CROSS);
		isReference.setBoxPadding(5, 5);
		isReference.addChangeEvent(c -> reference.setEditable(c.isToggled()));
		isReference.addChangeEvent(c -> face.getProperty(FaceRegistry.texture).setTextureId(c.isToggled() ? res : -1));
		addComponent(isReference);
	}

	@Override
	public void tick()
	{
		super.tick();

		if (getMouseHandler().getButton() == KeyList.LMB)
		{
			int s = atlas.getTotalSize() * 2;

			int sx = getX() + (getWidth() - 130) / 2 - s / 2;
			int sy = getY() + 34;

			int mx = getMain().getMouseX() - sx;
			int my = getMain().getMouseY() - sy;

			if (mx >= 0 && my >= 0 && mx < s && my < s)
			{
				selX = mx / 32;
				selY = my / 32;

				res = selX + selY * atlas.getTileCount();
			}
		}
	}

	@Override
	public void render()
	{
		super.render();

		int s = atlas.getTotalSize() * 2;

		int sx = getX() + (getWidth() - 130) / 2 - s / 2;
		int sy = getY() + 34;

		SpriteRender.renderSprite(sx, sy, s, s, 0, atlas.getSprite());

		int mx = getMain().getMouseX() - sx;
		int my = getMain().getMouseY() - sy;

//		SpriteRender.drawSoftCircle(mx, my, 8, 0.8f, 0, 1, 1, 1);

		if (mx >= 0 && my >= 0 && mx < s && my < s)
		{
			SpriteRender.fillRect(sx + mx / 32 * 32, sy + my / 32 * 32, 32, 32,
				0.8f, 0.8f, 0.8f, 0.5f);
		}

		if (res != -1)
		{
			SpriteRender.fillRect(sx + selX * 32, sy + selY * 32, 32, 32, 0.9f,
				0.9f, 0.9f, 0.6f);
		}
	}

	public int getTexture()
	{
		return res;
	}

	public boolean isReference()
	{
		return isReference.isToggled();
	}

	public String getReferenceName()
	{
		return "#" + reference.getText();
	}
}
