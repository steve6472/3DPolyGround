package com.steve6472.polyground.block.model.faceProperty;

import com.steve6472.polyground.block.model.registry.face.FaceEntry;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.11.2019
 * Project: SJP
 *
 ***********************/
public class BiomeTintFaceProperty extends AbstractBooleanProperty
{
	public BiomeTintFaceProperty()
	{
	}

	public BiomeTintFaceProperty(boolean flag)
	{
		setFlag(flag);
	}

	@Override
	protected FaceEntry<? extends AbstractBooleanProperty> getEntry()
	{
		return FaceRegistry.biomeTint;
	}

	@Override
	public FaceProperty createCopy()
	{
		return new BiomeTintFaceProperty(isFlag());
	}

	@Override
	public String getId()
	{
		return "biomeTint";
	}
}
