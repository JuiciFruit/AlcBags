package juicydev.alcbags.bag;

import java.util.UUID;

import juicydev.alcbags.bag.BagLoader.DyeColorRef;
import juicydev.alcbags.util.BagUtil;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class BagInventory {

	private int bagSize = 54;
	private UUID owner;
	private DyeColorRef colour;
	private Inventory inventory;

	public BagInventory(UUID owner, DyeColorRef colour) {
		this.owner = owner;
		this.colour = colour;
		this.inventory = Bukkit.createInventory(Bukkit.getPlayer(owner),
				bagSize, BagUtil.getBagInvName(colour));
	}

	public UUID getOwner() {
		return owner;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public DyeColorRef getColour() {
		return colour;
	}
}
