package steve6472.polyground.block.model.registry.face;

import steve6472.polyground.block.model.faceProperty.FaceProperty;

@FunctionalInterface
public interface IFaceFactory<T extends FaceProperty>
{
	T create();
}