package com.gmail.shadoruncegaming.advancedwhitelist;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class AdvancedWhiteList extends JavaPlugin implements PluginMessageListener {
	public Long start = System.currentTimeMillis();
	private static AdvancedWhiteList instance;
	private WLGui gui;
	private WLStorage storage;
	private WLEvent event;
	String configVersion = "1.1.3";

	public void onEnable() {
		instance = this;
		start = System.currentTimeMillis();
		this.storage = new WLStorage(this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", (PluginMessageListener) this);
//		getStorage().buildConfig();
		this.saveDefaultConfig();
/*		if (!new File(this.getDataFolder(), "config.yml").exists()) {
			this.getStorage().writeDefault();
		}*/
//		this.getStorage().setup(this);
		this.saveConfig();
		this.getCommand("advancedwhitelist").setExecutor(new WLCmd(this));
		this.getServer().getPluginManager().registerEvents(new WLEvent(this), this);
		this.getServer().getPluginManager().registerEvents(new WLGui(this), this);
		this.event = new WLEvent(this);
    	this.gui = this.getGUI();
		Utility.sendConsole("&6&lAdvanced&a&lWhitelist &7> Loaded!");
/*		if (getStorage().getConfigVersion() != configVersion) {
			new File("config.yml").renameTo(new File(new File(this.getDataFolder(), "AdvancedWhiteList"), "configBackup.yml"));
			this.saveDefaultConfig();
			//this.getStorage().setup(this);
		}*/
		getStorage().reload();
	}

	public void setStart() {
		this.start = System.currentTimeMillis();
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