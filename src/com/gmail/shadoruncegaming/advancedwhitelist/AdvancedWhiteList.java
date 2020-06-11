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
		this.storage = new WLStorage(this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", (PluginMessageListener) this);
		this.saveDefaultConfig();
		this.saveConfig();
		this.getCommand("advancedwhitelist").setExecutor(new WLCmd(this));
		this.getServer().getPluginManager().registerEvents(new WLEvent(), this);
		this.getServer().getPluginManager().registerEvents(new WLGui(), this);
		this.event = new WLEvent();
    	this.gui = this.getGUI();
		Utility.sendConsole("&6&lAdvanced&a&lWhitelist &7> Loaded!");
		getStorage();
		WLStorage.reload();
	}

	public void setStart() {
		AdvancedWhiteList.start = System.currentTimeMillis();
		return;
	}
	
	public static AdvancedWhiteList getInstance() {
		return instance;
	}

	public void onDisable() {
	}
	
	public WLEvent getEvent() {
		return this.event;
	}

	public WLStorage getStorage() {
		return this.storage;
	}
	
	public WLGui getGUI() {
		return this.gui;
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