package steve6472.polyground.generator.creator.components;

public enum EnumSizeType
{
	BIG(40, 40, 64, 64, 32, 32), SMALL(20, 20, 32, 32, 16, 16);

	int w;
	int h;
	int bw;
	int bh;
	int iw;
	int ih;

	EnumSizeType(int w, int h, int bw, int bh, int iw, int ih)
	{
		this.w = w;
		this.h = h;
		this.bw = bw;
		this.bh = bh;
		this.iw = iw;
		this.ih = ih;
	}
}