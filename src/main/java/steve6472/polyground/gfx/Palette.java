package steve6472.polyground.gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.12.2020
 * Project: CaveGame
 *
 ***********************/
public class Palette
{
	private final File palettePath;
	private int[] palette;

	public Palette(String path)
	{
		this.palettePath = new File("game/textures/palettes/" + path + ".png");
	}

	public void reloadPalette()
	{
		palette = new int[256];
		BufferedImage image;

		try
		{
			image = ImageIO.read(palettePath);
		} catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		if (image.getHeight() == 1)
		{
			for (int i = 0; i < 256; i++)
			{
				palette[i] = image.getRGB(i, 0);
			}
		} else
		{
			for (int i = 0; i < 256; i++)
			{
				palette[i] = image.getRGB(i % 16, i / 16);
			}
		}
	}

	public int[] getColors()
	{
		return palette;
	}
}
