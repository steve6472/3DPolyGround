package steve6472.polyground.block.model.faceProperty;

import steve6472.polyground.registry.face.FaceEntry;
import steve6472.polyground.registry.face.FaceRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.11.2019
 * Project: SJP
 *
 ***********************/
public class BiomeTintFaceProperty extends AbstractBooleanProperty
{
	public BiomeTintFaceProperty()
	{
	}

	public BiomeTintFaceProperty(boolean flag)
	{
		setFlag(flag);
	}

	@Override
	protected FaceEntry<? extends AbstractBooleanProperty> getEntry()
	{
		return FaceRegistry.biomeTint;
	}

	@Override
	public FaceProperty createCopy()
	{
		return new BiomeTintFaceProperty(isFlag());
	}

	@Override
	public String getId()
	{
		return "biomeTint";
	}
}
