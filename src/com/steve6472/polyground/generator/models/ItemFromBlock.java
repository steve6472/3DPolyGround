package com.steve6472.polyground.generator.models;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemFromBlock implements IModel
{
	private static String itemFromBlockJson = "{\"type\": \"from_block\",\"block\": \"BLOCK_HERE\"}";

	private String block;

	public ItemFromBlock(String block)
	{
		this.block = block;
	}

	@Override
	public String build()
	{
		return itemFromBlockJson.replace("BLOCK_HERE", block);
	}
}
