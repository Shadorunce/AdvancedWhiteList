package com.gmail.shadoruncegaming.advancedwhitelist;

import java.util.Arrays;

import org.bukkit.Bukkit;
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
	private AdvancedWhiteList m;
	private static Inventory inv = Bukkit.createInventory(null, 27, "AdvancedWhiteList");
	
	
    public WLGui(AdvancedWhiteList m)
    {
		this.m = m;
		// Create a new inventory, with "this" owner for comparison with other inventories, a size of nine, called example
    	//this.inv = Bukkit.createInventory(null, 27, "AdvancedWhiteList");
		inv = Bukkit.createInventory(null, 27, "AdvancedWhiteList");
        // Put the items into the inventory
        initializeItems();
    }



    // You can call this whenever you want to put the items in
    public void initializeItems()
    {
    	String tfClick = "§6Click material to toggle §aTrue§6/§cFalse.";
    	String intClick1 = "§6Left click material to §aIncrease.";
    	String intClick2 = "§6Right click material to §bDecrease.";
    	String intClick3 = "§6Middle click material to type amount.";
    	String msgClick = "§6Click material to type a new text.";
        inv.addItem(createGuiItem(getGUIMat(this.m.getStorage().isWhitelisting()), "§6Whitelist Enabled", "§eEnabled: " + getTFColor(this.m.getStorage().isWhitelisting()), tfClick));
        inv.addItem(createGuiItem(getGUIMat(this.m.getStorage().isConfigAccess()), "§eConfig Access Enabled", "§eEnabled: " + getTFColor(this.m.getStorage().isConfigAccess()), tfClick));
        inv.addItem(createGuiItem(getGUIMat(this.m.getStorage().isProjectTeamAccess()), "§eProjectTeam Access Enabled", "§eEnabled: " + getTFColor(this.m.getStorage().isProjectTeamAccess()), tfClick));
        inv.addItem(createGuiItem(getGUIMat(this.m.getStorage().isStaffAccess()), "§eStaff Access Enabled", "§eEnabled: " + getTFColor(this.m.getStorage().isStaffAccess()), tfClick));
        inv.addItem(createGuiItem(getGUIMat(this.m.getStorage().isTesterAccess()), "§eTester Access Enabled", "§eEnabled: " + getTFColor(this.m.getStorage().isTesterAccess()), tfClick));
        inv.addItem(createGuiItem(getGUIMat(this.m.getStorage().isAlternateAccess()), "§eAlternate Access Enabled", "§eEnabled: " + getTFColor(this.m.getStorage().isAlternateAccess()), tfClick));
        inv.addItem(createGuiItem(getGUIMat(this.m.getStorage().isOtherAccess()), "§eOther Access Enabled", "§eEnabled: " + getTFColor(this.m.getStorage().isOtherAccess()), tfClick));
        inv.addItem(createGuiItem(getGUIMat(this.m.getStorage().isServerCooldown()), "§eServer Cooldown Enabled", "§eEnabled: " + getTFColor(this.m.getStorage().isServerCooldown()), tfClick));
        inv.addItem(createGuiItem(Material.BLUE_STAINED_GLASS_PANE, "§eServer Cooldown Duration", "§eDuration: §b" + this.m.getStorage().getServerCooldown(), intClick1, intClick2, intClick3));
        inv.addItem(createGuiItem(Material.CYAN_STAINED_GLASS_PANE, "§eDelay Before Starting Kicks", "§eDuration: §b" + this.m.getStorage().getDelayBeforeStartingKicks(), intClick1, intClick2, intClick3));
        inv.addItem(createGuiItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, "§eKick Delay Per Player", "§eDuration: §b" + this.m.getStorage().getKickDelayPerPlayer(), intClick1, intClick2, intClick3));
        inv.addItem(createGuiItem(Material.MAGENTA_STAINED_GLASS_PANE, "§eNot Whitelisted Message", "§eMessage: " + this.m.getStorage().getNotWhitelistMsg(), msgClick));
        inv.addItem(createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "§eBroadcast Message before sending players", "§eMessage: " + this.m.getStorage().getBroadcastMsg(), msgClick));
        inv.addItem(createGuiItem(Material.WHITE_STAINED_GLASS_PANE, "§eMessage sent before sending player", "§eMessage: " + this.m.getStorage().getSendMsg(), msgClick));
        inv.addItem(createGuiItem(Material.PINK_STAINED_GLASS_PANE, "§eMessage sent if player gets kicked", "§eMessage: " + this.m.getStorage().getKickMsg(), msgClick));
        inv.addItem(createGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "§eHub/Lobby server", "§eServer: §6" + this.m.getStorage().getHubServer(), msgClick, "Refer to Bungee server settings to find Hub/Lobby name."));
    }

	public Material getGUIMat(Boolean trueFalse) {
		Material endMaterial = null;

		endMaterial = Material.BLACK_STAINED_GLASS_PANE;
		if (trueFalse) endMaterial = Material.GREEN_STAINED_GLASS_PANE;
		if (!trueFalse) endMaterial = Material.RED_STAINED_GLASS_PANE;		

		return endMaterial;
	}
	
	public String getTFColor(Boolean trueFalse) {
		String TF = "error";
		if (trueFalse) TF = "§aTrue";
		if (!trueFalse) TF = "§cFalse";
		return TF;
	}

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore)
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
        ent.openInventory(inv);
    }

/*    // Used in InventoryClick events due to InventoryHandler
    @Override
    public Inventory getInventory()
    {
        return inv;
    }*/
    
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
