package steve6472.polyground.entity;

import org.joml.Vector2i;
import org.joml.Vector3f;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.RandomUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.09.2019
 * Project: SJP
 *
 ***********************/
public class AIEntity extends EntityBase
{
	EntityHitbox hitbox;
	private boolean isDead = false;

	private List<Node> nodes;
	private List<Node> obstacleNodes;
	private HashMap<Block, BlockData> blockData;

	private Interpolator3D move;
	private boolean foundWithObstacles = false;
	int sx, sy, sz;

	public AIEntity()
	{
		addPosition(0, 0.5f, 0);
//		hitbox = new EntityHitbox(0.4999f, 0.4999f, 0.4999f);
		nodes = new ArrayList<>();
		obstacleNodes = new ArrayList<>();
		blockData = new HashMap<>();

		for (Block b : BlockRegistry.getAllBlocks())
		{
			blockData.put(b, new BlockData(b == Block.air ? 1.0f : 0.0f, b == Block.air));
		}

		move = new Interpolator3D();
		move.set(getX(), getY(), getZ());
	}

	public float tick;
	private byte moveTick;
	private int moveTime = 5;
	private int moveIteration = 1;

	@Override
	public void tick()
	{
		if (getY() < -16)
			isDead = true;

		tick += 1f / 60f;

		if (nodes.isEmpty())
		{
			obstacleNodes.clear();
			randomDest();
		}

		for (int i = 0; i < moveIteration; i++)
		{
			moveTick++;
			if (moveTick == moveTime)
			{
				moveToNextNode();
				moveTick = 0;
			}
		}
		setPosition(move.getX(), move.getY(), move.getZ());

		render();
	}

	private void randomDest()
	{
		int x = RandomUtil.randomInt(-16, 31);
		int z = RandomUtil.randomInt(-16, 31);
		if (!blockData.get(CaveGame.getInstance().world.getBlock(x, 1, z)).canPass)
		{
			randomDest();
		} else
		{
			calcPath(new Node(x, 1, z));
		}
	}

	private void moveToNextNode()
	{
		if (nodes.isEmpty())
			return;

		Node next;
		if (nodes.size() == 1)
		{
			next = nodes.get(0);
			nodes.clear();
			if (foundWithObstacles)
			{
				obstacleNodes.clear();
			}
			if (!obstacleNodes.isEmpty())
			{
				setPosition(sx + 0.5f, sy + 0.5f, sz + 0.5f);
				calcPath(next);
				return;
			}
		} else
		{
			next = nodes.get(1);
			nodes.remove(1);
		}

		move.set(next.x + 0.5f, next.y + 0.5f, next.z + 0.5f, (long) Math.floor(1000d / 60d * (double) moveTime));
	}

	public void calcPath(Node dest)
	{
		if (nodes.size() != 0)
			return;

		World world = CaveGame.getInstance().world;

		// Destination node is first
		nodes.add(dest);

		int cx = (int) Math.floor(getX());
		int cz = (int) Math.floor(getZ());

		sx = cx;
		sy = 1;
		sz = cz;

		for (int f = 0; f < 256 * 9; f++)
		{
			// Find next closest step
			Node next = findNextClosestNode(world, cx, cz);
			if (next == null)
			{
				System.err.println("AI Can not find path. Node count: " + nodes.size() + " Obstacles: " + obstacleNodes.size());
				moveTime = 1;
				moveIteration = 5;
				return;
			}

			if (next.x == dest.x && next.z == dest.z)
			{
				foundWithObstacles = !obstacleNodes.isEmpty();
				System.out.println("AI found destination");
				moveTime = 1;
				moveIteration = 1;
				return;
			} else
			{
				cx = next.x;
				cz = next.z;
				nodes.add(next);
			}
		}

		System.err.println("AI Was stuck in loop");
	}

	private Node findNextClosestNode(World world, int cx, int cz)
	{
		Vector2i destinationPos = new Vector2i(nodes.get(0).x, nodes.get(0).z);
		Vector2i nextPos = new Vector2i();
		Vector2i closestNode = new Vector2i();
		Vector2i currentPos = new Vector2i(cx, cz);

		double lastDistance = Double.MAX_VALUE;

		byte failCount = 0;
		int fx = 0, fz = 0;

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				// Ignore current pos
				if (i - 1 == 0 && j - 1 == 0)
					continue;

				// Dont go through corners
				//				if (i - 1 == -1 && j - 1 == -1) continue;
				//				if (i - 1 == 1 && j - 1 == 1) continue;
				//				if (i - 1 == 1 && j - 1 == -1) continue;
				//				if (i - 1 == -1 && j - 1 == 1) continue;

				// Ignore obstacle nodes
				boolean skip = false;
				for (Node n : obstacleNodes)
				{
					if (i - 1 + cx == n.x && j - 1 + cz == n.z)
					{
						skip = true;
						failCount++;
						fx = i;
						fz = j;
						break;
					}
				}
				if (skip)
					continue;

				// Don't backtrack
				skip = false;
				for (int k = 1; k < nodes.size(); k++)
				{
					Node n = nodes.get(k);
					if (i - 1 + cx == n.x && j - 1 + cz == n.z)
					{
						skip = true;
						failCount++;
						fx = i;
						fz = j;
						break;
					}
				}
				if (skip)
					continue;

				Block b = world.getBlock(i - 1 + cx, 1, j - 1 + cz);

				if (blockData.get(b).canPass)
				{
					nextPos.set(i - 1, j - 1).add(currentPos);

					if (nextPos.distance(destinationPos) < lastDistance)
					{
						closestNode.set(nextPos);
						lastDistance = nextPos.distance(destinationPos);
					} else
					{
						failCount++;
					}
				} else
				{
					failCount++;
				}
			}
		}

		if (failCount == 8)
		{
			System.err.println("Adding node at " + (cx) + "/" + (cz));
			obstacleNodes.add(new Node(cx, 1, cz));
			return null;
		}
		return new Node(closestNode.x, 1, closestNode.y);
	}

	private void render()
	{/*
		EntityHitbox plate = new EntityHitbox(0.4f, 1f / 32f, 0.4f);
		plate.setHitbox((int) Math.floor(getX()) + 0.5f, (int) Math.floor(getY()) + 1f / 32f, (int) Math.floor(getZ()) + 0.5f);

		CaveGame.t.add(plate.getHitbox());


		EntityHitbox node = new EntityHitbox(0.2f, 0.2f, 0.2f);
		for (Node n : nodes)
		{
			node.setHitbox(n.x + 0.5f, n.y + 0.5f, n.z + 0.5f);
			CaveGame.t.add(new AABBf(node.getHitbox()));
		}


		node = new EntityHitbox(0.3f, 0.3f, 0.3f);
		for (Node n : obstacleNodes)
		{
			node.setHitbox(n.x + 0.5f, n.y + 0.5f, n.z + 0.5f);
			CaveGame.t.add(new AABBf(node.getHitbox()));
		}*/
	}

	@Override
	public boolean isDead()
	{
		return isDead;
	}

	@Override
	public boolean sort()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "ai";
	}

	public static class Node
	{
		int x, y, z;

		public Node(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	private class BlockData
	{
		float foo;
		boolean canPass;

		public BlockData(float foo, boolean canPass)
		{
			this.foo = foo;
			this.canPass = canPass;
		}
	}

	@Override
	public void addPosition(Vector3f position)
	{
		super.addPosition(position);
		move.set(position.x, position.y, position.z);
	}
}
