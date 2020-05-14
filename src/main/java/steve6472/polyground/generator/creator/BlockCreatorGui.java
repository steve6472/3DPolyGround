package steve6472.polyground.generator.creator;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.polyground.generator.creator.components.ColorDialog;
import steve6472.polyground.generator.creator.components.ComponentHolder;
import steve6472.polyground.generator.creator.components.CubeList;
import steve6472.polyground.generator.creator.components.FaceList;
import steve6472.polyground.generator.creator.dialogs.EditCubeDialog;
import steve6472.polyground.generator.creator.dialogs.NewBlockDialog;
import steve6472.polyground.generator.creator.dialogs.TextureDialog;
import steve6472.polyground.generator.creator.dialogs.UVDialog;
import steve6472.polyground.gfx.shaders.ItemTextureShader;
import steve6472.polyground.tessellators.ItemTextureTessellator;
import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import steve6472.sge.gfx.Atlas;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Gui;
import steve6472.sge.gui.components.Background;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.ItemList;
import steve6472.sge.gui.components.dialog.*;
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
	private Button swichToItems, save, newBlock, importTexture, setTexture, editUv, applyToAllFaces, color;
	public ItemList blockList;
	private FaceList faceList;
	private ComponentHolder holder;
	private CubeList cubeList;

	public HashMap<String, BlockEntry> blocks;
	private HashMap<String, Integer> textureNames;
	private HashMap<Integer, String> textureNamesReference;
	private BlockPreview preview;

	private Atlas atlas;

	private ItemTextureShader textureShader;
	private ItemTextureTessellator tessellator;

	private SchemeButton buttonSheme;

	public BlockCreatorGui(MainApp mainApp)
	{
		super(mainApp);
	}

	@Override
	public void createGui()
	{
		blocks = new HashMap<>();
		textureNames = new HashMap<>();
		textureNamesReference = new HashMap<>();
		preview = new BlockPreview(this);
//		loadAllTextures();
		getMainApp().getEventHandler().register(preview);

		buttonSheme = MainApp.getSchemeRegistry().copyDefaultScheme(SchemeButton.class);
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


		cubeList = new CubeList(this);
		cubeList.setLocation(250, 200);
		cubeList.setSize(130, 200);
		addComponent(cubeList);


		holder = new ComponentHolder();
		addComponent(holder);

		/* Buttons */

		save = new Button("Save All");
		save.setLocation(10, 10);
		save.setSize(80, 25);
		save.addClickEvent(this::save);
		addComponent(save);

		newBlock = new Button("New Block");
		newBlock.setLocation(100, 10);
		newBlock.setSize(80, 25);
		newBlock.addClickEvent(this::newBlock);
		addComponent(newBlock);

		importTexture = new Button("Import Texture");
		importTexture.setLocation(190, 10);
		importTexture.setSize(140, 25);
		importTexture.addClickEvent(this::importTexture);
		addComponent(importTexture);

		swichToItems = new Button("Items");
		swichToItems.setSize(80, 25);
		swichToItems.setLocation(340, 10);
		swichToItems.addClickEvent(c ->
		{

		});
		addComponent(swichToItems);

		setTexture = new Button("Set Texture");
		setTexture.setRelativeLocation(0, 25 * 14 + 150);
		setTexture.setSize(200, 25);
		setTexture.setEnabled(false);
		setTexture.addClickEvent(this::setTexture);
		holder.addComponent(setTexture);

		editUv = new Button("Edit UV");
		editUv.setRelativeLocation(0, 25 * 14 + 185);
		editUv.setSize(95, 25);
		editUv.setEnabled(false);
		editUv.addClickEvent(this::editUV);
		holder.addComponent(editUv);

		applyToAllFaces = new Button("Apply to All");
		applyToAllFaces.setSize(95, 25);
		applyToAllFaces.setRelativeLocation(105, 25 * 14 + 185);
		applyToAllFaces.setEnabled(false);
		applyToAllFaces.addClickEvent(this::applyToAllFaces);
		holder.addComponent(applyToAllFaces);

		color = new Button("Color");
		color.setRelativeLocation(0, 25 * 14 + 185 + 35);
		color.setSize(95, 25);
		color.setEnabled(false);
		color.addClickEvent(this::color);
		holder.addComponent(color);

		/* Lists */

		blockList = new ItemList(10);
		blockList.setMultiselect(false);
		blockList.setLocation(10, 45);
		blockList.setSize(200, 250);
		addComponent(blockList);

		faceList = new FaceList(this);
		faceList.setRelativeLocation(0, 25 * 8 + 140);
		faceList.setSize(200, 25 * 6);
		holder.addComponent(faceList);

		loadBlocks();
		updateBlockTextures();
		cubeList.updateButtons();
	}

	@Override
	public void guiTick()
	{
		preview.tick();
	}

	@Override
	public void render()
	{
		renderPreview();

		String text = "Block Type: " + (getSelectedBlock() != null ? (getSelectedBlock().isParent() ? "Parent" : "Normal") : "Not Found");

		Font.render(text, getMainApp().getWidth() - Font.getTextWidth(text, 1) - 5, 5);

		//		SpriteRender.renderSpriteFromAtlas(0, 0, 32, 32, 0, Creator.UI.getId(), 0, 0, 4, 4);
	}

	@Event
	public void updatePositions(WindowSizeEvent e)
	{
		holder.setLocation(e.getWidth() - 210, 0);
		cubeList.setLocation(e.getWidth() - cubeList.getWidth() - 20, 20);
		preview.updateSize(e);
	}

	/* Button Click Events */

	private void color(Button button)
	{
		if (getSelectedCube() == null)
		{
			errorMessage("Cube is not selected!");
			return;
		}
		if (getSelectedFace() == null)
		{
			errorMessage("Face is not selected!");
			return;
		}

		ColorDialog colorDialog = new ColorDialog(getSelectedFace());
		colorDialog.setOkEvent((b, s) ->
		{
			getSelectedFace().getProperty(FaceRegistry.tint).setTint(s.getRed() / 255f, s.getGreen() / 255f, s.getBlue() / 255f);
		});
		colorDialog.addChangeEvent(c ->
		{
			getSelectedFace().getProperty(FaceRegistry.tint).setTint(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
		});

		getMainApp().showDialog(colorDialog).center();

	}

	private void applyToAllFaces(Button button)
	{
		if (getSelectedCube() == null)
		{
			errorMessage("Cube is not selected!");
			return;
		}
		if (getSelectedFace() == null)
		{
			errorMessage("Face is not selected!");
			return;
		}

		YesNoDialog proceed = new YesNoDialog("Do you wish to proceed?", "Proceed?");
		getMainApp().showDialog(proceed).center();

		proceed.addYesClickEvent(c ->
		{
			for (EnumFace face : EnumFace.getFaces())
			{
				if (face == getSelectedFace().getFace())
					continue;

				getSelectedCube().getFace(face).clearProperties();
				getSelectedCube().getFace(face).addProperties(getSelectedFace().copyProperties());
			}
		});
	}

	private void editUV(Button button)
	{
		UVDialog editUv;
		getMainApp().showDialog(editUv = new UVDialog(atlas, textureShader, tessellator, getSelectedFace()));

		editUv.addOkClickEvent(b -> autoUV());

		editUv.center();
	}

	private void editCube(Button button)
	{
		EditCubeDialog editCube;
		getMainApp().showDialog(editCube = new EditCubeDialog(getSelectedCube(), this));

		editCube.addOkClickEvent(b -> autoUV());

		editCube.center();
	}

	/**
	 * Check if selected cube has any faces with auto UV, if so it sets UV automatically
	 */
	public void autoUV()
	{
		for (CubeFace f : getSelectedCube().getFaces())
		{
			if (f != null && AutoUVFaceProperty.check(f))
			{
				f.getProperty(FaceRegistry.uv).autoUV(getSelectedCube(), f.getFace());
			}
		}
	}

	private void copyCube(Button button)
	{
		Cube c = getSelectedCube();
		CreatorCube cc = (CreatorCube) c;

		CreatorCube newCube = new CreatorCube(new AABBf(c.getAabb()));
		newCube.setCollisionBox(c.isCollisionBox());
		newCube.setHitbox(c.isHitbox());
		newCube.setName(cc.getName());

		for (EnumFace f : EnumFace.getFaces())
		{
			newCube.setFace(f, copyFace(cc.getFace(f), newCube));
		}

		getSelectedBlock().getModel().addCube(newCube);
	}

	private CubeFace copyFace(CubeFace face, Cube parent)
	{
		if (face == null)
		{
			errorMessage("No Face Selected!");
			return null;
		}

		return new CubeFace(parent, face.getFace(), face.copyProperties());
	}

	private void addCube(Button button)
	{
		BlockEntry entry = getSelectedBlock();
		entry.getModel().addCube(BlockEntry.createEmptyCube());
	}

	private void save(Button button)
	{
		for (BlockEntry be : blocks.values())
		{
			be.save();
		}

		getMainApp().showDialog(new OkDialog("Blocks saved!", "Information")).center();
	}

	private void setTexture(Button button)
	{
		CubeFace face = getSelectedFace();

		if (getSelectedBlock().isParent())
		{
			OkTextInputDialog reference;
			getMainApp().showDialog(reference = new OkTextInputDialog("texture", "Reference"));

			reference.addOkClickEvent(b ->
			{
				face.getProperty(FaceRegistry.texture).setTexture(reference.getText());
				face.getProperty(FaceRegistry.texture).setTextureId(-1);
				face.getProperty(FaceRegistry.texture).setReference(true);
			});
			reference.center();
		} else
		{
			TextureDialog texture;
			getMainApp().showDialog(texture = new TextureDialog(atlas, face));

			texture.addOkClickEvent(b ->
			{
				face.getProperty(FaceRegistry.texture).setTexture(textureNamesReference.get(texture.getTexture()));
				face.getProperty(FaceRegistry.texture).setTextureId(texture.getTexture());
				face.getProperty(FaceRegistry.texture).setReference(false);

			});
			texture.center();
		}
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
				loadAllTextures();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		});

		file.center();
	}

	private void newBlock(Button c)
	{
		getMainApp().showDialog(new NewBlockDialog(this)).center();
	}

	/* List Change Events */


	/* Get Functions */

	public CubeFace getSelectedFace()
	{
		Cube cube = getSelectedCube();
		//		return cube.getFace(faceList.getSelectedFace().getFace());
		return null;
	}

	public Cube getSelectedCube()
	{
		if (cubeList.getSelectedIndex() == -1)
			return null;

		int cubeId = cubeList.getSelectedIndex();
		BlockEntry entry = getSelectedBlock();
		return entry.getModel().getCube(cubeId);
	}

	public BlockEntry getSelectedBlock()
	{
		if (blockList.getSelectedItems().size() == 0)
			return null;

		String name = blockList.getSelectedItems().get(0).getText();
		BlockEntry entry = blocks.get(name);

		if (entry == null)
			throw new NullPointerException("BlockEntry not found for \"" + name + "\"");

		return entry;
	}

	/* Other */

	private void errorMessage(String message)
	{
		getMainApp().showDialog(new MessageDialog(message, "Error", 250)).center();
	}

	private void loadBlocks()
	{
		File blocks = new File("creator\\blocks");
		if (!blocks.exists())
			blocks.mkdirs();

		for (File f : Objects.requireNonNull(blocks.listFiles()))
		{
			if (f.isDirectory())
				continue;

			this.blockList.addItem(f.getName().substring(0, f.getName().length() - 5));
			this.blocks.put(f.getName().substring(0, f.getName().length() - 5), new BlockEntry(f));
		}
	}

	private void renderPreview()
	{
		if (blockList.getSelectedItemsIndices().size() == 0)
			return;

		preview.renderBlock(this.blocks.get(this.blockList.getSelectedItems().get(0).getText()).getModel());

		SpriteRender.renderSpriteInverted(0, 0, getMainApp().getWidth(), getMainApp().getHeight(), 0, preview.getId());
	}

	private void loadAllTextures()
	{
		File textures = new File("creator\\blocks\\textures");

		if (atlas != null)
		{
			atlas.getSprite().deleteSprite();
		}

		if (Objects.requireNonNull(textures.listFiles()).length == 0)
		{
			return;
		}

		textureNames.clear();
		textureNamesReference.clear();
		atlas = new Atlas(Objects.requireNonNull(textures.listFiles()).length, 16);

		for (int i = 0; i < Objects.requireNonNull(textures.listFiles()).length; i++)
		{
			File f = Objects.requireNonNull(textures.listFiles())[i];
			if (f.isDirectory())
				continue;

			try
			{
				atlas.addNonSource(f);
				textureNames.put(Util.substringEnd(f.getName(), 4), i);
				textureNamesReference.put(i, Util.substringEnd(f.getName(), 4));
			} catch (Exception ex)
			{
				System.err.println(f.getName());
				System.err.println(atlas);
				ex.printStackTrace();
			}
		}

		atlas.finish();
		preview.update(atlas);
		updateBlockTextures();
	}

	private void updateBlockTextures()
	{
		for (BlockEntry be : blocks.values())
		{
			for (Cube cube : be.getModel().getCubes())
			{
				for (CubeFace face : cube.getFaces())
				{
					if (face == null)
						continue;

					if (!face.hasProperty("texture"))
						continue;

					TextureFaceProperty tex = face.getProperty(FaceRegistry.texture);

					if (tex.getTexture() == null || tex.getTexture().equals("null"))
					{
						tex.setTextureId(-1);
						continue;
					}

					if (tex.isReference())
						continue;

					Integer id = textureNames.get(tex.getTexture());
					if (id == null)
						throw new NullPointerException("Id not found for " + tex.getTexture());

					tex.setTextureId(id);
				}
			}
		}
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
