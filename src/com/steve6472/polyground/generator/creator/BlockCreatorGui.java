package com.steve6472.polyground.generator.creator;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.TintedCube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.polyground.generator.creator.components.FaceList;
import com.steve6472.polyground.generator.creator.dialogs.EditCubeDialog;
import com.steve6472.polyground.generator.creator.dialogs.TextureDialog;
import com.steve6472.polyground.generator.creator.dialogs.UVDialog;
import com.steve6472.polyground.shaders.ItemTextureShader;
import com.steve6472.polyground.tessellators.ItemTextureTessellator;
import com.steve6472.sge.gfx.Atlas;
import com.steve6472.sge.gfx.SpriteRender;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.components.Background;
import com.steve6472.sge.gui.components.Button;
import com.steve6472.sge.gui.components.ItemList;
import com.steve6472.sge.gui.components.dialog.FileSelectorDialog;
import com.steve6472.sge.gui.components.dialog.OkDialog;
import com.steve6472.sge.gui.components.dialog.OkTextInputDialog;
import com.steve6472.sge.main.MainApp;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.events.Event;
import com.steve6472.sge.main.events.WindowSizeEvent;
import org.joml.AABBf;
import org.joml.Matrix4f;

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
	private Button swichToItems, compile, newBlock, importTexture, setTexture, editCube, addCube, editUv, copyCube;
	public ItemList blockList, cubeList;
	private FaceList faceList;

	private HashMap<String, BlockEntry> blocks;
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
		blocks = new HashMap<>();
		textureNames = new HashMap<>();
		textureNamesReference = new HashMap<>();
		preview = new BlockPreview(this);
		loadAllTextures();
		getMainApp().getEventHandler().register(preview);

		textureShader = new ItemTextureShader();
		textureShader.bind();
		textureShader.setUniform(ItemTextureShader.ATLAS, 0);

		tessellator = new ItemTextureTessellator();

		Background.createComponent(this);

		/* Buttons */

		compile = new Button("Compile");
		compile.setLocation(10, 10);
		compile.setSize(80, 25);
		compile.addClickEvent(this::compile);
		addComponent(compile);

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
		swichToItems.setLocation(getMainApp().getWidth() - 90, getMainApp().getHeight() - 35);
		addComponent(swichToItems);

		editCube = new Button("Edit Cube");
		editCube.setLocation(getMainApp().getWidth() - 210, 255);
		editCube.setSize(95, 25);
		editCube.addClickEvent(this::editCube);
		editCube.setEnabled(false);
		addComponent(editCube);

		addCube = new Button("Add Cube");
		addCube.setLocation(getMainApp().getWidth() - 105, 255);
		addCube.setSize(95, 25);
		addCube.addClickEvent(this::addCube);
		addCube.setEnabled(false);
		addComponent(addCube);

		copyCube = new Button("Copy Cube");
		copyCube.setSize(95, 25);
		copyCube.setLocation(getMainApp().getWidth() - 210, 285);
		copyCube.addClickEvent(this::copyCube);
		copyCube.setEnabled(false);
		addComponent(copyCube);

		setTexture = new Button("Set Texture");
		setTexture.setLocation(getMainApp().getWidth() - 210, 25 * 14 + 150);
		setTexture.setSize(200, 25);
		setTexture.setEnabled(false);
		setTexture.addClickEvent(this::setTexture);
		addComponent(setTexture);

		editUv = new Button("Edit UV");
		editUv.setLocation(getMainApp().getWidth() - 210, 25 * 14 + 185);
		editUv.setSize(200, 25);
		editUv.setEnabled(false);
		editUv.addClickEvent(this::editUV);
		addComponent(editUv);

		/* Lists */

		blockList = new ItemList(20);
		blockList.setMultiselect(false);
		blockList.setLocation(10, 45);
		blockList.setSize(200, 25 * 20);
		blockList.addChangeEvent(this::onBlockChange);
		addComponent(blockList);

		cubeList = new ItemList(8);
		cubeList.setMultiselect(false);
		cubeList.setLocation(getMainApp().getWidth() - 210, 45);
		cubeList.setSize(200, 25 * 8);
		cubeList.addChangeEvent(this::onCubeListChange);
		addComponent(cubeList);

		faceList = new FaceList(this);
		faceList.setLocation(getMainApp().getWidth() - 210, 25 * 8 + 140);
		faceList.setSize(200, 25 * 6);
		addComponent(faceList);
		faceList.setEnabled(false);
		faceList.hideAll();

		loadBlocks();
		updateBlockTextures();
	}

	@Override
	public void guiTick()
	{
		preview.tick();

		setTexture.setEnabled(faceList.getSelectedFace() != null);
		editUv.setEnabled(faceList.getSelectedFace() != null && faceList.getSelectedFace().isVisible());
	}

	@Override
	public void render()
	{
		renderPreview();
	}

	/* Button Click Events */

	private void editUV(Button button)
	{
		UVDialog editUv;
		getMainApp().showDialog(editUv = new UVDialog(atlas, textureShader, tessellator, getSelectedFace()));

		editUv.addOkClickEvent(b ->
		{
			autoUV();
		});

		editUv.center();
	}

	private void editCube(Button button)
	{
		EditCubeDialog editCube;
		getMainApp().showDialog(editCube = new EditCubeDialog(getSelectedCube(), this));

		editCube.addOkClickEvent(b ->
		{
			autoUV();
		});

		editCube.center();
	}

	/**
	 * Check if selected cube has any faces with auto UV, if so it sets UV automatically
	 */
	private void autoUV()
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
		if (c instanceof TintedCube)
		{
//			((TintedCube) c)
		} else
		{
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
			runOnBlockChange();
		}
	}

	private CubeFace copyFace(CubeFace face, Cube parent)
	{
		if (face == null) return null;

		CubeFace ccf = new CubeFace(parent, face.getFace());

		return ccf;
	}

	private void addCube(Button button)
	{
		BlockEntry entry = getSelectedBlock();
		entry.getModel().addCube(BlockEntry.createEmptyCube());

		onBlockChange(blockList);
	}

	private void compile(Button button)
	{
		if (getSelectedBlock() != null)
		{
			getSelectedBlock().save();
		}

		getMainApp().showDialog(new OkDialog("Blocks saved!", "Information")).center();
	}

	private void setTexture(Button button)
	{
		CubeFace face = getSelectedFace();

		TextureDialog texture;
		getMainApp().showDialog(texture = new TextureDialog(atlas, face));

		texture.addOkClickEvent(b ->
		{
			if (face.hasProperty(FaceRegistry.texture))
			{
				face.getProperty(FaceRegistry.texture).setTexture(textureNamesReference.get(texture.getTexture()));
				face.getProperty(FaceRegistry.texture).setTextureId(texture.getTexture());
			}
		});

		texture.center();
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
		OkTextInputDialog name;
		getMainApp().showDialog(name = new OkTextInputDialog("name", "Block Name"));

		name.addOkClickEvent(b -> {

			blocks.put(name.getText(), new BlockEntry(name.getText()));
			blockList.addItem(name.getText());
		});

		name.center();
	}

	/* List Change Events */

	public void runOnCubeListChange()
	{
		onCubeListChange(cubeList);
	}

	private void onCubeListChange(ItemList c)
	{
		editCube.setEnabled(cubeList.getSelectedItems().size() != 0);
		addCube.setEnabled(blockList.getSelectedItems().size() != 0);
		copyCube.setEnabled(blockList.getSelectedItems().size() != 0);

		setTexture.setEnabled(false);

		Cube cube = getSelectedCube();
		if (cube == null)
		{
			faceList.hideAll();
			faceList.setEnabled(false);
			return;
		}

		faceList.setEnabled(true);

		for (EnumFace face : EnumFace.getFaces())
		{
			CubeFace cf = cube.getFace(face);

			faceList.get(face).setVisible(cf != null);
		}
	}

	public void runOnBlockChange()
	{
		onBlockChange(blockList);
	}

	private void onBlockChange(ItemList c)
	{
		cubeList.clear();
		faceList.hideAll();
		faceList.setEnabled(false);

		editCube.setEnabled(cubeList.getSelectedItems().size() != 0);
		addCube.setEnabled(blockList.getSelectedItems().size() != 0);

		if (c.getSelectedItems().size() == 0) return;

		String name = c.getSelectedItems().get(0).getText();
		BlockEntry entry = blocks.get(name);

		cubeList.clear();

		for (int i = 0; i < entry.getModel().getCubes().size(); i++)
		{
			cubeList.addItem(((ICreatorCube) entry.getModel().getCube(i)).getName());
		}
	}

	/* Get Functions */

	public CubeFace getSelectedFace()
	{
		Cube cube = getSelectedCube();
		return cube.getFace(faceList.getSelectedFace().getFace());
	}

	public Cube getSelectedCube()
	{
		if (cubeList.getSelectedItemsIndices().size() == 0) return null;

		int cubeId = cubeList.getSelectedItemsIndices().get(0);
		BlockEntry entry = getSelectedBlock();
		return entry.getModel().getCube(cubeId);
	}

	public BlockEntry getSelectedBlock()
	{
		if (blockList.getSelectedItems().size() == 0) return null;

		String name = blockList.getSelectedItems().get(0).getText();
		BlockEntry entry = blocks.get(name);

		if (entry == null)
			throw new NullPointerException("BlockEntry not found for \"" + name + "\"");

		return entry;
	}

	/* Other */

	private void loadBlocks()
	{
		File blocks = new File("creator\\blocks");
		if (!blocks.exists())
			blocks.mkdirs();

		for (File f : Objects.requireNonNull(blocks.listFiles()))
		{
			if (f.isDirectory()) continue;

			this.blockList.addItem(f.getName().substring(0, f.getName().length() - 5));
			this.blocks.put(f.getName().substring(0, f.getName().length() - 5), new BlockEntry(f));
		}
	}

	private void renderPreview()
	{
		if (blockList.getSelectedItemsIndices().size() == 0) return;

		preview.renderBlock(this.blocks.get(this.blockList.getSelectedItems().get(0).getText()).getModel());

		SpriteRender.renderSpriteInverted(0, 0, 16 * 70, 9 * 70, 0, preview.getId(), 16 * 70, 9 * 70);
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
			if (f.isDirectory()) continue;

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
					if (face == null) continue;

					if (!face.hasProperty("texture")) continue;

					TextureFaceProperty tex = face.getProperty(FaceRegistry.texture);

					if (tex.getTexture() == null || tex.getTexture().equals("null"))
					{
						tex.setTextureId(-1);
						continue;
					}

					Integer id = textureNames.get(tex.getTexture());
					if (id == null) throw new NullPointerException("Id not found for " + tex.getTexture());

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
