package steve6472.polyground.block.blockdata.logic;

import steve6472.polyground.block.blockdata.logic.gates.*;
import steve6472.polyground.block.blockdata.logic.other.*;

import java.util.HashMap;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.11.2020
 * Project: CaveGame
 *
 ***********************/
public class GateReg
{
	private static final HashMap<String, GateEntry> REGISTRY = new HashMap<>();

	static
	{
		register("high_constant", LogOne::new, 1, 2, 1);
		register("low_constant", LogZero::new, 1, 2, 1);
		register("light", Light::new, 3, 2, 3, -1, 0, -1);
		register("switch", Switch::new, 3, 2, 3, -1, 0, -1);
		register("seven_segment_display", SevenSegmentDisplay::new, 7, 2, 4, -3, 0, -1);
		register("cross", Cross::new, 3, 1, 3, -1, 0, -1);
		register("connector_to_down", ConnectorDown::new, 1, 2, 1, 0, 0, 0);
		register("connector_to_up", ConnectorUp::new, 3, 2, 1, -1, 0, 0);

		register("input", Input::new, 1, 2, 2, 0, 0, -1);
		register("output", Output::new, 1, 2, 2);

		register("not", NotGate::new, 1, 1, 2);
		register("and", AndGate::new, 3, 1, 2, -1, 0, 0);
		register("nand", NandGate::new, 3, 1, 2, -1, 0, 0);
		register("or", OrGate::new, 3, 1, 2, -1, 0, -1);
		register("nor", NorGate::new, 3, 1, 2, -1, 0, -1);
	}

	public static AbstractGate newGate(String gateName)
	{
		return REGISTRY.get(gateName).supplier.get();
	}

	public static boolean has(String gateName)
	{
		return REGISTRY.containsKey(gateName);
	}

	public static boolean canFit(String gateName, int x, int y, int z, int[][] grid, int size)
	{
		return REGISTRY.get(gateName).canFit(x, y, z, grid, size);
	}

	public static AbstractGate createGate(String gateName, int x, int y, int z)
	{
		GateEntry entry = REGISTRY.get(gateName);
		AbstractGate gate = entry.supplier.get();

		gate.position.set(x + entry.offsetX, y + entry.offsetY, z + entry.offsetZ);
		gate.setSize(entry.sizeX, entry.sizeY, entry.sizeZ);

		return gate;
	}

	public static GateEntry getEntry(String gateName)
	{
		return REGISTRY.get(gateName);
	}

	private static void register(String gateName, Supplier<AbstractGate> supplier, int sizeX, int sizeY, int sizeZ)
	{
		REGISTRY.put(gateName, new GateEntry(supplier, sizeX, sizeY, sizeZ));
	}

	private static void register(String gateName, Supplier<AbstractGate> supplier, int sizeX, int sizeY, int sizeZ, int offsetX, int offsetY, int offsetZ)
	{
		REGISTRY.put(gateName, new GateEntry(supplier, sizeX, sizeY, sizeZ, offsetX, offsetY, offsetZ));
	}

	public static class GateEntry
	{
		public final int sizeX, sizeY, sizeZ;
		public final int offsetX, offsetY, offsetZ;
		final Supplier<AbstractGate> supplier;

		GateEntry(Supplier<AbstractGate> supplier, int sizeX, int sizeY, int sizeZ)
		{
			this(supplier, sizeX, sizeY, sizeZ, 0, 0, 0);
		}

		GateEntry(Supplier<AbstractGate> supplier, int sizeX, int sizeY, int sizeZ, int offsetX, int offsetY, int offsetZ)
		{
			this.supplier = supplier;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.sizeZ = sizeZ;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.offsetZ = offsetZ;
		}

		boolean canFit(int x, int y, int z, int[][] grid, int size)
		{
			return GateReg.canFit(x, y, z, offsetX, offsetY, offsetZ, sizeX, sizeY, sizeZ, grid, size);
		}
	}

	public static boolean canFit(int x, int y, int z, int offsetX, int offsetY, int offsetZ, int sizeX, int sizeY, int sizeZ, int[][] grid, int size)
	{
		if (x + offsetX < 0 ||
			y + offsetY < 0 ||
			z + offsetZ < 0 ||
			x + sizeX + offsetX > size ||
			y + sizeY + offsetY > size ||
			z + sizeZ + offsetZ > size)
			return false;

		for (int i = y + offsetY; i < sizeY + y + offsetY; i++)
		{
			for (int j = x + offsetX; j < sizeX + x + offsetX; j++)
			{
				for (int k = z + offsetZ; k < sizeZ + z + offsetZ; k++)
				{
					if (grid[i][j + k * size] != 0)
						return false;
				}
			}
		}

		return true;
	}
}
