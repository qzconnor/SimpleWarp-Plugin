package de.xconnortv.warps;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.xconnortv.warps.commands.WarpCommand;
import de.xconnortv.warps.commands.WarpsCommand;
import de.xconnortv.warps.configs.ConfigAccessor;
import de.xconnortv.warps.guis.WarpGui;
import de.xconnortv.warps.utils.ConfigUtils;

public class Warps extends JavaPlugin {
	private static Warps instance;
	private static ConfigUtils configUtils;
	private static ConfigAccessor warpConfig;

	private WarpGui warpGUI;

	@Override
	public void onEnable() {
		instance = this;

		configUtils = new ConfigUtils(getConfig());

		warpConfig = new ConfigAccessor(this, "userdata");

		warpConfig.reloadConfig();

		int s = (getConfig().contains("warpgui") && getConfig().contains("warpgui.size")
				? getConfig().getInt("warpgui.size")
				: 27);
		String n = (getConfig().contains("warpgui") && getConfig().contains("warpgui.name")
				? getConfig().getString("warpgui.name").replace("&", "§")
				: "$6$lWarps");

		warpGUI = new WarpGui(n, s);

		registerCommands();
		registerListeners();

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onLoad() {
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);

	}

	public void registerListeners() {

		Bukkit.getPluginManager().registerEvents(warpGUI, this);
	}

	public void registerCommands() {
		getCommand("warps").setExecutor(new WarpsCommand());
		getCommand("warp").setExecutor(new WarpCommand());
	}

	public WarpGui getWarpGUI() {
		return warpGUI;
	}

	public String getPrefix() {
		return getConfig().contains("PREFIX") ? getConfig().getString("PREFIX").replace("&", "§") : "§6Warps §7×";
	}

	public String getMessage(String message) {
		return getConfig().contains("messages") && getConfig().contains("messages." + message)
				? " " + getConfig().getString("messages." + message).replace("&", "§")
				: " §cMessage nicht gefunden!";
	}

	public static Warps getInstance() {
		return instance;
	}

	public static ConfigUtils getConfigUtils() {
		return configUtils;
	}

	public static ConfigAccessor getWarpConfig() {
		return warpConfig;
	}
}
