package steve6472.polyground.entity.model;

import org.joml.Vector3f;
import steve6472.polyground.entity.EntityBase;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.gfx.stack.StackUtil;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class EntityModel<T extends EntityBase>
{
	private List<ModelCube> cubes;

	public EntityModel()
	{
		cubes = new ArrayList<>();
	}

	public void update(T entity)
	{

	}

	protected ModelCube addCube(float x, float y, float z, float w, float h, float d)
	{
		ModelCube cube = new ModelCube(new Vector3f(), new Vector3f(), new Vector3f(x, y, z), new Vector3f(w, h, d), new Vector3f(0.5f, 0.5f, 0.5f));
		cubes.add(cube);
		return cube;
	}

	public void render(Stack stack)
	{
		for (ModelCube cube : cubes)
		{
			stack.color(cube.color.x, cube.color.y, cube.color.z, 1);
			stack.pushMatrix();
			stack.translate(cube.origin);
			stack.rotateXYZ(cube.rotation);
			stack.translate(-cube.origin.x, -cube.origin.y, -cube.origin.z);
			StackUtil.rect(stack, cube.position.x, cube.position.y, cube.position.z, cube.size.x, cube.size.y, cube.size.z);
			stack.popMatrix();
		}
	}
}
