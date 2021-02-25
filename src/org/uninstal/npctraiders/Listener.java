package org.uninstal.npctraiders;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.uninstal.npctraiders.data.Merch;

public class Listener implements org.bukkit.event.Listener {

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		
		Inventory inventory = e.getInventory();
		if(!inventory.getTitle()
				.startsWith("Merchant Editor")) 
			return;
		
		String merchId = inventory.getTitle()
				.substring(inventory.getTitle()
				.indexOf(":") + 2);
		
		LinkedList<Merch> merchs = new LinkedList<>();
		
		for(int k = 0; k < 9; k++) {
			
			int k2 = k + 9;
			int k3 = k2 + 9;
			
			ItemStack itemStack1 = inventory.getItem(k) == null ? new ItemStack(Material.AIR) : inventory.getItem(k);
			ItemStack itemStack2 = inventory.getItem(k2) == null ? new ItemStack(Material.AIR) : inventory.getItem(k2);
			ItemStack itemStack3 = inventory.getItem(k3) == null ? new ItemStack(Material.AIR) : inventory.getItem(k3);
			
			Merch merch = new Merch(itemStack1, itemStack2, itemStack3);
			merchs.add(merch);
			
			continue;
		}
		
		Main main = Main.getPlugin(Main.class);
		main.merchs.put(merchId, merchs);
		
		return;
	}
}
