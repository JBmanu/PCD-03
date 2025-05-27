package utils;

import java.util.Map;

public final class ConditionUtils {
    
    public static Map<Boolean, Runnable> createBoolean(final Runnable trueAction, final Runnable falseAction) {
        return Map.of(
                true, trueAction,
                false, falseAction
        );
    }
    
}
