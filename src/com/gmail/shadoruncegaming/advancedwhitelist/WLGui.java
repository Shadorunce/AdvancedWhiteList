package com.gmail.shadoruncegaming.advancedwhitelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private static AdvancedWhiteList m;
	private static Inventory inv = Bukkit.createInventory(null, 27, "AdvancedWhiteList");
	
	
    public WLGui(AdvancedWhiteList m)
    {
		WLGui.m = m;
		// Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
    	//this.inv = Bukkit.createInventory(null, 27, "AdvancedWhiteList");
        // Put the items into the inventory
    }



    // You can call this whenever you want to put the items in
    public static void initializeItems()
    {
		inv = Bukkit.createInventory(null, 27, "AdvancedWhiteList");
    	
    	WLStorage Storage = new WLStorage(m);
    	Storage.reload();
    	String tfClick = "§6Click material to toggle §aTrue§6/§cFalse.";
    	String intClick1 = "§6Left click material to §aIncrease.";
    	String intClick2 = "§6Right click material to §bDecrease.";
    	String intClick3 = "§6Middle click material to type amount.";
    	String msgClick = "§6Click material to type a new text.";
        
    	// Filler
    	for (Integer i = 0 ; i < inv.getSize() ; i++) {
        	inv.setItem(i,createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GOLD + "AWL on: " + getTFColor(Storage.isWhitelisting())));
        }
    	// setItem(slot, material)
    	// CreateGuiItem(Material, title, lore....)
    	// getGuiMat(determine material based on other variable)
    	// Enabled?
        inv.setItem(0,createGuiItem(getGUIMat(Storage.isWhitelisting()), "§6Whitelist Enabled", "§eEnabled: " + getTFColor(Storage.isWhitelisting()), tfClick));
        inv.setItem(1,createGuiItem(getGUIMat(Storage.isConfigAccess()), "§eConfig Access Enabled", "§eEnabled: " + getTFColor(Storage.isConfigAccess()), tfClick));
        inv.setItem(2,createGuiItem(getGUIMat(Storage.isProjectTeamAccess()), "§eProjectTeam Access Enabled", "§eEnabled: " + getTFColor(Storage.isProjectTeamAccess()), tfClick));
        inv.setItem(3,createGuiItem(getGUIMat(Storage.isStaffAccess()), "§eStaff Access Enabled", "§eEnabled: " + getTFColor(Storage.isStaffAccess()), tfClick));
        inv.setItem(4,createGuiItem(getGUIMat(Storage.isTesterAccess()), "§eTester Access Enabled", "§eEnabled: " + getTFColor(Storage.isTesterAccess()), tfClick));
        inv.setItem(5,createGuiItem(getGUIMat(Storage.isAlternateAccess()), "§eAlternate Access Enabled", "§eEnabled: " + getTFColor(Storage.isAlternateAccess()), tfClick));
        inv.setItem(6,createGuiItem(getGUIMat(Storage.isOtherAccess()), "§eOther Access Enabled", "§eEnabled: " + getTFColor(Storage.isOtherAccess()), tfClick));
        inv.setItem(7,createGuiItem(getGUIMat(Storage.isServerCooldown()), "§eServer Cooldown Enabled", "§eEnabled: " + getTFColor(Storage.isServerCooldown()), tfClick));
        // Messages
        inv.setItem(9,createGuiItem(Material.MAGENTA_STAINED_GLASS_PANE, "§eNot Whitelisted Message", "§eMessage: " + msgSplit(Storage.getNotWhitelistMsg()), msgClick));
        inv.setItem(10,createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "§eBroadcast Message before sending players", "§eMessage: " + msgSplit(Storage.getBroadcastMsg()), msgClick));
        inv.setItem(11,createGuiItem(Material.WHITE_STAINED_GLASS_PANE, "§eMessage sent before sending player", "§eMessage: " + msgSplit(Storage.getSendMsg()), msgClick));
        inv.setItem(12,createGuiItem(Material.PINK_STAINED_GLASS_PANE, "§eMessage sent if player gets kicked", "§eMessage: " + Storage.getKickMsg(), msgClick));
        inv.setItem(13,createGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "§eHub/Lobby server", "§eServer: §6" + Storage.getHubServer(), msgClick, "Refer to Bungee server settings to find Hub/Lobby name."));
        // Durations
        inv.setItem(18,createGuiItem(Material.BLUE_STAINED_GLASS_PANE, "§eServer Cooldown Duration", "§eDuration: §b" + Storage.getServerCooldown(), intClick1, intClick2, intClick3));
        inv.setItem(19,createGuiItem(Material.CYAN_STAINED_GLASS_PANE, "§eDelay Before Starting Kicks", "§eDuration: §b" + Storage.getDelayBeforeStartingKicks(), intClick1, intClick2, intClick3));
        inv.setItem(20,createGuiItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§eKick Delay Per Player", "§eDuration: §b" + Storage.getKickDelayPerPlayer(), intClick1, intClick2, intClick3));
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
	
	// Trying to make multiple lines if too long.
	public static List<String> msgSplit(String... lore) {
		if (lore.length <= 7) return Arrays.asList(lore);
		String lores = Arrays.asList(lore).toString();
		int size = 7;
	    List<String> ret = new ArrayList<String>((lores.length() + size - 1) / size);

	    for (int start = 0; start < lores.length(); start += size) {
	        ret.add(lores.substring(start, Math.min(lores.length(), start + size)));
	    }
	    return ret;
	}

    // Nice little method to create a gui item with a custom name, and description
    protected static ItemStack createGuiItem(final Material material, final String name, final String... lore)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);
        Utility.broadcast(Arrays.asList(lore).toString());
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

    
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e)
    {
        if (e.getInventory().getHolder() != this) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();

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
