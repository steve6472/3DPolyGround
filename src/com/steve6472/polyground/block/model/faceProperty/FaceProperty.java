package com.steve6472.polyground.block.model.faceProperty;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public abstract class FaceProperty
{
//	List<FaceProperty> properties;

	public FaceProperty()
	{
//		properties = new ArrayList<>();
	}

	public abstract void loadFromJSON(JSONObject json);

	public abstract void saveToJSON(JSONObject faceJson);

	public abstract FaceProperty createCopy();

	public abstract String getId();

//	public void loadChildProperties(JSONObject propertyJson)
//	{
//		for (FaceProperty f : properties)
//		{
//			f.loadFromJSON(propertyJson);
//		}
//	}

	/**
	 * @author https://stackoverflow.com/a/1526843
	 * @return Fields of FaceProperty
	 */
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getSimpleName());
		result.append(" Object ");
		result.append(newLine);
		result.append("{");
		result.append(newLine);

		//determine fields declared in this class only (no fields of superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		//print field names paired with their values
		for (Field field : fields)
		{
			result.append("\t");
			try
			{
				field.setAccessible(true);

				result.append(field.getName());
				result.append(": ");
				//requires access to private field:
				result.append(field.get(this));
			} catch (IllegalAccessException ex)
			{
				System.out.println(ex.toString());
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}
}
