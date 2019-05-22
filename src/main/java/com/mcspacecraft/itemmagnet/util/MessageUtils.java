package com.mcspacecraft.itemmagnet.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

/**
 * Misc helper functions for message parsing and colors
 *
 * @author Catalin Ionescu <cionescu@gmail.com>
 */
public class MessageUtils {
    static public String parseColors(String msg) {
        return msg != null ? ChatColor.translateAlternateColorCodes('&', msg) : "";
    }

    static public List<String> parseColors(List<String> msgs) {
        if (msgs == null) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>(msgs.size());

        for (String msg : msgs) {
            result.add(ChatColor.translateAlternateColorCodes('&', msg));
        }

        return result;
    }
}