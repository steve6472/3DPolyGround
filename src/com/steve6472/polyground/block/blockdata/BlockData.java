package com.steve6472.polyground.block.blockdata;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public abstract class BlockData
{
	public abstract void write(DataOutputStream output) throws IOException;

	public abstract void read(DataInputStream input) throws IOException;
}
