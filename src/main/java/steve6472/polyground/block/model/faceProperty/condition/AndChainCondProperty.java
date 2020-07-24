package steve6472.polyground.block.model.faceProperty.condition;

import org.json.JSONObject;
import steve6472.polyground.block.model.faceProperty.FaceProperty;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class AndChainCondProperty extends FaceProperty
{
	private ICheck check;

	public AndChainCondProperty()
	{
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		String condition = json.getString("andChain");
		check = new AndChain(condition);
	}

	public void fixBlockId()
	{
		check.fixBlockId();
	}

	boolean test(int x, int y, int z, World world)
	{
		return check.test(x, y, z, world);
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		System.err.println("Can not save CondProperty yet!");
		System.exit(0);
	}

	@Override
	public String getId()
	{
		return "andChain";
	}

	@Override
	public AndChainCondProperty createCopy()
	{
		return null;
	}
}
