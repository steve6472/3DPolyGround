package steve6472.polyground.item.groups;

import steve6472.sge.main.MainApp;

public interface IGroupWheelData
{
	float red();
	float green();
	float blue();

	String name();
	String id();
	String preview();

	PreviewType previewType();

	void renderPreview(MainApp mainApp, float x, float y);
}