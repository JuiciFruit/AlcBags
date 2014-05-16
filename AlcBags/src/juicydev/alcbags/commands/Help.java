package juicydev.alcbags.commands;

import juicydev.alcbags.util.AlcBagsMessageManager;
import juicydev.alcbags.util.Perms;
import juicydev.jcore.utils.MessageManager;

import org.bukkit.command.CommandSender;

/**
 * @author JuicyDev
 */
public class Help extends SubCommand {

	private MessageManager mm = AlcBagsMessageManager.getInstance();

	public void onCommand(CommandSender sender, String[] args) {
		if (!Perms.HELP.canUse(sender))
			return;
		if (args.length == 0) {
			CommandManager.getInstance().commandInfo(sender);
			return;
		}
		if (args.length == 1) {
			CommandManager.getInstance().commandInfo(sender, args[0]);
		} else {
			mm.errSender(sender, "Usage: /magicka help [command]");
		}
	}

	public String name() {
		return "help";
	}

	public String[] aliases() {
		return new String[] { "info" };
	}

	public String[] usage() {
		return new String[] { "[command]" };
	}
}
