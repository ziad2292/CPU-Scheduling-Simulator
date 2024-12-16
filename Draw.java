import javax.swing.*;
import java.awt.*;

class Draw extends JComponent {
    private final Color color;

    public Draw(Color color, int x, int y) {
        this.color = color;
        setBounds(x, y, 10, 30); // Set position and size (width=100, height=50)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw a filled rectangle
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight()); // Fill rectangle within component bounds
    }
}