package com.gmail.jrhluckow.sabotage.game;

public class GameStatus {
  public static boolean
  RUNNING = false;

  public static void startGame() {
    RUNNING = true;
  }
  public static void endGame() {
    RUNNING = false;
  }
  public static boolean isRunning() {
    return RUNNING;
  }
}
