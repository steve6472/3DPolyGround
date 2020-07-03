package steve6472.polyground.generator.models;

public interface IModel
{
	String build();

	default String name()
	{
		return "no_model_name";
	}
}