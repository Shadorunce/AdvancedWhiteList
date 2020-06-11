package com.gmail.shadoruncegaming.advancedwhitelist;

import java.util.ArrayList;
import org.bukkit.configuration.file.FileConfiguration;


public class WLStorage {
	static AdvancedWhiteList m;
	private static String configVersion;
	private static ArrayList<String> whitelists = new ArrayList<String>();
	public static boolean WhitelistEnabled = false;
	public static boolean ProjectTeamAccessEnabled = false;
	public static boolean StaffAccessEnabled = false;
	public static boolean TesterAccessEnabled = false;
	public static boolean AlternateAccessEnabled = false;
	public static boolean OtherAccessEnabled = false;
	public static boolean ConfigAccessEnabled = false;
	public static boolean ServerCooldownEnabled = true;
	public static long ServerCooldownDuration = 60; // In seconds based on from start of plugin.
	public static long delayBeforeStartingKicks = 4; // In seconds.
	public static long kickDelayPerPlayer = 1; // In seconds.
	public static String hubServer = "lobby";
	public static String notwhitelistmsg = "&eSorry, the server is currently in Whitelist mode, please enjoy one of our other servers. :)";
	public static String broadcastmsg = "&eSorry, the server is currently in Whitelist mode, please enjoy one of our other servers. :)";
	public static String sendmsg = "&eSorry, the server is currently in Whitelist mode, please enjoy one of our other servers. :)";
	public static String kickmsg = "&eSorry, the server is currently in Whitelist mode, please enjoy one of our other servers. :)";

	public WLStorage(AdvancedWhiteList m) {
        WLStorage.m = m;
    }

	public static void reload() {
		m.reloadConfig();
		FileConfiguration config = WLStorage.m.getConfig();
		WLStorage.configVersion = config.getString("config_version");
		WLStorage.whitelists = new ArrayList<String>(config.getStringList("whitelisted"));
		WLStorage.WhitelistEnabled = config.getBoolean("whitelist_enabled");
		WLStorage.ServerCooldownEnabled = config.getBoolean("server_cooldown");
		WLStorage.ServerCooldownDuration = config.getInt("server_cooldown_duration");
		WLStorage.ProjectTeamAccessEnabled = config.getBoolean("ProjectTeam_Access");
		WLStorage.StaffAccessEnabled = config.getBoolean("Staff_Access");
		WLStorage.TesterAccessEnabled = config.getBoolean("Tester_Access");
		WLStorage.AlternateAccessEnabled = config.getBoolean("Alternate_Access");
		WLStorage.OtherAccessEnabled = config.getBoolean("Other_Access");
		WLStorage.ConfigAccessEnabled = config.getBoolean("Config_Access");
		WLStorage.notwhitelistmsg = Utility.TransColor(config.getString("not_whitelisted_message"));
		WLStorage.hubServer = Utility.TransColor(config.getString("server_to_send_to"));
		WLStorage.broadcastmsg = Utility.TransColor(config.getString("send_or_kick_broadcast_message"));
		WLStorage.delayBeforeStartingKicks = config.getInt("delay_before_starting_kicks");
		WLStorage.kickDelayPerPlayer = config.getInt("kick_delay_per_player");
		WLStorage.sendmsg = Utility.TransColor(config.getString("send_message"));
		WLStorage.kickmsg = Utility.TransColor(config.getString("kick_message"));
		m.saveConfig();
		Utility.sendConsole("&e&lAdvancedWhitelist > &7Config reloaded.");
	}

	public static void saveWhitelists() {
/*		File configFile = new File(this.m.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			m.saveDefaultConfig();
//			setup(m);
			m.saveConfig();
		}*/
		FileConfiguration c = WLStorage.m.getConfig();
		c.set("whitelisted", WLStorage.whitelists);
		c.set("whitelist_enabled", WLStorage.isWhitelisting());
		c.set("server_cooldown", WLStorage.isServerCooldown());
		c.set("server_cooldown_duration", WLStorage.getServerCooldown());
		c.set("ProjectTeam_Access", WLStorage.isProjectTeamAccess());
		c.set("Staff_Access", WLStorage.isStaffAccess());
		c.set("Tester_Access", WLStorage.isTesterAccess());
		c.set("Alternate_Access", WLStorage.isAlternateAccess());
		c.set("Other_Access", WLStorage.isOtherAccess());
		c.set("Config_Access", WLStorage.isConfigAccess());
		c.set("not_whitelisted_message", WLStorage.getNotWhitelistMsg());
		c.set("server_to_send_to", WLStorage.getHubServer());
		c.set("send_or_kick_broadcast_message", WLStorage.getBroadcastMsg());
		c.set("delay_before_starting_kicks", WLStorage.getDelayBeforeStartingKicks());
		c.set("kick_delay_per_player", WLStorage.getKickDelayPerPlayer());
		c.set("send_message", WLStorage.getSendMsg());
		c.set("kick_message", WLStorage.getKickMsg());
		m.saveConfig();
		reload();
	}

	public String getConfigVersion() {
		return WLStorage.configVersion;
	}
	
	public static boolean isWhitelisted(String name) {
		return WLStorage.whitelists.contains(name.toLowerCase());
	}

	public static void addWhitelist(String name) {
		if (!WLStorage.whitelists.contains(name.toLowerCase())) {
			WLStorage.whitelists.add(name.toLowerCase());
			WLStorage.saveWhitelists();
		}
	}

	public static void removeWhitelist(String name) {
		if (WLStorage.whitelists.contains(name.toLowerCase())) {
			WLStorage.whitelists.remove(name.toLowerCase());
			WLStorage.saveWhitelists();
		}
	}

	public static void setWhitelist(Boolean onoff) {
		WhitelistEnabled = onoff;
		WLStorage.saveWhitelists();
	}

	public static void setServerCooldown(Boolean onoff) {
		WLStorage.ServerCooldownEnabled = onoff;
		WLStorage.saveWhitelists();
	}

	public static void setServerCooldownTime(Long arg) {
		WLStorage.ServerCooldownDuration = arg;
		WLStorage.saveWhitelists();
	}

	public static void setProjectTeamAccess(Boolean onoff) {
		WLStorage.ProjectTeamAccessEnabled = onoff;
		WLStorage.saveWhitelists();
	}

	public static void setStaffAccess(Boolean onoff) {
		WLStorage.StaffAccessEnabled = onoff;
		WLStorage.saveWhitelists();
	}

	public static void setTesterAccess(Boolean onoff) {
		WLStorage.TesterAccessEnabled = onoff;
		WLStorage.saveWhitelists();
	}

	public static void setAlternateAccess(Boolean onoff) {
		WLStorage.AlternateAccessEnabled = onoff;
		WLStorage.saveWhitelists();
	}

	public static void setOtherAccess(Boolean onoff) {
		WLStorage.OtherAccessEnabled = onoff;
		WLStorage.saveWhitelists();
	}

	public static void setConfigAccess(Boolean onoff) {
		WLStorage.ConfigAccessEnabled = onoff;
		WLStorage.saveWhitelists();
	}
	public static void setNotWhitelistMsg(String arg) {
		WLStorage.notwhitelistmsg = arg;
		WLStorage.saveWhitelists();
	}
	public static void setHubServer(String arg) {
		WLStorage.hubServer = arg;
		WLStorage.saveWhitelists();
	}
	public static void setBroadcastMsg(String arg) {
		WLStorage.broadcastmsg = arg;
		WLStorage.saveWhitelists();
	}
	public static void setDelayBeforeStartingKicks(Long arg) {
		WLStorage.delayBeforeStartingKicks = arg;
		WLStorage.saveWhitelists();
	}
	public static void setKickDelayPerPlayer(Long arg) {
		WLStorage.kickDelayPerPlayer = arg;
		WLStorage.saveWhitelists();
	}
	public static void setSendMsg(String arg) {
		WLStorage.sendmsg = arg;
		WLStorage.saveWhitelists();
	}
	public static void setKickMsg(String arg) {
		WLStorage.kickmsg = arg;
		WLStorage.saveWhitelists();
	}

	public static ArrayList<String> getWhiteLists() {
		return WLStorage.whitelists;
	}
	
	public static void clearWhiteLists() {
		whitelists.removeAll(whitelists);
		WLStorage.saveWhitelists();
		return;
	}

	public static boolean isWhitelisting() {
		return WLStorage.WhitelistEnabled;
	}

	public static boolean isServerCooldown() {
		return WLStorage.ServerCooldownEnabled;
	}

	public static long getServerCooldown() {
		return WLStorage.ServerCooldownDuration;
	}

	public static boolean isProjectTeamAccess() {
		return WLStorage.ProjectTeamAccessEnabled;
	}

	public static boolean isStaffAccess() {
		return WLStorage.StaffAccessEnabled;
	}

	public static boolean isTesterAccess() {
		return WLStorage.TesterAccessEnabled;
	}

	public static boolean isAlternateAccess() {
		return WLStorage.AlternateAccessEnabled;
	}

	public static boolean isOtherAccess() {
		return WLStorage.OtherAccessEnabled;
	}

	public static boolean isConfigAccess() {
		return WLStorage.ConfigAccessEnabled;
	}

	public static String getNotWhitelistMsg() {
		return WLStorage.notwhitelistmsg;
	}

	public static String getHubServer() {
		return WLStorage.hubServer;
	}

	public static String getBroadcastMsg() {
		return WLStorage.broadcastmsg;
	}

	public static Long getDelayBeforeStartingKicks() {
		return WLStorage.delayBeforeStartingKicks;
	}

	public static Long getKickDelayPerPlayer() {
		return WLStorage.kickDelayPerPlayer;
	}

	public static String getSendMsg() {
		return WLStorage.sendmsg;
	}

	public static String getKickMsg() {
		return WLStorage.kickmsg;
	}
	

}



