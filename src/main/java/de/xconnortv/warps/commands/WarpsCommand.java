package de.xconnortv.warps.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xconnortv.warps.Warps;
import de.xconnortv.warps.configs.ConfigAccessor;

public class WarpsCommand implements CommandExecutor {
	private Warps plugin = Warps.getInstance();
	private ConfigAccessor warpConfig = Warps.getWarpConfig();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (player.hasPermission(Warps.getConfigUtils().getPermission("player"))) {
				plugin.getWarpGUI().setPlayer(player);
				plugin.getWarpGUI().open();

			} else {
				player.sendMessage(plugin.getPrefix() + plugin.getMessage("NoPerms"));
			}
		}
		return false;
	}
}
