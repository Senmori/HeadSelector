package net.senmori.headselector.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener
{

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.SKULL))
		{
			
		}
	}
}
