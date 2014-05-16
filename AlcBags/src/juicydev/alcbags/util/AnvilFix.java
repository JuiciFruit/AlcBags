package juicydev.alcbags.util;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class AnvilFix extends BukkitRunnable {

	private InventoryView view;

	public AnvilFix(InventoryView view) {
		this.view = view;
		run();
	}

	public void run() {
		view.setItem(2, new ItemStack(Material.AIR));
		AlcBagsMessageManager.getInstance().debug(
				"Player \"" + view.getPlayer().getName()
						+ "\" attempted to rename an alcbag");
	}
}
