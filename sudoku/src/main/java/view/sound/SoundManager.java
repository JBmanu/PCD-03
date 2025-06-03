package view.sound;

import java.util.List;

public interface SoundManager {
    static SoundManager createBackground() {
        return new BackgroundManager();
    }
    
    static SoundManager createEffect() {
        return new EffectManager();
    }
    
    void playSound(final Track track);

    void playRandomSound(final List<Track> tracks);

    class BackgroundManager implements SoundManager {
        private final Sound backgroundSoundFX;

        public BackgroundManager() {
            this.backgroundSoundFX = new Sound.SimpleSound();
        }

        @Override
        public void playSound(final Track track) {
            this.backgroundSoundFX.stop();
            this.backgroundSoundFX.setSound(track);
            this.backgroundSoundFX.setLoop();
            this.backgroundSoundFX.play();
        }

        @Override
        public void playRandomSound(final List<Track> tracks) {
            final Track soundFX = tracks.get((int) (Math.random() * tracks.size()));
            this.playSound(soundFX);
        }
    }
    
    class EffectManager implements SoundManager {
        @Override
        public void playSound(final Track track) {
            final Sound sound = new Sound.SimpleSound();
            sound.setSound(track);
            sound.play();
        }

        @Override
        public void playRandomSound(final List<Track> tracks) {
            final Track soundFX = tracks.get((int) (Math.random() * tracks.size()));
            this.playSound(soundFX);
        }

    }
}
