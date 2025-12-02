package com.uwuScape;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("uwuScape")
public interface uwuScapeConfig extends Config
{
	@ConfigSection(
			name = "UwUify Text",
			description = "Settings for Text uwuification",
			position = 0
	)
	String uwuify = "uwuify";

	@ConfigItem(
		keyName = "uwuSpeak",
		name = "UwU Speak",
		description = "Whether to replace all text with partial or full uwu speak",
		section = uwuify,
		position = 1
	)
	default OwoifyMode owoifyMode()
	{
		return OwoifyMode.FULL;
	}
	@ConfigItem(
			keyName = "convertChat",
			name = "Convert Chat Messages",
			description = "Whether to uwuify people's chat messages",
			section = uwuify,
			position = 2
	)
	default boolean convertChat()
	{
		return true;
	}
	@ConfigItem(
			keyName = "randomPrefix",
			name = "Random Thematic Prefixes",
			description = "Whether to randomly add thematic prefixes to messages",
			section = uwuify,
			position = 3
	)
	default boolean randomPrefix()
	{
		return true;
	}
	@ConfigItem(
			keyName = "randomEmotes",
			name = "Random Cute Emotes",
			description = "Whether to randomly add cute emotes to the ends of messages",
			section = uwuify,
			position = 4
	)
	default boolean randomEmotes()
	{
		return true;
	}
/*    @ConfigItem(
            keyName = "convertRightClick",
            name = "Convert Right click Options",
            description = "Whether to convert right click options",
            section = uwuify,
            position = 5
    )
    default boolean convertRightClick() { return true; }*/

}

