package com.uwuScape;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import java.io.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOpened;

@Slf4j
@PluginDescriptor(
	name = "uwuScape"
)
public class uwuScapePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private uwuScapeConfig config;

	@Inject
	private ClientThread clientThread;

	@Override
	protected void startUp() throws Exception
	{
		log.info("uwuScape started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("uwuScape stopped!");
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e) {
		if(e.getGroupId() == WidgetID.DIALOG_NPC_GROUP_ID) {
			Widget widget = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT.getPackedId());
			clientThread.invokeLater(() -> {
				//System.out.println("Raw input: [" + widget.getText() + "]");

				String text = Owoify.convert(widget.getText(), config);
				widget.setText(text);
			});
		}
		else if(e.getGroupId() == WidgetID.DIALOG_PLAYER_GROUP_ID) {
			Widget widget = client.getWidget(WidgetInfo.DIALOG_PLAYER_TEXT.getPackedId());
			clientThread.invokeLater(() -> {
				String text = Owoify.convert(widget.getText(), config);
				widget.setText(text);
			});
		}
        else if (e.getGroupId() == WidgetID.DIALOG_OPTION_GROUP_ID)
        {
            clientThread.invokeLater(() ->
            {
                Widget parent = client.getWidget(WidgetID.DIALOG_OPTION_GROUP_ID, 1); // main container
                if (parent == null) return;

                for (Widget child : parent.getChildren())
                {
                    if (child.getText() != null)
                    {
                        String converted = Owoify.convert(child.getText(), config);
                        child.setText(converted);
                    }
                }
            });
        }
    }

/*    @Subscribe(priority = 100)
    public void onMenuEntryAdded(MenuEntryAdded e)
    {
        if (!config.convertRightClick()) return;

        MenuEntry entry = e.getMenuEntry();
        if (entry == null) return;

        // Skip left-click Walk here
        if (entry.getType() == MenuAction.WALK) return;

        // Convert option and target
        if (entry.getOption() != null)
            entry.setOption(Owoify.convertMenuText(entry.getOption(), config));
        if (entry.getTarget() != null)
            entry.setTarget(Owoify.convertMenuText(entry.getTarget(), config));
    }

    // --- shift/right-click handled when the menu fully opens ---
    @Subscribe(priority = -10)
    public void onMenuOpened(MenuOpened e)
    {
        if (!config.convertRightClick()) return;

        MenuEntry[] entries = e.getMenuEntries();
        if (entries == null || entries.length == 0) return;

        for (MenuEntry entry : entries)
        {
            if (entry == null) continue;

            if (entry.getOption() != null)
                entry.setOption(Owoify.convertMenuText(entry.getOption(), config));

            if (entry.getTarget() != null)
                entry.setTarget(Owoify.convertMenuText(entry.getTarget(), config));
        }

        client.setMenuEntries(entries);
    }*/


    @Subscribe
	public void onChatMessage(ChatMessage e) throws IOException {
		if(e.getType().equals(ChatMessageType.DIALOG)) {
			return;
		}
		else if (config.convertChat()){
			e.getMessageNode().setValue(Owoify.convert(e.getMessage(), config));
		}
	}

	@Subscribe
	public void onOverheadTextChanged(OverheadTextChanged e) {
		String text = Owoify.convert(e.getOverheadText(), config);
		e.getActor().setOverheadText(text);
		e.getActor().setOverheadCycle(30);
	}

	@Provides
	uwuScapeConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(uwuScapeConfig.class);
	}
}
