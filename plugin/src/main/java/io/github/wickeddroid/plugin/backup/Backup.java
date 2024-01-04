package io.github.wickeddroid.plugin.backup;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.player.UhcPlayer;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.UhcPlugin;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

public class Backup {

    @Inject
    private Plugin plugin;
    @Inject
    private UhcTeamRegistry uhcTeamRegistry;
    @Inject
    private UhcGame uhcGame;
    @Inject
    private UhcPlayerRegistry uhcPlayerRegistry;


    private StringBuilder teamsData = new StringBuilder("[]");
    private StringBuilder gameData = new StringBuilder("{}");
    private StringBuilder playersData = new StringBuilder("[]");

    private void saveGame(UhcGame game) {
        gameData = new StringBuilder("{");


        gameData
                .append("\"host\":").append("\"").append(game.getHost()).append("\",")
                .append("\"state\":").append("\"").append(game.getUhcGameState().name()).append("\",")
                .append("\"start-time\":").append(game.getStartTime()).append(",")
                .append("\"current-time\":").append(game.getCurrentTime()).append(",")
                .append("\"worldborder\":").append(game.getWorldBorder()).append(",")
                .append("\"pvp-time\":").append(game.getTimeForPvp()).append(",")
                .append("\"meetup-time\":").append(game.getTimeForMeetup()).append(",")
                .append("\"applerate\":").append(game.getAppleRate()).append(",")
                .append("\"cobweb-limit\":").append(game.getCobwebLimit()).append(",")
                .append("\"pvp-enabled\":").append(game.isPvp()).append(",")
                .append("\"game-started\":").append(game.isGameStart()).append(",")
                .append("\"clean-enabled\":").append(game.isCleanItem()).append(",")
                .append("\"teams-enabled\":").append(game.isTeamEnabled()).append(",")
                .append("\"own-teams-enabled\":").append(game.isOwnTeamsEnabled()).append(",")
                .append("\"team-size\":").append(game.getTeamSize()).append(",")
                .append("\"ironmans\":[");

        var it = game.getIronmans().iterator();

        while (it.hasNext()) {
            String s = it.next().getName();

            playersData.append("\"").append(s).append("\"");

            if(it.hasNext()) { playersData.append(","); }
        }

        gameData.append("]}");
    }

    private void savePlayers(List<UhcPlayer> players) {
        playersData = new StringBuilder("[");

        var it = players.iterator();

        while (it.hasNext()) {
            var p = it.next();

            playersData
                    .append("{")
                    .append("\"uuid\":").append("\"").append(p.getUuid()).append("\",")
                    .append("\"name\":").append("\"").append(p.getName()).append("\",")
                    .append("\"kills\":").append(p.getKills()).append(",")
                    .append("\"alive\":").append(p.isAlive()).append(",")
                    .append("\"scattered\":").append(p.isScattered())
                    .append(it.hasNext() ? "}," : "}");
        }

        playersData.append("]");
    }

    private void saveTeams(List<UhcTeam> teams) {
        teamsData = new StringBuilder("[");

        var it = teams.iterator();

        while (it.hasNext()) {
            var t = it.next();

            teamsData
                    .append("{")
                    .append("\"name\":").append("\"").append(t.getName()).append("\",")
                    .append("\"minecraft-team\":").append("\"").append(t.getLeader()).append("\",")
                    .append("\"alive-count\":").append(t.getPlayersAlive()).append(",")
                    .append("\"kills\":").append(t.getKills()).append(",")
                    .append("\"leader\":").append("\"").append(t.getLeader()).append("\",")
                    .append("\"alive\":").append(t.isAlive()).append(",")
                    .append("\"members\":[");

            var itMembers = t.getMembers().iterator();

            while (itMembers.hasNext()) {
                var m = itMembers.next();

                teamsData.append("\"").append(m).append("\"");

                if(itMembers.hasNext()) { teamsData.append(","); }
            }

            teamsData.append(it.hasNext() ? "]}," : "]}");
        }

        teamsData.append("]");
    }

    public void save() throws IOException {
        saveGame(uhcGame);
        saveTeams(uhcTeamRegistry.getTeams().stream().toList());
        savePlayers(uhcPlayerRegistry.getPlayers().stream().toList());

        String json = "{\"game\":%s,\"players\":%s,\"teams\":%s}".formatted(gameData.toString(), playersData.toString(), teamsData.toString());

        var file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "uhc_backup.uhc");

        if(!file.exists()) { file.createNewFile(); }
        file.deleteOnExit();

        byte[] encoded = Base64.getEncoder().encode(json.getBytes());

        Files.write(file.toPath(), encoded);
    }
}
