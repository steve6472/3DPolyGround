package steve6472.polyground.block.model.faceProperty.condition;

enum Type
{
	EQUALS("=="), NOT_EQUALS("!="), OR("||"), AND("&&"), NOT_FOUND("");

	String raw;

	Type(String raw)
	{
		this.raw = raw;
	}
}