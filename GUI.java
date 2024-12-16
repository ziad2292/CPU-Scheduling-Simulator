import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {
    JFrame frame;
    JPanel panel;
    JButton srtfButton;
    JButton priorityButton;
    JButton sjfButton;
    JButton fcaiButton;
    JLabel avgWait;
    JLabel avgTurnAround;
    static int processY = 120;
    static int xCoordinate = 100;
    static int increment;

    public GUI(){
        setWindow();
        placeComponents(30);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public void setWindow(){
        frame = new JFrame();
        panel = new JPanel();

        //panel.setBorder(BorderFactory.createEmptyBorder(300, 300, 300, 400));
        panel.setLayout(null);

        frame.setSize(1150, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Graphical Representation");
        frame.add(panel);
        frame.setVisible(true);


    }




    public void placeComponents(int maximumTime){

        //Borders and numbers
        for(int i = 1; i <= maximumTime; i++){
            JLabel number = new JLabel(String.valueOf(i));
            number.setBounds(xCoordinate, 100, 20, 20);
            panel.add(number);
            increment = 700/maximumTime;
            xCoordinate += increment;
        }


        updateView();
    }

    public void createProcess(Process process){
        JLabel processName = new JLabel(process.name);
        processName.setBounds(10, processY, 60, 50);
        panel.add(processName);
        updateView();
        processY += 50;
    }

    public void createRectangle(int x, int y, Color color){
        //Add Rectangle Object to main Panel
        Draw rectangle = new Draw(color, x, y);
        panel.add(rectangle);

    }

    public void updateView(){
        panel.revalidate();
        panel.repaint();
        frame.add(panel);
        frame.setVisible(true);
    }

    public void finalSummary(double wait, double turnaround){
        //Final Summary
        avgWait  = new JLabel();
        avgTurnAround = new JLabel();

        avgWait.setBounds(900, 100, 300, 20);
        avgTurnAround.setBounds(900, 200, 300, 20);

        avgWait.setText("Average Wait Time: " + String.valueOf(wait));
        //avgTurnAround.setText("Average Turnaround Time:" + String.valueOf(turnaround));
        panel.add(avgTurnAround);
        panel.add(avgWait);
    }

}