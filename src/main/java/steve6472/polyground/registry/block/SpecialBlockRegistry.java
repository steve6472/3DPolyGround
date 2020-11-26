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

	static
	{
		register("pilliar", PillarBlock::new);
		register("transparent", TransparentBlock::new);
		register("slab", SlabBlock::new);
		register("stala", StalaBlock::new);
		register("stairs", StairBlock::new);
		register("leaves", LeavesBlock::new);
		register("gravel", GravelBlock::new);
		register("custom", CustomBlock::new);
		register("light", LightSourceBlock::new);
		register("torch", TorchBlock::new);
		register("watergenerator", WaterGeneratorBlock::new);
		register("state_test", StateTest::new);
		register("snow_layer", SnowLayerBlock::new);
		register("double_block", DoubleBlock::new);
		register("tag_below", TagBelowBlock::new);
		register("spreadable", SpreadBlock::new);
		register("corrupted_stone", CorruptedStoneBlock::new);
		register("log_stack", LogStackBlock::new);
		register("dir_light", DirectionalLightSourceBlock::new);
		register("directional", DirectionalBlock::new);
		register("four_directional", FourDirectionalBlock::new);
		register("conveyor_belt", ConveyorBeltBlock::new);
		register("falling_block", FallingBlock::new);
		register("flint_knapping", KnappingBlock::new);
		register("hammerstone", HammerstoneBlock::new);
		register("chisel", ChiselBlock::new);
		register("flower", FlowerBlock::new);
		register("paint_bucket", PaintBucketBlock::new);
		register("axe_block", AxeBlock::new);
		register("hemp", HempBlock::new);
		register("branch", BranchBlock::new);
		register("sapling", SaplingBlock::new);
		register("root", RootBlock::new);
		register("liq_extractor", LiqExtractorBlock::new);
		register("pipe", PipeBlock::new);
		register("breakable", BreakableBlock::new);
		register("flint", FlintBlock::new);
		register("amethine_core", AmethineCoreBlock::new);
		register("door", DoorBlock::new);
		register("logic", LogicBlock::new);
	}

	public static <T extends Block> void register(String id, ISpecialBlockFactory<T> factory)
	{
		SpecialBlockEntry<T> entry = new SpecialBlockEntry<>(factory);
		specialBlockRegistry.put(id, entry);
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
