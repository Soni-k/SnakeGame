import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int B_Height = 400;
    int B_Width = 400;
    int MAX_DOTS = 1600;
    int X[] = new int[MAX_DOTS];
    int Y[] = new int[MAX_DOTS];
    int DOT_SIZE = 10;
    int DOTS;
    int apple_x;
    int apple_y;
    // images
    Image body, head, apple;
    Timer timer;
    int DELAY = 200;
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    boolean inGame = true;

    Board() {
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_Width, B_Height));
        setBackground(Color.BLACK);
        iniGame();
        loadImages();
    }

    //initialize the game
    public void iniGame() {
        DOTS = 3;
        // initialize snake's position
        X[0] = 150;
        Y[0] = 150;
        for (int i = 1; i < DOTS; i++) {
            X[i] = X[0] + DOT_SIZE * i;
            Y[i] = Y[0];
        }

        // initialize apple's position
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    // load images form resources to image object
    public void loadImages() {

        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();

    }

    //drow images snake and apple's positions
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    // drow image
    public void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < DOTS; i++) {
                if (i == 0) {
                    g.drawImage(head, X[0], Y[0], this);
                } else
                    g.drawImage(body, X[i], Y[i], this);

            }
        } else {
            gameOver(g);
            timer.stop();

        }

    }

    // Randomize apple's position
    public void locateApple() {
        apple_x = ((int) (Math.random() * 39)) * DOT_SIZE;
        apple_y = ((int) (Math.random() * 39)) * DOT_SIZE;
    }

    //Display game over message
    public void gameOver(Graphics g) {
        String msg = "Game Over";
        int score = (DOTS - 3) * 100;
        String scoremsg = "Score: " + Integer.toString(score);
        Font small = new Font("Arial", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(small);
        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (B_Width - fontMetrics.stringWidth(msg)) / 2, B_Height / 4);
        g.drawString(scoremsg, (B_Width - fontMetrics.stringWidth(scoremsg)) / 2, 3 * (B_Height / 4));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (inGame) {
            checkCollision();
            checkApple();

            move();
        }
        repaint();
    }

    public void move() {
        for (int i = DOTS - 1; i > 0; i--) {
            X[i] = X[i - 1];
            Y[i] = Y[i - 1];
        }
        if (leftDirection) {
            X[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            X[0] += DOT_SIZE;
        }
        if (upDirection) {
            Y[0] -= DOT_SIZE;

        }
        if (downDirection) {
            Y[0] += DOT_SIZE;
        }
    }

    // check collisions with border and body
    public void checkCollision() {
        // check body collisions
        for (int i = 1; i < DOTS; i++) {
            if (i > 4 && X[0] == X[i] && Y[0] == Y[i]) {
                inGame = false;
            }
        }
        // check border collisions
        if (X[0] < 0) {
            inGame = false;
        }
        if (X[0] >= B_Width) {
            inGame = false;
        }
        if (Y[0] < 0) {
            inGame = false;
        }
        if (Y[0] >= B_Height) {
            inGame = false;
        }
    }

    // make snake eat food
    public void checkApple() {
        if (apple_x == X[0] && apple_y == Y[0]) {
            DOTS++;
            locateApple();
        }
    }

    //implement controls
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key = keyEvent.getKeyCode();
            if (key == keyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                downDirection = false;
                upDirection = false;
            }
            if (key == keyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                downDirection = false;
                upDirection = false;
            }
            if (key == keyEvent.VK_UP && !downDirection) {
                leftDirection = false;
                rightDirection = false;
                upDirection = true;
            }
            if (key == keyEvent.VK_DOWN && !upDirection) {
                leftDirection = false;
                downDirection = true;
                rightDirection = false;
            }
        }
    }
}