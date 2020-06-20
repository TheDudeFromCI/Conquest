package me.ci.Conquest.Military.Powers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ci.Community.PlayerMg;
import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.Buildings.Main.Army;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.ArmyType;
import me.ci.Conquest.Military.PowerDetails;
import me.ci.Conquest.Misc.InventorySelect;

public class Mage_Transform implements PowerDetails{
	private static final int stamina = 18;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(plot.isKingdomPlot()){
				if(plot.getKingdom()==kingdom){
					if(plot.getBuilding() instanceof Army){
						final Army other = (Army)plot.getBuilding();
						if(troop.getStamina()>=stamina){
							if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=1
									&&Math.abs(plot.getZ()-troop.getNorthWestCourner().getZ())<=1){
								final Inventory i = Bukkit.createInventory(null, 36, "Choose Troop Type");
								for(ArmyType type : ArmyType.values()){ i.addItem(makeItem(type.getName())); }
								final InventorySelect inv = new InventorySelect(p, "Choose Troop Type", i);
								p.openInventory(i);
								while(true){
									try{ Thread.sleep(50);
									}catch(final Exception exception){}
									if(inv.hasResponed()){
										p.closeInventory();
										inv.close();
										other.transform(ArmyType.getById(inv.getSlot()));
										p.sendMessage(ChatColor.GREEN+"Troop transformed.");
										troop.setStamina(troop.getStamina()-stamina);
										return;
									}
									if(i.getViewers().size()==0){
										inv.close();
										return;
									}
								}
							}else{
								p.sendMessage(ChatColor.RED+"This unit is too far away!");
								explain(p);
							}
						}else{
							p.sendMessage(ChatColor.RED+"The troop you are trying to combine does not have enough stamina to preform this action.");
							explain(p);
						}
					}else{
						p.sendMessage(ChatColor.RED+"This is not an army chunk!");
						explain(p);
					}
				}else{
					p.sendMessage(ChatColor.RED+"This is not your kingdom!");
					explain(p);
				}
			}else{
				p.sendMessage(ChatColor.RED+"There is no kingdom here!");
				explain(p);
			}
		}else p.sendMessage(ChatColor.RED+"This troop is nullified! This means they cannot use their abilities anymore!");
	}
	private static ItemStack makeItem(final String name){
		final ItemStack item = new ItemStack(Material.PAPER);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW+name);
		item.setItemMeta(meta);
		return item;
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"To use this power, click on another military unit that is touching this one, and when an inventory view pops up, select the troop type you wish to transform it into. They shall remain in that form for five minutes."); }
}