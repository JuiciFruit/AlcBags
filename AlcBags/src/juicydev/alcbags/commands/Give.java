package juicydev.alcbags.commands;

import java.util.HashMap;

import juicydev.alcbags.bag.BagLoader.DyeColorRef;
import juicydev.alcbags.util.AlcBagsMessageManager;
import juicydev.alcbags.util.BagUtil;
import juicydev.alcbags.util.Perms;
import juicydev.jcore.utils.MessageManager;
import juicydev.jcore.utils.item.ItemGlow;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Give extends SubCommand {

	private MessageManager mm = AlcBagsMessageManager.getInstance();

	@SuppressWarnings("deprecation")
	public void onCommand(CommandSender sender, String[] args) {
		if (!Perms.GIVE.canUse(sender))
			return;
		if (!(args.length == 1 || args.length == 2)) {
			mm.errSender(sender, "Usage: /magicka give <colour> [player]");
			return;
		}
		Player player = null;
		boolean isSelf = false;
		if (args.length == 1) {
			if (!(sender instanceof Player)) {
				mm.errSender(sender, "Only players can use this command");
				return;
			}
			player = (Player) sender;
			isSelf = true;
		} else if (args.length == 2) {
			player = Bukkit.getPlayer(args[1]);
			if (player == null) {
				mm.errSender(sender, "Could not find player \"" + args[1]
						+ "\".");
				return;
			}
			isSelf = false;
		}
		DyeColorRef c = DyeColorRef.getByString(args[0]);
		if (c == null) {
			mm.errSender(sender, "Could not find bag with colour \"" + args[0]
					+ "\".");
			mm.errSender(sender, "Use \"/alcbags list\" for a list of colours.");
			return;
		}

		ItemStack bagItem = ItemGlow.addGlow(BagUtil.getBagItem(c));
		HashMap<Integer, ItemStack> map = player.getInventory()
				.addItem(bagItem);
		if (isSelf) {
			if (map.isEmpty())
				mm.infoSender(sender,
						"Successfully given an alchemy bag (" + c.getName()
								+ ").");
			else
				mm.infoSender(sender,
						"Could not give you an alchemy bag, are you out of inventory space?");
		} else {
			if (map.isEmpty())
				mm.infoSender(sender,
						"Successfully given an alchemy bag (" + c.getName()
								+ ") to \"" + player.getName() + "\".");
			else
				mm.infoSender(sender, "Could not give an alchemy bag to \""
						+ player.getName()
						+ "\", are they out of inventory space?");
		}
	}

	public String name() {
		return "give";
	}

	public String[] aliases() {
		return null;
	}

	public String[] usage() {
		return new String[] { "bag <colour> [player]" };
	}
}
