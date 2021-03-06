package steve6472.polyground.gfx.shaders;

import org.joml.Matrix4f;
import steve6472.polyground.PolyUtil;
import steve6472.polyground.gfx.light.LightManager;
import steve6472.polyground.gfx.shaders.particles.BasicParticleShader;
import steve6472.polyground.gfx.shaders.particles.BreakParticleShader;
import steve6472.polyground.gfx.shaders.particles.FlatParticleShader;
import steve6472.polyground.gfx.shaders.world.FlatTexturedShader;
import steve6472.polyground.gfx.shaders.world.WorldShader;
import steve6472.sge.gfx.shaders.AbstractDeferredShader;
import steve6472.sge.gfx.shaders.DialogShader;
import steve6472.sge.gfx.shaders.Shader;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.WindowSizeEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.07.2019
 * Project: SJP
 *
 ***********************/
public class Shaders
{
	private Matrix4f projectionMatrix;

	public BasicParticleShader basicParticleShader;
	public FlatParticleShader flatParticleShader;

	public FlatTexturedShader flatTexturedShader;
	public WorldShader worldShader;
	public BreakParticleShader breakParticleShader;
	public DebugShader debugShader;
	public RiftShader riftShader;
	public WaterShader waterShader;
	public DialogShader dialogShader;
	public EntityShader entityShader;
	public Color2DShader color2DShader;

	public CGGShader gShader;
	public AbstractDeferredShader<LightUniform> deferredShader;

	public MainShader mainShader;

	public Shaders()
	{
		projectionMatrix = new Matrix4f();

		initShaders();
	}

	private void initShaders()
	{
		basicParticleShader = new BasicParticleShader();
		flatParticleShader = new FlatParticleShader();

		flatTexturedShader = new FlatTexturedShader();
		worldShader = new WorldShader();
		breakParticleShader = new BreakParticleShader();
		debugShader = new DebugShader();
		riftShader = new RiftShader();
		waterShader = new WaterShader();
		dialogShader = new DialogShader();
		color2DShader = new Color2DShader();

		gShader = new CGGShader("deferred/g");

		deferredShader = new CGDeferredShader();
		deferredShader.createLights(() -> new LightUniform[LightManager.LIGHT_COUNT], LightUniform::new);

		entityShader = new EntityShader();
		entityShader.createLights(() -> new LightUniform[LightManager.LIGHT_COUNT], LightUniform::new);

		mainShader = new MainShader();
	}

	@Event
	public void windowResize(WindowSizeEvent event)
	{
		projectionMatrix = PolyUtil.createProjectionMatrix(event.getWidth(), event.getHeight());

		basicParticleShader.getShader().bind();
		basicParticleShader.setProjection(projectionMatrix);

		mainShader.getShader().bind();
		mainShader.setProjection(projectionMatrix);

		flatParticleShader.getShader().bind();
		flatParticleShader.setProjection(projectionMatrix);

		flatTexturedShader.bind();
		flatTexturedShader.setProjection(projectionMatrix);

		worldShader.bind();
		worldShader.setProjection(projectionMatrix);
		worldShader.setUniform(WorldShader.ATLAS, 0);

		dialogShader.bind();
		dialogShader.setProjection(projectionMatrix);
		dialogShader.setUniform(DialogShader.SAMPLER, 0);

		breakParticleShader.bind();
		breakParticleShader.setProjection(projectionMatrix);

		Matrix4f ortho = new Matrix4f().ortho(0, event.getWidth(), event.getHeight(), 0, 1, -1);

		color2DShader.bind();
		color2DShader.setProjection(ortho);

		debugShader.bind();
		debugShader.setProjection(projectionMatrix);

		riftShader.bind();
		riftShader.setProjection(projectionMatrix);
		riftShader.setUniform(RiftShader.TEXTURE, 0);

		gShader.bind();
		gShader.setProjection(projectionMatrix);
		gShader.setUniform(CGGShader.ALBEDO, 0);

		deferredShader.bind();
		deferredShader.setProjection(projectionMatrix);
		deferredShader.setUniform(CGDeferredShader.albedo, 0);
		deferredShader.setUniform(CGDeferredShader.position, 1);
		deferredShader.setUniform(CGDeferredShader.normal, 2);
		deferredShader.setUniform(CGDeferredShader.emission, 3);
		deferredShader.setUniform(CGDeferredShader.emissionPos, 4);

		entityShader.bind();
		entityShader.setProjection(projectionMatrix);
		entityShader.setUniform(EntityShader.ATLAS, 0);

		waterShader.getShader().bind();
		waterShader.setProjection(ortho);
		waterShader.setTransformation(new Matrix4f());
		waterShader.setUniform(WaterShader.TEXTURE, 0);

		Shader.releaseShader();
	}

	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}
}
