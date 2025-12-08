package ui.multiPlayer.panel;

import ui.color.Palette;
import ui.components.ColorComponent;
import ui.utils.PanelUtils;
import ui.utils.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static ui.utils.StyleUtils.*;

public class GameInfoPanel extends JPanel implements ColorComponent, InfoPanel {
    public static final String ROOM_LABEL = "ROOM ID: ";
    public static final String SELF = "You";
    private final JLabel infoRoom;
    private final JLabel infoLabel;
    private final JTextArea playerArea;

    public GameInfoPanel() {
        super(new BorderLayout());
        PanelUtils.transparent(this);

        this.infoRoom = new JLabel();
        this.infoLabel = new JLabel();
        this.playerArea = new JTextArea("");
        final JScrollPane scrollPane = new JScrollPane(this.playerArea);

        this.infoRoom.setFont(StyleUtils.CELL_FONT);
        this.playerArea.setFont(StyleUtils.INFO_FONT);
        this.playerArea.setEditable(false);
        this.playerArea.setOpaque(false);
        this.playerArea.setBorder(null);
        this.playerArea.getCaret().setVisible(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(V_GAP, ZERO_GAP, ZERO_GAP, ZERO_GAP));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        this.setBorder(BorderFactory.createEmptyBorder(V_GAP, H_GAP + H_GAP, V_GAP, H_GAP + H_GAP));

        this.add(PanelUtils.createCenter(this.infoRoom), BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(PanelUtils.createCenter(this.infoLabel), BorderLayout.SOUTH);
    }

    public void buildRoom(final String roomId) {
        this.infoRoom.setText(ROOM_LABEL + roomId);
        this.playerArea.setText(SELF);
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
        this.playerArea.revalidate();
        this.playerArea.repaint();
    }

    @Override
    public void showInfo(final String info) {
        this.infoLabel.setForeground(Color.BLACK);
        this.infoLabel.setText(info);
    }

    @Override
    public void showError(final String error) {
        this.infoLabel.setForeground(Color.RED);
        this.infoLabel.setText(error);
    }

    @Override
    public void refreshPalette(final Palette palette) {
        final int alphaTitle = 230;
        final int alphaPlayer = alphaTitle - 50;
        this.infoRoom.setForeground(palette.secondaryWithAlpha(alphaTitle));
        this.playerArea.setForeground(palette.secondaryWithAlpha(alphaPlayer));
        this.infoLabel.setForeground(palette.secondary());
    }

}
