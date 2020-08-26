package steve6472.polyground.gfx.atlas;

import steve6472.sge.gfx.StaticTexture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2020
 * Project: CaveGame
 *
 ***********************/
public class Atlas
{
	private final ImagePacker packer;
	private StaticTexture texture;
	private final int size;

	public Atlas(int size)
	{
		this.size = size;
		packer = new ImagePacker(size, size, 0, true);
	}

	public void add(String name, File f)
	{
		try
		{
			packer.insertImage(name, ImageIO.read(f));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void finish()
	{
		texture = StaticTexture.fromBufferedImage(packer.getImage());
	}

	/**
	 * @return the rectangle in the output image of each inserted image
	 */
	public Map<String, Rectangle> getRects()
	{
		return packer.getRects();
	}

	public StaticTexture getSprite()
	{
		return texture;
	}

	public int getTileCount()
	{
		return size;
	}
}
