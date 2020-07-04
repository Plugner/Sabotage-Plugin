package com.gmail.jrhluckow.sabotage.game;

import com.gmail.jrhluckow.sabotage.Sabotage;
import com.gmail.jrhluckow.sabotage.lang.TranslatableContent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

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
    RUNNING = true;
    Bukkit.getOnlinePlayers().forEach(player -> {
      alivePlayers.add(player);
    });
  }
  public static void endGame() {
    RUNNING = false;

    Bukkit.getOnlinePlayers().forEach(player -> {
      player.damage(20.0);
      player.sendMessage(TranslatableContent.translateContent("messages.GAME_ENDED") + Team.SABOTEURS.toArray().toString());
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
    Bukkit.getScheduler().runTaskTimer(main, new BukkitRunnable() {
      @Override
      public void run() {
        if(secondsToStart == 0) {
          if(Bukkit.getOnlinePlayers().size() <= config.getInt("config.MIN_PLAYER_TO_START")) {
            Bukkit.broadcastMessage(TranslatableContent.translateContent("messages.LOWER_THAN_MIN_PLAYER_COUNT"));
            secondsToStart = 100;
          }else{
            forceStartGame();
          }

        }else{
          Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle(TranslatableContent.translateContent("messages.TITLE_STARTING").replace("?", ""+secondsToStart), TranslatableContent.translateContent("message.SUBTITLE_STARTING").replace("?", "" + secondsToStart), 10, 10, 10 );
          });
          secondsToStart = secondsToStart - 1;
        }
      }
    },20,20);
  }
}
