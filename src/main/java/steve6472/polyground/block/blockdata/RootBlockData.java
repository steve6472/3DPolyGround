package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import steve6472.polyground.block.properties.enums.EnumTreeType;
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
	public int maxTrunkWidth;
	public int maxTrunkHeight;
	public int branchGrowthTrunkThreshold;
	public int maxBranchCount;
	public int minBranchHeight;
	public int maxBranchRadius;
	public int maxBranchAroundBlock;
	public int branchMaxBlockCount, branchMinBlockCount;
	public int branchMaxSize, branchMinSize;
	public double leavesBlobMultiplier;
	public double branchLeavesBlobMultiplier;
	public boolean isGiant;
	public EnumTreeType treeType;
	List<BranchData> branches;

	public record BranchData(int x, int y, int z, int maxSize, int maxBlockCount) {}

	public RootBlockData()
	{
		branches = new ArrayList<>();
	}

	public RootBlockData(EnumTreeType treeType)
	{
		this.treeType = treeType;
		branches = new ArrayList<>();

		switch (treeType)
		{
			case OAK -> {
				if (RandomUtil.decide(800))
				{
					isGiant = true;

					this.maxTrunkSize = RandomUtil.randomInt(80, 100);
					this.maxTrunkWidth = 15;
					this.maxTrunkHeight = RandomUtil.randomInt(20, 32);

					this.branchGrowthTrunkThreshold = RandomUtil.randomInt(8, 13);
					this.maxBranchCount = 14;
					this.minBranchHeight = RandomUtil.randomInt(6, 8);
					this.maxBranchRadius = 5;
					this.maxBranchAroundBlock = 3;

					this.leavesBlobMultiplier = 4;
					this.branchLeavesBlobMultiplier = 3;

					this.branchMinSize = 40;
					this.branchMaxSize = 60;
					this.branchMinBlockCount = 20;
					this.branchMaxBlockCount = 30;
				} else
				{
					this.maxTrunkSize = RandomUtil.randomInt(20, 45);
					this.maxTrunkWidth = 7;
					this.maxTrunkHeight = 16;
					this.branchGrowthTrunkThreshold = RandomUtil.randomInt(4, 6);
					this.maxBranchCount = maxTrunkSize / 8;
					this.minBranchHeight = RandomUtil.randomInt(3, 4);
					this.maxBranchRadius = 2;
					this.maxBranchAroundBlock = 2;
					this.branchMinSize = 4;
					this.branchMaxSize = 8;
					this.branchMinBlockCount = 3;
					this.branchMaxBlockCount = 5;
					this.leavesBlobMultiplier = 1;
					this.branchLeavesBlobMultiplier = 1;
				}
			}

			case ACACIA -> {
				this.maxTrunkSize = RandomUtil.randomInt(20, 25);
				this.maxTrunkWidth = 2;
				this.maxTrunkHeight = 8;
				this.branchGrowthTrunkThreshold = RandomUtil.randomInt(10, 12);
				this.maxBranchCount = 3;
				this.minBranchHeight = RandomUtil.randomInt(5, 6);
				this.maxBranchRadius = 1;
				this.maxBranchAroundBlock = 4;
				this.branchMinSize = 4;
				this.branchMaxSize = 8;
				this.branchMinBlockCount = 3;
				this.branchMaxBlockCount = 4;
				this.leavesBlobMultiplier = 0.45;
				this.branchLeavesBlobMultiplier = 1;
			}
		}
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
		tag.putInt("maxTrunkWidth", maxTrunkWidth);
		tag.putInt("maxTrunkHeight", maxTrunkHeight);
		tag.putInt("branchGrowthTrunkThreshold", branchGrowthTrunkThreshold);
		tag.putInt("maxBranchCount", maxBranchCount);
		tag.putInt("minBranchHeight", minBranchHeight);
		tag.putInt("maxBranchRadius", maxBranchRadius);
		tag.putInt("maxBranchAroundBlock", maxBranchAroundBlock);
		tag.putDouble("leavesBlobMultiplier", leavesBlobMultiplier);
		tag.putDouble("branchLeavesBlobMultiplier", branchLeavesBlobMultiplier);
		tag.putInt("branchMinSize", branchMinSize);
		tag.putInt("branchMaxSize", branchMaxSize);
		tag.putInt("branchMinBlockCount", branchMinBlockCount);
		tag.putInt("branchMaxBlockCount", branchMaxBlockCount);

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
		tag.putString("treeType", treeType.toString());

		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		maxTrunkSize = tag.getInt("maxTrunkSize");
		maxTrunkWidth = tag.getInt("maxTrunkWidth");
		maxTrunkHeight = tag.getInt("maxTrunkHeight");
		branchGrowthTrunkThreshold = tag.getInt("branchGrowthTrunkThreshold");
		maxBranchCount = tag.getInt("maxBranchCount");
		minBranchHeight = tag.getInt("minBranchHeight");
		maxBranchRadius = tag.getInt("maxBranchRadius");
		treeType = EnumTreeType.valueOf(tag.getString("treeType"));
		maxBranchAroundBlock = tag.getInt("maxBranchAroundBlock");
		leavesBlobMultiplier = tag.getDouble("leavesBlobMultiplier");
		branchLeavesBlobMultiplier = tag.getDouble("branchLeavesBlobMultiplier");
		branchMinSize = tag.getInt("branchMinSize");
		branchMaxSize = tag.getInt("branchMaxSize");
		branchMinBlockCount = tag.getInt("branchMinBlockCount");
		branchMaxBlockCount = tag.getInt("branchMaxBlockCount");


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
