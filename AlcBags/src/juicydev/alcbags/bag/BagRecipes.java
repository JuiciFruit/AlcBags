package juicydev.alcbags.bag;

import juicydev.alcbags.bag.BagLoader.DyeColorRef;
import juicydev.alcbags.util.BagUtil;
import juicydev.jcore.utils.item.ItemGlow;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

/**
 * @author JuicyDev
 */
public class BagRecipes {

	@SuppressWarnings("deprecation")
	public static void registerRecipes() {
		for (DyeColorRef c : DyeColorRef.values()) {
			ShapedRecipe r = new ShapedRecipe(ItemGlow.addGlow(BagUtil
					.getBagItem(c)));
			r.shape("wcw", "eme", "wcw");
			r.setIngredient('c', Material.ENDER_CHEST);
			r.setIngredient('m', Material.EMERALD);
			r.setIngredient('e', Material.EYE_OF_ENDER);
			r.setIngredient('w', new MaterialData(Material.WOOL, c.getColour()
					.getWoolData()));
			Bukkit.addRecipe(r);

			ShapedRecipe r2 = new ShapedRecipe(ItemGlow.addGlow(BagUtil
					.getBagItem(c)));
			r2.shape("wew", "cmc", "wew");
			r2.setIngredient('c', Material.ENDER_CHEST);
			r2.setIngredient('m', Material.EMERALD);
			r2.setIngredient('e', Material.EYE_OF_ENDER);
			r2.setIngredient('w', new MaterialData(Material.WOOL, c.getColour()
					.getWoolData()));
			Bukkit.addRecipe(r2);
		}
	}
}
