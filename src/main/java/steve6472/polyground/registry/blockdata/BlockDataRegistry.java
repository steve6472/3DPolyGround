package steve6472.polyground.registry.blockdata;

import steve6472.polyground.block.blockdata.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class BlockDataRegistry
{
	private static final HashMap<String, BlockDataEntry<? extends BlockData>> dataRegistry = new HashMap<>();

	public static final BlockDataEntry<KnappingData> stoneKnapping = register("knapping", KnappingData::new);
	public static final BlockDataEntry<ChiselBlockData> chisel = register("chisel", ChiselBlockData::new);
	public static final BlockDataEntry<PaintBucketData> paintBucket = register("paint_bucket", PaintBucketData::new);
	public static final BlockDataEntry<RootBlockData> root = register("root", RootBlockData::new);
	public static final BlockDataEntry<LiqExtractorData> liqExtractor = register("liq_extractor", LiqExtractorData::new);
	public static final BlockDataEntry<PipeData> pipeData = register("pipe", PipeData::new);
	public static final BlockDataEntry<AmethineCoreData> amethineCore = register("amethine_core", AmethineCoreData::new);
	public static final BlockDataEntry<DoorData> door = register("door", DoorData::new);

	public static <T extends BlockData> BlockDataEntry<T> register(String id, IBlockDataFactory<T> factory)
	{
		if (dataRegistry.containsKey(id))
			throw new IllegalArgumentException("Duplicate id '" + id + "' !");

		BlockDataEntry<T> entry = new BlockDataEntry<>(factory, id);
		dataRegistry.put(id, entry);
		return entry;
	}

	public static BlockData createData(String id)
	{
		return dataRegistry.get(id).createNew();
	}

	public static Collection<BlockDataEntry<? extends BlockData>> getEntries()
	{
		return dataRegistry.values();
	}

	public static Set<String> getKeys()
	{
		return dataRegistry.keySet();
	}
}
