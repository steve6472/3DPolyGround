package com.steve6472.polyground.entity;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.World;
import com.steve6472.sge.main.util.RandomUtil;
import org.joml.AABBf;
import org.joml.Vector2i;
import org.joml.Vector3f;

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
	private HashMap<Block, BlockData> blockData;

	private Interpolator3D move;

	public AIEntity()
	{
		addPosition(0, 0.5f, 0);
		hitbox = new EntityHitbox(0.4999f, 0.4999f, 0.4999f);
		nodes = new ArrayList<>();
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

	@Override
	public void tick()
	{
		if (getY() < -16) isDead = true;

		tick += 1f / 60f;

		if (nodes.isEmpty())
		{
			randomDest();
		}

		moveTick++;
		if (moveTick == moveTime)
		{
			moveToNextNode();
			moveTick = 0;
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
		if (nodes.isEmpty()) return;

		Node next;
		if (nodes.size() == 1)
		{
			next = nodes.get(0);
			nodes.clear();
		} else
		{
			next = nodes.get(1);
			nodes.remove(1);
		}

		move.set(next.x + 0.5f, next.y + 0.5f, next.z + 0.5f, (long) Math.floor(1000d / 60d * (double) moveTime));
	}

	private void calcPath(Node dest)
	{
		if (nodes.size() != 0) return;

		World world = CaveGame.getInstance().world;

		// Destination node is first
		nodes.add(dest);

		int cx = (int) Math.floor(getX());
		int cz = (int) Math.floor(getZ());

		for (int f = 0; f < 256 * 9; f++)
		{
			// Find next closest step
			Node next = findNextClosestNode(world, cx, cz);
			if (next == null)
			{
				System.err.println("AI Can not find path");
				moveTime = 5;
				return;
			}

			if (next.x == dest.x && next.z == dest.z)
			{
				System.out.println("AI found destination");
//				moveTime = (int) Math.floor((60.0 * 2.5) / (double) f);
//				if (moveTime == 0 || moveTime > 10) moveTime = 1;
				moveTime = 1;
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

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				// Ignore current pos
				if (i - 1 == 0 && j - 1 == 0) continue;

				// Dont go through corners
//				if (i - 1 == -1 && j - 1 == -1) continue;
//				if (i - 1 == 1 && j - 1 == 1) continue;
//				if (i - 1 == 1 && j - 1 == -1) continue;
//				if (i - 1 == -1 && j - 1 == 1) continue;

				// Don't backtrack
				boolean skip = false;
				for (int k = 1; k < nodes.size(); k++)
				{
					Node n = nodes.get(k);
					if (i - 1 + cx == n.x && j - 1 + cz == n.z)
					{
						skip = true;
						failCount++;
						break;
					}
				}
				if (skip) continue;

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

		if (failCount == 8) return null;
		return new Node(closestNode.x, 1, closestNode.y);
	}

	private void render()
	{
		EntityHitbox plate = new EntityHitbox(0.4f, 1f / 32f, 0.4f);
		plate.setHitbox((int) Math.floor(getX()) + 0.5f, (int) Math.floor(getY()) + 1f / 32f, (int) Math.floor(getZ()) + 0.5f);

		CaveGame.t.add(plate.getHitbox());


		EntityHitbox node = new EntityHitbox(0.2f, 0.2f, 0.2f);
		for (Node n : nodes)
		{
			node.setHitbox(n.x + 0.5f, n.y + 0.5f, n.z + 0.5f);
			CaveGame.t.add(new AABBf(node.getHitbox()));
		}
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

	private class Node
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
