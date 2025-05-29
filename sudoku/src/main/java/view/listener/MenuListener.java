package view.listener;

import model.Settings;

public interface MenuListener {
    
    void onStart(final Settings.Schema schema, final Settings.Difficulty difficulty);
    
    void onExit();
    
    void onLightMode();
    
    void onDarkMode();
    
}
