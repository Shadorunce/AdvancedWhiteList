package com.gmail.shadoruncegaming.advancedwhitelist;

//import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class WLEvent implements Listener {
	private AdvancedWhiteList m;

    public WLEvent(AdvancedWhiteList m) {
        this.m = m;
    }
//	private HashMap<Player, Long> cooldown = new HashMap<Player, Long>();

	@EventHandler
	public void onConnect(PlayerLoginEvent e) {
		Long start = m.start;
		Player p = e.getPlayer();
		if (p != null) {
			if (this.m.getStorage().isServerCooldown() == true) {
				Long differ = System.currentTimeMillis() - start;
				Long cooldown = this.m.getStorage().getServerCooldown();
				Long timeLeft;
				if (differ < ((cooldown/3)*1000) && (p.hasPermission("AdvancedWhiteList.Bypass.Operator") || p.hasPermission("AdvancedWhiteList.Bypass.Operators"))) {
					timeLeft = ((cooldown/3) - TimeUnit.MILLISECONDS.toSeconds(differ));
					e.disallow(Result.KICK_WHITELIST, "Operators have " + timeLeft + " Seconds left until they can join. Please wait at least "+(cooldown/3)+" seconds after the server has started to join.");
					return;
				}
				if (differ < ((cooldown/2)*1000) && (m.getStorage().isProjectTeamAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.ProjectTeam")) && (!p.hasPermission("AdvancedWhiteList.Bypass.Operator") || !p.hasPermission("AdvancedWhiteList.Bypass.Operators"))) {
					timeLeft = ((cooldown/2) - TimeUnit.MILLISECONDS.toSeconds(differ));
					e.disallow(Result.KICK_WHITELIST, "ProjectTeam have " + timeLeft + " Seconds left until they can join. Please wait at least "+(cooldown/2)+" seconds after the server has started to join.");
					return;
				}
				if (differ < (cooldown*1000) && !p.hasPermission("AdvancedWhiteList.Bypass.ProjectTeam") && (!p.hasPermission("AdvancedWhiteList.Bypass.Operator") || !p.hasPermission("AdvancedWhiteList.Bypass.Operators"))) {
				timeLeft = (cooldown - TimeUnit.MILLISECONDS.toSeconds(differ));
				e.disallow(Result.KICK_WHITELIST, "Players have " + timeLeft + " Seconds left until you can join. Please wait at least "+cooldown+" seconds after the server has started to join.");
				}
			}
			if (m.getStorage().isWhitelisting()) {
				if (this.permCheck(p)) return;
/*				if (p.hasPermission("AdvancedWhiteList.Bypass.Operator") || p.hasPermission("AdvancedWhiteList.Bypass.Operators")) {return;}
				if (this.m.getStorage().isConfigAccess() == true && m.getStorage().isWhitelisted(p.getName()))  {return;}
				if (this.m.getStorage().isProjectTeamAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.ProjectTeam")) {return;}
				if (this.m.getStorage().isStaffAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Staff")) {return;}
				if (this.m.getStorage().isTesterAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Tester")) {return;}
				if (this.m.getStorage().isAlternateAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Alternate")) {return;}
				if (this.m.getStorage().isOtherAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Other")) {return;}
*/				else {
					e.disallow(Result.KICK_WHITELIST, m.getStorage().getNotWhitelistMsg());
				}
			}
		}
	}
		boolean permCheck(Player p) {
			if (p.hasPermission("AdvancedWhiteList.Bypass.Operator") || p.hasPermission("AdvancedWhiteList.Bypass.Operators")) {return true;}
			if (this.m.getStorage().isConfigAccess() == true && m.getStorage().isWhitelisted(p.getName()))  {return true;}
			if (this.m.getStorage().isProjectTeamAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.ProjectTeam")) {return true;}
			if (this.m.getStorage().isStaffAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Staff")) {return true;}
			if (this.m.getStorage().isTesterAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Tester")) {return true;}
			if (this.m.getStorage().isAlternateAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Alternate")) {return true;}
			if (this.m.getStorage().isOtherAccess() == true && p.hasPermission("AdvancedWhiteList.Bypass.Other")) {return true;}
			return false;
		}
}