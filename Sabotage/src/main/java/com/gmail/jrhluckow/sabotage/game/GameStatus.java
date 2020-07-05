package com.gmail.jrhluckow.sabotage.game;

import com.gmail.jrhluckow.sabotage.Sabotage;
import com.gmail.jrhluckow.sabotage.lang.TranslatableContent;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GameStatus {
  public static FileConfiguration config;
  public static Sabotage main;

  public static List<Player> alivePlayers = new ArrayList<>();

  public GameStatus(Sabotage main) {
    this.config = main.getConfig();
    this.main = main;
  }
  public static boolean
  RUNNING = false;

  public static void startGame() {
    RUNNING = false;
    secondsToStart = 100;
    startDelay();
  }
  public static void forceStartGame() {
    int min = config.getInt("config.MIN_PLAYER_TO_START");
    int players = Bukkit.getOnlinePlayers().size();

    AtomicInteger saboteurs = new AtomicInteger((2) * (players / min));
    AtomicInteger detectives = new AtomicInteger((1) * (players / min));

    Bukkit.getOnlinePlayers().forEach(player -> {
      if(saboteurs.get() != 0) {
        player.sendMessage(TranslatableContent.translateContent("messages.GAME_INTRO_TEAM_SABOTEUR"));
        saboteurs.getAndDecrement();
        Team.SABOTEURS.add(player);
      }else if(detectives.get() != 0) {
        player.sendMessage(TranslatableContent.translateContent("messages.GAME_INTRO_TEAM_DETECTIVE"));
        detectives.getAndDecrement();
        Team.DETECTIVES.add(player);
      }else{
        player.sendMessage(TranslatableContent.translateContent("messages.GAME_INTRO_TEAM_INNOCENT"));
        Team.INNOCENTS.add(player);
      }
    });

    RUNNING = true;
    Bukkit.getOnlinePlayers().forEach(player -> {
      alivePlayers.add(player);
    });
    handleStart();
  }
  public static void endGame() {
    RUNNING = false;
    secondsToStart = 100;
    Bukkit.getOnlinePlayers().forEach(player -> {

      final String[] saboteurs = {""};
      Team.SABOTEURS.forEach(sab -> {
       saboteurs[0] = saboteurs[0] + " " + sab.getName();
      });
      player.sendMessage(TranslatableContent.translateContent("messages.GAME_ENDED") + saboteurs[0]);
      player.teleport(new Location(Bukkit.getWorld("world"), 0 , 0, 0));
      Sabotage.getMultiverseCore().deleteWorld(runningGame.getName());
      runningGame = null;
    });
    Team.clearTeams();
    alivePlayers.clear();
  }
  public static boolean isRunning() {
    return RUNNING;
  }

  public static int secondsToStart = 100;
  public static void startDelay() {

    RUNNING = false;
    Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
      @Override
      public void run() {
        if(isRunning()) {
          return;
        }
        if(secondsToStart == 0) {
          if(Bukkit.getOnlinePlayers().size() <= config.getInt("config.MIN_PLAYER_TO_START")) {
            Bukkit.broadcastMessage(TranslatableContent.translateContent("messages.LOWER_THAN_MIN_PLAYER_COUNT"));
            secondsToStart = 100;
          }else{
            forceStartGame();
          }

        }else{
          Bukkit.getOnlinePlayers().forEach(player -> {
            /**
             * @author Plugner
             * @see com.gmail.jrhluckow.sabotage.listener.PluginEventHandler
             */
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(TranslatableContent.translateContent("messages.TITLE_STARTING").replace("?", ""+secondsToStart) + " - " + TranslatableContent.translateContent("messages.SUBTITLE_STARTING").replace("?", "" + secondsToStart)));
          });
          secondsToStart = secondsToStart - 1;
        }
      }
    },20,20);
  }

  public static MultiverseWorld runningGame = null;
  private static void handleStart() {

    MultiverseWorld arena = Sabotage.randomWorld();
    String newName = arena.getName() + "inGame";

    Sabotage.getMultiverseCore().cloneWorld(arena.getName(), newName, "normal");
    MultiverseWorld arena_world = Sabotage.getMultiverseCore().getMVWorldManager().getMVWorld(newName);
    Location loc = arena_world.getSpawnLocation();
    runningGame = arena_world;

    Bukkit.getOnlinePlayers().forEach(player -> {
      player.sendMessage(TranslatableContent.translateContent("messages.LOAD_MAP").replace("$mapname$", arena.getName()));
      String role = "";
      int alive = alivePlayers.size();
      if(Team.SABOTEURS.contains(player)) {
        role = TranslatableContent.translateContent("config.ROLE_SABOTEUR");
      }else if(Team.DETECTIVES.contains(player)) {
        role = TranslatableContent.translateContent("config.ROLE_DETECTIVE");
      }else{
        role = TranslatableContent.translateContent("config.ROLE_INNOCENT");
      }

      List<String> data = new ArrayList<>();
      data.add(player.getName());
      data.add(role);
      data.add(alive + "");

      player.teleport(loc);
      taskActionBarIG(data);
    });
  }
  private static void taskActionBarIG(List<String> data) {
    Player p = Bukkit.getPlayer(data.get(0));
    String role = data.get(1);
    int alive = Integer.parseInt(data.get(2));
    String inGameActionBar = TranslatableContent.translateContent("messages.INGAME_ACTIONBAR").replace("$role$", role).replace("$alive$", alive + "");
    Bukkit.getScheduler().runTaskTimer(main, () -> {
       if(!isRunning()) {
         return;
       }else{
         p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(inGameActionBar));
       }
    },40,40);
  }

}
