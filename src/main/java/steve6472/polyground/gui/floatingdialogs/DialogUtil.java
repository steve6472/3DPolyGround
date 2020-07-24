package steve6472.polyground.gui.floatingdialogs;

import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gui.components.ComponentRender;
import steve6472.sge.gui.components.schemes.SchemeBackground;
import steve6472.sge.gui.floatingdialog.FloatingDialog;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.07.2020
 * Project: CaveGame
 *
 ***********************/
public class DialogUtil
{
	public static void createDialogBackground(FloatingDialog dialog)
	{
		SchemeBackground scheme = (SchemeBackground) MainApp.getSchemeRegistry().getDefaultScheme(SchemeBackground.class);
		dialog.addComponent(new ComponentRender(() -> {
			SpriteRender.renderDoubleBorder(0, 0, dialog.getWidth(), dialog.getHeight(), scheme.outsideBorder, scheme.insideBorder, scheme.fill);
		}));
	}

	public static void renderCursor(MainApp main)
	{
		SpriteRender.drawCircle(main.getMouseX(), main.getMouseY(), 2f, 1, 1, 1, 1);
	}
}
