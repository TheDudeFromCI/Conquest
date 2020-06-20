package me.ci.Community;

import me.ci.WhCommunity;
import me.ci.Conquest.Events.BuildingBuildEvent;
import me.ci.Conquest.Events.BuildingDestroyEvent;
import me.ci.Conquest.Misc.ConquestEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

@SuppressWarnings("deprecation")
public class WhCommunityListeners implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(PlayerBucketEmptyEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(PlayerBucketFillEvent e)
	{
		AntiGreif.e(e);
	}

	// @EventHandler(priority = EventPriority.HIGHEST)
	// public void evn(PaintingBreakByEntityEvent e)
	// {
	// AntiGreif.e(e);
	// }

	// @EventHandler(priority = EventPriority.HIGHEST)
	// public void evn(PaintingBreakEvent e)
	// {
	// AntiGreif.e(e);
	// }

	// @EventHandler(priority = EventPriority.HIGHEST)
	// public void evn(PaintingPlaceEvent e)
	// {
	// AntiGreif.e(e);
	// }

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(BlockBreakEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(BlockPlaceEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(EntityBreakDoorEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(EntityChangeBlockEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(BlockBurnEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(BlockIgniteEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(EntityExplodeEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(CreatureSpawnEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(PlayerInteractEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(BlockFromToEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void evn(EntityDamageByEntityEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(PlayerLoginEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(AsyncPlayerChatEvent e)
	{
		ChatSystem.e(e);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerQuitEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerMoveEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerJoinEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(PlayerTeleportEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(BlockPhysicsEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void event(BuildingBuildEvent e)
	{
		ConquestEvents.e(e);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void event(BuildingDestroyEvent e)
	{
		ConquestEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(ChunkUnloadEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(InventoryClickEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(PlayerDropItemEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(PlayerPickupItemEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(FoodLevelChangeEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(EntityDamageEvent e)
	{
		AntiGreif.e(e);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void event(PlayerItemHeldEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(ServerListPingEvent e)
	{
		MiscEvents.e(e);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(PluginDisableEvent e)
	{
		if (e.getPlugin() == WhCommunity.plugin)
			WhCommunity.plugin.onDisable();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void event(ServerCommandEvent e)
	{
		if (e	.getCommand()
				.equals("save-all"))
			Save.saveAll();
	}
}
