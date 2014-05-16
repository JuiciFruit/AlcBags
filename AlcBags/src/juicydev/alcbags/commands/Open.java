package juicydev.alcbags.commands;

import java.util.UUID;

import juicydev.alcbags.bag.BagLoader;
import juicydev.alcbags.bag.BagLoader.DyeColorRef;
import juicydev.alcbags.util.AlcBagsMessageManager;
import juicydev.alcbags.util.Perms;
import juicydev.jcore.JCore;
import juicydev.jcore.utils.MessageManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Open extends SubCommand {

	private MessageManager mm = AlcBagsMessageManager.getInstance();

	public void onCommand(CommandSender sender, String[] args) {
		if (!Perms.OPEN.canUse(sender))
			return;
		if (!(args.length == 1 || args.length == 2)) {
			mm.errSender(sender, "Usage: /magicka open <colour> [player]");
			return;
		}
		if (!(sender instanceof Player)) {
			mm.errSender(sender, "Only players can use this command");
			return;
		}
		Player player = (Player) sender;
		if (args.length == 1) {
			DyeColorRef c = DyeColorRef.getByString(args[0]);
			if (c == null) {
				mm.errSender(sender, "Could not find bag with colour \""
						+ args[0] + "\".");
				mm.errSender(sender,
						"Use \"/alcbags list\" for a list of colours.");
				return;
			}

			mm.infoSender(player, "Opening \"" + c.getName()
					+ "\" alchemy bag.");
			player.openInventory(BagLoader.getInstance()
					.getBag(player.getUniqueId(), c).getInventory());
		} else if (args.length == 2) {
			try {
				Player target = JCore.getInstance().getPlayerLoader()
						.loadPlayer(args[1]);
				if (target == null) {
					mm.errSender(player, "Could not find player \"" + args[1] + "\"");
					return;
				}
				UUID uuid = target.getUniqueId();

				DyeColorRef c = DyeColorRef.getByString(args[0]);
				if (c == null) {
					mm.errSender(sender, "Could not find bag with colour \""
							+ args[0] + "\".");
					mm.errSender(sender,
							"Use \"/alcbags list\" for a list of colours.");
					return;
				}

				mm.infoSender(player, "Opening \"" + c.getName()
						+ "\" alchemy bag for \"" + target.getName() + "\".");
				player.openInventory(BagLoader.getInstance().getBag(uuid, c)
						.getInventory());
			} catch (Exception e) {
				mm.errSender(player, "An error has occured.");
				mm.error(e);
			}
		}
	}

	public String name() {
		return "open";
	}

	public String[] aliases() {
		return new String[] { "openalc" };
	}

	public String[] usage() {
		return new String[] { "<colour> [player]" };
	}
}
