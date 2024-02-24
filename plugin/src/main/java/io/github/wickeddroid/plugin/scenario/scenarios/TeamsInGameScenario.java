package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.ScenarioOption;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.util.glowing.GlowingEntities;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scenario.ListenerScenario;
import io.github.wickeddroid.plugin.scenario.RegisteredScenario;
import io.github.wickeddroid.plugin.team.UhcTeamHandler;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import io.github.wickeddroid.plugin.team.UhcTeamRegistry;
import io.github.wickeddroid.plugin.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Inject;

import java.io.IOException;
import java.util.*;

@RegisteredScenario
@Scenario(
        name = "Teams In Game",
        key = "tig",
        description = {
                "<gray>- Los equipos se forman en la partida.",
                "<dark_red>   » Requiere que los jugadores esten con teams FFA",
                "<dark_red>   » Requiere que al iniciar el Team Size no sea 1"
        },
        material = Material.BEACON,
        supportsOptions = true
)
public class TeamsInGameScenario extends ListenerScenario {

    @Inject
    private UhcTeamRegistry uhcTeamRegistry;
    @Inject
    private UhcPlayerRegistry uhcPlayerRegistry;
    @Inject
    private UhcTeamManager uhcTeamManager;
    @Inject
    private UhcGame uhcGame;
    @Inject
    private UhcTeamHandler uhcTeamHandler;
    @Inject
    private Plugin plugin;
    @Inject
    private Messages messages;
    @Inject
    private MessageHandler messageHandler;
    @Inject
    private GlowingEntities glowingEntities;

    private Map<Player, List<Player>> glowingList = new HashMap<>();

    @ScenarioOption(optionName = "Distancia de Unión", dynamicValue = "range")
    private LinkedList<OptionValue<Integer>> rangeOption = Option.buildRangedValues(3, 15, "Bloques");

    private Integer range;

    @ScenarioOption(optionName = "Advertencia a Distancia", dynamicValue = "rangeWarning")
    private LinkedList<OptionValue<Boolean>> rangeWarningOption = Option.buildBooleanValues(true);

    private Boolean rangeWarning;

    @ScenarioOption(optionName = "Bloques de Advertencia", dynamicValue = "rangeWarningRange")
    private LinkedList<OptionValue<Integer>> rangeWarningRangeOption = Option.buildRangedValues(5, 75, 5, "Bloques");

    private Integer rangeWarningRange;

    @ScenarioOption(optionName = "Advertencia con Glowing", dynamicValue = "glowing")
    private LinkedList<OptionValue<Boolean>> glowingOption = Option.buildBooleanValues(false);

    private Boolean glowing;


    /*@ScenarioOption(optionName = "Forzar Unión a Equipo", dynamicValue = "forceJoin")
    private LinkedList<OptionValue<Boolean>> forceJoinOption = Option.buildBooleanValues(true);

    private Boolean forceJoin;

    @ScenarioOption(optionName = "Tiempo de Unión Forzosa", dynamicValue = "forceJoinTime")
    private LinkedList<OptionValue<Integer>> forceJoinTimeOption = Option.buildRangedValues(0, 300, 5, "Minutos");

    private Integer forceJoinTime;*/

    @ScenarioOption(optionName = "Delay de Comprobación de Jugadores", dynamicValue = "delay")
    private LinkedList<OptionValue<Integer>> delayOption = Option.buildRangedValues(2, 7, "Segundos");

    private Integer delay;

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
            if(!this.isEnabled()) { return; }
            if(uhcGame.getUhcGameState() == UhcGameState.WAITING || uhcGame.getUhcGameState() == UhcGameState.FINISH) { return; }

            checkGlowing();

            Bukkit.getOnlinePlayers().forEach(player -> {
                if(player.getGameMode() != GameMode.SURVIVAL) { return; }

                var team = uhcTeamRegistry.getTeam(player.getName());

                if(team == null) { return; }
                
                if(team.getMembers().size() != 1) { return; }

                var uhcPlayer = uhcPlayerRegistry.getPlayer(player.getName());

                if(uhcPlayer == null) { return; }

                if(rangeWarning) {
                    var playersNearbyWarning = player.getNearbyEntities(rangeWarningRange, rangeWarningRange, range).stream().filter(e -> e.getType() == EntityType.PLAYER && !team.getMembers().contains(e.getName())).map(e -> (Player)e).filter(p -> p.getGameMode() == GameMode.SURVIVAL).toList();

                    if(!playersNearbyWarning.isEmpty()) {
                        playersNearbyWarning.forEach(p -> {
                            var distance = p.getLocation().distance(player.getLocation());

                            p.sendActionBar(messageHandler.parse(messages.other().teamsInGamePlayerNearby(), String.valueOf(Math.round(distance))));
                        });

                        var distance = player.getLocation().distance(playersNearbyWarning.get(0).getLocation());

                        player.sendActionBar(messageHandler.parse(messages.other().teamsInGamePlayerNearby(), String.valueOf(Math.round(distance))));
                    }
                }

                var playersNearby = player.getNearbyEntities(range, 2.65D, range).stream().filter(e -> e.getType() == EntityType.PLAYER && !team.getMembers().contains(e.getName())).map(e -> (Player)e).filter(p -> p.getGameMode() == GameMode.SURVIVAL).toList();

                if(playersNearby.isEmpty()) { return; }

                playersNearby.forEach(player2 -> {
                    var uhcPlayer2 = uhcPlayerRegistry.getPlayer(player2.getName());
                    if(uhcPlayer2 == null) { return; }

                    if(team.getMembers().size() < uhcGame.getTeamSize()) {
                        var join = uhcTeamHandler.forcePlayerToTeam(player, player2);

                        if(join) {
                            messageHandler.sendGlobal(messages.other().teamsInGameTeamJoin(), player2.getName(), player.getName());
                        }
                    }
                });
            });

        }, 1L, delay*20L);

    }

    private void checkGlowing() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            var nearby = p.getNearbyEntities(rangeWarningRange, rangeWarningRange, range).stream().filter(e -> e.getType() == EntityType.PLAYER).map(e -> (Player)e).filter(pl -> pl.getGameMode() == GameMode.SURVIVAL).toList();

            var team = uhcTeamRegistry.getTeam(p.getName());

            if(team == null) { return; }

            nearby.forEach(n -> {
                if(team.getMembers().contains(n.getName())) {
                    try {
                        removeGlowing(n, p);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        addGlowing(n, p);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


            Bukkit.getOnlinePlayers().stream().filter(o -> !nearby.contains(o) && o.getUniqueId() != p.getUniqueId()).forEach(viewer -> {
                try {
                    removeGlowing(viewer, p);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
        });

    }

    private List<Player> getList(Player player) {
        if(!glowingList.containsKey(player) || glowingList.get(player) == null) {
            glowingList.put(player, new ArrayList<>());
        }

        return glowingList.get(player);
    }

    private void addGlowing(Player viewer, Player victim) throws ReflectiveOperationException {
        var viewerList = getList(viewer);


        if(!viewerList.contains(victim)) {
            glowingEntities.setGlowing(victim, viewer);
            viewerList.add(victim);
        }

        var victimList = getList(victim);

        if(!victimList.contains(viewer)) {
            glowingEntities.setGlowing(viewer, victim);
            victimList.add(viewer);
        }
    }

    private void removeGlowing(Player viewer, Player victim) throws ReflectiveOperationException {
        var viewerList = getList(viewer);

        if(viewerList.contains(victim)) {
            glowingEntities.unsetGlowing(victim, viewer);
            viewerList.remove(victim);
        }

        var victimList = getList(victim);

        if(victimList.contains(viewer)) {
            glowingEntities.unsetGlowing(viewer, victim);
            victimList.remove(viewer);
        }
    }

}
