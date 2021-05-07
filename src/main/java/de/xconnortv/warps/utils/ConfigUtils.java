package de.xconnortv.warps.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtils {
	FileConfiguration config;

	public ConfigUtils(FileConfiguration config) {
		this.config = config;
	}

	public String getPermission(String perm) {
		return config.getString("permissions." + perm.toLowerCase() + ".permission");
	}

}
