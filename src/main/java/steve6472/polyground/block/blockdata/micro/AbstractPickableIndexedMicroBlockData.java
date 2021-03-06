package steve6472.polyground.block.blockdata.micro;

import net.querz.nbt.tag.CompoundTag;
import org.joml.Matrix4f;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.gfx.DynamicEntityModel;
import steve6472.polyground.gfx.IModel;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.11.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class AbstractPickableIndexedMicroBlockData extends AbstractIndexedMicroBlockData implements IModel
{
	public DynamicEntityModel model;

	public AbstractPickableIndexedMicroBlockData()
	{
		super();
		model = new DynamicEntityModel();
		updateModel();
	}

	public void updateModel()
	{
		model.load((modelBuilder) -> {

			Bakery.tempBuilder(modelBuilder);

			int tris = 0;

			for (int i = 0; i < getSize(); i++)
			{
				for (int j = 0; j < getSize(); j++)
				{
					for (int k = 0; k < getSize(); k++)
					{
						if (grid[j][i + k * getSize()] != -128)
						{
							int flags = Bakery.createFaceFlags(
								i != (getSize() - 1) && grid[j][(i + 1) + k * getSize()] != -128,
								k != (getSize() - 1) && grid[j][i + (k + 1) * getSize()] != -128,
								i != 0 && grid[j][(i - 1) + k * getSize()] != -128,
								k != 0 && grid[j][i + (k - 1) * getSize()] != -128,
								j != (getSize() - 1) && grid[j + 1][i + k * getSize()] != -128,
								j != 0 && grid[j - 1][i + k * getSize()] != -128
							);
							int color = getPalette().getColors()[grid[j][i + k * getSize()] + 128];
							tris += Bakery.coloredCube_1x1(i, j, k, color, flags);
						}
					}
				}
			}

			Bakery.worldBuilder();

			return tris;
		});
	}

	@Override
	public void read(CompoundTag tag)
	{
		super.read(tag);

		updateModel();
	}

	@Override
	public void render(Matrix4f viewMatrix, Matrix4f mat)
	{
		model.render(viewMatrix, mat);
	}
}
