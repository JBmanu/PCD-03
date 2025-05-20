package model.settings;

public enum Difficulty {
    EASY(30),
    MEDIUM(40),
    HARD(50);

    private final int percent;

    Difficulty(final int percent) {
        this.percent = percent;
    }

    public int percent() {
        return this.percent;
    }
}
