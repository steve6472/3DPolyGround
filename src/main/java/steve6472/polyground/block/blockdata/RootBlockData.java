package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;
import steve6472.sge.main.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.10.2020
 * Project: CaveGame
 *
 ***********************/
public class RootBlockData extends BlockData
{
	public int maxTrunkSize;
	public int branchGrowthTrunkThreshold;
	public int maxBranchCount;
	public int minBranchHeight;
	public int maxBranchRadius;
	List<BranchData> branches;

	public record BranchData(int x, int y, int z, int maxSize, int maxBlockCount) {}

	public RootBlockData()
	{
		branches = new ArrayList<>();
		this.maxTrunkSize = RandomUtil.randomInt(20, 45);
		this.branchGrowthTrunkThreshold = RandomUtil.randomInt(4, 6);
		this.maxBranchCount = maxTrunkSize / 8;
		this.minBranchHeight = RandomUtil.randomInt(3, 4);
		this.maxBranchRadius = 2;
	}

	public void addBranch(int x, int y, int z, int maxSize, int maxBlockCount)
	{
		branches.add(new BranchData(x, y, z, maxSize, maxBlockCount));
	}

	public BranchData getBranch(int x, int y, int z)
	{
		for (BranchData d : branches)
		{
			if (d.x == x && d.y == y && d.z == z)
				return d;
		}
		return null;
	}

	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("maxTrunkSize", maxTrunkSize);
		tag.putInt("branchGrowthTrunkThreshold", branchGrowthTrunkThreshold);
		tag.putInt("maxBranchCount", maxBranchCount);
		tag.putInt("minBranchHeight", minBranchHeight);
		tag.putInt("maxBranchRadius", maxBranchRadius);

		ListTag<CompoundTag> branches = new ListTag<>(CompoundTag.class);
		for (BranchData b : this.branches)
		{
			CompoundTag branchTag = new CompoundTag();
			branchTag.putInt("x", b.x);
			branchTag.putInt("y", b.y);
			branchTag.putInt("z", b.z);
			branchTag.putInt("maxSize", b.maxSize);
			branchTag.putInt("maxBlockCount", b.maxBlockCount);
			branches.add(branchTag);
		}

		tag.put("branches", branches);

		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		maxTrunkSize = tag.getInt("maxTrunkSize");
		branchGrowthTrunkThreshold = tag.getInt("branchGrowthTrunkThreshold");
		maxBranchCount = tag.getInt("maxBranchCount");
		minBranchHeight = tag.getInt("minBranchHeight");
		maxBranchRadius = tag.getInt("maxBranchRadius");

		ListTag<CompoundTag> branches = (ListTag<CompoundTag>) tag.getListTag("branches");
		for (CompoundTag branch : branches)
		{
			addBranch(branch.getInt("x"), branch.getInt("y"), branch.getInt("z"), branch.getInt("maxSize"), branch.getInt("maxBlockCount"));
		}
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.root.id();
	}
}
