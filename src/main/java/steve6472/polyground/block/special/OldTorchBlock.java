package steve6472.polyground.block.special;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.11.2019
 * Project: SJP
 *
 ***********************/
public class OldTorchBlock// extends Block
{/*
	private File f;
	private float r, g, b, a;
	private float constant, linear, quadratic;
	private float xOffset, yOffset, zOffset;
	private float spawnRadiusX, spawnRadiusY, spawnRadiusZ, spawnRadius;
	private boolean sphereRadius;

	public OldTorchBlock(File f)
	{
		super(f);
		this.f = f;
	}

	@Override
	public void postLoad()
	{
		if (f.isFile())
		{
			int color;

			SSS sss = new SSS(f);
			if (sss.hasValue("color"))
				color = sss.getHexInt("color");
			else
				color = 0xffffffff;

			float[] col = ColorUtil.getColors(color);
			r = col[0];
			g = col[1];
			b = col[2];
			a = col[3];

			spawnRadiusX = spawnRadiusY = spawnRadiusZ = spawnRadius = 1f / 16f + 1f / 32f;

			constant = sss.getFloat("constant");
			linear = sss.getFloat("linear");
			quadratic = sss.getFloat("quadratic");

			if (sss.containsName("lightXOffset")) xOffset = sss.getFloat("lightXOffset") / 16f;
			if (sss.containsName("lightYOffset")) yOffset = sss.getFloat("lightYOffset") / 16f;
			if (sss.containsName("lightZOffset")) zOffset = sss.getFloat("lightZOffset") / 16f;

			if (sss.containsName("spawnRadiusX")) spawnRadiusX = sss.getFloat("spawnRadiusX") / 16f;
			if (sss.containsName("spawnRadiusY")) spawnRadiusY = sss.getFloat("spawnRadiusY") / 16f;
			if (sss.containsName("spawnRadiusZ")) spawnRadiusZ = sss.getFloat("spawnRadiusZ") / 16f;
			if (sss.containsName("spawnRadius")) spawnRadius = sss.getFloat("spawnRadius") / 16f;

			if (sss.containsName("sphereRadius")) sphereRadius = sss.getBoolean("sphereRadius");
		}

		f = null;
	}

	@Override
	public void tick(BlockState state, World world, int x, int y, int z)
	{
		if (!RandomUtil.decide(6))
			return;

		Light l = LightManager.replaceIdeal(EnumLightSource.PARTICLE, 0, 0, 0, 0, 0, 0, constant, linear, quadratic, 0, 0, 0, 0);

		if (l == null)
			return;

		float posX = x + 0.5f + xOffset;
		float posY = y + 0.5f + yOffset;
		float posZ = z + 0.5f + zOffset;

		float dx;
		float dz;

		if (sphereRadius)
		{
			Vector3f pos = getRandomSpherePos(spawnRadius);
			dx = pos.x();
			dz = pos.z();

			posY += pos.y();
		} else
		{
			dx = RandomUtil.randomFloat(-spawnRadiusX, spawnRadiusX);
			dz = RandomUtil.randomFloat(-spawnRadiusZ, spawnRadiusZ);

			posY += RandomUtil.randomFloat(spawnRadiusY, spawnRadiusY);
		}
		posX += dx;
		posZ += dz;
/*
		LightParticle p = new LightParticle(
			new Vector3f(-dx / 30f, RandomUtil.randomFloat(0.015f, 0.025f), -dz / 30f),
			new Vector3f(posX, posY, posZ),
			RandomUtil.randomFloat(1f / 48f, 1f / 24f),
			r, g, b, a, l
			);
		subChunk.getWorld().getPg().particles.addParticle(p);*/
	/*}

	/**
	 * @author https://karthikkaranth.me/blog/generating-random-points-in-a-sphere/
	 *//*
	private Vector3f getRandomSpherePos(float radius)
	{
		double u = Math.random();
		float x1 = RandomUtil.randomFloat(0f, radius);
		float x2 = RandomUtil.randomFloat(0f, radius);
		float x3 = RandomUtil.randomFloat(0f, radius);

		float mag = (float) Math.sqrt(x1*x1 + x2*x2 + x3*x3);
		x1 /= mag; x2 /= mag; x3 /= mag;

		// Math.cbrt is cube root
		float c = (float) Math.cbrt(u);

		return new Vector3f(x1 * c, x2 * c, x3 * c);
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}

	private enum EnumMotionFormula
	{
		TORCH, UP, BEAM
	}*/
}
