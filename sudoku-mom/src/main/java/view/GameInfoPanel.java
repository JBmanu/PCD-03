package view;

import model.Player;
import ui.utils.PanelUtils;
import ui.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static ui.utils.StyleUtils.*;

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
        this.playerArea.setFont(StyleUtils.INFO_FONT);
        this.playerArea.setEditable(false);
        this.playerArea.setOpaque(false);
        this.playerArea.setBorder(null);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(V_GAP, ZERO_GAP, ZERO_GAP, ZERO_GAP));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        this.setBorder(BorderFactory.createEmptyBorder(V_GAP + V_GAP, H_GAP + H_GAP, V_GAP + V_GAP, H_GAP + H_GAP));

        this.add(PanelUtils.createCenter(this.infoRoom), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void build(final Player player) {
        player.computeRoomID().ifPresent(id -> this.infoRoom.setText(ROOM_LABEL + id));
        this.playerArea.append(SELF);
    }

    public void joinPlayer(final String playerName) {
        this.playerArea.append("\n" + playerName);
    }

    public void leavePlayer(final String playerName) {
        final String text = this.playerArea.getText();
        this.playerArea.setText(text.replace("\n" + playerName, ""));
    }

    public void appendPlayers(final List<String> players) {
        players.forEach(this::joinPlayer);
    }
}
