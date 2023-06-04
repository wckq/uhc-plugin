package io.github.wickeddroid.plugin.command;

import io.github.wickeddroid.plugin.command.staff.CommandGame;
import io.github.wickeddroid.plugin.command.staff.CommandTeam;
import io.github.wickeddroid.plugin.command.staff.CommandWorld;
import io.github.wickeddroid.plugin.command.staff.CommandScenario;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;

@Command(names = "uhc-staff", permission = "uhc.uhc-staff")
@SubCommandClasses({ CommandGame.class, CommandTeam.class, CommandWorld.class, CommandScenario.class })
public class CommandUhcStaff implements CommandClass {
}
