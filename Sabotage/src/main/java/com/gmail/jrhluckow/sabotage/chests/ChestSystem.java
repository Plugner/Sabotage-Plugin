package com.gmail.jrhluckow.sabotage.chests;

import com.gmail.jrhluckow.sabotage.Sabotage;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestSystem {
    public static FileConfiguration config;
    public static Sabotage main;
    public ChestSystem(Sabotage main) {
        this.config = main.getConfig();
        this.main = main;
    }
    public static List<ItemStack> CHEST_ITEMS = new ArrayList<>();
    public static void registerItems() {
      for(String s: config.getStringList("chest_items")) {
          String[] item_string = s.split(":");
          String item_name = item_string[1];
          int amount = Integer.parseInt(item_string[0]);
          ItemStack item = new ItemStack(Material.valueOf(item_name), amount);
          CHEST_ITEMS.add(item);
      }
    }
    public static ItemStack randomItem() {
        Random r = new Random();
        int random_num = r.nextInt(CHEST_ITEMS.size());
        return CHEST_ITEMS.get(random_num);
    }
}
