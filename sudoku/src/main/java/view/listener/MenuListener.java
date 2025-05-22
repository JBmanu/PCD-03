package view.listener;

public interface MenuListener {
    
    void onStart(final String schema, final String difficulty);
    
    void onExit();
    
    void onLightMode();
    
    void onDarkMode();
    
}
