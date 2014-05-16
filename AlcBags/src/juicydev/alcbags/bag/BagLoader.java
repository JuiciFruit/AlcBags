package juicydev.alcbags.bag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import juicydev.alcbags.AlcBags;
import juicydev.alcbags.util.BagUtil;
import juicydev.jcore.utils.item.ItemGlow;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * @author JuicyDev
 */
public class BagLoader {

	private static BagLoader instance = new BagLoader();

	public static BagLoader getInstance() {
		return instance;
	}

	public enum DyeColorRef {
		/**
		 * Represents white dye.
		 */
		WHITE("white", DyeColor.WHITE, "White", ChatColor.WHITE),
		/**
		 * Represents orange dye.
		 */
		ORANGE("orange", DyeColor.ORANGE, "Orange", ChatColor.GOLD),
		/**
		 * Represents magenta dye.
		 */
		MAGENTA("magenta", DyeColor.MAGENTA, "Magenta", ChatColor.LIGHT_PURPLE),
		/**
		 * Represents light blue dye.
		 */
		LIGHT_BLUE("light_blue", DyeColor.LIGHT_BLUE, "Light Blue",
				ChatColor.BLUE),
		/**
		 * Represents yellow dye.
		 */
		YELLOW("yellow", DyeColor.YELLOW, "Yellow", ChatColor.YELLOW),
		/**
		 * Represents lime dye.
		 */
		LIME("lime", DyeColor.LIME, "Light Green", ChatColor.GREEN),
		/**
		 * Represents pink dye.
		 */
		PINK("pink", DyeColor.PINK, "Pink", ChatColor.LIGHT_PURPLE),
		/**
		 * Represents gray dye.
		 */
		GREY("grey", DyeColor.GRAY, "Grey", ChatColor.DARK_GRAY),
		/**
		 * Represents silver dye.
		 */
		LIGHT_GREY("light_grey", DyeColor.SILVER, "Light Grey", ChatColor.GRAY),
		/**
		 * Represents cyan dye.
		 */
		CYAN("cyan", DyeColor.CYAN, "Cyan", ChatColor.DARK_AQUA),
		/**
		 * Represents purple dye.
		 */
		PURPLE("purple", DyeColor.PURPLE, "Purple", ChatColor.DARK_PURPLE),
		/**
		 * Represents blue dye.
		 */
		BLUE("blue", DyeColor.BLUE, "Blue", ChatColor.DARK_BLUE),
		/**
		 * Represents brown dye.
		 */
		BROWN("brown", DyeColor.BROWN, "Brown", ChatColor.DARK_RED),
		/**
		 * Represents green dye.
		 */
		GREEN("green", DyeColor.GREEN, "Green", ChatColor.DARK_GREEN),
		/**
		 * Represents red dye.
		 */
		RED("red", DyeColor.RED, "Red", ChatColor.RED),
		/**
		 * Represents black dye.
		 */
		BLACK("black", DyeColor.BLACK, "Black", ChatColor.BLACK);

		private String str;
		private DyeColor colour;
		private String name;
		private ChatColor chat;

		DyeColorRef(String str, DyeColor colour, String name, ChatColor chat) {
			this.str = str;
			this.colour = colour;
			this.name = name;
			this.chat = chat;
		}

		public String toString() {
			return str;
		}

		public DyeColor getColour() {
			return colour;
		}

		public String getName() {
			return name;
		}

		public ChatColor getChatColor() {
			return chat;
		}

		public static DyeColorRef getByString(String str) {
			for (DyeColorRef dcr : values()) {
				if (dcr.toString().equalsIgnoreCase(str))
					return dcr;
				if (dcr.toString().replace("_", "").equalsIgnoreCase(str))
					return dcr;
				if (dcr.toString().replace("grey", "gray")
						.equalsIgnoreCase(str))
					return dcr;
			}
			return null;
		}

		public static DyeColorRef getByStringStrict(String str) {
			for (DyeColorRef dcr : values()) {
				if (dcr.toString().equals(str))
					return dcr;
			}
			return null;
		}

		public static DyeColorRef getByColour(DyeColor colour) {
			for (DyeColorRef dcr : values()) {
				if (dcr.getColour().equals(colour))
					return dcr;
			}
			return null;
		}
	}

	private File bagsDir = new File(AlcBags.getInstance().getDataFolder(),
			"bags");

	private HashMap<UUID, HashMap<DyeColorRef, BagInventory>> bagMap = new HashMap<UUID, HashMap<DyeColorRef, BagInventory>>();

	public void load() {
		bagsDir.mkdirs();
		for (File f : bagsDir.listFiles()) {
			if (f.getName().endsWith(".yml")) {
				UUID uuid = UUID.fromString(f.getName().substring(0,
						f.getName().length() - ".yml".length()));
				FileConfiguration config = YamlConfiguration
						.loadConfiguration(f);
				HashMap<DyeColorRef, BagInventory> map = new HashMap<DyeColorRef, BagInventory>();
				for (DyeColorRef colour : DyeColorRef.values()) {
					String str = colour.toString();
					ConfigurationSection cs = config
							.getConfigurationSection(str);
					if (cs == null)
						cs = config.createSection(str);
					BagInventory bookInv = new BagInventory(uuid,
							DyeColorRef.getByStringStrict(str));
					for (int i = 0; i < bookInv.getInventory().getSize(); i++) {
						ItemStack stack = cs.getItemStack(String.valueOf(i),
								new ItemStack(Material.AIR));
						if (stack != null
								&& !stack.getType().equals(Material.AIR)) {
							if (BagUtil.isBag(stack))
								bookInv.getInventory().setItem(i, ItemGlow.addGlow(stack));
							else
								bookInv.getInventory().setItem(i, stack);
						}
					}
					map.put(colour, bookInv);
				}
				bagMap.put(uuid, map);
			}
		}
	}

	public void save() {
		bagsDir.mkdirs();
		for (Entry<UUID, HashMap<DyeColorRef, BagInventory>> entry : bagMap
				.entrySet()) {
			File f = new File(bagsDir, entry.getKey().toString() + ".yml");
			FileConfiguration config = new YamlConfiguration();
			for (DyeColorRef c : DyeColorRef.values()) {
				ConfigurationSection cs = config.createSection(c.toString());
				for (int i = 0; i < entry.getValue().get(c).getInventory()
						.getSize(); i++) {
					cs.set(String.valueOf(i), entry.getValue().get(c)
							.getInventory().getItem(i));
				}
			}
			try {
				config.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public HashMap<UUID, HashMap<DyeColorRef, BagInventory>> getInventoryMap() {
		return bagMap;
	}

	public ArrayList<BagInventory> getBagList(UUID uuid) {
		ArrayList<BagInventory> list = new ArrayList<BagInventory>();
		for (Entry<DyeColorRef, BagInventory> entry : getInventoryMap().get(
				uuid).entrySet()) {
			BagInventory bookInv = getInventoryMap().get(uuid).get(
					entry.getKey());
			if (bookInv == null) {
				bookInv = new BagInventory(uuid, entry.getKey());
				getInventoryMap().get(uuid).put(entry.getKey(), bookInv);
			}
		}
		return list;
	}

	public BagInventory getBag(UUID uuid, DyeColorRef colour) {
		HashMap<DyeColorRef, BagInventory> map = getInventoryMap().get(uuid);
		if (map == null) {
			map = new HashMap<DyeColorRef, BagInventory>();
			for (DyeColorRef c : DyeColorRef.values()) {
				map.put(c, new BagInventory(uuid, c));
			}
			while (getInventoryMap().containsKey(uuid))
				getInventoryMap().remove(uuid);
			getInventoryMap().put(uuid, map);
			return map.get(colour);
		} else {
			BagInventory bookInv = map.get(colour);
			if (bookInv == null) {
				bookInv = new BagInventory(uuid, colour);
				map.put(colour, bookInv);
				while (getInventoryMap().containsKey(uuid))
					getInventoryMap().remove(uuid);
				getInventoryMap().put(uuid, map);
			}
			return bookInv;
		}
	}
}
