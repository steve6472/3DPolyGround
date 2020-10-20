package steve6472.polyground.block.special;

import org.joml.AABBf;
import org.json.JSONObject;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.blockdata.PipeData;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.model.elements.CubeBakerBuilder;
import steve6472.polyground.block.model.elements.FaceBakerBuilder;
import steve6472.polyground.block.model.elements.UvBuilder;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.10.2020
 * Project: CaveGame
 *
 ***********************/
public class PipeBlock extends CustomBlock implements IBlockData
{
	public PipeBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		BlockAtlas.putTexture("block/pipe");
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL && modelLayer != ModelLayer.TRANSPARENT)
			return 0;

		int tris = 0;

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		int r = 3;
		int d = r * 2;

		String texture = "block/pipe";

		boolean north = check(world.getState(x + 1, y, z), state);
		boolean east = check(world.getState(x, y, z + 1), state);
		boolean south = check(world.getState(x - 1, y, z), state);
		boolean west = check(world.getState(x, y, z - 1), state);
		boolean up = check(world.getState(x, y + 1, z), state);
		boolean down = check(world.getState(x, y - 1, z), state);

		if (modelLayer == ModelLayer.NORMAL)
		{
			tris += Bakery.autoTexturedCube(8 - r, 8 - r, 8 - r, d, d, d, texture, Bakery.createFaceFlags(north, east, south, west, up, down));
			tris += Bakery.autoTexturedCube(8 - r + d, 8 - r + d, 8 - r + d, -d, -d, -d, texture, Bakery.createFaceFlags(south, west, north, east, down, up));
		}

		/*
		PipeData data = (PipeData) world.getData(x, y, z);
		float h = data.getEnergyStored() / 10f * 0.99f;

		if (modelLayer == ModelLayer.TRANSPARENT)
		{
			tris += Bakery.autoTexturedCube(5.05f, 5.05f, 5.05f, 5.9f, h, 5.9f, "block/liq", 0);
			if (north) tris += Bakery.autoTexturedCube(10.95f, 5.05f, 5.05f, 5.05f, h, 5.9f, "block/liq", 0);
			if (east) tris += Bakery.autoTexturedCube(5.05f, 5.05f, 10.95f, 5.9f, h, 5.05f, "block/liq", 0);
			if (south) tris += Bakery.autoTexturedCube(0, 5.05f, 5.05f, 5.05f, h, 5.9f, "block/liq", 0);
			if (west) tris += Bakery.autoTexturedCube(5.05f, 5.05f, 0f, 5.9f, h, 5.05f, "block/liq", 0);
			if (up) tris += Bakery.autoTexturedCube(5.05f, 11f, 5.05f, 5.9f, 5, 5.9f, "block/liq", 0);
			if (down) tris += Bakery.autoTexturedCube(5.05f, 0f, 5.05f, 5.9f, 5, 5.9f, "block/liq", 0);
			return tris;
		} */

		if (north)
		{
			tris += CubeBakerBuilder
				.cubeBaker()
				.withBox(new AABBf(11, 5, 5, 16, 11, 11))
				.withUp(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 0, 11, 5))
					.withBothSides(true)
					.build())
				.withDown(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 0, 11, 5).flipY())
					.withBothSides(true)
					.build())
				.withEast(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(11, 5, 16, 11))
					.withBothSides(true)
					.build())
				.withWest(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(11, 5, 16, 11).flipX())
					.withBothSides(true)
					.build())
				.build()
				.add();
		}

		if (east)
		{
			tris += CubeBakerBuilder
				.cubeBaker()
				.withBox(new AABBf(5, 5, 11, 11, 11, 16))
				.withUp(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(11, 5, 16, 11))
					.withBothSides(true)
					.build())
				.withDown(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(11, 5, 16, 11).flipY())
					.withBothSides(true)
					.build())
				.withNorth(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(11, 5, 16, 11).flipX())
					.withBothSides(true)
					.build())
				.withSouth(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(11, 5, 16, 11))
					.withBothSides(true)
					.build())
				.build()
				.add();
		}

		if (south)
		{
			tris += CubeBakerBuilder
				.cubeBaker()
				.withBox(new AABBf(0, 5, 5, 5, 11, 11))
				.withUp(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 11, 11, 16))
					.withBothSides(true)
					.build())
				.withDown(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 0, 11, 5))
					.withBothSides(true)
					.build())
				.withEast(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(11, 5, 16, 11).flipX())
					.withBothSides(true)
					.build())
				.withWest(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(11, 5, 16, 11))
					.withBothSides(true)
					.build())
				.build()
				.add();
		}

		if (west)
		{
			tris += CubeBakerBuilder
				.cubeBaker()
				.withBox(new AABBf(5, 5, 0, 11, 11, 5))
				.withUp(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(0, 5, 5, 11))
					.withBothSides(true)
					.build())
				.withDown(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(11, 5, 16, 11).flipY())
					.withBothSides(true)
					.build())
				.withNorth(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(0, 5, 5, 11).flipX())
					.withBothSides(true)
					.build())
				.withSouth(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(0, 5, 5, 11))
					.withBothSides(true)
					.build())
				.build()
				.add();
		}

		if (up)
		{
			tris += CubeBakerBuilder
				.cubeBaker()
				.withBox(new AABBf(5, 11, 5, 11, 16, 11))
				.withNorth(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 0, 11, 5).flipX())
					.withBothSides(true)
					.build())
				.withEast(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 0, 11, 5))
					.withBothSides(true)
					.build())
				.withSouth(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 0, 11, 5))
					.withBothSides(true)
					.build())
				.withWest(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 0, 11, 5).flipX())
					.withBothSides(true)
					.build())
				.build()
				.add();
		}

		if (down)
		{
			tris += CubeBakerBuilder
				.cubeBaker()
				.withBox(new AABBf(5, 0, 5, 11, 5, 11))
				.withNorth(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 11, 11, 16))
					.withBothSides(true)
					.build())
				.withEast(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 11, 11, 16).flipX())
					.withBothSides(true)
					.build())
				.withSouth(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 11, 11, 16).flipX())
					.withBothSides(true)
					.build())
				.withWest(FaceBakerBuilder
					.faceBaker()
					.withTexture(texture)
					.withUv(UvBuilder.uv(5, 11, 11, 16))
					.withBothSides(true)
					.build())
				.build()
				.add();
		}

		return tris;
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		AABBf union = new AABBf();

		float radius = 3 / 16f;
		float diameter = 6 / 16f;

		List<AABBf> collisionBox = new ArrayList<>();

		collisionBox.add(fromWidth(0.5f - radius, 0.5f - radius, 0.5f - radius, diameter, diameter, diameter));

		if (check(world.getState(x + 1, y, z), state))
			collisionBox.add(fromWidth(0.5f - radius + diameter, 0.5f - radius, 0.5f - radius, 0.5f - radius, diameter, diameter));

		if (check(world.getState(x, y, z + 1), state))
			collisionBox.add(fromWidth(0.5f - radius, 0.5f - radius, 0.5f - radius + diameter, diameter, diameter, 0.5f - radius));

		if (check(world.getState(x - 1, y, z), state))
			collisionBox.add(fromWidth(0, 0.5f - radius, 0.5f - radius, 0.5f - radius, diameter, diameter));

		if (check(world.getState(x, y, z - 1), state))
			collisionBox.add(fromWidth(0.5f - radius, 0.5f - radius, 0, diameter, diameter, 0.5f - radius));

		if (check(world.getState(x, y + 1, z), state))
			collisionBox.add(fromWidth(0.5f - radius, 0.5f - radius + diameter, 0.5f - radius, diameter, 0.5f - radius, diameter));

		if (check(world.getState(x, y - 1, z), state))
			collisionBox.add(fromWidth(0.5f - radius, 0, 0.5f - radius, diameter, 0.5f - radius, diameter));

		CubeHitbox[] hitboxes = new CubeHitbox[collisionBox.size() + 1];

		for (int i = 0; i < collisionBox.size(); i++)
		{
			union.union(collisionBox.get(i));
			CubeHitbox hb = new CubeHitbox(collisionBox.get(i));
			hb.setVisible(true);
			hb.setHitbox(false);
			hitboxes[i] = hb;
		}

		CubeHitbox hitbox = new CubeHitbox(union);
		hitbox.setCollisionBox(false);

		hitboxes[hitboxes.length - 1] = hitbox;

		return hitboxes;
	}

	private AABBf fromWidth(float x, float y, float z, float w, float h, float d)
	{
		return new AABBf(x, y, z, x + w, y + h, z + d);
	}

	private boolean check(BlockState world, BlockState itself)
	{
		return world.getBlock() == this || world.hasTag(Tags.LIQ_CONNECT);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new PipeData();
	}
}
