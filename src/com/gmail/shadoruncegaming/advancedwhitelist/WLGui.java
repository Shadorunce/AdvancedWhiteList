package com.gmail.shadoruncegaming.advancedwhitelist;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
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
	static int slots;
	
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
    	slots = rows*9;
		inv = Bukkit.createInventory(null, slots, WLGui.title + ": §5§l" + "Commands");
        
    	// Filler
    	for (Integer i = 0 ; i < inv.getSize() ; i++) {
        	inv.setItem(i,createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GOLD + "AWL on: " + getTFColor(WLStorage.isWhitelisting())));
        }
    	// setItem(slot, material)
    	// CreateGuiItem(Material, title, lore....)
    	// getGuiMat(determine material based on other variable)
    	// Enabled?
        inv.setItem(0,createGuiItem(getGUITFMat(WLStorage.isWhitelisting()), "§6§lWhitelist Enabled", "§eEnabled: " + getTFColor(WLStorage.isWhitelisting()), tfClick, tfClick2, perm + "operator"));
        inv.setItem(1,createGuiItem(getGUITFMat(WLStorage.isConfigAccess()), "§eConfig Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isConfigAccess()), "§dThis will need to be enabled to allow added players-"," to join regardless of permissions.", tfClick));
        inv.setItem(2,createGuiItem(getGUITFMat(WLStorage.isProjectTeamAccess()), "§eProjectTeam Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isProjectTeamAccess()), tfClick, tfClick2, perm + "ProjectTeam"));
        inv.setItem(3,createGuiItem(getGUITFMat(WLStorage.isStaffAccess()), "§eStaff Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isStaffAccess()), tfClick, tfClick2, perm + "Staff"));
        inv.setItem(4,createGuiItem(getGUITFMat(WLStorage.isTesterAccess()), "§eTester Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isTesterAccess()), tfClick, tfClick2, perm + "Tester"));
        inv.setItem(5,createGuiItem(getGUITFMat(WLStorage.isAlternateAccess()), "§eAlternate Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isAlternateAccess()), tfClick, tfClick2, perm + "Alternate"));
        inv.setItem(6,createGuiItem(getGUITFMat(WLStorage.isOtherAccess()), "§eOther Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isOtherAccess()), tfClick, tfClick2, perm + "Other"));
        inv.setItem(7,createGuiItem(getGUITFMat(WLStorage.isServerCooldown()), "§eServer Cooldown Enabled", "§eEnabled: " + getTFColor(WLStorage.isServerCooldown()), tfClick));
        // Messages
        int maxSize = 40;
        String string;
        string = WLStorage.getNotWhitelistMsg();
        if (WLStorage.getNotWhitelistMsg().length() > maxSize) string = WLStorage.getNotWhitelistMsg().substring(0, maxSize-1) + "[...]";
        inv.setItem(9,createGuiItem(WLStorage.getGuiNotWlMsg(), "§eNot Whitelisted Message", "§eMessage: " + string, msgClick, msgClick2));
        string = WLStorage.getBroadcastMsg();
        if (WLStorage.getNotWhitelistMsg().length() > maxSize) string = WLStorage.getBroadcastMsg().substring(0, maxSize-1) + "[...]";
        inv.setItem(10,createGuiItem(WLStorage.getGuiBcMsg(), "§eBroadcast Message before sending players", "§eMessage: " + string, msgClick, msgClick2));
        string = WLStorage.getSendMsg();
        if (WLStorage.getNotWhitelistMsg().length() > maxSize) string = WLStorage.getSendMsg().substring(0, maxSize-1) + "[...]";
        inv.setItem(11,createGuiItem(WLStorage.getGuiSendMsg(), "§eMessage sent before sending player", "§eMessage: " + string, msgClick, msgClick2));
        string = WLStorage.getKickMsg();
        if (WLStorage.getNotWhitelistMsg().length() > maxSize) string = WLStorage.getKickMsg().substring(0, maxSize-1) + "[...]";
        inv.setItem(12,createGuiItem(WLStorage.getGuiKickMsg(), "§eMessage sent if player gets kicked", "§eMessage: " + string, msgClick, msgClick2));
        inv.setItem(13,createGuiItem(WLStorage.getGuiLobby(), "§eServer sent to when Whitelist Enforced", "§eServer: §6" + WLStorage.getHubServer(), msgClick, "Refer to Bungee server settings to find Hub/Lobby name."));
        // Durations
        inv.setItem(18,createGuiItem(WLStorage.getGuiCdDuration(), "§eServer Cooldown Duration", 
        		"§eDuration: §b" + WLStorage.getServerCooldown() + " for players", 
        		"-- §b" + WLStorage.getServerCooldown()/2 + " for ProjectTeam perm", 
        		"-- §b" + WLStorage.getServerCooldown()/3 + " for operator perm", intClick1, intClick2, intClick3));
        inv.setItem(19,createGuiItem(WLStorage.getGuiDelayStartKicks(), "§eDelay Before Starting Kicks", "§eDuration: §b" + WLStorage.getDelayBeforeStartingKicks(), intClick1, intClick2, intClick3));
        inv.setItem(20,createGuiItem(WLStorage.getGuiKickDelayPlayer(), "§eKick Delay Per Player", "§eDuration: §b" + WLStorage.getKickDelayPerPlayer(), intClick1, intClick2, intClick3));
        inv.setItem(26,createGuiItem(getGUITFMat(WLStorage.isOpBypass()), "§eOP and * Access Enabled", "§eEnabled: " + getInvertedTFColor(WLStorage.isOpBypass()), 
        		"§dIf enabled, OP and * perms will have admin and bypass access.", "§c-- It's recommended to keep false for security.", "§6Left Click to toggle §cTrue§6/§aFalse."));
        // Commands
        	//status
        setStatusGuiItem(); // Slot 27 for the status
        inv.setItem(28,createGuiItem(WLStorage.getGuiWlConfigList(), "§bShow Config Access List", "§9Get the Config Access List in chat.", cmdClick));
        inv.setItem(29,createGuiItem(WLStorage.getGuiAdd(), ChatColor.DARK_GREEN + "Add player to Config Access List", "§dNeeds Config Access Enabled to ", "§d-- allow added players to connect.", cmdClick));
        inv.setItem(30,createGuiItem(WLStorage.getGuiRemove(), ChatColor.DARK_RED + "Remove player from Config Access List", cmdClick));
        inv.setItem(32,createGuiItem(WLStorage.getGuiAddAllOnline(), ChatColor.DARK_GREEN + "Add everyone to Config Access", "§dAdd everyone currently connected.", cmdClickConf));
        inv.setItem(33,createGuiItem(WLStorage.getGuiRemoveAll(), ChatColor.DARK_RED + "Reset Config Access List", "Remove all names from the Config Access List", cmdClickConf));
        inv.setItem(34,createGuiItem(WLStorage.getGuiSendPlayers(), "§cSend Players to lobby", ChatColor.DARK_RED + "  Only if WhiteList is on,", ChatColor.DARK_RED + "  only sends non-whitelisted players.", cmdClickConf));
        inv.setItem(35,createGuiItem(WLStorage.getGuiRestartServer(), "§cRestart the server", ChatColor.DARK_RED + "This will send players back to lobby first.", cmdClickConf));
    }
    
	public static Material getGUITFMat(Boolean trueFalse) {
		if (trueFalse) return WLStorage.getGuiTrue();
		else return WLStorage.getGuiFalse();
	}

	public static String getTFColor(Boolean trueFalse) {
		String TF = "error";
		if (trueFalse) TF = "§aTrue";
		if (!trueFalse) TF = "§cFalse";
		return TF;
	}

	public static String getInvertedTFColor(Boolean trueFalse) {
		String TF = "error";
		if (trueFalse) TF = "§cTrue";
		if (!trueFalse) TF = "§aFalse";
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

    public static Boolean invertBoolean(Boolean b) {
    	if (b == true) return false;
    	else return true;
    }
    
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
    	if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains(title)) {
			if (e.getCurrentItem() == null) {
				e.getWhoClicked().closeInventory();
				return;
			}
    		e.setCancelled(true);
    		TextComponent msg = new TextComponent("Click me to be given the command. Hover to see current.");
    		msg.setColor(ChatColor.GOLD);

			String name = e.getCurrentItem().getItemMeta().getDisplayName();
    		if (e.getCurrentItem() != null) {
    			if (e.getSlot() < slots) { // Main AWL Inv check
	    			// Boolean
	    			if (name.contains("Whitelist Enabled")) {
	    				if (e.isLeftClick()) {
	        				WLStorage.setWhitelist(invertBoolean(WLStorage.isWhitelisting()));
	        				inv.setItem(0,createGuiItem(getGUITFMat(WLStorage.isWhitelisting()), "§6§lWhitelist Enabled", "§eEnabled: " + getTFColor(WLStorage.isWhitelisting()), tfClick, tfClick2, perm + "operator"));
	        		        setStatusGuiItem();
	        				// Redo Filler
	        		    	for (Integer i = 0 ; i < inv.getSize() ; i++) {
	        		    		if (inv.getItem(i).getItemMeta().getDisplayName().contains("AWL on")) {
	        		    			inv.setItem(i,createGuiItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.GOLD + "AWL on: " + getTFColor(WLStorage.isWhitelisting())));
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
	    				WLStorage.setConfigAccess(invertBoolean(WLStorage.isConfigAccess()));
	    				inv.setItem(1,createGuiItem(getGUITFMat(WLStorage.isConfigAccess()), "§eConfig Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isConfigAccess()), "§dThis will need to be enabled to allow added players-"," to join regardless of permissions.", tfClick));
	    		        setStatusGuiItem();
	    			}
	    			if (name.contains("ProjectTeam Access Enabled"))  {
	    				if (e.isLeftClick()) {
	        				WLStorage.setProjectTeamAccess(invertBoolean(WLStorage.isProjectTeamAccess()));
	        				inv.setItem(2,createGuiItem(getGUITFMat(WLStorage.isProjectTeamAccess()), "§eProjectTeam Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isProjectTeamAccess()), tfClick, tfClick2, perm + "ProjectTeam"));
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
	        				WLStorage.setStaffAccess(invertBoolean(WLStorage.isStaffAccess()));
	        				inv.setItem(3,createGuiItem(getGUITFMat(WLStorage.isStaffAccess()), "§eStaff Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isStaffAccess()), tfClick, tfClick2, perm + "Staff"));
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
	        				WLStorage.setTesterAccess(invertBoolean(WLStorage.isTesterAccess()));
	        				inv.setItem(4,createGuiItem(getGUITFMat(WLStorage.isTesterAccess()), "§eTester Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isTesterAccess()), tfClick, tfClick2, perm + "Tester"));
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
	        				WLStorage.setAlternateAccess(invertBoolean(WLStorage.isAlternateAccess()));
	        				inv.setItem(5,createGuiItem(getGUITFMat(WLStorage.isAlternateAccess()), "§eAlternate Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isAlternateAccess()), tfClick, tfClick2, perm + "Alternate"));
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
	        				WLStorage.setOtherAccess(invertBoolean(WLStorage.isOtherAccess()));
	        				inv.setItem(6,createGuiItem(getGUITFMat(WLStorage.isOtherAccess()), "§eOther Access Enabled", "§eEnabled: " + getTFColor(WLStorage.isOtherAccess()), tfClick, tfClick2, perm + "Other"));
	        		        setStatusGuiItem();
	    				}
	    		    	if (e.isRightClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Permission: ").color(ChatColor.GOLD).append("advancedwhitelist.bypass.Other").color(ChatColor.BLUE).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/<permission command> advancedwhitelist.bypass.Other"));
	    	    			p.spigot().sendMessage(msg);
	    		    	}
	    			}
	    			if (name.contains("Server Cooldown Enabled")) {
	    				WLStorage.setServerCooldown(invertBoolean(WLStorage.isServerCooldown()));
	    		        inv.setItem(7,createGuiItem(getGUITFMat(WLStorage.isServerCooldown()), "§eServer Cooldown Enabled", "§eEnabled: " + getTFColor(WLStorage.isServerCooldown()), tfClick));
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
								msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(numToString(WLStorage.getKickDelayPerPlayer())).create()));
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
								msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(numToString(WLStorage.getKickDelayPerPlayer())).create()));
		    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl kickdelay <seconds>"));
		    	    			p.spigot().sendMessage(msg);
							}
						}
	    		        inv.setItem(19,createGuiItem(WLStorage.getGuiDelayStartKicks(), "§eDelay Before Starting Kicks", "§eDuration: §b" + WLStorage.getDelayBeforeStartingKicks(), intClick1, intClick2, intClick3));
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
								msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(numToString(WLStorage.getKickDelayPerPlayer())).create()));
		    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl kickdelay <seconds>"));
		    	    			p.spigot().sendMessage(msg);
							}
						}
	    		        inv.setItem(20,createGuiItem(WLStorage.getGuiKickDelayPlayer(), "§eKick Delay Per Player", "§eDuration: §b" + WLStorage.getKickDelayPerPlayer(), intClick1, intClick2, intClick3));
	    		        setStatusGuiItem();
	    			}
	    			if (name.contains("OP and * Access Enabled")) {
	    				if (e.isLeftClick()) {
	        				WLStorage.setOpBypass(invertBoolean(WLStorage.isOpBypass()));
	        				inv.setItem(26,createGuiItem(getGUITFMat(WLStorage.isOpBypass()), "§eOP and * Access Enabled", "§eEnabled: " + getInvertedTFColor(WLStorage.isOpBypass()), 
	        		        		"§dIf enabled, OP and * perms will have admin and bypass access.", "§c-- It's recommended to keep false for security.", "§6Left Click to toggle §cTrue§6/§aFalse."));
	        		        setStatusGuiItem();
	    				}
	    			}
	    		    	/*if (e.isRightClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Permission: ").color(ChatColor.GOLD).append("advancedwhitelist.bypass.Tester").color(ChatColor.BLUE).create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/<permission command> advancedwhitelist.bypass.Tester"));
	    	    			p.spigot().sendMessage(msg);
	    		    	}*/
	    			
	    			// Commands
	    			if (name.contains("Help Menu")) {
	    				if (e.isLeftClick()) WLCmd.getHelp(p);
	    				if (e.isRightClick()) {
	    				}
	    			}
	    			if (name.contains("Whitelist Status")) {
	    				if (e.isLeftClick()) {
	    					if (!e.isShiftClick()) WLCmd.getStatus(p);
	    					if (e.isShiftClick()) {
	    						WLStorage.reload();
	    						openInventory(p);
	    					}
	    				}
	    				if (e.isRightClick()) {
	    					if (!e.isShiftClick()) WLCmd.getHelp(p);
	    					if (e.isShiftClick()) {
				    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/awl help <Page number>").create()));
				    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl help <1-9>"));
				    			p.spigot().sendMessage(msg);
	    					}
	    				}
	    			}
	    			if (name.contains("Add player to Config Access List")) {
		    			//if (e.isLeftClick()) {anvilInv(e.getWhoClicked(), "Add Player");}
	    				if (e.isLeftClick()) {
	    					msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/awl add <name>").create()));
			    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl add <name>"));
			    			p.spigot().sendMessage(msg);
	    				}
	    			}
	    			if (name.contains("Remove player from Config Access List")) {
		    			//if (e.isLeftClick()) {anvilInv(e.getWhoClicked(), "Remove Player");}
	    				if (e.isLeftClick()) {
	    	    			msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/awl remove <name>").create()));
	    	    			msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/awl remove <name>"));
	    	    			p.spigot().sendMessage(msg);
	    				}
	    			}
	    			if (name.contains("Show Config Access List")) WLCmd.listPlayers(p);
	    			if (name.contains("Add everyone to Config Access")) if (e.isRightClick()) if (e.isShiftClick()) WLCmd.addAllPlayers();
	    			// Caution commands
	    			if (name.contains("Reset Config Access List")) if (e.isRightClick()) if (e.isShiftClick()) {
	    				WLStorage.clearWhiteLists();
	    				Utility.sendMsg(p, "All player names have been removed from Config Access List.");
	    			}
	    			if (name.contains("Send Players to lobby")) if (e.isRightClick()) if (e.isShiftClick()) WLCmd.sendPlayers(p);
	    			if (name.contains("Restart the server")) if (e.isRightClick()) if (e.isShiftClick()) WLCmd.restartServer(p);
    			} // End Slot/Main Inv Check
    			// Anvil Inv start
    			if (e.getClickedInventory().getType().equals(InventoryType.ANVIL) && 
    					name.contains("Anvil")) {
        			name = "Anvil" + name;
        			String[] names = name.split(" "); // Also used for numbers. Can't use for messages as it only allows 35 characters.
    				if (e.getSlotType().equals(SlotType.RESULT)) {
    					if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains("Add Player")) {
    						WLCmd.addPlayer(p, names);
    					}
						if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains("Remove Player")) {
							WLCmd.removePlayer(p, names);
						}
						if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains("Server Cooldown Duration")) {
							WLStorage.setServerCooldownTime(parseLongFromString(p, name));
						}
						if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains("Delay Before Starting Kicks")) {
							WLStorage.setDelayBeforeStartingKicks(parseLongFromString(p, name));
						}
						if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).contains("Kick Delay Per Player")) {
							WLStorage.setKickDelayPerPlayer(parseLongFromString(p, name));
						}
    				} // End Anvil Result check
    			} // End Anvil Inv Check
    		} // 
    		
    	} // End AWL Inv Check
        return;
    }
    
    public static void setStatusGuiItem() {
    	inv.setItem(27,createGuiItem(WLStorage.getGuiStatus(), 
        		ChatColor.YELLOW + "Whitelist Status", 
        		"§dLeft-Click to get WL Status in chat",
        		"§dShift Left-Click to reload from config",
        		"§dRight-Click to get Help Menu in chat",
        		"§dShift-Right-Click to get Help command suggetion",
        		"§6Whitelist Enabled: " + getTFColor(WLStorage.isWhitelisting()), 
        		"§6Config Access Enabled: " + getTFColor(WLStorage.isConfigAccess()), 
        		"§6ProjectTeam Access Enabled: " + getTFColor(WLStorage.isProjectTeamAccess()), 
        		"§6Staff Access Enabled: " + getTFColor(WLStorage.isStaffAccess()), 
        		"§6Tester Access Enabled: " + getTFColor(WLStorage.isTesterAccess()), 
        		"§6Alternate Acccess Enabled: " + getTFColor(WLStorage.isAlternateAccess()),
        		"§6Other Access Enabled: " + getTFColor(WLStorage.isOtherAccess()),
        		"§6Server Cooldown Enabled: " + getTFColor(WLStorage.isServerCooldown()),
        		"§6Server Cooldown time: " + getTFColor(WLStorage.isWhitelisting()),
        		"§6Delay Before Starting Kicks: " + getTFColor(WLStorage.isWhitelisting()),
        		"§6Kick Delay Per Player: " + getTFColor(WLStorage.isWhitelisting()),
        		"§6OP bypass: " + getInvertedTFColor(WLStorage.isOpBypass())
        		));
    }
    
    public String numToString(Long in) {
    	return in.toString();
    }
    
    public static String numToString(int in) {
    	Long l = (long) in;
    	String s = l.toString();
    	return s;
    }
    
 // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
          e.setCancelled(true);
        }
    }
    
	public static long parseLongFromString(Player p, String arg) {
		long num = 5;
		try {
			num = Long.getLong(arg);
		} catch(Exception e) {
			
			Utility.sendConsole("String to long number conversion failed. Setting value to 5");
		}
		return num;
	}
    
}
