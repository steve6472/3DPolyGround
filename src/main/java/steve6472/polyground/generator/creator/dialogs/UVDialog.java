package steve6472.polyground.generator.creator.dialogs;

import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import steve6472.polyground.block.model.registry.face.FaceRegistry;
import steve6472.polyground.gfx.shaders.ItemTextureShader;
import steve6472.polyground.tessellators.ItemTextureTessellator;
import steve6472.sge.gfx.Atlas;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.gfx.font.CustomChar;
import steve6472.sge.gfx.shaders.Shader;
import steve6472.sge.gui.components.NamedCheckBox;
import steve6472.sge.gui.components.dialog.OkDialog;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.Util;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.10.2019
 * Project: SJP
 *
 ***********************/
public class UVDialog extends OkDialog
{
	private NamedCheckBox autoUv;

	private Atlas atlas;
	private ItemTextureShader shader;
	private ItemTextureTessellator tessellator;
	private CubeFace face;

	private int minU, minV, maxU, maxV;

	private EnumSelected selected = EnumSelected.NONE;

	public UVDialog(Atlas atlas, ItemTextureShader shader, ItemTextureTessellator tessellator, CubeFace face)
	{
		super(" ", "Set UV");
		this.atlas = atlas;
		this.shader = shader;
		this.tessellator = tessellator;
		this.face = face;

		width = 200;
		height = 249;

		minU = (int) (face.getProperty(FaceRegistry.uv).getMinU() * 16f);
		minV = (int) (face.getProperty(FaceRegistry.uv).getMinV() * 16f);
		maxU = (int) (face.getProperty(FaceRegistry.uv).getMaxU() * 16f);
		maxV = (int) (face.getProperty(FaceRegistry.uv).getMaxV() * 16f);
	}

	@Override
	public void init(MainApp main)
	{
		super.init(main);

		autoUv = new NamedCheckBox();
		autoUv.setText("Auto UV");
		autoUv.setToggled(AutoUVFaceProperty.check(face));
		autoUv.setRelativeLocation(18, 172);
		autoUv.setSize(getWidth() - 36, 25);
		autoUv.setBoxSize(14, 14);
		autoUv.setSelectedChar(CustomChar.CROSS);
		autoUv.setBoxPadding(5, 5);
		autoUv.addChangeEvent(c ->
		{
			face.getProperty(FaceRegistry.autoUv).setAuto(autoUv.isToggled());
			if (autoUv.isToggled())
			{
				face.getProperty(FaceRegistry.uv).autoUV(face.getParent(), face.getFace());
			} else
			{
				face.getProperty(FaceRegistry.uv).setUV(minU / 16f, minV / 16f, maxU / 16f, maxV / 16f);
			}
		});
		addComponent(autoUv);
	}

	@Override
	public void tick()
	{
		ok.setEnabled(selected == EnumSelected.NONE);
		autoUv.setEnabled(selected == EnumSelected.NONE);

		super.tick();

		if (getMouseHandler().getButton() == KeyList.LMB && !AutoUVFaceProperty.check(face))
		{
			int sx = getX() + getWidth() / 2 - 64;
			int sy = getY() + 34;

			int mx = getMain().getMouseX() - sx + 4;
			int my = getMain().getMouseY() - sy + 4;

			if (selected == EnumSelected.NONE)
			{
				if (Util.getDistance(minU * 8, minV * 8, mx, my) <= 7)
					selected = EnumSelected.MIN;
				else if (Util.getDistance(maxU * 8, maxV * 8, mx, my) <= 7)
					selected = EnumSelected.MAX;
			} else
			{
				if (selected == EnumSelected.MIN)
				{
					minU = mx / 8;
					minV = my / 8;

					minU = Util.clamp(0, maxU - 1, minU);
					minV = Util.clamp(0, maxV - 1, minV);

				} else if (selected == EnumSelected.MAX)
				{
					maxU = mx / 8;
					maxV = my / 8;

					maxU = Util.clamp(minU + 1, 16, maxU);
					maxV = Util.clamp(minV + 1, 16, maxV);
				}

				if (selected == EnumSelected.MIN || selected == EnumSelected.MAX)
				{
					face.getProperty(FaceRegistry.uv).setUV(minU / 16f, minV / 16f, maxU / 16f, maxV / 16f);
				}
			}
		} else
		{
			selected = EnumSelected.NONE;
		}

		/*
		minU = Util.clamp(0, maxU - 1, minU);
		minV = Util.clamp(0, maxV - 1, minV);

		maxU = Util.clamp(minU + 1, 16, maxU);
		maxV = Util.clamp(minV + 1, 16, maxV);*/
	}

	@Override
	public void render()
	{
		super.render();

		int sx = getX() + getWidth() / 2 - 64;
		int sy = getY() + 34;

		atlas.getSprite().bind(0);

		shader.bind();
		shader.setUniform(ItemTextureShader.ATLAS, 0);

		renderItem(sx, sy, face.getProperty(FaceRegistry.texture).getTextureId());

		if (!autoUv.isToggled())
		{

			SpriteRender.manualStart();
			SpriteRender.drawSoftCircle(minU * 8 + sx, minV * 8 + sy, 5f, 0.3f, 0.2f, 0.2f, 0.2f, 0.8f);
			SpriteRender.drawSoftCircle(maxU * 8 + sx, maxV * 8 + sy, 5f, 0.3f, 0.2f, 0.2f, 0.2f, 0.8f);

			SpriteRender.fillRect(minU * 8 + sx, minV * 8 + sy, (maxU - minU) * 8, 1, 1, 1, 1, 1);
			SpriteRender.fillRect(minU * 8 + sx, minV * 8 + sy, 1, (maxV - minV) * 8, 1, 1, 1, 1);
			SpriteRender.fillRect(minU * 8 + sx + (maxU - minU) * 8, minV * 8 + sy, 1, (maxV - minV) * 8, 1, 1, 1, 1);
			SpriteRender.fillRect(minU * 8 + sx, minV * 8 + sy + (maxV - minV) * 8, (maxU - minU) * 8 + 1, 1, 1, 1, 1, 1);
			SpriteRender.manualEnd();
		}

		Shader.releaseShader();
	}

	private void renderItem(int x, int y, int index)
	{
		float u = index % atlas.getTileCount();
		float v = index / atlas.getTileCount();

		float t = 1f / (float) atlas.getTileCount();

		float minU = u * t;
		float minV = v * t;
		float maxU = u * t + t;
		float maxV = v * t + t;

		tessellator.begin(6);

		tessellator.alpha(1f);

		tessellator.pos(x, y).texture(minU, minV).endVertex();
		tessellator.pos(x, y + 128).texture(minU, maxV).endVertex();
		tessellator.pos(x + 128, y + 128).texture(maxU, maxV).endVertex();

		tessellator.pos(x + 128, y + 128).texture(maxU, maxV).endVertex();
		tessellator.pos(x + 128, y).texture(maxU, minV).endVertex();
		tessellator.pos(x, y).texture(minU, minV).endVertex();

		tessellator.loadPos(0);
		tessellator.loadTexture(1);
		tessellator.loadAlpha(2);
		tessellator.draw(Tessellator.TRIANGLES);
		tessellator.disable(0, 1, 2);
	}

	private enum EnumSelected
	{
		NONE, MIN, MAX
	}

	@Override
	protected boolean canMove()
	{
		return super.canMove() && selected == EnumSelected.NONE;
	}
}
