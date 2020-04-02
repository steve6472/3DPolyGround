package steve6472.polyground.entity.model.models;

import steve6472.polyground.entity.EntityBase;
import steve6472.polyground.entity.model.EntityModel;
import steve6472.sge.gfx.Sprite;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.09.2019
 * Project: SJP
 *
 ***********************/
public class EmptyModel implements EntityModel
{
	@Override
	public void initModel()
	{
	}

	@Override
	public void initTexture()
	{
	}

	@Override
	public void render(EntityBase entity)
	{
	}

	@Override
	public Sprite getTexture()
	{
		return null;
	}
}
