package steve6472.polyground.generator.creator;

import org.joml.AABBf;
import steve6472.polyground.block.model.CubeHitbox;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.10.2019
 * Project: SJP
 *
 ***********************/
public class CreatorCube extends CubeHitbox implements ICreatorCube
{
	private String name;
	private int index;

	public CreatorCube(AABBf aabb)
	{
		super(aabb);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(String newName)
	{
		this.name = newName;
	}

	@Override
	public int getIndex()
	{
		return index;
	}

	@Override
	public void setIndex(int index)
	{
		this.index = index;
	}

	@Override
	public CubeHitbox getCube()
	{
		return this;
	}
}
