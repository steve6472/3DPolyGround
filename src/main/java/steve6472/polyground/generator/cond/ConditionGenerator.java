package steve6472.polyground.generator.cond;

import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Component;
import steve6472.sge.gui.Gui;
import steve6472.sge.gui.components.Background;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.ItemList;
import steve6472.sge.gui.components.TextField;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.util.RandomUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.04.2020
 * Project: CaveGame
 *
 ***********************/
public class ConditionGenerator extends Gui
{
	EnumState[][][] states;
	private TextField block, texture;
	private JSONArray[] faces;
	private ItemList list;

	private View view;

	public ConditionGenerator(MainApp mainApp)
	{
		super(mainApp);
		setVisible(true);
	}

	@Override
	public void createGui()
	{
		Background.createComponent(this);
		faces = new JSONArray[6];
		states = new EnumState[3][3][3];

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
//					states[i][j][k] = EnumState.IGNORE;
					states[i][j][k] = EnumState.values()[RandomUtil.randomInt(0, 2)];
				}
			}
		}

		view = new View(this);

		block = new TextField();
		block.setText("block");
		block.endCarret();
		block.setLocation(0, 10);
		block.setSize(180, 30);
		toRight(block);
		addComponent(block);

		texture = new TextField();
		texture.setText("texture");
		texture.endCarret();
		texture.setSize(180, 30);
		toRight(texture);
		under(texture, block);
		addComponent(texture);

		list = new ItemList(10);
		list.setSize(180, 150);
		toRight(list);
		under(list, texture);
		addComponent(list);

		Button face = new Button("Select Face");
		face.setSize(180, 30);
		toRight(face);
		under(face, list);
		addComponent(face);

		Button save = new Button("Save");
		save.setSize(180, 30);
		toRight(save);
		under(save, face);
		addComponent(save);
	}

	private int getPadding()
	{
		return 10;
	}

	private void toLeftOf(Component c, Component source)
	{
		c.setLocation(source.getX() - c.getWidth() - getPadding(), c.getY());
	}

	private void toRight(Component c)
	{
		c.setLocation(getMainApp().getWidth() - c.getWidth() - getPadding(), c.getY());
	}

	private void under(Component c, Component source)
	{
		c.setLocation(source.getX(), source.getY() + source.getHeight() + getPadding());
	}

	public void loadJson() throws IOException
	{
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(AndChainGenerator.PATH_TO_JSON)))
		{
			String line;
			while ((line = reader.readLine()) != null)
				sb.append(line);
		}

		JSONObject json = new JSONObject(sb.toString());

		/* new file */
		if (!json.has("faces"))
		{
			JSONObject faces = new JSONObject();
			faces.put("up", new JSONArray());
			faces.put("down", new JSONArray());
			faces.put("north", new JSONArray());
			faces.put("east", new JSONArray());
			faces.put("south", new JSONArray());
			faces.put("west", new JSONArray());
			json.put("faces", faces);
			saveJson(json);
		} else
		{
			JSONObject faces = json.getJSONObject("faces");
			this.faces[0] = faces.getJSONArray("up");
			this.faces[1] = faces.getJSONArray("down");
			this.faces[2] = faces.getJSONArray("north");
			this.faces[3] = faces.getJSONArray("east");
			this.faces[4] = faces.getJSONArray("south");
			this.faces[5] = faces.getJSONArray("west");
		}
	}

	public void saveJson(JSONObject json)
	{
		try (FileWriter file = new FileWriter(AndChainGenerator.PATH_TO_JSON)) {

//			System.out.println("Saving JSON: \"" + json.toString(4));
			json.write(file, 4, 4);
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void guiTick()
	{
		if (AndChainGenerator.PATH_TO_JSON != null)
			view.tick();
	}

	@Override
	public void render()
	{/*
		for (int i = 0; i < 9; i++)
		{
			Button b = buttons[i];
			if (b == null)
				continue;

			EnumState state = states[i];
			SpriteRender.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), ColorUtil.getVector4Color(state.color));
			Font.render(b.getX() + 4, b.getY() + 4, "" + i);
		}*/


		if (AndChainGenerator.PATH_TO_JSON != null)
			view.render(states);

		Font.render(10, 190, getCondition("\n"));
	}

	private String getCondition(String lastAppend)
	{
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 9; i++)
		{
//			EnumState state = states[i];
//			if (state.sign == null)
				continue;

//			int x = i % 3;
//			int y = i / 3;
//
//			if (x == 1 && y == 1)
//				continue;
//
//			x--;
//			y--;
//
//			y = -y;
//
//			sb.append("[").append(y).append(", 0, ").append(x).append("] ").append(state.sign).append(" ").append(block.getText()).append(lastAppend);
		}

		return sb.toString();
	}
}
