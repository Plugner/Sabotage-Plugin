package com.gmail.jrhluckow.sabotage.listener;

import com.gmail.jrhluckow.sabotage.chests.ChestSystem;
import com.gmail.jrhluckow.sabotage.game.GameStatus;
import com.gmail.jrhluckow.sabotage.lang.TranslatableContent;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

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

    @EventHandler
    public void onDead(PlayerDeathEvent event) {
        Player p = event.getEntity();
        event.setDeathMessage(null);
        if(GameStatus.isRunning()) {
          GameStatus.alivePlayers.remove(p);
          p.sendMessage(TranslatableContent.translateContent("messages.PLAYER_DIE"));
          p.setGameMode(GameMode.SPECTATOR);
          if(GameStatus.alivePlayers.size() == 0) {

              Bukkit.getOnlinePlayers().forEach(player -> {p.setGameMode(GameMode.SURVIVAL);player.teleport(new Location(Bukkit.getWorld(Objects.requireNonNull(GameStatus.config.getString("config.SPAWN_WORLD"))),0,0,0));});
              GameStatus.endGame();
          }

        }
    }

}
