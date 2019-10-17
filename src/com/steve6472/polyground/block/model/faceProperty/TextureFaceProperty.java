package com.steve6472.polyground.block.model.faceProperty;

import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.10.2019
 * Project: SJP
 *
 ***********************/
public class TextureFaceProperty extends FaceProperty
{
	private boolean isReference;
	private String texture;
	private int textureId;

	public TextureFaceProperty()
	{
	}

	@Override
	public void loadFromJSON(JSONObject json)
	{
		texture = json.getString("texture");
		isReference = texture.startsWith("#");
	}

	@Override
	public void saveToJSON(JSONObject faceJson)
	{
		faceJson.put("texture", texture);
	}

	public boolean isReference()
	{
		return isReference;
	}

	public String getTexture()
	{
		return texture;
	}

	public void setTexture(String texture)
	{
		this.texture = texture;
	}

	public int getTextureId()
	{
		return textureId;
	}

	public void setTextureId(int textureId)
	{
		this.textureId = textureId;
	}

	public boolean isBlank()
	{
		return texture == null || texture.isBlank();
	}

	@Override
	public String getId()
	{
		return "texture";
	}

	@Override
	public TextureFaceProperty createCopy()
	{
		TextureFaceProperty o = new TextureFaceProperty();
		o.isReference = isReference;
		o.texture = texture;
		o.textureId = textureId;

		return o;
	}
}
