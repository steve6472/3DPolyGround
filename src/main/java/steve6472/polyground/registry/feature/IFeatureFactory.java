package steve6472.polyground.registry.feature;

import steve6472.polyground.world.generator.Feature;

@FunctionalInterface
public interface IFeatureFactory
{
	Feature create();
}