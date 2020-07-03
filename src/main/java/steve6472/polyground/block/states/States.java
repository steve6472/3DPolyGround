package steve6472.polyground.block.states;

import steve6472.polyground.block.properties.BooleanProperty;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IntProperty;
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

	public static final EnumProperty<EnumSlabType> SLAB_TYPE = EnumProperty.create("type", EnumSlabType.class, EnumSlabType.getValues());

	public static final IntProperty STALA_WIDTH = IntProperty.create("width", 1, 7);
}
