package steve6472.polyground.generator.cond;

import org.json.JSONObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.dialog.FileSelectorDialog;
import steve6472.sge.gui.components.dialog.OkTextInputDialog;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.MainFlags;
import steve6472.sge.main.Window;
import steve6472.sge.main.events.WindowSizeEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.04.2020
 * Project: CaveGame
 *
 ***********************/
public class AndChainGenerator extends MainApp
{
	public static File PATH_TO_JSON;

	private ConditionGenerator generator;

	@Override
	public void init()
	{
		BufferedImage b = loadImage(MainApp.class.getResourceAsStream("/textures/block/debug.png"));
		if (b != null)
		{
			GLFWImage.Buffer gb = GLFWImage.create(1);
			GLFWImage iconGI = GLFWImage.create().set(b.getWidth(), b.getHeight(), toBuffer(b));
			gb.put(0, iconGI);
			glfwSetWindowIcon(getWindow().getWindow(), gb);
		}

		getEventHandler().runEvent(new WindowSizeEvent(getWidth(), getHeight()));

//		getPath();

		PATH_TO_JSON = new File("D:/CaveGame/generator/smooth_stone.json");

//		new AndChainGui(this);
		generator = new ConditionGenerator(this);
	}

	private void getPath()
	{
		NewOrExistingDialog newOrExisting = new NewOrExistingDialog();
		showDialog(newOrExisting).center();

		FileSelectorDialog fileSelect = new FileSelectorDialog();

		newOrExisting.addYesClickEvent(c ->
		{
			fileSelect.setTitle("Select Directory to save");
			showDialog(fileSelect).center();
			((Button) fileSelect.getComponent(0)).setEnabled(false);
			fileSelect.setBiAccept((d, f) ->
			{
				OkTextInputDialog nameSelect = new OkTextInputDialog("name", "File Name");
				showDialog(nameSelect).center();
				nameSelect.addOkClickEvent(o -> createNewJson(d, nameSelect.getText()));
			});
		});
		newOrExisting.addNoClickEvent(c ->
		{
			fileSelect.setTitle("Select File to load from");
			showDialog(fileSelect).center();
			((Button) fileSelect.getComponent(0)).setEnabled(false);
			fileSelect.setBiAccept((d, f) -> loadJson(f));
		});
	}

	private void createNewJson(File directory, String name)
	{
		JSONObject newJson = new JSONObject();
		PATH_TO_JSON = new File(directory, name + ".json");
		try (FileWriter file = new FileWriter(PATH_TO_JSON)) {

			newJson.write(file, 4, 4);
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		loadJson(PATH_TO_JSON);
	}

	private void loadJson(File f)
	{
		PATH_TO_JSON = f;
		try
		{
			generator.loadJson();
		} catch (IOException e)
		{
			exit();
			e.printStackTrace();
		}
	}

	private BufferedImage loadImage(InputStream file)
	{
		try {
			return ImageIO.read(file);
		} catch (IOException var2) {
			var2.printStackTrace();
			return null;
		}
	}

	private ByteBuffer toBuffer(BufferedImage image)
	{
		int[] rawPixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		ByteBuffer pixels = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

		for(int y = 0; y < image.getHeight(); ++y) {
			for(int x = 0; x < image.getWidth(); ++x) {
				int pixel = rawPixels[y * image.getWidth() + x];
				pixels.put((byte)(pixel >> 16 & 255));
				pixels.put((byte)(pixel >> 8 & 255));
				pixels.put((byte)(pixel & 255));
				pixels.put((byte)(pixel >> 24 & 255));
			}
		}

		pixels.flip();
		return pixels;
	}

	@Override
	public void tick()
	{
		tickGui();
		tickDialogs();
	}

	@Override
	public void render()
	{
		renderGui();
		renderDialogs();
	}

	@Override
	public void setWindowHints()
	{
		Window.setResizable(false);
		Window.setFloating(true);
	}

	@Override
	public int getWindowWidth()
	{
		return 16 * 50;
	}

	@Override
	public int getWindowHeight()
	{
		return 9 * 50;
	}

	@Override
	public void exit()
	{
		getWindow().close();
	}

	@Override
	public String getTitle()
	{
		return "And Chain Generator";
	}

	@Override
	protected int[] getFlags()
	{
		return new int[] {MainFlags.ADD_BASIC_ORTHO};
	}

	public static void main(String[] args)
	{
		new AndChainGenerator();
	}
}
