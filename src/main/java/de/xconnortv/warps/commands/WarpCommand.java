package de.xconnortv.warps.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xconnortv.warps.Warps;
import de.xconnortv.warps.configs.ConfigAccessor;

public class WarpCommand implements CommandExecutor {
	private Warps plugin = Warps.getInstance();
	private ConfigAccessor warpConfig = Warps.getWarpConfig();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (player.hasPermission(Warps.getConfigUtils().getPermission("player"))) {

				if (args.length == 0) {
					printHelp(player);
					return true;
				}
				String sub = args[0];
				if (sub.equalsIgnoreCase("create")) {
					if (args.length != 2) {
						printHelp(player);
						return true;
					}
					String name = args[1];
					if (name.length() < 2 || name.length() > 16) {
						player.sendMessage(plugin.getPrefix() + plugin.getMessage("WarpNameToLoS"));
						return true;
					}

					List<String> blocked = plugin.getConfig().contains("blacklistedWorlds")
							? plugin.getConfig().getStringList("blacklistedWorlds")
							: new ArrayList<>();

					for (String b : blocked) {
						if (player.getWorld().getName().equals(b)) {
							player.sendMessage(plugin.getPrefix() + plugin.getMessage("BlackListedWorld"));
							return true;
						}
					}
					if (addWarp(player, name)) {
						player.sendMessage(
								plugin.getPrefix() + plugin.getMessage("WarpSuccess").replace("%WARP%", name));
					} else {
						player.sendMessage(plugin.getPrefix() + plugin.getMessage("WarpError").replace("%WARP%", name));
					}
					return true;
				} else if (sub.equalsIgnoreCase("redefine")) {
					if (args.length != 2) {
						printHelp(player);
						return true;
					}
					String name = args[1];
					if (name.length() < 2 || name.length() > 16) {
						player.sendMessage(plugin.getPrefix() + plugin.getMessage("WarpNameToLoS"));
						return true;
					}
					if (redefineWarp(player, name)) {
						player.sendMessage(
								plugin.getPrefix() + plugin.getMessage("WarpSuccess").replace("%WARP%", name));
					} else {
						player.sendMessage(plugin.getPrefix() + plugin.getMessage("WarpError").replace("%WARP%", name));
					}

					return true;
				} else if (sub.equalsIgnoreCase("remove")) {
					if (args.length != 2) {
						printHelp(player);
						return true;
					}
					String name = args[1];
					if (name.length() < 2 || name.length() > 16) {
						player.sendMessage(plugin.getPrefix() + plugin.getMessage("WarpNameToLoS"));
						return true;
					}
					if (removeWarp(player, name)) {
						player.sendMessage(
								plugin.getPrefix() + plugin.getMessage("WarpDelete").replace("%WARP%", name));
					} else {
						player.sendMessage(
								plugin.getPrefix() + plugin.getMessage("WarpDeleteError").replace("%WARP%", name));
					}

					return true;
				} else {
					printHelp(player);
					return true;
				}

			} else {
				player.sendMessage(plugin.getPrefix() + plugin.getMessage("NoPerms"));
			}
		}
		return false;
	}

	public void printHelp(Player player) {

	}

	public boolean addWarp(Player player, String name) {

		if (warpConfig.getConfig().contains(player.getName() + ".warps." + name)) {
			return false;
		} else {
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".world", player.getWorld().getName());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".x", player.getLocation().getX());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".y", player.getLocation().getY());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".z", player.getLocation().getZ());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".yaw", player.getLocation().getYaw());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".pitch", player.getLocation().getPitch());
			warpConfig.saveConfig();
			warpConfig.reloadConfig();
			return true;
		}
	}

	public boolean removeWarp(Player player, String name) {

		if (!warpConfig.getConfig().contains(player.getName() + ".warps." + name)) {
			return false;
		} else {
			warpConfig.getConfig().set(player.getName() + ".warps." + name, null);
			warpConfig.saveConfig();
			warpConfig.reloadConfig();
			return true;
		}
	}

	public boolean redefineWarp(Player player, String name) {

		if (!warpConfig.getConfig().contains(player.getName() + ".warps." + name)) {
			return false;
		} else {
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".world", player.getWorld().getName());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".x", player.getLocation().getX());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".y", player.getLocation().getY());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".z", player.getLocation().getZ());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".yaw", player.getLocation().getYaw());
			warpConfig.getConfig().set(player.getName() + ".warps." + name + ".pitch", player.getLocation().getPitch());
			warpConfig.saveConfig();
			warpConfig.reloadConfig();
			return true;
		}
	}
}
