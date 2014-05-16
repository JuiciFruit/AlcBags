package juicydev.alcbags.util;

import juicydev.alcbags.bag.AlcBag;
import juicydev.alcbags.bag.BagLoader.DyeColorRef;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author JuicyDev
 */
public class BagUtil {

	private static String bagCode = ChatColor.AQUA.toString()
			+ ChatColor.GOLD.toString() + ChatColor.YELLOW.toString()
			+ ChatColor.BLACK.toString() + ChatColor.RESET.toString();
	private static String bagInvCode = ChatColor.AQUA.toString()
			+ ChatColor.GOLD.toString() + ChatColor.RESET.toString();

	private static String bagNameFormat = bagCode + "%s Alchemy Bag";
	private static String bagInvNameFormat = bagInvCode + "%s Alchemy Bag";
	public static Material bagMaterial = Material.INK_SACK;

	public static String getBagCode() {
		return bagCode;
	}

	public static String getBagFormat() {
		return bagNameFormat;
	}

	public static String getBagName(DyeColorRef colour) {
		return bagNameFormat.replace("%s",
				colour.getChatColor() + colour.getName());
	}
	
	public static String getBagInvName(DyeColorRef colour) {
		return bagInvNameFormat.replace("%s",
				colour.getChatColor() + colour.getName());
	}

	public static AlcBag getBagItem(DyeColorRef colour) {
		return new AlcBag(colour);
	}

	public static AlcBag getBagItem(ItemStack stack) {
		return new AlcBag(stack);
	}

	public static boolean isBag(ItemStack stack) {
		if (stack == null || stack.getType().equals(Material.AIR))
			return false;
		if (stack instanceof AlcBag)
			return true;
		if (stack.getType().equals(bagMaterial)) {
			if (stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()) {
				if (stack.getItemMeta().getDisplayName().startsWith(bagCode))
					return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static DyeColorRef getColour(ItemStack stack) {
		if (!isBag(stack))
			return null;
		DyeColor dc = DyeColor.getByDyeData(stack.getData().getData());
		return DyeColorRef.getByColour(dc);
	}
}
