package com.gmail.shadoruncegaming.advancedwhitelist;

import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WLCmd implements CommandExecutor {
	private AdvancedWhiteList m;
	String prefix = "&6&lA&e&lWL > &7";

    public WLCmd(AdvancedWhiteList m) {
        this.m = m;
    }

	public boolean onCommand(CommandSender snd, Command pluginCommand, String chatCommand, String[] strings) {	
		if (!snd.hasPermission("advancedwhitelist.admin") || !snd.hasPermission("easywhitelist.admin")) {
			Utility.sendMsg(snd, prefix);
			return true;
		} if (strings.length == 0) {
			this.getStatus(snd);
			return true;
		} else {
			this.remanage(snd, strings);
			return true;
		}
	}


	private void remanage(CommandSender snd, String[] args) {
		String names;
		Long time;
		String cmd = args[0].toLowerCase();

		Utility.sendMsg(snd, "&6&lAdvanced&a&lWhitelist &7>");
		
		if (args.length == 0) {
			this.getStatus(snd);
			return;
		}

		switch(cmd) {
			case "info":
			case "inf":
			case "in":
			case "i":
			case "status":
			case "s":
			default:
				this.getStatus(snd);
				return;
				
			case "messages":
			case "message":
			case "msgs":
			case "msg":
				this.getMsgs(snd);
				return;
				
			case "help":
			case "h":
			case "?":
				this.getHelp(snd,args);
				return;
				
			case "wlgui":
			case "gui":
				if (snd instanceof Player) {
					WLGui.openInventory((Player) snd);
					return;
				}

				Utility.sendMsg(snd, "&7This is a player only Command.");
				return;
				
				
			case "spigot":
				Utility.sendMsg(snd, "&6Feel free to visit the plugin page at https://www.spigotmc.org/resources/advancedwhitelist-lite.78612");
			
			case "reload":
			case "rel":
			case "rl":
				this.m.getStorage().reload();
				return;
			
			case "remove":
			case "rem":
			case "re":
			case "r":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a name!");
					return;
				}
				names = args[1];
				this.m.getStorage().removeWhitelist(names);
				Utility.sendMsg(snd, prefix + "Whitelist removed for &c" + names);
				return;
			
			case "add":
			case "ad":
			case "a":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a name!");
					return;
				}
	
				names = args[1];
				this.m.getStorage().addWhitelist(names);
				Utility.sendMsg(snd, prefix + "Whitelisted &a" + names);
				return;
			
			case "list":
			case "li":
			case "l":
				names = "";
	
				String str;
				for (Iterator<String> var6 = this.m.getStorage().getWhiteLists().iterator(); var6
						.hasNext(); names = names + str + "&e&l, &7") {
					str = (String) var6.next();
				}
	
				Utility.sendMsg(snd, "&a&lWhitelisted: &7" + names);
				return;
			
			case "whitelist":
			case "wl":
				this.getStatus(snd);
				return;
			case "whiteliston":
			case "wlon":
			case "on":
				this.m.getStorage().setWhitelist(true);
				Utility.sendMsg(snd, prefix + "&fWhitelist is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
				
			case "whitelistoff":
			case "fullblockoff":
			case "wloff":
			case "off":
			case "of":
				this.m.getStorage().setWhitelist(false);
				Utility.sendMsg(snd, prefix + "&fWhitelist is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
				
			case "servercooldown":
			case "scooldown":
			case "cooldown":
			case "servercd":
			case "scd":
			case "cd":
				Utility.sendMsg(snd, "&e> &fServer Start to Player Login Cooldown Enabled: &e" + this.m.getStorage().isServerCooldown());
				return;
			case "servercooldownon":
			case "scooldownon":
			case "cooldownon":
			case "servercdon":
			case "scdon":
			case "cdon":
				this.m.getStorage().setServerCooldown(true);
				Utility.sendMsg(snd, prefix + "&fServer Start to Player Login Cooldown is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;

			case "servercooldownoff":
			case "scooldownoff":
			case "cooldownoff":
			case "servercdoff":
			case "scdoff":
			case "cdoff":
			case "cdof":
				this.m.getStorage().setServerCooldown(false);
				Utility.sendMsg(snd, prefix + "&fServer Start to Player Login Cooldown is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
				
			case "servercooldowntimetest":
			case "scooldowntimetest":
			case "cooldowntimetest":
			case "servercdtimetest":
			case "scdtimetest":
			case "cdtimetest":
			case "scdttest":
			case "cdttest":
			case "test":
				this.m.setStart();
				Utility.sendMsg(snd, prefix + "&fCooldown reset to now. This is only used for testing purposes only.");
				Utility.sendMsg(snd, prefix + "&fPlayers will need to wait " + m.getStorage().getServerCooldown() + " seconds.");
				Utility.sendMsg(snd, prefix + "&fProjectTeam will need to wait " + (m.getStorage().getServerCooldown()/2) + " seconds.");
				Utility.sendMsg(snd, prefix + "&fOperators will need to wait " + m.getStorage().getServerCooldown()/3 + " seconds.");
				return;
				
			case "servercooldowntime":
			case "scooldowntime":
			case "cooldowntime":
			case "servercdtime":
			case "scdtime":
			case "cdtime":
			case "scdt":
			case "cdt":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a time in seconds!");
					return;
				}
				try {
					time = Long.parseLong(args[1]);
					this.m.getStorage().setServerCooldownTime(time);
					Utility.sendMsg(snd, prefix + "Server Start to Player Login Cooldown is now: &e" + time);
					return;
				}
				catch (Exception e) {
					Utility.sendMsg(snd, "&7Please only input numbers for a time in seconds!");
					return;
				}
			

			case "setlobby":
			case "sethub":
			case "lobby":
			case "hub":
			case "lobbyserver":
			case "hubserver":
			case "server":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a message!");
					return;
				}
				this.m.getStorage().setHubServer(args[1]);
				Utility.sendMsg(snd, prefix + "New Hub Server Set: &a" + args[1]);
				return;
				

			case "senddelay":
			case "startdelay":
			case "sd":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a time in seconds!");
					return;
				}
				try {
					time = Long.parseLong(args[1]);
					this.m.getStorage().setDelayBeforeStartingKicks(time);
					Utility.sendMsg(snd, prefix + "Server Start to Player Login Cooldown is now: " + time);
					return;
				}
				catch (Exception e) {
					Utility.sendMsg(snd, "&7Please only input numbers for a time in seconds!");
					return;
				}
			

			case "kickdelay":
			case "kd":
			case "perplayerdelay":
			case "ppd":
			case "pd":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a time in seconds!");
					return;
				}
				try {
					time = Long.parseLong(args[1]);
					this.m.getStorage().setKickDelayPerPlayer(time);
					Utility.sendMsg(snd, this.prefix + "Server Start to Player Login Cooldown is now: " + time);
					return;
				}
				catch (Exception e) {
					Utility.sendMsg(snd, "&7Please only input numbers for a time in seconds!");
					return;
				}
				
				
			case "notwhitelistedmessage":
			case "notwhitelitsedmsg":
			case "nowlmsg":
			case "nowl":
			case "wlmsg":
			case "wlm":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a message!");
					return;
				}
				this.m.getStorage().setNotWhitelistMsg(msgOnly(args));
				Utility.sendMsg(snd, prefix + "New Send Message &a" + msgOnly(args));
				return;

			case "broadcastmessage":
			case "broadcastmsg":
			case "bcastmessage":
			case "bcastmsg":
			case "bcmsg":
			case "bmsg":
			case "bm":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a message!");
					return;
				}
				this.m.getStorage().setBroadcastMsg(msgOnly(args));
				Utility.sendMsg(snd, prefix + "New Send Message: " + msgOnly(args));
				return;

			case "sendmessage":
			case "sendmsg":
			case "smsg":
			case "sm":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a message!");
					return;
				}
				this.m.getStorage().setSendMsg(msgOnly(args));
				Utility.sendMsg(snd, prefix + "New Send Message: " + msgOnly(args));
				return;

			case "kickmessage":
			case "kickmsg":
			case "kmsg":
			case "km":
				if (args.length < 2) {
					Utility.sendMsg(snd, "&7Please input a message!");
					return;
				}
				this.m.getStorage().setKickMsg(msgOnly(args));
				Utility.sendMsg(snd, prefix + "New Send Message: " + msgOnly(args));
				return;
			
			case "projectteamaccess":
			case "projectteam":
			case "pta":
				Utility.sendMsg(snd, "&e> &fProjectTeam Access Enabled: " + this.m.getStorage().isProjectTeamAccess());
				return;
				
			case "projectteamon":
			case "projecton":
			case "teamon":
			case "ptaon":
			case "pon":
				this.m.getStorage().setProjectTeamAccess(true);
				Utility.sendMsg(snd, prefix + "&fProjectTeamAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
			case "projectteamoff":
			case "projectoff":
			case "teamoff":
			case "ptaoff":
			case "poff":
			case "pof":
				this.m.getStorage().setProjectTeamAccess(false);
				Utility.sendMsg(snd, prefix + "&fProjectTeamAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
			
			case "staffaccess":
			case "sa":
				Utility.sendMsg(snd, "&e> &fStaff Enabled: &e" + this.m.getStorage().isStaffAccess());
				return;
				
			case "staffon":
			case "saon":
			case "son":
				this.m.getStorage().setStaffAccess(true);
				Utility.sendMsg(snd, prefix + "&fStaffAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
			case "staffoff":
			case "saoff":
			case "soff":
			case "sof":
				this.m.getStorage().setStaffAccess(false);
				Utility.sendMsg(snd, prefix + "&fStaffAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
			
			case "testeraccess":
			case "ta":
				Utility.sendMsg(snd, "&e> &7Tester Enabled: &e" + this.m.getStorage().isTesterAccess());
				return;
				
			case "testeron":
			case "taon":
			case "ton":
				this.m.getStorage().setTesterAccess(true);
				Utility.sendMsg(snd, prefix + "&fTesterAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
			case "testeroff":
			case "taoff":
			case "toff":
			case "tof":
				this.m.getStorage().setTesterAccess(false);
				Utility.sendMsg(snd, prefix + "&fTesterAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
			
			case "alternateaccess":
			case "aa":
				Utility.sendMsg(snd, "&e> &7Alternate Enabled: &e" + this.m.getStorage().isAlternateAccess());
			return;
				
			case "alternateon":				
			case "alteon":
			case "aaon":
			case "aon":
				this.m.getStorage().setAlternateAccess(true);
				Utility.sendMsg(snd, prefix + "&fAlternateAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;				
			case "alternateoff":
			case "altoff":
			case "aaoff":
			case "aoff":
			case "aof":
				this.m.getStorage().setAlternateAccess(false);
				Utility.sendMsg(snd, prefix + "&fAlternateAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
			
			case "otheraccess":
			case "oa":
			case "oo":
				Utility.sendMsg(snd, "&e> &fOther Enabled: &e" + this.m.getStorage().isOtherAccess());
				return;
				
			case "otheron":
			case "oaon":
			case "ooon":
			case "oon":
				this.m.getStorage().setOtherAccess(true);
				Utility.sendMsg(snd, prefix + "&fOtherAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
			case "otheroff":
			case "oaoff":
			case "oooff":
			case "ooff":
			case "oof":
				this.m.getStorage().setOtherAccess(false);
				Utility.sendMsg(snd, prefix + "&fOtherAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
				
			case "configaccess":
			case "ca":
				Utility.sendMsg(snd, "&e> &fConfig Enabled: &e" + this.m.getStorage().isConfigAccess());
				return;
				
			case "configon":
			case "caon":
			case "con":
				this.m.getStorage().setConfigAccess(true);
				Utility.sendMsg(snd, prefix + "&fConfigAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
			case "configoff":
			case "caoff":
			case "coff":
			case "cof":
				this.m.getStorage().setConfigAccess(false);
				Utility.sendMsg(snd, prefix + "&fConfigAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
				
			case "fullblock":
			case "block":
			case "full":
			case "lockdown":
			case "alloff":
			case "allof":
				this.m.getStorage().setWhitelist(true);
				this.m.getStorage().setProjectTeamAccess(false);
				this.m.getStorage().setStaffAccess(false);
				this.m.getStorage().setTesterAccess(false);
				this.m.getStorage().setAlternateAccess(false);
				this.m.getStorage().setOtherAccess(false);
				this.m.getStorage().setConfigAccess(false);
				Utility.sendMsg(snd, prefix + "&cFull lockdown enabled!");
				Utility.sendMsg(snd, prefix + "&cYou will still need to send or kick unwanted players!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
				
			case "halfblock":
			case "staffonly":
				this.m.getStorage().setWhitelist(true);
				this.m.getStorage().setProjectTeamAccess(true);
				this.m.getStorage().setStaffAccess(true);
				this.m.getStorage().setTesterAccess(false);
				this.m.getStorage().setAlternateAccess(false);
				this.m.getStorage().setOtherAccess(false);
				this.m.getStorage().setConfigAccess(false);
				Utility.sendMsg(snd, prefix + "&6Server is set to staff only!");
				Utility.sendMsg(snd, prefix + "&cYou will still need to send or kick unwanted players!");
				Utility.sendMsg(snd, "&f=======================");
				this.getStatus(snd);
				return;
				
			case "kick":
			case "whitelistonly":
			case "wlonly":
			case "only":
			case "send":
			case "enforce":
				Utility.sendMsg(snd, prefix + "&6Whitelist is  " + this. m.getStorage().isWhitelisting());
					if (this.m.getStorage().isWhitelisting()) {
						Utility.broadcast(this.m.getStorage().getBroadcastMsg());
						Utility.sendMsg(snd, prefix + "&6Sending non-whitelisted players back to Lobby based on the status below!");
						getStatus(snd);
						try {
				            Thread.sleep(this.m.getStorage().getDelayBeforeStartingKicks()*1000);
				        } catch (InterruptedException e) {
				            e.printStackTrace();
				        }
						playerSendKick(snd);
						Utility.sendMsg(snd, prefix + "&aDone sending non-whitelisted players!");
						Utility.sendMsg(snd, prefix + "&cIf players are still connected, check the Whitelist settings.");
						return;
					}
			case "restart":
				Boolean currentWL = this.m.getStorage().isWhitelisting();
				Boolean currentCA = this.m.getStorage().isConfigAccess();
				Boolean currentPA = this.m.getStorage().isProjectTeamAccess();
				Boolean currentSA = this.m.getStorage().isStaffAccess();
				Boolean currentAA = this.m.getStorage().isAlternateAccess();
				Boolean currentTA = this.m.getStorage().isTesterAccess();
				Boolean currentOA = this.m.getStorage().isOtherAccess();

				this.m.getStorage().setWhitelist(true);
				this.m.getStorage().setProjectTeamAccess(false);
				this.m.getStorage().setStaffAccess(false);
				this.m.getStorage().setTesterAccess(false);
				this.m.getStorage().setAlternateAccess(false);
				this.m.getStorage().setOtherAccess(false);
				this.m.getStorage().setConfigAccess(false);				
				Utility.sendMsg(snd, prefix + "&6Whitelist is  " + this.m.getStorage().isWhitelisting());
					
				Utility.broadcast(this.m.getStorage().getBroadcastMsg());
				Utility.sendMsg(snd, prefix + "&6Sending non-whitelisted players back to Lobby based on the status below!");
				getStatus(snd);
				try {
		            Thread.sleep(this.m.getStorage().getDelayBeforeStartingKicks()*1000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				playerSendKick(snd);
				Utility.sendMsg(snd, prefix + "&aDone sending non-whitelisted players!");
				Utility.sendMsg(snd, prefix + "&cIf players are still connected, check the Whitelist settings.");

				this.m.getStorage().setWhitelist(currentWL);
				this.m.getStorage().setProjectTeamAccess(currentPA);
				this.m.getStorage().setStaffAccess(currentSA);
				this.m.getStorage().setTesterAccess(currentTA);
				this.m.getStorage().setAlternateAccess(currentAA);
				this.m.getStorage().setOtherAccess(currentOA);
				this.m.getStorage().setConfigAccess(currentCA);
				
				Bukkit.shutdown();
				
				return;
		}
	}
	
	void playerSendKick(CommandSender snd) {
		Integer playersCount = 0;
		Integer playersPerms = 0;
		Integer playersSent = 0;
		Integer playersKicked = 0;
		Integer playersLeft = 0;
		String playerList = "";
		for(Player player : Bukkit.getOnlinePlayers()) {
			Player p = player;
			String pname = p.getName();
			playersCount++;
			if (this.m.getEvent().permCheck(p)) {
				playersPerms++;
				playerList = playerList + pname + " ";
				continue;
			}
			else {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
	            DataOutputStream out = new DataOutputStream(bout);
				try {
					out.writeUTF("ConnectOther");
					out.writeUTF(pname);
					out.writeUTF(this.m.getStorage().getHubServer());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Utility.sendMsg(p, prefix + this.m.getStorage().getSendMsg());
				p.sendPluginMessage(this.m, "BungeeCord", bout.toByteArray());

				try {
		            Thread.sleep(this.m.getStorage().getKickDelayPerPlayer() * 1000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				if (!Bukkit.getOnlinePlayers().contains(p)) {
//					Utility.sendMsg(snd, prefix + "&eSent " + pname + " to lobby.");
					playersSent++;
				}
				if (Bukkit.getOnlinePlayers().contains(p)) {
					p.kickPlayer(this.m.getStorage().getKickMsg());
					playersKicked++;
//					Utility.sendMsg(snd, prefix + "&eKicked " + pname + " as lobby wasn't available or send failed.");
				}
			}
			for(@SuppressWarnings("unused") Player player2 : Bukkit.getOnlinePlayers()) {
				playersLeft++;
			}
			Utility.sendMsg(snd, prefix + "&e&lSend/Kick Results:");
			Utility.sendMsg(snd, prefix + "&6Players on server: &f" + playersCount);
			Utility.sendMsg(snd, prefix + "&6Players sent to Lobby: &f" + playersSent);
			Utility.sendMsg(snd, prefix + "&6Players kicked from Server: &f" + playersKicked);
			Utility.sendMsg(snd, prefix + "&6Players on server after kick/send: &f" + playersLeft);
			Utility.sendMsg(snd, prefix + "&6Players with Whitelist perms not kicked: &f" + playersPerms);
			Utility.sendMsg(snd, prefix + "&6Players with perms: &f" + playerList);
			Utility.sendMsg(snd, prefix + "&eNote: This information may not be correct, but if you send me information of what actually happened, such as what the result given was, how many were actually connected and how many were actually sent or not sent, I can attempt to troubleshoot this more. It's harder to keep testing without affecting productivity.");
		}
	}
	
	String msgOnly(String[] args) {
		String msg = "";
		int i;
		for (i=1; i< args.length;i++) {
			msg = msg+args[i]+" ";
		}
		return msg;
	}
	
	void getStatus(CommandSender snd) {
		Utility.sendMsg(snd, "&a&lWhitelist Status &7>");
		Utility.sendMsg(snd, "&e> &7Use &a&l/awl help &r&7for commands list.");
		Utility.sendMsg(snd, "&e> &7Use &a&l/awl msgs &r&7for Messages list.");
		Utility.sendMsg(snd, "&e> &7========== Access Settings ==========");
		Utility.sendMsg(snd, "&e> &7Whitelisting Enabled: &e" + this.m.getStorage().isWhitelisting());
		Utility.sendMsg(snd, "&e> &7Config Access Enabled: &e" + this.m.getStorage().isConfigAccess());
		Utility.sendMsg(snd, "&e> &7ProjectTeam Access Enabled: &e" + this.m.getStorage().isProjectTeamAccess());
		Utility.sendMsg(snd, "&e> &7Staff Access Enabled: &e" + this.m.getStorage().isStaffAccess());
		Utility.sendMsg(snd, "&e> &7Tester Access Enabled: &e" + this.m.getStorage().isTesterAccess());
		Utility.sendMsg(snd, "&e> &7Alternate Access Enabled: &e" + this.m.getStorage().isAlternateAccess());
		Utility.sendMsg(snd, "&e> &7Other Access Enabled: &e" + this.m.getStorage().isOtherAccess());
		Utility.sendMsg(snd, "&e> &7========== Misc Settings ==========");
		Utility.sendMsg(snd, "&e> &7Hub Server: &e" + this.m.getStorage().getHubServer());
		Utility.sendMsg(snd, "&e> &7Server Start to Player Login Cooldown Enabled: &e" + this.m.getStorage().isServerCooldown());
		Utility.sendMsg(snd, "&e> &7Server Cooldown time: &e" + this.m.getStorage().getServerCooldown() + " Seconds");
		Utility.sendMsg(snd, "&e> &7Delay time in seconds before starting kicks: &e" + this.m.getStorage().getDelayBeforeStartingKicks() + " Seconds");
		Utility.sendMsg(snd, "&e> &7Delay time between player being kicked and checked: &e" + this.m.getStorage().getKickDelayPerPlayer() + " Seconds");
	}
	
	void getMsgs(CommandSender snd) {

		Utility.sendMsg(snd, "&e> &7Not Whitelisted message: &e" + this.m.getStorage().getNotWhitelistMsg());
		Utility.sendMsg(snd, "&e> &7");
		Utility.sendMsg(snd, "&e> &7Send/Kick broadcast message: &e" + this.m.getStorage().getBroadcastMsg());
		Utility.sendMsg(snd, "&e> &7");
		Utility.sendMsg(snd, "&e> &7Message sent to player just before they are sent to Hub Server: &e" + this.m.getStorage().getSendMsg());
		Utility.sendMsg(snd, "&e> &7");
		Utility.sendMsg(snd, "&e> &7Message sent to player when a player is kicked from the network: &e" + this.m.getStorage().getKickMsg());
	}
	
	void HelpMenu(CommandSender snd) {
		Utility.sendMsg(snd, "&e> &7/awl &bhelp 1 - Base Help commands");
		Utility.sendMsg(snd, "&e> &7/awl &bhelp 2-3 - For more commands");
		Utility.sendMsg(snd, "&e> &7/awl &bhelp 4 - For more specific permissions");
		Utility.sendMsg(snd, "&e> &7/awl &bhelp 5-9 - Command Aliases");
	}
	
	
	void getHelp(CommandSender snd, String[] args) {
		this.HelpMenu(snd);
		
		if (args.length < 2 || args[1].equals("1")) {
			Utility.sendMsg(snd, "&e> &7/awl &bstatus/info");
			Utility.sendMsg(snd, "&e> &7/awl &bmessages - ");
			Utility.sendMsg(snd, "&e> &7/awl &aadd &f<name>");
			Utility.sendMsg(snd, "&e> &7/awl &cremove &f<name>");
			Utility.sendMsg(snd, "&e> &7/awl &flist");
			Utility.sendMsg(snd, "&e> &7/awl &a&lon &f/ &coff");
			Utility.sendMsg(snd, "&e> &7/awl &bscdon&7/&8scdoff &e- Server start to player login cooldown");
			Utility.sendMsg(snd, "&e> &7/awl &bscdtime <seconds>");
			Utility.sendMsg(snd, "&e> &7/awl &cwlonly/send/kick &e- Kicks players that aren't whitelisted.");
			Utility.sendMsg(snd, "&e> &7/awl &creload");
			Utility.sendMsg(snd, "&e> &7/awl &crestart &e- Kicks everyone except Operators and restarts the server.");
			return;
		}
		
		if (args[1].equals("2")) {
			Utility.sendMsg(snd, "&e> &7/awl &bnotwlmsg <message> &e- Msg sent to the player if they aren't whitelisted for a server.");
			Utility.sendMsg(snd, "&e> &7/awl &bbcastmsg <message> &e- Msg sent before everyone gets kicked from send/kick command.");
			Utility.sendMsg(snd, "&e> &7/awl &bsendmsg <message> &e- Msg sent to the player just before they're sent back to Hub/Lobby.");
			Utility.sendMsg(snd, "&e> &7/awl &bkickmsg <message> &e- Msg sent to player when they're kicked from the server.");
			Utility.sendMsg(snd, "&e> &7/awl &bhubserver <servername> &e- This is based on what's set in Bungee");
			Utility.sendMsg(snd, "&e> &7/awl &bsenddelay <seconds> &e- Amount of time a message is displayed before players are sent and kicked from the server.");
			Utility.sendMsg(snd, "&e> &7/awl &bkickdelay <seconds> &e- Amount of time between attempting to send a player before checking if kick is needed, also delay per kicked player to not overload bungee server.");
			return;
		}
		if (args[1].equals("3")) {
			Utility.sendMsg(snd, "&e> &7Access Commands: &aAdvancedWhiteList.Bypass.&b<Role>");
			Utility.sendMsg(snd, "&e> Whitelist: &7/awl <wlon/on or wloff/off &f- &bOperators");
			Utility.sendMsg(snd, "&e> ProjectTeamAccess: &7/awl <ptaon/ptaoff> &f- &bProjectTeam");
			Utility.sendMsg(snd, "&e> StaffAccess: &7/awl <saon/saoff> &f- &bStaff");
			Utility.sendMsg(snd, "&e> TesterAccess: &7/awl <taon/taoff> &f- &bTester");
			Utility.sendMsg(snd, "&e> AlternateAccess: &7/awl <aaon/aaoff> &f- &bAlternateAccess");
			Utility.sendMsg(snd, "&e> OtherAccess: &7/awl <oaon/oaoff> &f- &bOther");
			Utility.sendMsg(snd, "&e> ConfigListAccess: &7/awl <caon/caoff>");
			Utility.sendMsg(snd, "&e> Lockdown: &7/awl <fullblock, block, full, lockdown, alloff>");
			Utility.sendMsg(snd, "&e> Staff Only: &7/awl <halfblock, staffonly>");
			return;
		}

		if (args[1].equals("4")) {
			Utility.sendMsg(snd, "&e> &7Permissions:");
			Utility.sendMsg(snd, "&e> &7Command access: &6AdvancedWhiteList.Admin");
			Utility.sendMsg(snd, "&e> &7Full Whitelist Bypass: &6AdvancedWhiteList.Bypass.&bOperators");
			Utility.sendMsg(snd, "&e> &7ProjectTeam Access Bypass: &6AdvancedWhiteList.Bypass.&bProjectTeam");
			Utility.sendMsg(snd, "&e> &7Staff Access Bypass: &6AdvancedWhiteList.Bypass.&bStaff");
			Utility.sendMsg(snd, "&e> &7Tester Access Bypass: &6AdvancedWhiteList.Bypass.&bTester");
			Utility.sendMsg(snd, "&e> &7Alternate Bypass: &6AdvancedWhiteList.Bypass.&bAlternate");
			Utility.sendMsg(snd, "&e> &7Other Access Bypass: &6AdvancedWhiteList.Bypass.&bOther");
			return;
		}

		if (args[1].equals("5")) {
			Utility.sendMsg(snd, "&e> advancedwhitelist, advancedwl, awhitelist, advwl, awl, easywhitelist, easywl, ewhitelist, ewl, ezwl, whitelist, wl, wlist");
			Utility.sendMsg(snd, "&e> status, s, info, inf, in, i");
			Utility.sendMsg(snd, "&e> messages, message, msgs, msg");
			Utility.sendMsg(snd, "&e> help, h, ?");
			Utility.sendMsg(snd, "&e> reload, rel, rl");
			Utility.sendMsg(snd, "&e> remove, rem, re, r");
			Utility.sendMsg(snd, "&e> add, ad, a");
			Utility.sendMsg(snd, "&e> list, li, l");
			return;
		}

		if (args[1].equals("6")) {
			Utility.sendMsg(snd, "&e> whitelist, wl");
			Utility.sendMsg(snd, "&e> whiteliston, wlon, on");
			Utility.sendMsg(snd, "&e> whitelistoff, fullblockoff, wloff, off, of");
			Utility.sendMsg(snd, "&e> servercooldown, scooldown, cooldown, servercd, scd, cd");
			Utility.sendMsg(snd, "&e> servercooldownon, scooldownon, cooldownon, servercdon, scdon, cdon");
			Utility.sendMsg(snd, "&e> servercooldownoff, scooldownoff, cooldownoff, servercdoff, scdoff, cdoff");
			Utility.sendMsg(snd, "&e> servercooldowntime, scooldowntime, cooldowntime, servercdtime, scdtime, cdtime, scdt, cdt");
			return;
		}

		if (args[1].equals("7")) {
			Utility.sendMsg(snd, "&e> setlobby, sethub, lobby, hub, lobbyserver, hubserver, server");
			Utility.sendMsg(snd, "&e> senddelay, startdelay, sd");
			Utility.sendMsg(snd, "&e> kickdelay, perplayerdelay, ppd, pd, kd");
			Utility.sendMsg(snd, "&e> notwhitelistedmessage, notwhitelistedmsg, notwlmsg, wlmsg, wlm");
			Utility.sendMsg(snd, "&e> broadcastmessage, broadcastmsg, bcastmessage, bcastmsg, bcmsg, bmsg, bm");
			Utility.sendMsg(snd, "&e> sendmessage, sendmsg, smsg, sm");
			Utility.sendMsg(snd, "&e> kickmessage, kickmsg, kmsg, km");
			return;
		}

		if (args[1].equals("8")) {
			Utility.sendMsg(snd, "&e> projectteamaccess, projectteam, pta");
			Utility.sendMsg(snd, "&e> projectteamon, projecton, teamon, ptaon, pon");
			Utility.sendMsg(snd, "&e> projectteamoff, projectoff, teamoff, ptaoff, poff");
			Utility.sendMsg(snd, "&e> staffaccess, sa");
			Utility.sendMsg(snd, "&e> staffon, saon, son");
			Utility.sendMsg(snd, "&e> staffoff, saoff, soff");
			Utility.sendMsg(snd, "&e> testeraccess, ta");
			Utility.sendMsg(snd, "&e> testeron, taon, ton");
			Utility.sendMsg(snd, "&e> testeroff, taoff, toff");
			return;
		}

		if (args[1].equals("9")) {
			Utility.sendMsg(snd, "&e> alternateaccess, aa");
			Utility.sendMsg(snd, "&e> alternateon, alteon, aaon, aon");
			Utility.sendMsg(snd, "&e> alternateoff, altoff, aaoff, aoff");
			Utility.sendMsg(snd, "&e> otheraccess, oa, oo");
			Utility.sendMsg(snd, "&e> otheron, oaon, ooon, oon");
			Utility.sendMsg(snd, "&e> otheroff, oaoff, oooff, ooff");
			Utility.sendMsg(snd, "&e> configaccess, ca");
			Utility.sendMsg(snd, "&e> configon, caon, con");
			Utility.sendMsg(snd, "&e> configoff, caoff, coff");
			Utility.sendMsg(snd, "&e> fullblock, block, full, lockdown, alloff");
			Utility.sendMsg(snd, "&e> halfblock, staffonly");
			Utility.sendMsg(snd, "&e> kick, whitelistonly, wlonly, only, send, enforce");
			return;
		}
		
		else {
			return;
		}
	}
}			