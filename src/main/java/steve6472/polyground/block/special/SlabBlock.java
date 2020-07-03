package steve6472.polyground.block.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabBlock extends Block
{
	public static final EnumProperty<EnumSlabType> TYPE = States.SLAB_TYPE;

	public EnumSlabType slabType;

	public SlabBlock(File f, int id)
	{
		super(f, id);
//		this.f = f;
		isFull = false;
		setDefaultState(getDefaultState().with(TYPE, EnumSlabType.BOTTOM).get());
	}

	@Override
	public void onClick(SubChunk subChunk, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (!(click.getAction() == KeyList.PRESS && click.getButton() == KeyList.LMB))
			return;

		HitResult hitResult = CaveGame.getInstance().hitPicker.getHitResult();

		BlockState tobeplaced = null;

		final BlockState bottom = state.with(TYPE, EnumSlabType.BOTTOM).get();
		final BlockState top = state.with(TYPE, EnumSlabType.TOP).get();

		if (state.get(SlabBlock.TYPE) == EnumSlabType.DOUBLE)
		{
			if (hitResult.getFace() == EnumFace.UP)
			{
				subChunk.getWorld().setState(hitResult, tobeplaced = bottom);
			} else if (hitResult.getFace() == EnumFace.DOWN)
			{
				subChunk.getWorld().setState(hitResult, tobeplaced = top);
			} else if (hitResult.getFace().isSide())
			{
				if (Double.parseDouble("0." + ("" + hitResult.getPy()).split("\\.")[1]) >= 0.5f)
				{
					subChunk.getWorld().setState(hitResult, tobeplaced = bottom);
				} else
				{
					subChunk.getWorld().setState(hitResult, tobeplaced = top);
				}
			}
		} else
		{
			subChunk.getWorld().setBlock(hitResult, Block.air);
		}

		assert tobeplaced != null;

		if (tobeplaced == bottom)
			tobeplaced = top;
		else
			tobeplaced = bottom;

		tobeplaced.getBlock().onBreak(subChunk, state, player, hitResult.getFace(), hitResult.getX(), hitResult.getY(), hitResult.getZ());

		player.processNextBlockBreak = false;
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(TYPE);
	}

	@Override
	public void postLoad()
	{/*
		if (f.isFile())
		{
			SSS sss = new SSS(f);
			slabType = switch (sss.getString("type"))
				{
					case "top" -> EnumSlabType.TOP;
					case "bottom" -> EnumSlabType.BOTTOM;
					default -> throw new IllegalStateException("Unexpected value: " + sss.getString("type"));
				};
		}
		f = null;*/
	}
}
