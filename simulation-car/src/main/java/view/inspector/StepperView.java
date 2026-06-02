package view.inspector;

import simulation.SimulationManager;
import view.ViewUtils;

import javax.swing.*;
import java.awt.*;

public class StepperView extends JPanel {
    private static final String STEP = "Step:";
    private static final int COLUMNS = 5;

    private final JLabel titleSetStepLabel;
    private final JTextField stepTextField;
    private final JLabel currentStepLabel;
    private final JLabel errorLabel;
    private final SimulationManager manager;

    public StepperView(final SimulationManager manager) {
        this.manager = manager;
        this.titleSetStepLabel = new JLabel(STEP);
        this.currentStepLabel = new JLabel("Current Step: 0");
        this.errorLabel = new JLabel();
        this.stepTextField = new JTextField(COLUMNS);

        this.setBackground(ViewUtils.GUI_BACKGROUND_COLOR);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.stepTextField.setText("100");

        this.add(this.titleSetStepLabel);
        this.add(this.stepTextField);
        this.add(this.currentStepLabel);
        this.add(this.errorLabel);
    }

    public int getStep() {
        try {
            this.errorLabel.setText("");
            return Integer.parseInt(this.stepTextField.getText());
        } catch (final NumberFormatException e) {
            this.errorLabel.setText("Put a number in the text field");
            return -1;
        }
    }

    public void updateStepper(final int currentStep) {
        this.currentStepLabel.setText("Current Step: " + currentStep);
    }

    public void onIdle() {
        this.stepTextField.setEnabled(true);
        this.currentStepLabel.setText("Current Step: 0");
    }

    public void onRunning() {
        this.stepTextField.setEnabled(false);
    }
}