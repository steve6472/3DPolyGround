package steve6472.polyground.generator.models;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemFromModel implements IModel
{
	private static String itemFromBlockJson = "{\"type\": \"from_model\",\"model_name\": \"XXX\"}";

	private String model;

	public ItemFromModel(String model)
	{
		this.model = model;
	}

	@Override
	public String build()
	{

		return itemFromBlockJson.replace("XXX", model);
	}
}
