package com.steve6472.polyground.gui;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.BlockLoader;
import com.steve6472.polyground.commands.CommandRegistry;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.polyground.events.InGameGuiEvent;
import com.steve6472.sge.gfx.SpriteRender;
import com.steve6472.sge.gfx.font.CustomChar;
import com.steve6472.sge.gfx.font.Font;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.components.TextField;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.MainApp;
import com.steve6472.sge.main.events.Event;
import com.steve6472.sge.main.events.KeyEvent;
import com.steve6472.sge.main.events.WindowSizeEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class InGameGui extends Gui implements IGamePause
{
	public InGameGui(MainApp mainApp)
	{
		super(mainApp);
	}

	public TextField commandBox;
	public ItemBar itemBar;

	@Override
	public void createGui()
	{
		commandBox = new TextField()
		{
			@Override
			public void render()
			{
				renderText();
			}

			@Override
			public void tick()
			{
				carretTick += 1;
				if (carretTick >= 60)
				{
					carretTick = 0;
					showCarret = !showCarret;
				}
			}
		};
		commandBox.setLocation(2, getMainApp().getHeight() - 14);
		commandBox.setSize((int) (getMainApp().getWidth() * .8), 14);
		addComponent(commandBox);

		itemBar = new ItemBar();
		addComponent(itemBar);
	}

	@Event
	public void executeCommand(KeyEvent e)
	{
		if (e.getAction() == KeyList.PRESS && e.getKey() == KeyList.ENTER || e.getKey() == KeyList.KP_ENTER)
		{
			CaveGame pg = ((CaveGame) getMainApp());
			if (commandBox.isFocused())
			{
				if (!commandBox.getText().isBlank())
				{
					try
					{
						pg.commandRegistry.dispatcher.execute(commandBox.getText(), pg.commandRegistry.commandSource);
					} catch (CommandSyntaxException ex)
					{
						ex.printStackTrace();
					}
				}
				commandBox.setText("");
				commandBox.loseFocus();
				commandBox.setCarretPosition(0);
			} else
			{
				commandBox.focus();
			}
		}
	}

	@Event
	public void updatePosition(WindowSizeEvent e)
	{
		commandBox.setLocation(2, e.getHeight() - 14);
	}

	@Override
	public void guiTick()
	{
		if (CaveGame.runGameEvent(new InGameGuiEvent.PreTick(this))) return;

		CaveGame.getInstance().options.isMouseFree = commandBox.isFocused();
		itemBar.setLocation(getMainApp().getWidth() - 38, getMainApp().getHeight() / 2 - (7 * 38) / 2);

		CaveGame.runGameEvent(new InGameGuiEvent.PostTick(this));
	}

	@Override
	public void render()
	{
		if (CaveGame.runGameEvent(new InGameGuiEvent.PreRender(this))) return;

		renderCommandBox();
		renderTheRest();

		renderCrosshair();

		runEvent(new InGameGuiEvent.PostRender(this));
	}

	private void renderCrosshair()
	{
		if (CaveGame.runGameEvent(new InGameGuiEvent.PreRenderCrosshair(this))) return;

		Font.renderCustom(getMainApp().getWidth() / 2 - 9, getMainApp().getHeight() / 2 - 9, 2, "[s0]", CustomChar.UNSELECTED_BOX);

		CaveGame.runGameEvent(new InGameGuiEvent.PostRenderCrosshair(this));
	}

	public void renderCommandBox()
	{
		if (!commandBox.isFocused()) return;

		CommandRegistry registry = CaveGame.getInstance().commandRegistry;

		int y = getMainApp().getHeight() - 14;
		int i = 0;
		boolean shownError = false;
		ParseResults<CommandSource> parseResults = registry.dispatcher.parse(commandBox.getText(), registry.commandSource);
		for (CommandSyntaxException value : parseResults.getExceptions().values())
		{
			shownError = true;
			Font.renderCustom(5, y - 10 - i * 10, 1, "[127,0,0]", value.getMessage());
			i++;
		}

		if (!commandBox.getText().isBlank())
		{
			CompletableFuture<Suggestions> suggestions = registry.dispatcher.getCompletionSuggestions(parseResults, commandBox.getCarretPosition());
			try
			{
				for (Suggestion suggestion : suggestions.get().getList())
				{
					Font.renderCustom(5, y - (shownError ? 20 : 10) - i * 10, 1, "[0,255,255]", suggestion.getText());
					i++;
				}
			} catch (InterruptedException | ExecutionException e)
			{
				e.printStackTrace();
			}
		}

		if (commandBox.getText().isBlank())
		{
			SuggestionContext<CommandSource> suggestionContext = parseResults.getContext().findSuggestionContext(commandBox.getCarretPosition());
			for (String value : registry.dispatcher.getSmartUsage(suggestionContext.parent, registry.commandSource).values())
			{
				Font.render(value, 5, y - (shownError ? 20 : 10) - i * 10);
				i++;
			}
		}
	}

	public void renderTheRest()
	{
		CaveGame main = CaveGame.getInstance();

		Font.renderFps(5, 5, main.getFps());
		Font.render("XYZ: " + main.getPlayer().getX() + " " + main.getPlayer().getY() + " " + main.getPlayer().getZ(), 5, 15);
		Font.render("XYZ: " + (int) Math.floor(main.getPlayer().getX()) + " " + (int) Math.floor(main.getPlayer().getY()) + " " + (int) Math.floor(main.getPlayer().getZ()), 5, 25);

		final int as = BlockLoader.getAtlas().getTotalSize();

		if (main.hitPicker.hit)
		{
			Font.render(5, 45,
				"Distance: " + main.hitPicker.getHitResult().getDistance() + "\n" +
					"Side: " + main.hitPicker.getHitResult().getFace() + "\n" +
					"Particles: " + main.particles.count());
			SpriteRender.renderSprite(0, 80, as, as, 0, BlockLoader.getAtlas().getSpriteId(), as, as);
		} else
		{
			Font.render("Particles: " + main.particles.count(), 5, 45);
			SpriteRender.renderSprite(0, 70, as, as, 0, BlockLoader.getAtlas().getSpriteId(), as, as);
		}

		int ts = CaveGame.getInstance().itemAtlas.totalSize;

		SpriteRender.renderSprite(getMainApp().getWidth() - ts, 15, ts, ts, 0, CaveGame.getInstance().itemAtlas.itemAtlas.texture, ts, ts);
	}

	@Override
	public boolean pauseGameIfOpen()
	{
		return false;
	}
}
