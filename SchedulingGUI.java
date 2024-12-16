import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Collections;

public class SchedulingGUI {

    JFrame mainFrame = new JFrame();
    JPanel firstScreen, PriorityScheduling, SJF, SRTF, FCAIScheduling;
    JLabel TitleLabelScreen1;
    JButton button1MainScreen, button2MainScreen, button3MainScreen, button4MainScreen;
    JTextField inputFieldPriority, inputFieldQuantum;
    CardLayout cardLayout;
    JPanel cardsPanel;
    String selectedButton = "";
    static int rectangleX = GUI.xCoordinate;
    int increment = GUI.increment;
    static int rectangleY = GUI.processY;
    static int maximumTime = 0;
    static GUI gui;

    DefaultListModel<Process> processListModel = new DefaultListModel<>();
    JList<Process> processList = new JList<>(processListModel);

    SchedulingGUI() {
        mainFrame = new JFrame("Main screen");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        firstScreen = new JPanel();
        firstScreen.setLayout(new GridLayout(5, 1));
        TitleLabelScreen1 = new JLabel("Choose the Scheduler", JLabel.CENTER);
        firstScreen.add(TitleLabelScreen1);

        button1MainScreen = new JButton("Priority Scheduling");
        button2MainScreen = new JButton("SJF");
        button3MainScreen = new JButton("SRTF");
        button4MainScreen = new JButton("FCAI Scheduling");

        button1MainScreen.addActionListener(new ButtonListener());
        button2MainScreen.addActionListener(new ButtonListener());
        button3MainScreen.addActionListener(new ButtonListener());
        button4MainScreen.addActionListener(new ButtonListener());

        firstScreen.add(button1MainScreen);
        firstScreen.add(button2MainScreen);
        firstScreen.add(button3MainScreen);
        firstScreen.add(button4MainScreen);

        PriorityScheduling = createSchedulingPanel("Priority Scheduling");
        SJF = createSchedulingPanel("SJF");
        SRTF = createSchedulingPanel("SRTF");
        FCAIScheduling = createSchedulingPanel("FCAI Scheduling");

        cardsPanel.add(firstScreen, "Main");
        cardsPanel.add(PriorityScheduling, "Priority Scheduling");
        cardsPanel.add(SJF, "SJF");
        cardsPanel.add(SRTF, "SRTF");
        cardsPanel.add(FCAIScheduling, "FCAI Scheduling");

        mainFrame.add(cardsPanel);
        mainFrame.setVisible(true);
    }

    private JPanel createSchedulingPanel(String title) {

        JTextField localInputFieldPriority = new JTextField(10);
        JTextField localInputFieldQuantum = new JTextField(10);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton backButton = new JButton("Back to Main Screen");
        backButton.addActionListener(e -> {
            processListModel.clear(); // Clear process list when going back
            cardLayout.show(cardsPanel, "Main");
        });

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(backButton);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(backPanel, gbc);

        String[] labels = { "Process name:", "Burst time:", "Arrival time:" };
        JTextField[] textFields = { new JTextField(10), new JTextField(10), new JTextField(10) };

        gbc.gridwidth = 1;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            panel.add(new JLabel(labels[i], JLabel.LEFT), gbc);
            gbc.gridx = 1;
            panel.add(textFields[i], gbc);
        }

        // Conditional fields based on the selected scheduler
        if (title.equals("Priority Scheduling")) {
            JLabel priorityLabel = new JLabel("Priority value:");
            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(priorityLabel, gbc);
            inputFieldPriority = new JTextField(10);
            gbc.gridx = 1;
            panel.add(localInputFieldPriority, gbc);
        } else if (title.equals("FCAI Scheduling")) {
            JLabel priorityLabel = new JLabel("Priority value:");
            JLabel quantumLabel = new JLabel("Quantum value:");
            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(priorityLabel, gbc);
            //inputFieldPriority = new JTextField(10);
            gbc.gridx = 1;
            panel.add(localInputFieldPriority, gbc);
            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(quantumLabel, gbc);
            inputFieldQuantum = new JTextField(10);
            gbc.gridx = 1;
            panel.add(localInputFieldQuantum, gbc);
        }

        JButton addProcessButton = new JButton("Add Process");
        addProcessButton.addActionListener(e -> {

            System.out.println();

            String processName = textFields[0].getText();
                int burstTime = Integer.parseInt(textFields[1].getText());
                int arrivalTime = Integer.parseInt(textFields[2].getText());
                int priority = 0;
                int quantum = 0;
            
            try {
                if (title.equals("Priority Scheduling") || title.equals("FCAI Scheduling")) {
                    if (localInputFieldPriority == null || localInputFieldPriority.getText().isEmpty()) {
                        throw new IllegalArgumentException("Priority value is required for this scheduler.");
                    }
                    priority = Integer.parseInt(localInputFieldPriority.getText());
                }
                
                
                if (title.equals("FCAI Scheduling") && localInputFieldQuantum != null) {
                    quantum = Integer.parseInt(localInputFieldQuantum.getText());
                }
            }catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter valid numeric values for burst time, arrival time, and priority.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
            

            Process process = null;
            if (title.equals("FCAI Scheduling")) {
                process = new Process(processName, priority, arrivalTime, burstTime, quantum);
            } else if (title.equals("Priority Scheduling")) {
                process = new Process(processName,"NULL", arrivalTime, burstTime, priority);
            } else if (title.equals("SJF")) {
                process = new Process(processName,0,arrivalTime,burstTime,0);
            }else if(title.equals("SRTF")){
                process = new Process(processName,"NULL", arrivalTime, burstTime, 0);
            }

            processListModel.addElement(process);

            for (JTextField field : textFields) {
                field.setText("");
            }
            if (localInputFieldPriority != null) {
                localInputFieldPriority.setText("");
            }
            if (localInputFieldQuantum != null) {
                localInputFieldQuantum.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(addProcessButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(new JScrollPane(processList), gbc);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {

            if(gui != null)gui.frame.dispose();
            gui = new GUI();
            gui.xCoordinate = 100;
            gui.processY = 120;
            rectangleY = GUI.processY;
            rectangleX = GUI.xCoordinate;

            List<Process> processList = Collections.list(processListModel.elements());

    


            if(selectedButton.equals("Priority Scheduling")){
                new PriorityScheduling().execute(processList, 0);
            }
              
            else if(selectedButton.equals("SJF")){
                new sjfScheduling().execute(processList, 0);
            }
                
            else if(selectedButton.equals("SRTF")){
                new srtfScheduling().execute(processList, 0);
            }
                
            else if(selectedButton.equals("FCAI Scheduling")){
                new fcaiScheduling().execute(processList, 0);
            }

            


        });
        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(submitButton, gbc);


        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;

        return panel;
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton sourceButton = (JButton) e.getSource();

            if (sourceButton == button1MainScreen) {
                selectedButton = "Priority Scheduling";
                cardLayout.show(cardsPanel, "Priority Scheduling");
            } else if (sourceButton == button2MainScreen) {
                selectedButton = "SJF";
                cardLayout.show(cardsPanel, "SJF");
            } else if (sourceButton == button3MainScreen) {
                selectedButton = "SRTF";
                cardLayout.show(cardsPanel, "SRTF");
            } else if (sourceButton == button4MainScreen) {
                selectedButton = "FCAI Scheduling";
                cardLayout.show(cardsPanel, "FCAI Scheduling");
            }

            System.out.println("Button pressed: " + selectedButton);
        }
    }
}