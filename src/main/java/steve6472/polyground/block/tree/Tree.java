package steve6472.polyground.block.tree;

import org.joml.Math;
import org.joml.Vector3d;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.RootBlockData;
import steve6472.polyground.block.special.BranchBlock;
import steve6472.polyground.block.special.LeavesBlock;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.Pair;
import steve6472.sge.main.util.RandomUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Tree
{
	private static int maxTrunkSize = 40;
	private static int branchGrowthTrunkThreshold = 5;
	private static int maxBranchCount = 5;
	private static int minBranchHeight = 3;
	private static int maxBranchRadius = 2;

	private Node root;
	private Array3D<Integer> nodes;
	private List<Branch> branches;
	private int totalSize, trunkSize, trunkHeight;
	private long seed;
	private RootBlockData data;

	public Tree()
	{
		seed = System.nanoTime();
	}

	public Tree(long seed)
	{
		this.seed = seed;
	}

	public Node getRoot()
	{
		return root;
	}

	public Array3D<Integer> getNodes()
	{
		return nodes;
	}

	public List<Branch> getBranches()
	{
		return branches;
	}

	public void analyze(World world, int x, int y, int z)
	{
		root = findTree(world, x, y, z);

		BlockData data = world.getData(root.x, root.y - 1, root.z);
		if (data == null)
			return;

		this.data = (RootBlockData) data;

		seed = Objects.hash(root.x, root.y, root.z);
		nodes.put(root.toBlockPos(), root.radius);
		totalSize = calculateSize(nodes);
		calculateTrunkSize();
		branches = findBranches();

		maxTrunkSize = this.data.maxTrunkSize;
		branchGrowthTrunkThreshold = this.data.branchGrowthTrunkThreshold;
		maxBranchCount = this.data.maxBranchCount;
		minBranchHeight = this.data.minBranchHeight;
		maxBranchRadius = this.data.maxBranchRadius;
	}

	public void grow(World world)
	{
		if (data == null)
			return;

		// Decide what should grow
		if (RandomUtil.flipACoin())
		{
			if (trunkSize < maxTrunkSize && !RandomUtil.decide(10))
			{
				growTrunk();
				calculateTrunkSize();
			}
		} else
		{
			if (branches.size() < maxBranchCount)
			{
				if (!RandomUtil.decide(10))
					growNewBranch(world);
			}

			if (!RandomUtil.decide(10) && branches.size() > 0)
			{
				growBranch(world, branches.get(RandomUtil.randomInt(0, branches.size() - 1)));
			}
		}

		set(world);
	}

	private void growNewBranch(World world)
	{
		EnumFace f = EnumFace.getCardinal()[RandomUtil.randomInt(0, 3)];

		int x = root.x + f.getXOffset();
		int y = trunkHeight + root.y - 1;
		int z = root.z + f.getZOffset();

		long hash = hash(seed, x, y, z);

		int scale = 12;

		int ts = Math.max(1, scale - (int) (trunkSize * ((double) scale / (double) maxTrunkSize)));

		if ((hash % ts) == 0 && trunkHeight >= minBranchHeight)
		{
			if (countBranchesAroundTrunk(y) < (trunkHeight < 4 ? 1 : 2))
			{
				Node newBranch = new Node(x, y, z, 0);

				if (canBranchGrow(world, newBranch, EnumFace.NONE, true))
				{
					nodes.put(newBranch.toBlockPos(), newBranch.radius);
					Node n = new Node(newBranch.toBlockPos(), newBranch.radius);
					List<Node> nodes = new ArrayList<>();
					nodes.add(n);
					Branch branch = new Branch(n, nodes, RandomUtil.randomInt(4, 8), RandomUtil.randomInt(3, 5));
					data.addBranch(branch.start().x, branch.start().y, branch.start().z, branch.maxSize(), branch.maxCount());
					branches.add(branch);
				}
			}
		}
	}

	private long hash(long seed, int x, int y, int z)
	{
		long h = seed + x * 668265263L + y * 2147483647L + z * 374761393L;
		h = (h ^ (h >> 14)) * 1274126177L;
		return h ^ (h >> 16);
	}

	private void set(World world)
	{
		Block branchBlock = Blocks.getBlockByName("branch");

		nodes.forEach((pos, radius) ->
			world.setState(branchBlock.getDefaultState().with(BranchBlock.LEAVES, radius == 0 && !pos.equals(root.x, root.y, root.z)).with(BranchBlock.RADIUS, radius).get(), pos.getX(), pos.getY(), pos.getZ()));

		leavesBlob(world, root.toBlockPos().up(trunkHeight), trunkSize / (maxTrunkSize / 3.5));

		for (Branch branch : branches)
		{
			Node lastNode = branch.nodes().get(branch.nodes().size() - 1);
			leavesBlob(world, lastNode.toBlockPos(), 1.2);

			if (branch.nodes().size() > 1)
			{
				int r = RandomUtil.randomInt(0, branch.nodes().size() - 2);
				Node n = branch.nodes().get(r);
				leavesBlob(world, n.toBlockPos(), Math.min(n.radius(), 1) + 0.5);
			}
		}
	}

	private void leavesBlob(World world, BlockPos pos, double radius)
	{
		radius = Math.min(radius, 3.2);
		int r = (int) Math.ceil(radius) + 1;

		Block leaves = Blocks.getBlockByName("oak_leaves");

		for (int x = -r; x < r; x++)
		{
			for (int y = -1; y < r; y++)
			{
				for (int z = -r; z < r; z++)
				{
					double dist = Vector3d.distance(0, 0, 0, x, y, z);
					if (dist <= radius)
					{
						if (world.getBlock(pos.getX() + x, pos.getY() + y, pos.getZ() + z) == Block.AIR)
							world.setBlock(leaves, pos.getX() + x, pos.getY() + y, pos.getZ() + z);
					}
				}
			}
		}
	}

	private void growBranch(World world, Branch branch)
	{
		Node start = branch.start();
		List<Node> nodes = branch.nodes();

		int branchSize = 0;
		for (Node n1 : branch.nodes())
		{
			branchSize += n1.radius + 1;
		}

		if (branchSize >= branch.maxSize())
			return;

		if (trunkSize < branchGrowthTrunkThreshold)
			return;

		EnumFace f = EnumFace.getValues()[RandomUtil.randomInt(0, 5)];

		if (f == EnumFace.DOWN)
			return;

		// The first branch can NOT grow up, it could connect to trunk
		if (branchSize == 1 && f == EnumFace.UP)
			return;

		int x = root.x + f.getXOffset();
		int y = trunkHeight + root.y - 1;
		int z = root.z + f.getZOffset();

		long hash = hash(seed, x, y, z);

		int scale = 6;

		int ts = Math.max(1, scale - (int) (trunkSize * ((double) scale / (double) maxTrunkSize)));

		if (nodes.size() < branch.maxCount() && (branchSize == 1 || (hash % ts) == 0) && RandomUtil.flipACoin())
		{
			Node from;

			if (branchSize == 1)
				from = start;
			else
				from = nodes.get(nodes.size() - 1);

			growBranchNext(world, f, from, branch);
		}
		else if (RandomUtil.decide(2))
		{
			int r = RandomUtil.randomInt(0, nodes.size() - 1);
			Node n = nodes.get(r);

			if (n.radius >= maxBranchRadius)
				return;

			// Test if trunk is smaller
			if (n == start)
			{
				Integer tr = this.nodes.get(new BlockPos(root.x, n.y, root.z));
				if (tr != null && tr <= n.radius)
					return;
			}

			// Test if previous branch is smaller
			if (r > 0)
			{
				Node prev = nodes.get(r - 1);
				if (prev.radius <= n.radius)
				{
					return;
				}
			}

			// Test if next branch is not bigger
			if (r + 1 < nodes.size())
			{
				Node next = nodes.get(r + 1);

				if (n.radius > next.radius)
				{
					return;
				}
			}

			// If the selected branch is last, it can not grow
			if (n == nodes.get(nodes.size() - 1))
				return;

			Node node = new Node(n.x, n.y, n.z, n.radius + 1);
			nodes.set(r, node);
			this.nodes.put(n.toBlockPos(), n.radius + 1);
		}
	}

	private void growBranchNext(World world, EnumFace f, Node from, Branch branch)
	{
		Node n = new Node(from.x + f.getXOffset(), from.y + f.getYOffset(), from.z + f.getZOffset(), 0);
		if (canBranchGrow(world, n, f, false))
		{
			branch.nodes().add(n);
			nodes.put(n.toBlockPos(), n.radius);
		}
	}

	private int countBranchesAroundTrunk(int y)
	{
		int c = 0;
		for (EnumFace f : EnumFace.getCardinal())
		{
			int x = root.x + f.getXOffset();
			int z = root.z + f.getZOffset();
			if (nodes.contains(new BlockPos(x, y, z)))
				c++;
		}
		return c;
	}

	private boolean canBranchGrow(World world, Node block, EnumFace from, boolean ignoreTrunk)
	{
		for (EnumFace f : EnumFace.getFaces())
		{
			if (f == from.getOpposite())
				continue;

			int x = block.x + f.getXOffset();
			int y = block.y + f.getYOffset();
			int z = block.z + f.getZOffset();

			if (ignoreTrunk && root.x == x && root.z == z)
				continue;

			if (nodes.contains(new BlockPos(x, y, z)))
				return false;

			Block b = world.getBlock(x, y, z);
			if (!(b instanceof LeavesBlock) && b != Block.AIR)
			{
				return false;
			}
		}

		return true;
	}

	private void growTrunk()
	{
		List<Node> trunk = getTrunkNodes();

		if (trunk.size() == 1)
		{
			if (!RandomUtil.decide(10))
			{
				nodes.clear();
				nodes.put(trunk.get(0).toBlockPos(), 1);
				nodes.put(trunk.get(0).toBlockPos().up(), 0);
			}
			return;
		}

		// Try to prevent bad trees
		if (trunkHeight < 5 && trunkSize >= 20)
		{
			nodes.put(root.toBlockPos().up(trunkHeight), 0);
		}

		for (int i = 0, size = trunk.size(); i < size; i++)
		{
			Node n = trunk.get(i);
			if (n.radius < 7)
			{
				// decide if it should increse radius
				if (RandomUtil.decide(2))
				{
					// Check if it can increse radius
					if (i > 0)
					{
						Node down = trunk.get(i - 1);

						// Trunk below is not bigger
						if (down.radius <= n.radius)
						{
							continue;
						}
					}

					if (i + 1 < trunk.size())
					{
						Node up = trunk.get(i + 1);

						if (n.radius > up.radius)
						{
							continue;
						}
					} else
					{
						// Increse chance to grow up if trunk has nothing above itself and is 3 or 4 radius
						int chance = 4;
						if (n.radius == 3 || n.radius == 4)
						{
							chance = 2;
						}

						if (RandomUtil.decide(chance))
						{
							nodes.put(n.toBlockPos().up(), 0);
						}
					}

					if (RandomUtil.decide(1))
					{
						nodes.put(n.toBlockPos(), n.radius + 1);
					}
				}
			}
		}
	}

	private List<Node> getTrunkNodes()
	{
		List<Node> trunkNodes = new ArrayList<>();

		for (Pair<BlockPos, Integer> node : nodes)
		{
			if (node.getA().getX() == root.x && node.getA().getZ() == root.z)
				trunkNodes.add(new Node(node.getA(), node.getB()));
		}
		trunkNodes.sort(Comparator.comparingInt(a -> a.y));
		return trunkNodes;
	}

	private int calculateSize(Array3D<Integer> nodes)
	{
		int i = 0;
		for (Pair<BlockPos, Integer> n : nodes)
		{
			i += n.getB() + 1;
		}
		return i;
	}

	private void calculateTrunkSize()
	{
		trunkHeight = 0;
		trunkSize = 0;

		for (Pair<BlockPos, Integer> node : nodes)
		{
			if (node.getA().getX() == root.x && node.getA().getZ() == root.z)
			{
				trunkSize += node.getB() + 1;
				trunkHeight++;
			}
		}
	}

	private List<Branch> findBranches()
	{
		List<Branch> branches = new ArrayList<>();
		List<Node> branchStarts = new ArrayList<>();

		for (Pair<BlockPos, Integer> node : nodes)
		{
			for (EnumFace f : EnumFace.getCardinal())
			{
				int x = node.getA().getX() + f.getXOffset();
				int z = node.getA().getZ() + f.getZOffset();

				// Block is next to trunk
				if (root.x == x && root.z == z)
				{
					branchStarts.add(new Node(node.getA(), node.getB()));
					break;
				}
			}
		}

		for (Node bs : branchStarts)
		{
			RootBlockData.BranchData branch = data.getBranch(bs.x, bs.y, bs.z);
			if (branch != null)
			{
				branches.add(new Branch(bs, findBranch(bs), branch.maxSize(), branch.maxBlockCount()));
			}
		}

		return branches;
	}

	private int getRadius(int x, int y, int z)
	{
		Integer r = nodes.get(new BlockPos(x, y, z));
		return r == null ? -1 : r;
	}

	private List<Node> findBranch(Node node)
	{
		List<Node> pos = new ArrayList<>();
		pos.add(node);
		
		List<Node> newNodes = new ArrayList<>();

		int iterations = 0;

		while (iterations < 8)
		{
			for (Node n : pos)
			{
				for (EnumFace f : EnumFace.getFaces())
				{
					Node newNode = new Node(n.x + f.getXOffset(), n.y + f.getYOffset(), n.z + f.getZOffset(), getRadius(n.x + f.getXOffset(), n.y + f.getYOffset(), n.z + f.getZOffset()));

					// Do NOT got back to trunk!
					if (newNode.x == root.x && newNode.z == root.z)
						continue;

					// Check if node exists and is not already listed
					if (nodes.contains(newNode.toBlockPos()) && !pos.contains(newNode))
					{
						newNodes.add(newNode);
					}
				}
			}

			if (newNodes.isEmpty())
				break;

			pos.addAll(newNodes);
			newNodes.clear();

			iterations++;
		}

		return pos;
	}

	/**
	 *
	 * @param world world
	 * @param x x
	 * @param y y
	 * @param z z
	 * @return Root (lowest, biggest branch block)
	 */
	public Node findTree(World world, int x, int y, int z)
	{
		nodes = new Array3D<>();
		nodes.put(new BlockPos(x, y, z), world.getState(x, y, z).get(BranchBlock.RADIUS));
		int maxRadius = world.getState(x, y, z).get(BranchBlock.RADIUS);

		List<Node> newNodes = new ArrayList<>();

		int iterations = 0;

		while (iterations < 32)
		{
			for (Pair<BlockPos, Integer> pair : nodes)
			{
				BlockPos node = pair.getA();

				for (EnumFace face : EnumFace.getFaces())
				{
					BlockPos pos = new BlockPos(node).offset(face);

					if (world.getBlock(pos.getX(), pos.getY(), pos.getZ()) instanceof BranchBlock)
					{
						int r = world.getState(pos.getX(), pos.getY(), pos.getZ()).get(BranchBlock.RADIUS);

						// Do not test already existing branches
						if (nodes.contains(pos))
							continue;

						maxRadius = Math.max(maxRadius, r);
						newNodes.add(new Node(pos, r));
					}
				}
			}

			if (newNodes.isEmpty())
				break;

			for (Node node : newNodes)
			{
				nodes.put(node.toBlockPos(), node.radius);
			}
			newNodes.clear();

			iterations++;
		}

		List<BlockPos> biggestNodes = new ArrayList<>();
		final int finalMaxRadius = maxRadius;

		nodes.forEach((pos, rad) -> {
			if (rad == finalMaxRadius)
				biggestNodes.add(pos);
		});

		BlockPos lowestNode = biggestNodes.get(0);

		for (int i = 1; i < biggestNodes.size(); i++)
		{
			BlockPos node = biggestNodes.get(i);
			if (node.getY() < lowestNode.getY())
				lowestNode = node;
		}

		return new Node(lowestNode, maxRadius);
	}

	public record Node(int x, int y, int z, int radius)
	{
		public Node(BlockPos pos, int radius)
		{
			this(pos.getX(), pos.getY(), pos.getZ(), radius);
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Node position = (Node) o;
			return x == position.x && y == position.y && z == position.z;
		}

		public BlockPos toBlockPos()
		{
			return new BlockPos(x, y, z);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(x, y, z);
		}
	}
}