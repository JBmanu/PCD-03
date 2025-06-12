package view;

import model.Player;
import ui.utils.PanelUtils;
import ui.utils.StyleUtils;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class GameInfoPanel extends JPanel {
    final JLabel infoRoom;
    final JTextArea playerArea;

    public GameInfoPanel() {
        super(new BorderLayout());
        PanelUtils.transparent(this);

        this.infoRoom = new JLabel();
        this.playerArea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(this.playerArea);

        this.playerArea.setEditable(false);
        this.playerArea.setFont(StyleUtils.INFO_FONT);

        this.add(this.infoRoom, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void build(final Player player) {
        this.infoRoom.setText("ROOM ID: " + player.computeRoomID());
        this.playerArea.append("Tu");
    }
    
    public void joinPlayer(final String playerName) {
        this.playerArea.append("\n" + playerName);
    }
    
    public void refresh(final List<String> players) {
        players.forEach(this.playerArea::append);
    }
}
