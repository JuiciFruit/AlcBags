package juicydev.alcbags.bag;

import juicydev.alcbags.bag.BagLoader.DyeColorRef;
import juicydev.alcbags.util.BagUtil;
import juicydev.jcore.utils.item.ItemGlow;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class AlcBag extends ItemStack {

	private DyeColorRef colour;

	@SuppressWarnings("deprecation")
	private static ItemStack getBag(DyeColorRef colour) {
		ItemStack bag = new MaterialData(BagUtil.bagMaterial, colour
				.getColour().getDyeData()).toItemStack(1);
		ItemMeta meta = bag.getItemMeta();
		meta.setDisplayName(BagUtil.getBagName(colour));
		bag.setItemMeta(meta);
		return ItemGlow.addGlow(bag);
	}

	public AlcBag(DyeColorRef colour) {
		super(ItemGlow.addGlow(getBag(colour)));
		this.colour = colour;
	}

	public AlcBag(ItemStack stack) {
		super(ItemGlow.addGlow(stack));
		this.colour = BagUtil.getColour(stack);
	}

	public void setColour(DyeColorRef colour) {
		this.colour = colour;
	}

	public DyeColorRef getColour() {
		if (colour == null) {
			this.colour = BagUtil.getColour(this);
		}
		return colour;
	}
}
