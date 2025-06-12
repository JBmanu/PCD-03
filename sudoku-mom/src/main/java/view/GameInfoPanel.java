package view;

import model.Player;
import ui.utils.PanelUtils;
import ui.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameInfoPanel extends JPanel {
    public static final String ROOM_LABEL = "ROOM ID: ";
    public static final String SELF = "You";
    final JLabel infoRoom;
    final JTextArea playerArea;

    public GameInfoPanel() {
        super(new BorderLayout());
        PanelUtils.transparent(this);

        this.infoRoom = new JLabel();
        this.playerArea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(this.playerArea);

        this.infoRoom.setFont(StyleUtils.CELL_FONT);
        this.playerArea.setEditable(false);
        this.playerArea.setFont(StyleUtils.INFO_FONT);

        this.add(this.infoRoom, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void build(final Player player) {
        player.computeRoomID().ifPresent(id -> this.infoRoom.setText(ROOM_LABEL + id));
        this.playerArea.append(SELF);
    }

    public void joinPlayer(final String playerName) {
        this.playerArea.append("\n" + playerName);
    }

    public void appendPlayers(final List<String> players) {
        players.forEach(this::joinPlayer);
    }
    
    public static void main(final String[] args) {
        JFrame frame = new JFrame();
        GameInfoPanel gameInfoPanel = new GameInfoPanel();
        frame.getContentPane().add(gameInfoPanel, BorderLayout.CENTER);
        
        frame.setVisible(true);

        Player player = Player.create();
        player.computeToCreateRoom("1", "1", "testPlayer");
        gameInfoPanel.build(player);
        gameInfoPanel.appendPlayers(List.of("manu", "lu", "luca"));
    }
}
