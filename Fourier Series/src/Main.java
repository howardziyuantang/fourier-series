import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int[] center = new int[]{screenSize.width / 2, screenSize.height / 2 - 30};
//        Rectangle square = new Rectangle(center[0] - 150, center[1] - 150, 300, 300);
//        double r1 = 0, r2 = 0;
//        for(double t = 0; t < 2 * 3.14159; t+=3.14159/200) {
//            Line2D line2D =
//        }

        JFrame frame = new JFrame("Fourier Transform");
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //comment and uncomment to choose between images
        
        BufferedImage img = ImageIO.read(new File("Outlines/6star1.png"));
        // BufferedImage img = ImageIO.read(new File("Outlines/Circle.png"));
        // BufferedImage img = ImageIO.read(new File("Outlines/Heart.png"));
        // BufferedImage img = ImageIO.read(new File("Outlines/Lightning.png"));
        // BufferedImage img = ImageIO.read(new File("Outlines/Person.png"));
        // BufferedImage img = ImageIO.read(new File("Outlines/Square.png"));
        // BufferedImage img = ImageIO.read(new File("Outlines/Star.png"));
        // BufferedImage img = ImageIO.read(new File("Outlines/powerhouse.png"));
        // BufferedImage img = ImageIO.read(new File("Outlines/tree.png"));
        
        ArrayList<ComplexNum> points = new ArrayList<>();
        int[] imgCenter = new int[]{img.getWidth()/2, img.getHeight()/2};
        int x = imgCenter[0], y = imgCenter[1], startX;
        for(; x < img.getWidth(); x++) if(img.getRGB(x, y) != -1) break;
        points.add(new ComplexNum(x - imgCenter[0], 0, false));
        startX = x;
        while(true) {
            int move = -1;
            boolean done = false;
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if((i == 1 && j == 1)) {
                        //System.out.println("move 4 skipped");
                        continue;
                    }
                    if(x + j - 1 == img.getWidth() || x + j - 1 == -1) {
                        //System.out.println("x out of bounds");
                        continue;
                    }
                    if(y + i - 1 == -1 || y + i - 1 == img.getHeight()) {
                        //System.out.println("y out of bounds");
                        continue;
                    }
                    if(img.getRGB(x + j - 1, y + i - 1) == -1) {
                        //System.out.println("blank space");
                        continue;
                    }
                    if(points.contains(new ComplexNum(x + j - 1 - imgCenter[0], imgCenter[1] - (y + i - 1), false))) {
                        if(x + j - 1 == startX && y + i - 1 == imgCenter[1]) done = true;
                        //System.out.println("backtracking");
                        continue;
                    }
                    if(move == -1 || (move % 2 == 0 && (i * 3 + j) % 2 == 1)) {
                        move = (i * 3 + j);
                    }
                    //System.out.println(move);
                }
            }
            if(done && move == -1) break;
            //System.out.println(x + " " + y + " " + move);
            x += move % 3 - 1;
            y += move / 3 - 1;
            if(x == startX && y == imgCenter[1]) break;
            points.add(new ComplexNum(x - imgCenter[0], imgCenter[1] - y, false));
        }
//        for(ComplexNum point : points) System.out.println(point.x + " " + point.y);

        ArrayList<double[]> circles = new ArrayList<>();
        double unitSpeed = 3.14159 / 200;
        int n = 100;
        for(int i = -n; i <= n; i++) {
            ComplexNum sum = ComplexNum.ZERO;
            //System.out.println(i);
            for(int j = 0; j < points.size(); j++) {
                ComplexNum copy = new ComplexNum(points.get(j).x, points.get(j).y, false);
                //System.out.println(copy.x + " " + copy.y);
                sum.add(copy.multiply(new ComplexNum(1, -i * Math.PI * 2 * j/points.size(), true)));
                //System.out.println(sum.x + " " + sum.y);
            }
            sum.divide(new ComplexNum(points.size(), 0, false));
//            System.out.println(sum.r);
            circles.add(new double[]{3*sum.r, i * unitSpeed, sum.theta});
            //System.out.println(sum.r + " " + i + " " + sum.theta);
        }

        Panel panel = new Panel(circles, 30);

        frame.add(panel);
        frame.setVisible(true);

        //basic shape with n sides: angle ratio of 1:(1-n):(1+n)

    }

}
