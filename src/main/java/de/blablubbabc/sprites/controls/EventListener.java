package de.blablubbabc.sprites.controls;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import de.blablubbabc.sprites.SpritesPlugin;

public class EventListener implements Listener {

	private final SpritesPlugin plugin;

	public EventListener(SpritesPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_AIR
				&& event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		if (event.getHand() != EquipmentSlot.HAND) return;

		var player = event.getPlayer();
		var itemInHand = player.getInventory().getItemInMainHand();
		if (itemInHand == null || (itemInHand.getType() != Material.BLAZE_ROD && itemInHand.getType() != Material.STICK)) {
			return;
		}

		var rayTraceResult = player.rayTraceBlocks(200, FluidCollisionMode.NEVER);
		if (rayTraceResult == null) return;

		var hitPosition = rayTraceResult.getHitPosition();
		if (hitPosition == null) return;

		var spawnLocation = new Location(
				player.getWorld(),
				hitPosition.getX(),
				hitPosition.getY(),
				hitPosition.getZ()
		);

		if (itemInHand.getType() == Material.BLAZE_ROD) {
			player.sendMessage("Boom");
			plugin.playEffect(spawnLocation);
		}
	}
}
