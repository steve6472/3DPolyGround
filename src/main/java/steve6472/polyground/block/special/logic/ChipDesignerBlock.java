package steve6472.polyground.block.special.logic;

import org.joml.AABBf;
import org.joml.AABBi;
import org.joml.Vector4i;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.ISpecialRender;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.logic.ChipDesignerData;
import steve6472.polyground.block.blockdata.logic.data.LogicBlockData;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.special.micro.AbstractIndexedMicroBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.VoxModel;
import steve6472.polyground.gfx.stack.EntityTess;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.world.World;
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
//		System.out.println(piece);

		ChipDesignerData data = (ChipDesignerData) world.getData(x, y, z);

		if (click.getButton() == KeyList.RMB && player.holdsBlock() && player.getBlockDataInHand() instanceof LogicBlockData logicData)
		{
			data.chip = logicData;
			BOARD_INSIDE.insert(data.grid, 32, 32, 8, 10, 27);
			player.processNextBlockPlace = false;

			data.updateModel();
			world.getSubChunkFromBlockCoords(x, y, z).rebuild();
			return;
		}

		if (click.getButton() != KeyList.LMB)
			return;

		if (lookingAtBox(COLOR_PICKER_BUTTON, piece))
		{
			toggleColorSelector(world, data, x, y, z);
		}

		if (data.isColorSelectorOpen && lookingAtBox(PALETTE, piece))
		{
			setColor(world, data, x, y, z, piece);
		}

		if (data.isColorSelectorOpen && lookingAtBox(PALETTE, piece))
		{
			setColor(world, data, x, y, z, piece);
		}

		if (lookingAtBox(INPUT_TYPE_UP_ARROW, piece))
		{
			data.selectedInputType = data.selectedInputType.next();
			updateInput(world, data, x, y, z, piece);
		}

		if (lookingAtBox(OUTPUT_TYPE_UP_ARROW, piece))
		{
			data.selectedOutputType = data.selectedOutputType.next();
			updateOutput(world, data, x, y, z, piece);
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
			data.selectedType = ChipDesignerData.SelectedType.INPUT;

			data.updateModel();
			world.getSubChunkFromBlockCoords(x, y, z).rebuild();
		}

		if (lookingAtBox(OUTPUT, piece))
		{
			NOT_SELECTED.insert(data.grid, 32, 32, 26, 9, 0);
			SELECTED.insert(data.grid, 32, 32, 26, 9, 26);
			data.selectedType = ChipDesignerData.SelectedType.OUTPUT;

			data.updateModel();
			world.getSubChunkFromBlockCoords(x, y, z).rebuild();
		}
	}

	private void setNumber(World world, ChipDesignerData data, int wx, int wy, int wz, int number, int x, int y, int z)
	{
		NUMBERS[number].insert(data.grid, 32, 32, x, y, z);

		data.updateModel();
		world.getSubChunkFromBlockCoords(wx, wy, wz).rebuild();
	}

	private void updateInput(World world, ChipDesignerData data, int x, int y, int z, Vector4i c)
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

	private void updateOutput(World world, ChipDesignerData data, int x, int y, int z, Vector4i c)
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
		data.isColorSelectorOpen = !data.isColorSelectorOpen;

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				byte b = -127;

				if (data.isColorSelectorOpen)
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
//		ChipDesignerData data = (ChipDesignerData) world.getData(x, y, z);

		final EntityTess tess = stack.getEntityTess();
		tess.color(1, 1, 1, 1f);
		tess.rectShade(0, 0, 0, 1, 1, 1);
	}

	private void renderAABBi(EntityTess tess, AABBi aabb)
	{
		tess.rectShade(aabb.minX / 16f, aabb.minY / 16f, aabb.minZ / 16f, (aabb.maxX - aabb.minX) / 16f, (aabb.maxY - aabb.minY) / 16f, (aabb.maxZ - aabb.minZ) / 16f);
	}

	@Override
	public boolean isPickable(BlockState state, World world, int x, int y, int z, Player player)
	{
		final Vector4i piece = getLookedAtPiece(world, player, x, y, z);
		return piece != null && TABLE.containsPoint(piece.x, piece.y, piece.z);
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
//		if (c == null)
//			return super.getHitbox(world, state, x, y, z);

		List<CubeHitbox> hitboxes = new ArrayList<>();

		hitboxes.add(new CubeHitbox(toAABBf(TABLE)).setVisible(lookingAtBox(TABLE, c)));
		hitboxes.add(new CubeHitbox(toAABBf(COLOR_PICKER_BUTTON)).setVisible(lookingAtBox(COLOR_PICKER_BUTTON, c)));
		hitboxes.add(new CubeHitbox(toAABBf(DISPLAY)).setVisible(lookingAtBox(DISPLAY, c)));

		if (lookingAtBox(PALETTE, c) && data.isColorSelectorOpen)
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
		chipDesignerData.updateModel();

		return chipDesignerData;
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
		boolean b = (point != null && (point.x >= box.minX && point.y >= box.minY && point.z >= box.minZ && point.x < box.maxX && point.y < box.maxY && point.z < box.maxZ));
		if (b) System.out.println(box + " " + point);
		return b;
	}

	private static AABBf toAABBf(AABBi box)
	{
		return new AABBf(box.minX / 16f - 8 / 16f, box.minY / 16f, box.minZ / 16f - 8 / 16f, box.maxX / 16f - 8 / 16f, box.maxY / 16f, box.maxZ / 16f - 8 / 16f);
	}
}
