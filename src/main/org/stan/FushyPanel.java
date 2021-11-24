/*From MakingStan
 Written on 24 november 2021.
 For polymars seajam:  https://itch.io/jam/seajam
 */

package main.org.stan;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FushyPanel extends JPanel implements ActionListener {

    private final BufferedImage fishSprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/fish.png"));
    private final BufferedImage backgroundSprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/background.png"));
    private final BufferedImage trashSprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/trash.png"));

    private static final int SCREEN_WIDTH = 1200;
    private static final int SCREEN_HEIGTH = 800;
    private static int playerX = SCREEN_WIDTH/8;
    private static int playerY = SCREEN_HEIGTH/2;

    private int points = 0;
    private int movingTrashX = SCREEN_WIDTH-30;
    private int DELAY = 7;

    private String direction = "R";

    private boolean running = false;
    private boolean activeMainMenu = true;


    private final int[] wallStartingPositions = new int[SCREEN_HEIGTH-150];

    private final Random random = new Random();
    private final Timer gameTimer;
    private int randomInt = random.nextInt(SCREEN_HEIGTH-150);

    public FushyPanel() throws IOException {
        new MusicManager();

        gameTimer = new Timer(DELAY, this);
        gameTimer.start();

        for(int i = 0; i < SCREEN_HEIGTH-150; i++)
        {
            wallStartingPositions[i] = i;
        }
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGTH));
        this.addKeyListener(new MyKeyAdapter());
        setFocusable(true);
    }

    private void mainMenu(Graphics g)
    {
        //new Font("Ink Free",Font.BOLD, 70)
        drawText(
                "How to play",
                Color.orange,
                new Font("Ink Free", Font.BOLD, 50),
                SCREEN_WIDTH/2-150,
                SCREEN_HEIGTH/2, g);

        drawText(
           "Press the arrow keys ↑→↓← to move your fish.\n" +
                "And make sure to avoid the trash!\n\n" +
                "tip:you can move oblique.(↑+←=↖, ↓+→=↘ etc...)",
                Color.GREEN,
                new Font("Ink Free", Font.BOLD, 30),
                SCREEN_WIDTH/2-350,
                SCREEN_HEIGTH/2+100, g);



        drawText("Main Menu", Color.ORANGE, new Font("Ink Free",Font.BOLD, 70),400, SCREEN_HEIGTH/8, g);
        drawText("Press \"p\" to play the game!", Color.CYAN, new Font("Ink Free", Font.BOLD, 30), SCREEN_WIDTH/2-200, SCREEN_HEIGTH/4, g);
    }

    private void drawText(String text, Color color, Font font, int xPos, int yPos, Graphics g)
    {
        g.setColor(color);
        g.setFont(font);
        FontMetrics metrics = getFontMetrics(g.getFont());

        for (String line : text.split("\n"))
        {
            g.drawString(line, xPos, yPos += g.getFontMetrics().getHeight());
        }
    }

    public void reTry()
    {
        running = true;
        points = 0;
        DELAY = 7;
        playerX  = SCREEN_WIDTH/8;
        playerY = SCREEN_HEIGTH/2;
        movingTrashX = SCREEN_WIDTH;

        gameTimer.setDelay(DELAY);
    }


    public void startGame()
    {
        running = true;
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(backgroundSprite, 0, 0, backgroundSprite.getWidth(), backgroundSprite.getHeight(), null);

        if(running)
        {
            g.drawImage(fishSprite, playerX, playerY, fishSprite.getWidth(), fishSprite.getHeight(), null);
            displayScoreText(g);
            drawWall(g);
        }
        else if(activeMainMenu)
        {
            mainMenu(g);
        }
        else
        {
            gameOver(g);
        }
    }

    private void displayScoreText(Graphics g)
    {
        drawText("Score: "+points, Color.RED, new Font("Ink Free",Font.BOLD, 40), SCREEN_WIDTH/2-50, SCREEN_HEIGTH/20, g);
    }

    private void drawWall(Graphics g)
    {
        g.setColor(Color.darkGray);

        g.drawImage(trashSprite, movingTrashX, -trashSprite.getHeight()+wallStartingPositions[randomInt], trashSprite.getWidth(), trashSprite.getHeight(), null);
        g.drawImage(trashSprite, movingTrashX, wallStartingPositions[randomInt]+150, trashSprite.getWidth(), SCREEN_HEIGTH-wallStartingPositions[randomInt]+150, null);

        checkCollistions(g);
    }

    private void checkCollistions(Graphics g)
    {
        if(movingTrashX <= 0)
        {
            movingTrashX = SCREEN_WIDTH-30;
            randomInt = random.nextInt(SCREEN_HEIGTH-150);
            points++;
        }


        if(points == 5&& DELAY != 6)
        {
            DELAY=6;
            gameTimer.setDelay(DELAY);
        }
        else if(points == 10 && DELAY != 5)
        {
            DELAY=5;
            gameTimer.setDelay(DELAY);
        }
        else if(points == 15 && DELAY != 4)
        {
            DELAY=4;
            gameTimer.setDelay(DELAY);
        }


        if(playerX>=SCREEN_WIDTH-50||playerX < 0||playerY>SCREEN_HEIGTH||playerY<-50)
        {
            activeMainMenu = false;
            running = false;
        }


        if(movingTrashX-playerX > 0&&movingTrashX -playerX < 120 ) //Math.abs(wallX-x-140) < 5
        {
            System.out.println("wallposition index randomint: "+ wallStartingPositions[randomInt]+"\ny: "+playerY);

            if(playerY + 50 <  wallStartingPositions[randomInt]||playerY+110>wallStartingPositions[randomInt]+150) //y<wallPositions[randomInt]-50 || y > wallPositions[randomInt]+50
            {
                activeMainMenu = false;
                running = false;
            }
        }
    }

    private void gameOver(Graphics g)
    {
        //game over text
        drawText("Game Over", Color.RED, new Font("Ink Free",Font.BOLD, 75), 350, SCREEN_HEIGTH/2-100,g);
        drawText("Score: " +points, Color.RED, new Font("Ink Free",Font.BOLD, 40), 500, SCREEN_HEIGTH/4, g);

        drawText("Press r to try again!", Color.ORANGE,  new Font("Ink Free",Font.BOLD, 40), 400, SCREEN_HEIGTH/2+100, g);

    }

    private void moveCube()
    {
        switch(direction)
        {
            case "R": playerX += 3;
                break;
            case "L": playerX -= 3;
                break;
            case "U": playerY -= 3;
                break;
            case "D": playerY += 3;
                break;
            case "DL":
                playerY += 3;
                playerX -= 3;
                break;
            case "DR":
                playerY += 3;
                playerX += 3;
                break;
            case "UL":
                playerY -= 3;
                playerX -= 3;
                break;
            case "UR":
                playerY -= 3;
                playerX += 3;
                break;

        }
    }



    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (running)
        {
            moveCube();
            movingTrashX-=3;
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter
    {
        ArrayList<Integer> keys=new ArrayList();
        @Override
        public void keyPressed(KeyEvent e)
        {
            if(!keys.contains(e.getKeyCode())){
                keys.add(e.getKeyCode());
            }

            switch (e.getKeyCode())
            {
                case KeyEvent.VK_LEFT: direction = "L";
                    break;
                case KeyEvent.VK_RIGHT: direction = "R";
                    break;
                case KeyEvent.VK_UP: direction = "U";
                    break;
                case KeyEvent.VK_DOWN: direction = "D";
                    break;
                case KeyEvent.VK_R:reTry();
                   break;
            }
            if(e.getKeyCode() == KeyEvent.VK_P&&activeMainMenu)
            {
                startGame();
            }
            if(keys.contains(KeyEvent.VK_DOWN) &&keys.contains(KeyEvent.VK_LEFT))
            {
                direction = "DL";
            }
            else if(keys.contains(KeyEvent.VK_DOWN) &&keys.contains(KeyEvent.VK_RIGHT))
            {
                direction = "DR";
            }
            else if(keys.contains(KeyEvent.VK_UP) &&keys.contains(KeyEvent.VK_RIGHT))
            {
                direction = "UR";
            }
            else if(keys.contains(KeyEvent.VK_UP) &&keys.contains(KeyEvent.VK_LEFT))
            {
                direction = "UL";
            }
        }

        @Override
        public void keyReleased(KeyEvent k)
        {
            if(keys.contains(k.getKeyCode()))
            {
                keys.remove(keys.indexOf(k.getKeyCode()));
            }
        }
    }
}
