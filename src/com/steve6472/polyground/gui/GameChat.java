package com.steve6472.polyground.gui;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.commands.CommandRegistry;
import com.steve6472.polyground.commands.CommandSource;
import com.steve6472.sge.gfx.font.Font;
import com.steve6472.sge.gui.components.TextField;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.MainApp;
import com.steve6472.sge.main.events.Event;
import com.steve6472.sge.main.events.KeyEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.11.2019
 * Project: SJP
 *
 ***********************/
public class GameChat extends TextField
{
	public TextLog textLog;

	@Override
	public void init(MainApp main)
	{
		super.init(main);

		textLog = new TextLog();
		textLog.setDisplayTime(5000);
		textLog.setFadeStart(3500);
	}

	@Override
	public void render()
	{
		renderText();

		if (!isFocused())
			textLog.render(x, getMain().getHeight() - textLog.getMaxDisplayed() * 10);

		if (!isFocused()) return;

		CommandRegistry registry = CaveGame.getInstance().commandRegistry;

		int y = getMain().getHeight() - 14;
		int i = 0;
		boolean shownError = false;

		ParseResults<CommandSource> parseResults = registry.dispatcher.parse(getText(), registry.commandSource);
		for (CommandSyntaxException value : parseResults.getExceptions().values())
		{
			shownError = true;
			Font.renderCustom(5, y - 10 - i * 10, 1, "[127,0,0]", value.getMessage());
			i++;
		}

		if (!getText().isBlank())
		{
			CompletableFuture<Suggestions> suggestions = registry.dispatcher.getCompletionSuggestions(parseResults, getCarretPosition());
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

		if (getText().isBlank())
		{
			SuggestionContext<CommandSource> suggestionContext = parseResults.getContext().findSuggestionContext(getCarretPosition());
			for (String value : registry.dispatcher.getSmartUsage(suggestionContext.parent, registry.commandSource).values())
			{
				Font.render(value, 5, y - (shownError ? 20 : 10) - i * 10);
				i++;
			}
		}
	}

	@Event
	public void executeCommand(KeyEvent e)
	{
		if (e.getAction() == KeyList.PRESS && (e.getKey() == KeyList.ENTER || e.getKey() == KeyList.KP_ENTER))
		{
			CaveGame pg = ((CaveGame) getMain());
			if (isFocused())
			{
				if (!getText().isBlank())
				{
					try
					{
						pg.commandRegistry.dispatcher.execute(getText(), pg.commandRegistry.commandSource);
					} catch (CommandSyntaxException ex)
					{
						textLog.addLine("[#FF5555]", ex.getMessage());
					}
				}
				setText("");
				loseFocus();
				setCarretPosition(0);
			} else
			{
				focus();
			}
		}
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

		textLog.tick();
	}

	public void addText(Object... text)
	{
		textLog.addLine(text);
	}
}
