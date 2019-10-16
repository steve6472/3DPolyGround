package com.steve6472.polyground.entity.model;

import com.steve6472.polyground.EnumFace;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2019
 * Project: SJP
 *
 ***********************/
public class EntityModelFace
{
	private ModelPart parent;
	private EnumFace face;

	private float minU;
	private float minV;
	private float maxU;
	private float maxV;
	private float shade;

	public EntityModelFace(ModelPart parent, EnumFace face)
	{
		this.parent = parent;
		this.face = face;
		setUV(0, 0, 1, 1);
		shade = switch (face)
		{
			case WEST, EAST -> 0.7921568627450980392156862745098f;
			case SOUTH, NORTH -> 0.5921568627450980392156862745098f;
			case UP -> 0.98823529411764705882352941176471f;
			case DOWN -> 0.49019607843137254901960784313725f;
			case NONE -> 1.0f;
		};
	}

	public float getShade()
	{
		return shade;
	}

	public void setShade(float shade)
	{
		this.shade = shade;
	}

	public void setUV(float minU, float minV, float maxU, float maxV)
	{
		this.minU = minU;
		this.minV = minV;
		this.maxU = maxU;
		this.maxV = maxV;
	}

	public float getMinU()
	{
		return minU;
	}

	public float getMinV()
	{
		return minV;
	}

	public float getMaxU()
	{
		return maxU;
	}

	public float getMaxV()
	{
		return maxV;
	}

	@Override
	public String toString()
	{
		return "EntityCubeFace{" + "parent=" + parent + ", face=" + face + ", minU=" + minU + ", minV=" + minV + ", maxU=" + maxU + ", maxV=" + maxV + ", shade=" + shade + '}';
	}
}
