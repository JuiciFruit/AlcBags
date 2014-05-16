package juicydev.alcbags.commands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

	/**
	 * Executes the given command
	 * 
	 * @param sender
	 *            The sender of the command
	 * @param args
	 *            Passed command arguments
	 */
	public abstract void onCommand(CommandSender sender, String[] args);

	/**
	 * @return Name of the command
	 */
	public abstract String name();

	/**
	 * @return An string array of the command aliases
	 */
	public abstract String[] aliases();

	/**
	 * @return A string array of the command usage
	 */
	public abstract String[] usage();
}
