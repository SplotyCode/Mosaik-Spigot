package io.github.splotycode.mosaik.spigot.util;

import io.github.splotycode.mosaik.spigot.locale.SpigotMessageContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Infoboard {

    private static final char[] NUMBERS = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'k', 'l', 'm', 'n', 'o', 'r'
    };

    private Scoreboard scoreboard;
    private Objective objective;
    private Map<String, String> scores = new HashMap<>();
    private SpigotMessageContext ctx;

    public Infoboard() {
        this(null);
    }

    public Infoboard(SpigotMessageContext ctx) {
        this.ctx = ctx;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("mosaik-board", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private void reset() {
        scoreboard.getEntries().forEach(scoreboard::resetScores);
        scoreboard.getTeams().forEach(Team::unregister);
    }

    public Infoboard addRawScore(String key, String value) {
        scores.put(key, value);
        return this;
    }

    public Infoboard addScore(String translation, String value) {
        if (ctx == null) throw new IllegalArgumentException("Infoboard has no message context");
        return addRawScore(ctx.translate(translation), value);
    }

    public void send(Player player) {
        update();
        player.setScoreboard(scoreboard);
    }

    public void update() {
        reset();
        int entryCount = 0;
        int lineCount = scores.size() * 3 - 1;
        Iterator<Map.Entry<String, String>> iter = scores.entrySet().iterator();
        Map.Entry<String, String> e = null;
        for (int i = lineCount; i > 0; i--) {
            StringBuilder entryName = new StringBuilder();
            for (int j = 0; j <= entryCount / NUMBERS.length; j++) {
                entryName.append("§").append(NUMBERS[entryCount % NUMBERS.length]);
            }
            entryCount++;
            int mode = i % 3;
            if (mode == 2) {
                if (iter.hasNext()) {
                    e = iter.next();
                } else {
                    return;
                }
            }
            Team team = scoreboard.registerNewTeam(e.getKey() + mode);
            switch (mode) {
                case 2:
                    team.setPrefix("§a" + e.getKey());
                    team.setSuffix("§a:");
                    break;
                case 1:
                    String val = ChatColor.stripColor(e.getValue());
                    if (val.length() > 14) {
                        team.setPrefix("§7" + val.substring(0, 14));
                        team.setSuffix("§7" + val.substring(14));
                    } else {
                        team.setPrefix("§7" + val);
                    }
                    break;
                case 0:
                    team.setDisplayName("");
                    break;
            }
            team.addEntry(entryName.toString());
            objective.getScore(entryName.toString()).setScore(i);
        }
    }

}
