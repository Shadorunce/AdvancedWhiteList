package com.gmail.shadoruncegaming.advancedwhitelist;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.permission.Permission;

public class WLCmd implements CommandExecutor {
	private static AdvancedWhiteList m;

	static Permission perms = AdvancedWhiteList.getPermissions();
	static String prefix = "&6&lA&e&lWL > &7";
	static String names;

	public WLCmd(AdvancedWhiteList m) {
		WLCmd.m = m;
	}

	public boolean onCommand(CommandSender snd, Command pluginCommand, String chatCommand, String[] strings) {
		
		if (snd.isOp() || snd.hasPermission("*")) {
			if (snd instanceof Player && !WLStorage.isOpBypass()) {
				boolean hasAwlPerm = false;
				for (PermissionAttachmentInfo perm : snd.getEffectivePermissions()) {
					if (perm.getPermission().equalsIgnoreCase("advancedwhitelist.admin") || perm.getPermission().equalsIgnoreCase("easywhitelist.admin")) hasAwlPerm = true;
				}
				if (hasAwlPerm == false) {
					Utility.sendMsg(snd, prefix);
					Utility.sendConsole(snd.getName() + " attempted to access AWL but is missing permission: advancedwhitelist.admin");
					return true;
				}
			}
		}
		
		if (!snd.hasPermission("advancedwhitelist.admin") && !snd.hasPermission("easywhitelist.admin")) {
			Utility.sendMsg(snd, prefix);
			Utility.sendConsole(snd.getName() + " attempted to access AWL but is missing permission: advancedwhitelist.admin");
			return true;
		}

		if (strings.length == 0) {
			if (snd instanceof Player) {
				TextComponent prfx = new TextComponent("AWL > ");
				prfx.setColor(ChatColor.GOLD);
				prfx.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to open GUI for ").color(ChatColor.GOLD).append("Advanced").color(ChatColor.GOLD).append("WhiteList").color(ChatColor.GREEN).create()));
				prfx.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/awl gui"));
				snd.spigot().sendMessage(prfx);
				WLGui.openInventory((Player) snd);
				return true;
			}
			if (!(snd instanceof Player)) {
				Utility.sendMsg(snd, "&6&lA&a&lWL&7>");
				getStatus(snd);
				return true;
			}

		} else {
			remanage(snd, strings);
			return true;
		}
		return true;
	}

	private void remanage(CommandSender snd, final String[] args) {
		Long time;
		String cmd = args[0].toLowerCase();
		
		if (args.length == 0) {
			getStatus(snd);
			return;
		}

		switch(cmd) {
			case "messages":
			case "message":
			case "msgs":
			case "msg":
				getStatus2(snd);
				return;

			case "help":
			case "h":
			case "?":
				String arg = "1";
				if (args.length >= 2) arg = args[1];
				getHelp(snd,arg);
				return;

			case "spigot":
				Utility.sendMsg(snd, "&6Feel free to visit the plugin page at https://www.spigotmc.org/resources/advancedwhitelist-lite.78612");

			case "reload":
			case "rel":
			case "rl":
				WLStorage.reload();
				return;

			case "remove":
			case "rem":
			case "re":
			case "r":
				removePlayer(snd, args);
				return;

			case "add":
			case "ad":
			case "a":
				addPlayer(snd, args);
				return;

			case "list":
			case "li":
			case "l":
				listPlayers(snd);
				return;

			case "addallplayers":
			case "addallplayer":
			case "allplayers":
			case "allplayer":
			case "addplayers":
			case "addall":
			case "aall":
				addAllPlayers();
				return;

			case "resetlist":
			case "reset":
			case "listreset":
			case "clearlist":
			case "listclear":
			case "removeall":
			case "clear":
			case "rall":
				WLStorage.clearWhiteLists();
				return;

			case "info":
			case "inf":
			case "in":
			case "i":
			case "status":
			case "s":
			case "whitelist":
			case "wl":
				if (args.length == 1) getStatus(snd);
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("on")) {
						WLStorage.setWhitelist(true);
						Utility.sendMsg(snd, prefix + "&fWhitelist is now &a&lON&f!");
						getStatus(snd);
					}
					if (args[1].equalsIgnoreCase("off")) {
						WLStorage.setWhitelist(false);
						Utility.sendMsg(snd, prefix + "&fWhitelist is &c&lOFF!&8");
					}
					else getStatus(snd,args[1]);
				}
				return;
			case "whiteliston":
			case "wlon":
			case "on":
				WLStorage.setWhitelist(true);
				Utility.sendMsg(snd, prefix + "&fWhitelist is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;

			case "whitelistoff":
			case "fullblockoff":
			case "wloff":
			case "off":
			case "of":
				WLStorage.setWhitelist(false);
				Utility.sendMsg(snd, prefix + "&fWhitelist is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;

			case "opbypass":
			case "bypass":
			case "op":
				if (args.length == 1) WLStorage.isOpBypass();
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("on")) {
						WLStorage.setOpBypass(true);
						Utility.sendMsg(snd, prefix + "&fOP Bypass is now &c&lON&f!");
					}
					if (args[1].equalsIgnoreCase("off")) {
						WLStorage.setOpBypass(false);
						Utility.sendMsg(snd, prefix + "&fOP Bypass is &a&lOFF!&8");
					}
					getStatus(snd);
				}
				return;
				
			case "servercooldown":
			case "scooldown":
			case "cooldown":
			case "servercd":
			case "scd":
			case "cd":
				if (args.length == 1) Utility.sendMsg(snd, "&e> &fServer Start to Player Login Cooldown Enabled: &e" + WLStorage.isServerCooldown());
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("on")) {
						WLStorage.setServerCooldown(true);
						Utility.sendMsg(snd, prefix + "&fServer Start to Player Login Cooldown is now &a&lON&f!");
					}
					if (args[1].equalsIgnoreCase("off")) {
						WLStorage.setServerCooldown(false);
						Utility.sendMsg(snd, prefix + "&fServer Start to Player Login Cooldown is &c&lOFF!&8");
					}
				}
				return;
			case "servercooldownon":
			case "scooldownon":
			case "cooldownon":
			case "servercdon":
			case "scdon":
			case "cdon":
				WLStorage.setServerCooldown(true);
				Utility.sendMsg(snd, prefix + "&fServer Start to Player Login Cooldown is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			case "servercooldownoff":
			case "scooldownoff":
			case "cooldownoff":
			case "servercdoff":
			case "scdoff":
			case "cdoff":
			case "cdof":
				WLStorage.setServerCooldown(false);
				Utility.sendMsg(snd, prefix + "&fServer Start to Player Login Cooldown is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
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
				m.setStart();
				Utility.sendMsg(snd, prefix + "&fCooldown reset to now. This is only used for testing purposes only.");
				Utility.sendMsg(snd, prefix + "&fPlayers will need to wait " + WLStorage.getServerCooldown() + " seconds.");
				Utility.sendMsg(snd, prefix + "&fProjectTeam will need to wait " + (WLStorage.getServerCooldown()/2) + " seconds.");
				Utility.sendMsg(snd, prefix + "&fOperators will need to wait " + WLStorage.getServerCooldown()/3 + " seconds.");
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
					WLStorage.setServerCooldownTime(time);
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
				WLStorage.setHubServer(args[1]);
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
					WLStorage.setDelayBeforeStartingKicks(time);
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
					WLStorage.setKickDelayPerPlayer(time);
					Utility.sendMsg(snd, prefix + "Server Start to Player Login Cooldown is now: " + time);
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
				WLStorage.setNotWhitelistMsg(msgOnly(args));
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
				WLStorage.setBroadcastMsg(msgOnly(args));
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
				WLStorage.setSendMsg(msgOnly(args));
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
				WLStorage.setKickMsg(msgOnly(args));
				Utility.sendMsg(snd, prefix + "New Send Message: " + msgOnly(args));
				return;
			
			case "projectteamaccess":
			case "projectteam":
			case "pta":
				if (args.length == 1) Utility.sendMsg(snd, "&e> &fProjectTeamAccess Access Enabled: " + WLStorage.isProjectTeamAccess());
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("on")) {
						WLStorage.setProjectTeamAccess(true);
						Utility.sendMsg(snd, prefix + "&fProjectTeamAccess is now &a&lON&f!");
					}
					if (args[1].equalsIgnoreCase("off")) {
						WLStorage.setProjectTeamAccess(false);
						Utility.sendMsg(snd, prefix + "&fProjectTeamAccess is &c&lOFF!&8");
					}
				}
				return;

			case "projectteamon":
			case "projecton":
			case "teamon":
			case "ptaon":
			case "pon":
				WLStorage.setProjectTeamAccess(true);
				Utility.sendMsg(snd, prefix + "&fProjectTeamAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			case "projectteamoff":
			case "projectoff":
			case "teamoff":
			case "ptaoff":
			case "poff":
			case "pof":
				WLStorage.setProjectTeamAccess(false);
				Utility.sendMsg(snd, prefix + "&fProjectTeamAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			
			case "staffaccess":
			case "sa":
				if (args.length == 1) Utility.sendMsg(snd, "&e> &fStaffAccess Enabled: &e" + WLStorage.isStaffAccess());
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("on")) {
						WLStorage.setStaffAccess(true);
						Utility.sendMsg(snd, prefix + "&fStaffAccess is now &a&lON&f!");
					}
					if (args[1].equalsIgnoreCase("off")) {
						WLStorage.setStaffAccess(false);
						Utility.sendMsg(snd, prefix + "&fStaffAccess is &c&lOFF!&8");
					}
				}
				return;
				
			case "staffon":
			case "saon":
			case "son":
				WLStorage.setStaffAccess(true);
				Utility.sendMsg(snd, prefix + "&fStaffAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			case "staffoff":
			case "saoff":
			case "soff":
			case "sof":
				WLStorage.setStaffAccess(false);
				Utility.sendMsg(snd, prefix + "&fStaffAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			
			case "testeraccess":
			case "tester":
			case "ta":
				if (args.length == 1) Utility.sendMsg(snd, "&e> &fTesterAccess Enabled: &e" + WLStorage.isTesterAccess());
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("on")) {
						WLStorage.setTesterAccess(true);
						Utility.sendMsg(snd, prefix + "&fTesterAccess is now &a&lON&f!");
					}
					if (args[1].equalsIgnoreCase("off")) {
						WLStorage.setTesterAccess(false);
						Utility.sendMsg(snd, prefix + "&fTesterAccess is &c&lOFF!&8");
					}
				}
				return;
				
			case "testeron":
			case "taon":
			case "ton":
				WLStorage.setTesterAccess(true);
				Utility.sendMsg(snd, prefix + "&fTesterAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			case "testeroff":
			case "taoff":
			case "toff":
			case "tof":
				WLStorage.setTesterAccess(false);
				Utility.sendMsg(snd, prefix + "&fTesterAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			
			case "alternateaccess":
			case "alternate":
			case "alt":
			case "aa":
				if (args.length == 1) Utility.sendMsg(snd, "&e> &fAlternateAccess Enabled: &e" + WLStorage.isAlternateAccess());
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("on")) {
						WLStorage.setAlternateAccess(true);
						Utility.sendMsg(snd, prefix + "&fAlternateAccess is now &a&lON&f!");
					}
					if (args[1].equalsIgnoreCase("off")) {
						WLStorage.setAlternateAccess(false);
						Utility.sendMsg(snd, prefix + "&fAlternateAccess is &c&lOFF!&8");
					}
				}
				return;
				
			case "alternateon":				
			case "alteon":
			case "aaon":
			case "aon":
				WLStorage.setAlternateAccess(true);
				Utility.sendMsg(snd, prefix + "&fAlternateAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;				
			case "alternateoff":
			case "altoff":
			case "aaoff":
			case "aoff":
			case "aof":
				WLStorage.setAlternateAccess(false);
				Utility.sendMsg(snd, prefix + "&fAlternateAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			
			case "otheraccess":
			case "other":
			case "oa":
			case "oo":
			case "o":
				if (args.length == 1) Utility.sendMsg(snd, "&e> &fOtherAccess Enabled: &e" + WLStorage.isOtherAccess());
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("on")) {
						WLStorage.setAlternateAccess(true);
						Utility.sendMsg(snd, prefix + "&fOtherAccess is now &a&lON&f!");
					}
					if (args[1].equalsIgnoreCase("off")) {
						WLStorage.setAlternateAccess(false);
						Utility.sendMsg(snd, prefix + "&fOtherAccess is &c&lOFF!&8");
					}
				}
				return;
				
			case "otheron":
			case "oaon":
			case "ooon":
			case "oon":
				WLStorage.setOtherAccess(true);
				Utility.sendMsg(snd, prefix + "&fOtherAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			case "otheroff":
			case "oaoff":
			case "oooff":
			case "ooff":
			case "oof":
				WLStorage.setOtherAccess(false);
				Utility.sendMsg(snd, prefix + "&fOtherAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
				
			case "configaccess":
			case "config":
			case "ca":
			case "c":
				if (args.length == 1) Utility.sendMsg(snd, "&e> &fConfigAccess Enabled: &e" + WLStorage.isConfigAccess());
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("on")) {
						WLStorage.setAlternateAccess(true);
						Utility.sendMsg(snd, prefix + "&fConfigAccess is now &a&lON&f!");
					}
					if (args[1].equalsIgnoreCase("off")) {
						WLStorage.setAlternateAccess(false);
						Utility.sendMsg(snd, prefix + "&fConfigAccess is &c&lOFF!&8");
					}
				}
				return;
				
			case "configon":
			case "caon":
			case "con":
				WLStorage.setConfigAccess(true);
				Utility.sendMsg(snd, prefix + "&fConfigAccess is now &a&lON&f!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
			case "configoff":
			case "caoff":
			case "coff":
			case "cof":
				WLStorage.setConfigAccess(false);
				Utility.sendMsg(snd, prefix + "&fConfigAccess is &c&lOFF!&8");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				
				return;
				
			case "fullblock":
			case "block":
			case "full":
			case "lockdown":
			case "alloff":
			case "allof":
				WLStorage.setWhitelist(true);
				WLStorage.setProjectTeamAccess(false);
				WLStorage.setStaffAccess(false);
				WLStorage.setTesterAccess(false);
				WLStorage.setAlternateAccess(false);
				WLStorage.setOtherAccess(false);
				WLStorage.setConfigAccess(false);
				Utility.sendMsg(snd, prefix + "&cFull lockdown enabled!");
				Utility.sendMsg(snd, prefix + "&cYou will still need to send or kick unwanted players!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
				
			case "halfblock":
			case "half":
			case "staffonly":
				WLStorage.setWhitelist(true);
				WLStorage.setProjectTeamAccess(true);
				WLStorage.setStaffAccess(true);
				WLStorage.setTesterAccess(false);
				WLStorage.setAlternateAccess(false);
				WLStorage.setOtherAccess(false);
				WLStorage.setConfigAccess(false);
				Utility.sendMsg(snd, prefix + "&6Server is set to staff only!");
				Utility.sendMsg(snd, prefix + "&cYou will still need to send or kick unwanted players!");
				Utility.sendMsg(snd, "&f=======================");
				getStatus(snd);
				return;
				
			case "kick":
			case "whitelistonly":
			case "wlonly":
			case "sendall":
			case "only":
			case "enforce":
				sendPlayers(snd);
				return;
				
			case "send":
			case "sendplayer":
				sendPlayerToServer(snd,args);
				return;
				
				
			case "restart":
				restartServer(snd);
				return;

			case "convert":
				if (args.length == 1) {
					Utility.sendMsg(snd, prefix + "§6Convert commands §7> §d/awl convert <type>"
							+ "\n§dawl §f- §7Convert from an old config version of AdvancedWhiteList config file without version check"
							+ "\n§dewl §f- §7Convert from EasyWhiteList config file."
							+ "\n§dmc §f- §7Convert from Minecraft Whitelist. This will also set Mc Whitelist off, and add Whitelisted names to AWL list, but doesn't enable Config Access"
							+ "\n§dversion §f §7Runs Version check and converts as needed. This is the same as server start.");
					}
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("awl")) {WLStorage.convertConfig();}
					if (args[1].equalsIgnoreCase("ewl")) {WLStorage.convertEWLConfig();}
					if (args[1].equalsIgnoreCase("mc")) {WLStorage.convertMcConfig();}
					if (args[1].equalsIgnoreCase("version")) {WLStorage.versionCheck();}
					Utility.sendMsg(snd, "Conversion finished. Check console more additional messages.");
				}
				return;
				
			case "uuid":
				Utility.uuid(snd, args);
				return;
				
			case "checkaccess":
			case "checkplayer":
			case "player":
			case "check":
			case "access":
				accessCheck(snd,args);
				return;
				
			case "listgui":
			case "guilist":
			case "players":
			case "playerlist":
			case "playergui":
				if (snd instanceof Player) {
					WLGui.playersGUI((Player) snd, 1);
					return;
				}
				else {
					Utility.sendMsg(snd, "§cSorry, GUI is only for players.");
					getStatus(snd);
				}
				return;
				
			case "awlgui":
			case "wlgui":
			case "gui":
			default:
				if (snd instanceof Player) {
					WLGui.openInventory((Player) snd);
					return;
				}
				else {
					Utility.sendMsg(snd, "§cSorry, GUI is only for players.");
					getStatus(snd);
				}
				return;
		}
	}
	
	private void sendPlayerToServer(CommandSender snd, String[] args) {
		if (args.length < 3) {
			snd.sendMessage("§cThe command is §6/awl send <player> <Bungeecord Server>");
			return;
		}
		Player p = null;
		String pname = args[1];
		if (snd instanceof Player) {
			p = (Player) snd;
		}
		if (!(snd instanceof Player)) {
			p = Bukkit.getPlayer(pname);
			if (p == null) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					p = player;
					break;
				}
			}
		}
		if (p == null) {
			snd.sendMessage("There were no players on the server to use as a Bungee send messenger.");
			return;
		}
		String otherServer = args[2];
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
		try {
			out.writeUTF("ConnectOther");
			out.writeUTF(pname);
			out.writeUTF(otherServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.sendMsg(p, prefix + WLStorage.getSendMsg());
		((PluginMessageRecipient) p).sendPluginMessage(m, "BungeeCord", bout.toByteArray());
		return;
	}

	@SuppressWarnings("deprecation")
	public static void accessCheck(CommandSender snd, String[] args) {
		boolean playerOnline = false;
		String core = "advancedwhitelist.bypass.";
		ArrayList<Boolean> permsList = new ArrayList<Boolean>(Arrays.asList(false,false,false,false,false,false,false));
		if (args.length == 1) {args[1] = snd.getName();}
		if (args[1].length() == 36 && args[1].contains("-")) {
			args[1] = Bukkit.getOfflinePlayer(UUID.fromString(args[1])).getName();
		}
		Utility.sendMsg(snd, prefix + " §6Access check for §d" + args[1] + "§6:");
		// If Player Online:
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getName().equalsIgnoreCase(args[1])) {
				Utility.sendMsg(snd, "§d" + args[1] + " §6is online.");
				playerOnline = true;
				Utility.sendMsg(snd, "§6Whitelist enabled: " + Utility.getTFColor(WLStorage.isWhitelisting()) + 
						"   §f|   §6Has access overall: " + Utility.getTFColor(WLEvent.permCheck(player)));
				Utility.sendMsg(snd, "§6Config access enabled: " + Utility.getTFColor(WLStorage.isConfigAccess()) + 
						"  §f|  §6On Config Whitelist: " + Utility.getTFColor(WLStorage.isWhitelisted(player.getName())));
				Utility.sendMsg(snd, "§6OP and * perm bypass enabled: " + Utility.getInvertedTFColor(WLStorage.isOpBypass()) + 
						"  §f|  §6OPed: " + Utility.getTFColor(player.isOp()));
				Utility.sendMsg(snd, "§f--- §6Access and advancedwhitelist.bypass.<perm> check §f---");
				// star "*", operator, projectteam, staff, tester, alternate, other
				for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
					if (perm.getPermission().equalsIgnoreCase("*")) {
						permsList.set(0, perm.getPermission().equalsIgnoreCase("*"));
					}
					if (perm.getPermission().equalsIgnoreCase(core + "operator")) {
						permsList.set(1, perm.getPermission().equalsIgnoreCase(core + "operator"));
					}
					if (perm.getPermission().equalsIgnoreCase(core + "projectteam")) {
						permsList.set(2, perm.getPermission().equalsIgnoreCase(core + "projectteam"));
					}
					if (perm.getPermission().equalsIgnoreCase(core + "staff")) {
						permsList.set(3, perm.getPermission().equalsIgnoreCase(core + "staff"));
					}
					if (perm.getPermission().equalsIgnoreCase(core + "tester")) {
						permsList.set(4, perm.getPermission().equalsIgnoreCase(core + "tester"));
					}
					if (perm.getPermission().equalsIgnoreCase(core + "alternate")) {
						permsList.set(5, perm.getPermission().equalsIgnoreCase(core + "alternate"));
					}
					if (perm.getPermission().equalsIgnoreCase(core + "other")) {
						permsList.set(6, perm.getPermission().equalsIgnoreCase(core + "other"));
					}
				} // End for loop
				Utility.sendMsg(snd, "§6Has * Perm: " + Utility.getTFColor(permsList.get(0)) + 
						"   §f|   §6Has .operator: " + Utility.getTFColor(permsList.get(1)));
				Utility.sendMsg(snd, "§6ProjectTeam enabled: " + Utility.getTFColor(WLStorage.isProjectTeamAccess()) + 
						"  §f|  §6Has .projectteam: " + Utility.getTFColor(permsList.get(2))); 
				Utility.sendMsg(snd, "§6Staff enabled: " + Utility.getTFColor(WLStorage.isStaffAccess()) + 
						"  §f|  §6Has .staff: " + Utility.getTFColor(permsList.get(3)));
				Utility.sendMsg(snd, "§6Tester enabled: " + Utility.getTFColor(WLStorage.isTesterAccess()) + 
						"  §f|  §6Has .tester: " + Utility.getTFColor(permsList.get(4)));
				Utility.sendMsg(snd, "§6Alternate enabled: " + Utility.getTFColor(WLStorage.isAlternateAccess()) 
				+ "  §f|  §6Has .alternate: " + Utility.getTFColor(permsList.get(5)));
				Utility.sendMsg(snd, "§6Other enabled: " + Utility.getTFColor(WLStorage.isOtherAccess()) + 
						"  §f|  §6Has .other: " + Utility.getTFColor(permsList.get(6)));
			} // End if player online
		}
		// If Offline and has Vault
		if (playerOnline == false) {
			if (AdvancedWhiteList.hasVault()) {
				Utility.sendMsg(snd, "§d" + args[1] + " §6is offline, using Vault to get permissions, which may not show correctly if player has * perm.");
				perms = AdvancedWhiteList.getPermissions();
				OfflinePlayer offlinePlayerUUID = Bukkit.getOfflinePlayer(args[1]); //UUID.fromString(args[1]); //Bukkit.getOfflinePlayer(args[1]).getUniqueId());
				Bukkit.getScheduler().runTaskAsynchronously(m, new Runnable() {
					@Override
					public void run() {
						String world = m.getServer().getWorlds().get(0).getName().toString();
						permsList.set(0,perms.playerHas(world, offlinePlayerUUID, "*"));
						permsList.set(1,perms.playerHas(world, offlinePlayerUUID, core + "operator"));
						permsList.set(2,perms.playerHas(world, offlinePlayerUUID, core + "projectteam"));
						permsList.set(3,perms.playerHas(world, offlinePlayerUUID, core + "staff"));
						permsList.set(4,perms.playerHas(world, offlinePlayerUUID, core + "tester"));
						permsList.set(5,perms.playerHas(world, offlinePlayerUUID, core + "alternate"));
						permsList.set(6,perms.playerHas(world, offlinePlayerUUID, core + "other"));

						Utility.sendMsg(snd, "§6Whitelist enabled: " + Utility.getTFColor(WLStorage.isWhitelisting()));
						Utility.sendMsg(snd, "§6Config access enabled: " + Utility.getTFColor(WLStorage.isConfigAccess()) + 
								"  §f|  §6On Config Whitelist: " + Utility.getTFColor(WLStorage.isWhitelisted(args[1])));
						Utility.sendMsg(snd, "§6OP and * perm bypass enabled: " + Utility.getInvertedTFColor(WLStorage.isOpBypass()) + 
								"  §f|  §6OPed: " + Utility.getTFColor(Bukkit.getOfflinePlayer(offlinePlayerUUID.getUniqueId()).isOp()));
						Utility.sendMsg(snd, "§f--- §6Access and advancedwhitelist.bypass.<perm> check §f---");
						Utility.sendMsg(snd, "§6Has * Perm: " + Utility.getTFColor(permsList.get(0)) + 
								"   §f|   §6Has .operator: " + Utility.getTFColor(permsList.get(1)));
						if (permsList.get(0)) Utility.sendMsg(snd, "§d" + args[1] + " §chas §e* §cperm, so other perms will show true. This does not properly reflect correctly for effective perms.");
						Utility.sendMsg(snd, "§6ProjectTeam enabled: " + Utility.getTFColor(WLStorage.isProjectTeamAccess()) + 
								"  §f|  §6Has .projectteam: " + Utility.getTFColor(permsList.get(2))); 
						Utility.sendMsg(snd, "§6Staff enabled: " + Utility.getTFColor(WLStorage.isStaffAccess()) + 
								"  §f|  §6Has .staff: " + Utility.getTFColor(permsList.get(3)));
						Utility.sendMsg(snd, "§6Tester enabled: " + Utility.getTFColor(WLStorage.isTesterAccess()) + 
								"  §f|  §6Has .tester: " + Utility.getTFColor(permsList.get(4)));
						Utility.sendMsg(snd, "§6Alternate enabled: " + Utility.getTFColor(WLStorage.isAlternateAccess()) 
						+ "  §f|  §6Has .alternate: " + Utility.getTFColor(permsList.get(5)));
						Utility.sendMsg(snd, "§6Other enabled: " + Utility.getTFColor(WLStorage.isOtherAccess()) + 
								"  §f|  §6Has .other: " + Utility.getTFColor(permsList.get(6)));
					}});
			} // End if hasVault
			// If Offline and Doesn't have fault
			if (!AdvancedWhiteList.hasVault()) {
				Utility.sendMsg(snd, "§6Player is offline, and Vault isn't running on the server, "
						+ "unable to get permissions of named player.");
				Utility.sendMsg(snd, "§6Whitelist enabled: " + Utility.getTFColor(WLStorage.isWhitelisting()));
				Utility.sendMsg(snd, "§6Config access enabled: " + Utility.getTFColor(WLStorage.isConfigAccess()) + 
						"  §f|  §6On Config Whitelist: " + Utility.getTFColor(WLStorage.isWhitelisted(args[1])));
				Utility.sendMsg(snd, "§6OP and * perm bypass enabled: " + Utility.getInvertedTFColor(WLStorage.isOpBypass()) + 
						"  §f|  §6OPed: " + Utility.getTFColor(Bukkit.getOfflinePlayer(args[1]).isOp()));
				getStatus(snd);
			} // End if doesn't have vault
		} // End if player offline
	}

	static void restartServer(CommandSender snd) {
		Boolean currentWL = WLStorage.isWhitelisting();
		Boolean currentCA = WLStorage.isConfigAccess();
		Boolean currentPA = WLStorage.isProjectTeamAccess();
		Boolean currentSA = WLStorage.isStaffAccess();
		Boolean currentAA = WLStorage.isAlternateAccess();
		Boolean currentTA = WLStorage.isTesterAccess();
		Boolean currentOA = WLStorage.isOtherAccess();

		WLStorage.setWhitelist(true);
		WLStorage.setProjectTeamAccess(false);
		WLStorage.setStaffAccess(false);
		WLStorage.setTesterAccess(false);
		WLStorage.setAlternateAccess(false);
		WLStorage.setOtherAccess(false);
		WLStorage.setConfigAccess(false);				
		sendPlayers(snd);

		WLStorage.setWhitelist(currentWL);
		WLStorage.setProjectTeamAccess(currentPA);
		WLStorage.setStaffAccess(currentSA);
		WLStorage.setTesterAccess(currentTA);
		WLStorage.setAlternateAccess(currentAA);
		WLStorage.setOtherAccess(currentOA);
		WLStorage.setConfigAccess(currentCA);
		
		Bukkit.shutdown();
		
	}
	
	static void sendPlayers(CommandSender snd) {
		Utility.sendMsg(snd, prefix + "&6Whitelist is  " + WLStorage.isWhitelisting());
			if (WLStorage.isWhitelisting()) {
				Utility.broadcast(WLStorage.getBroadcastMsg());
				Utility.sendMsg(snd, prefix + "&6Sending non-whitelisted players back to Lobby based on the status below!");
				getStatus(snd);
				try {
		            Thread.sleep(WLStorage.getDelayBeforeStartingKicks()*1000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				playerSendKick(snd);
				Utility.sendMsg(snd, prefix + "&aDone sending non-whitelisted players!");
				Utility.sendMsg(snd, prefix + "&cIf players are still connected, check the Whitelist settings.");
			}
	}
	
	static void playerSendKick(CommandSender snd) {
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
			if (WLEvent.permCheck(p)) {
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
					out.writeUTF(WLStorage.getHubServer());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Utility.sendMsg(p, prefix + WLStorage.getSendMsg());
				p.sendPluginMessage(m, "BungeeCord", bout.toByteArray());

				try {
		            Thread.sleep(WLStorage.getKickDelayPerPlayer() * 1000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
				if (!Bukkit.getOnlinePlayers().contains(p)) {
					playersSent++;
				}
				if (Bukkit.getOnlinePlayers().contains(p)) {
					p.kickPlayer(WLStorage.getKickMsg());
					playersKicked++;
				}
			}
			for(@SuppressWarnings("unused") Player player2 : Bukkit.getOnlinePlayers()) {
				playersLeft++;
			}
			sendBungeeMsg(pname);
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
	
	static void sendBungeeMsg(String pname) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bout);
		try {
			out.writeUTF("message");
			out.writeUTF(pname);
			out.writeUTF(WLStorage.getSendMsg());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void removePlayer(CommandSender snd, String[] args) {
		if (args.length < 2) {
			Utility.sendMsg(snd, "&7Please input a name!");
			return;
		}
		String names = "";
		for (int i=1 ; i < args.length ; i++) {
			WLStorage.removeWhitelist(args[i]);
			if (i == 1) names = names + args[1];
			if (i < 1) names = names + ", " + args[i];
		}
		Utility.sendMsg(snd, prefix + "Whitelist removed for &c" + names);
	}
	
	static void addPlayer (CommandSender snd, String[] args) {
		if (args.length < 2) {
			Utility.sendMsg(snd, "&7Please input a name!");
			return;
		}
		String names = "";
		for (int i=1 ; i < args.length ; i++) {
			WLStorage.addWhitelist(args[i]);
			if (i == 1) names = names + args[i];
			else names = names + ", " + args[i];
		}
		Utility.sendMsg(snd, prefix + "Whitelisted &a" + names);
	}
	
	static void listPlayers (CommandSender snd) {
		names = "";
		String str;
		for (Iterator<String> var6 = WLStorage.getWhiteLists().iterator(); var6
				.hasNext(); names = names + str + "&e&l, &7") {
			str = (String) var6.next();
		}
		Utility.sendMsg(snd, "&a&lWhitelisted: &7" + names);
	}

	public static void addAllPlayers() {
		String names = "";
		for (Player player : Bukkit.getOnlinePlayers()) {
			WLStorage.addWhitelist(player.getName());
			names = names + ", " + player.getName();
		}
	}
	
	static void resetList () {
		for (String n : WLStorage.getWhiteLists()) {
			WLStorage.removeWhitelist(n);
		}
	}
	
	static String msgOnly(String[] args) {
		String msg = "";
		for (int i=1; i< args.length;i++) {
			msg = msg+args[i]+" ";
		}
		return msg;
	}
	
	static void getStatusMenu(CommandSender snd) {
		Utility.sendMsg(snd, "&a&lWhitelist Status &7>");
		Utility.sendMsg(snd, "&e> &7Use &a&l/awl help &r&7for commands list.");
		Utility.sendMsg(snd, "&e> &7/awl &bstatus 1 &7- &dAccess Settings");
		Utility.sendMsg(snd, "&e> &7/awl &bstatus 2 &7- &dMessages Settings");
		Utility.sendMsg(snd, "&e> &7/awl &bstatus 3 &7- &dDuration and Misc Settings");
	}
	
	
	static void getStatus(CommandSender snd) {
		getStatusMenu(snd);
		getStatus1(snd);
	}
	
	static void getStatus(CommandSender snd, String arg) {
		getStatusMenu(snd);
		if (arg.equals("1")) getStatus1(snd);
		if (arg.equals("2")) getStatus2(snd);
		if (arg.equals("3")) getStatus3(snd);
	}
	
	static void getStatus1(CommandSender snd) {
		Utility.sendMsg(snd, "&e> &7========== Access Settings ==========");
		Utility.sendMsg(snd, "&e> &7Whitelisting Enabled: &e" + Utility.getTFColor(WLStorage.isWhitelisting()));
		Utility.sendMsg(snd, "&e> &7Config Access Enabled: &e" + Utility.getTFColor(WLStorage.isConfigAccess()));
		Utility.sendMsg(snd, "&e> &7ProjectTeam Access Enabled: &e" + Utility.getTFColor(WLStorage.isProjectTeamAccess()));
		Utility.sendMsg(snd, "&e> &7Staff Access Enabled: &e" + Utility.getTFColor(WLStorage.isStaffAccess()));
		Utility.sendMsg(snd, "&e> &7Tester Access Enabled: &e" + Utility.getTFColor(WLStorage.isTesterAccess()));
		Utility.sendMsg(snd, "&e> &7Alternate Access Enabled: &e" + Utility.getTFColor(WLStorage.isAlternateAccess()));
		Utility.sendMsg(snd, "&e> &7Other Access Enabled: &e" + Utility.getTFColor(WLStorage.isOtherAccess()));
	}
	
	static void getStatus2(CommandSender snd) {
		Utility.sendMsg(snd, "&e> &7========== Message Settings ==========");
		Utility.sendMsg(snd, "&e> &7Not Whitelisted message: &e" + WLStorage.getNotWhitelistMsg());
		Utility.sendMsg(snd, "&e> &7");
		Utility.sendMsg(snd, "&e> &7Send/Kick broadcast message: &e" + WLStorage.getBroadcastMsg());
		Utility.sendMsg(snd, "&e> &7");
		Utility.sendMsg(snd, "&e> &7Message sent to player just before they are sent to Hub Server: &e" + WLStorage.getSendMsg());
		Utility.sendMsg(snd, "&e> &7");
		Utility.sendMsg(snd, "&e> &7Message sent to player when a player is kicked from the network: &e" + WLStorage.getKickMsg());
	}
	
	static void getStatus3(CommandSender snd) {
		Utility.sendMsg(snd, "&e> &7========== Duration Settings ==========");
		Utility.sendMsg(snd, "&e> &7Server Cooldown time: &e" + WLStorage.getServerCooldown() + " Seconds");
		Utility.sendMsg(snd, "&e> &7Delay time in seconds before starting kicks: &e" + WLStorage.getDelayBeforeStartingKicks() + " Seconds");
		Utility.sendMsg(snd, "&e> &7Delay time between player being kicked and checked: &e" + WLStorage.getKickDelayPerPlayer() + " Seconds");
		Utility.sendMsg(snd, "&e> &7========== Misc Settings ==========");
		Utility.sendMsg(snd, "&e> &7Hub Server: &e" + WLStorage.getHubServer());
		Utility.sendMsg(snd, "&e> &7Server Start to Player Login Cooldown Enabled: &e" + WLStorage.isServerCooldown());
		Utility.sendMsg(snd, "&e> &7OP Bypass: &e" + Utility.getInvertedTFColor(WLStorage.isOpBypass()));
	}
	
	static void HelpMenu(CommandSender snd) {
		Utility.sendMsg(snd, "&e> &7========== Help Menu ==========");
		Utility.sendMsg(snd, "&e> &7/awl &bhelp 1 - Base Help commands");
		Utility.sendMsg(snd, "&e> &7/awl &bhelp 2-3 - For more commands");
		Utility.sendMsg(snd, "&e> &7/awl &bhelp 4 - For more specific permissions");
		Utility.sendMsg(snd, "&e> &7/awl &bhelp 5-9 - Command Aliases");
	}
	
	static void getHelp(CommandSender snd) {
		getHelp1(snd);
	}
	
	static void getHelp(CommandSender snd, String arg) {
		HelpMenu(snd);
		if (arg.equals("1")) getHelp1(snd);
		if (arg.equals("2")) getHelp2(snd);
		if (arg.equals("3")) getHelp3(snd);
		if (arg.equals("4")) getHelp4(snd);
		if (arg.equals("5")) getHelp5(snd);
		if (arg.equals("6")) getHelp6(snd);
		if (arg.equals("7")) getHelp7(snd);
		if (arg.equals("8")) getHelp8(snd);
		if (arg.equals("9")) getHelp9(snd);
		else {
			return;
		}
	}

	static void getHelp1(CommandSender snd) {
		Utility.sendMsg(snd, "&e> &7/awl &bstatus/info (1-3)");
		Utility.sendMsg(snd, "&e> &7/awl &bmessages - ");
		Utility.sendMsg(snd, "&e> &7/awl &aadd&7/&cremove &f<name>");
		Utility.sendMsg(snd, "&e> &7/awl &flist");
		Utility.sendMsg(snd, "&e> &7/awl &a(addall)players &e- Add all current players to Config Access list");
		Utility.sendMsg(snd, "&e> &7/awl &cclearlist §6- Remove all players from Config Access list");
		Utility.sendMsg(snd, "&e> &7/awl &a&lon &f/ &coff");
		Utility.sendMsg(snd, "&e> &7/awl &cwlonly/kick/sendall &e- Kicks players that aren't whitelisted.");
		Utility.sendMsg(snd, "&e> &7/awl &6send <player> <Bungeecord Server> &e- Attempts to send the named player to the named Bungeecord server.");
		Utility.sendMsg(snd, "&e> &7/awl &creload");
		Utility.sendMsg(snd, "&e> &7/awl &crestart &e- Kicks everyone except Operators and restarts the server.");
		Utility.sendMsg(snd, "&e> &7/awl uuid (Name/UUID) &e- Get the UUID of any name, or name from any UUID");
		Utility.sendMsg(snd, "&e> &7/awl check (Name/UUID) &e- Check which access a player has for bypasses.");
		return;
	}
	static void getHelp2(CommandSender snd) {
		Utility.sendMsg(snd, "&e> &7/awl convert &e- Convert from Minecraft Whitelist, EasyWhiteList, and/or itself.");
		Utility.sendMsg(snd, "&e> &7/awl scd &bon&7/&8off &e- Server start to player login cooldown");
		Utility.sendMsg(snd, "&e> &7/awl &bscdtime <seconds>");
		Utility.sendMsg(snd, "&e> &7/awl &bnotwlmsg <message> &e- Msg sent to the player if they aren't whitelisted for a server.");
		Utility.sendMsg(snd, "&e> &7/awl &bbcastmsg <message> &e- Msg sent before everyone gets kicked from send/kick command.");
		Utility.sendMsg(snd, "&e> &7/awl &bsendmsg <message> &e- Msg sent to the player just before they're sent back to Hub/Lobby.");
		Utility.sendMsg(snd, "&e> &7/awl &bkickmsg <message> &e- Msg sent to player when they're kicked from the server.");
		Utility.sendMsg(snd, "&e> &7/awl &bhubserver <servername> &e- This is based on what's set in Bungee");
		Utility.sendMsg(snd, "&e> &7/awl &bsenddelay <seconds> &e- Amount of time a message is displayed before players are sent and kicked from the server.");
		Utility.sendMsg(snd, "&e> &7/awl &bkickdelay <seconds> &e- Amount of time between attempting to send a player before checking if kick is needed, also delay per kicked player to not overload bungee server.");
		return;
	}
	static void getHelp3(CommandSender snd) {
		Utility.sendMsg(snd, "&e> &7Access Commands: &aAdvancedWhiteList.Bypass.&b<Role>");
		Utility.sendMsg(snd, "&e> Whitelist: &7/awl <wlon/on or wloff/off> &f- &bOperator");
		Utility.sendMsg(snd, "&e> ProjectTeamAccess: &7/awl pta <on/off> &f- &bProjectTeam");
		Utility.sendMsg(snd, "&e> StaffAccess: &7/awl staff <on/off> &f- &bStaff");
		Utility.sendMsg(snd, "&e> TesterAccess: &7/awl tester <on/off> &f- &bTester");
		Utility.sendMsg(snd, "&e> AlternateAccess: &7/awl alt <on/off> &f- &bAlternateAccess");
		Utility.sendMsg(snd, "&e> OtherAccess: &7/awl other <on/off> &f- &bOther");
		Utility.sendMsg(snd, "&e> ConfigListAccess: &7/awl config <on/off>");
		Utility.sendMsg(snd, "&e> Lockdown: &7/awl <fullblock, block, full, lockdown, alloff>");
		Utility.sendMsg(snd, "&e> Staff Only: &7/awl <halfblock, half, staffonly>");
		return;
	}
	static void getHelp4(CommandSender snd) {
		Utility.sendMsg(snd, "&e> &7Permissions:");
		Utility.sendMsg(snd, "&e> &7Command access: &6AdvancedWhiteList.Admin");
		Utility.sendMsg(snd, "&e> &7Full Whitelist Bypass: &6AdvancedWhiteList.Bypass.&bOperator");
		Utility.sendMsg(snd, "&e> &7ProjectTeam Access Bypass: &6AdvancedWhiteList.Bypass.&bProjectTeam");
		Utility.sendMsg(snd, "&e> &7Staff Access Bypass: &6AdvancedWhiteList.Bypass.&bStaff");
		Utility.sendMsg(snd, "&e> &7Tester Access Bypass: &6AdvancedWhiteList.Bypass.&bTester");
		Utility.sendMsg(snd, "&e> &7Alternate Bypass: &6AdvancedWhiteList.Bypass.&bAlternate");
		Utility.sendMsg(snd, "&e> &7Other Access Bypass: &6AdvancedWhiteList.Bypass.&bOther");
		return;
	}
	static void getHelp5(CommandSender snd) {
		Utility.sendMsg(snd, "&e> advancedwhitelist, advancedwl, awhitelist, advwl, awl, whitelist, wl, wlist");
		Utility.sendMsg(snd, "&e> status, s, info, inf, in, i");
		Utility.sendMsg(snd, "&e> messages, message, msgs, msg");
		Utility.sendMsg(snd, "&e> help, h, ?");
		Utility.sendMsg(snd, "&e> reload, rel, rl");
		Utility.sendMsg(snd, "&e> remove, rem, re, r");
		Utility.sendMsg(snd, "&e> add, ad, a");
		Utility.sendMsg(snd, "&e> list, li, l");
		return;
	}
	static void getHelp6(CommandSender snd) {
		Utility.sendMsg(snd, "&e> addallplayers, addallplayer, allplayers, allplayer, addplayers, addall, aall");
		Utility.sendMsg(snd, "&e> resetlist, reset, listreset, clearlist, listclear, removeall, rall");
		Utility.sendMsg(snd, "&e> whitelist, wl (on/off)");
		Utility.sendMsg(snd, "&e> opbypass, bypass, op (on/off)");
		Utility.sendMsg(snd, "&e> servercooldown, scooldown, cooldown, servercd, scd, cd (on/off)");
		Utility.sendMsg(snd, "&e> servercooldowntime, scooldowntime, cooldowntime, servercdtime, scdtime, cdtime, scdt, cdt");
		return;
	}
	static void getHelp7(CommandSender snd) {
		Utility.sendMsg(snd, "&e> setlobby, sethub, lobby, hub, lobbyserver, hubserver, server");
		Utility.sendMsg(snd, "&e> senddelay, startdelay, sd");
		Utility.sendMsg(snd, "&e> kickdelay, perplayerdelay, ppd, pd, kd");
		Utility.sendMsg(snd, "&e> notwhitelistedmessage, notwhitelistedmsg, notwlmsg, wlmsg, wlm");
		Utility.sendMsg(snd, "&e> broadcastmessage, broadcastmsg, bcastmessage, bcastmsg, bcmsg, bmsg, bm");
		Utility.sendMsg(snd, "&e> sendmessage, sendmsg, smsg, sm");
		Utility.sendMsg(snd, "&e> kickmessage, kickmsg, kmsg, km");
		return;
	}
	static void getHelp8(CommandSender snd) {
		Utility.sendMsg(snd, "&e> projectteamaccess, projectteam, pta (on/off)");
		Utility.sendMsg(snd, "&e> staffaccess, staff, sa (on/off)");
		Utility.sendMsg(snd, "&e> testeraccess, ta (on/off)");
		return;
	}
	static void getHelp9(CommandSender snd) {
		Utility.sendMsg(snd, "&e> alternateaccess, aa (on/off)");
		Utility.sendMsg(snd, "&e> otheraccess, other, oa, oo (on/off)");
		Utility.sendMsg(snd, "&e> configaccess, config, ca (on/off)");
		Utility.sendMsg(snd, "&e> fullblock, block, full, lockdown, alloff");
		Utility.sendMsg(snd, "&e> halfblock, half, staffonly");
		Utility.sendMsg(snd, "&e> kick, whitelistonly, wlonly, only, send, enforce");
		return;
	}
}			