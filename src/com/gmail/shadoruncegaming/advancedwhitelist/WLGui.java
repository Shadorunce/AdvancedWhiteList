package com.gmail.shadoruncegaming.advancedwhitelist;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
	private static AdvancedWhiteList m;
	private static Inventory inv;
	static String tfClick = "§6Left Click to toggle §aTrue§6/§cFalse.";
	static String tfClick2 = "§6Right Click to put permission in chat.";
	static String intClick1 = "§6Left Click to §aIncrease. Shift for +5";
	static String intClick2 = "§6Right Click to §bDecrease. Shift for -5";
	static String intClick3 = "§6Middle Click to type amount.";
	static String msgClick = "§6Left Click to type a new text.";
	static String msgClick2 = "§6Right Click to view the full message in chat.";
	static String cmdClick = "§6Click to run command function";
	static String cmdClickConf = "§cShift Right-Click to run command function";
	static String title = "§4§lA§2§lWL";
	static String perm = "§6Bypass permission: §dAdvancedWhiteList.bypass.";
	static int totalNames = (Bukkit.getOnlinePlayers().size() + WLStorage.getWhiteLists().size());
	static int totalRows = (totalNames / 9);
	static int pagesTotal = (totalRows / 5) + 1;
	static int rows = 6;
	static int slots = rows * 9;
	private ItemStack[] restoredContents;
	static int currentPage = 1;

	public WLGui(AdvancedWhiteList m) {
		WLGui.m = m;
	}
	
	/* This would require NMS to be used at this time, which would need to be redone per MCUpdate due to packet compatibility.
	public static void anvilInv(HumanEntity ent, String anvilTitle) {
		inv = Bukkit.createInventory(null, InventoryType.ANVIL, title + ": §6" + anvilTitle);
		inv.setItem(0, createGuiItem(WLStorage.getGuiAnvilItem(), "§6Rename me to Player Name", "§dThe name you put will be used for the command selected.", "§dClick the item on the right to run the command with the given name."));
        ent.openInventory(inv);
	}*/

    // You can call this whenever you want to put the items in
    public static void initializeItems()
    {
    	// 0-8 | 9-17 | 18-26 | (27-35) | 36-44 | 45-53
    	int rows = 4;
    	slots = rows * 9;
		inv = Bukkit.createInventory(null, slots, WLGui.title + ": §5§l" + "Commands");
        
    	// Filler
    	for (Integer i = 0 ; i < inv.getSize() ; i++) {
        	inv.setItem(i,createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GOLD + "AWL on: " + Utility.getTFColor(WLStorage.isWhitelisting())));
        }
    	// setItem(slot, material)
    	// CreateGuiItem(Material, title, lore....)
    	// getGuiMat(determine material based on other variable)
    	// Enabled?
        inv.setItem(0,createGuiItem(Utility.getGUITFMat(WLStorage.isWhitelisting()), 
        		"§6§lWhitelist Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isWhitelisting()), 
        		tfClick, tfClick2, perm + "operator"));
        inv.setItem(1,createGuiItem(Utility.getGUITFMat(WLStorage.isConfigAccess()), 
        		"§eConfig Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isConfigAccess()), 
        		"§dThis will need to be enabled to allow added players-"," to join regardless of permissions.", tfClick));
        inv.setItem(2,createGuiItem(Utility.getGUITFMat(WLStorage.isProjectTeamAccess()), 
        		"§eProjectTeam Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isProjectTeamAccess()), 
        		tfClick, tfClick2, perm + "ProjectTeam"));
        inv.setItem(3,createGuiItem(Utility.getGUITFMat(WLStorage.isStaffAccess()), 
        		"§eStaff Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isStaffAccess()), 
        		tfClick, tfClick2, perm + "Staff"));
        inv.setItem(4,createGuiItem(Utility.getGUITFMat(WLStorage.isTesterAccess()), 
        		"§eTester Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isTesterAccess()), 
        		tfClick, tfClick2, perm + "Tester"));
        inv.setItem(5,createGuiItem(Utility.getGUITFMat(WLStorage.isAlternateAccess()), 
        		"§eAlternate Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isAlternateAccess()), 
        		tfClick, tfClick2, perm + "Alternate"));
        inv.setItem(6,createGuiItem(Utility.getGUITFMat(WLStorage.isOtherAccess()), 
        		"§eOther Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isOtherAccess()), 
        		tfClick, tfClick2, perm + "Other"));
        inv.setItem(7,createGuiItem(Utility.getGUITFMat(WLStorage.isServerCooldown()), 
        		"§eServer Cooldown Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isServerCooldown()), 
        		tfClick));
        // Messages
        int maxSize = 40;
        String string;
        string = WLStorage.getNotWhitelistMsg();
        if (WLStorage.getNotWhitelistMsg().length() > maxSize) {
        	string = WLStorage.getNotWhitelistMsg().substring(0, maxSize-1) + "[...]";
        }
        inv.setItem(9,createGuiItem(WLStorage.getGuiNotWlMsg(), "§eNot Whitelisted Message", 
        		"§eMessage: " + string, msgClick, msgClick2));
        string = WLStorage.getBroadcastMsg();
        if (WLStorage.getNotWhitelistMsg().length() > maxSize) {
        	string = WLStorage.getBroadcastMsg().substring(0, maxSize-1) + "[...]";
        }
        inv.setItem(10,createGuiItem(WLStorage.getGuiBcMsg(), "§eBroadcast Message before sending players",
        		"§eMessage: " + string, msgClick, msgClick2));
        string = WLStorage.getSendMsg();
        if (WLStorage.getNotWhitelistMsg().length() > maxSize) string = WLStorage.getSendMsg().substring(0, maxSize-1) + "[...]";
        inv.setItem(11,createGuiItem(WLStorage.getGuiSendMsg(), "§eMessage sent before sending player", 
        		"§eMessage: " + string, msgClick, msgClick2));
        string = WLStorage.getKickMsg();
        if (WLStorage.getNotWhitelistMsg().length() > maxSize) string = WLStorage.getKickMsg().substring(0, maxSize-1) + "[...]";
        inv.setItem(12,createGuiItem(WLStorage.getGuiKickMsg(), "§eMessage sent if player gets kicked", 
        		"§eMessage: " + string, msgClick, msgClick2));
        inv.setItem(13,createGuiItem(WLStorage.getGuiLobby(), "§eServer sent to when Whitelist Enforced", 
        		"§eServer: §6" + WLStorage.getHubServer(), msgClick, "Refer to Bungee server settings to find Hub/Lobby name."));
        // Durations
        inv.setItem(18,createGuiItem(WLStorage.getGuiCdDuration(), "§eServer Cooldown Duration", 
        		"§eDuration: §b" + WLStorage.getServerCooldown() + " for players", 
        		"-- §b" + WLStorage.getServerCooldown()/2 + " for ProjectTeam perm", 
        		"-- §b" + WLStorage.getServerCooldown()/3 + " for operator perm", intClick1, intClick2, intClick3));
        inv.setItem(19,createGuiItem(WLStorage.getGuiDelayStartKicks(), 
        		"§eDelay Before Starting Kicks", "§eDuration: §b" + WLStorage.getDelayBeforeStartingKicks(), 
        		intClick1, intClick2, intClick3));
        inv.setItem(20,createGuiItem(WLStorage.getGuiKickDelayPlayer(), 
        		"§eKick Delay Per Player", "§eDuration: §b" + WLStorage.getKickDelayPerPlayer(),
        		intClick1, intClick2, intClick3));
        inv.setItem(26,createGuiItem(Utility.getGUITFMat(WLStorage.isOpBypass()), 
        		"§eOP and * Access Enabled", "§eEnabled: " + Utility.getInvertedTFColor(WLStorage.isOpBypass()), 
        		"§dIf enabled, OP and * perms will have admin and bypass access.", 
        		"§c-- It's recommended to keep false for security.", "§6Left Click to toggle §cTrue§6/§aFalse."));
        // Commands
        	//status
        setStatusGuiItem(); // Slot 27 for the status
        inv.setItem(28,createGuiItem(WLStorage.getGuiWlConfigList(), "§bConfig Access List or GUI", 
        		"§9Left-Click to get the Config Access List in chat.", 
        		"§6Shift Left-Click for GUI of online and whitelisted players", 
        		"§dRight-Click to get Help Menu in chat", 
        		"§dShift Right-Click to get Help command suggetion"));
        inv.setItem(29,createGuiItem(WLStorage.getGuiAdd(), ChatColor.DARK_GREEN + 
        		"Add player to Config Access List", "§dNeeds Config Access Enabled to ", 
        		"§d-- allow added players to connect.", cmdClick, "Right-click for convert whitelist command"));
        inv.setItem(30,createGuiItem(WLStorage.getGuiRemove(), ChatColor.DARK_RED + 
        		"Remove player from Config Access List", cmdClick, "Right-click for Name/UUID check command"));
        inv.setItem(32,createGuiItem(WLStorage.getGuiAddAllOnline(), ChatColor.DARK_GREEN + 
        		"Add everyone to Config Access", "§dAdd everyone currently connected.", cmdClickConf));
        inv.setItem(33,createGuiItem(WLStorage.getGuiRemoveAll(), ChatColor.DARK_RED + 
        		"Reset Config Access List", "Remove all names from the Config Access List", cmdClickConf));
        inv.setItem(34,createGuiItem(WLStorage.getGuiSendPlayers(), "§cSend Players to lobby", 
        		ChatColor.DARK_RED + "  Only if WhiteList is on,", ChatColor.DARK_RED + 
        		"  only sends non-whitelisted players.", cmdClickConf));
        inv.setItem(35,createGuiItem(WLStorage.getGuiRestartServer(), "§cRestart the server", 
        		ChatColor.DARK_RED + "This will send players back to lobby first.", cmdClickConf));
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
    
	public static void playersGUI(final HumanEntity ent, int currentPage) {
    	// 0-8 | 9-17 | 18-26 | (27-35) | 36-44 | 45-53
    	// Top row has left and right arrows for pages, and information in center.

		totalNames = (Bukkit.getOnlinePlayers().size() + WLStorage.getWhiteLists().size());
    	totalRows = (totalNames / 9);
    	pagesTotal = (totalRows / 5) + 1;
    	rows = 6;
    	slots = rows * 9; // 9*6=54
		inv = Bukkit.createInventory(null, slots, WLGui.title + ": §5§l" + "Config Whitelist");

    	// Filler
    	for (Integer i = 0 ; i < inv.getSize() ; i++) {
        	inv.setItem(i,createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
    	inv.setItem(0, createGuiItem(Material.BARRIER, "§bBack", "Click to go back to previous menu"));
    	inv.setItem(3, createGuiItem(Material.PAPER, "§ePrevious Page"));
    	inv.setItem(4, createGuiItem(Utility.getGUITFMat(WLStorage.isWhitelisting()),
    			"§6§lAdvancedWhiteList Config", 
    			"§6Whitelist Enabled: " + Utility.getTFColor(WLStorage.isWhitelisting()), 
    			"§6Config Access Enabled: " + Utility.getTFColor(WLStorage.isConfigAccess()), 
    			"Left-Click to toggle Whitelist enabled", "Right-Click to toggle Config Access"));
    	inv.setItem(5, createGuiItem(Material.PAPER, "§eNext Page"));
    	inv.setItem(8, createGuiItem(Material.BOOKSHELF, "§9Refresh List", "Click to refresh the list of heads/names.", "Right-Click to get text whitelist."));

    	getPlayerHeads(currentPage);
    	ent.openInventory(inv);
    }
    
	@SuppressWarnings("deprecation")
	public static void getPlayerHeads(int page) {
//    	Bukkit.getScheduler().runTaskAsynchronously(m, new Runnable() {
//			@SuppressWarnings("deprecation")
//			@Override
//			public void run() {
				// 45 per page
				int headNum = ((page-1)*45); // used for where to start for which page of players.
				// TODO
				int i = 9; // used for start gui heads location
				int in = 0; // Used for counting through players in each list.
				
				if (i <= 53) {
					for (int index = i ; index <= 53 ; index++) {
						inv.setItem(index,createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " "));
					}
				}
				for (String player : WLStorage.getWhiteLists()) {
					in++;
					if ((i > 53)) {
						Bukkit.getScheduler().cancelTasks(m);
						break;
					}
					if (in < headNum) {
						continue;
					}
					if (i <= 53) { // 9-53 of slots   then 63 for page 2    81 for page 3
						OfflinePlayer p = Bukkit.getOfflinePlayer(player);
						inv.setItem(i, createGuiHead(Bukkit.getOfflinePlayer(player), p.isOnline()));
						i++;
					}
				}
				if (i <= 53) {
					for (Player player : m.getServer().getOnlinePlayers()) {
//						Utility.sendConsole("i: " + i +
//								"\nin: " + in + 
//								"\npage: " + page
//								+ "\nheadNum: " + headNum +
//								"\ntotalNames: " + totalNames + 
//								"\nList Size: " + WLStorage.getWhiteLists().size() + 
//								"\nOnline Player size: " + Bukkit.getOnlinePlayers().size());
						in++;
						if ((i > 53)) {
							Bukkit.getScheduler().cancelTasks(m);
							break;
						}
						if (in < headNum) {
							continue;
						}
						if (WLStorage.isWhitelisted(player.getName())) {
							continue;
						}
						if (i <= 53 && !WLStorage.isWhitelisted(player.getName())) { // 9-53 of slots   then 63 for page 2    81 for page 3
							inv.setItem(i, createGuiHead(player, player.isOnline()));
							i++;
						}
					}
				}
				Bukkit.getScheduler().cancelTasks(m);
				return;
//			}
//    	});
    }
    
    public static ItemStack createGuiHead(OfflinePlayer player, Boolean tf) {
    	ItemStack head = new ItemStack(Material.PAPER, 1);
    	if (!tf) {head = new ItemStack(Material.WRITABLE_BOOK, 1);}
    	if (tf) {head = new ItemStack(Material.WRITTEN_BOOK, 1);}
    	ItemMeta meta = head.getItemMeta(); //SkullMeta meta = (SkullMeta) head.getItemMeta();
    	//meta.setOwningPlayer(player);
    	meta.setDisplayName(player.getName());
    	meta.setLore(Arrays.asList("§6Player Online: " + Utility.getTFColor(player.isOnline()),
    			"§6On Config Whitelist: " + Utility.getTFColor(WLStorage.isWhitelisted(player.getName())),
    			"§6Click to §aadd§7/§cremove §7-",
    			"  §6player from Config Whitelist"));
    	head.setItemMeta(meta);
    	return head;
    }
    
    // Check for clicks on items
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
    	if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains(title)) {
    		e.setCancelled(true);
//    		Utility.sendMsg(p, "Slot Clicked: " + String.valueOf(e.getSlot())
//    				+ "\nClicked Inv: " + e.getClickedInventory()
//    				+ "\nCursor: " + e.getCursor()+
//    				"\nRaw Slot: " + e.getRawSlot()+
//    				"\nAction: " + e.getAction());
			if (e.getCurrentItem() == null) {
				if (e.getRawSlot() == -999) {
					if (e.getView().getTitle().contains("Commands")) {
						e.getWhoClicked().closeInventory();
					}
					if (e.getView().getTitle().contains("Config Whitelist")) {
						openInventory(p);
					}
					return;
				}
				if (e.getRawSlot() != -999) {return;}
			}
    		TextComponent msg = new TextComponent("Click me to be given the command. Hover to see current.");
    		msg.setColor(ChatColor.GOLD);

			String name = e.getCurrentItem().getItemMeta().getDisplayName();
    		if (e.getCurrentItem() != null) {
    			if (e.getRawSlot() < slots && e.getView().getTitle().contains("Commands")) { // Main AWL Inv check
    	    		e.setCancelled(true);
	    			// Boolean
	    			if (name.contains("Whitelist Enabled")) {
	    				if (e.isLeftClick()) {
	        				WLStorage.setWhitelist(Utility.invertBoolean(WLStorage.isWhitelisting()));
	        				inv.setItem(0,createGuiItem(Utility.getGUITFMat(WLStorage.isWhitelisting()), "§6§lWhitelist Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isWhitelisting()), tfClick, tfClick2, perm + "operator"));
	        		        setStatusGuiItem();
	        				// Redo Filler
	        		    	for (Integer i = 0 ; i < inv.getSize() ; i++) {
	        		    		if (inv.getItem(i).getItemMeta().getDisplayName().contains("AWL on")) {
	        		    			inv.setItem(i,createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GOLD + "AWL on: " + Utility.getTFColor(WLStorage.isWhitelisting())));
	        		        	}
	        		        }
	    				}
	    		    	if (e.isRightClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Permission: ").color(ChatColor.GOLD).append("advancedwhitelist.bypass.operator").color(ChatColor.BLUE).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/<permission command> advancedwhitelist.bypass.operator"));
	    	    			p.spigot().sendMessage(msg);
	    		    	}
	    			}
	    			if (name.contains("Config Access Enabled")) {
	    				WLStorage.setConfigAccess(Utility.invertBoolean(WLStorage.isConfigAccess()));
	    				inv.setItem(1,createGuiItem(Utility.getGUITFMat(WLStorage.isConfigAccess()), "§eConfig Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isConfigAccess()), "§dThis will need to be enabled to allow added players-"," to join regardless of permissions.", tfClick));
	    		        setStatusGuiItem();
	    			}
	    			if (name.contains("ProjectTeam Access Enabled"))  {
	    				if (e.isLeftClick()) {
	        				WLStorage.setProjectTeamAccess(Utility.invertBoolean(WLStorage.isProjectTeamAccess()));
	        				inv.setItem(2,createGuiItem(Utility.getGUITFMat(WLStorage.isProjectTeamAccess()), "§eProjectTeam Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isProjectTeamAccess()), tfClick, tfClick2, perm + "ProjectTeam"));
	        		        setStatusGuiItem();
	    				}
	    		    	if (e.isRightClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Permission: ").color(ChatColor.GOLD).append("advancedwhitelist.bypass.projectteam").color(ChatColor.BLUE).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/<permission command> advancedwhitelist.bypass.projectteam"));
	    	    			p.spigot().sendMessage(msg);
	    		    	}
	    			}
	    			if (name.contains("Staff Access Enabled")) {
	    				if (e.isLeftClick()) {
	        				WLStorage.setStaffAccess(Utility.invertBoolean(WLStorage.isStaffAccess()));
	        				inv.setItem(3,createGuiItem(Utility.getGUITFMat(WLStorage.isStaffAccess()), "§eStaff Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isStaffAccess()), tfClick, tfClick2, perm + "Staff"));
	        		        setStatusGuiItem();
	    				}
	    		    	if (e.isRightClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Permission: ").color(ChatColor.GOLD).append("advancedwhitelist.bypass.Staff").color(ChatColor.BLUE).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/<permission command> advancedwhitelist.bypass.Staff"));
	    	    			p.spigot().sendMessage(msg);
	    		    	}
	    			}
	    			if (name.contains("Tester Access Enabled")) {
	    				if (e.isLeftClick()) {
	        				WLStorage.setTesterAccess(Utility.invertBoolean(WLStorage.isTesterAccess()));
	        				inv.setItem(4,createGuiItem(Utility.getGUITFMat(WLStorage.isTesterAccess()), "§eTester Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isTesterAccess()), tfClick, tfClick2, perm + "Tester"));
	        		        setStatusGuiItem();
	    				}
	    		    	if (e.isRightClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Permission: ").color(ChatColor.GOLD).append("advancedwhitelist.bypass.Tester").color(ChatColor.BLUE).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/<permission command> advancedwhitelist.bypass.Tester"));
	    	    			p.spigot().sendMessage(msg);
	    		    	}
	    			}
	    			if (name.contains("Alternate Access Enabled")) {
	    				if (e.isLeftClick()) {
	        				WLStorage.setAlternateAccess(Utility.invertBoolean(WLStorage.isAlternateAccess()));
	        				inv.setItem(5,createGuiItem(Utility.getGUITFMat(WLStorage.isAlternateAccess()), "§eAlternate Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isAlternateAccess()), tfClick, tfClick2, perm + "Alternate"));
	        		        setStatusGuiItem();
	    				}
	    		    	if (e.isRightClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Permission: ").color(ChatColor.GOLD).append("advancedwhitelist.bypass.Alternate").color(ChatColor.BLUE).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/<permission command> advancedwhitelist.bypass.Alternate"));
	    	    			p.spigot().sendMessage(msg);
	    		    	}
	    			}
	    			if (name.contains("Other Access Enabled")) {
	    				if (e.isLeftClick()) {
	        				WLStorage.setOtherAccess(Utility.invertBoolean(WLStorage.isOtherAccess()));
	        				inv.setItem(6,createGuiItem(Utility.getGUITFMat(WLStorage.isOtherAccess()), "§eOther Access Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isOtherAccess()), tfClick, tfClick2, perm + "Other"));
	        		        setStatusGuiItem();
	    				}
	    		    	if (e.isRightClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Permission: ").color(ChatColor.GOLD).append("advancedwhitelist.bypass.Other").color(ChatColor.BLUE).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/<permission command> advancedwhitelist.bypass.Other"));
	    	    			p.spigot().sendMessage(msg);
	    		    	}
	    			}
	    			if (name.contains("Server Cooldown Enabled")) {
	    				WLStorage.setServerCooldown(Utility.invertBoolean(WLStorage.isServerCooldown()));
	    		        inv.setItem(7,createGuiItem(Utility.getGUITFMat(WLStorage.isServerCooldown()), "§eServer Cooldown Enabled", "§eEnabled: " + Utility.getTFColor(WLStorage.isServerCooldown()), tfClick));
	    		        setStatusGuiItem();
	    			}
	    			
	    			// Messages
	    			
	    			if (name.contains("Not Whitelisted Message")) {
	    				if (e.isRightClick()) Utility.sendMsg(p, "§7Not Whitelisted Message: " + WLStorage.getNotWhitelistMsg());
	    				if (e.isLeftClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getNotWhitelistMsg()).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl notwlmsg <msg>"));
	    	    			p.spigot().sendMessage(msg);
	    				}
	    		    }
	    			if (name.contains("Broadcast Message before sending players")) {
	    				if (e.isLeftClick()) {
	    					//if (!e.isShiftClick()) anvilInv(e.getWhoClicked(), "Broadcast Message before sending players");
	    					if (e.isShiftClick() || !e.isShiftClick()) {
		    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getBroadcastMsg()).create()));
		    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl bcastmsg <message>"));
		    	    			p.spigot().sendMessage(msg);
	    					}
	    				}
	    				if (e.isRightClick()) Utility.sendMsg(p, "§7Broadcast Message before sending players: " + WLStorage.getBroadcastMsg());

	    		    }
	    			if (name.contains("Message sent before sending player")) {
	    				if (e.isLeftClick()) {
	    					//if (!e.isShiftClick()) anvilInv(e.getWhoClicked(), "Message sent before sending player");
	    					if (e.isShiftClick() || !e.isShiftClick()) {
		    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getSendMsg()).create()));
		    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl sendmsg <message>"));
		    	    			p.spigot().sendMessage(msg);
	    					}
	    				}
	    				if (e.isRightClick()) Utility.sendMsg(p, "§7Message sent before sending player: " + WLStorage.getSendMsg());
	    		    }
	    			if (name.contains("Message sent if player gets kicked")) {
	    				if (e.isLeftClick()) {
	    					//if (!e.isShiftClick()) anvilInv(e.getWhoClicked(), "Message sent if player gets kicked");
	    					if (e.isShiftClick() || !e.isShiftClick()) {
		    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getKickMsg()).create()));
		    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl kickmsg <message>"));
		    	    			p.spigot().sendMessage(msg);
	    					}
	    				}
	    				if (e.isRightClick()) Utility.sendMsg(p, "§7Message sent if player gets kicked: " + WLStorage.getKickMsg());
	    		    }
	    			if (name.contains("Hub Server")) {
	    				if (e.isRightClick()) Utility.sendMsg(p, "§7Hub Server: " + WLStorage.getHubServer());
	    				if (e.isLeftClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(WLStorage.getHubServer()).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl hubserver <servername>"));
	    	    			p.spigot().sendMessage(msg);
	    				}
	    			}
	    			
	    			// Durations
	    			if (name.contains("Server Cooldown Duration")) {
	    				if (e.isLeftClick()) {
	    					if (!e.isShiftClick()) {
	    						WLStorage.setServerCooldownTime(WLStorage.getServerCooldown() + 1);
	    					}
	    					if (e.isShiftClick()) {
	    						WLStorage.setServerCooldownTime(WLStorage.getServerCooldown() + 5);
	    					}
	    				}
	    				if (e.isRightClick()) {
	    					if (!e.isShiftClick()) if (WLStorage.getServerCooldown() > 0) {
	    						WLStorage.setServerCooldownTime(WLStorage.getServerCooldown() - 1);
	    					}
	    					if (e.isShiftClick()) {
	    						if (WLStorage.getServerCooldown() > 4) {
		    						WLStorage.setServerCooldownTime(WLStorage.getServerCooldown() - 5);
		    					}
	    						if (WLStorage.getServerCooldown() <= 4 && WLStorage.getServerCooldown() > 0) {
	    							WLStorage.setServerCooldownTime(WLStorage.getServerCooldown() - 1);
	    						}
	    					}
	    				}
	    				if (e.getClick() == ClickType.MIDDLE) {
							//if (!e.isShiftClick()) {anvilInv(e.getWhoClicked(), "Kick Delay Per Player");}
							if (e.isShiftClick() || !e.isShiftClick()) {
								msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
										new ComponentBuilder(Utility.numToString(WLStorage.getKickDelayPerPlayer())).create()));
		    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl kickdelay <seconds>"));
		    	    			p.spigot().sendMessage(msg);
							}
						}
	    				inv.setItem(18,createGuiItem(WLStorage.getGuiCdDuration(), "§eServer Cooldown Duration", 
	    		        		"§eDuration: §b" + WLStorage.getServerCooldown() + " for players", 
	    		        		"-- §b" + WLStorage.getServerCooldown()/2 + " for ProjectTeam perm", 
	    		        		"-- §b" + WLStorage.getServerCooldown()/3 + " for operator perm", intClick1, intClick2, intClick3));
	    				setStatusGuiItem();
	    			}
	    			if (e.getRawSlot() == 17 && e.isRightClick() && e.isShiftClick()) Utility.executeConsole("plugman reload AdvancedWhiteList");
	    			
	    			if (name.contains("Delay Before Starting Kicks")) {
	    				if (e.isLeftClick()) {
	    					if (!e.isShiftClick()) {
	    						WLStorage.setDelayBeforeStartingKicks(WLStorage.getDelayBeforeStartingKicks() + 1);
	    					}
	    					if (e.isShiftClick()) {
	    						WLStorage.setDelayBeforeStartingKicks(WLStorage.getDelayBeforeStartingKicks() + 5);
	    					}
	    				}
	    				if (e.isRightClick()) {
	    					if (!e.isShiftClick()) if (WLStorage.getDelayBeforeStartingKicks() > 0) {
	    						WLStorage.setDelayBeforeStartingKicks(WLStorage.getDelayBeforeStartingKicks() - 1);
	    					}
	    					if (e.isShiftClick()) {
	    						if (WLStorage.getDelayBeforeStartingKicks() > 4) {
	    						WLStorage.setDelayBeforeStartingKicks(WLStorage.getDelayBeforeStartingKicks() - 5);
	    						}
	    						if (WLStorage.getDelayBeforeStartingKicks() <= 4 && WLStorage.getDelayBeforeStartingKicks() > 0) {
	    							WLStorage.setDelayBeforeStartingKicks(WLStorage.getDelayBeforeStartingKicks() - 1);
	    						}
	    					}
	    				}
	    				if (e.getClick() == ClickType.MIDDLE) {
							//if (!e.isShiftClick()) {anvilInv(e.getWhoClicked(), "Kick Delay Per Player");}
							if (e.isShiftClick() || !e.isShiftClick()) {
								msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
										new ComponentBuilder(Utility.numToString(WLStorage.getKickDelayPerPlayer())).create()));
		    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl kickdelay <seconds>"));
		    	    			p.spigot().sendMessage(msg);
							}
						}
	    		        inv.setItem(19,createGuiItem(WLStorage.getGuiDelayStartKicks(), 
	    		        		"§eDelay Before Starting Kicks", "§eDuration: §b" + WLStorage.getDelayBeforeStartingKicks(), 
	    		        		intClick1, intClick2, intClick3));
	    		        setStatusGuiItem();
	    			}
	    			if (name.contains("Kick Delay Per Player")) {
	    				if (e.isLeftClick()) {
	    					if (!e.isShiftClick()) {
	    						WLStorage.setKickDelayPerPlayer(WLStorage.getKickDelayPerPlayer() + 1);
	    					}
	    					if (e.isShiftClick()) {
	    						WLStorage.setKickDelayPerPlayer(WLStorage.getKickDelayPerPlayer() + 5);
	    					}
	    				}
	    				if (e.isRightClick()) {
	    					if (!e.isShiftClick()) if (WLStorage.getKickDelayPerPlayer() > 0) {
	    						WLStorage.setKickDelayPerPlayer(WLStorage.getKickDelayPerPlayer() - 1);
	    					}
	    					if (e.isShiftClick()) {
	    						if (WLStorage.getKickDelayPerPlayer() > 4) {
	    						WLStorage.setKickDelayPerPlayer(WLStorage.getKickDelayPerPlayer() - 5);
	    						}
	    						if (WLStorage.getKickDelayPerPlayer() <= 4 && WLStorage.getKickDelayPerPlayer() > 0) {
	    							WLStorage.setKickDelayPerPlayer(WLStorage.getKickDelayPerPlayer() - 1);
	    						}
	    					}
	    				}
	    				if (e.getClick() == ClickType.MIDDLE) {
							//if (!e.isShiftClick()) {anvilInv(e.getWhoClicked(), "Kick Delay Per Player");}
							if (e.isShiftClick() || !e.isShiftClick()) {
								msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
										new ComponentBuilder(Utility.numToString(WLStorage.getKickDelayPerPlayer())).create()));
		    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl kickdelay <seconds>"));
		    	    			p.spigot().sendMessage(msg);
							}
						}
	    		        inv.setItem(20,createGuiItem(WLStorage.getGuiKickDelayPlayer(), 
	    		        		"§eKick Delay Per Player", "§eDuration: §b" + WLStorage.getKickDelayPerPlayer(), 
	    		        		intClick1, intClick2, intClick3));
	    		        setStatusGuiItem();
	    			}
	    			if (name.contains("OP and * Access Enabled")) {
	    				if (e.isLeftClick()) {
	        				WLStorage.setOpBypass(Utility.invertBoolean(WLStorage.isOpBypass()));
	        				inv.setItem(26,createGuiItem(Utility.getGUITFMat(WLStorage.isOpBypass()), 
	        						"§eOP and * Access Enabled", "§eEnabled: " + Utility.getInvertedTFColor(WLStorage.isOpBypass()), 
	        		        		"§dIf enabled, OP and * perms will have admin and bypass access.", 
	        		        		"§c-- It's recommended to keep false for security.", 
	        		        		"§6Left Click to toggle §cTrue§6/§aFalse."));
	        		        setStatusGuiItem();
	    				}
	    			}
	    		    	/*if (e.isRightClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Permission: ").color(ChatColor.GOLD).append("advancedwhitelist.bypass.Tester").color(ChatColor.BLUE).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/<permission command> advancedwhitelist.bypass.Tester"));
	    	    			p.spigot().sendMessage(msg);
	    		    	}*/
	    			
	    			// Commands
//	    			if (name.contains("Help Menu")) {
//	    				if (e.isLeftClick()) WLCmd.getHelp(p);
//	    				if (e.isRightClick()) {
//	    				}
//	    			}
	    			if (name.contains("Whitelist Status")) {
	    				if (e.isLeftClick()) {
	    					if (!e.isShiftClick()) WLCmd.getStatus(p);
	    					if (e.isShiftClick()) {
	    						WLStorage.reload();
	    						openInventory(p);
	    					}
	    				}
	    				if (e.isRightClick()) {
	    					if (!e.isShiftClick()) {
	    						String[] args = {"check",e.getWhoClicked().getName()};
	    						WLCmd.accessCheck(e.getWhoClicked(), args);
	    					}
	    					if (e.isShiftClick()) {
				    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
				    					new ComponentBuilder("/awl check <Player Name>").create()));
				    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl check <Player Name/UUID>"));
				    			p.spigot().sendMessage(msg);
	    					}
	    				}
	    			}
	    			if (name.contains("Config Access List or GUI")) {
	    				if (e.isLeftClick()) {
	    					if (!e.isShiftClick()) {
		    					WLCmd.listPlayers(p);
	    					}
	    					if (e.isShiftClick()) {
	    						currentPage = 1;
	    						playersGUI(e.getWhoClicked(),currentPage);
	    					}
	    				}
		    			if (e.isRightClick()) {
							if (!e.isShiftClick()) WLCmd.getHelp(p);
							if (e.isShiftClick()) {
				    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
				    					new ComponentBuilder("/awl help <Page number>").create()));
				    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl help <1-9>"));
				    			p.spigot().sendMessage(msg);
							}
						}
	    			}
	    			if (name.contains("Add player to Config Access List")) {
		    			//if (e.isLeftClick()) {anvilInv(e.getWhoClicked(), "Add Player");}
	    				if (e.isLeftClick()) {
	    					msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
	    							new ComponentBuilder("/awl add <name>").create()));
			    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl add <name>"));
			    			p.spigot().sendMessage(msg);
	    				}
	    				if (e.isRightClick()) {
	    					msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
	    							new ComponentBuilder("/awl convert <mc/awl/ewl/version>").create()));
			    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl convert <name>"));
			    			p.spigot().sendMessage(msg);
	    				}
	    			}
	    			if (name.contains("Remove player from Config Access List")) {
		    			//if (e.isLeftClick()) {anvilInv(e.getWhoClicked(), "Remove Player");}
	    				if (e.isLeftClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
	    	    					new ComponentBuilder("/awl remove <name>").create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl remove <name>"));
	    	    			p.spigot().sendMessage(msg);
	    				}
	    				if (e.isRightClick()) {
	    					msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
	    							new ComponentBuilder("/awl uuid <Name/UUID>").create()));
			    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl uuid <Name/UUID>"));
			    			p.spigot().sendMessage(msg);
	    				}
	    			}
	    			if (name.contains("Add everyone to Config Access")) if (e.isRightClick()) if (e.isShiftClick()) WLCmd.addAllPlayers();
	    			// Caution commands
	    			if (name.contains("Reset Config Access List")) if (e.isRightClick()) if (e.isShiftClick()) {
	    				WLStorage.clearWhiteLists();
	    				Utility.sendMsg(p, "All player names have been removed from Config Access List.");
	    			}
	    			if (name.contains("Send Players to lobby")) if (e.isRightClick()) if (e.isShiftClick()) WLCmd.sendPlayers(p);
	    			if (name.contains("Restart the server")) if (e.isRightClick()) if (e.isShiftClick()) WLCmd.restartServer(p);
    			} // End Slot/Main Inv Check
				/*
				 * // Anvil Inv start if
				 * (e.getClickedInventory().getType().equals(InventoryType.ANVIL) &&
				 * name.contains("Anvil")) { name = "Anvil" + name; String[] names =
				 * name.split(" "); // Also used for numbers. Can't use for messages as it only
				 * allows 35 characters. if (e.getSlotType().equals(SlotType.RESULT)) { if
				 * (ChatColor.translateAlternateColorCodes('&',
				 * e.getView().getTitle()).contains("Add Player")) { WLCmd.addPlayer(p, names);
				 * } if (ChatColor.translateAlternateColorCodes('&',
				 * e.getView().getTitle()).contains("Remove Player")) { WLCmd.removePlayer(p,
				 * names); } if (ChatColor.translateAlternateColorCodes('&',
				 * e.getView().getTitle()).contains("Server Cooldown Duration")) {
				 * WLStorage.setServerCooldownTime(Utility.parseLongFromString(p, name)); } if
				 * (ChatColor.translateAlternateColorCodes('&',
				 * e.getView().getTitle()).contains("Delay Before Starting Kicks")) {
				 * WLStorage.setDelayBeforeStartingKicks(Utility.parseLongFromString(p, name));
				 * } if (ChatColor.translateAlternateColorCodes('&',
				 * e.getView().getTitle()).contains("Kick Delay Per Player")) {
				 * WLStorage.setKickDelayPerPlayer(Utility.parseLongFromString(p, name)); } } //
				 * End Anvil Result check } // End Anvil Inv Check
				 */    			
    			if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains("Config Whitelist")) {
    	    		e.setCancelled(true);
    				if (e.getRawSlot() == 0 && name.contains("Back")) {
        	    		if (Bukkit.getScheduler().toString().contains("advancedwhitelist.WLGui")) {
        	    			Utility.sendMsg(p, "§cPlease wait, GUI loading.");
        	    			return;
        	    		}
    					openInventory(p);
					}
//    				if (e.getRawSlot() == 1) {
//    					if (e.isRightClick() && e.isShiftClick()) {
//	    					Utility.sendConsole("Active Workers" + Bukkit.getScheduler().getActiveWorkers().toString() + "\n");
//	    					Utility.sendConsole("Pending Tasks" + Bukkit.getScheduler().getPendingTasks().toString() + "\n");
//	    					Utility.sendConsole("Currently running" + String.valueOf(Bukkit.getScheduler().isCurrentlyRunning(0) + "\n"));
//	    					Utility.sendConsole("Queued" + String.valueOf(Bukkit.getScheduler().isQueued(0) + "\n"));
//	    			    	Utility.sendConsole("Scheduler to string" + Bukkit.getScheduler().toString().contains("advancedwhitelist.WLGui"));
//    					}
//    				}
    				if (e.getRawSlot() == 3 && name.contains("Previous Page")) {
    					if (pagesTotal > 1) {
	    					if (currentPage > 1) {
	    						currentPage = currentPage - 1;
		    					getPlayerHeads(currentPage);
		    					return;
    						}
	    					if (currentPage == 1 ) {
	    						currentPage = pagesTotal;
		    					getPlayerHeads(currentPage);
		    					return;
    						}
    					}
    				}
    				if (e.getRawSlot() == 4 && name.contains("AdvancedWhiteList Config")) {
	    				if (e.isLeftClick()) {
	        				WLStorage.setWhitelist(Utility.invertBoolean(WLStorage.isWhitelisting()));
	    				}
	    				if (e.isRightClick()) {
	        				WLStorage.setConfigAccess(Utility.invertBoolean(WLStorage.isConfigAccess()));
	    				}
	    		    	inv.setItem(4,createGuiItem(Utility.getGUITFMat(WLStorage.isWhitelisting()),
	    		    			"§6§lAdvancedWhiteList Config", 
	    		    			"§6Whitelist Enabled: " + Utility.getTFColor(WLStorage.isWhitelisting()), 
	    		    			"§6Config Access Enabled: " + Utility.getTFColor(WLStorage.isConfigAccess()), 
	    		    			"Left-Click to toggle Whitelist enabled", "Right-Click to toggle Config Access"));
    				}
    				if (e.getRawSlot() == 5 && name.contains("Next Page")) {
    					if (pagesTotal > 1) {
	    					if (currentPage < pagesTotal) {
	    						currentPage = currentPage + 1;
	    						getPlayerHeads(currentPage);
	    						return;
	    					}
	    					if (currentPage == pagesTotal) {
	    						currentPage = 1;
		    					getPlayerHeads(currentPage);
		    					return;
    						}
    					}
    				}
    				if (e.getRawSlot() == 8 && name.contains("Refresh List")) {
    					if (e.isLeftClick()) {
    						getPlayerHeads(currentPage);
    					}
    					if (e.isRightClick()) {
    						WLCmd.listPlayers(p);
    					}
					}
    				if (e.getRawSlot() > 53) {return;}
    				if (e.getRawSlot() >= 9 && e.getRawSlot() <= 53) {
        	    		e.setCancelled(true);
        	    		if (Bukkit.getScheduler().toString().contains("advancedwhitelist.WLGui")) {
        	    			Utility.sendMsg(p, "§cPlease wait, GUI loading.");
        	    			return;
        	    		}
						if (!name.equalsIgnoreCase("") && !name.equalsIgnoreCase(" ") && !Bukkit.getScheduler().toString().contains("advancedwhitelist.WLGui")) {
							OfflinePlayer pl = Bukkit.getOfflinePlayer(name);
		    	    		e.setCancelled(true);
	    					if (WLStorage.isWhitelisted(name)) {
	    						WLStorage.removeWhitelist(name);
	        					inv.setItem(e.getRawSlot(), createGuiHead(Bukkit.getOfflinePlayer(name),pl.isOnline()));
	        					return;
	    					}
	    					if (!WLStorage.isWhitelisted(name) ) {
	    						WLStorage.addWhitelist(name);
	        					inv.setItem(e.getRawSlot(), createGuiHead(Bukkit.getOfflinePlayer(name),pl.isOnline()));
	        					return;
	    					}
						}
    				}
    			}
    		} // 
    	} // End AWL Inv Check
        return;
    }
    
    public static void setStatusGuiItem() {
    	inv.setItem(27,createGuiItem(WLStorage.getGuiStatus(), 
        		ChatColor.YELLOW + "Whitelist Status", 
        		"§dLeft-Click to get WL Status in chat",
        		"§dShift Left-Click to reload from config",
        		"§dRight-Click to get access check for yourself",
        		"§dShift Right-Click to get access check for another player",
        		"§6Whitelist Enabled: " + Utility.getTFColor(WLStorage.isWhitelisting()), 
        		"§6Config Access Enabled: " + Utility.getTFColor(WLStorage.isConfigAccess()), 
        		"§6ProjectTeam Access Enabled: " + Utility.getTFColor(WLStorage.isProjectTeamAccess()), 
        		"§6Staff Access Enabled: " + Utility.getTFColor(WLStorage.isStaffAccess()), 
        		"§6Tester Access Enabled: " + Utility.getTFColor(WLStorage.isTesterAccess()), 
        		"§6Alternate Acccess Enabled: " + Utility.getTFColor(WLStorage.isAlternateAccess()),
        		"§6Other Access Enabled: " + Utility.getTFColor(WLStorage.isOtherAccess()),
        		"§6Server Cooldown Enabled: " + Utility.getTFColor(WLStorage.isServerCooldown()),
        		"§6Server Cooldown time: " + Utility.getTFColor(WLStorage.isWhitelisting()),
        		"§6Delay Before Starting Kicks: " + Utility.getTFColor(WLStorage.isWhitelisting()),
        		"§6Kick Delay Per Player: " + Utility.getTFColor(WLStorage.isWhitelisting()),
        		"§6OP bypass: " + Utility.getInvertedTFColor(WLStorage.isOpBypass())
        		));
    }
    
    public void BottomGUI(Player p) {
        restoredContents = p.getInventory().getContents();
        //TODO save inv to file, make bottom gui
        p.getInventory().clear();
        refresh();
    }

    public void refresh() {}
    
    public void restoreOnClose(Player p) {
        p.getInventory().clear();
        int n = 0;
        for (ItemStack itemStack : restoredContents) {
            if (itemStack != null) {
                p.getInventory().setItem(n, itemStack);
            }
            ++n;
        }
    }
    
 // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
          e.setCancelled(true);
        }
    }
    
}
