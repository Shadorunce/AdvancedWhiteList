package com.gmail.shadoruncegaming.advancedwhitelist;

//import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
				if (differ < ((cooldown/3)*1000) && (p.hasPermission("advancedwhitelist.bypass.operator"))) {
					timeLeft = ((cooldown/3) - TimeUnit.MILLISECONDS.toSeconds(differ));
					e.disallow(Result.KICK_WHITELIST, "Operators have " + timeLeft + 
							" Seconds left until they can join. Please wait at least " + (cooldown/3) + 
							" seconds after the server has started to join.");
					return;
				}
				if (differ < ((cooldown/2)*1000) && (WLStorage.isProjectTeamAccess() == true && 
						p.hasPermission("advancedwhitelist.bypass.projectteam")) && 
						(!p.hasPermission("advancedwhitelist.bypass.operator"))) {
					timeLeft = ((cooldown/2) - TimeUnit.MILLISECONDS.toSeconds(differ));
					e.disallow(Result.KICK_WHITELIST, "ProjectTeam have " + timeLeft + 
							" Seconds left until they can join. Please wait at least " + (cooldown/2) + 
							" seconds after the server has started to join.");
					return;
				}
				if (differ < (cooldown*1000) && !p.hasPermission("advancedwhitelist.bypass.projectteam") && 
						(!p.hasPermission("advancedwhitelist.bypass.operator"))) {
				timeLeft = (cooldown - TimeUnit.MILLISECONDS.toSeconds(differ));
				e.disallow(Result.KICK_WHITELIST, "Players have " + timeLeft + 
						" Seconds left until you can join. Please wait at least " + cooldown + 
						" seconds after the server has started to join.");
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
		if (WLStorage.isConfigAccess() == true && WLStorage.isWhitelisted(p.getName())) return true;
		boolean hasAwlPerm = false;
		if (!WLStorage.isOpBypass()) {
			for (PermissionAttachmentInfo perm : p.getEffectivePermissions()) {
				if (perm.getPermission().equalsIgnoreCase("advancedwhitelist.bypass.operator")) {hasAwlPerm = true;}
				if (WLStorage.isProjectTeamAccess() == true && perm.getPermission().equalsIgnoreCase("advancedwhitelist.bypass.projectteam")) {hasAwlPerm = true;}
				if (WLStorage.isStaffAccess() == true && perm.getPermission().equalsIgnoreCase("advancedwhitelist.bypass.staff")) {hasAwlPerm = true;}
				if (WLStorage.isTesterAccess() == true && perm.getPermission().equalsIgnoreCase("advancedwhitelist.bypass.tester")) {hasAwlPerm = true;}
				if (WLStorage.isAlternateAccess() == true && perm.getPermission().equalsIgnoreCase("advancedwhitelist.bypass.alternate")) {hasAwlPerm = true;}
				if (WLStorage.isOtherAccess() == true && perm.getPermission().equalsIgnoreCase("advancedwhitelist.bypass.other")) {hasAwlPerm = true;}
			}
			if (hasAwlPerm == false) {
				if (p.isOp() || p.hasPermission("*")) {
					Utility.sendConsole(p.getName() + " has OP or * Perm but doesn't have permission to connect.");
				}
				else {Utility.sendConsole("§d" + p.getName() + " was not able to connect");}
			}
		}
		else { // If isOpBypass == true
			if (p.hasPermission("advancedwhitelist.bypass.operator")) {hasAwlPerm = true;}
			if (WLStorage.isProjectTeamAccess() == true && p.hasPermission("advancedwhitelist.bypass.projectteam")) {hasAwlPerm = true;}
			if (WLStorage.isStaffAccess() == true && p.hasPermission("advancedwhitelist.bypass.staff")) {hasAwlPerm = true;}
			if (WLStorage.isTesterAccess() == true && p.hasPermission("advancedwhitelist.bypass.tester")) {hasAwlPerm = true;}
			if (WLStorage.isAlternateAccess() == true && p.hasPermission("advancedwhitelist.bypass.alternate")) {hasAwlPerm = true;}
			if (WLStorage.isOtherAccess() == true && p.hasPermission("advancedwhitelist.bypass.other")) {hasAwlPerm = true;}
		}
		return hasAwlPerm;
	}
}