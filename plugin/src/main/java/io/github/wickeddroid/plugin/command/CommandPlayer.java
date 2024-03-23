package io.github.wickeddroid.plugin.command;

import com.google.common.collect.Lists;
import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.api.player.UhcPlayer;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import io.github.wickeddroid.plugin.menu.PlayerInventory;
import io.github.wickeddroid.plugin.menu.scenario.ScenariosEnabledInventory;
import io.github.wickeddroid.plugin.menu.settings.SettingsEnabledInventory;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scenario.ScenarioManager;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import team.unnamed.commandflow.annotated.CommandClass;
import team.unnamed.commandflow.annotated.annotation.Command;
import team.unnamed.commandflow.annotated.annotation.Named;
import team.unnamed.commandflow.annotated.annotation.OptArg;
import team.unnamed.commandflow.annotated.annotation.Sender;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.InjectAll;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@InjectAll
public class CommandPlayer implements CommandClass {

  private ScenariosEnabledInventory scenariosInventory;
  private UhcPlayerRegistry uhcPlayerRegistry;
  private ScenarioManager scenarioManager;
  private UhcGameHandler uhcGameHandler;
  private UhcTeamManager uhcTeamManager;
  private MessageHandler messageHandler;
  private Messages messages;
  private UhcGame uhcGame;
  private PlayerInventory playerInventory;
  private SettingsEnabledInventory settingsEnabledInventory;

  @Command(names = {"teamchat", "tc", "chat", "c"})
  public void teamChat(final @Sender Player sender) {
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(sender.getName());

    if (uhcPlayer == null) {
      return;
    }

    if (this.uhcTeamManager.getTeamByPlayer(uhcPlayer.getName()) == null) {
      this.messageHandler.send(sender, this.messages.team().doesNotExist());
      return;
    }

    uhcPlayer.setTeamChat(!uhcPlayer.isTeamChat());

    this.messageHandler.send(sender, uhcPlayer.isTeamChat()
            ? this.messages.other().teamChatOn()
            : this.messages.other().teamChatOff());
  }

  @Command(names = {"coords", "c", "location"})
  public void coords(final @Sender Player sender) {
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(sender.getName());

    if (uhcPlayer == null) {
      return;
    }

    this.uhcTeamManager.getTeamByPlayer(uhcPlayer.getName());
    if (this.uhcTeamManager.getTeamByPlayer(uhcPlayer.getName()) == null) {
      this.messageHandler.send(sender, this.messages.team().doesNotExist());
      return;
    }

    var location = sender.getLocation();

    uhcTeamManager.sendMessageTeam(sender, messageHandler.parse(messages.team().sendCoords(),
            String.valueOf(Math.round(location.getX())),
            String.valueOf(Math.round(location.getY())),
            String.valueOf(Math.round(location.getZ()))
    ));
  }

  @Command(names = {"ti", "teaminv", "teaminventory"})
  public void teamInventory(final @Sender Player sender) {
    final var uhcPlayer = this.uhcPlayerRegistry.getPlayer(sender.getName());

    if (uhcPlayer == null) {
      return;
    }

    this.uhcTeamManager.getTeamByPlayer(uhcPlayer.getName());
    if (this.uhcTeamManager.getTeamByPlayer(uhcPlayer.getName()) == null) {
      this.messageHandler.send(sender, this.messages.team().doesNotExist());
      return;
    }

    if(!this.uhcGame.isTeamInventory()) {
      this.messageHandler.send(sender, this.messages.team().teamInventoryNotEnabled());
      return;
    }

    sender.openInventory(this.uhcTeamManager.getTeamByPlayer(uhcPlayer.getName()).getTeamInventory());
  }

  @Command(names = { "kt", "killstop" })
  public void killsTop(@Sender Player sender, @OptArg() @Named("count") Integer count) {
    var requested = count == null ? -1 : count;

    if(requested > uhcPlayerRegistry.getPlayers().size()) {
      messageHandler.send(sender, messages.other().invalidCount());
      return;
    }

    List<UhcPlayer> killsSorted = new ArrayList<>(uhcPlayerRegistry.getPlayers());
    StringBuilder builder = new StringBuilder();

    killsSorted.sort(Comparator.comparing(UhcPlayer::getKills).reversed());
    AtomicInteger i = new AtomicInteger(1);


    killsSorted.forEach(uhcPlayer -> {
      if(requested > 0 && i.get() == requested) { return; }

      builder.append("  <gray><bold>").append(i.get()).append(" ").append(uhcPlayer.getName()).append(" » ").append(uhcPlayer.getKills()).append(" Kills<br>");
      i.getAndIncrement();
    });

    messageHandler.send(sender, messages.other().killsTop(), builder.toString());
  }
  @Command(names = "cleanitem")
  public void cleanItem(@Sender Player sender) {
    final var inventory = sender.getInventory();
    final var item = inventory.getItemInMainHand();

    if (!this.uhcGame.isCleanItem()) {
      this.messageHandler.send(sender, this.messages.other().cleanItemDisabled());
      return;
    }

    if (item.getType() != Material.AIR) {
      if (item.getEnchantments().size() > 0 || item.getType() == Material.ENCHANTED_BOOK) {
        inventory.removeItem(inventory.getItemInMainHand());

        item.getEnchantments().forEach((enchantment, integer) -> item.removeEnchantment(enchantment));

        inventory.addItem((item.getType() == Material.ENCHANTED_BOOK) ?
                new ItemStack(Material.BOOK) :
                item);

        sender.playSound(sender.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 1.0F, 1.0F);
      }
    }
  }

  @Command(names = "scenarios")
  public void scenarios(final @Sender Player sender) {
    sender.openInventory(scenariosInventory.createInventory());
  }

  @Command(names = "settings")
  public void settings(final @Sender Player sender) {
    sender.openInventory(settingsEnabledInventory.createInventory());
  }

  @Command(names = {"inv", "inventory", "invsee"}, permission = "uhc.staff.inventory")
  public void inventory(final @Sender Player sender, final Player target) {
    if(sender.getGameMode() == GameMode.SPECTATOR) {
      playerInventory.openInv(target, sender);
    }
  }
}
