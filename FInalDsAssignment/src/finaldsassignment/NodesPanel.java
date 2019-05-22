/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgtest2;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import java.util.List;

/**
 *
 * @author Zakkir
 */
public class NodesPanel extends JPanel implements Runnable, KeyListener {

    //FIELD
    public static int WIDTH = 1500;
    public static int HEIGHT = 800;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    public static ArrayList<Circle> circle;
    private int V;
    List<List<Node>> adj = new ArrayList<List<Node>>();
    private ArrayList<String> shortpath = new ArrayList<>();

    private boolean Reset;
    public int numOfCircle;

    //CONSTRUCTOR
    public NodesPanel(int V, List<List<Node>> adj, ArrayList<String> shortpath) {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        this.adj = adj;
        this.V = V;
        this.shortpath = shortpath;

    }

    //FUNCTIONS
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
        addKeyListener(this);
    }

    public void run() {
        running = true;
        Reset = false;
        //PAINTBRUSH
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        //Anti - Aliasing ---- Smooth the jagged line.
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //Creating array of circle based on circle class.
        circle = new ArrayList<>();
        circle.add(new Circle(WIDTH, HEIGHT)); // First node is unique.
        for (int i = 1; i < V; i++) {// Other nodes must not have same coordinate and also not overlapping with each other.
            boolean overlapping = true;
            circle.add(new Circle());
            do {
                for (int j = 0; j < i; j++) {
                    overlapping = circle.get(i).overlapping(circle.get(j).getX(), circle.get(j).getY(), circle.get(j).getR());
                    if (overlapping) {
                        circle.remove(i);
                        circle.add(new Circle());
                        j = 0;
                    }
                }
            } while (overlapping);

        }

        while (running) {
            nodesUpdate();
            nodesRender();
            nodesDraw();
        }
    }

    public void generate() {
        circle.add(new Circle(WIDTH, HEIGHT)); // First node is unique.
        for (int i = 1; i < V; i++) {// Other nodes must not have same coordinate and also not overlapping with each other.
            boolean overlapping = true;
            circle.add(new Circle());
            do {
                for (int j = 0; j < i; j++) {
                    overlapping = circle.get(i).overlapping(circle.get(j).getX(), circle.get(j).getY(), circle.get(j).getR());
                    if (overlapping) {
                        circle.remove(i);
                        circle.add(new Circle());
                        j = 0;
                    }
                }
            } while (overlapping);
        }
    }

    public void drawLines(Graphics2D g) {
        /*-------- Drawing Line between the neuron ----------*/
        g.setColor(new Color(243, 134, 48));
        g.setStroke(new BasicStroke(3));
        for (int i = 0; i < this.V; i++) {
            for (int j = 0; j < adj.get(i).size(); j++) {
                g.setColor(Color.BLACK);
                g.drawLine((int) circle.get(i).getX(), (int) circle.get(i).getY(), (int) circle.get(adj.get(i).get(j).node).getX(), (int) circle.get(adj.get(i).get(j).node).getY());

                /*------- Display Inforamtion about the path connected---------*/
                int dx, dy;
                dx = (int) (circle.get(i).getX() + circle.get(adj.get(i).get(j).node).getX()) / 2;
                dy = (int) (circle.get(i).getY() + circle.get(adj.get(i).get(j).node).getY()) / 2;

                // dx and dy are used to search the midpoint based on mathematics formula
                String s = "Dist: " + adj.get(i).get(j).costD + " Time: " + adj.get(i).get(j).costT;
                int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
                g.setColor(Color.white);
                g.drawRect(dx - length, dy - 18, length, 18);
                g.fillRect(dx - length, dy - 18, length, 18);
                g.setFont(new Font("Century Gothic", Font.PLAIN, 18));

                g.setColor(Color.BLACK);
                g.drawString(s, dx - length, dy);
            }
        }
    }

    public void drawStringUpdater(Graphics2D g) {

        
        int j = 50 ;
        g.setFont(new Font("Century Gothic", Font.BOLD, 15));
        g.drawString("Information",500/2-8,20);
        for (int i = 0; i < shortpath.size(); i++) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Century Gothic", Font.BOLD, 15));
            g.drawString(shortpath.get(i),5,j);
            j+= 20;
            
        }
    }

    /*------------ Drawing Graphic User Interface -------------*/
    private void nodesUpdate() {
        if (Reset == true) {
            circle.clear();
            generate();
            Reset = false;
        }
    }

    private void nodesRender() { //Render all drawing by invoke the 'draw' method
        g.setColor(new Color(224, 228, 204));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 500, HEIGHT);

//        for (int i = 0; i < get.getV(); i++){
//            int j = 0 ;
//            while(j < get.adj.get(i).size()){
//            get.adj.get(i).get(j).draw(g);
//            j++;
//                   
//            }
//        }
        drawLines(g);
        for (int i = 0; i < circle.size(); i++) {

            circle.get(i).draw(g);
            g.setColor(Color.BLACK);
            g.drawString("" + i, (int) circle.get(i).getX() - 5, (int) circle.get(i).getY() - 5);

        }

        drawStringUpdater(g);

        g.setColor(Color.BLACK);
        g.drawString("Sheldon's Amazing Human Brain Visualizer", WIDTH / 2, HEIGHT-20);

    }

    private void nodesDraw() { // Paint all rendered object into canvas.
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

    }

    @Override
    public void keyTyped(KeyEvent key) {

    }

    @Override
    public void keyPressed(KeyEvent key) {
        int keyCode = key.getKeyCode();
        if (keyCode == KeyEvent.VK_R) {
            Reset = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {
        int keyCode = key.getKeyCode();
        if (keyCode == KeyEvent.VK_R) {
        }
    }

}
