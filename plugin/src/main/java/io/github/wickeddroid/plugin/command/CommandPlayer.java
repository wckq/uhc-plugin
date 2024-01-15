package io.github.wickeddroid.plugin.command;

import io.github.wickeddroid.api.game.UhcGame;
import io.github.wickeddroid.plugin.game.UhcGameHandler;
import io.github.wickeddroid.plugin.menu.PlayerInventory;
import io.github.wickeddroid.plugin.menu.scenario.ScenariosEnabledInventory;
import io.github.wickeddroid.plugin.menu.settings.SettingsEnabledInventory;
import io.github.wickeddroid.plugin.menu.settings.SettingsInventory;
import io.github.wickeddroid.plugin.message.MessageHandler;
import io.github.wickeddroid.plugin.message.Messages;
import io.github.wickeddroid.plugin.player.UhcPlayerRegistry;
import io.github.wickeddroid.plugin.scenario.ScenarioManager;
import io.github.wickeddroid.plugin.team.UhcTeamManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.InjectAll;

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

  @Command(names = {"inv", "inventory", "invsee"}, permission = "uhc.inventory")
  public void inventory(final @Sender Player sender, final Player target) {
    if(sender.getGameMode() == GameMode.SPECTATOR) {
      playerInventory.openInv(target, sender);
    }
  }
}
