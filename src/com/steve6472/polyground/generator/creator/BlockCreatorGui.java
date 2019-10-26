package com.steve6472.polyground.generator.creator;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.CubeFace;
import com.steve6472.polyground.block.model.faceProperty.AutoUVFaceProperty;
import com.steve6472.polyground.block.model.faceProperty.TextureFaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import com.steve6472.polyground.generator.creator.components.ColorDialog;
import com.steve6472.polyground.generator.creator.components.ComponentHolder;
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
import com.steve6472.sge.gui.components.dialog.*;
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
	private Button swichToItems, save, newBlock, importTexture, setTexture, editCube, addCube, editUv, copyCube, applyToAllFaces, color;
	public ItemList blockList, cubeList;
	private FaceList faceList;
	private ComponentHolder holder;

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

		holder = new ComponentHolder();
		addComponent(holder);

		/* Buttons */

		save = new Button("Save");
		save.setLocation(10, 10);
		save.setSize(80, 25);
		save.addClickEvent(this::compile);
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

		editCube = new Button("Edit Cube");
		editCube.setRelativeLocation(0, 255);
		editCube.setSize(95, 25);
		editCube.addClickEvent(this::editCube);
		editCube.setEnabled(false);
		holder.addComponent(editCube);

		addCube = new Button("Add Cube");
		addCube.setRelativeLocation(105, 255);
		addCube.setSize(95, 25);
		addCube.addClickEvent(this::addCube);
		addCube.setEnabled(false);
		holder.addComponent(addCube);

		copyCube = new Button("Copy Cube");
		copyCube.setSize(95, 25);
		copyCube.setRelativeLocation(0, 285);
		copyCube.addClickEvent(this::copyCube);
		copyCube.setEnabled(false);
		holder.addComponent(copyCube);

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
		blockList.addChangeEvent(this::onBlockChange);
		addComponent(blockList);

		cubeList = new ItemList(8);
		cubeList.setMultiselect(false);
		cubeList.setRelativeLocation(0, 45);
		cubeList.setSize(200, 25 * 8);
		cubeList.addChangeEvent(this::onCubeListChange);
		holder.addComponent(cubeList);

		faceList = new FaceList(this);
		faceList.setRelativeLocation(0, 25 * 8 + 140);
		faceList.setSize(200, 25 * 6);
		holder.addComponent(faceList);
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
		applyToAllFaces.setEnabled(faceList.getSelectedFace() != null && faceList.getSelectedFace().isVisible());
		color.setEnabled(faceList.getSelectedFace() != null && faceList.getSelectedFace().isVisible());
	}

	@Override
	public void render()
	{
		renderPreview();
	}

	@Event
	public void updatePositions(WindowSizeEvent e)
	{
		holder.setLocation(e.getWidth() - 210, 0);
		preview.updateSize(e);
	}

	/* Button Click Events */

	private void color(Button button)
	{
		if (getSelectedCube() == null) { errorMessage("Cube is not selected!"); return; }
		if (getSelectedFace() == null) { errorMessage("Face is not selected!"); return; }

		ColorDialog colorDialog = new ColorDialog(getSelectedFace());
		colorDialog.setOkEvent((b, s)->
		{
			getSelectedFace().getProperty(FaceRegistry.emissive).setEmissive(colorDialog.isEmissive());
			getSelectedFace().getProperty(FaceRegistry.tint)
				.setTint(s.getRed() / 255f, s.getGreen() / 255f, s.getBlue() / 255f);
		});
		colorDialog.addChangeEvent(c -> {
			getSelectedFace().getProperty(FaceRegistry.tint)
				.setTint(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
		});

		getMainApp().showDialog(colorDialog).center();

	}

	private void applyToAllFaces(Button button)
	{
		if (getSelectedCube() == null) { errorMessage("Cube is not selected!"); return; }
		if (getSelectedFace() == null) { errorMessage("Face is not selected!"); return; }

		YesNoDialog proceed = new YesNoDialog("Do you wish to proceed?", "Proceed?");
		getMainApp().showDialog(proceed).center();

		proceed.addYesClickEvent(c ->
		{
			for (EnumFace face : EnumFace.getFaces())
			{
				if (face == getSelectedFace().getFace()) continue;

				getSelectedCube().getFace(face).clearProperties();
				getSelectedCube().getFace(face).setProperties(getSelectedFace().copyProperties());
			}
		});

		runOnCubeListChange();
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

	private CubeFace copyFace(CubeFace face, Cube parent)
	{
		if (face == null) return null;

		CubeFace ccf = new CubeFace(parent, face.getFace(), face.copyProperties());

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
			if (texture.isReference())
			{
				face.getProperty(FaceRegistry.texture).setTexture(texture.getReferenceName());
				face.getProperty(FaceRegistry.texture).setTextureId(-1);
				face.getProperty(FaceRegistry.texture).setReference(true);
			} else
			{
				face.getProperty(FaceRegistry.texture).setTexture(textureNamesReference.get(texture.getTexture()));
				face.getProperty(FaceRegistry.texture).setTextureId(texture.getTexture());
				face.getProperty(FaceRegistry.texture).setReference(false);
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

			faceList.get(face).setVisible(cf.getProperty(FaceRegistry.isVisible).isVisible());
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
			if (f.isDirectory()) continue;

			this.blockList.addItem(f.getName().substring(0, f.getName().length() - 5));
			this.blocks.put(f.getName().substring(0, f.getName().length() - 5), new BlockEntry(f));
		}
	}

	private void renderPreview()
	{
		if (blockList.getSelectedItemsIndices().size() == 0) return;

		preview.renderBlock(this.blocks.get(this.blockList.getSelectedItems().get(0).getText()).getModel());

		SpriteRender.renderSpriteInverted(0, 0, getMainApp().getWidth(), getMainApp().getHeight(), 0, preview.getId(),
			getMainApp().getWidth(), getMainApp().getHeight());
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

					if (tex.isReference()) continue;

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
