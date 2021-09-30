package steve6472.polyground.generator;

import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.main.*;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.MouseEvent;
import steve6472.sge.main.util.MathUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.09.2020
 * Project: CaveGame
 *
 ***********************/
public class KnappingRecipeCreator extends MainApp
{
	private boolean[][] grid;

	@Override
	protected int[] getFlags()
	{
		return new int[] {MainFlags.ENABLE_EXIT_KEY};
	}

	@Override
	public void init()
	{
		Window.enableVSync(true);

		grid = new boolean[16][16];
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void render()
	{
		SpriteRender.manualStart();
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				if (grid[i][j])
					SpriteRender.fillRect(i * 32, j * 32, 32, 32, 0.8f, 0.8f, 0.8f, 1f);

				SpriteRender.renderBorder(i * 32, j * 32, 32, 32, 1f, 1f, 1f, 1f);
			}
		}

		SpriteRender.fillRect(520, 300, 100, 30, 0, 1, 0, 1);

		SpriteRender.manualEnd();

		Font.render(530, 310, "save");
	}

	@Event
	public void click(MouseEvent e)
	{
		if (e.getAction() == KeyList.PRESS && e.getButton() == KeyList.LMB)
		{
			int x = e.getX() >> 5;
			int y = e.getY() >> 5;
			if (x < 16 && y < 16 && x >= 0 && y >= 0)
				grid[x][y] = !grid[x][y];

			if (MathUtil.isInRectangle(520, 300, 620, 330, e.getX(), e.getY()))
			{
				StringBuilder sb = new StringBuilder();
				sb.append("{\"recipe\": {\n");
				for (int i = 0; i < 16; i++)
				{
					sb.append("\t\"line_").append(Integer.toHexString(i)).append("\": [");
					for (int j = 0; j < 16; j++)
					{
						sb.append(grid[j][i] ? 1 : 0);
						if (j < 15)
							sb.append(",");
					}
					if (i < 15)
						sb.append("],\n");
				}
				sb.append("]\n}}");

				System.out.println(sb);
			}
		}
	}

	@Override
	public void setWindowHints()
	{

	}

	@Override
	public int getWindowWidth()
	{
		return 512 + 128;
	}

	@Override
	public int getWindowHeight()
	{
		return 512;
	}

	@Override
	public void exit()
	{
		getWindow().close();
	}

	@Override
	public String getTitle()
	{
		return "Knapping Recipe Generator";
	}

	public static void main(String[] args)
	{
		new KnappingRecipeCreator();
	}
}
