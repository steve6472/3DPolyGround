package steve6472.polyground;

import org.joml.AABBf;
import org.joml.Vector4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 16.08.2019
 * Project: SJP
 *
 ***********************/
public class ParticleHitbox
{
	private AABBf hitbox;
	private float size;

	private Vector4f color;

	public ParticleHitbox(float x, float y, float z, float size, Vector4f color)
	{
		this.size = size;
		this.color = color;
		hitbox = new AABBf(x - size, y - size, z - size, x + size, y + size, z + size);
	}

	public AABBf getHitbox()
	{
		return hitbox;
	}

	public void tick(CaveGame pg)
	{
		pg.particles.addBasicTickParticle(hitbox.minX + size, hitbox.minY + size, hitbox.minZ + size, size, color.x, color.y, color.z, color.w);
	}
}
