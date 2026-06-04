package road;

import simulation.engineseq.Action;

/**
 * Car agent move forward action
 */
public record MoveForward(double distance) implements Action {}
