package steve6472.polyground.generator.cond;

import org.lwjgl.glfw.GLFW;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Gui;
import steve6472.sge.gui.components.Background;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.TextField;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.util.ColorUtil;

import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.04.2020
 * Project: CaveGame
 *
 ***********************/
public class AndChainGui extends Gui
{
	private Button[] buttons;
	private EnumState[] states;
	private TextField block, texture;

	public AndChainGui(MainApp mainApp)
	{
		super(mainApp);
		setVisible(true);
	}

	@Override
	public void createGui()
	{
		Background.createComponent(this);
		buttons = new Button[9];
		states = new EnumState[9];
		for (int i = 0; i < 9; i++)
		{
			states[i] = EnumState.IGNORE;
		}

		block = new TextField();
		block.setText("smooth_stone");
		block.endCarret();
		block.setLocation(180, 20);
		block.setSize(180, 30);
		addComponent(block);

		texture = new TextField();
		texture.setText("");
		texture.endCarret();
		texture.setLocation(180, 60);
		texture.setSize(180, 30);
		addComponent(texture);

		for (int i = 0; i < 9; i++)
		{
			int x = i % 3;
			int y = i / 3;

			if (x == 1 && y == 1)
				continue;

			final int index = i;

			buttons[i] = new Button();
			buttons[i].setLocation(20 + x * 50, 20 + y * 50);
			buttons[i].setSize(50, 50);
			buttons[i].addClickEvent(c -> states[index] = EnumState.cycle(states[index]));
			addComponent(buttons[i]);
		}

		Button copy = new Button("Copy");
		copy.setLocation(180, 100);
		copy.setSize(180, 30);
		copy.addClickEvent(c -> {

			String sb = getCondition(" && ");

			if (sb.length() >= 4)
				sb = sb.substring(0, sb.length() - 4);

			GLFW.glfwSetClipboardString(getMainApp().getWindowId(), sb);
		});
		addComponent(copy);

		Button rot = new Button("Rotate");
		rot.setLocation(180, 140);
		rot.setSize(180, 30);
		rot.addClickEvent(c -> {
			for (int i = 0; i < (getMainApp().isKeyPressed(KeyList.L_SHIFT) ? 1 : 2); i++)
			{
				EnumState temp = states[0];
				states[0] = states[1];
				states[1] = states[2];
				states[2] = states[5];
				states[5] = states[8];
				states[8] = states[7];
				states[7] = states[6];
				states[6] = states[3];
				states[3] = temp;
			}
		});
		addComponent(rot);

		Button jsonGen = new Button("Json Gen");
		jsonGen.setLocation(180, 180);
		jsonGen.setSize(180, 30);
		jsonGen.addClickEvent(c -> {
			StringBuilder sb = new StringBuilder();

			Supplier<String> s = () -> {
				String s1 = getCondition(" && ");

				if (s1.length() >= 4)
					s1 = s1.substring(0, s1.length() - 4);
				return s1;
			};

			sb.append("{\n\t\"andChain\": \"").append(s.get()).append("\",\n\t\"texture\": \"").append(block.getText()).append("/").append(texture.getText())
				.append("\"\n},\n");
			rot.forceDoClick();
			sb.append("{\n\t\"andChain\": \"").append(s.get()).append("\",\n\t\"texture\": \"").append(block.getText()).append("/").append(texture.getText())
				.append("\",\n\t\"rotation\": \"R_90\"\n},\n");
			rot.forceDoClick();
			sb.append("{\n\t\"andChain\": \"").append(s.get()).append("\",\n\t\"texture\": \"").append(block.getText()).append("/").append(texture.getText())
				.append("\",\n\t\"rotation\": \"R_180\"\n},\n");
			rot.forceDoClick();
			sb.append("{\n\t\"andChain\": \"").append(s.get()).append("\",\n\t\"texture\": \"").append(block.getText()).append("/").append(texture.getText())
				.append("\",\n\t\"rotation\": \"R_270\"\n},");

			GLFW.glfwSetClipboardString(getMainApp().getWindowId(), sb);
			rot.forceDoClick();
		});
		addComponent(jsonGen);
	}

	@Override
	public void guiTick()
	{

	}

	@Override
	public void render()
	{
		for (int i = 0; i < 9; i++)
		{
			Button b = buttons[i];
			if (b == null)
				continue;

			EnumState state = states[i];
			SpriteRender.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), ColorUtil.getVector4Color(state.color));
			Font.render(b.getX() + 4, b.getY() + 4, "" + i);
		}

		Font.render(10, 190, getCondition("\n"));
	}

	private String getCondition(String lastAppend)
	{
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 9; i++)
		{
			EnumState state = states[i];
			if (state.sign == null)
				continue;

			int x = i % 3;
			int y = i / 3;

			if (x == 1 && y == 1)
				continue;

			x--;
			y--;

			y = -y;

			sb.append("[").append(y).append(", 0, ").append(x).append("] ").append(state.sign).append(" ").append(block.getText()).append(lastAppend);
		}

		return sb.toString();
	}
}
