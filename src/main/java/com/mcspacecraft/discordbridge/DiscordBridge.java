package com.mcspacecraft.discordbridge;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

public class DiscordBridge extends JavaPlugin {
    private static DiscordBridge plugin;
    public static final Logger logger = Logger.getLogger("Minecraft.DiscordBridge");

    private DiscordConfig config;
    private static IDiscordClient discordClient;

    @Override
    public void onEnable() {
        plugin = this;

        config = new DiscordConfig(this);
        config.load();

        if (config.getBotToken() == null || config.getBotToken().equalsIgnoreCase("")) {
            logErrorMessage("================================");
            logErrorMessage("You have not set your bot token");
            logErrorMessage("in the config file, please do that");
            logErrorMessage("before I can continue. Bye bye for now...");
            logErrorMessage("================================");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        discordClient = new ClientBuilder().withToken(config.getBotToken()).build();
        discordClient.login();

        DiscordMsgBridge discordMsgBridge = new DiscordMsgBridge(this);
        EventDispatcher dispatcher = discordClient.getDispatcher();
        dispatcher.registerListener(discordMsgBridge);
        getServer().getPluginManager().registerEvents(discordMsgBridge, this);

        getCommand("discordbridge").setExecutor(new DiscordCommandHandler(this));
    }

    public static DiscordBridge getPlugin() {
        return plugin;
    }

    public DiscordConfig getDiscordConfig() {
        return config;
    }

    public static IDiscordClient getDiscordClient() {
        return discordClient;
    }

    public void logInfoMessage(final String msg, final Object... args) {
        if (args == null || args.length == 0) {
            logger.info(String.format("[%s] %s", getDescription().getName(), msg));
        } else {
            logger.info(String.format("[%s] %s", getDescription().getName(), String.format(msg, args)));
        }
    }

    public void logErrorMessage(final String msg, final Object... args) {
        if (args == null || args.length == 0) {
            logger.severe(String.format("[%s] %s", getDescription().getName(), msg));
        } else {
            logger.severe(String.format("[%s] %s", getDescription().getName(), String.format(msg, args)));
        }
    }
}