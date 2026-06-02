package view.inspector.setting;

import simulation.SimulationManager;
import view.ViewUtils;
import view.inspector.StepperView;

import javax.swing.*;
import java.awt.*;

public class StartStopView extends JPanel {
    private static final String START = "Start";
    private static final String PAUSE = "Pause";
    private static final String RESUME = "Resume";
    public static final String STOP = "Stop";

    private final JButton startButton;
    private final JButton pauseResumeButton;
    private final JButton stopButton;
    private final SimulationManager manager;
    private StepperView stepperView;

    public StartStopView(final SimulationManager manager) {
        this.manager = manager;
        this.startButton = new JButton(START);
        this.pauseResumeButton = new JButton(PAUSE);
        this.stopButton = new JButton(STOP);

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(ViewUtils.GUI_BACKGROUND_COLOR);

        this.startButton.setBackground(Color.green);
        this.pauseResumeButton.setBackground(Color.yellow);
        this.stopButton.setBackground(Color.red);

        this.add(this.startButton);
        this.add(this.pauseResumeButton);
        this.add(this.stopButton);

        this.setupListeners();
        this.onIdle();
    }

    public void setStepperView(final StepperView stepperView) {
        this.stepperView = stepperView;
    }

    private void setupListeners() {
        this.startButton.addActionListener(e -> {
            if (this.stepperView == null) return;
            final int steps = this.stepperView.getStep();
            if (steps <= 0) return;
            this.manager.start(steps);
        });
        this.pauseResumeButton.addActionListener(e -> {
            if (this.manager.state() == SimulationManager.State.RUNNING) {
                this.manager.pause();
            } else if (this.manager.state() == SimulationManager.State.PAUSED) {
                this.manager.resume();
            }
        });
        this.stopButton.addActionListener(e -> {
            this.manager.stop();
            JOptionPane.showMessageDialog(this, "Simulation closed");
            System.exit(0);
        });
    }

    public void onIdle() {
        this.startButton.setEnabled(true);
        this.pauseResumeButton.setEnabled(false);
        this.pauseResumeButton.setText(PAUSE);
        this.stopButton.setEnabled(false);
    }

    public void onRunning() {
        this.startButton.setEnabled(false);
        this.pauseResumeButton.setEnabled(true);
        this.pauseResumeButton.setText(PAUSE);
        this.stopButton.setEnabled(true);
    }

    public void onPaused() {
        this.pauseResumeButton.setText(RESUME);
    }

    public void onEnded() {
        this.startButton.setEnabled(false);
        this.pauseResumeButton.setEnabled(false);
        this.stopButton.setEnabled(false);
    }
}