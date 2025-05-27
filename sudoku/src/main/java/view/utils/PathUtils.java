package view.utils;

import java.nio.file.Paths;

public final class PathUtils {
    public static final String PATH_SOUND = Paths.get("sound").toString();
    public static final String PATH_WINNER_SOUND = Paths.get(PATH_SOUND, "win").toString();

    public static final String PATH_ICON_DARK = Paths.get("icon", "dark").toString();
    public static final String PATH_ICON_LIGHT = Paths.get("icon", "light").toString();

    public static final String ICON_START = Paths.get("logo.png").toString();

    public static String getAbsolutePathOf(final String path) {
        return path.getClass().getClassLoader().getResource(path).getFile();
    }
}


