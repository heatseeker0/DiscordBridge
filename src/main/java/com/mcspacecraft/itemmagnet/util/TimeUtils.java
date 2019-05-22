package com.mcspacecraft.itemmagnet.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;

/**
 * Collection of helper functions for day / night management
 *
 * @author Catalin Ionescu
 */
public class TimeUtils {
    /**
     * Stops day / night cycle in specified world.
     *
     * @param world World to stop day / night cycle for
     */
    public static void stopTimeFlow(final World world) {
        if (world != null) {
            world.setGameRuleValue("doDaylightCycle", "false");
        }
    }

    /**
     * Stops day / night cycle in all Overworld type worlds.
     */
    public static void stopTimeFlow() {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getEnvironment() == Environment.NORMAL) {
                stopTimeFlow(world);
            }
        }
    }

    /**
     * Starts day / night cycle in specified world.
     *
     * @param world World to start day / night cycle for
     */
    public static void startTimeFlow(final World world) {
        if (world != null) {
            world.setGameRuleValue("doDaylightCycle", "true");
        }
    }

    /**
     * Starts day / night cycle in all Overworld type worlds.
     */
    public static void startTimeFlow() {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getEnvironment() == Environment.NORMAL) {
                startTimeFlow(world);
            }
        }
    }

    /**
     * Sets the time to 12:00 PM in a specific Overworld world.
     *
     * @param world World to set time to mid-day
     */
    public static void setTimeMidDay(final World world) {
        if (world != null) {
            world.setTime(8000L);
        }
    }

    /**
     * Sets the time to 12:00 PM in all Overworld worlds.
     */
    public static void setTimeMidDay() {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getEnvironment() == Environment.NORMAL) {
                setTimeMidDay(world);
            }
        }
    }

    /**
     * Sets the time to 12:00 AM in a specific Overworld world.
     *
     * @param world World to set time to midnight
     */
    public static void setTimeMidnight(final World world) {
        if (world != null) {
            world.setTime(17000L);
        }
    }

    /**
     * Sets the time to 12:00 AM in all Overworld worlds.
     */
    public static void setTimeMidnight() {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getEnvironment() == Environment.NORMAL) {
                setTimeMidnight(world);
            }
        }
    }

    public static String formatDateDiff(long date) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c);
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return "now";
        }
        if (toDate.after(fromDate)) {
            future = true;
        }

        StringBuilder sb = new StringBuilder();
        int[] types = new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND };
        String[] names = new String[] { "year", "years", "month", "months", "day", "days", "hour", "hours", "minute", "minutes", "second", "seconds" };
        int accuracy = 0;
        for (int i = 0; i < types.length; i++) {
            if (accuracy > 2) {
                break;
            }
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                accuracy++;
                sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
            }
        }
        if (sb.length() == 0) {
            return "now";
        }
        return sb.toString().trim();
    }

    private static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }
}
