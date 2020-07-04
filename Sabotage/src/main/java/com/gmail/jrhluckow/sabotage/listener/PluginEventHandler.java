package com.gmail.jrhluckow.sabotage.listener;

import com.gmail.jrhluckow.sabotage.chests.ChestSystem;
import com.gmail.jrhluckow.sabotage.game.GameStatus;
import com.gmail.jrhluckow.sabotage.lang.TranslatableContent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginEventHandler implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        event.getPlayer().sendMessage(TranslatableContent.translateContent("%messages.JOIN_MESSAGE"));
    }
    @EventHandler
    public void onChestRightClick(PlayerInteractEvent event) {
        if(event.getClickedBlock().getType() == Material.CHEST) {
            if(GameStatus.isRunning()) {
                event.setCancelled(true);
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
                event.getClickedBlock().setType(Material.AIR);
                event.getPlayer().getInventory().addItem(ChestSystem.randomItem());
            }
        }

    }
}
