package io.github.wickeddroid.plugin.scenario.scenarios;

import io.github.wickeddroid.api.event.game.GameStartEvent;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.game.UhcGameState;
import io.github.wickeddroid.api.player.UhcPlayer;
import io.github.wickeddroid.api.scenario.Scenario;
import io.github.wickeddroid.api.scenario.ScenarioOption;
import io.github.wickeddroid.api.scenario.options.Option;
import io.github.wickeddroid.api.scenario.options.OptionValue;
import io.github.wickeddroid.api.team.UhcTeam;
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
import org.bukkit.Location;
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
import java.util.stream.Collectors;

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
    private LinkedList<OptionValue<Integer>> rangeWarningRangeOption = Option.buildRangedValues(20, 90, 5, "Bloques");

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

            });

            uhcTeamRegistry.getTeams().forEach(uhcTeam -> {
                if(uhcTeam == null) { return; }
                if(!uhcTeam.isAlive()) { return; }

                if(uhcTeam.getMembers().size() >= uhcGame.getTeamSize()) { return; }

                var player = uhcTeam.getMembers().stream().map(s -> uhcPlayerRegistry.getPlayer(s)).toList().stream().filter(uhcPlayer -> uhcPlayer.isAlive() && Bukkit.getOfflinePlayer(uhcPlayer.getName()).isOnline()).findFirst().orElse(null);

                if(player == null) { return; }

                if(rangeWarning) {
                    var teamsNearbyWarning= onRadius(Bukkit.getPlayer(player.getName()), rangeWarningRange);

                    if(!teamsNearbyWarning.isEmpty()) {
                        var nearTeam =  teamsNearbyWarning.get(0);
                        var teamPlayer = nearTeam.getMembers().stream().map(s -> uhcPlayerRegistry.getPlayer(s)).toList().stream().filter(uhcPlayer -> uhcPlayer.isAlive() && Bukkit.getOfflinePlayer(uhcPlayer.getName()).isOnline()).findFirst().orElse(null);
                        var onlinePlayer = Bukkit.getPlayer(player.getName());
                        var distance = onlinePlayer.getLocation().distance(Bukkit.getPlayer(teamPlayer.getName()).getLocation());

                        onlinePlayer.sendActionBar(messageHandler.parse(messages.other().teamsInGamePlayerNearby(), String.valueOf(Math.round(distance))));
                        Bukkit.getPlayer(teamPlayer.getName()).sendActionBar(messageHandler.parse(messages.other().teamsInGamePlayerNearby(), String.valueOf(Math.round(distance))));
                    }
                }

                var teamsNearby = onRadius(Bukkit.getPlayer(player.getName()), range);

                if(teamsNearby.isEmpty()) { return; }

                var nearestTeam = teamsNearby.get(0);

                if(nearestTeam == null) { return; }

                var join = uhcTeamHandler.forceTeamJoin(uhcTeam, nearestTeam);

                if(join) {
                    messageHandler.sendGlobal(messages.other().teamsInGameTeamJoin(), nearestTeam.getName(), uhcTeam.getName());
                }

            });

        }, 1L, delay*20L);

    }

    private void checkGlowing() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            var nearby = onRadius(p, rangeWarningRange);

            var team = uhcTeamRegistry.getTeam(p.getName());

            if(team == null) { return; }

            nearby.forEach(t -> {
                if(team == t) {
                        t.getMembers().forEach(s -> {
                            try {
                                removeGlowing(Bukkit.getPlayer(s), p);
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            } catch (NullPointerException ignored) {}
                        });
                } else {
                    t.getMembers().forEach(s -> {
                        try {
                            addGlowing(Bukkit.getPlayer(s), p);
                        } catch (ReflectiveOperationException e) {
                            throw new RuntimeException(e);
                        } catch (NullPointerException ignored) {}
                    });
                }
            });


            uhcTeamRegistry.getTeams().stream().filter(t -> !nearby.contains(t)).forEach(uhcTeam -> {
                uhcTeam.getMembers().forEach(s -> {
                    try {
                        removeGlowing(Bukkit.getPlayer(s), p);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    } catch (NullPointerException ignored) {}
                });
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

    private List<UhcTeam> onRadius(Player center, double radius) {
        List<UhcTeam> pl = new ArrayList<>();
        var location = center.getLocation();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getGameMode() != GameMode.SURVIVAL){
                continue;
            }

            if(p.getWorld().getEnvironment() != center.getWorld().getEnvironment()) {
                continue;
            }

            if(!p.getWorld().getName().equals(center.getWorld().getName())) {
                continue;
            }

            if(p.getLocation().distance(location) > radius) {
                continue;
            }

            if(p.getName().equals(center.getName())){
                continue;
            }

            var team = uhcTeamRegistry.getTeam(p.getName());

            if(team == null) {
                continue;
            }

            if(team.getMembers().size() >= uhcGame.getTeamSize()) {
                continue;
            }

            if(team.getMembers().contains(center.getName())) {
                continue;
            }

            var uhcTeam = uhcTeamRegistry.getTeam(center.getName());

            if(uhcTeam == null) {
                continue;
            }

            if(team.getMembers().size() + uhcTeam.getMembers().size() > uhcGame.getTeamSize()) {
                continue;
            }

            pl.add(uhcTeamRegistry.getTeam(p.getName()));
        }

        pl.sort(Comparator.comparing(uhcTeam -> uhcTeam.getMembers().stream().map(s -> uhcPlayerRegistry.getPlayer(s)).toList().stream().filter(uhcPlayer -> uhcPlayer.isAlive() && Bukkit.getOfflinePlayer(uhcPlayer.getName()).isOnline()).findFirst().orElse(null), (o1, o2) -> {
            if(o1 == null) { return 1; }
            if(o2 == null) { return -1; }
            var distance1 = Bukkit.getPlayer(o1.getName()).getLocation().distance(location);
            var distance2 = Bukkit.getPlayer(o2.getName()).getLocation().distance(location);

            return Double.compare(distance1, distance2);
        }));

        return pl;
    }

}
