package com.mcspacecraft.discordbridge;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RequestBuffer;

public class DiscordMsgBridge implements Listener {
    private final DiscordBridge plugin;

    public DiscordMsgBridge(final DiscordBridge plugin) {
        this.plugin = plugin;
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IUser user = event.getAuthor();
        IMessage message = event.getMessage();

        String guildID = plugin.getDiscordConfig().getGuildId();

        String chatFormat = plugin.getDiscordConfig().getMcChatFormat();

        String msg = com.mcspacecraft.itemmagnet.util.MessageUtils.parseColors(chatFormat.replace("{USER}", user.getName()).replace("{MESSAGE}", message.toString().replace(String.valueOf(user.getLongID()),
            user.getName())).replace("{ID}", user.getDiscriminator()).replace("{NICK}", user.getNicknameForGuild(DiscordBridge.getDiscordClient().getGuildByID(Long.valueOf(
                guildID).longValue())) == null ? "" : user.getNicknameForGuild(DiscordBridge.getDiscordClient().getGuildByID(Long.valueOf(guildID).longValue()))));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(msg);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMinecraftChatMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        String guildId = plugin.getDiscordConfig().getGuildId();

        String chatFormat = plugin.getDiscordConfig().getDiscordChatFormat();
        String msg = ChatColor.stripColor(chatFormat.replace("{PLAYER}", player.getName()).replace("{DISPLAYNAME}", player.getDisplayName()).replace("{MESSAGE}", message));

        IGuild guild = DiscordBridge.getDiscordClient().getGuildByID(Long.valueOf(guildId));
        if (guild == null) {
            // DiscordBridge.getPlugin().logErrorMessage("Null guild!");
            return;
        }

        List<IChannel> channels = guild.getChannels();
        String sendChannel = plugin.getDiscordConfig().getDiscordChatChannels().get(0);
        // plugin.logInfoMessage("%d channels available, sending on %s", channels.size(), sendChannel);

        RequestBuffer.request(() -> {
            try {
                for (IChannel channel : channels) {
                    // plugin.logInfoMessage("Trying channel: %s", channel.getName());
                    if (channel.getName().equalsIgnoreCase(sendChannel)) {
                        channel.sendMessage(msg);
                    }
                }
            } catch (DiscordException e) {
                plugin.logErrorMessage("Message could not be sent with error: %s", e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
