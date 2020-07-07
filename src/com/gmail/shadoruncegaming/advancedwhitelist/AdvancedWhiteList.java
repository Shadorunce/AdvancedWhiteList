package com.gmail.shadoruncegaming.advancedwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.milkbowl.vault.permission.Permission;

public class AdvancedWhiteList extends JavaPlugin implements PluginMessageListener {
	private static Permission perms = null;
	public static Long start = System.currentTimeMillis();
	private static AdvancedWhiteList instance;
	private WLGui gui;
	private WLStorage storage;
	private WLEvent event;

	public void onEnable() {
		instance = this;
        int pluginId = 7676;
        Metrics metrics = new Metrics((Plugin)this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
		start = System.currentTimeMillis();
		storage = new WLStorage(this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", (PluginMessageListener) this);
		getCommand("advancedwhitelist").setExecutor(new WLCmd(this));
		getServer().getPluginManager().registerEvents(new WLEvent(), this);
		getServer().getPluginManager().registerEvents(new WLGui(this), this);
		event = new WLEvent();
    	gui = getGUI();
		getStorage();
		setupConfig();
		WLStorage.reload();
		if (hasVault()) {
			setupPermissions();
			Utility.sendConsole("Vault has been found and offline player permissions can be cheked.");
		}
		if (!hasVault()) Utility.sendConsole("Vault isn't loaded, won't be able to get offline player permissions.");
		//WLCmd.getStatus(Bukkit.getConsoleSender());
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("advancedwhitelist.admin")) Utility.sendMsg(p, "§bAdvancedWhiteList has loaded or been reloaded!");
		}
		Utility.sendConsole("&6&lAdvanced&a&lWhitelist &7> Loaded!");
	}
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
	public static boolean hasVault() {
		return Bukkit.getPluginManager().isPluginEnabled("Vault");
	}
	
	public static Permission getPermissions() {
        return perms;
    }

	public void setStart() {
		AdvancedWhiteList.start = System.currentTimeMillis();
		return;
	}
	
	public void setupConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		WLStorage.versionCheck();
		saveConfig();
	}
	
	public static AdvancedWhiteList getInstance() {
		return instance;
	}

	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("advancedwhitelist.admin")) Utility.sendMsg(p, "§cAdvancedWhiteList has been disabled!");
		}
	}
	
	public WLEvent getEvent() {
		return event;
	}

	public WLStorage getStorage() {
		return storage;
	}
	
	public WLGui getGUI() {
		return gui;
	}
	
	
	public void onPluginMessageReceived(String channel, Player player, byte[] message){
		   if(!channel.equals("BungeeCord")){
		     return;
		   }
		   ByteArrayDataInput in = ByteStreams.newDataInput(message);
		   String subchannel = in.readUTF();

		   if (subchannel.contains("From BungeeCordAuthMe.v2.Broadcast") || subchannel.contains("From BungeeCordCMIPlayerFeedback")) {
				Utility.sendConsole("&eE-Whitelist > &7From BungeeCord" + subchannel);
		   }
		}
	}