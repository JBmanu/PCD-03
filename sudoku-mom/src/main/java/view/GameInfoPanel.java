package view;

import model.Player;
import ui.utils.PanelUtils;
import ui.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameInfoPanel extends JPanel {
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
        player.computeRoomID().ifPresent(id -> this.infoRoom.setText("ROOM ID: " + id));
        this.playerArea.append("Tu");
    }

    public void joinPlayer(final String playerName) {
        this.playerArea.append("\n" + playerName);
    }

    public void refresh(final List<String> players) {
        players.forEach(this.playerArea::append);
    }
}
