package org.uninstal.npctraiders;

import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.uninstal.npctraiders.data.Merch;

import com.github.RulerOfTheCards.core.Merchant;
import com.github.RulerOfTheCards.core.MerchantOffer;

public class Main extends JavaPlugin {
	
	private Files files;
	public Map<String, LinkedList<Merch>> merchs;

	@Override
	public void onEnable() {
		
		this.files = new Files(this);
		YamlConfiguration merchs = files.registerNewFile("merchs");
		
		for(String merchId : merchs.getKeys(false)) {
			LinkedList<Merch> list = new LinkedList<>();
			
			for(int j = 1; j < 10; j++) {
				String way = merchId + "." + j + ".";
				
				ItemStack i1 = merchs.getItemStack(way + "i1");
				ItemStack i2 = merchs.getItemStack(way + "i2");
				ItemStack r = merchs.getItemStack(way + "r");
				
				Merch merch = new Merch(i1, i2, r);
				list.add(merch);
			}
			
			this.merchs.put(merchId, list);
			continue;
		}
		
		Bukkit.getPluginManager().registerEvents(new Listener(), this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(command.getName().equalsIgnoreCase("merch")) {
			
			if(args.length >= 3 && args[0].equalsIgnoreCase("open")) {
				
				String merchId = args[1];
				String playerName = args[2];
				
				LinkedList<Merch> merchs = 
				this.merchs.get(merchId);
				
				if(merchs == null) {
					
					sender.sendMessage("Merch is null!");
					return false;
				}
				
				Player player = Bukkit.getPlayer(playerName);
				if(player == null) {
					
					sender.sendMessage("Player is null!");
					return false;
				}
				
				else {
					
					//Дата из библиотеки другого плагина
					Merchant merchant = new Merchant();
					
					for(Merch merch : merchs) {
						
						MerchantOffer merchantOffer = new MerchantOffer(
								merch.getIngredient1(),
								merch.getIngredient2(),
								merch.getResult());
						
						merchant.addOffer(merchantOffer);
						continue;
					}
					
					merchant.setTitle(merchId);
					merchant.openTrading(player);
					
					return false;
				}
			}
			
			if(args.length >= 2 && args[0].equalsIgnoreCase("create")) {
				
				String merchId = args[1];
				this.merchs.put(merchId, null);
				
				return false;
			}
			
			if(args.length >= 2 && args[0].equalsIgnoreCase("edit")) {
				
				String merchId = args[1];
				Player player = (Player) sender;
				
				if(this.merchs.containsKey(merchId)) {
					
					LinkedList<Merch> list = this.merchs.get(merchId);
					Inventory inventory = Bukkit.createInventory(null, 3*9, "Merchant Editor: " + merchId);
					
					int k = 0;
					for(Merch merch : list) {
						
						ItemStack itemStack1 = merch.getIngredient1();
						ItemStack itemStack2 = merch.getIngredient2();
						ItemStack itemStack3 = merch.getResult();
						
						int k2 = k + 9;
						int k3 = k2 + 9;
						
						inventory.setItem(k, itemStack1);
						inventory.setItem(k2, itemStack2);
						inventory.setItem(k3, itemStack3);
						
						continue;
					}
					
					player.openInventory(inventory);
					return false;
				}
				
				else {
					
					Inventory inventory = Bukkit.createInventory(null, 3*9, "Merchant Editor");
					player.openInventory(inventory);
					
					return false;
				}
			}
		}
		
		return false;
	}
}
