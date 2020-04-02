package steve6472.polyground.registry.face;

import steve6472.polyground.block.model.faceProperty.FaceProperty;

public class FaceEntry<T extends FaceProperty>
{
	private IFaceFactory<T> factory;
	private T instance;

	FaceEntry(IFaceFactory<T> factory)
	{
		this.factory = factory;
		this.instance = createNew();
	}

	public T createNew()
	{
		return factory.create();
	}

	public T getInstance()
	{
		return instance;
	}
}