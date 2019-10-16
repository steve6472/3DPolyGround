package com.steve6472.polyground.block.model;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.model.faceProperty.FaceProperty;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.face.FaceEntry;
import com.steve6472.polyground.block.model.registry.face.FaceRegistry;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.08.2019
 * Project: SJP
 *
 ***********************/
public class CubeFace
{
	private Cube parent;
	private EnumFace face;

	private List<FaceProperty> properties;

	private float shade;
	private boolean emissive = false;

	public CubeFace(Cube parent, EnumFace face)
	{
		this.parent = parent;
		this.face = face;
		properties = new ArrayList<>();

		shade = switch (face)
		{
			case WEST, EAST -> 0.8f;
			case SOUTH, NORTH -> 0.6f;
			case UP, NONE -> 1.0f;
			case DOWN -> 0.5f;
		};
	}

	public CubeFace(Cube parent, EnumFace face, List<FaceProperty> properties)
	{
		this.parent = parent;
		this.face = face;
		this.properties = properties;

		shade = switch (face)
			{
				case WEST, EAST -> 0.8f;
				case SOUTH, NORTH -> 0.6f;
				case UP, NONE -> 1.0f;
				case DOWN -> 0.5f;
			};
	}

	public void loadFromJSON(JSONObject faceJson)
	{
		for (String key : faceJson.keySet())
		{
			if (FaceRegistry.contains(key))
			{
				FaceProperty newProperty = FaceRegistry.createProperty(key);
				newProperty.loadFromJSON(faceJson);
				addProperty(newProperty);
			} else
			{
				throw new IllegalArgumentException(key + " is not valid key!");
			}
		}
	}

	public boolean hasProperty(String id)
	{
		for (FaceProperty fp : properties)
		{
			if (fp.getId().equals(id))
				return true;
		}

		return false;
	}

	public boolean hasProperty(FaceEntry property)
	{
		return hasProperty(property.getInstance().getId());
	}

	public <T extends FaceProperty> T getProperty(FaceEntry<T> a)
	{
		for (FaceProperty fp : properties)
		{
			if (fp.getId().equals(a.getInstance().getId()))
			{
				//noinspection unchecked
				return (T) fp;
			}
		}

		return null;
	}

	public List<FaceProperty> copyProperties()
	{
		List<FaceProperty> newList = new ArrayList<>();
		for (FaceProperty fp : properties)
		{
			newList.add(fp.createCopy());
		}

		return newList;
	}

	public void addProperty(FaceProperty property)
	{
		properties.add(property);
	}

	public EnumFace getFace()
	{
		return face;
	}

	public void setFace(EnumFace face)
	{
		this.face = face;
	}

	public float getShade()
	{
		return shade;
	}

	public void setShade(float shade)
	{
		this.shade = shade;
	}

	public Cube getParent()
	{
		return parent;
	}

	@Override
	public String toString()
	{
		return "CubeFace{" + "face=" + face + ", shade=" + shade + ", emissive=" + emissive + '}';
	}

	public boolean isEmissive()
	{
		return emissive;
	}

	public void setEmissive(boolean emissive)
	{
		this.emissive = emissive;
	}
}
