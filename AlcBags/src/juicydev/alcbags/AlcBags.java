package juicydev.alcbags;

import java.util.List;

import juicydev.alcbags.bag.BagLoader;
import juicydev.alcbags.bag.BagRecipes;
import juicydev.alcbags.commands.CommandManager;
import juicydev.alcbags.listeners.BagListener;
import juicydev.alcbags.util.AlcBagsMessageManager;
import net.minecraft.util.com.google.common.base.Joiner;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AlcBags extends JavaPlugin {

	private static AlcBags instance;

	public static AlcBags getInstance() {
		return instance;
	}

	private PluginManager pm = Bukkit.getPluginManager();

	public void onEnable() {
		instance = this;

		if (!checkDepend()) {
			pm.disablePlugin(this);
			return;
		}

		AlcBagsMessageManager.setup();
		BagLoader.getInstance().load();
		CommandManager.getInstance().setup();
		BagRecipes.registerRecipes();

		PluginCommand cmd = getCommand("alcbags");
		cmd.setExecutor(CommandManager.getInstance());
		cmd.setTabCompleter(CommandManager.getInstance());

		pm.registerEvents(new BagListener(), this);
	}

	public void onDisable() {
		BagLoader.getInstance().save();
	}

	private boolean checkDepend() {
		List<String> plugins = getDescription().getSoftDepend();
		for (String name : plugins) {
			Plugin depend = pm.getPlugin(name);
			boolean isGood = true;
			if (depend == null) {
				getLogger().severe("Could not find plugin " + name + ".");
				getLogger().severe(
						"Please make sure " + name
								+ " is installed and up to date.");
				isGood = false;
			} else if (!depend.isEnabled()) {
				getLogger().severe(name + " is disabled.");
				getLogger().severe(
						"Please make sure " + name + " is up to date.");
				isGood = false;
			}
			if (!isGood) {
				getLogger().severe(
						"Dependencies: "
								+ Joiner.on(" ,").join(
										getDescription().getSoftDepend()));
				return false;
			}
			getLogger().info("Hooked " + name + "!");
		}
		return true;
	}
}
