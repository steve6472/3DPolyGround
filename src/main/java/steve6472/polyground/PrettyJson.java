package steve6472.polyground;

import org.json.JSONArray;
import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2020
 * Project: CaveGame
 *
 ***********************/
public class PrettyJson
{
	public static String prettify(JSONObject json)
	{
		byte i = 0;

		StringBuilder sb = new StringBuilder();

		boolean inString = false;
		boolean isValidArray = false;

		String formattedJSON = json.toString(4);
//		System.out.println(formattedJSON);

		int index = 0;
		for (char c : formattedJSON.toCharArray())
		{
			if (c == '"' && i == 0) i++;
			if (c == ':' && i == 1) i++;
			if (c == '[' && i == 2) i++;

			// Check if array is not object array
			if (i == 3 && !isValidArray)
			{
				boolean inStr = false;
//				StringBuilder test = new StringBuilder();
				for (int j = index; j < formattedJSON.length(); j++)
				{
					char ch = formattedJSON.charAt(j);
//					test.append(ch);
					if (ch == '"')
						inStr = !inStr;
					if (inStr)
						continue;
					if (ch == ' ' || ch == '\n')
						continue;
					if (ch == '{' || ch == '}')
					{
						i = 0;
						break;
					}
					if (ch == ']')
					{
						isValidArray = true;
						break;
					}
				}
//				System.out.println("Test: \n'" + test + "\n', isValid: " + isValidArray);
			}

			if (i == 3)
			{
				if (c == '"')
					inString = !inString;

				if (!inString)
				{
					if (c != ' ' && c != '\n')
						sb.append(c);
					if (c == ']')
					{
						i = 0;
						isValidArray = false;
					}
					if (c == ',')
					{
						sb.append(' ');
					}
				} else
				{
					sb.append(c);
				}
			} else
			{
				sb.append(c);
			}

			index++;
		}

		return sb.toString();
	}

	public static void main(String[] args)
	{
		JSONObject main = new JSONObject();
		main.put("value", 5);
		main.put("object", new JSONObject().put("value", "inside_object").put("another_value", 8));
		main.put("array", new JSONArray().put(0).put(1).put(2).put(3));
		main.put("array_", new JSONArray().put("hello").put("[text]"));
		main.put("obj_arr", new JSONArray().put(new JSONObject().put("a", "b").put("c", "d")).put(new JSONObject().put("e", 5).put("f", 16)));
		main.put("obj_arr_", new JSONArray().put(new JSONObject().put("from", new JSONArray().put(0).put(1).put(2))));
		System.out.println(prettify(main));
	}
}
