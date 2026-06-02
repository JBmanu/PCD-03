package ui.multiPlayer.panel;

import grid.Settings;
import ui.color.Palette;
import ui.components.ColorComponent;
import ui.utils.PanelUtils;
import ui.utils.StyleUtils;
import utils.Pair;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.List;

import static ui.utils.StyleUtils.*;

public class GameInfoPanel extends JPanel implements ColorComponent, InfoPanel {
    public static final String ROOM_LABEL = "ROOM ID: ";
    public static final String SELF = "You";
    private final JLabel infoRoom;
    private final JLabel infoLabel;
    private final JTextPane playerArea; 

    public GameInfoPanel() {
        super(new BorderLayout());
        PanelUtils.transparent(this);

        this.infoRoom = new JLabel();
        this.infoLabel = new JLabel();
        this.playerArea = new JTextPane();
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

    // Metodo helper per inserire testo colorato
    private void appendColored(final String text, final Color color) {
        final StyledDocument doc = this.playerArea.getStyledDocument();
        final Style style = this.playerArea.addStyle("style", null);
        StyleConstants.setForeground(style, color);
        StyleConstants.setFontFamily(style, StyleUtils.INFO_FONT.getFamily());
        StyleConstants.setFontSize(style, StyleUtils.INFO_FONT.getSize());
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (final BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void buildRoom(final String roomId, final String playerName, final Settings settings) {
        this.infoRoom.setText("<html><center>" + ROOM_LABEL + ": " + roomId + "<br>" +
                settings.difficulty() + "(" + settings.schema() + ") </center></html>");
        this.playerArea.setText("");
        this.appendColored(SELF + " (" + playerName + ")", Color.black);
    }

    public void joinPlayer(final String playerName, final Color color) {
        this.appendColored("\n" + playerName, color);
    }

    public void leavePlayer(final String playerName) {
        final StyledDocument doc = this.playerArea.getStyledDocument();
        final String text = this.playerArea.getText();
        final String toRemove = "\n" + playerName;
        final int index = text.indexOf(toRemove);
        if (index >= 0) {
            try {
                doc.remove(index, toRemove.length());
            } catch (final BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public void appendPlayers(final List<Pair<String, Color>> playersColors) {
        playersColors.forEach(playerColor -> this.joinPlayer(playerColor.first(), playerColor.second()));
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
        this.infoRoom.setForeground(palette.secondaryWithAlpha(alphaTitle));
        // playerArea non ha più un colore globale, ogni nome ha il suo
        this.infoLabel.setForeground(palette.secondary());
    }
}