package org.uninstal.npctraiders.data;

import org.bukkit.inventory.ItemStack;

public class Merch {

	private ItemStack ingredient1;
	private ItemStack ingredient2;
	private ItemStack result;
	
	public Merch(ItemStack ingredient1, ItemStack ingredient2, ItemStack result) {
		
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.result = result;
	}

	public ItemStack getIngredient1() {
		return ingredient1;
	}
	
	public ItemStack getIngredient2() {
		return ingredient2;
	}
	
	public ItemStack getResult() {
		return result;
	}
}
