package juicydev.alcbags.util;

import juicydev.alcbags.AlcBags;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public enum Perms {
	HELP(newPerm("help", "Allows the player to use the \"/alcbags help\" command.",
			PermissionDefault.TRUE)),
	GIVE(newPerm("give", "Allows the player to use the \"/alcbags give\" command.",
			PermissionDefault.OP)),
	OPEN(newPerm("open", "Allows the player to use the \"/alcbags open\" command.",
			PermissionDefault.OP)),
	LIST(newPerm("list", "Allows the player to use the \"/alcbags list\" command.",
			PermissionDefault.OP)),
	USE(newPerm("use", "Allows the player to use alcbags (Has -nodes to disallow usage per world).",
			PermissionDefault.TRUE));

	private Permission perm;
	private String name;

	private Perms(Permission perm) {
		this.perm = perm;
		this.name = perm.getName();
	}

	/**
	 * Check if a sender has the permission and send the sender a message
	 * telling them they do not have permission for the action.
	 * 
	 * @param player
	 * @return
	 */
	public boolean canUse(CommandSender sender) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		if (!sender.hasPermission(perm)) {
			AlcBagsMessageManager.getInstance().noPermSender(sender);
			return false;
		}
		return true;
	}

	/**
	 * Check if a sender has the permission.
	 * 
	 * @param player
	 * @return
	 */
	public boolean canUseSilent(CommandSender sender) {
		if (sender instanceof ConsoleCommandSender)
			return true;
		if (!sender.hasPermission(perm))
			return false;
		return true;
	}

	/**
	 * Gets the permission.
	 */
	public Permission getPerm() {
		return perm;
	}

	/**
	 * Gets the string permission node for the permission.
	 */
	public String toString() {
		return name;
	}

	private static Permission newPerm(String name, String description,
			PermissionDefault defaultValue) {
		return new Permission(AlcBags.getInstance().getName().toLowerCase()
				+ "." + name, description, defaultValue);
	}
}
