package de.xconnortv.warps.guis;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.xconnortv.warps.Warps;
import de.xconnortv.warps.configs.ConfigAccessor;
import de.xconnortv.warps.utils.InventoryFill;
import de.xconnortv.warps.utils.ItemSkulls;

public class WarpGui implements Listener {
	private Warps plugin = Warps.getInstance();
	private int size;
	private String name;
	private Inventory inventory;
	private ConfigAccessor warpConfig = Warps.getWarpConfig();
	private Player player;

	public WarpGui(String name, int size) {
		this.size = size;
		this.name = name;
	}

	public void open() {
		fillInventory();
		this.player.openInventory(inventory);

	}

	public void setPlayer(Player player) {
		this.player = player;
		inventory = Bukkit.createInventory(player, size, name);
	}

	private void fillInventory() {
		inventory.clear();
		InventoryFill iF = new InventoryFill(inventory);
		iF.fillSidesWithItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData()));
		List<Integer> freeSlots = iF.getNonSideSlots();

		if (warpConfig.getConfig().contains(player.getName() + ".warps")) {
			ConfigurationSection cS = warpConfig.getConfig().getConfigurationSection(player.getName() + ".warps");
			int warps = cS.getKeys(true).size();
			if (warps > 0) {
				int index = 0;
				for (String s : cS.getKeys(false)) {
					String name = s;
					World world = Bukkit
							.getWorld(warpConfig.getConfig().getString(player.getName() + ".warps." + name + ".world"));
					double x = warpConfig.getConfig().getDouble(player.getName() + ".warps." + name + ".x");
					double y = warpConfig.getConfig().getDouble(player.getName() + ".warps." + name + ".y");
					double z = warpConfig.getConfig().getDouble(player.getName() + ".warps." + name + ".z");
					float yaw = (float) warpConfig.getConfig().getDouble(player.getName() + ".warps." + name + ".yaw");
					float pitch = (float) warpConfig.getConfig()
							.getDouble(player.getName() + ".warps." + name + "pitch");
					Location pos = new Location(world, x, y, z, yaw, pitch);

					ArrayList<String> lore = new ArrayList<>();
					lore.add("§e");
					lore.add("§7Welt × §a" + world.getName());
					lore.add("§7Pos X × §a" + pos.getBlockX());
					lore.add("§7Pos Y × §a" + pos.getBlockY());
					lore.add("§7Pos Z × §a" + pos.getBlockZ());
					setWarpItem(freeSlots.get(index), inventory, "§a§l" + name,
							"438cf3f8e54afc3b3f91d20a49f324dca1486007fe545399055524c17941f4dc", lore);
					index++;
				}
			}
		}

	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		if (event.getInventory().getName().contentEquals(this.name)) {
			if (clicked.getType() != Material.STAINED_GLASS_PANE) {
				String name = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
				if (warpConfig.getConfig().contains(player.getName() + ".warps." + name)) {
					World world = Bukkit
							.getWorld(warpConfig.getConfig().getString(player.getName() + ".warps." + name + ".world"));
					double x = warpConfig.getConfig().getDouble(player.getName() + ".warps." + name + ".x");
					double y = warpConfig.getConfig().getDouble(player.getName() + ".warps." + name + ".y");
					double z = warpConfig.getConfig().getDouble(player.getName() + ".warps." + name + ".z");
					float yaw = (float) warpConfig.getConfig().getDouble(player.getName() + ".warps." + name + ".yaw");
					float pitch = (float) warpConfig.getConfig()
							.getDouble(player.getName() + ".warps." + name + "pitch");
					Location pos = new Location(world, x, y, z, yaw, pitch);
					player.teleport(pos);
					player.sendMessage(plugin.getPrefix() + plugin.getMessage("Teleport"));
					playTo(player, pos, Sound.ENDERMAN_TELEPORT, 1, 1);

				}
			}

			event.setCancelled(true);
		}
	}

	public void setWarpItem(int slot, Inventory inv, String name, String texture, ArrayList<String> Lore) {
		ItemStack item = ItemSkulls.getSkull("http://textures.minecraft.net/texture/" + texture);
		SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
		itemMeta.setDisplayName(name);
		if (Lore != null) {
			ArrayList<String> lore = Lore;
			itemMeta.setLore(lore);
		}
		item.setItemMeta(itemMeta);
		item.setAmount(1);
		inv.setItem(slot, item);

	}

	public void playTo(Player player, Location location, Sound sound, float volume, float pitch) {
		player.playSound(location, sound, volume, pitch);
	}
}