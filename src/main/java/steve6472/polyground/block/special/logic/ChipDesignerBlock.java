package steve6472.polyground.block.special.logic;

import org.joml.AABBf;
import org.joml.AABBi;
import org.joml.Vector3f;
import org.joml.Vector4i;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.ISpecialRender;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.logic.AbstractGate;
import steve6472.polyground.block.blockdata.logic.ChipDesignerData;
import steve6472.polyground.block.blockdata.logic.data.LogicBlockData;
import steve6472.polyground.block.blockdata.logic.other.Output;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.special.micro.AbstractIndexedMicroBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.item.ItemEntity;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.VoxModel;
import steve6472.polyground.gfx.stack.EntityTess;
import steve6472.polyground.gfx.stack.LineTess;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.item.Item;
import steve6472.polyground.item.itemdata.ChipData;
import steve6472.polyground.item.itemdata.IItemData;
import steve6472.polyground.item.itemdata.ItemData;
import steve6472.polyground.registry.Items;
import steve6472.polyground.registry.palette.PaletteRegistry;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.12.2020
 * Project: CaveGame
 *
 ***********************/
public class ChipDesignerBlock extends AbstractIndexedMicroBlock implements ISpecialRender
{
	private static final int CHIP_OFFSET_X = -2;
	private static final int CHIP_OFFSET_Y = -10;
	private static final int CHIP_OFFSET_Z = -2;

	private static final VoxModel DESIGNER_MODEL = new VoxModel(new File("custom_models/vox/logic/chip_designer.vox"));
	private static final VoxModel BOARD_INSIDE = new VoxModel(new File("custom_models/vox/logic/board_inside.vox"));

	private static final VoxModel INPUT_MODEL = new VoxModel(new File("custom_models/vox/logic/input.vox"));
	private static final VoxModel SWITCH_MODEL = new VoxModel(new File("custom_models/vox/logic/switch.vox"));

	private static final VoxModel OUTPUT_MODEL = new VoxModel(new File("custom_models/vox/logic/output.vox"));
	private static final VoxModel LIGHT_MODEL = new VoxModel(new File("custom_models/vox/logic/light.vox"));

	private static final VoxModel SELECTED = new VoxModel(new File("custom_models/vox/logic/selected.vox"));
	private static final VoxModel NOT_SELECTED = new VoxModel(new File("custom_models/vox/logic/not_selected.vox"));

	private static final VoxModel[] NUMBERS = new VoxModel[16];

	private static final AABBi TABLE = newAABBi(0, 0, 0, 32, 9, 32);
	private static final AABBi COLOR_PICKER_BUTTON = newAABBi(0, 10, 26, 6, 2, 6);
	private static final AABBi DISPLAY = newAABBi(31, 12, 6, 1, 20, 20);
	private static final AABBi PALETTE = newAABBi(31, 14, 8, 1, 16, 16);
	private static final AABBi BUILD_BOX = newAABBi(2, 10, 2, 22, 22, 22);
	private static final AABBi BUILD_BOX_EXTENDED = newAABBi(1, 9, 1, 24, 24, 24);
	private static final AABBi BOARD = newAABBi(7, 10, 26, 6, 2, 6);

	private static final AABBi INPUT = newAABBi(27, 10, 1, 4, 4, 4);
	private static final AABBi OUTPUT = newAABBi(27, 10, 27, 4, 4, 4);

	private static final AABBi INPUT_UP_ARROW = newAABBi(31, 29, 1, 1, 2, 4);
	private static final AABBi INPUT_DOWN_ARROW = newAABBi(31, 18, 1, 1, 2, 4);
	private static final AABBi OUTPUT_UP_ARROW = newAABBi(31, 29, 27, 1, 2, 4);
	private static final AABBi OUTPUT_DOWN_ARROW = newAABBi(31, 18, 27, 1, 2, 4);
	private static final AABBi INPUT_TYPE_UP_ARROW = newAABBi(31, 15, 1, 1, 2, 4);
	private static final AABBi OUTPUT_TYPE_UP_ARROW = newAABBi(31, 15, 27, 1, 2, 4);

	public ChipDesignerBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		DESIGNER_MODEL.reloadModel();
		BOARD_INSIDE.reloadModel();

		INPUT_MODEL.reloadModel();
		SWITCH_MODEL.reloadModel();

		OUTPUT_MODEL.reloadModel();
		LIGHT_MODEL.reloadModel();

		SELECTED.reloadModel();
		NOT_SELECTED.reloadModel();

		for (int i = 0; i < 16; i++)
		{
			NUMBERS[i] = new VoxModel(new File("custom_models/vox/numbers/number_" + i + ".vox"));
			NUMBERS[i].reloadModel();
		}
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() != KeyList.PRESS)
			return;

		final Vector4i piece = getLookedAtPiece(world, player, x, y, z);
		if (piece == null)
			return;
		EnumFace f = EnumFace.getFaces()[piece.w];
		int cx = piece.x + f.getXOffset();
		int cy = piece.y + f.getYOffset();
		int cz = piece.z + f.getZOffset();

		ChipDesignerData data = (ChipDesignerData) world.getData(x, y, z);

		if (click.getButton() == KeyList.RMB && player.holdsBlock() && player.getBlockDataInHand() instanceof LogicBlockData logicData)
		{
			data.setChipComponents(logicData.components);
			BOARD_INSIDE.insert(data.grid, 32, 32, 8, 10, 27);
			player.processNextBlockPlace = false;

			data.updateModel();
			world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			return;
		}

		if (data.chipComponents != null && click.getButton() == KeyList.RMB && !player.holdsBlock() && !player.holdsItem() && lookingAtBox(BUILD_BOX, cx, cy, cz))
		{
			place(data, piece, world, x, y, z);
		}

		if (data.chipComponents != null && click.getButton() == KeyList.LMB && lookingAtBox(BUILD_BOX, piece.x, piece.y, piece.z))
		{
			breakPixel(data, piece, world, x, y, z);
		}

		if (click.getButton() != KeyList.LMB)
			return;

		if (lookingAtBox(BOARD, piece))
		{
			if (data.chipComponents != null)
			{
				Item item = Items.getItemByName("chip");
				if (item != null)
				{
					ItemData itemData = null;

					if (item instanceof IItemData id)
						itemData = id.createNewItemData();
					if (itemData instanceof ChipData chipData)
					{
						chipData.setData(data.chipComponents, data.chipModel);
					} else
					{
						throw new RuntimeException("Chip item did not create correct ItemData (expected ChipData, got " + itemData.getClass().getName() + ")");
					}

					ItemEntity itemEntity = new ItemEntity(item, itemData, x + .5f, y + 2f, z + 0.5f);
					world.getEntityManager().addEntity(itemEntity);
				}
			}
		}

		if (lookingAtBox(COLOR_PICKER_BUTTON, piece))
		{
			data.selectedType = ChipDesignerData.SelectedType.COLOR;
			NOT_SELECTED.insert(data.grid, 32, 32, 26, 9, 0);
			NOT_SELECTED.insert(data.grid, 32, 32, 26, 9, 26);
			SELECTED.insert(data.grid, 32, 32, 0, 10, 26);

			toggleColorSelector(world, data, x, y, z);
		}

		if (data.selectedType == ChipDesignerData.SelectedType.COLOR && lookingAtBox(PALETTE, piece))
		{
			setColor(world, data, x, y, z, piece);
		}

		if (lookingAtBox(INPUT_TYPE_UP_ARROW, piece))
		{
			data.selectedInputType = data.selectedInputType.next();
			updateInput(world, data, x, y, z);
		}

		if (lookingAtBox(OUTPUT_TYPE_UP_ARROW, piece))
		{
			data.selectedOutputType = data.selectedOutputType.next();
			updateOutput(world, data, x, y, z);
		}

		if (lookingAtBox(INPUT_UP_ARROW, piece))
		{
			data.selectedInputIndex += 1;
			if (data.selectedInputIndex > 15)
				data.selectedInputIndex = 0;
			setNumber(world, data, x, y, z, data.selectedInputIndex, 31, 21, 1);
		}

		if (lookingAtBox(OUTPUT_UP_ARROW, piece))
		{
			data.selectedOutputIndex += 1;
			if (data.selectedOutputIndex > 15)
				data.selectedOutputIndex = 0;
			setNumber(world, data, x, y, z, data.selectedOutputIndex, 31, 21, 27);
		}

		if (lookingAtBox(INPUT_DOWN_ARROW, piece))
		{
			data.selectedInputIndex -= 1;
			if (data.selectedInputIndex < 0)
				data.selectedInputIndex = 15;
			setNumber(world, data, x, y, z, data.selectedInputIndex, 31, 21, 1);
		}

		if (lookingAtBox(OUTPUT_DOWN_ARROW, piece))
		{
			data.selectedOutputIndex -= 1;
			if (data.selectedOutputIndex < 0)
				data.selectedOutputIndex = 15;
			setNumber(world, data, x, y, z, data.selectedOutputIndex, 31, 21, 27);
		}

		if (lookingAtBox(INPUT, piece))
		{
			SELECTED.insert(data.grid, 32, 32, 26, 9, 0);
			NOT_SELECTED.insert(data.grid, 32, 32, 26, 9, 26);
			NOT_SELECTED.insert(data.grid, 32, 32, 0, 10, 26);
			data.selectedType = ChipDesignerData.SelectedType.INPUT;

			toggleColorSelector(world, data, x, y, z);
		}

		if (lookingAtBox(OUTPUT, piece))
		{
			NOT_SELECTED.insert(data.grid, 32, 32, 26, 9, 0);
			SELECTED.insert(data.grid, 32, 32, 26, 9, 26);
			NOT_SELECTED.insert(data.grid, 32, 32, 0, 10, 26);
			data.selectedType = ChipDesignerData.SelectedType.OUTPUT;

			toggleColorSelector(world, data, x, y, z);
		}
	}

	private void breakPixel(ChipDesignerData data, Vector4i piece, World world, int x, int y, int z)
	{
		int c = getChipColor(data, piece.x, piece.y, piece.z);

		// Break input
		if (c == 0xffffffff)
		{
			final AbstractGate gate = data.getInputGateAt(piece.x + CHIP_OFFSET_X, piece.y + CHIP_OFFSET_Y, piece.z + CHIP_OFFSET_Z);
			if (gate != null)
			{
				gate.setPosition(-1, -1, -1);
			}
		}

		// Break output
		if (c == 0xff010101)
		{
			final AbstractGate gate = data.getOutputGateAt(piece.x + CHIP_OFFSET_X, piece.y + CHIP_OFFSET_Y, piece.z + CHIP_OFFSET_Z);
			if (gate != null)
			{
				gate.setPosition(-1, -1, -1);
			}
		}

		setChipColor(data, piece.x, piece.y, piece.z, 0);

		data.updateModel();
		world.getSubChunkFromBlockCoords(x, y, z).rebuild();
	}

	private void place(ChipDesignerData data, Vector4i piece, World world, int x, int y, int z)
	{
		EnumFace f = EnumFace.getFaces()[piece.w];
		int cx = piece.x + f.getXOffset();
		int cy = piece.y + f.getYOffset();
		int cz = piece.z + f.getZOffset();

		if (data.selectedType == ChipDesignerData.SelectedType.COLOR)
		{
			setChipColor(data, cx, cy, cz, PaletteRegistry.RAINBOW.getColors()[data.selectedColorIndex + 128]);

			data.updateModel();
			world.getSubChunkFromBlockCoords(x, y, z).rebuild();
		}

		if (data.selectedType == ChipDesignerData.SelectedType.INPUT)
		{
			if (data.selectedInputIndex < data.inputComponents.size())
			{
				setChipColor(data, cx, cy, cz, 0xffffffff);
				AbstractGate gate = data.inputComponents.get(data.selectedInputIndex);

				// Remove pixel of old gate
				if (!gate.getPosition().equals(-1, -1, -1))
				{
					setChipColor(data, gate.getPosition().x - CHIP_OFFSET_X, gate.getPosition().y - CHIP_OFFSET_Y, gate.getPosition().z - CHIP_OFFSET_Z, 0);
				}
				gate.setPosition(cx + CHIP_OFFSET_X, cy + CHIP_OFFSET_Y, cz + CHIP_OFFSET_Z);

				data.updateModel();
				world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			}
		}

		if (data.selectedType == ChipDesignerData.SelectedType.OUTPUT)
		{
			if (data.selectedOutputIndex < data.outputComponents.size())
			{
				setChipColor(data, cx, cy, cz, 0xff010101);
				Output gate = (Output) data.outputComponents.get(data.selectedOutputIndex);

				// Remove pixel of old gate
				if (!gate.getPosition().equals(-1, -1, -1))
				{
					setChipColor(data, gate.getPosition().x - CHIP_OFFSET_X, gate.getPosition().y - CHIP_OFFSET_Y, gate.getPosition().z - CHIP_OFFSET_Z, 0);
				}
				gate.setPosition(cx + CHIP_OFFSET_X, cy + CHIP_OFFSET_Y, cz + CHIP_OFFSET_Z);
				gate.setLight(data.selectedOutputType == ChipDesignerData.OutputType.LIGHT);

				data.updateModel();
				world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			}
		}
	}

	private void setNumber(World world, ChipDesignerData data, int wx, int wy, int wz, int number, int x, int y, int z)
	{
		NUMBERS[number].insert(data.grid, 32, 32, x, y, z);

		data.updateModel();
		world.getSubChunkFromBlockCoords(wx, wy, wz).rebuild();
	}

	private void updateInput(World world, ChipDesignerData data, int x, int y, int z)
	{
		VoxModel model;

		if (data.selectedInputType == ChipDesignerData.InputType.INPUT)
			model = INPUT_MODEL;
		else
			model = SWITCH_MODEL;

		model.insert(data.grid, 32, 32, 27, 10, 1);

		data.updateModel();
		world.getSubChunkFromBlockCoords(x, y, z).rebuild();
	}

	private void updateOutput(World world, ChipDesignerData data, int x, int y, int z)
	{
		VoxModel model;

		if (data.selectedOutputType == ChipDesignerData.OutputType.OUTPUT)
			model = OUTPUT_MODEL;
		else
			model = LIGHT_MODEL;

		model.insert(data.grid, 32, 32, 27, 10, 27);

		data.updateModel();
		world.getSubChunkFromBlockCoords(x, y, z).rebuild();
	}

	/**
	 * Sets selectedColorIndex in ChipDesignerData from the color palette
	 *
	 * @param world world
	 * @param data data
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @param c clicked pixel
	 */
	private void setColor(World world, ChipDesignerData data, int x, int y, int z, Vector4i c)
	{
		INPUT_MODEL.insert(data.grid, 32, 32, 27, 10, 1);
		OUTPUT_MODEL.insert(data.grid, 32, 32, 27, 10, 27);
		int cx = c.z - 8;
		int cy = -(c.y - 14 - 15);

		byte index = (byte) (cx + cy * 16 - 128);

		if (data.selectedColorIndex == index)
			return;

		data.selectedColorIndex = index;

		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				data.grid[11][(1 + j) + (26 + 5 * i) * 32] = data.selectedColorIndex;
			}
		}

		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				data.grid[11][(5 * i) + (27 + j) * 32] = data.selectedColorIndex;
			}
		}

		data.grid[11][1 + 27 * 32] = data.selectedColorIndex;
		data.grid[11][1 + 30 * 32] = data.selectedColorIndex;
		data.grid[11][4 + 27 * 32] = data.selectedColorIndex;
		data.grid[11][4 + 30 * 32] = data.selectedColorIndex;

		data.updateModel();
		world.getSubChunkFromBlockCoords(x, y, z).rebuild();
	}

	private void toggleColorSelector(World world, ChipDesignerData data, int x, int y, int z)
	{
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				byte b = -127;

				if (data.selectedType == ChipDesignerData.SelectedType.COLOR)
					b = (byte) (i + j * 16 - 128);

				data.grid[14 + (15 - j)][31 + (i + 8) * 32] = b;
			}
		}

		data.updateModel();
		world.getSubChunkFromBlockCoords(x, y, z).rebuild();
	}

	@Override
	public void render(Stack stack, World world, BlockState state, int x, int y, int z)
	{
		stack.translate(-1f, 0, -1f);
		ChipDesignerData data = (ChipDesignerData) world.getData(x, y, z);

		 if (data.chipComponents == null)
		 	return;

		for (AbstractGate g : data.inputComponents)
		{
			stack.pushMatrix();
			stack.scale(1f / 16f);

			LineTess ltess = stack.getLineTess();

			ltess.coloredBoxWHD(g.getPosition().x - 0.1f - CHIP_OFFSET_X, g.getPosition().y - 0.1f - CHIP_OFFSET_Y, g.getPosition().z - 0.1f - CHIP_OFFSET_Z,
				1.2f, 1.2f, 1.2f, 0.7f, 0.7f, 0.7f, 1);

			stack.popMatrix();
		}

		for (AbstractGate g : data.outputComponents)
		{
			stack.pushMatrix();
			stack.scale(1f / 16f);

			LineTess ltess = stack.getLineTess();

			ltess.coloredBoxWHD(g.getPosition().x - 0.1f - CHIP_OFFSET_X, g.getPosition().y - 0.1f - CHIP_OFFSET_Y, g.getPosition().z - 0.1f - CHIP_OFFSET_Z,
				1.2f, 1.2f, 1.2f, 0.3f, 0.3f, 0.3f, 1);

			stack.popMatrix();
		}

		Vector4i c = getLookedAtPiece(world, CaveGame.getInstance().getPlayer(), x, y, z);
		if (c == null)
			return;

		EnumFace f = EnumFace.getFaces()[c.w];
		int cx = c.x + f.getXOffset();
		int cy = c.y + f.getYOffset();
		int cz = c.z + f.getZOffset();

		if (lookingAtBox(BUILD_BOX, cx, cy, cz))
		{
			float cr = (float) (Math.cos(Math.toRadians((System.currentTimeMillis() % (3600)) * 0.1f)) / 4f + 0.70f);
			float cg = (float) (Math.cos(Math.toRadians(((System.currentTimeMillis() + 1200) % (3600)) * 0.1f)) / 4f + 0.70f);
			float cb = (float) (Math.cos(Math.toRadians(((System.currentTimeMillis() + 2400) % (3600)) * 0.1f)) / 4f + 0.70f);
			// Highlight input
			if (getChipColor(data, c.x, c.y, c.z) == 0xffffffff)
			{
				stack.pushMatrix();
				stack.scale(1f / 16f);

				EntityTess tess = stack.getEntityTess();
				tess.color(cr, cg, cb, 0.5f);
				tess.rect(26.9f, 9.9f, 0.9f, 4.2f, 4.2f, 4.2f);

				LineTess ltess = stack.getLineTess();
				ltess.coloredBoxWHD(26.9f, 9.9f, 0.9f, 4.2f, 4.2f, 4.2f, 0, 0, 0, 1);

				stack.popMatrix();
			}
			// Highlight output
			if (getChipColor(data, c.x, c.y, c.z) == 0xff010101)
			{
				stack.pushMatrix();
				stack.scale(1f / 16f);

				EntityTess tess = stack.getEntityTess();
				tess.color(cr, cg, cb, 0.5f);
				tess.rect(26.9f, 9.9f, 26.9f, 4.2f, 4.2f, 4.2f);

				LineTess ltess = stack.getLineTess();
				ltess.coloredBoxWHD(26.9f, 9.9f, 26.9f, 4.2f, 4.2f, 4.2f, 0, 0, 0, 1);

				stack.popMatrix();
			}

			if (data.selectedType == ChipDesignerData.SelectedType.COLOR)
			{
				renderColor(stack, PaletteRegistry.RAINBOW.getColors()[data.selectedColorIndex + 128], c);
			}

			if (data.selectedType == ChipDesignerData.SelectedType.INPUT && data.selectedInputIndex < data.inputComponents.size())
			{
				renderColor(stack, 0xffffffff, c);
			}

			if (data.selectedType == ChipDesignerData.SelectedType.OUTPUT && data.selectedOutputIndex < data.outputComponents.size())
			{
				renderColor(stack, 0xff010101, c);
			}
		}
	}

	private void renderColor(Stack stack, int color, Vector4i c)
	{
		EnumFace f = EnumFace.getFaces()[c.w];
		int cx = c.x + f.getXOffset();
		int cy = c.y + f.getYOffset();
		int cz = c.z + f.getZOffset();

		color &= ~0xff000000; // Erase alpha bits
		color |= 0xc0000000;  // Set alpha bits to 0.75 opacity

		EntityTess tess = stack.getEntityTess();
		tess.color(color);
		tess.rectShade(cx / 16f, cy / 16f, cz / 16f, 1f / 16, 1f / 16f, 1f / 16f);
	}

	private int getChipColor(ChipDesignerData data, int x, int y, int z)
	{
		x += CHIP_OFFSET_X;
		y += CHIP_OFFSET_Y;
		z += CHIP_OFFSET_Z;
		if (y >= 0 && y < 22 && x >= 0 && x < 22 && z >= 0 && z < 22)
			return data.chipModel[y][x + z * 22];
		return 0;
	}

	private void setChipColor(ChipDesignerData data, int x, int y, int z, int color)
	{
		x += CHIP_OFFSET_X;
		y += CHIP_OFFSET_Y;
		z += CHIP_OFFSET_Z;
		if (y >= 0 && y < 22 && x >= 0 && x < 22 && z >= 0 && z < 22)
			data.chipModel[y][x + z * 22] = color;
	}

	@Override
	public boolean isPickable(BlockState state, World world, int x, int y, int z, Player player)
	{
		final Vector4i piece = getLookedAtPiece(world, player, x, y, z);
		return piece != null && TABLE.containsPoint(piece.x, piece.y, piece.z) && !BUILD_BOX_EXTENDED.containsPoint(piece.x, piece.y, piece.z);
	}

	@Override
	protected boolean renderSelectedMicro(World world, BlockState state, int x, int y, int z)
	{
		final Vector4i piece = getLookedAtPiece(world, CaveGame.getInstance().getPlayer(), x, y, z);
		if (piece != null)
			return !lookingAtBox(TABLE, piece);
		else
			return false;
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		final Player player = CaveGame.getInstance().getPlayer();
		ChipDesignerData data = (ChipDesignerData) world.getData(x, y, z);

		Vector4i c = getLookedAtPiece(world, player, x, y, z);

		List<CubeHitbox> hitboxes = new ArrayList<>();

		final AABBf table = toAABBf(TABLE);
		table.maxY += 1;
		hitboxes.add(new CubeHitbox(table).setVisible(lookingAtBox(TABLE, c)));
		hitboxes.add(new CubeHitbox(toAABBf(COLOR_PICKER_BUTTON)).setVisible(lookingAtBox(COLOR_PICKER_BUTTON, c)));
		hitboxes.add(new CubeHitbox(toAABBf(DISPLAY)).setVisible(lookingAtBox(DISPLAY, c)));
		hitboxes.add(new CubeHitbox(toAABBf(BOARD)).setVisible(lookingAtBox(BOARD, c)));

		if (lookingAtBox(PALETTE, c) && data.selectedType == ChipDesignerData.SelectedType.COLOR)
		{
			hitboxes.add(createPixelHitbox(c));
			hitboxes.add(new CubeHitbox(toAABBf(PALETTE)));
		}

		hitboxes.add(new CubeHitbox(toAABBf(INPUT_UP_ARROW)).setVisible(lookingAtBox(INPUT_UP_ARROW, c)));
		hitboxes.add(new CubeHitbox(toAABBf(INPUT_DOWN_ARROW)).setVisible(lookingAtBox(INPUT_DOWN_ARROW, c)));
		hitboxes.add(new CubeHitbox(toAABBf(INPUT_TYPE_UP_ARROW)).setVisible(lookingAtBox(INPUT_TYPE_UP_ARROW, c)));

		hitboxes.add(new CubeHitbox(toAABBf(OUTPUT_UP_ARROW)).setVisible(lookingAtBox(OUTPUT_UP_ARROW, c)));
		hitboxes.add(new CubeHitbox(toAABBf(OUTPUT_DOWN_ARROW)).setVisible(lookingAtBox(OUTPUT_DOWN_ARROW, c)));
		hitboxes.add(new CubeHitbox(toAABBf(OUTPUT_TYPE_UP_ARROW)).setVisible(lookingAtBox(OUTPUT_TYPE_UP_ARROW, c)));

		hitboxes.add(new CubeHitbox(toAABBf(INPUT)).setVisible(lookingAtBox(INPUT, c)));
		hitboxes.add(new CubeHitbox(toAABBf(OUTPUT)).setVisible(lookingAtBox(OUTPUT, c)));

		if (renderSelectedMicro(world, state, x, y, z) && c != null)
			hitboxes.add(createPixelHitbox(c));

		return hitboxes.toArray(CubeHitbox[]::new);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		final ChipDesignerData chipDesignerData = new ChipDesignerData(DESIGNER_MODEL.createCopy());
		INPUT_MODEL.insert(chipDesignerData.grid, 32, 32, 27, 10, 1);
		OUTPUT_MODEL.insert(chipDesignerData.grid, 32, 32, 27, 10, 27);
		NUMBERS[0].insert(chipDesignerData.grid, 32, 32, 31, 21, 1);
		NUMBERS[0].insert(chipDesignerData.grid, 32, 32, 31, 21, 27);
		SELECTED.insert(chipDesignerData.grid, 32, 32, 26, 9, 0);
		NOT_SELECTED.insert(chipDesignerData.grid, 32, 32, 26, 9, 26);
		NOT_SELECTED.insert(chipDesignerData.grid, 32, 32, 0, 10, 26);
		chipDesignerData.updateModel();

		return chipDesignerData;
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL)
			return 0;

		// Create model from indexed data
		int tris = super.createModel(x, y, z, world, state, buildHelper, modelLayer);

		// Add chip color model data
		ChipDesignerData data = (ChipDesignerData) world.getData(x, y, z);

		for (int i = 0; i < 22; i++)
		{
			for (int j = 0; j < 22; j++)
			{
				for (int k = 0; k < 22; k++)
				{
					if (data.chipModel[j][i + k * 22] != 0)
					{
						int flags = Bakery.createFaceFlags(
							i != (22 - 1) && data.chipModel[j][(i + 1) + k * 22] != 0,
							k != (22 - 1) && data.chipModel[j][i + (k + 1) * 22] != 0,
							i != 0 && data.chipModel[j][(i - 1) + k * 22] != 0,
							k != 0 && data.chipModel[j][i + (k - 1) * 22] != 0,
							j != (22 - 1) && data.chipModel[j + 1][i + k * 22] != 0,
							j != 0 && data.chipModel[j - 1][i + k * 22] != 0
						);
						tris += Bakery.coloredCube_1x1(i + 2 + getOffsetX(), j + 10 + getOffsetY(), k + 2 + getOffsetZ(), data.chipModel[j][i + k * 22], flags);
					}
				}
			}
		}

		return tris;
	}

	/**
	 *
	 * @param world world
	 * @param player player
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @return x, y, z, side
	 *         UP, DOWN, NORTH, EAST, SOUTH, WEST
	 *         0    1      2     3      4     5
	 */
	protected Vector4i getLookedAtPiece(World world, Player player, int x, int y, int z)
	{
		ChipDesignerData data = (ChipDesignerData) world.getData(x, y, z);
		if (data == null)
			return null;

		AABBf box = new AABBf();
		Vector3f res = new Vector3f();

		int cx = 0, cy = 0, cz = 0;
		float closestDistance = 10f;
		int face = 0;
		float inv = 1f / 16f;

		for (int i = 0; i < getSize(); i++)
		{
			for (int j = 0; j < getSize(); j++)
			{
				for (int k = 0; k < getSize(); k++)
				{
					boolean doCheck;
					// Check the Build Area
					if (i >= 2 && j >= 10 && k >= 2 && i < 24 && j < 32 && k < 24)
					{
						doCheck = data.chipModel[(j - 10)][(i - 2) + (k - 2) * 22] != 0;
					} else
					{
						doCheck = data.grid[j][i + k * getSize()] != -128;
					}
					if (doCheck)
					{
						box.setMin(x + i * inv + getOffsetX() * inv, y + j * inv + getOffsetY() * inv, z + k * inv + getOffsetZ() * inv);
						box.setMax(x + i * inv + inv + getOffsetX() * inv, y + j * inv + inv + getOffsetY() * inv, z + k * inv + inv + getOffsetZ() * inv);

						if (intersectsAABB(player.viewDir, player.getCamera().getPosition(), box, res))
						{
							if (res.y < closestDistance)
							{
								closestDistance = res.y;
								cx = i;
								cy = j;
								cz = k;
								face = (int) res.z;
							}
						}
					}
				}
			}
		}

		if (closestDistance == 10f)
			return null;

		return new Vector4i(cx, cy, cz, face);
	}

	@Override
	protected int getSize()
	{
		return 32;
	}

	private static AABBi newAABBi(int x, int y, int z, int w, int h, int d)
	{
		return new AABBi(x, y, z, x + w, y + h, z + d);
	}

	private static boolean lookingAtBox(AABBi box, Vector4i point)
	{
		return point != null && (point.x >= box.minX && point.y >= box.minY && point.z >= box.minZ && point.x < box.maxX && point.y < box.maxY && point.z < box.maxZ);
	}

	private static boolean lookingAtBox(AABBi box, int x, int y, int z)
	{
		return (x >= box.minX && y >= box.minY && z >= box.minZ && x < box.maxX && y < box.maxY && z < box.maxZ);
	}

	private static AABBf toAABBf(AABBi box)
	{
		return new AABBf(
			box.minX / 16f - 8 / 16f,
			box.minY / 16f,
			box.minZ / 16f - 8 / 16f,
			box.maxX / 16f - 8 / 16f,
			box.maxY / 16f,
			box.maxZ / 16f - 8 / 16f);
	}
}
