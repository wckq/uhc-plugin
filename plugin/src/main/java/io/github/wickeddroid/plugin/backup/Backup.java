package io.github.wickeddroid.plugin.backup;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.api.player.UhcPlayer;
import io.github.wickeddroid.api.scenario.GameScenario;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.team.UhcTeam;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import io.github.wickeddroid.plugin.game.UhcGameManager;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scenario.ScenarioManager;
import io.github.wickeddroid.plugin.scenario.SettingManager;
import io.github.wickeddroid.plugin.team.DefaultUhcTeam;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.world.Worlds;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

@Singleton
public class Backup {

    private final Integer BACKUP_FILE_VERSION = 5;

    @Inject
    private Plugin plugin;
    @Inject
    private UhcTeamRegistry uhcTeamRegistry;
    @Inject
    private UhcGame uhcGame;
    @Inject
    private UhcPlayerRegistry uhcPlayerRegistry;
    @Inject
    private Worlds worlds;
    @Inject
    private UhcGameManager uhcGameManager;
    @Inject
    private UhcGameHandler uhcGameHandler;
    @Inject
    private ScenarioManager scenarioManager;
    @Inject
    private SettingManager settingManager;

    private StringBuilder teamsData = new StringBuilder("[]");
    private StringBuilder gameData = new StringBuilder("{}");
    private StringBuilder playersData = new StringBuilder("[]");
    private StringBuilder scenariosData = new StringBuilder("[]");
    private StringBuilder settingsData = new StringBuilder("[]");

    private void saveGame(UhcGame game) {
        gameData = new StringBuilder("{");

        gameData
                .append("\"host\":").append("\"").append(game.getHost()).append("\",")
                .append("\"state\":").append("\"").append(game.getUhcGameState().name()).append("\",")
                .append("\"start-time\":").append(game.getStartTime()).append(",")
                .append("\"current-time\":").append(game.getCurrentTime()).append(",")
                .append("\"current-episode-time\":").append(game.getCurrentEpisodeTime()).append(",")
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
                .append("\"ironman\":").append("\"").append(game.ironman()).append("\",")
                .append("\"paperman\":").append("\"").append(game.paperman()).append("\",")
                .append("\"ironmans\":[");

        var it = game.getIronmans().iterator();

        while (it.hasNext()) {
            String s = it.next();

            gameData.append("\"").append(s).append("\"");

            if(it.hasNext()) { gameData.append(","); }
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
                    .append("\"ip\":").append("\"").append(p.getSession().IP()).append("\",")
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

    private void saveScenarios() {
        scenariosData = new StringBuilder("[");

        var it = scenarioManager.getScenarios().stream().filter(GameScenario::isEnabled).toList().iterator();

        while (it.hasNext()) {
            var s = it.next();

            scenariosData.append("\"").append(s.getKey());

            if(s.isSupportsOptions()) {

                for (var opt : s.getOptions().values()) {
                    scenariosData.append("@").append(opt.optionName()).append("#").append(opt.options().indexOf(opt.value()));
                }
            }

            scenariosData.append("\"");
            if(it.hasNext()) {
                scenariosData.append(",");
            }
        }

        scenariosData.append("]");
    }

    private void saveSettings() {
        settingsData = new StringBuilder("[");

        var it = settingManager.getSettings().stream().filter(GameScenario::isEnabled).toList().iterator();

        while (it.hasNext()) {
            var s = it.next();

            settingsData.append("\"").append(s.getKey()).append("\"");

            if(it.hasNext()) {
                settingsData.append(",");
            }
        }

        settingsData.append("]");
    }



    public void save() throws IOException {
        saveGame(uhcGame);
        saveTeams(uhcTeamRegistry.getTeams().stream().toList());
        savePlayers(uhcPlayerRegistry.getPlayers().stream().toList());
        saveScenarios();
        saveSettings();

        String json = "{\"version\":%s,\"game\":%s,\"players\":%s,\"teams\":%s,\"scenarios\":%s,\"settings\":%s}".formatted(BACKUP_FILE_VERSION, gameData.toString(), playersData.toString(), teamsData.toString(), scenariosData.toString(), settingsData.toString());

        var file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "uhc_backup.uhc");

        if(!file.exists()) { file.createNewFile(); }
        file.deleteOnExit();

        byte[] encoded = Base64.getEncoder().encode(json.getBytes());

        Files.write(file.toPath(), encoded);
    }

    public void load() throws IOException, JsonParseException, NullPointerException, IllegalArgumentException {
        var file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "uhc_backup.uhc");
        if(!file.exists()) {
            return;
        }

        String jsonString = new String(Base64.getDecoder().decode(Files.readAllBytes(file.toPath())), StandardCharsets.UTF_8);

        var json = JsonParser.parseString(jsonString).getAsJsonObject();

        var version = json.get("version").getAsInt();

        if(version < BACKUP_FILE_VERSION) {
            Bukkit.getLogger().severe("Backup file version is older than supported");
            return;
        }

        var game = json.get("game").getAsJsonObject();

        var host = game.get("host").getAsString();
        var state = UhcGameState.valueOf(game.get("state").getAsString());
        var startTime = game.get("start-time").getAsLong();
        var currentTime = game.get("current-time").getAsInt();
        var currentEpisodeTime = game.get("current-episode-time").getAsInt();
        var worldBorder = game.get("worldborder").getAsInt();
        var pvpTime = game.get("pvp-time").getAsInt();
        var meetupTime = game.get("meetup-time").getAsInt();
        var appleRate = game.get("applerate").getAsInt();
        var cobwebLimit = game.get("cobweb-limit").getAsInt();
        var pvpEnabled = game.get("pvp-enabled").getAsBoolean();
        var gameStarted = game.get("game-started").getAsBoolean();
        var cleanEnabled = game.get("clean-enabled").getAsBoolean();
        var teamsEnabled = game.get("teams-enabled").getAsBoolean();
        var ownTeamsEnabled = game.get("own-teams-enabled").getAsBoolean();
        var teamSize = game.get("team-size").getAsInt();
        var ironmans = game.get("ironmans").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList();
        var ironman = game.get("ironman").getAsString();
        var paperman = game.get("paperman").getAsString();

        uhcGame.setHost(host);
        uhcGame.setUhcGameState(state);
        uhcGame.setStartTime(startTime);
        uhcGame.setCurrentTime(currentTime);
        uhcGame.setCurrentEpisodeTime(currentEpisodeTime);
        uhcGame.setWorldBorder(worldBorder);
        uhcGame.setTimeForPvp(pvpTime);
        uhcGame.setTimeForMeetup(meetupTime);
        uhcGame.setAppleRate(appleRate);
        uhcGame.setCobwebLimit(cobwebLimit);
        uhcGame.setPvp(pvpEnabled);
        uhcGame.setGameStart(gameStarted);
        uhcGame.setCleanItem(cleanEnabled);
        uhcGame.setTeamEnabled(teamsEnabled);
        uhcGame.setOwnTeamsEnabled(ownTeamsEnabled);
        uhcGame.setTeamSize(teamSize);
        uhcGame.getIronmans().addAll(ironmans);
        uhcGame.setIronman(ironman.equals("null") ? null : ironman);
        uhcGame.setPaperman(paperman.equals("null") ? null : paperman);

        var players = json.get("players").getAsJsonArray();

        players.forEach(jsonElement -> {
            var object = jsonElement.getAsJsonObject();

            var uuid = object.get("uuid").getAsString();
            var name = object.get("name").getAsString();
            var ip = object.get("ip").getAsString();
            var kills = object.get("kills").getAsInt();
            var alive = object.get("alive").getAsBoolean();
            var scattered = object.get("scattered").getAsBoolean();

            uhcPlayerRegistry.createPlayer(UUID.fromString(uuid), name, ip);
            uhcGame.getBackupPlayers().add(name);

            var player = uhcPlayerRegistry.getPlayer(name);

            player.setKills(kills);
            player.setAlive(alive);
            player.setScattered(scattered);
        });


        var teams = json.get("teams").getAsJsonArray();

        Map<String, UhcTeam> teamMap = new HashMap<>();
        teams.forEach(jsonElement -> {
            var object = jsonElement.getAsJsonObject();
            var name = object.get("name").getAsString();
            var alive_count = object.get("alive-count").getAsInt();
            var kills = object.get("kills").getAsInt();
            var leader = object.get("leader").getAsString();
            var alive = object.get("alive").getAsBoolean();
            var members = object.get("members").getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList();

            var team = new DefaultUhcTeam(leader, name, NamedTextColor.WHITE, null, false);

            team.setKills(kills);

            members.forEach(m -> {
                team.addMember(m);

                uhcPlayerRegistry.getPlayer(m).setUhcTeam(team);
            });

            team.setAlive(alive);
            team.setPlayersAlive(alive_count);

            teamMap.put(leader, team);
        });

        var scenarios = json.get("scenarios").getAsJsonArray();

        scenarios.forEach(jsonElement -> {
            var s = jsonElement.getAsString();

            var scenarioData = s.split("@");

            var name = scenarioData[0];
            scenarioManager.enableScenario(null, name);

            for (String str : scenarioData) {
                if(str.equalsIgnoreCase(name)) { continue; }

                var str2 = str.split("#");
                var optionName = str2[0];
                var optionValueIndex = Integer.parseInt(str2[1]);

                var scenario = scenarioManager.getScenario(name);
                var option = scenario.getOption(optionName);

                var value = option.options().get(optionValueIndex);
                option.setValue(scenario, (OptionValue) value);

            }
        });

        var settings = json.get("settings").getAsJsonArray();

        settings.forEach(jsonElement -> {
            var s = jsonElement.getAsString();

            settingManager.enableSetting(null, s);
        });

        uhcTeamRegistry.setBackupTeams(teamMap);

        file.delete();

        if(uhcGame.isGameStart()) {
            uhcGameManager.startBackup();

            if(uhcGame.getUhcGameState() == UhcGameState.PVP) {
                uhcGameHandler.changePvp(true);
            }

            if(uhcGame.getUhcGameState() == UhcGameState.MEETUP) {
                uhcGameHandler.changePvp(true);
                uhcGameManager.startMeetup();
            }
        }

        uhcGame.setLoadedBackup(true);
    }
}
