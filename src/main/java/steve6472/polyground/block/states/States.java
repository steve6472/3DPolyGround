package steve6472.polyground.block.states;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.BooleanProperty;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.block.properties.enums.EnumHalf;
import steve6472.polyground.block.properties.enums.EnumHalfSnowy;
import steve6472.polyground.block.properties.enums.EnumSlabType;

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

	public static final EnumProperty<EnumSlabType> SLAB_TYPE = EnumProperty.create("type", EnumSlabType.class, EnumSlabType.getValues());
	public static final EnumProperty<EnumFace> FACING_BLOCK = EnumProperty.create("facing", EnumFace.class, EnumFace.NORTH, EnumFace.EAST, EnumFace.SOUTH, EnumFace.WEST);
	public static final EnumProperty<EnumAxis> AXIS = EnumProperty.create("axis", EnumAxis.class, EnumAxis.getValues());
	public static final EnumProperty<EnumHalf> HALF = EnumProperty.create("half", EnumHalf.class, EnumHalf.getValues());
	public static final EnumProperty<EnumHalfSnowy> HALF_SNOWY = EnumProperty.create("half_snowy", EnumHalfSnowy.class, EnumHalfSnowy.getValues());

	public static final IntProperty STALA_WIDTH = IntProperty.create("width", 1, 7);
	public static final IntProperty SNOW_LAYERS = IntProperty.create("layers", 1, 8);
	public static final IntProperty DISTANCE = IntProperty.create("distance", 1, 7);
}
