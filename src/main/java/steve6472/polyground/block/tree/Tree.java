package steve6472.polyground.block.tree;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.special.BranchBlock;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.RandomUtil;

import java.util.*;

public class Tree
{
	private static final int MAX_TRUNK_SIZE = 40;

	private Node root;
	private HashSet<Node> nodes;
	private int totalSize, trunkSize;

	public Tree()
	{

	}

	public Node getRoot()
	{
		return root;
	}

	public HashSet<Node> getNodes()
	{
		return nodes;
	}

	public void analyze(World world, int x, int y, int z)
	{
		root = findTree(world, x, y, z);
		nodes.add(root);
		totalSize = calculateSize();
		trunkSize = calculateTrunkSize();
	}

	public void grow(World world)
	{
		if (trunkSize < MAX_TRUNK_SIZE)
		{
			growTrunk();
			trunkSize = calculateTrunkSize();
		}

		set(world);
	}

	private void set(World world)
	{
		Block branch = Blocks.getBlockByName("branch");

		for (Node n : nodes)
		{
			world.setState(branch.getDefaultState().with(BranchBlock.LEAVES, false).with(BranchBlock.RADIUS, n.radius).get(), n.x, n.y, n.z);
		}
	}

	private void growTrunk()
	{
		List<Node> trunk = getTrunkNodes();

		if (trunk.size() == 1)
		{
			if (RandomUtil.flipACoin())
			{
				nodes.clear();
				nodes.add(new Node(trunk.get(0).x, trunk.get(0).y, trunk.get(0).z, 1));
				nodes.add(new Node(trunk.get(0).x, trunk.get(0).y + 1, trunk.get(0).z, 0));
			}
			return;
		}

		for (int i = 0, size = trunk.size(); i < size; i++)
		{
			Node n = trunk.get(i);
			if (n.radius < 7)
			{
				// decide if it should increse radius
				if (RandomUtil.decide(3))
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
							nodes.add(new Node(n.x, n.y + 1, n.z, 0));
						}
					}

					if (RandomUtil.decide(2))
					{
						nodes.remove(n);
						nodes.add(new Node(n.x, n.y, n.z, n.radius + 1));
					}
				}
			}
		}
	}

	private List<Node> getTrunkNodes()
	{
		List<Node> trunkNodes = new ArrayList<>();
		for (Node n : nodes)
		{
			if (n.x == root.x && n.z == root.z)
				trunkNodes.add(n);
		}
		trunkNodes.sort(Comparator.comparingInt(a -> a.y));
		return trunkNodes;
	}

	private int calculateSize()
	{
		int i = 0;
		for (Node n : nodes)
		{
			i += n.radius + 1;
		}
		return i;
	}

	private int calculateTrunkSize()
	{
		int i = 0;
		for (Node n : nodes)
		{
			if (n.x == root.x && n.z == root.z)
				i += n.radius + 1;
		}
		return i;
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
		HashSet<Node> checkedPositions = new HashSet<>();
		nodes = checkedPositions;
		checkedPositions.add(new Node(x, y, z, world.getState(x, y, z).get(BranchBlock.RADIUS)));
		int maxRadius = world.getState(x, y, z).get(BranchBlock.RADIUS);

		List<Node> newNodes = new ArrayList<>();

		int iterations = 0;

		while (iterations < 32)
		{
			for (Node node : checkedPositions)
			{
				for (EnumFace face : EnumFace.getFaces())
				{
					int cx = node.x + face.getXOffset();
					int cy = node.y + face.getYOffset();
					int cz = node.z + face.getZOffset();

					if (world.getBlock(cx, cy, cz) instanceof BranchBlock)
					{
						int r = world.getState(cx, cy, cz).get(BranchBlock.RADIUS);
						Node pos = new Node(cx, cy, cz, r);

						// Do not test already existing branches
						if (checkedPositions.contains(pos))
							continue;

						maxRadius = Math.max(maxRadius, r);
						newNodes.add(pos);
					}
				}
			}

			if (newNodes.isEmpty())
				break;

			checkedPositions.addAll(newNodes);
			newNodes.clear();

			iterations++;
		}

		List<Node> biggestNodes = new ArrayList<>();
		for (Node node : checkedPositions)
		{
			if (node.radius == maxRadius)
			{
				biggestNodes.add(node);
			}
		}

		Node lowestNode = biggestNodes.get(0);

		for (int i = 1; i < biggestNodes.size(); i++)
		{
			Node node = biggestNodes.get(i);
			if (node.y < lowestNode.y)
				lowestNode = node;
		}

		return lowestNode;
	}

	public Node findRoot(World world, int x, int y, int z)
	{
		HashSet<Node> checkedPositions = new HashSet<>();
		checkedPositions.add(new Node(x, y, z, world.getState(x, y, z).get(BranchBlock.RADIUS)));
		int maxRadius = world.getState(x, y, z).get(BranchBlock.RADIUS);

		List<Node> newNodes = new ArrayList<>();

		int iterations = 0;

		while (iterations < 32)
		{
			for (Node node : checkedPositions)
			{
				for (EnumFace face : EnumFace.getFaces())
				{
					int cx = node.x + face.getXOffset();
					int cy = node.y + face.getYOffset();
					int cz = node.z + face.getZOffset();

					if (world.getBlock(cx, cy, cz) instanceof BranchBlock)
					{
						int r = world.getState(cx, cy, cz).get(BranchBlock.RADIUS);

						// Branch can only grow, this node needs to be smaller or have the same size as the one before it
						if (node.radius > r)
							continue;

						Node pos = new Node(cx, cy, cz, r);

						// Do not test already existing branches
						if (checkedPositions.contains(pos))
							continue;

						maxRadius = Math.max(maxRadius, r);

						// Found radius 7, only possible in trunk
						if (maxRadius == 7)
						{
							int ly = pos.y;
							// Find lowest block
							while (ly > 0)
							{
								if (!(world.getBlock(x, ly, z) instanceof BranchBlock))
								{
									return pos;
								} else
								{
									ly--;
								}
							}
						}

						newNodes.add(pos);
					}
				}
			}

			if (newNodes.isEmpty())
				break;

			checkedPositions.addAll(newNodes);
			newNodes.clear();

			iterations++;
		}

		List<Node> biggestNodes = new ArrayList<>();
		for (Node node : checkedPositions)
		{
			if (node.radius == maxRadius)
			{
				biggestNodes.add(node);
			}
		}

		Node lowestNode = biggestNodes.get(0);

		for (int i = 1; i < biggestNodes.size(); i++)
		{
			Node node = biggestNodes.get(i);
			if (node.y < lowestNode.y)
				lowestNode = node;
		}

		return lowestNode;
	}

	public record Node(int x, int y, int z, int radius)
	{
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

		@Override
		public int hashCode()
		{
			return Objects.hash(x, y, z);
		}
	}
}