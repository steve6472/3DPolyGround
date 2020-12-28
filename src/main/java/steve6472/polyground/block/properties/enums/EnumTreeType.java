package steve6472.polyground.block.properties.enums;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.12.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumTreeType
{
	OAK("oak_leaves", "oak_planks", "oak_log", "oak_log_top"),
	ACACIA("acacia_leaves", "acacia_planks", "acacia_log", "acacia_log_top");

	private final String leaves, leavesTexture;
	private final String planks, planksTexture;
	private final String logTopTexture;
	private final String log, logTexture;

	EnumTreeType(String leaves, String planks, String log, String logTop)
	{
		this.leaves = leaves;
		this.planks = planks;
		this.log = log;

		this.logTopTexture = "block/wood/log/" + logTop;
		this.leavesTexture = "block/leaves/" + leaves;
		this.planksTexture = "block/wood/planks/" + planks;
		this.logTexture = "block/wood/log/" + log;
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
