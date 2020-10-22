package steve6472.polyground.entity.model;

import steve6472.polyground.entity.Testity;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ClaySolderModel extends EntityModel<Testity>
{
	private final ModelCube rightArm, leftArm, rightLeg, leftLeg, torso, head;

	public ClaySolderModel()
	{
		rightArm = addCube(1.0F, 3.0F, -0.5F, 1.0F, 3.0F, 1.0F);
		leftArm = addCube(-2.0F, 3.0F, -0.5F, 1.0F, 3.0F, 1.0F);
		rightLeg = addCube(-1.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F);
		leftLeg = addCube(0.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F);
		torso = addCube(-1.0F, 3.0F, -0.5F, 2.0F, 3.0F, 1.0F);
		head = addCube(-1.0F, 6.0F, -1.0F, 2.0F, 2.0F, 2.0F);

		leftLeg.setColor(1, 0, 0);
		rightLeg.setColor(0, 0, 1);

		rightArm.setOrigin(1f, 6f, 0f);
		leftArm.setOrigin(-1f, 6f, 0f);
		rightLeg.setOrigin(0, 3, 0);
		leftLeg.setOrigin(0, 3, 0);
	}

	@Override
	public void update(Testity entity)
	{
		float speed = 1f / 10f;
		float swingAmount = 1f / 20f;

		float arm = (float) Math.sin(Math.toRadians((System.currentTimeMillis() % 3600) * speed)) * swingAmount + swingAmount;

		rightArm.setRotation(0, 0, arm);
		leftArm.setRotation(0, 0, -arm);

		float motion = entity.getMotion().length();
		float leg = (float) Math.sin(Math.toRadians((System.currentTimeMillis() % 3600) * 0.5f)) * motion * 6f;
		leftLeg.setRotation(leg, 0, 0);
		rightLeg.setRotation(-leg, 0, 0);
	}
}
