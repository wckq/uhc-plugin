package io.github.wickeddroid.plugin.message;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@SuppressWarnings("")
@ConfigSerializable
public class Messages {

  private String prefix = "<gradient:#FEE679:#E5BF18>UHC ➣";
  private Team team = new Team();
  private Other other = new Other();
  private Game game = new Game();
  private Staff staff = new Staff();

  public @NonNull Team team() {
    return this.team;
  }

  public @NonNull Other other() {
    return this.other;
  }

  public @NonNull Game game() {
    return this.game;
  }

  public @NonNull Staff staff() {
    return this.staff;
  }

  public @NonNull String prefix() {
    return this.prefix;
  }

  @ConfigSerializable
  public static class Game {
    private String hasStarted = "El juego ya ha empezado.";
    private String ironmanLost = "<gold>El jugador <param-player> ya no puede ser ironman. Quedan <green><param-ironmanleft><gold> ironman.";
    private String papermanPlayer = "<gold>El jugador <param-player> es el <red>Paperman <gold>de la partida.";
    private String ironmanPlayer = "<gold>El jugador <param-player> es el <green>Ironman <gold>de la partida.";

    public @NonNull String hasStarted() {
      return this.hasStarted;
    }
    public @NonNull String ironmanLost() { return this.ironmanLost; }
    public @NonNull String papermanPlayer() { return this.papermanPlayer; }
    public @NonNull String ironmanPlayer() { return this.ironmanPlayer; }
  }

  @ConfigSerializable
  public static class Team {
    private String create = "Tu equipo se ha creado correctamente con el nombre <color:#93FF9E><param-name>.";
    private String remove = "Se ha disuelto el equipo correctamente.";
    private String join = "Te has unido al equipo <color:#93FF9E><param-name>";
    private String joinPlayer = "Se ha unido el jugador <color:#93FF9E><param-player>";
    private String leave = "Te has salido del equipo <color:#93FF9E><param-name>";
    private String leavePlayer = "El jugador <color:#93FF9E><param-player> <white>ha abandonado tu equipo.";
    private String leaveAsLeader = "No puedes abandonar tu propio equipo. Usa /team disband o /team promote";
    private String inviterEqualsPlayer = "No puedes invitarte a ti mismo.";
    private String leaderAsMember = "No eres el lider del equipo.";
    private String alreadyExists = "Ya te encuentras en un equipo.";
    private String doesNotExist = "No te encuentras en un equipo.";
    private String playerTeamExists = "El jugador ya se encuentra en un equipo.";
    private String playerDoesNotTeamExist = "El jugador no se encuentra en un equipo.";
    private String leaderInvitePlayer = "El jugador <color:#93FF9E><param-player> <white>ha sido invitado correctamente.";
    private String invitePlayer = "El jugador <color:#93FF9E><param-leader> <white>te ha invitado a su equipo. <gold><bold> <click:run_command:/team accept <param-leader>>¡Click aquí para unirte!</click> <reset>o utiliza <gold>/team accept <param-leader>";
    private String inviteDoesNotExist = "No se te ha invitado al equipo <color:#93FF9E><param-name>";
    private String leaderCancelInvite = "Se ha cancelado la invitación del jugador <color:#93FF9E><param-player>";
    private String cancelInvite = "Se ha cancelado la invitación del equipo <color:#93FF9E><param-name>.";
    private String enableTeam = "Se han activado los equipos.";
    private String disableTeam = "Se han desactivado los equipos.";
    private String teamHasNotEnabled = "Los equipos se encuentran desactivados.";
    private String ownTeamsHasNotEnabled = "Los equipos propios se encuentran desactivados.";
    private String changeTeamSize = "Se ha cambiado el tamaño de equipos a <color:#93FF9E><param-size>";
    private String changeTeamNameError = "No se pueden realizar cambios a los nombres de los equipos.";
    private String changeTeamPrefixError = "No se pueden realizar cambios al prefix de los equipos.";
    private String settingChanged = "Has cambiado la configuración a \"<param-value>\"";
    private String maxLengthReached = "Has sobrepasado la cantidad máxima de <param-length> caracteres.";
    private String sendCoords = "<gray>Estoy en <bold>X: <red><param-coordx> <gray><bold>Y: <red><param-coordy> <gray><bold>Z: <red><param-coordz>";

    public @NonNull String create() {
      return this.create;
    }

    public @NonNull String remove() {
      return this.remove;
    }

    public @NonNull String join() {
      return this.join;
    }

    public @NonNull String joinPlayer() {
      return this.joinPlayer;
    }

    public @NonNull String inviteDoesNotExist() {
      return this.inviteDoesNotExist;
    }

    public @NonNull String leaderAsMember() {
      return this.leaderAsMember;
    }

    public @NonNull String alreadyExists() {
      return this.alreadyExists;
    }

    public @NonNull String leave() {
      return this.leave;
    }

    public @NonNull String leavePlayer() {
      return this.leavePlayer;
    }

    public @NonNull String leaveAsLeader() {
      return this.leaveAsLeader;
    }

    public @NonNull String cancelInvite() {
      return this.cancelInvite;
    }

    public @NotNull String leaderCancelInvite() {
      return this.leaderCancelInvite;
    }

    public @NonNull String invitePlayer() {
      return this.invitePlayer;
    }

    public @NonNull String leaderInvitePlayer() {
      return this.leaderInvitePlayer;
    }

    public @NonNull String doesNotExist() {
      return this.doesNotExist;
    }

    public @NonNull String playerTeamExist() {
      return this.playerTeamExists;
    }

    public @NonNull String playerDoesNotTeamExist() {
      return this.playerDoesNotTeamExist;
    }

    public @NonNull String inviterEqualsPlayer() {
      return this.inviterEqualsPlayer;
    }

    public @NonNull String teamHasNotEnabled() {
      return this.teamHasNotEnabled;
    }

    public @NonNull String ownTeamsHasNotEnabled() {
      return ownTeamsHasNotEnabled;
    }

    public @NonNull String changeTeamSize() {
      return this.changeTeamSize;
    }

    public @NonNull String enableTeam() {
      return this.enableTeam;
    }

    public @NonNull String disableTeam() {
      return this.disableTeam;
    }

    public @NonNull String changeTeamNameError() { return this.changeTeamNameError; }

    public @NonNull String changeTeamPrefixError() { return this.changeTeamPrefixError; }
    public @NonNull String settingChanged() { return this.settingChanged; }
    public @NonNull String maxLengthReached() { return this.maxLengthReached; }
    public @NonNull String sendCoords() { return this.sendCoords; }
  }

  @ConfigSerializable
  public static class Staff {
    private String changePvpTime = "Has cambiado el tiempo que comience el pvp a <param-time>";
    private String changeMeetupTime = "Has cambiado el tiempo que comience el meetup a <param-time>";
    private String invalidTime = "El tiempo que has dado no es valido";

    public @NonNull String changeMeetupTime() {
      return this.changeMeetupTime;
    }

    public @NonNull String changePvpTime() {
      return this.changePvpTime;
    }

    public @NonNull String invalidTime() {
      return invalidTime;
    }
  }

  @ConfigSerializable
  public static class Other {
    private String teamChatOn = "Te has cambiado al chat de equipo.";
    private String teamChatOff = "Te has cambiado al chat global.";
    private String scattered = "<param-player> has scattered";
    private String gameHasStarted = "El juego ya ha iniciado";
    private String scenarioNotExists = "El scenario <color:#93FF9E><param-name> <white>no existe.";
    private String scenarioEnabled = "El scenario <color:#93FF9E><param-name> <white>ha sido activado correctamente,";
    private String scenarioDisabled = "El scenario <color:#93FF9E><param-name> <white>ha sido desactivado correctamente";

    public @NonNull String teamChatOn() {
      return this.teamChatOn;
    }

    public @NonNull String teamChatOff() {
      return this.teamChatOff;
    }

    public @NonNull String gameHasStarted() {
      return this.gameHasStarted;
    }

    public @NonNull String scattered() {
      return this.scattered;
    }

    public @NonNull String scenarioEnabled() {
      return this.scenarioEnabled;
    }

    public @NonNull String scenarioDisabled() {
      return this.scenarioDisabled;
    }

    public @NonNull String scenarioNotExists() {
      return this.scenarioNotExists;
    }
  }
}
