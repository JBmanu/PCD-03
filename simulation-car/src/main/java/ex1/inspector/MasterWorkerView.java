package ex1.inspector;

import ex1.simulation.InspectorSimulation;
import ex1.synchronizers.service.ExecutorType;
import ex1.synchronizers.worker.master.MultiWorkerGeneric;
import ex1.synchronizers.worker.master.MultiWorkerSpecific;
import ex1.synchronizers.worker.master.WorkerType;
import ex1.utils.ViewUtils;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;


public class MasterWorkerView extends JPanel implements StartStopViewListener {
    private final DefaultComboBoxModel<WorkerType> comboBoxModelMasterWorker;
    private final JComboBox<WorkerType> comboBoxMasterWorker;

    // Generic
    private final JPanel genericPanel;
    private final JLabel genericLabel;
    private final JTextField genericTextField;
    // Specific
    private final JPanel specificPanel;
    private final JLabel senseLabel;
    private final JTextField senseTextField;
    private final JLabel decideLabel;
    private final JTextField decideTextField;
    private final JLabel actionLabel;
    private final JTextField actionTextField;

    // Executor
    private final JPanel executorPanel;
    private final JLabel executorLabel;
    private final JTextField executorTextField;
    private final DefaultComboBoxModel<ExecutorType> comboBoxModelExecutor;
    private final JComboBox<ExecutorType> comboBoxExecutor;

    public MasterWorkerView() {
        this.comboBoxModelMasterWorker = new DefaultComboBoxModel<>();
        this.comboBoxModelMasterWorker.addElement(WorkerType.GENERIC);
        this.comboBoxModelMasterWorker.addElement(WorkerType.SPECIFIC);
        this.comboBoxMasterWorker = new JComboBox<>(this.comboBoxModelMasterWorker);

        // Generic
        this.genericLabel = new JLabel(WorkerType.GENERIC.getName());
        this.genericTextField = new JTextField("2", 3);
        // Specific
        this.senseLabel = new JLabel("Sense");
        this.senseTextField = new JTextField("2", 3);
        this.decideLabel = new JLabel("Decide");
        this.decideTextField = new JTextField("2", 3);
        this.actionLabel = new JLabel("Action");
        this.actionTextField = new JTextField("2", 3);

        // Generic setup
        this.genericPanel = new JPanel(new FlowLayout());
        this.genericPanel.add(this.genericLabel);
        this.genericPanel.add(this.genericTextField);

        // Specific setup
        this.specificPanel = new JPanel(new FlowLayout());
        this.specificPanel.add(this.senseLabel);
        this.specificPanel.add(this.senseTextField);
        this.specificPanel.add(this.decideLabel);
        this.specificPanel.add(this.decideTextField);
        this.specificPanel.add(this.actionLabel);
        this.specificPanel.add(this.actionTextField);

        // Executor setup
        this.executorLabel = new JLabel("Thread");
        this.executorTextField = new JTextField("2", 3);
        this.comboBoxModelExecutor = new DefaultComboBoxModel<>();
        this.comboBoxModelExecutor.addElement(ExecutorType.FIXED);
        this.comboBoxModelExecutor.addElement(ExecutorType.SCHEDULED);
        this.comboBoxModelExecutor.addElement(ExecutorType.SINGLE);
        this.comboBoxExecutor = new JComboBox<>(this.comboBoxModelExecutor);
        this.executorPanel = new JPanel(new FlowLayout());
        this.executorPanel.add(this.executorLabel);
        this.executorPanel.add(this.executorTextField);
        this.executorPanel.add(this.comboBoxExecutor);

        this.setLayout(new FlowLayout());
        this.add(this.genericPanel);
        this.add(this.specificPanel);
        this.add(this.comboBoxMasterWorker);
        this.add(this.executorPanel);

        this.specificPanel.setVisible(false);

        this.setBackground(ViewUtils.GUI_BACKGROUND_COLOR);
        this.setOpaque(false);

        this.comboBoxMasterWorker.addActionListener(e -> {
            final JComboBox<WorkerType> comboBox = (JComboBox<WorkerType>) e.getSource();
            final WorkerType selectedOption = (WorkerType) comboBox.getSelectedItem();
            System.out.println("Opzione selezionata: " + selectedOption);

            if (WorkerType.GENERIC.equals(selectedOption)) {
                MasterWorkerView.this.genericPanel.setVisible(true);
                MasterWorkerView.this.specificPanel.setVisible(false);
            } else {
                MasterWorkerView.this.genericPanel.setVisible(false);
                MasterWorkerView.this.specificPanel.setVisible(true);
            }
        });
    }

    @Override
    public boolean conditionToStart(final InspectorSimulation simulation) {
        return this.checkValue();
    }

    @Override
    public void onStart(final InspectorSimulation simulation) {
        if (!this.checkValue()) return;
        this.setupSimulation(simulation);
    }

    @Override
    public void reset(final InspectorSimulation simulation) {

    }

    private void setupSimulation(final InspectorSimulation simulation) {
        switch (this.workerType()) {
            case GENERIC:
                simulation.setMasterWorker(
                        new MultiWorkerGeneric(this.getExecutorService(), this.getGenericWorker()));
                break;
            case SPECIFIC:
                simulation.setMasterWorker(
                        new MultiWorkerSpecific(this.getExecutorService(), this.getSense(), this.getDecide(), this.getAction()));
                break;
        }
    }

    private boolean checkValue() {
        return this.getGenericWorker() > 0
                && this.getSense() > 0 && this.getDecide() > 0 && this.getAction() > 0
                && this.getExecutor() > 0;
    }

    private WorkerType workerType() {


        return (WorkerType) this.comboBoxMasterWorker.getSelectedItem();
    }

    private ExecutorService getExecutorService() {
        final ExecutorType executorType = (ExecutorType) this.comboBoxExecutor.getSelectedItem();
        return ExecutorType.createExecutor(executorType, this.getExecutor());
    }

    private int getGenericWorker() {
        try {
            return Integer.parseInt(this.genericTextField.getText());
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    private int getSense() {
        try {
            return Integer.parseInt(this.senseTextField.getText());
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    private int getDecide() {
        try {
            return Integer.parseInt(this.decideTextField.getText());
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    private int getAction() {
        try {
            return Integer.parseInt(this.actionTextField.getText());
        } catch (final NumberFormatException e) {
            return -1;
        }
    }

    private int getExecutor() {
        try {
            return Integer.parseInt(this.executorTextField.getText());
        } catch (final NumberFormatException e) {
            return -1;
        }
    }


}
