import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Panel extends JPanel implements ActionListener, KeyListener {

    private double[] origin, prev;
    private List<double[]> circles;
    private List<double[]> arc;
    private List<Double> startAngles;
    private int index = 0;
    private boolean running = false, done = false, first = true;
    private Timer timer;
    private JLabel radii;

    public Panel(List<double[]> circles, int delay) {
        startAngles = new ArrayList<>();
        radii = new JLabel();
        radii.setForeground(Color.WHITE);
        radii.setFont(new Font("Calibri", Font.PLAIN, 30));
        if(circles == null) {
            this.circles = new ArrayList<>();
            this.circles.add(new double[]{200d, 3.14159 / 200});
            startAngles.add(0d);
        } else {
            this.circles = circles;
            for(double[] circle : circles)
                startAngles.add((circle[2] >= 0) ? circle[2] : circle[2] + 2 * 3.14159);
        }
        updateRadii();
        Dimension prefSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(prefSize);
        origin = new double[]{prefSize.width / 2d, prefSize.height / 2d - 30};
        this.setBackground(Color.BLACK);
        this.setVisible(true);
        setFocusable(true);
        addKeyListener(this);
        //add(radii);
        timer = new Timer(delay, this);
        arc = new LinkedList<>();
    }

    private void updateRadii() {
        String s = "";
        for(double[] circle : circles)
            s += circle[0] + "   ";
        radii.setText(s);
    }

    private void resetAngles() {
        for(int i = 0; i < circles.size(); i++) {
            double[] temp = circles.get(i);
            temp[2] = startAngles.get(i);
            circles.set(i, temp);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        double[] center = origin;
        for(int i = 0; i < circles.size(); i++) {
            if(i == index) g2d.setPaint(Color.RED);
            else g2d.setPaint(Color.WHITE);
            double[] circle = circles.get(i);
            double[] dest = {center[0] + circle[0] * Math.cos(circle[2]), center[1] - circle[0] * Math.sin(circle[2])};
            g2d.drawLine((int) center[0], (int) center[1], (int) dest[0], (int) dest[1]);
            if(i + 1 == circles.size()) {
                if(prev != null) {
                    arc.add(new double[]{prev[0], prev[1], dest[0], dest[1]});
                    if(first) first = false;
                    else if(isHome(1.0E-4)) {
                        timer.stop();
                        running = false;
                        done = true;
                    }
                }
                prev = dest;
            }
            center = dest;
        }
        ListIterator<double[]> iterator = arc.listIterator();
        g2d.setPaint(Color.WHITE);
        while(iterator.hasNext()) {
            double[] current = iterator.next();
            g2d.drawLine((int) current[0], (int) current[1], (int) current[2], (int) current[3]);
        }
    }

    private boolean isHome(double tolerance) {
        for(int i = 0; i < circles.size(); i++) {
            double[] circle = circles.get(i);
            double diff = Math.abs(circle[2] - startAngles.get(i));
            if(diff > tolerance && 2 * 3.14159 - diff > tolerance) return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i = 0; i < circles.size(); i++) {
            double[] circle = circles.get(i);
            double temp = circle[2] + circle[1];
            if(temp >= 2 * 3.14159) temp -= 2 * 3.14159;
            else if(temp < 0) temp += 2 * 3.14159;
            circle[2] = temp;
            circles.set(i, circle);
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyChar()) {
            case (char) 32:
                if(running) timer.stop();
                else {
                    if(done) {
                        arc.clear();
                        prev = null;
                        resetAngles();
                        done = false;
                    }
                    timer.start();
                }
                running = !running;
                break;
            case 'p':
            case 'm':
            case 'r':
                if(e.getKeyChar() == 'p') {
                    circles.add(new double[]{circles.get(circles.size() - 1)[0] * .6, 3.14159 / 200, 0});
                    startAngles.add(0d);
                    updateRadii();
                } else if(e.getKeyChar() == 'm' && circles.size() > 1) {
                    circles.remove(circles.size() - 1);
                    startAngles.remove(startAngles.size() - 1);
                    if(index == circles.size()) index--;
                    updateRadii();
                }
                first = true;
                arc.clear();
                prev = null;
                resetAngles();
                break;
            case '+':
                circles.get(index)[0] += 1;
                updateRadii();
                break;
            case '-':
                circles.get(index)[0] -= 1;
                updateRadii();
                break;
            case 'a':
                if(index > 0) index--;
                break;
            case 'd':
                if(index < circles.size() - 1) index++;
                break;
            case 'w':
                circles.get(index)[1] += .001;
                break;
            case 's':
                circles.get(index)[1] -= .001;
                break;
            case 'c':
                arc.clear();
                break;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
