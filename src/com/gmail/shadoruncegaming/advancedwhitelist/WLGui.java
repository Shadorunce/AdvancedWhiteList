package com.gmail.shadoruncegaming.advancedwhitelist;

import java.util.Arrays;

import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class WLGui implements Listener {
	private static Inventory inv;
	static String tfClick = "�6Click material to toggle �aTrue�6/�cFalse.";
	static String intClick1 = "�6Left click material to �aIncrease. Shift for +5";
	static String intClick2 = "�6Right click material to �bDecrease. Shift for -5";
	static String intClick3 = "�6Middle click material to type amount.";
	static String msgClick = "�6Left Click material to type a new text.";
	static String msgClick2 = "�6Right Click material to view the full message in chat.";
	
    // You can call this whenever you want to put the items in
    public static void initializeItems()
    {
		inv = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Advanced" + ChatColor.GREEN + "WhiteList");
    	
    	WLStorage.reload();
        
    	// Filler
    	for (Integer i = 0 ; i < inv.getSize() ; i++) {
        	inv.setItem(i,createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GOLD + "AWL on: " + getTFColor(WLStorage.isWhitelisting())));
        }
    	// setItem(slot, material)
    	// CreateGuiItem(Material, title, lore....)
    	// getGuiMat(determine material based on other variable)
    	// Enabled?
        inv.setItem(0,createGuiItem(getGUIMat(WLStorage.isWhitelisting()), "�6Whitelist Enabled", "�eEnabled: " + getTFColor(WLStorage.isWhitelisting()), tfClick));
        inv.setItem(1,createGuiItem(getGUIMat(WLStorage.isConfigAccess()), "�eConfig Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isConfigAccess()), tfClick));
        inv.setItem(2,createGuiItem(getGUIMat(WLStorage.isProjectTeamAccess()), "�eProjectTeam Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isProjectTeamAccess()), tfClick));
        inv.setItem(3,createGuiItem(getGUIMat(WLStorage.isStaffAccess()), "�eStaff Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isStaffAccess()), tfClick));
        inv.setItem(4,createGuiItem(getGUIMat(WLStorage.isTesterAccess()), "�eTester Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isTesterAccess()), tfClick));
        inv.setItem(5,createGuiItem(getGUIMat(WLStorage.isAlternateAccess()), "�eAlternate Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isAlternateAccess()), tfClick));
        inv.setItem(6,createGuiItem(getGUIMat(WLStorage.isOtherAccess()), "�eOther Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isOtherAccess()), tfClick));
        inv.setItem(7,createGuiItem(getGUIMat(WLStorage.isServerCooldown()), "�eServer Cooldown Enabled", "�eEnabled: " + getTFColor(WLStorage.isServerCooldown()), tfClick));
        // Messages
        inv.setItem(9,createGuiItem(Material.MAGENTA_STAINED_GLASS_PANE, "�eNot Whitelisted Message", "�eMessage: " + WLStorage.getNotWhitelistMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
        inv.setItem(10,createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "�eBroadcast Message before sending players", "�eMessage: " + WLStorage.getBroadcastMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
        inv.setItem(11,createGuiItem(Material.WHITE_STAINED_GLASS_PANE, "�eMessage sent before sending player", "�eMessage: " + WLStorage.getSendMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
        inv.setItem(12,createGuiItem(Material.PINK_STAINED_GLASS_PANE, "�eMessage sent if player gets kicked", "�eMessage: " + WLStorage.getKickMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
        inv.setItem(13,createGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "�eMessage sent if player gets kicked", "�eServer: �6" + WLStorage.getHubServer(), msgClick, "Refer to Bungee server settings to find Hub/Lobby name."));
        // Durations
        inv.setItem(18,createGuiItem(Material.BLUE_STAINED_GLASS_PANE, "�eServer Cooldown Duration", "�eDuration: �b" + WLStorage.getServerCooldown(), intClick1, intClick2, intClick3));
        inv.setItem(19,createGuiItem(Material.CYAN_STAINED_GLASS_PANE, "�eDelay Before Starting Kicks", "�eDuration: �b" + WLStorage.getDelayBeforeStartingKicks(), intClick1, intClick2, intClick3));
        inv.setItem(20,createGuiItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "�eKick Delay Per Player", "�eDuration: �b" + WLStorage.getKickDelayPerPlayer(), intClick1, intClick2, intClick3));
        
    }

	public static Material getGUIMat(Boolean trueFalse) {
		if (trueFalse) return Material.GREEN_STAINED_GLASS_PANE;
		else return Material.RED_STAINED_GLASS_PANE;
	}
	
	public static String getTFColor(Boolean trueFalse) {
		String TF = "error";
		if (trueFalse) TF = "�aTrue";
		if (!trueFalse) TF = "�cFalse";
		return TF;
	}

    // Nice little method to create a gui item with a custom name, and description
    protected static ItemStack createGuiItem(final Material material, final String name, final String... lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        
        // Set the name of the item
        meta.setDisplayName(name);
        
        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this in command
    public static void openInventory(final HumanEntity ent)
    {
        initializeItems();
        ent.openInventory(inv);
    }

    
    Boolean getTF(Boolean b) {
    	if (b == true) return false;
    	else return true;
    }
    
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e)
    {
        Player p = (Player) e.getWhoClicked();
		e.setCancelled(true);
    	if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.GOLD + "AdvancedWhiteList")) {
    		if (e.getCurrentItem() != null) {
    			String name = e.getCurrentItem().getItemMeta().getDisplayName();
    			// Boolean
    			if (name.contains("Whitelist Enabled")) {
    				WLStorage.setWhitelist(getTF(WLStorage.isWhitelisting()));
    				inv.setItem(0,createGuiItem(getGUIMat(WLStorage.isWhitelisting()), "�6Whitelist Enabled", "�eEnabled: " + getTFColor(WLStorage.isWhitelisting()), WLGui.tfClick));
    			}
    			if (name.contains("Config Access Enabled")) {
    				WLStorage.setConfigAccess(getTF(WLStorage.isConfigAccess()));
    		        inv.setItem(1,createGuiItem(getGUIMat(WLStorage.isConfigAccess()), "�eConfig Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isConfigAccess()), tfClick));
    			}
    			if (name.contains("ProjectTeam Access Enabled")) {
    				WLStorage.setProjectTeamAccess(getTF(WLStorage.isProjectTeamAccess()));
    		        inv.setItem(2,createGuiItem(getGUIMat(WLStorage.isProjectTeamAccess()), "�eProjectTeam Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isProjectTeamAccess()), tfClick));
    			}
    			if (name.contains("Staff Access Enabled")) {
    				WLStorage.setStaffAccess(getTF(WLStorage.isStaffAccess()));
    		        inv.setItem(3,createGuiItem(getGUIMat(WLStorage.isStaffAccess()), "�eStaff Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isStaffAccess()), tfClick));
    			}
    			if (name.contains("Tester Access Enabled")) {
    				WLStorage.setTesterAccess(getTF(WLStorage.isTesterAccess()));
    		        inv.setItem(4,createGuiItem(getGUIMat(WLStorage.isTesterAccess()), "�eTester Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isTesterAccess()), tfClick));
    			}
    			if (name.contains("Alternate Access Enabled")) {
    				WLStorage.setAlternateAccess(getTF(WLStorage.isAlternateAccess()));
    		        inv.setItem(5,createGuiItem(getGUIMat(WLStorage.isAlternateAccess()), "�eAlternate Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isAlternateAccess()), tfClick));
    			}
    			if (name.contains("Other Access Enabled")) {
    				WLStorage.setOtherAccess(getTF(WLStorage.isOtherAccess()));
    		        inv.setItem(6,createGuiItem(getGUIMat(WLStorage.isOtherAccess()), "�eOther Access Enabled", "�eEnabled: " + getTFColor(WLStorage.isOtherAccess()), tfClick));
    			}
    			if (name.contains("Server Cooldown Enabled")) {
    				WLStorage.setServerCooldown(getTF(WLStorage.isServerCooldown()));
    		        inv.setItem(7,createGuiItem(getGUIMat(WLStorage.isServerCooldown()), "�eServer Cooldown Enabled", "�eEnabled: " + getTFColor(WLStorage.isServerCooldown()), tfClick));
    			}
    			
    			
    			// Messages
    			TextComponent msg = new TextComponent("Click me to enter a number or string as required. Hover to see current.");
    			msg.setColor(ChatColor.GOLD);
    			
    			if (name.contains("Not Whitelisted Message")) {
    				if (e.isRightClick()) Utility.sendMsg(p, "�7Not Whitelisted Message: " + WLStorage.getNotWhitelistMsg());
    				if (e.isLeftClick()) {
    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getNotWhitelistMsg()).create()));
    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl notwlmsg <msg>"));
    	    			p.spigot().sendMessage(msg);
    				}
    		        inv.setItem(9,createGuiItem(Material.MAGENTA_STAINED_GLASS_PANE, "�eNot Whitelisted Message", "�eMessage: " + WLStorage.getNotWhitelistMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
    			}
    			if (name.contains("Broadcast Message before sending players")) {
    				if (e.isRightClick()) Utility.sendMsg(p, "�7Broadcast Message before sending players: " + WLStorage.getBroadcastMsg());
    				if (e.isLeftClick()) {
    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getBroadcastMsg()).create()));
    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl bcastmsg <message>"));
    	    			p.spigot().sendMessage(msg);
    				}
    		        inv.setItem(10,createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "�eBroadcast Message before sending players", "�eMessage: " + WLStorage.getBroadcastMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
    			}
    			if (name.contains("Message sent before sending player")) {
    				if (e.isRightClick()) Utility.sendMsg(p, "�7Message sent before sending player: " + WLStorage.getSendMsg());
    				if (e.isLeftClick()) {
    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getSendMsg()).create()));
    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl sendmsg <message>"));
    	    			p.spigot().sendMessage(msg);
    				}
    		        inv.setItem(11,createGuiItem(Material.WHITE_STAINED_GLASS_PANE, "�eMessage sent before sending player", "�eMessage: " + WLStorage.getSendMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
    			}
    			if (name.contains("Message sent if player gets kicked")) {
    				if (e.isRightClick()) Utility.sendMsg(p, "�7Message sent if player gets kicked: " + WLStorage.getKickMsg());
    				if (e.isLeftClick()) {
    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getKickMsg()).create()));
    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl kickmsg <message>"));
    	    			p.spigot().sendMessage(msg);
    				}
    		        inv.setItem(12,createGuiItem(Material.PINK_STAINED_GLASS_PANE, "�eMessage sent if player gets kicked", "�eMessage: " + WLStorage.getKickMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
    			}
    			if (name.contains("Hub Server")) {
    				if (e.isRightClick()) Utility.sendMsg(p, "�7Hub Server: " + WLStorage.getHubServer());
    				if (e.isLeftClick()) {
    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getHubServer()).create()));
    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl hubserver <servername>"));
    	    			p.spigot().sendMessage(msg);
    				}
    		        inv.setItem(13,createGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "�eLobby/Hub Server", "�eServer: �6" + WLStorage.getHubServer(), msgClick, "Refer to Bungee server settings to find Hub/Lobby name."));
    			}
    			
    			
    			// Durations
    			if (name.contains("Server Cooldown Duration")) {
    				if (e.isLeftClick()) if (e.isShiftClick()) WLStorage.setServerCooldownTime(WLStorage.getServerCooldown() + 4);
    				if (e.isLeftClick()) WLStorage.setServerCooldownTime(WLStorage.getServerCooldown() + 1);
    				if (WLStorage.getServerCooldown() > 0) if (e.isRightClick()) WLStorage.setServerCooldownTime(WLStorage.getServerCooldown() - 1);
    				if (WLStorage.getServerCooldown() > 3) if (e.isRightClick()) if (e.isShiftClick()) WLStorage.setServerCooldownTime(WLStorage.getServerCooldown() - 4);
					if (e.getClick() == ClickType.MIDDLE) {
    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(numToString(WLStorage.getServerCooldown())).create()));
    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl scdtime <seconds>"));
    	    			p.spigot().sendMessage(msg);
					};
    		        inv.setItem(18,createGuiItem(Material.BLUE_STAINED_GLASS_PANE, "�eServer Cooldown Duration", "�eDuration: �b" + WLStorage.getServerCooldown(), intClick1, intClick2, intClick3));
    			}
    			if (name.contains("Delay Before Starting Kicks")) {
    				if (e.isLeftClick()) if (e.isShiftClick()) WLStorage.setDelayBeforeStartingKicks(WLStorage.getDelayBeforeStartingKicks() + 4);
    				if (e.isLeftClick()) WLStorage.setDelayBeforeStartingKicks(WLStorage.getDelayBeforeStartingKicks() + 1);
    				if (WLStorage.getDelayBeforeStartingKicks() > 0) if (e.isRightClick()) WLStorage.setDelayBeforeStartingKicks(WLStorage.getDelayBeforeStartingKicks() - 1);
    				if (WLStorage.getDelayBeforeStartingKicks() > 3) if (e.isRightClick()) if (e.isShiftClick()) WLStorage.setDelayBeforeStartingKicks(WLStorage.getDelayBeforeStartingKicks() - 4);
					if (e.getClick() == ClickType.MIDDLE) {
    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(numToString(WLStorage.getDelayBeforeStartingKicks())).create()));
    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl senddelay <seconds>"));
    	    			p.spigot().sendMessage(msg);
					};
    		        inv.setItem(19,createGuiItem(Material.CYAN_STAINED_GLASS_PANE, "�eDelay Before Starting Kicks", "�eDuration: �b" + WLStorage.getDelayBeforeStartingKicks(), intClick1, intClick2, intClick3));
    			}
    			if (name.contains("Kick Delay Per Player")) {
    				if (e.isLeftClick()) if (e.isShiftClick()) WLStorage.setKickDelayPerPlayer(WLStorage.getKickDelayPerPlayer() + 4);
    				if (e.isLeftClick()) WLStorage.setKickDelayPerPlayer(WLStorage.getKickDelayPerPlayer() + 1);
    				if (WLStorage.getKickDelayPerPlayer() > 0) if (e.isRightClick()) WLStorage.setKickDelayPerPlayer(WLStorage.getKickDelayPerPlayer() - 1);
    				if (WLStorage.getKickDelayPerPlayer() > 3) if (e.isRightClick()) if (e.isShiftClick()) WLStorage.setKickDelayPerPlayer(WLStorage.getKickDelayPerPlayer() - 4);
					if (e.getClick() == ClickType.MIDDLE) {
    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(numToString(WLStorage.getKickDelayPerPlayer())).create()));
    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl kickdelay <seconds>"));
    	    			p.spigot().sendMessage(msg);
					};
    		        inv.setItem(20,createGuiItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "�eKick Delay Per Player", "�eDuration: �b" + WLStorage.getKickDelayPerPlayer(), intClick1, intClick2, intClick3));
    			}
    		}
    	}
        return;
    }
    
    public String numToString(Long in) {
    	return in.toString();
    }
    
 // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
          e.setCancelled(true);
        }
    }
}
