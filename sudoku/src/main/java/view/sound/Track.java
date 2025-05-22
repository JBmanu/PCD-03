package view.sound;

import java.nio.file.Paths;

public interface Track {
    String SOUND_PATH = Paths.get("sound").toString();
    String WINNER_PATH = Paths.get(SOUND_PATH, "win").toString();
    String RESET_PATH = Paths.get(SOUND_PATH, "reset").toString();
    String SUGGEST_PATH = Paths.get(SOUND_PATH, "suggest").toString();
    String BACKGROUND_PATH = Paths.get(SOUND_PATH, "text").toString();


    String path();

    enum SoundFX implements Track {
        BUTTON_CLICK(Paths.get(SOUND_PATH, "click.wav").toString()),

        SUGGEST(Paths.get(SUGGEST_PATH, "Underwater explosion sound.wav").toString()),

        RESET(Paths.get(RESET_PATH, "Water splash sound.wav").toString()),

        WIN(Paths.get(WINNER_PATH, "Applause.wav").toString()),
        WIN1(Paths.get(WINNER_PATH, "Ba-da-dum by Simon Lacelle.wav").toString()),
        WIN2(Paths.get(WINNER_PATH, "Fanfara medievale.wav").toString()),
        WIN3(Paths.get(WINNER_PATH, "Time Over Deep Voice.wav").toString());

        private final String path;

        SoundFX(final String path) {
            this.path = path;
        }

        public String path() {
            return this.path;
        }
    }

    enum SoundBG implements Track {
        START(Paths.get(BACKGROUND_PATH, "mainSound1.wav").toString()),
        SUDOKU(Paths.get(BACKGROUND_PATH, "mainSound1.wav").toString());

        private final String path;

        SoundBG(final String path) {
            this.path = path;
        }

        public String path() {
            return this.path;
        }
    }
}
