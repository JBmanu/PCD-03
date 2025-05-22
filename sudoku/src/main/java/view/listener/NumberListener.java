package view.listener;

import java.util.Optional;

public interface NumberListener {
    
    void onSelectNumber(final Optional<Integer> number);
    
    void onFocusNumber(final Optional<Integer> number);
    
    void onHoverNumber(final Optional<Integer> number);
    
}
