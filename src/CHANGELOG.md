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

Future Plans:
- Add a summary, Put numbers instead of names for summary
- Added proper Config version check at start so that it will rename the old file and enter a new one with existing values for easy transfer
- Get Header comments to save into file.


Not likely to happen:
- Added Anvil GUI to add names and type numbers for delay amounts.
-- Due to it requiring NMS at the moment as it's not Bukkit/Spigot implemented and it would need to be updated for each MC Version.