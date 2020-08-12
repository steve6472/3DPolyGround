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
}
