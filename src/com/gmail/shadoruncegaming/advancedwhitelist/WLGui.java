package com.gmail.shadoruncegaming.advancedwhitelist;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WLGui implements Listener {
	private static Inventory inv;
	static String tfClick = "§6Click material to toggle §aTrue§6/§cFalse.";
	static String intClick1 = "§6Left click material to §aIncrease.";
	static String intClick2 = "§6Right click material to §bDecrease.";
	static String intClick3 = "§6Middle click material to type amount.";
	static String msgClick = "§6Left Click material to type a new text.";
	static String msgClick2 = "§6Right Click material to view the full message in chat.";
	
    // You can call this whenever you want to put the items in
    public static void initializeItems()
    {
		inv = Bukkit.createInventory(null, 27, ChatColor.GOLD + "AdvancedWhiteList");
    	
    	WLStorage.reload();
        
    	// Filler
    	for (Integer i = 0 ; i < inv.getSize() ; i++) {
        	inv.setItem(i,createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GOLD + "AWL on: " + getTFColor(WLStorage.isWhitelisting())));
        }
    	// setItem(slot, material)
    	// CreateGuiItem(Material, title, lore....)
    	// getGuiMat(determine material based on other variable)
    	// Enabled?
        inv.setItem(0,createGuiItem(getGUIMat(WLStorage.isWhitelisting()), "§6Whitelist Enabled", "§eEnabled: " + getTFColor(WLStorage.isWhitelisting()), tfClick));
        inv.setItem(1,createGuiItem(getGUIMat(WLStorage.isConfigAccess()), "§eConfig Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isConfigAccess()), tfClick));
        inv.setItem(2,createGuiItem(getGUIMat(WLStorage.isProjectTeamAccess()), "§eProjectTeam Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isProjectTeamAccess()), tfClick));
        inv.setItem(3,createGuiItem(getGUIMat(WLStorage.isStaffAccess()), "§eStaff Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isStaffAccess()), tfClick));
        inv.setItem(4,createGuiItem(getGUIMat(WLStorage.isTesterAccess()), "§eTester Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isTesterAccess()), tfClick));
        inv.setItem(5,createGuiItem(getGUIMat(WLStorage.isAlternateAccess()), "§eAlternate Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isAlternateAccess()), tfClick));
        inv.setItem(6,createGuiItem(getGUIMat(WLStorage.isOtherAccess()), "§eOther Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isOtherAccess()), tfClick));
        inv.setItem(7,createGuiItem(getGUIMat(WLStorage.isServerCooldown()), "§eServer Cooldown Enabled", "§eEnabled: " + getTFColor(WLStorage.isServerCooldown()), tfClick));
        // Messages
        inv.setItem(9,createGuiItem(Material.MAGENTA_STAINED_GLASS_PANE, "§eNot Whitelisted Message", "§eMessage: " + WLStorage.getNotWhitelistMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
        inv.setItem(10,createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "§eBroadcast Message before sending players", "§eMessage: " + WLStorage.getBroadcastMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
        inv.setItem(11,createGuiItem(Material.WHITE_STAINED_GLASS_PANE, "§eMessage sent before sending player", "§eMessage: " + WLStorage.getSendMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
        inv.setItem(12,createGuiItem(Material.PINK_STAINED_GLASS_PANE, "§eMessage sent if player gets kicked", "§eMessage: " + WLStorage.getKickMsg().substring(0, 50) + "[...]", msgClick, msgClick2));
        inv.setItem(13,createGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "§eMessage sent if player gets kicked", "§eServer: §6" + WLStorage.getHubServer(), msgClick, "Refer to Bungee server settings to find Hub/Lobby name."));
        // Durations
        inv.setItem(18,createGuiItem(Material.BLUE_STAINED_GLASS_PANE, "§eServer Cooldown Duration", "§eDuration: §b" + WLStorage.getServerCooldown(), intClick1, intClick2, intClick3));
        inv.setItem(19,createGuiItem(Material.CYAN_STAINED_GLASS_PANE, "§eDelay Before Starting Kicks", "§eDuration: §b" + WLStorage.getDelayBeforeStartingKicks(), intClick1, intClick2, intClick3));
        inv.setItem(20,createGuiItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§eKick Delay Per Player", "§eDuration: §b" + WLStorage.getKickDelayPerPlayer(), intClick1, intClick2, intClick3));
        
    }

	public static Material getGUIMat(Boolean trueFalse) {
		if (trueFalse) return Material.GREEN_STAINED_GLASS_PANE;
		else return Material.RED_STAINED_GLASS_PANE;
	}
	
	public static String getTFColor(Boolean trueFalse) {
		String TF = "error";
		if (trueFalse) TF = "§aTrue";
		if (!trueFalse) TF = "§cFalse";
		return TF;
	}

    // Nice little method to create a gui item with a custom name, and description
    protected static ItemStack createGuiItem(final Material material, final String name, final String... lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        
        // Set the name of the item
        meta.setDisplayName(name);
        
        // Can undo the comment below to see the lores that are processed.
        //Utility.broadcast(Arrays.asList(lore).toString());
        
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
    			if (name.contains("Whitelist Enabled")) {
    				WLStorage.setWhitelist(getTF(WLStorage.isWhitelisting()));
    				inv.setItem(0,createGuiItem(getGUIMat(WLStorage.isWhitelisting()), "§6Whitelist Enabled", "§eEnabled: " + getTFColor(WLStorage.isWhitelisting()), WLGui.tfClick));
    			}
    			if (name.contains("Config Access Enabled")) {
    				WLStorage.setWhitelist(true);
    				WLStorage.setWhitelist(getTF(WLStorage.isWhitelisting()));
    			}
    			if (name.contains("ProjectTeam Access Enabled")) {
    				WLStorage.setWhitelist(false);
    				WLStorage.setWhitelist(getTF(WLStorage.isWhitelisting()));
    			}
    			if (name.contains("Staff Access Enabled")) {
    				WLStorage.setWhitelist(getTF(WLStorage.isWhitelisting()));
    			}
    			if (name.contains("Tester Access Enabled")) {
    				WLStorage.setWhitelist(getTF(WLStorage.isWhitelisting()));
    			}
    			if (name.contains("Alternate Access Enabled")) {
    				WLStorage.setWhitelist(getTF(WLStorage.isWhitelisting()));
    			}
    			if (name.contains("Other Access Enabled")) {
    				WLStorage.setWhitelist(getTF(WLStorage.isWhitelisting()));
    			}
    			if (name.contains("Server Cooldown Enabled")) {
    				WLStorage.setWhitelist(getTF(WLStorage.isWhitelisting()));
    			}
    			if (name.contains("Not Whitelisted Message")) {
    				
    			}
    			if (name.contains("Broadcast Message before sending players")) {
    				
    			}
    			if (name.contains("Message sent before sending player")) {
    				
    			}
    			if (name.contains("Message sent if player gets kicked")) {
    				
    			}
    			if (name.contains("Message sent if player gets kicked")) {
    				
    			}
    			if (name.contains("Server Cooldown Duration")) {
    				
    			}
    			if (name.contains("Delay Before Starting Kicks")) {
    				
    			}
    			if (name.contains("Kick Delay Per Player")) {
    				
    			}
    			//openInventory(p);
    			
    			
    		}
    	}
        
        
        
        if (e.getInventory().getHolder() != this) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;


        if (clickedItem.getItemMeta().getDisplayName() == "") {
        	if (e.isShiftClick()) {
        		return;
        	}
        }
        
        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
        return;
    }
    
 // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
          e.setCancelled(true);
        }
    }
}
