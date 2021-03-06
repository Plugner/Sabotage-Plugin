package com.gmail.jrhluckow.sabotage.commands;


import com.gmail.jrhluckow.sabotage.game.GameStatus;
import com.gmail.jrhluckow.sabotage.lang.TranslatableContent;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiversePlugin;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class End implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp() && GameStatus.isRunning()) {
            GameStatus.endGame();
            Bukkit.getOnlinePlayers().forEach((player) -> player.sendMessage(TranslatableContent.translateContent("%messages.FORCE_END")));
        }
        return false;
    }
}
