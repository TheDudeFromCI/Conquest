package me.ci.Conquest.Military.Powers;

import java.util.ArrayList;

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
import me.ci.Conquest.Military.PowerDetails;
import me.ci.Conquest.Misc.HeldArmy;
import me.ci.Conquest.Misc.InventorySelect;

public class SiegeTower_Transport implements PowerDetails{
	private static final int stamina = 1;
	public void run(final Player p, final Location click, final Army troop){
		final Kingdom kingdom = PlayerMg.getKingdom(p.getName());
		final Plot plot = PlotMg.getPlotAt(click.getChunk());
		if(!troop.isNullified()){
			if(Math.abs(plot.getX()-troop.getNorthWestCourner().getX())<=1
					&&Math.abs(plot.getZ()-troop.getNorthWestCourner().getZ())<=1){
				if(plot.isClaimed()){
					if(troop.getStamina()>=stamina){
						if(plot.isKingdomPlot()){
							if(plot.getKingdom()==kingdom){
								if(plot.getBuilding() instanceof Army){
									if(plot.getBuilding()!=troop){
										if(troop.getHeldArmies().size()<54){
											final Army a = (Army)plot.getBuilding();
											if(a.getHeldArmies().size()==0){
												troop.addHeldArmy(a);
												troop.setStamina(troop.getStamina()-stamina);
												a.delete(true, false);
												p.sendMessage(ChatColor.GREEN+"Unit loaded.");
											}else{
												p.sendMessage(ChatColor.RED+"You cannot load units that are holding other units!");
												explain(p);
											}
										}else{
											p.sendMessage(ChatColor.RED+"This troop is holding too many units!");
											explain(p);
										}
									}else{
										p.sendMessage(ChatColor.RED+"This unit cannot transport itself!");
										explain(p);
									}
								}else{
									p.sendMessage(ChatColor.RED+"This is not an army unit!");
									explain(p);
								}
							}else{
								p.sendMessage(ChatColor.RED+"This is not your kingdom!");
								explain(p);
							}
						}else{
							p.sendMessage(ChatColor.RED+"You cannot unload here!");
							explain(p);
						}
					}else{
						p.sendMessage(ChatColor.RED+"This unit doesn't have enough stamina to do this!");
						explain(p);
					}
				}else{
					final Inventory i = Bukkit.createInventory(null, 54, "Unload Unit");
					ArrayList<String> lore;
					ItemStack item;
					ItemMeta meta;
					for(HeldArmy h : troop.getHeldArmies()){
						lore=new ArrayList<>();
						item=new ItemStack(Material.PAPER);
						meta=item.getItemMeta();
						meta.setDisplayName(ChatColor.YELLOW+h.getRealArmyType().getName());
						lore.add(ChatColor.GRAY+"HP: "+h.getHp());
						lore.add(ChatColor.GRAY+"Stamina: "+h.getStamina());
						lore.add(ChatColor.GRAY+"Level: "+h.getLevel());
						lore.add(ChatColor.GRAY+"Nullified?: "+(h.isNullified()?"Yes":"No"));
						lore.add(ChatColor.GRAY+"Blessed?: "+(h.isBlessed()?"Yes":"No"));
						lore.add(ChatColor.GRAY+"Wizard?: "+(h.hasWizard()?"Yes":"No"));
						lore.add(ChatColor.GRAY+"Commander?: "+(h.hasCommander()?"Yes":"No"));
						if(h.isTransformed())lore.add(ChatColor.LIGHT_PURPLE+"Transformed Into: "+h.getArmyType());
						meta.setLore(lore);
						item.setItemMeta(meta);
						i.addItem(item);
					}
					final InventorySelect inv = new InventorySelect(p, "Unload Unit", i);
					p.openInventory(i);
					while(true){
						try{ Thread.sleep(50);
						}catch(final Exception exception){}
						if(inv.hasResponed()){
							final HeldArmy held = troop.getHeldArmies().get(inv.getSlot());
							troop.removeHeldArmy(held);
							held.unload(plot.getChunk());
							p.closeInventory();
							p.sendMessage(ChatColor.GREEN+"Unit unloaded.");
							inv.close();
							return;
						}
						if(i.getViewers().size()==0){
							inv.close();
							return;
						}
					}
				}
			}else{
				p.sendMessage(ChatColor.RED+"That area is too far away!");
				explain(p);
			}
		}else{
			p.sendMessage(ChatColor.RED+"This unit is nullified!");
			explain(p);
		}
	}
	private static void explain(final Player p){ p.sendMessage(ChatColor.GRAY+"There are two ways to use this power. The first is loading units, and the second is unloading units. Only loading units costs stamina. To load a unit, click on a unit that is touching this siege tower. To unload, click on a wilderness chunk touching this unit and a menu will popup, allowing you choose what you wish to unload."); }
}