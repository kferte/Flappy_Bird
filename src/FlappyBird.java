import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    static FlappyBird flappyBird;

    private final int WIDTH = 1200, HEIGHT = 800;

    private Renderer renderer;

    private Rectangle bird;

    private int ticks, yMotion, score;

    private ArrayList<Rectangle> columns;

    private Random rand;

    private boolean gameOver, started;

    private FlappyBird(){
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        rand = new Random();
        jframe.add(renderer);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);

        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        timer.start();
    }

    private void addColumn(boolean start){
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if(start) {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 2).x, 0, width, HEIGHT - height - space));
        }
    }

    private void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.GREEN.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    void repaint(Graphics g){
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.ORANGE);
        g.fillRect(0, HEIGHT - 120, WIDTH, 150);
        g.setColor(Color.GREEN);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);
        g.setColor(Color.RED);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);
        for(Rectangle column : columns){
            paintColumn(g, column);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 100));
        if(!started)
            g.drawString("Click to start!", 250, HEIGHT / 2 - 50);
        if(gameOver) {
            g.drawString("Game Over!", 300, HEIGHT / 2 - 50);
            g.drawString("Your score: ", 300, HEIGHT / 2 + 50);
            g.drawString(String.valueOf(score), 550, HEIGHT / 2 + 150);
        }
        if(!gameOver && started)
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
    }

    private void jump(){
        if(gameOver){
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            gameOver = false;
        }
        if(!started){
            started = true;
        } else {
            if(yMotion > 0)
                yMotion = 0;
            yMotion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 10;
        ticks ++;
        if(started) {
            for (Rectangle column : columns) {
                column.x -= speed;
            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);
                    addColumn(true);
                    addColumn(true);
                    addColumn(false);
                }
            }
            bird.y += yMotion;
            for (Rectangle column : columns) {
                if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 5 && bird.x + bird.width / 2 < column.x + column.width / 2 + 5)
                    score++;
                if (column.intersects(bird)) {
                    gameOver = true;
                    bird.x = column.x - bird.width;
                }
            }
            if (bird.y > HEIGHT - 120 || bird.y < 0)
                gameOver = true;
            if(gameOver)
                bird.y = HEIGHT - 120 - bird.height;
        }
        renderer.repaint();
    }

    public static void main(String[] args) {
        flappyBird = new FlappyBird();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
            jump();
    }
}
