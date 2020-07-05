package steve6472.polyground.registry.face;

import steve6472.polyground.block.model.faceProperty.*;
import steve6472.polyground.block.model.faceProperty.condition.AndChainCondProperty;
import steve6472.polyground.block.model.faceProperty.condition.CondProperty;
import steve6472.polyground.block.model.faceProperty.condition.ConditionFaceProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class FaceRegistry
{
	private static final HashMap<String, FaceEntry<? extends FaceProperty>> faceRegistry = new HashMap<>();

	public static final FaceEntry<TextureFaceProperty> texture = register("texture", TextureFaceProperty::new);
	public static final FaceEntry<AutoUVFaceProperty> autoUv = register("autoUV", AutoUVFaceProperty::new);
	public static final FaceEntry<VisibleFaceProperty> isVisible = register("isVisible", VisibleFaceProperty::new);
	public static final FaceEntry<UVFaceProperty> uv = register("uv", UVFaceProperty::new);
	public static final FaceEntry<TintFaceProperty> tint = register("tint", TintFaceProperty::new);
	public static final FaceEntry<RefTintFaceProperty> refTint = register("refTint", RefTintFaceProperty::new);
	public static final FaceEntry<RotationFaceProperty> rotation = register("rotation", RotationFaceProperty::new);
	public static final FaceEntry<LayerFaceProperty> modelLayer = register("modelLayer", LayerFaceProperty::new);
	public static final FaceEntry<BiomeTintFaceProperty> biomeTint = register("biomeTint", BiomeTintFaceProperty::new);
	public static final FaceEntry<UVLockFaceProperty> uvlock = register("uvlock", UVLockFaceProperty::new);
	public static final FaceEntry<LightFaceProperty> light = register("light", LightFaceProperty::new); // Runtime added property!

	/* Conditioned Properties */
	public static final FaceEntry<ConditionFaceProperty> conditionedTexture = register("conditionedTexture", ConditionFaceProperty::new);
	public static final FaceEntry<CondProperty> condition = register("condition", CondProperty::new);
	public static final FaceEntry<AndChainCondProperty> andChain = register("andChain", AndChainCondProperty::new);

	public static <T extends FaceProperty> FaceEntry<T> register(String id, IFaceFactory<T> factory)
	{
		FaceEntry<T> entry = new FaceEntry<>(factory);
		faceRegistry.put(id, entry);
		return entry;
	}

	public static FaceProperty createProperty(String id)
	{
		return faceRegistry.get(id).createNew();
	}

	public static boolean contains(String id)
	{
		return faceRegistry.containsKey(id);
	}

	public static Collection<FaceEntry<? extends FaceProperty>> getEntries()
	{
		return faceRegistry.values();
	}

	public static FaceEntry<? extends FaceProperty> getEntry(String id)
	{
		return faceRegistry.get(id);
	}

	public static Set<String> getKeys()
	{
		return faceRegistry.keySet();
	}
}
