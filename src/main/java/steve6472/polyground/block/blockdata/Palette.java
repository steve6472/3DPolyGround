package steve6472.polyground.block.blockdata;

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
	private static final File PALETTE_PATH = new File("game/textures/palette.png");

	private static int[] PALETTE;

	public static void reloadPalette()
	{
		PALETTE = new int[256];
		BufferedImage image;

		try
		{
			image = ImageIO.read(PALETTE_PATH);
		} catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < 256; i++)
		{
			PALETTE[i] = image.getRGB(i % 16, i / 16);
		}
	}

	public static int[] getPalette()
	{
		return PALETTE;
	}
}
