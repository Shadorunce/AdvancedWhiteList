package com.gmail.shadoruncegaming.advancedwhitelist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class WLStorage {
	static AdvancedWhiteList m;
	private static String configVersion = "2.0";
	private static ArrayList<String> whitelists = new ArrayList<String>(Arrays.asList("ExampleNameThatWouldHaveAccessIfConfigAccessIsOn_PleaseReplaceThis"));
	private static boolean WhitelistEnabled = false;
	private static boolean ProjectTeamAccessEnabled = false;
	private static boolean StaffAccessEnabled = false;
	private static boolean TesterAccessEnabled = false;
	private static boolean AlternateAccessEnabled = false;
	private static boolean OtherAccessEnabled = false;
	private static boolean ConfigAccessEnabled = false;
	private static String notwhitelistmsg = "&eSorry, the server is currently in Whitelist mode, please enjoy one of our other servers. :)";
	private static String broadcastmsg = "&eSorry, the server is currently in Whitelist mode, please enjoy one of our other servers. :)";
	private static String sendmsg = "&eSorry, the server is currently in Whitelist mode, please enjoy one of our other servers. :)";
	private static String kickmsg = "&eSorry, the server is currently in Whitelist mode, please enjoy one of our other servers. :)";
	private static long ServerCooldownDuration = 60; // In seconds based on from start of plugin.
	private static long delayBeforeStartingKicks = 4; // In seconds.
	private static long kickDelayPerPlayer = 1; // In seconds.
	private static boolean ServerCooldownEnabled = true;
	private static String hubServer = "lobby";
	private static boolean opBypass = false;

	private static Material guiTrue = Material.GREEN_STAINED_GLASS_PANE;
	private static Material guiFalse = Material.RED_STAINED_GLASS_PANE;
	private static Material guiLobby = Material.COMPASS;
	private static Material guiNotWlMsg = Material.PAPER;
	private static Material guiBcMsg = Material.PAPER;
	private static Material guiSendMsg = Material.PAPER;
	private static Material guiKickMsg = Material.PAPER;
	private static Material guiCdDuration = Material.CLOCK;
	private static Material guiDelayStartKicks = Material.CLOCK;
	private static Material guiKickDelayPlayer = Material.CLOCK;
	private static Material guiHelp = Material.BOOKSHELF;
	private static Material guiStatus = Material.BOOK;
	private static Material guiWlConfigList = Material.WRITABLE_BOOK;
	private static Material guiAdd = Material.EMERALD;
	private static Material guiRemove = Material.REDSTONE;
	private static Material guiAddAllOnline = Material.EMERALD_BLOCK;
	private static Material guiRemoveAll = Material.REDSTONE_BLOCK;
	private static Material guiSendPlayers = Material.BARRIER;
	private static Material guiRestartServer = Material.BARRIER;
	private static Material guiAnvilItem = Material.NAME_TAG;

	public WLStorage(AdvancedWhiteList m) {
        WLStorage.m = m;
    }

	public static void versionCheck() {
		m.reloadConfig();
		FileConfiguration config = m.getConfig();
		if (!(config.getString("config_version_dont_change").equals(configVersion))) {
			Utility.sendConsole("§4Old Version detected");
			convertConfig();
		}
		if ((config.getString("config_version_dont_change").equals(configVersion))) {
			Utility.sendConsole("§1Config version matches");
		}
	}
	
	
	public static void reload() {
		try {
			m.reloadConfig();
		} catch(Exception e) {
			m.setupConfig();
			Utility.sendConsole("Something went wrong with config. Please recheck your configuration as the setting that went wrong was likely set back to default.");
			m.reloadConfig();
			e.printStackTrace();
		}
		FileConfiguration config = m.getConfig();
		try {
			configVersion = config.getString("config_version_dont_change");
		} catch(Exception e) {Utility.sendConsole("Something was wrong with config version.");}
		try {
			whitelists = new ArrayList<String>(config.getStringList("whitelisted"));
		} catch(Exception e) {whitelists = new ArrayList<String>(Arrays.asList("ExampleNameThatWouldHaveAccessIfConfigAccessIsOn_PleaseReplaceThis"));
		Utility.sendConsole("Something was wrong with whitelisted list, setting to default.");}
		try {
			WhitelistEnabled = config.getBoolean("config.access_enabled.whitelist");
		} catch(Exception e) {WhitelistEnabled = false;
		Utility.sendConsole("Something was wrong with enabled: whitelist, setting to default.");}
		try {
			ConfigAccessEnabled = config.getBoolean("config.access_enabled.config_access");
		} catch(Exception e) {ConfigAccessEnabled = false;
		Utility.sendConsole("Something was wrong with enabled: config access, setting to default.");}
		try {
			ProjectTeamAccessEnabled = config.getBoolean("config.access_enabled.teams.projectteam");
		} catch(Exception e) {ProjectTeamAccessEnabled = false;
		Utility.sendConsole("Something was wrong with access enabled: teams: projectteam, setting to default.");}
		try {
			StaffAccessEnabled = config.getBoolean("config.access_enabled.teams.staff");
		} catch(Exception e) {StaffAccessEnabled = false;
		Utility.sendConsole("Something was wrong with enabled: teams: staff, setting to default.");}
		try {
			TesterAccessEnabled = config.getBoolean("config.access_enabled.teams.tester");
		} catch(Exception e) {TesterAccessEnabled = false;
		Utility.sendConsole("Something was wrong with enabled: teams: tester, setting to default.");}
		try {
			AlternateAccessEnabled = config.getBoolean("config.access_enabled.teams.alternate");
		} catch(Exception e) {AlternateAccessEnabled = false;
		Utility.sendConsole("Something was wrong with enabled: teams: alternate, setting to default.");}
		try {
			OtherAccessEnabled = config.getBoolean("config.access_enabled.teams.other");
		} catch(Exception e) {OtherAccessEnabled = false;
		Utility.sendConsole("Something was wrong with enabled: teams: other, setting to default.");}
		try {
			notwhitelistmsg = Utility.TransColor(config.getString("config.messages.not_whitelisted_message"));
		} catch(Exception e) {notwhitelistmsg = "Something went wrong with the config, contact an Admin if you got this message.";
		Utility.sendConsole("Something was wrong with not_whitelisted_message, setting to error message to players.");}
		try {
			broadcastmsg = Utility.TransColor(config.getString("config.messages.send_or_kick_broadcast_message"));
		} catch(Exception e) {broadcastmsg = "Something went wrong with the config, contact an Admin if you got this message.";
		Utility.sendConsole("Something was wrong with send_or_kick_broadcast_message, setting to error message to players.");}
		try {
			sendmsg = Utility.TransColor(config.getString("config.messages.send_message"));
		} catch(Exception e) {sendmsg = "Something went wrong with the config, contact an Admin if you got this message.";
		Utility.sendConsole("Something was wrong with send_message, setting to error message to players.");}
		try {
			kickmsg = Utility.TransColor(config.getString("config.messages.kick_message"));
		} catch(Exception e) {kickmsg = "Something went wrong with the config, contact an Admin if you got this message.";
		Utility.sendConsole("Something was wrong with kick_message, setting to error message to players.");}
		try {
			ServerCooldownDuration = config.getInt("config.durations.server_cooldown_duration");
		} catch(Exception e) {ServerCooldownDuration = 90;
		Utility.sendConsole("Something was wrong with server_cooldown_duration, setting to default.");}
		try {
			delayBeforeStartingKicks = config.getInt("config.durations.delay_before_starting_kicks");
		} catch(Exception e) {delayBeforeStartingKicks = 5;
		Utility.sendConsole("Something was wrong with delay_before_starting_kicks, setting to default.");}
		try {
			kickDelayPerPlayer = config.getInt("config.durations.kick_delay_per_player");
		} catch(Exception e) {kickDelayPerPlayer = 1;
		Utility.sendConsole("Something was wrong with kick_delay_per_player, setting to default.");}
		try {
			ServerCooldownEnabled = config.getBoolean("config.misc.server_cooldown");
		} catch(Exception e) {ServerCooldownEnabled = true;
		Utility.sendConsole("Something was wrong with server_cooldown, setting to default.");}
		try {
			hubServer = Utility.TransColor(config.getString("config.misc.server_to_send_to"));
		} catch(Exception e) {hubServer = "lobby";
		Utility.sendConsole("Something was wrong with server_to_send_to, setting to default.");}
		try {
			opBypass = config.getBoolean("config.misc.allow_op_and_star_bypass");
		} catch(Exception e) {opBypass = false;
		Utility.sendConsole("Something was wrong with allow_op_and_star_bypass, setting to default.");}
		
		// GUI
		try {
			guiTrue = Material.getMaterial(config.getString("gui.enabled_row1.true").toUpperCase());
		} catch(Exception e) {guiTrue = Material.GREEN_STAINED_GLASS_PANE;
		Utility.sendConsole("GUI item True was invalid, setting to default.");}
		try {
			guiFalse = Material.getMaterial(config.getString("gui.enabled_row1.false").toUpperCase());
		} catch(Exception e) {guiFalse = Material.RED_STAINED_GLASS_PANE;
		Utility.sendConsole("GUI item False was invalid, setting to default.");}
		try {
			guiLobby = Material.getMaterial(config.getString("gui.messages_row2.lobby").toUpperCase());
		} catch(Exception e) {guiLobby = Material.COMPASS;
		Utility.sendConsole("GUI item Lobby was invalid, setting to default.");}
		try {
			guiNotWlMsg = Material.getMaterial(config.getString("gui.messages_row2.not_whitelisted_message").toUpperCase());
		} catch(Exception e) {guiNotWlMsg = Material.PAPER;
		Utility.sendConsole("GUI item Not Whitelisted Message was invalid, setting to default.");}
		try {
			guiBcMsg = Material.getMaterial(config.getString("gui.messages_row2.send_or_kick_broadcast_message").toUpperCase());
		} catch(Exception e) {guiBcMsg = Material.PAPER;
		Utility.sendConsole("GUI item Send or Kick Broadcast Message was invalid, setting to default.");}
		try {
			guiSendMsg = Material.getMaterial(config.getString("gui.messages_row2.send_message").toUpperCase());
		} catch(Exception e) {guiSendMsg = Material.PAPER;
		Utility.sendConsole("GUI item Send Message was invalid, setting to default.");}
		try {
			guiKickMsg = Material.getMaterial(config.getString("gui.messages_row2.kick_message").toUpperCase());
		} catch(Exception e) {guiKickMsg = Material.PAPER;
		Utility.sendConsole("GUI item Kick Message was invalid, setting to default.");}
		try {
			guiCdDuration = Material.getMaterial(config.getString("gui.durations_row3.server_cooldown_duration").toUpperCase());
		} catch(Exception e) {guiCdDuration = Material.CLOCK;
		Utility.sendConsole("GUI item Server Cooldown Duration was invalid, setting to default.");}
		try {
			guiDelayStartKicks = Material.getMaterial(config.getString("gui.durations_row3.delay_before_starting_kicks").toUpperCase());
		} catch(Exception e) {guiDelayStartKicks = Material.CLOCK;
		Utility.sendConsole("GUI item Delay Before Starting Kicks was invalid, setting to default.");}
		try {
			guiKickDelayPlayer = Material.getMaterial(config.getString("gui.durations_row3.kick_delay_per_player").toUpperCase());
		} catch(Exception e) {guiKickDelayPlayer = Material.CLOCK;
		Utility.sendConsole("GUI item Kick Delay per Player was invalid, setting to default.");}
		try {
			guiStatus = Material.getMaterial(config.getString("gui.commands_row4.status").toUpperCase());
		} catch(Exception e) {guiStatus = Material.BOOK;
		Utility.sendConsole("GUI item Status was invalid, setting to default.");}
		try {
			guiWlConfigList = Material.getMaterial(config.getString("gui.commands_row4.whitelisted_config_list").toUpperCase());
		} catch(Exception e) {guiWlConfigList = Material.WRITABLE_BOOK;
		Utility.sendConsole("GUI item Whitelisted Config List was invalid, setting to default.");}
		try {
			guiAdd = Material.getMaterial(config.getString("gui.commands_row4.add").toUpperCase());
		} catch(Exception e) {guiAdd = Material.EMERALD;
		Utility.sendConsole("GUI item Add was invalid, setting to default.");}
		try {
			guiRemove = Material.getMaterial(config.getString("gui.commands_row4.remove").toUpperCase());
		} catch(Exception e) {guiRemove = Material.REDSTONE;
		Utility.sendConsole("GUI item Remove was invalid, setting to default.");}
		try {
			guiAddAllOnline = Material.getMaterial(config.getString("gui.commands_row4.add_all_online").toUpperCase());
		} catch(Exception e) {guiAddAllOnline = Material.EMERALD_BLOCK;
		Utility.sendConsole("GUI item Add All Online was invalid, setting to default.");}
		try {
			guiRemoveAll = Material.getMaterial(config.getString("gui.commands_row4.remove_all").toUpperCase());
		} catch(Exception e) {guiRemoveAll = Material.REDSTONE_BLOCK;
		Utility.sendConsole("GUI item Remove All was invalid, setting to default.");}
		try {
			guiSendPlayers = Material.getMaterial(config.getString("gui.commands_row4.send_players_enforce_whitelist").toUpperCase());
		} catch(Exception e) {guiSendPlayers = Material.BARRIER;
		Utility.sendConsole("GUI item Send Players Enforce Whitelist was invalid, setting to default.");}
		try {
			guiRestartServer = Material.getMaterial(config.getString("gui.commands_row4.restart_server").toUpperCase());
		} catch(Exception e) {guiRestartServer = Material.BARRIER;
		Utility.sendConsole("GUI item Restart Server was invalid, setting to default.");}
		try {
			guiAnvilItem = Material.getMaterial(config.getString("gui.anvil_item").toUpperCase());
		} catch(Exception e) {guiRestartServer = Material.NAME_TAG;
		Utility.sendConsole("GUI item Anvil Item was invalid, setting to default.");}
		
		Utility.sendConsole("&e&lAWL > &7Config reloaded - In-game values matched to config.");
		WLCmd.getStatus(Bukkit.getConsoleSender());
	}
	
	public static void convertConfig() {
		m.reloadConfig();
		FileConfiguration config = m.getConfig();
		if (config.contains("config_version_dont_change")) {
			try {
				config.set("config_version_dont_change", configVersion);
			} catch(Exception e) {e.printStackTrace();}
		}
		if (config.contains("whitelist_enabled")) {
			try {
				WhitelistEnabled = config.getBoolean("whitelist_enabled");
				config.set("whitelist_enabled", null);
			} catch(Exception e) {e.printStackTrace();}
		}
		if (config.contains("Config_Access")) {
			try {
				ConfigAccessEnabled = config.getBoolean("Config_Access");
				config.set("Config_Access", null);
			} catch(Exception e) {e.printStackTrace();}
		}
		if (config.contains("ProjectTeam_Access")) {
			try {
				ProjectTeamAccessEnabled = config.getBoolean("ProjectTeam_Access");
				config.set("ProjectTeam_Access", null);
			} catch(Exception e) {e.printStackTrace();}
		}
		if (config.contains("Staff_Access")) {
			try {
				StaffAccessEnabled = config.getBoolean("Staff_Access");
				config.set("Staff_Access", null);
			} catch(Exception e) {}
		}
		if (config.contains("Tester_Access")) {
			try {
				TesterAccessEnabled = config.getBoolean("Tester_Access");
				config.set("Tester_Access", null);
			} catch(Exception e) {}
		}
		if (config.contains("Alternate_Access")) {
			try {
				AlternateAccessEnabled = config.getBoolean("Alternate_Access");
				config.set("Alternate_Access", null);
			} catch(Exception e) {}
		}
		if (config.contains("Other_Access")) {
			try {
				OtherAccessEnabled = config.getBoolean("Other_Access");
				config.set("Other_Access", null);
			} catch(Exception e) {}
		}
		if (config.contains("not_whitelisted_message")) {
			try {
				notwhitelistmsg = Utility.TransColor(config.getString("not_whitelisted_message"));
				config.set("not_whitelisted_message", null);
			} catch(Exception e) {}
		}
		if (config.contains("send_or_kick_broadcast_message")) {
			try {
				broadcastmsg = Utility.TransColor(config.getString("send_or_kick_broadcast_message"));
				config.set("send_or_kick_broadcast_message", null);
			} catch(Exception e) {}
		}
		if (config.contains("send_message")) {
			try {
				sendmsg = Utility.TransColor(config.getString("send_message"));
				config.set("send_message", null);
			} catch(Exception e) {}
		}
		if (config.contains("kick_message")) {
			try {
				kickmsg = Utility.TransColor(config.getString("kick_message"));
				config.set("kick_message", null);
			} catch(Exception e) {}
		}
		if (config.contains("server_cooldown_duration")) {
			try {
				ServerCooldownDuration = config.getInt("server_cooldown_duration");
				config.set("server_cooldown_duration", null);
			} catch(Exception e) {}
		}
		if (config.contains("delay_before_starting_kicks")) {
			try {
				delayBeforeStartingKicks = config.getInt("delay_before_starting_kicks");
				config.set("delay_before_starting_kicks", null);
			} catch(Exception e) {}
		}
		if (config.contains("kick_delay_per_player")) {
			try {
				kickDelayPerPlayer = config.getInt("kick_delay_per_player");
				config.set("kick_delay_per_player", null);
			} catch(Exception e) {}
		}
		if (config.contains("server_cooldown")) {
			try {
				ServerCooldownEnabled = config.getBoolean("server_cooldown");
				config.set("server_cooldown", null);
			} catch(Exception e) {}
		}
		if (config.contains("server_to_send_to")) {
			try {
				hubServer = Utility.TransColor(config.getString("server_to_send_to"));
				config.set("server_to_send_to", null);
			} catch(Exception e) {}
		}
		saveWhitelists();
		m.saveConfig();
		Utility.sendConsole("&e&lAdvancedWhitelist > &7Config converted from old version.");
	}


	public static void convertMcConfig() {
		m.reloadConfig();
		FileConfiguration config = m.getConfig();
		try {
			if (Bukkit.hasWhitelist()) {
				config.set("config.access_enabled.whitelist", Bukkit.hasWhitelist());
				Bukkit.setWhitelist(false);
			}
			Utility.sendConsole("Minecraft Whitelist size: " + Bukkit.getWhitelistedPlayers().size());
			if (Bukkit.getWhitelistedPlayers().size() > 0) {
				for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
					addWhitelist(player.getName());
					Utility.sendConsole("§aAdded to whitelist from MC Whitelist: " + player.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void convertEWLConfig() {

		File getFile = new File("plugins\\EasyWhiteList", "config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile);
		if (getFile.exists()) {
			WhitelistEnabled = config.getBoolean("whitelist_enabled");
			ConfigAccessEnabled = config.getBoolean("Config_Access");
			ProjectTeamAccessEnabled = config.getBoolean("ProjectTeam_Access");
			StaffAccessEnabled = config.getBoolean("Staff_Access");
			TesterAccessEnabled = config.getBoolean("Tester_Access");
			AlternateAccessEnabled = config.getBoolean("Alternate_Access");
			OtherAccessEnabled = config.getBoolean("Other_Access");
			notwhitelistmsg = Utility.TransColor(config.getString("not_whitelisted_message"));
			broadcastmsg = Utility.TransColor(config.getString("send_or_kick_broadcast_message"));
			sendmsg = Utility.TransColor(config.getString("send_message"));
			kickmsg = Utility.TransColor(config.getString("kick_message"));
			ServerCooldownDuration = config.getInt("server_cooldown_duration");
			delayBeforeStartingKicks = config.getInt("delay_before_starting_kicks");
			kickDelayPerPlayer = config.getInt("kick_delay_per_player");
			ServerCooldownEnabled = config.getBoolean("server_cooldown");
			hubServer = Utility.TransColor(config.getString("server_to_send_to"));
			
			saveWhitelists();
			m.saveConfig();
			try {
				File renameFile = new File("plugins\\\\EasyWhiteList", "config.yml.old");
				getFile.renameTo(renameFile);
				getFile.deleteOnExit();
				config.save(getFile);
			} catch (IOException e) {
				e.printStackTrace();
				Utility.sendConsole("&e&lAdvancedWhitelist > &7EasyWhiteList Config not found.");
			}
			
			Utility.sendConsole("&e&lAdvancedWhitelist > &7EasyWhiteList Config converted.");
			}
		if (!getFile.exists()) {Utility.sendConsole("&e&lAdvancedWhitelist > &7EasyWhiteList Config not found.");}
	}
	
	
	public static void saveWhitelists() {
/*		File configFile = new File(this.m.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			m.saveDefaultConfig();
//			setup(m);
			m.saveConfig();
		}*/

		FileConfiguration c = m.getConfig();
		c.set("whitelisted", whitelists);
		c.set("config.access_enabled.whitelist", isWhitelisting());
		c.set("config.access_enabled.config_access", isConfigAccess());
		c.set("config.access_enabled.teams.projectteam", isProjectTeamAccess());
		c.set("config.access_enabled.teams.staff", isStaffAccess());
		c.set("config.access_enabled.teams.tester", isTesterAccess());
		c.set("config.access_enabled.teams.alternate", isAlternateAccess());
		c.set("config.access_enabled.teams.other", isOtherAccess());
		c.set("config.messages.not_whitelisted_message", getNotWhitelistMsg());
		c.set("config.messages.send_or_kick_broadcast_message", getBroadcastMsg());
		c.set("config.messages.send_message", getSendMsg());
		c.set("config.messages.kick_message", getKickMsg());
		c.set("config.durations.server_cooldown_duration", getServerCooldown());
		c.set("config.durations.delay_before_starting_kicks", getDelayBeforeStartingKicks());
		c.set("config.durations.kick_delay_per_player", getKickDelayPerPlayer());
		c.set("config.misc.server_cooldown", isServerCooldown());
		c.set("config.misc.server_to_send_to", getHubServer());
		c.set("config.misc.allow_op_and_star_bypass", isOpBypass());
		c.set("gui.enabled_row1.true", getGuiTrue().name());
		c.set("gui.enabled_row1.false", getGuiFalse().name());
		c.set("gui.messages_row2.lobby", getGuiLobby().name());
		c.set("gui.messages_row2.not_whitelisted_message", getGuiNotWlMsg().name());
		c.set("gui.messages_row2.send_or_kick_broadcast_message", getGuiBcMsg().name());
		c.set("gui.messages_row2.send_message", getGuiSendMsg().name());
		c.set("gui.messages_row2.kick_message", getGuiKickMsg().name());
		c.set("gui.durations_row3.server_cooldown_duration", getGuiCdDuration().name());
		c.set("gui.durations_row3.delay_before_starting_kicks", getGuiDelayStartKicks().name());
		c.set("gui.durations_row3.kick_delay_per_player", getGuiKickDelayPlayer().name());
		c.set("gui.commands_row4.status", getGuiStatus().name());
		c.set("gui.commands_row4.whitelisted_config_list", getGuiWlConfigList().name());
		c.set("gui.commands_row4.add", getGuiAdd().name());
		c.set("gui.commands_row4.remove", getGuiRemove().name());
		c.set("gui.commands_row4.add_all_online", getGuiAddAllOnline().name());
		c.set("gui.commands_row4.remove_all", getGuiRemoveAll().name());
		c.set("gui.commands_row4.send_players_enforce_whitelist", getGuiSendPlayers().name());
		c.set("gui.commands_row4.restart_server", getGuiRestartServer().name());
		c.set("gui.anvil_item", getGuiAnvilItem().name());
		m.saveConfig();
		//reload();
	}

	public static String getConfigVersion() {
		return configVersion;
	}
	
	public static boolean isWhitelisted(String name) {
		return whitelists.contains(name.toLowerCase());
	}

	public static void addWhitelist(String name) {
		if (!whitelists.contains(name.toLowerCase())) {
			whitelists.add(name.toLowerCase());
			saveWhitelists();
		}
	}

	public static void removeWhitelist(String name) {
		if (whitelists.contains(name.toLowerCase())) {
			whitelists.remove(name.toLowerCase());
			saveWhitelists();
		}
	}

	public static void setWhitelist(Boolean onoff) {
		WhitelistEnabled = onoff;
		saveWhitelists();
	}

	public static void setServerCooldown(Boolean onoff) {
		ServerCooldownEnabled = onoff;
		saveWhitelists();
	}

	public static void setServerCooldownTime(Long arg) {
		ServerCooldownDuration = arg;
		saveWhitelists();
	}

	public static void setProjectTeamAccess(Boolean onoff) {
		ProjectTeamAccessEnabled = onoff;
		saveWhitelists();
	}

	public static void setStaffAccess(Boolean onoff) {
		StaffAccessEnabled = onoff;
		saveWhitelists();
	}

	public static void setTesterAccess(Boolean onoff) {
		TesterAccessEnabled = onoff;
		saveWhitelists();
	}

	public static void setAlternateAccess(Boolean onoff) {
		AlternateAccessEnabled = onoff;
		saveWhitelists();
	}

	public static void setOtherAccess(Boolean onoff) {
		OtherAccessEnabled = onoff;
		saveWhitelists();
	}

	public static void setConfigAccess(Boolean onoff) {
		ConfigAccessEnabled = onoff;
		saveWhitelists();
	}
	public static void setNotWhitelistMsg(String arg) {
		notwhitelistmsg = arg;
		saveWhitelists();
	}
	public static void setHubServer(String arg) {
		hubServer = arg;
		saveWhitelists();
	}
	public static void setBroadcastMsg(String arg) {
		broadcastmsg = arg;
		saveWhitelists();
	}
	public static void setDelayBeforeStartingKicks(Long arg) {
		delayBeforeStartingKicks = arg;
		saveWhitelists();
	}
	public static void setKickDelayPerPlayer(Long arg) {
		kickDelayPerPlayer = arg;
		saveWhitelists();
	}
	public static void setSendMsg(String arg) {
		sendmsg = arg;
		saveWhitelists();
	}
	public static void setKickMsg(String arg) {
		kickmsg = arg;
		saveWhitelists();
	}
	public static void setOpBypass(Boolean onoff) {
		opBypass = onoff;
		saveWhitelists();
	}

	public static ArrayList<String> getWhiteLists() {
		return whitelists;
	}
	
	public static void clearWhiteLists() {
		whitelists.removeAll(whitelists);
		saveWhitelists();
		return;
	}

	public static boolean isWhitelisting() {
		return WhitelistEnabled;
	}

	public static boolean isServerCooldown() {
		return ServerCooldownEnabled;
	}

	public static long getServerCooldown() {
		return ServerCooldownDuration;
	}

	public static boolean isProjectTeamAccess() {
		return ProjectTeamAccessEnabled;
	}

	public static boolean isStaffAccess() {
		return StaffAccessEnabled;
	}

	public static boolean isTesterAccess() {
		return TesterAccessEnabled;
	}

	public static boolean isAlternateAccess() {
		return AlternateAccessEnabled;
	}

	public static boolean isOtherAccess() {
		return OtherAccessEnabled;
	}

	public static boolean isConfigAccess() {
		return ConfigAccessEnabled;
	}

	public static String getNotWhitelistMsg() {
		return notwhitelistmsg;
	}

	public static String getHubServer() {
		return hubServer;
	}

	public static String getBroadcastMsg() {
		return broadcastmsg;
	}

	public static Long getDelayBeforeStartingKicks() {
		return delayBeforeStartingKicks;
	}

	public static Long getKickDelayPerPlayer() {
		return kickDelayPerPlayer;
	}
	
	public static boolean isOpBypass() {
		return opBypass;
	}

	public static String getSendMsg() {
		return sendmsg;
	}

	public static String getKickMsg() {
		return kickmsg;
	}
	
	public static Material getGuiTrue() {
		return guiTrue;
	}
	
	public static Material getGuiFalse() {
		return guiFalse;
	}
	
	public static Material getGuiLobby() {
		return guiLobby;
	}
	
	public static Material getGuiNotWlMsg() {
		return guiNotWlMsg;
	}
	
	public static Material getGuiBcMsg() {
		return guiBcMsg;
	}
	
	public static Material getGuiSendMsg() {
		return guiSendMsg;
	}
	
	public static Material getGuiKickMsg() {
		return guiKickMsg;
	}
	
	public static Material getGuiCdDuration() {
		return guiCdDuration;
	}
	
	public static Material getGuiDelayStartKicks() {
		return guiDelayStartKicks;
	}
	
	public static Material getGuiKickDelayPlayer() {
		return guiKickDelayPlayer;
	}
	
	public static Material getGuiHelp() {
		return guiHelp;
	}
	
	public static Material getGuiStatus() {
		return guiStatus;
	}
	
	public static Material getGuiWlConfigList() {
		return guiWlConfigList;
	}
	
	public static Material getGuiAdd() {
		return guiAdd;
	}
	
	public static Material getGuiRemove() {
		return guiRemove;
	}
	
	public static Material getGuiAddAllOnline() {
		return guiAddAllOnline;
	}
	
	public static Material getGuiRemoveAll() {
		return guiRemoveAll;
	}
	
	public static Material getGuiSendPlayers() {
		return guiSendPlayers;
	}
	
	public static Material getGuiRestartServer() {
		return guiRestartServer;
	}
	
	public static Material getGuiAnvilItem() {
		return guiAnvilItem;
	}
}



