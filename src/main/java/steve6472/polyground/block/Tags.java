package steve6472.polyground.block;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.08.2020
 * Project: CaveGame
 *
 ***********************/
public class Tags
{
	/**
	 * Allow blocks like flowers, grass to be placed on top of this block
	 */
	public static final String FLOWER_TOP = "flower_top";

	/**
	 * Allow blocks like cactus to be placed on top of this block
	 */
	public static final String CACTUS_TOP = "cactus_top";

	/**
	 * Leaves around block with this tag set their distance to the closest log
	 */
	public static final String LOG = "log";

	/**
	 * Blocks with this tag will not prevent SpreadBlocks from dying when on top of them
	 */
	public static final String TRANSPARENT = "transparent";

	/**
	 * Blocks with this tag can cause corruption.
	 */
	public static final String CORRUPTED = "corrupted";

	/**
	 * Blocks with this tag will force to kill spread block below them
	 */
	public static final String KILL_SPREAD_BOTTOM = "kill_spread_bottom";

	/**
	 * Allow blocks like shrubs to be placed on top of this block
	 */
	public static final String SHRUBS_TOP = "shrubs_top";

	/**
	 * Only error block has this tag
	 */
	public static final String ERROR = "error";

	/**
	 * Any block that has all faces solid and is 16x16x16 should have this tag
	 */
	public static final String SOLID = "solid";

	/**
	 * Any block that can be picked up by the player with hands
	 */
	public static final String PICKABLE = "pickable";

	/**
	 * Liq pipes can connect to these blocks
	 */
	public static final String LIQ_CONNECT = "liq_connect";
}
