package steve6472.polyground.gfx.model;

import java.util.Objects;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public final class Element extends OutlinerElement
{
	public float fromX, fromY, fromZ;
	public float toX, toY, toZ;
	public Face north, east, south, west, up, down;

	public static final class Face
	{
		private float u0;
		private float v0;
		private float u1;
		private float v1;
		private final int texture;

		public Face(float u0, float v0, float u1, float v1, int texture)
		{
			this.u0 = u0;
			this.v0 = v0;
			this.u1 = u1;
			this.v1 = v1;
			this.texture = texture;
		}

		public float getU0()
		{
			return u0;
		}

		public float getV0()
		{
			return v0;
		}

		public float getU1()
		{
			return u1;
		}

		public float getV1()
		{
			return v1;
		}

		public void setU0(float u0)
		{
			this.u0 = u0;
		}

		public void setV0(float v0)
		{
			this.v0 = v0;
		}

		public void setU1(float u1)
		{
			this.u1 = u1;
		}

		public void setV1(float v1)
		{
			this.v1 = v1;
		}

		public int texture()
		{
			return texture;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == this)
				return true;
			if (obj == null || obj.getClass() != this.getClass())
				return false;
			var that = (Face) obj;
			return Float.floatToIntBits(this.u0) == Float.floatToIntBits(that.u0) && Float.floatToIntBits(this.v0) == Float
				.floatToIntBits(that.v0) && Float.floatToIntBits(this.u1) == Float.floatToIntBits(that.u1) && Float.floatToIntBits(this.v1) == Float
				.floatToIntBits(that.v1) && this.texture == that.texture;
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(u0, v0, u1, v1, texture);
		}

		@Override
		public String toString()
		{
			return "Face[" + "u0=" + u0 + ", " + "v0=" + v0 + ", " + "u1=" + u1 + ", " + "v1=" + v1 + ", " + "texture=" + texture + ']';
		}
	}
}
