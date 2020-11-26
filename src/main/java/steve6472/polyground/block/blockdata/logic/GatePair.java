package steve6472.polyground.block.blockdata.logic;

public class GatePair
{
	private final AbstractGate gate;
	private final int index;

	public GatePair(AbstractGate gate, int index)
	{
		this.gate = gate;
		this.index = index;
	}

	public AbstractGate getGate()
	{
		return gate;
	}

	public int getIndex()
	{
		return index;
	}

	public GatePair copy()
	{
		return new GatePair(gate.copy(), index);
	}
}