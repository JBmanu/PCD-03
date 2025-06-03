package view.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Optional;

public interface Sound {
    static Sound create() {
        return new SimpleSound();
    }
    
    void play();

    void stop();

    void setLoop();

    void setOneShot();

    void setVolume(final float volume);

    void setSound(final Track soundFile);


    class SimpleSound implements Sound {
        private Optional<Clip> clip;
        private final boolean isLoop;
        
        public SimpleSound() {
            this.clip = Optional.empty();
            this.isLoop = false;
        }

        @Override
        public void play() {
            this.clip.ifPresent(Clip::start);
        }

        @Override
        public void stop() {
            this.clip.ifPresent(Clip::stop);
        }

        @Override
        public void setVolume(final float volume) {
            this.clip.ifPresent(clip -> {
                final FloatControl clipControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
                clipControl.setValue(volume);
            });
        }

        @Override
        public void setLoop() {
            this.clip.ifPresent(clip -> clip.loop(Clip.LOOP_CONTINUOUSLY));
        }

        @Override
        public void setOneShot() {
            this.clip.ifPresent(clip -> clip.loop(0));
        }

        @Override
        public void setSound(final Track soundFile) {
            try {
                final Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(ClassLoader.getSystemResource(soundFile.path())));
                this.clip = Optional.of(clip);
            } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                this.clip = Optional.empty();
            }
        }


    }
}
