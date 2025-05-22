package view.sound;

import java.util.List;

public interface SoundManager {
    void playBackgroundSound(final Track soundBG);

    void playSoundFX(final Track soundFX);

    void playRandomSoundFX(final List<Track> soundFXs);

    class SimpleManager implements SoundManager {
        private final Sound backgroundSoundFX;

        public SimpleManager() {
            this.backgroundSoundFX = new Sound.SimpleSound();
            this.backgroundSoundFX.setLoop();
        }

        @Override
        public void playBackgroundSound(final Track soundBG) {
            this.backgroundSoundFX.stop();
            this.backgroundSoundFX.setSound(soundBG);
            this.backgroundSoundFX.play();
        }

        @Override
        public void playSoundFX(final Track soundFX) {
            final Sound sound = new Sound.SimpleSound();
            sound.setSound(soundFX);
            sound.play();
        }

        @Override
        public void playRandomSoundFX(final List<Track> soundFXs) {
            final Track soundFX = soundFXs.get((int) (Math.random() * soundFXs.size()));
            this.playSoundFX(soundFX);
        }

    }
}
