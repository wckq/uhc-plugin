package io.github.wickeddroid.plugin.command;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.command.staff.CommandGame;
import io.github.wickeddroid.plugin.command.staff.CommandTeam;
import io.github.wickeddroid.plugin.command.staff.CommandWorld;
import io.github.wickeddroid.plugin.scenario.ScenarioRegistration;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command(names = "uhc-staff", permission = "uhc.uhc-staff")
@SubCommandClasses({ CommandGame.class, CommandTeam.class, CommandWorld.class })
public class CommandUhcStaff implements CommandClass {
  @Inject private ScenarioRegistration scenarioRegistration;
  @Inject private UhcGame uhcGame;

  @Command(names = {"revive", "ls", "later-scatter"})
  public void revive(final @Sender Player sender, final Player target) {
    for (final var scenario : this.scenarioRegistration.getScenarios().values()) {
      sender.sendMessage(scenario.toString());
    }

    sender.sendMessage(this.scenarioRegistration.getScenarios().size() + "");
  }
}
