package io.github.wickeddroid.plugin.command.staff;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.menu.settings.SettingsInventory;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.Named;
import team.unnamed.commandflow.annotated.annotation.Sender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command(names = "setting")
public class CommandSetting implements CommandClass {

    @Inject
    private SettingsInventory settingsInventory;
    @Inject
    private UhcGame uhcGame;
    @Inject
    private Messages messages;
    @Inject
    private MessageHandler messageHandler;

    @Command(names = "")
    public void enable(
            final @Sender Player sender
    ) {
        sender.openInventory(settingsInventory.createInventory());
        messageHandler.send(sender, messages.other().getDeprecatedWarning(), "1.4.0-BETA", "/uhcstaff");
    }

    @Command(names = "apple-rate")
    public void appleRate(final @Sender Player sender, final int appleRate) {
        this.uhcGame.setAppleRate(appleRate);
    }

    @Command(names = "final-heal")
    public void finalHeal(final @Sender Player sender, @Named("time") final int time, @Named("amplifier") final int amplifier) {
        if(amplifier < 0 || amplifier > 15) {
            messageHandler.send(sender, messages.staff().invalidAmplifier(), "0", "15");
            return;
        }

        this.uhcGame.setTimeForFinalHeal(time);
        messageHandler.send(sender, messages.staff().changeFinalHealTime(), String.valueOf(time));

        this.uhcGame.setFinalHealAmplifier(amplifier);
        messageHandler.send(sender, messages.staff().changeFinalHealAmplifier(), String.valueOf(amplifier));
    }

    @Command(names = "final-resistance")
    public void finalResistance(final @Sender Player sender,  @Named("time") final int time, @Named("amplifier") final int amplifier) {
        if(amplifier < 0 || amplifier > 5) {
            messageHandler.send(sender, messages.staff().invalidAmplifier(), "0", "5");
            return;
        }

        this.uhcGame.setTimeForFinalResistance(time);
        messageHandler.send(sender, messages.staff().changeFinalResistanceTime(), String.valueOf(time));

        this.uhcGame.setFinalResistanceAmplifier(amplifier);
        messageHandler.send(sender, messages.staff().changeFinalResistanceAmplifier(), String.valueOf(amplifier));
    }
}