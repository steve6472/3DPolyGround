package steve6472.polyground.generator.creator;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import steve6472.polyground.block.model.elements.TriangleElement;
import steve6472.polyground.generator.creator.components.*;
import steve6472.polyground.generator.creator.dialogs.NewElementDialog;
import steve6472.polyground.gfx.atlas.Atlas;
import steve6472.polyground.gfx.shaders.ItemTextureShader;
import steve6472.polyground.tessellators.ItemTextureTessellator;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gui.Gui;
import steve6472.sge.gui.components.Background;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.ItemList;
import steve6472.sge.gui.components.dialog.FileSelectorDialog;
import steve6472.sge.gui.components.dialog.MessageDialog;
import steve6472.sge.gui.components.schemes.SchemeButton;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.Util;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.WindowSizeEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockCreatorGui extends Gui
{
	/* UI Components */
	private Button save, newElement, importTexture;
	public ItemList blockList;
	private FaceList faceList;
	private ComponentHolder holder;
	public ElementList elements;
	private TriangleEditWindow triangleEditWindow;
	private PositionSlider elementPosition;

	private HashMap<String, Integer> textureNames;
	private HashMap<Integer, String> textureNamesReference;
	private BlockPreview preview;

	private Atlas atlas;

	private ItemTextureShader textureShader;
	private ItemTextureTessellator tessellator;

	public BlockCreatorGui(MainApp mainApp)
	{
		super(mainApp);
	}

	@Override
	public void createGui()
	{
		textureNames = new HashMap<>();
		textureNamesReference = new HashMap<>();
		preview = new BlockPreview(this);
//		loadAllTextures();
		getMainApp().getEventHandler().register(preview);

		SchemeButton buttonSheme = MainApp.getSchemeRegistry().copyDefaultScheme(SchemeButton.class);
		buttonSheme.disabledFill = new Vector4f(0.25f, 0.25f, 0.25f, 1);
		buttonSheme.enabledFill = new Vector4f(0.25f, 0.25f, 0.25f, 1);
		buttonSheme.hoveredFill = new Vector4f(0.25f, 0.25f, 0.25f, 1);
		buttonSheme.disabledOutsideBorder = new Vector4f(buttonSheme.disabledInsideBorder);
		buttonSheme.disabledInsideBorder = new Vector4f(buttonSheme.disabledInsideBorder.mul(1.2f));
		buttonSheme.enabledOutsideBorder = new Vector4f(buttonSheme.enabledInsideBorder);
		buttonSheme.enabledInsideBorder = new Vector4f(buttonSheme.enabledInsideBorder.mul(1.2f));
		buttonSheme.hoveredOutsideBorder = new Vector4f(buttonSheme.hoveredInsideBorder);
		buttonSheme.hoveredInsideBorder = new Vector4f(buttonSheme.hoveredInsideBorder.mul(1.2f));

		textureShader = new ItemTextureShader();
		textureShader.bind();
		textureShader.setUniform(ItemTextureShader.ATLAS, 0);

		tessellator = new ItemTextureTessellator(6);

		Background.createComponent(this);


		holder = new ComponentHolder();
		addComponent(holder);

		/* Buttons */

		save = new Button("Save All");
		save.setLocation(10, 10);
		save.setSize(80, 25);
		addComponent(save);

		newElement = new Button("New Element");
		newElement.setLocation(100, 10);
		newElement.setSize(80, 25);
		newElement.addClickEvent(this::newBlock);
		addComponent(newElement);

		importTexture = new Button("Import Texture");
		importTexture.setLocation(190, 10);
		importTexture.setSize(140, 25);
		importTexture.addClickEvent(this::importTexture);
		addComponent(importTexture);

		triangleEditWindow = new TriangleEditWindow(this);
		triangleEditWindow.setRelativeLocation(-34, 45);
		triangleEditWindow.setSize(234, 114);
		holder.addComponent(triangleEditWindow);

		/* Lists */

		elementPosition = new PositionSlider();
		elementPosition.setLocation(-24, 7);
		elementPosition.setSize(220, 30);
		holder.addComponent(elementPosition);


		elements = new ElementList();
		elements.setLocation(10, 45);
		elements.setSize(200, 250);
		elements.addSelectElementEvent(c -> {
			if (c instanceof TriangleElement t)
			{
				triangleEditWindow.update(t);
			}
			elementPosition.setElement(c);
			elementPosition.setValues(c.getCreatorData().position.x, c.getCreatorData().position.y, c.getCreatorData().position.z);
		});
		addComponent(elements);

		faceList = new FaceList(this);
		faceList.setRelativeLocation(0, 25 * 8 + 140);
		faceList.setSize(200, 25 * 6);
		holder.addComponent(faceList);

		loadAllTextures(16);
	}

	@Override
	public void guiTick()
	{
		preview.tick();

		triangleEditWindow.setVisible(elements.getSelected() != null && elements.getSelected() instanceof TriangleElement);
		elementPosition.setVisible(elementPosition.element != null && elementPosition.element.getCreatorData().canMove);
	}

	@Override
	public void render()
	{
		renderPreview();

		//		SpriteRender.renderSpriteFromAtlas(0, 0, 32, 32, 0, Creator.UI.getId(), 0, 0, 4, 4);
	}

	@Event
	public void updatePositions(WindowSizeEvent e)
	{
		holder.setLocation(e.getWidth() - 210, 0);
		preview.updateSize(e);
	}

	private void importTexture(Button button)
	{
		FileSelectorDialog file;
		getMainApp().showDialog(file = new FileSelectorDialog(new File("").getPath()));
		file.setIdealAccept(c ->
		{
			try
			{
				Files.copy(c.toPath(), new File("creator\\blocks\\textures\\" + c.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
				loadAllTextures(16);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		});

		file.center();
	}

	private void newBlock(Button c)
	{
		getMainApp().showDialog(new NewElementDialog(this)).center();
	}

	private void errorMessage(String message)
	{
		getMainApp().showDialog(new MessageDialog(message, "Error", 250)).center();
	}

	private void renderPreview()
	{
		preview.renderBlock(elements.getElements());

		SpriteRender.renderSpriteInverted(0, 0, getMainApp().getWidth(), getMainApp().getHeight(), 0, preview.getId());
	}

	private void loadAllTextures(int size)
	{
		File textures = new File("game/textures/block");

		if (atlas != null)
		{
			if (atlas.getSprite() != null)
			{
				System.out.println("Deleting old atlas texture");
				atlas.getSprite().delete();
			}
		}

		if (Objects.requireNonNull(textures.listFiles()).length == 0)
		{
			return;
		}

		textureNames.clear();
		textureNamesReference.clear();
		atlas = new Atlas(size);

		for (int i = 0; i < Objects.requireNonNull(textures.listFiles()).length; i++)
		{
			File f = Objects.requireNonNull(textures.listFiles())[i];
			if (f.isDirectory())
				continue;

			try
			{
				atlas.add(f.getName(), f);
				textureNames.put(Util.substringEnd(f.getName(), 4), i);
				textureNamesReference.put(i, Util.substringEnd(f.getName(), 4));
			} catch (RuntimeException ex)
			{
				int newSize = size << 1;
				System.out.println("Image did not fit, incresing size to " + newSize);
				loadAllTextures(newSize);
				return;
			} catch (Exception ex)
			{
				System.err.println(f.getName());
				System.err.println(atlas);
				ex.printStackTrace();
			}
		}

		atlas.finish();
		preview.update(atlas);
	}

	/* Shader update */
	@Event
	public void resize(WindowSizeEvent e)
	{
		Matrix4f ortho = new Matrix4f().ortho(0, e.getWidth(), e.getHeight(), 0, 1, -1);

		textureShader.getShader().bind();
		textureShader.setProjection(ortho);
	}
}
