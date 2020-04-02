package steve6472.polyground.entity.model;

import steve6472.polyground.entity.EntityBase;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.10.2019
 * Project: SJP
 *
 ***********************/
public interface IPostRender<T extends EntityBase>
{
	void postRender(T entity);
}
