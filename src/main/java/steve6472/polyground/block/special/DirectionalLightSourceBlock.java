package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.light.EnumLightSource;
import steve6472.polyground.gfx.light.LightManager;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.ColorUtil;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.11.2019
 * Project: SJP
 *
 ***********************/
public class DirectionalLightSourceBlock extends Block implements ILightBlock
{
	public static final EnumProperty<EnumFace> FACING = States.FACING;
	private int color;
	private float constant, linear, quadratic;
	private float xOffset, yOffset, zOffset;
	private float cutOff;

	public DirectionalLightSourceBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(FACING, EnumFace.UP).get());
	}

	@Override
	public void load(JSONObject json)
	{
		constant = json.getFloat("constant");
		linear = json.getFloat("linear");
		quadratic = json.getFloat("quadratic");

		isFull = json.optBoolean("isFull", isFull);

		xOffset = json.optFloat("lightXOffset") / 16f;
		yOffset = json.optFloat("lightYOffset") / 16f;
		zOffset = json.optFloat("lightZOffset") / 16f;

		cutOff = json.optFloat("cutOff", -60);
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (placedOn == null)
			return getDefaultState();

		return getDefaultState().with(FACING, placedOn).get();
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockState oldState, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) != this)
			return;

		spawnLight(state, world, x, y, z);
	}

	@Override
	public void onPlayerBreak(BlockState state, World world, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		super.onPlayerBreak(state, world, player, breakedFrom, x, y, z);
		LightManager.removeLight(EnumLightSource.BLOCK, x + 0.5f + xOffset, y + 0.5f + yOffset, z + 0.5f + zOffset);
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(FACING);
	}

	@Override
	public void spawnLight(BlockState state, World world, int x, int y, int z)
	{
		float[] col = ColorUtil.getColors(color);
		EnumFace f = state.get(FACING);
		LightManager.replaceIdeal(EnumLightSource.BLOCK, x + 0.5f + xOffset, y + 0.5f + yOffset, z + 0.5f + zOffset, col[0], col[1], col[2], constant, linear, quadratic, f.getXOffset(), f.getYOffset(), f.getZOffset(), cutOff);
	}
}
