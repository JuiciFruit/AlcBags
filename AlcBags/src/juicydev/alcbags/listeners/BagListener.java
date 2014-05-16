package juicydev.alcbags.listeners;

import juicydev.alcbags.AlcBags;
import juicydev.alcbags.bag.AlcBag;
import juicydev.alcbags.bag.BagInventory;
import juicydev.alcbags.bag.BagLoader;
import juicydev.alcbags.bag.BagLoader.DyeColorRef;
import juicydev.alcbags.util.AlcBagsMessageManager;
import juicydev.alcbags.util.AnvilFix;
import juicydev.alcbags.util.BagUtil;
import juicydev.alcbags.util.GlowFix;
import juicydev.alcbags.util.Perms;
import juicydev.jcore.utils.MessageManager;
import juicydev.jcore.utils.item.ItemGlow;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author JuicyDev
 */
public class BagListener implements Listener {

	private MessageManager mm = AlcBagsMessageManager.getInstance();

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!BagUtil.isBag(event.getItem()))
			return;
		Player player = event.getPlayer();

		if (!Perms.USE.canUseSilent(player)) {
			mm.errSender(player, "You do not have permission to use alcbags.");
			return;
		}
		if (player.hasPermission("-" + AlcBags.getInstance().getName()
				+ ".use." + player.getWorld().getName().toLowerCase())) {
			mm.errSender(player,
					"You do not have permission to use alcbags in this world.");
			return;
		}
		if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event
				.getAction().equals(Action.RIGHT_CLICK_AIR)))
			return;
		AlcBag bag = BagUtil.getBagItem(event.getItem());
		DyeColorRef colour = BagUtil.getColour(bag);
		if (colour == null)
			return;
		BagInventory bagInv = BagLoader.getInstance().getBag(
				player.getUniqueId(), colour);
		player.openInventory(bagInv.getInventory());
		mm.debug("Opening \"" + colour.getName() + "\" alcbag for player \""
				+ player.getName() + "\"");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBagRename(InventoryClickEvent event) {
		if (!(event.getInventory() instanceof AnvilInventory))
			return;

		if (BagUtil.isBag(event.getView().getItem(0))) {
			event.getView().setItem(2, new ItemStack(Material.AIR));
		}

		if (event.getRawSlot() == 0) {
			if (event.getCursor() == null)
				return;
			if (BagUtil.isBag(event.getCursor()))
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						AlcBags.getInstance(), new AnvilFix(event.getView()));
		}

		if (event.getRawSlot() == 2) {
			if (BagUtil.isBag(event.getView().getItem(0))) {
				event.setCancelled(true);
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						AlcBags.getInstance(), new AnvilFix(event.getView()));
			}
		}

		if (event.isShiftClick()) {
			if (BagUtil.isBag(event.getCurrentItem())) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						AlcBags.getInstance(), new AnvilFix(event.getView()));
			}
		}
	}

	@EventHandler
	public void onBagMoveCreative(InventoryCreativeEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(AlcBags.getInstance(),
				new GlowFix(event.getView()));
	}

	@EventHandler
	public void onCraftBag(PrepareItemCraftEvent event) {
		if (event.getRecipe().getResult() == null)
			return;
		if (BagUtil.isBag(event.getRecipe().getResult())) {
			event.getInventory().setResult(
					ItemGlow.addGlow(event.getRecipe().getResult()));
			return;
		}
		for (ItemStack stack : event.getInventory().getMatrix()) {
			if (BagUtil.isBag(stack)) {
				event.getInventory().setResult(new ItemStack(Material.AIR));
				mm.debug("Player \"" + event.getView().getPlayer().getName()
						+ "\" attempted to craft with an alcbag");
				return;
			}
		}
	}
}
