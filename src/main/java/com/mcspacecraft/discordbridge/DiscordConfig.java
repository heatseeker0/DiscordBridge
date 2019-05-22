package com.mcspacecraft.discordbridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.mcspacecraft.itemmagnet.util.MessageUtils;

public class DiscordConfig {
    private final DiscordBridge plugin;
    private FileConfiguration config;

    private boolean debug = false;
    private String botToken;
    private String guildId;
    private String discordChatFormat;
    private String mcChatFormat;
    private List<String> discordChatChannels = new ArrayList<>();

    private Map<String, String> messages = new HashMap<>();

    public DiscordConfig(DiscordBridge plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        applyConfig();
    }

    private void applyConfig() {
        plugin.reloadConfig();

        config = plugin.getConfig();
        debug = config.getBoolean("debug", false);
        botToken = config.getString("bot-token");
        guildId = config.getString("guild-id");
        discordChatFormat = config.getString("discord.chat-format");
        mcChatFormat = config.getString("minecraft.chat-format");
        discordChatChannels = config.getStringList("discord.minecraft-chat-channels");

        messages.clear();
        for (String msgKey : config.getConfigurationSection("messages").getKeys(false)) {
            messages.put(msgKey, MessageUtils.parseColors(config.getString("messages." + msgKey)));
        }
    }

    public boolean getDebug() {
        return debug;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getDiscordChatFormat() {
        return discordChatFormat;
    }

    public List<String> getDiscordChatChannels() {
        return discordChatChannels;
    }

    public String getMcChatFormat() {
        return mcChatFormat;
    }

    public FileConfiguration getRawConfig() {
        return config;
    }

    public String getMessage(final String key) {
        if (messages.containsKey(key)) {
            return messages.get(key);
        }
        final String errorMsg = "No message text in config.yml for " + key;
        plugin.logErrorMessage(errorMsg);
        return errorMsg;
    }
}
