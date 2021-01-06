#Changelog

1.0.0:
- Initial version from Gober/Reinassance/Jycko

1.1.0:
- Taken over by Shadorunce
- Added options to allow access based on permissions
-- Added commands to allow in-game editing of new config options
-- Added command to check the status of all the whitelist options.
-- Operators Bypass perm will always have access to the server regardless of config settings.
- Removed Config save on disable.
- Added a set full whitelist/lockdown to allow only Operators Bypass, but it doesn't automatically kick everyone.

1.1.1-1.1.5:
- Not sure at the moment. Sorry!

1.1.6:
- Added Server/Plugin start to player join cooldown time.
-- Added an option to turn it off and to control how long the cooldown should be.
-- Operators Bypass perm can access the server at 1/3 of the set cooldown time.
-- ProjectTeam Bypass perm can access the server at 1/2 of the set cooldown time.

1.1.7:
- Added a send/kick command to restrict to only Whitelisted players.
-- Those that are not will be sent to lobby.
- Added ChangeLog.

1.1.8:
- Made kick only run if still connected to the server 1 second after attempted to send.
-- This should hopefully reduce errors and speed up kick process instead of 5 seconds per person, it'll only be 1 second per person.

1.1.9:
- Made the broadcast, kick, and send configurable.

1.1.10:
- Centralized Perm/Access checking into WLEvent.
- Separated Send/Kick method for future second use.
- Fixed Kick and Send messages to point to the stored config instead of set messages.
- Added Send/Kick summary, though may not be correct yet.

1.1.11:
- Added Restart option

1.1.12:
- Separated Messages from main status check to lessen the amount of text being sent to admin.
- Added reset list command to clear the Whitelist list

1.2.0:
- Added GUI
- Added an option to Add All connected players to the Config Access List

1.2.1:
- Removed OP access and "*" permission from having access to anything in AWL automatically for commands and bypass and require the specific permissions.
- Made OP access customizable in game with command or GUI.
- Added extra permissions that combine lower bypass.
- Customizable GUI items
- Cleaned Config file.
- Added close GUI when clicking outside inv

1.2.2:
- Added Convert from EasyWhiteList and Minecraft Whitelist
-- Added to right click for add gui item
- Added auto convert for old config version of AdvancedWhiteList

1.2.3:
- Added UUID command to get UUID from Name or Name from UUID.
- Added Check command to see what access a player may have.
-- Offline through vault not working yet
- Added Name GUI for current Whitelist and Currently Online
- Fixed so clicked items can only be in the inv slots.

1.2.4:
- Added UUID and Check commands into GUI options
- Fixed permission logins, previous added something I shouldn't have, but confirmed working now.

1.2.5:
- Removed send as a command to send all non-whitelisted players back to lobby/kick them
- Added send command to new feature to send a player to a specified Bungeecord server.

Future Plans not in any specific order:
- Add a summary, Put numbers instead of names for summary
- Get Header comments to save into file.
- Show empty inv for lower section. Without NMS, would need to save inv to file, clear it, give what I want them to have, and when done, give it back. Would only do this for using Anvil inv to run add/remove/etc commands to put a tag or something similar.
- Connect to Bungee to be able to change all servers from one location .. might need to do from Database connection.
- Notify permission that will send a message to those with permission when someone not whitelisted tries to join the server, and includes what the attempted joining player does have permissions for.
- Add permissions for different functions to add more customizable access, and add permission to be able to check access even though it doesn't change anything.

Not likely to happen:
- Add Anvil GUI to add names and type numbers for delay amounts.
-- Due to it requiring NMS at the moment as it's not Bukkit/Spigot implemented and it would need to be updated for each MC Version.


