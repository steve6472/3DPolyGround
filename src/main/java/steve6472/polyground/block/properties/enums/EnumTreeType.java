package steve6472.polyground.block.properties.enums;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.12.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumTreeType
{
	OAK("oak_leaves", "oak_planks", "oak_log", "oak_log_top", true),
	ACACIA("acacia_leaves", "acacia_planks", "acacia_log", "acacia_log_top", false);

	private final String leaves, leavesTexture;
	private final String planks, planksTexture;
	private final String logTopTexture;
	private final String log, logTexture;

	EnumTreeType(String leaves, String planks, String log, String logTop, boolean big)
	{
		this.leaves = leaves;
		this.planks = planks;
		this.log = log;

		if (big)
		{
			this.logTopTexture = "block/wood/big_log/" + logTop;
			this.logTexture = "block/wood/big_log/" + log;
		} else
		{
			this.logTopTexture = "block/wood/log/" + logTop;
			this.logTexture = "block/wood/log/" + log;
		}
		this.leavesTexture = "block/leaves/" + leaves;
		this.planksTexture = "block/wood/planks/" + planks;
	}

	public String getLeaves()
	{
		return leaves;
	}

	public String getPlanks()
	{
		return planks;
	}

	public String getLog()
	{
		return log;
	}

	public String getLeavesTexture()
	{
		return leavesTexture;
	}

	public String getPlanksTexture()
	{
		return planksTexture;
	}

	public String getLogTopTexture()
	{
		return logTopTexture;
	}

	public String getLogTexture()
	{
		return logTexture;
	}

	private static final EnumTreeType[] VALUES = values();

	public static EnumTreeType[] getValues()
	{
		return VALUES;
	}
}
