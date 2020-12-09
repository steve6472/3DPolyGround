package steve6472.polyground.block.special.logic;

import org.joml.AABBf;
import org.joml.AABBi;
import org.joml.Vector4i;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.logic.ChipDesignerData;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.special.micro.AbstractIndexedMicroBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.12.2020
 * Project: CaveGame
 *
 ***********************/
public class ChipDesignerBlock extends AbstractIndexedMicroBlock
{
	private static final File DESIGNER_MODEL = new File("custom_models/chip_designer.vox");

	private static final AABBi TABLE = newAABBi(0, 0, 0, 32, 10, 32);
	private static final AABBi COLOR_PICKER = newAABBi(0, 10, 26, 6, 2, 6);

	private byte[][] model;

	public ChipDesignerBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		VoxelFileReader voxReader = new VoxelFileReader();
		try
		{
			model = new byte[32][32 * 32];
			for (int i = 0; i < 32; i++)
			{
				for (int j = 0; j < 32 * 32; j++)
				{
					model[i][j] = -128;
				}
			}
			voxReader.read(DESIGNER_MODEL, (x, y, z, i) ->
			{
				model[z][x + (31 - y) * 32] = (byte) (i - 1);
			});
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		
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

	private static boolean lookingAtBox(AABBi box, Vector4i point)
	{
		return box.containsPoint(point.x, point.y, point.z);
	}

	private static AABBf toAABBf(AABBi box)
	{
		return new AABBf(box.minX / 16f - 8 / 16f, box.minY / 16f, box.minZ / 16f - 8 / 16f, box.maxX / 16f - 8 / 16f, box.maxY / 16f, box.maxZ / 16f - 8 / 16f);
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		final Player player = CaveGame.getInstance().getPlayer();

		Vector4i c = getLookedAtPiece(world, player, x, y, z);
		if (c == null)
			return super.getHitbox(world, state, x, y, z);

		List<CubeHitbox> hitboxes = new ArrayList<>();

		if (lookingAtBox(TABLE, c))
			hitboxes.add(new CubeHitbox(toAABBf(TABLE)));

		if (lookingAtBox(COLOR_PICKER, c))
			hitboxes.add(new CubeHitbox(toAABBf(COLOR_PICKER)));

		return hitboxes.toArray(CubeHitbox[]::new);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new ChipDesignerData(model);
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
}
