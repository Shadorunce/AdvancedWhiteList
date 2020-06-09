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
	
/*	public void buildConfig() {
		FileConfiguration config = this.m.getConfig();
		config.options().header("# Welcome to AdvancedWhitelist.\r\n" + 
				"## Once configured and permissions set, it will give the smoothest experience for managing access for entire teams. \r\n" + 
				"### Everything you can configure here in the file, can also be checked and configured in game with various commands.\r\n" + 
				"#### All commands, permissions, and aliases are listed in the help menu at \"/awl help <number>\"\r\n" + 
				"#\r\n" + 
				"# Important: To use any of the commands in game, you will need the permission advancedwhitelist.admin\r\n" + 
				"#\r\n" + 
				"# Initially the Whitelist will be disabled, but you can enable it here at \"whitelist_enabled\" and reload the config with \"/awl reload\" or you can use \"/awl on\" from console, or in-game if you have the permission.\r\n" + 
				"#\r\n" + 
				"# Without any config changes, \"advancedwhitelist.bypass.operators\" is the only permission that will always have access to the server regardless of below settings.\r\n" + 
				"## Recommended Note: This permission should be restricted to a Server Owner and the most trusted Staff/Admins/Operators, whatever term your server uses for it.\r\n" + 
				"#\r\n" + 
				"# There are other Permissions that will allow access if the below access are turned on.\r\n" + 
				"## The permission is \"advancedwhitelist.bypass.<perm>\" where <perm> is one of the following access options.\r\n" + 
				"### <ProjectTeam, Staff, Tester, Alternate, Other> Access\r\n" + 
				"#### You can choose how you want to use the permissions for access.\r\n" + 
				"##### Each perm/access is separate from each other, so if tester access is enabled but staff isn't, staff won't be able to access it unless they also have the perm.\r\n" + 
				"#\r\n" + 
				"# This is still a WhiteList plugin, so you can still add players names either in the config or using the in-game commands.\r\n" + 
				"## For the list to be effective, you will need to turn on the Config Access option.\r\n" + 
				"#\r\n" + 
				"# There's also a command setup so that it will restrict the current online players to only those that are allowed by the Whitelist\r\n" + 
				"## It will first attempt to send the Non-Whitelisted player to the Lobby/Hub server specified in the config below at \"server_to_send_to\".\r\n" + 
				"### If they're currently on that server, or if it fails to send for some reason, it will then kick the player.\r\n" + 
				"#\r\n" + 
				"# During the send and kick, there's an initial Broadcast message, which can be configured below.\r\n" + 
				"## There's a message that's sent to the player just before they're sent to the lobby/hub server.\r\n" + 
				"### There's a separate kick message that's configurable for the players.\r\n" + 
				"#### Sorry for other servers, the message set is for the server/network the plugin was developed for.\r\n" + 
				"#\r\n" + 
				"# This plugin is also setup with a cooldown from when the plugin starts, generally when the server starts, to when players can connect.\r\n" + 
				"## This has been added so that you can allow a server to settle before players rush to join again. But if you don't want this, it can also be turned off.\r\n" + 
				"### It's on by default, but can be turned on, and the amount of time to wait can also be changed.\r\n" + 
				"#### There are two exceptions. Users with the Operators Bypass perm will be able to connect at one-third (1/3) the time specified to allow the highest admins to connect and make changes as needed, such as if a server needs to be set to whitelist.\r\n" + 
				"#### Second, the ProjectTeam Bypass perm will be able to connect at half (1/2) the time specified for the same reason.\r\n" + 
				"#\r\n" + 
				"# Note: All times are based on seconds.\r\n" + 
				"#\r\n" + 
				"# Don't change the Config Version").copyHeader(true).copyDefaults(true);
//		config.addDefaults(defaults);
//		config.options().copyHeader(true);
//        config.options().copyDefaults(true);
	}*/
	
	

 /*   private Plugin p;
    private String configversion = "1.1.3";


    FileConfiguration config;
    File cfile;

    FileConfiguration message;
    File mfile;

    public void setup(Plugin plugin){
        p = plugin;
        if(!p.getDataFolder().exists()) p.getDataFolder().mkdir();
        cfile = new File(p.getDataFolder(), "config.yml");
        if(!cfile.exists()){
            copy(p.getResource("config.yml"), cfile);
        }
        new YamlConfiguration();
		config = YamlConfiguration.loadConfiguration(cfile);
        if(getConfig().getString("Config Version") != configversion){
            Bukkit.getLogger().severe("Your config file is outdated! Your version: " + getConfig().getString("Config Version") + "required version: " + configversion + " check discord for how to update it or delete the file! Disabling......");
            new File("config.yml").renameTo(new File(new File(p.getDataFolder(), "AdvancedWhiteList"), "configBackup.yml"));
            //Bukkit.getServer().getPluginManager().disablePlugin(p);
        }

        mfile = new File(p.getDataFolder(), "message.yml");
        if(!mfile.exists()){
            copy(p.getResource("message.yml"), mfile);
        }
		message = YamlConfiguration.loadConfiguration(mfile);

    }


    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig(){
        try{
            config.save(cfile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void reloadConfig(){
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public FileConfiguration getMessage() {
        return message;
    }

    public void saveMessage(){
        try {
            message.save(mfile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void reloadMessage(){
        message = YamlConfiguration.loadConfiguration(mfile);
    }

    public PluginDescriptionFile getDesc(){
        return p.getDescription();
    }
    
    public void copy(InputStream in, File file){
        try{
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/

	
/*
	public void writeDefault() {
		try {
            File mainConfigFile = new File(this.m.getDataFolder(), "config.yml");
            Config mainConfig = new Config(this, mainConfigFile);
            FileConfiguration mainConfigLoad = mainConfig.getFileConfiguration();
			String configString = mainConfigLoad.saveToString();
			BufferedWriter writer = new BufferedWriter(new FileWriter("config.yml"));
//			FileOutputStream writer = new FileOutputStream(new File(this.m.getDataFolder() + File.separator + "config.yml"));
            writer.write(this.prepareConfigString(configString)); 
			InputStream out = WLStorage.class.getResourceAsStream("config.yml");
			byte[] linebuffer = new byte[4096];
			int lineLength = 0;
			while((lineLength = out.read(linebuffer)) > 0)
			{
			writer.write(linebuffer, 0, lineLength);
			}
            writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		m.saveConfig();
	} 

    private String prepareConfigString(String configString) {
        String[] lines = configString.split("\n");
        StringBuilder config = new StringBuilder();
        for (String line : lines) {
            if (line.contains(this.m.getDescription().getName() + "_COMMENT")) {
                config.append(line.replace("IMPORTANT", "[!]").replace("\n", "").replace(new StringBuilder().append(this.m.getDescription().getName()).append("_COMMENT_").toString(), "#").replaceAll("[0-9]+:", "") + "\n");
                continue;
            }
            if (!line.contains(":")) continue;
            config.append(line + "\n");
        }
        return config.toString();
    }
	
	public static class Config {
        private File configFile;
        private FileConfiguration configLoad;

        public Config(WLStorage fileManager, File configPath) {
            this.configFile = configPath;
            this.configLoad = configPath.getName().equals("config.yml") ? YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(fileManager.getConfigContent(this.configFile))) : YamlConfiguration.loadConfiguration((File)configPath);
        }

        public File getFile() {
            return this.configFile;
        }

        public FileConfiguration getFileConfiguration() {
            return this.configLoad;
        }

        public FileConfiguration loadFile() {
            this.configLoad = YamlConfiguration.loadConfiguration((File)this.configFile);
            return this.configLoad;
        }
    }
	
	public InputStream getConfigContent(Reader reader) {
        try {
            String currentLine;
            String pluginName = this.m.getName();
            int commentNum = 0;
            StringBuilder whole = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(reader);
            while ((currentLine = bufferedReader.readLine()) != null) {
                if (currentLine.contains("#")) {
                    String addLine = currentLine.replace("[!]", "IMPORTANT").replace(":", "-").replaceFirst("#", pluginName + "_COMMENT_" + commentNum + ":");
                    whole.append(addLine + "\n");
                    ++commentNum;
                    continue;
                }
                whole.append(currentLine + "\n");
            }
            String config = whole.toString();
            ByteArrayInputStream configStream = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8));
            bufferedReader.close();
            return configStream;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public InputStream getConfigContent(File configFile) {
        if (!configFile.exists()) {
            return null;
        }
        try {
            return this.getConfigContent(new FileReader(configFile));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }*/
/*	public static String getConfigVersion() {
		return this.ConfigVersion;
	}*/

}



