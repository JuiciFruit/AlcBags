package juicydev.alcbags.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import juicydev.alcbags.AlcBags;
import juicydev.alcbags.bag.BagLoader.DyeColorRef;
import juicydev.alcbags.util.AlcBagsMessageManager;
import juicydev.jcore.utils.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;

/**
 * @author JuicyDev
 */
public class CommandManager implements CommandExecutor, TabCompleter {

	private static CommandManager instance = new CommandManager();

	public static CommandManager getInstance() {
		return instance;
	}

	private ArrayList<SubCommand> commands = new ArrayList<SubCommand>();

	private MessageManager mm = AlcBagsMessageManager.getInstance();

	public void setup() {
		commands.clear();
		commands.add(new Help());
		commands.add(new Give());
		commands.add(new Open());
		commands.add(new List());
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		if (args.length == 0) {
			commandInfo(sender);
			return true;
		}

		SubCommand target = get(args[0]);

		if (target == null) {
			mm.errSender(sender, "\"/" + commandLabel + " " + args[0]
					+ "\" is not a valid command!");
			return true;
		}

		ArrayList<String> argsList = new ArrayList<String>();
		argsList.addAll(Arrays.asList(args));
		argsList.remove(0);
		String[] newArgs = argsList.toArray(new String[argsList.size()]);

		try {
			target.onCommand(sender, newArgs);
		} catch (Exception e) {
			mm.errSender(sender, "An error has occured");
			mm.error(e);
		}

		return true;
	}

	public void commandInfo(CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "=====[ "
				+ AlcBags.getInstance().getName() + " Command Help ]=====");
		for (SubCommand cmd : getCommands()) {
			sender.sendMessage(ChatColor.BLUE + "> " + ChatColor.GOLD
					+ cmd.name() + ":");
			if (cmd.aliases() != null)
				sender.sendMessage(ChatColor.YELLOW + "    Aliases: "
						+ ChatColor.GRAY + "("
						+ Joiner.on(" ,").join(cmd.aliases()) + ")");
			if (cmd.usage().length == 1)
				sender.sendMessage(ChatColor.YELLOW + "    Usage:" + ChatColor.GRAY
						+ " /alcbags " + cmd.name() + " " + cmd.usage()[0]);
			else {
				sender.sendMessage(ChatColor.YELLOW + "    Usage:");
				for (String str : cmd.usage())
					sender.sendMessage(ChatColor.GRAY + "     /alcbags " + cmd.name() + " "
							+ str);
			}
		}
		sender.sendMessage(ChatColor.GREEN + "==============================");
	}

	public SubCommand get(String name) {
		for (SubCommand cmd : getCommands()) {
			if (cmd.name().equalsIgnoreCase(name))
				return cmd;
			if (cmd.aliases() != null) {
				for (String alias : cmd.aliases())
					if (name.equalsIgnoreCase(alias))
						return cmd;
			}
		}
		return null;
	}

	public ArrayList<SubCommand> getCommands() {
		return commands;
	}

	public void cmdInfo(CommandSender sender, SubCommand cmd) {
		sender.sendMessage(ChatColor.BLUE + "=====[ "
				+ AlcBags.getInstance().getName() + " Command Help ]=====");
		sender.sendMessage(ChatColor.BLUE + "> " + ChatColor.GOLD + cmd.name()
				+ ":");
		if (cmd.aliases() != null)
			sender.sendMessage(ChatColor.YELLOW + "    Aliases: "
					+ ChatColor.GRAY + "("
					+ Joiner.on(" ,").join(cmd.aliases()) + ")");
		if (cmd.usage().length == 1)
			sender.sendMessage(ChatColor.YELLOW + "    Usage:" + ChatColor.GRAY
					+ " /alcbags " + cmd.name() + " " + cmd.usage()[0]);
		else {
			sender.sendMessage(ChatColor.YELLOW + "    Usage:");
			for (String str : cmd.usage())
				sender.sendMessage(ChatColor.GRAY + "     /alcbags " + cmd.name() + " "
						+ str);
		}
		sender.sendMessage(ChatColor.BLUE + "==============================");
	}

	public void commandInfo(CommandSender sender, String command) {
		SubCommand cmd = get(command);
		if (cmd != null)
			cmdInfo(sender, cmd);
		else
			mm.errSender(sender, "Could not find sub command \"" + command
					+ "\".");

	}

	public ArrayList<String> onTabComplete(CommandSender sender, Command cmd,
			String alias, String[] args) {

		ArrayList<String> tabComplete = new ArrayList<String>();
		if (args.length == 1) {
			for (SubCommand sc : getCommands()) {
				tabComplete.add(sc.name());
			}
		} else if (args.length >= 2) {
			SubCommand sc = get(args[0]);
			if (sc == null)
				return tabComplete;

			if (sc instanceof Give) {
				if (args.length == 2) {
					for (DyeColorRef c : DyeColorRef.values())
						tabComplete.add(c.toString());
				} else if (args.length == 3) {
					for (Player player : Bukkit.getOnlinePlayers())
						tabComplete.add(player.getName());
				}
			} else if (sc instanceof Help) {
				if (args.length == 2) {
					for (SubCommand sc2 : getCommands())
						tabComplete.add(sc2.name());
				}
			} else if (sc instanceof List) {
			} else if (sc instanceof Open) {
				if (args.length == 2) {
					for (DyeColorRef c : DyeColorRef.values())
						tabComplete.add(c.toString());
				} else if (args.length == 3) {
					File playerfolder = new File(
							((World) Bukkit.getWorlds().get(0)).getWorldFolder(),
							"players");
					if (!playerfolder.exists() || !playerfolder.isDirectory()) {
						for (Player player : Bukkit.getOnlinePlayers())
							tabComplete.add(player.getName());
						return tabComplete;
					} else {
						for (File f : playerfolder.listFiles()) {
							String playername = f.getName().substring(0, f.getName().length() - 4);
							tabComplete.add(playername);
						}
					}
				}
			}
		}

		return tabComplete;
	}
}