package juicydev.alcbags.util;

import juicydev.jcore.utils.item.ItemGlow;

import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author JuicyDev
 */
public class GlowFix extends BukkitRunnable {

	private InventoryView view;

	public GlowFix(InventoryView view) {
		this.view = view;
		run();
	}

	public void run() {
		for (ItemStack is : view.getTopInventory()) {
			if (BagUtil.isBag(is)) {
				is = ItemGlow.addGlow(is);
				AlcBagsMessageManager.getInstance().debug(
						"Fixed the enchantment glow on \""
								+ is.getType().toString() + "\" in player \""
								+ view.getPlayer().getName() + "\" inventory");
			}
		}
		for (ItemStack is : view.getBottomInventory()) {
			if (BagUtil.isBag(is)) {
				is = ItemGlow.addGlow(is);
				AlcBagsMessageManager.getInstance().debug(
						"Fixed the enchantment glow on \""
								+ is.getType().toString() + "\" in player \""
								+ view.getPlayer().getName() + "\" inventory");
			}
		}
	}
}
