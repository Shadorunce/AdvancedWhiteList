package com.gmail.shadoruncegaming.advancedwhitelist;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class AdvancedWhiteList extends JavaPlugin implements PluginMessageListener {
	public static Long start = System.currentTimeMillis();
	private static AdvancedWhiteList instance;
	private WLGui gui;
	private WLStorage storage;
	private WLEvent event;
	String configVersion = "1.1.3";

	public void onEnable() {
		instance = this;
        int pluginId = 7676;
        Metrics metrics = new Metrics((Plugin)this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
		start = System.currentTimeMillis();
		storage = new WLStorage(this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", (PluginMessageListener) this);
		setupConfig();
		getCommand("advancedwhitelist").setExecutor(new WLCmd(this));
		getServer().getPluginManager().registerEvents(new WLEvent(), this);
		getServer().getPluginManager().registerEvents(new WLGui(), this);
		event = new WLEvent();
    	gui = getGUI();
		Utility.sendConsole("&6&lAdvanced&a&lWhitelist &7> Loaded!");
		getStorage();
		WLStorage.reload();
	}

	public void setStart() {
		AdvancedWhiteList.start = System.currentTimeMillis();
		return;
	}
	
	public void setupConfig() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		saveConfig();
	}
	
	public static AdvancedWhiteList getInstance() {
		return instance;
	}

	public void onDisable() {
		
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