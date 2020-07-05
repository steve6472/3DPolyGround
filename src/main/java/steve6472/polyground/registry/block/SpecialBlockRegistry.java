package steve6472.polyground.registry.block;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.special.*;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class SpecialBlockRegistry
{
	private static final HashMap<String, SpecialBlockEntry<? extends Block>> specialBlockRegistry = new HashMap<>();

	public static final SpecialBlockEntry<PilliarBlock> pilliar = register("pilliar", PilliarBlock::new);
	public static final SpecialBlockEntry<TransparentBlock> transparentBlock = register("transparent", TransparentBlock::new);
	public static final SpecialBlockEntry<DropBlock> drop = register("drop", DropBlock::new);
	public static final SpecialBlockEntry<SlabBlock> slab = register("slab", SlabBlock::new);
	public static final SpecialBlockEntry<StalaBlock> stalaBlock = register("stala", StalaBlock::new);
	public static final SpecialBlockEntry<WorldButtonBlock> worldButton = register("world_button", WorldButtonBlock::new);
	public static final SpecialBlockEntry<SnapButtonBlock> snapButton = register("snap_button", SnapButtonBlock::new);
	public static final SpecialBlockEntry<StairBlock> stairs = register("stairs", StairBlock::new);
	public static final SpecialBlockEntry<LeavesBlock> leaves = register("leaves", LeavesBlock::new);
	public static final SpecialBlockEntry<GravelBlock> gravel = register("gravel", GravelBlock::new);
	public static final SpecialBlockEntry<DoubleSlabBlockTinted> doubleSlabTinted = register("double_slab_tinted", DoubleSlabBlockTinted::new);
	public static final SpecialBlockEntry<TimeSliderBlock> timeSlider = register("time_slider", TimeSliderBlock::new);
	public static final SpecialBlockEntry<CustomBlock> custom = register("custom", CustomBlock::new);
	public static final SpecialBlockEntry<LightSourceBlock> light = register("light", LightSourceBlock::new);
	public static final SpecialBlockEntry<TorchBlock> torch = register("torch", TorchBlock::new);
	public static final SpecialBlockEntry<MicroBlock> microBlock = register("micro_block", MicroBlock::new);
	public static final SpecialBlockEntry<WaterGeneratorBlock> waterGenerator = register("watergenerator", WaterGeneratorBlock::new);
	public static final SpecialBlockEntry<StateTest> stateTest = register("state_test", StateTest::new);

	public static <T extends Block> SpecialBlockEntry<T> register(String id, ISpecialBlockFactory<T> factory)
	{
		SpecialBlockEntry<T> entry = new SpecialBlockEntry<>(factory);
		specialBlockRegistry.put(id, entry);
		return entry;
	}

	public static Block createSpecialBlock(String id, File f, int blockId)
	{
		return specialBlockRegistry.get(id).createNew(f, blockId);
	}

	public static Collection<SpecialBlockEntry<? extends Block>> getEntries()
	{
		return specialBlockRegistry.values();
	}

	public static Set<String> getKeys()
	{
		return specialBlockRegistry.keySet();
	}
}
