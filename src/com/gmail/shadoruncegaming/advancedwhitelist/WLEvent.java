package com.gmail.shadoruncegaming.advancedwhitelist;

//import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class WLEvent implements Listener {

//	private HashMap<Player, Long> cooldown = new HashMap<Player, Long>();

	@EventHandler
	public void onConnect(PlayerLoginEvent e) {
		Long start = AdvancedWhiteList.start;
		Player p = e.getPlayer();
		if (p != null) {
			if (WLStorage.isServerCooldown() == true) {
				Long differ = System.currentTimeMillis() - start;
				Long cooldown = WLStorage.getServerCooldown();
				Long timeLeft;
				if (differ < ((cooldown/3)*1000) && (p.hasPermission("AdvancedWhiteList.Bypass.Operator") || p.hasPermission("AdvancedWhiteList.Bypass.Operators"))) {
					timeLeft = ((cooldown/3) - TimeUnit.MILLISECONDS.toSeconds(differ));
					e.disallow(Result.KICK_WHITELIST, "Operators have " + timeLeft + " Seconds left until they can join. Please wait at least "+(cooldown/3)+" seconds after the server has started to join.");
					return;
				}
				if (differ < ((cooldown/2)*1000) && (WLStorage.isProjectTeamAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.ProjectTeam")) && (!p.hasPermission("AdvancedWhiteList.Bypass.Operator") || !p.hasPermission("AdvancedWhiteList.Bypass.Operators"))) {
					timeLeft = ((cooldown/2) - TimeUnit.MILLISECONDS.toSeconds(differ));
					e.disallow(Result.KICK_WHITELIST, "ProjectTeam have " + timeLeft + " Seconds left until they can join. Please wait at least "+(cooldown/2)+" seconds after the server has started to join.");
					return;
				}
				if (differ < (cooldown*1000) && !p.hasPermission("AdvancedWhiteList.Bypass.ProjectTeam") && (!p.hasPermission("AdvancedWhiteList.Bypass.Operator") || !p.hasPermission("AdvancedWhiteList.Bypass.Operators"))) {
				timeLeft = (cooldown - TimeUnit.MILLISECONDS.toSeconds(differ));
				e.disallow(Result.KICK_WHITELIST, "Players have " + timeLeft + " Seconds left until you can join. Please wait at least "+cooldown+" seconds after the server has started to join.");
				}
			}
			if (WLStorage.isWhitelisting()) {
				if (WLEvent.permCheck(p)) return;
				else {
					e.disallow(Result.KICK_WHITELIST, WLStorage.getNotWhitelistMsg());
				}
			}
		}
	}
	
	static boolean permCheck(Player p) {
		if (WLStorage.isConfigAccess() == true && WLStorage.isWhitelisted(p.getName()))  return true;
		boolean hasAwlPerm = false;

		if (!WLStorage.isOpBypass()) {
			if (p.isOp() || p.hasPermission("*")) {
				for (PermissionAttachmentInfo perm : p.getEffectivePermissions()) {
					if (perm.getPermission().equalsIgnoreCase("AdvancedWhiteList.Bypass.Operator")) {hasAwlPerm = true;}
					if (WLStorage.isProjectTeamAccess() == true && perm.getPermission().equalsIgnoreCase("AdvancedWhiteList.Bypass.ProjectTeam")) {hasAwlPerm = true;}
					if (WLStorage.isStaffAccess() == true && perm.getPermission().equalsIgnoreCase("AdvancedWhiteList.Bypass.Staff")) {hasAwlPerm = true;}
					if (WLStorage.isTesterAccess() == true && perm.getPermission().equalsIgnoreCase("AdvancedWhiteList.Bypass.Tester")) {hasAwlPerm = true;}
					if (WLStorage.isAlternateAccess() == true && perm.getPermission().equalsIgnoreCase("AdvancedWhiteList.Bypass.Alternate")) {hasAwlPerm = true;}
					if (WLStorage.isOtherAccess() == true && perm.getPermission().equalsIgnoreCase("AdvancedWhiteList.Bypass.Other")) {hasAwlPerm = true;}
				}
				if (hasAwlPerm == false) {
					Utility.sendConsole(p.getName() + " has OP or * Perm but doesn't have permission to connect.");
				}
			}
		}
		else {
			if (p.hasPermission("AdvancedWhiteList.Bypass.Operator")) {hasAwlPerm = true;}
			if (WLStorage.isProjectTeamAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.ProjectTeam")) {hasAwlPerm = true;}
			if (WLStorage.isStaffAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Staff")) {hasAwlPerm = true;}
			if (WLStorage.isTesterAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Tester")) {hasAwlPerm = true;}
			if (WLStorage.isAlternateAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Alternate")) {hasAwlPerm = true;}
			if (WLStorage.isOtherAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Other")) {hasAwlPerm = true;}
		}
		return hasAwlPerm;
	}
	
	@SuppressWarnings("deprecation")
	static void checkPlayer(CommandSender snd, String name) {
		OfflinePlayer p = Bukkit.getOfflinePlayer(name); // TODO try to get uuid then put into get offline player to avoid deprection
		checkPlayer(snd,p);
	}
	
	static void checkPlayer(CommandSender snd, OfflinePlayer p) {
		Utility.sendMsg(snd,WLCmd.prefix);

		if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			// TODO
		}
	}
}