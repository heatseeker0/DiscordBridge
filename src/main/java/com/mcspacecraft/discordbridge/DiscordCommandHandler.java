package com.mcspacecraft.discordbridge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DiscordCommandHandler implements CommandExecutor {
    private DiscordBridge plugin;

    public DiscordCommandHandler(DiscordBridge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 0) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                subCmdReload(sender);
                break;
        }

        return true;
    }

    private void subCmdReload(CommandSender sender) {
        if (sender.hasPermission("discord.reload")) {
            plugin.getDiscordConfig().load();
            sender.sendMessage(plugin.getDiscordConfig().getMessage("configuration-loaded"));
        }
    }
}
