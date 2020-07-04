package com.gmail.jrhluckow.sabotage.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    public static List<Player> INNOCENTS = new ArrayList<>();
    public static List<Player> DETECTIVES = new ArrayList<>();
    public static List<Player> SABOTEURS = new ArrayList<>();

    public static void clearTeams() {
        INNOCENTS.clear();
        DETECTIVES.clear();
        SABOTEURS.clear();
    }

}
