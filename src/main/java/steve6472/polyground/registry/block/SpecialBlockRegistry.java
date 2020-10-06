package steve6472.polyground.registry.block;

import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.special.*;

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

	public static final SpecialBlockEntry<PillarBlock> pilliar = register("pilliar", PillarBlock::new);
	public static final SpecialBlockEntry<TransparentBlock> transparentBlock = register("transparent", TransparentBlock::new);
	public static final SpecialBlockEntry<SlabBlock> slab = register("slab", SlabBlock::new);
	public static final SpecialBlockEntry<StalaBlock> stalaBlock = register("stala", StalaBlock::new);
	public static final SpecialBlockEntry<StairBlock> stairs = register("stairs", StairBlock::new);
	public static final SpecialBlockEntry<LeavesBlock> leaves = register("leaves", LeavesBlock::new);
	public static final SpecialBlockEntry<GravelBlock> gravel = register("gravel", GravelBlock::new);
	public static final SpecialBlockEntry<CustomBlock> custom = register("custom", CustomBlock::new);
	public static final SpecialBlockEntry<LightSourceBlock> light = register("light", LightSourceBlock::new);
	public static final SpecialBlockEntry<TorchBlock> torch = register("torch", TorchBlock::new);
	public static final SpecialBlockEntry<WaterGeneratorBlock> waterGenerator = register("watergenerator", WaterGeneratorBlock::new);
	public static final SpecialBlockEntry<StateTest> stateTest = register("state_test", StateTest::new);
	public static final SpecialBlockEntry<SnowLayerBlock> snowLayer = register("snow_layer", SnowLayerBlock::new);
	public static final SpecialBlockEntry<DoubleBlock> doubleBlock = register("double_block", DoubleBlock::new);
	public static final SpecialBlockEntry<TagBelowBlock> tagBelowBlock = register("tag_below", TagBelowBlock::new);
	public static final SpecialBlockEntry<SpreadBlock> spreadBlock = register("spreadable", SpreadBlock::new);
	public static final SpecialBlockEntry<CorruptedStoneBlock> corruptedStone = register("corrupted_stone", CorruptedStoneBlock::new);
	public static final SpecialBlockEntry<LogStackBlock> logStack = register("log_stack", LogStackBlock::new);
	public static final SpecialBlockEntry<DirectionalLightSourceBlock> dirLight = register("dir_light", DirectionalLightSourceBlock::new);
	public static final SpecialBlockEntry<DirectionalBlock> directional = register("directional", DirectionalBlock::new);
	public static final SpecialBlockEntry<FourDirectionalBlock> fourDirectional = register("four_directional", FourDirectionalBlock::new);
	public static final SpecialBlockEntry<ConveyorBeltBlock> conveyorBelt = register("conveyor_belt", ConveyorBeltBlock::new);
	public static final SpecialBlockEntry<FallingBlock> fallingBlock = register("falling_block", FallingBlock::new);
	public static final SpecialBlockEntry<KnappingBlock> stoneKnapping = register("stone_knapping", KnappingBlock::new);
	public static final SpecialBlockEntry<HammerstoneBlock> hammerstone = register("hammerstone", HammerstoneBlock::new);
	public static final SpecialBlockEntry<ChiselBlock> chisel = register("chisel", ChiselBlock::new);
	public static final SpecialBlockEntry<FlowerBlock> flower = register("flower", FlowerBlock::new);

	public static <T extends Block> SpecialBlockEntry<T> register(String id, ISpecialBlockFactory<T> factory)
	{
		SpecialBlockEntry<T> entry = new SpecialBlockEntry<>(factory);
		specialBlockRegistry.put(id, entry);
		return entry;
	}

	public static Block createSpecialBlock(String id, JSONObject json)
	{
		return specialBlockRegistry.get(id).createNew(json);
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
