package steve6472.polyground.block.states;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.BooleanProperty;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.properties.enums.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.07.2020
 * Project: StateTest
 *
 ***********************/
public class States
{
	public static final BooleanProperty LIT = BooleanProperty.create("lit");
	public static final BooleanProperty PERSISTENT = BooleanProperty.create("persistent");
	public static final BooleanProperty HAS_LEAVES = BooleanProperty.create("leaves");

	public static final BooleanProperty HAS_HEAD = BooleanProperty.create("head");
	public static final BooleanProperty HAS_STICK = BooleanProperty.create("stick");
	public static final BooleanProperty HAS_STRING = BooleanProperty.create("string");

	public static final EnumProperty<EnumSlabType> SLAB_TYPE = EnumProperty.create("type", EnumSlabType.class, EnumSlabType.getValues());
	public static final EnumProperty<EnumFace> FACING_BLOCK = EnumProperty.create("facing", EnumFace.class, EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST);
	public static final EnumProperty<EnumFace> FACING = EnumProperty.create("facing", EnumFace.class, EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST, EnumFace.UP, EnumFace.DOWN);
	public static final EnumProperty<EnumAxis> AXIS = EnumProperty.create("axis", EnumAxis.class, EnumAxis.getValues());
	public static final EnumProperty<EnumHalf> HALF = EnumProperty.create("half", EnumHalf.class, EnumHalf.getValues());
	public static final EnumProperty<EnumStoneType> STONE_TYPE = EnumProperty.create("stone_type", EnumStoneType.class, EnumStoneType.getValues());
	public static final EnumProperty<EnumHalfSnowy> HALF_SNOWY = EnumProperty.create("half_snowy", EnumHalfSnowy.class, EnumHalfSnowy.getValues());
	public static final EnumProperty<EnumFlowerColor> FLOWER_COLOR = EnumProperty.create("color", EnumFlowerColor.class, EnumFlowerColor.getValues());

	public static final IntProperty STALA_WIDTH = IntProperty.create("width", 1, 7);
	public static final IntProperty SNOW_LAYERS = IntProperty.create("layers", 1, 8);
	public static final IntProperty DISTANCE_1_7 = IntProperty.create("distance", 1, 7);
	public static final IntProperty DISTANCE_1_6 = IntProperty.create("distance", 1, 6);
	public static final IntProperty LOGS = IntProperty.create("logs", 1, 6);
	public static final IntProperty STAGE_1_3 = IntProperty.create("stage", 1, 3);
	public static final IntProperty RADIUS_0_7 = IntProperty.create("radius", 0, 7);
}
