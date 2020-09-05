package steve6472.polyground.block.special;

import steve6472.SSS;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.Player;
import steve6472.polyground.gfx.light.EnumLightSource;
import steve6472.polyground.gfx.light.LightManager;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.ColorUtil;

import java.io.File;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.11.2019
 * Project: SJP
 *
 ***********************/
public class DirectionalLightSourceBlock extends Block
{
	public static final EnumProperty<EnumFace> FACING = States.FACING;
	private File f;
	private int color;
	private float constant, linear, quadratic;
	private float xOffset, yOffset, zOffset;
	private float cutOff;

	public DirectionalLightSourceBlock(File f)
	{
		super(f);
		this.f = f;
		setDefaultState(getDefaultState().with(FACING, EnumFace.UP).get());
	}

	@Override
	public void postLoad()
	{
		if (f.isFile())
		{
			SSS sss = new SSS(f);
			if (sss.hasValue("color"))
				color = sss.getHexInt("color");
			else
				color = 0xffffff;

			constant = sss.getFloat("constant");
			linear = sss.getFloat("linear");
			quadratic = sss.getFloat("quadratic");
			if (sss.containsName("isFull"))
				isFull = sss.getBoolean("isFull");

			if (sss.containsName("lightXOffset"))
				xOffset = sss.getFloat("lightXOffset") / 16f;
			if (sss.containsName("lightYOffset"))
				yOffset = sss.getFloat("lightYOffset") / 16f;
			if (sss.containsName("lightZOffset"))
				zOffset = sss.getFloat("lightZOffset") / 16f;

			if (sss.containsName("cutOff"))
				cutOff = sss.getFloat("cutOff");
			else
				cutOff = -60;
		}

		f = null;
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

		float[] col = ColorUtil.getColors(color);
		EnumFace f = state.get(FACING);
		LightManager.replaceIdeal(EnumLightSource.BLOCK, x + 0.5f + xOffset, y + 0.5f + yOffset, z + 0.5f + zOffset, col[0], col[1], col[2], constant, linear, quadratic, f.getXOffset(), f.getYOffset(), f.getZOffset(), cutOff);
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
}
