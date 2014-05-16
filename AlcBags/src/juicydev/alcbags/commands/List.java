package juicydev.alcbags.commands;

import java.util.ArrayList;

import juicydev.alcbags.bag.BagLoader.DyeColorRef;
import juicydev.alcbags.util.Perms;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author JuicyDev
 */
public class List extends SubCommand {

	public void onCommand(CommandSender sender, String[] args) {
		if (!Perms.LIST.canUse(sender))
			return;

		ArrayList<String> list = new ArrayList<String>();
		for (DyeColorRef c : DyeColorRef.values())
			list.add(ChatColor.YELLOW + c.getName() + ": " + ChatColor.GRAY
					+ "\"" + c.toString() + "\"");
		sender.sendMessage(ChatColor.BLUE + "=====[ Bag Colours ]=====");
		for (String str : list)
			sender.sendMessage(str);
		sender.sendMessage(ChatColor.BLUE + "======================");
	}

	public String name() {
		return "list";
	}

	public String[] aliases() {
		return new String[] { "colours" };
	}

	public String[] usage() {
		return new String[] { "" };
	}
}
