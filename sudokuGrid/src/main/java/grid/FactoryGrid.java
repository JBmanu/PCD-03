package grid;

public final class FactoryGrid {

    public static Settings settings(final Settings.Schema schema, final Settings.Difficulty difficulty) {
        return new Settings.SettingsImpl(schema, difficulty);
    }

    public static Coordinate coordinate(final int row, final int column) {
        return new Coordinate.CoordinateImpl(row, column);
    }
    
    public static Grid grid(final Settings settings) {
        return new Grid.GridImpl(settings);
    }

}
