package com.gmail.jrhluckow.sabotage.game;

import com.gmail.jrhluckow.sabotage.Sabotage;
import com.gmail.jrhluckow.sabotage.lang.TranslatableContent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
  }
  public static void endGame() {
    RUNNING = false;
    secondsToStart = 100;
    Bukkit.getOnlinePlayers().forEach(player -> {
      player.damage(20.0);
      final String[] saboteurs = {""};
      Team.SABOTEURS.forEach(sab -> {
       saboteurs[0] = saboteurs[0] + " " + sab.getName();
      });
      player.sendMessage(TranslatableContent.translateContent("messages.GAME_ENDED") + saboteurs[0]);
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
}
