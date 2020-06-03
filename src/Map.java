package src;

import src.GameObject.Moveable.Tank;
import src.GameObject.Moveable.Bullet;
import src.GameObject.Stationary.BreakWall;
import src.GameObject.Stationary.Wall;
import src.GameObject.Stationary.PowerUp;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.util.ConcurrentModificationException;

public class Map extends JPanel {
    private ArrayList<Wall> walls;
    private ArrayList<BreakWall> breakableWall;
    private ArrayList<PowerUp> powerUp;
    private int mapHeight;
    private int mapWidth;
    private int minimapHeight;
    private int minimapWidth;
    private int windowWidth;
    private int windowHeight;
    private Tank tank1;
    private Tank tank2;
    BufferedImage p1w;
    BufferedImage  p2w;
    Image minimap;
    private int p1WindowBoundX;
    private int p1WindowBoundY;
    private int p2WindowBoundX;
    private int p2WindowBoundY;
    private ArrayList<Bullet> bullets;
    private BufferedImage backgrImage;
    private BufferedImage lifeIcon1;
    private BufferedImage  lifeIcon2;
    public Map() {
    }
    public Map(int mapWidth, int mapHeight, int windowWidth, int windowHeight,
                 String backgroundPath, String[] imagePaths) {
        super();
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.minimapWidth = 150;
        this.minimapHeight = 150;
        this.setSize(mapWidth, mapHeight);
        this.setPreferredSize(new Dimension(mapWidth, mapHeight));
        this.backgrImage = setImage(backgroundPath);
        walls = new ArrayList<>();
        breakableWall = new ArrayList<>();
        powerUp = new ArrayList<>();
        bullets = new ArrayList<>();
    }
    @Override
    public void paintComponent(Graphics graphics) {
        getGameImage();
        super.paintComponent(graphics);
        graphics.drawImage(p1w, 0, 0, this);
        graphics.drawImage(p2w, windowWidth / 2, 0, this);
        graphics.setColor(Color.RED);
        graphics.draw3DRect(0, 0, (windowWidth/2)-1, windowHeight-22, true);
        graphics.draw3DRect(windowWidth/2, 0, (windowWidth/2)-1, windowHeight-2, true);
        graphics.drawImage(minimap, (windowWidth / 2) - (minimapWidth / 2), 0, this);
        graphics.draw3DRect((windowWidth / 2) - (minimapWidth / 2), 0, minimapWidth, minimapHeight, true);
        if (tank1.getLife() == 0) {
            graphics.setFont(new Font(graphics.getFont().getFontName(), Font.CENTER_BASELINE, 90));
            graphics.drawString("Tank 2 has won", 70, windowHeight/2);
        }
        if (tank2.getLife() == 0) {
            graphics.setFont(new Font(graphics.getFont().getFontName(), Font.CENTER_BASELINE, 90));
            graphics.drawString("Tank 1 has won", 70, windowHeight/2);
        }
    }
    //game image
    public void getGameImage() {
        BufferedImage backgrImage = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = backgrImage.createGraphics();
        drawBackground(g2);
        drawMapLayout(g2);
        drawTanks(g2);
        drawProjectiles(g2);
        playerViewBoundChecker();
        p1w = backgrImage.getSubimage(this.p1WindowBoundX, this.p1WindowBoundY, windowWidth/2, windowHeight);
        p2w = backgrImage.getSubimage(this.p2WindowBoundX, this.p2WindowBoundY, windowWidth/2, windowHeight);
        minimap = backgrImage.getScaledInstance(minimapWidth, minimapHeight, Image.SCALE_SMOOTH);
    }

    private void playerViewBoundChecker() {
        if ((this.p1WindowBoundX = tank1.getTankCenterX() - windowWidth / 4) < 0) {
            this.p1WindowBoundX = 0;
        } else if (this.p1WindowBoundX >= mapWidth - windowWidth / 2) {
            this.p1WindowBoundX = (mapWidth - windowWidth / 2);
        }

        if ((this.p1WindowBoundY = tank1.getTankCenterY() - windowHeight / 2) < 0) {
            this.p1WindowBoundY = 0;
        } else if (this.p1WindowBoundY >= mapHeight - windowHeight) {
            this.p1WindowBoundY = (mapHeight - windowHeight);
        }

        if ((this.p2WindowBoundX = tank2.getTankCenterX() - windowWidth / 4) < 0) {
            this.p2WindowBoundX = 0;
        } else if (this.p2WindowBoundX >= mapWidth - windowWidth / 2) {
            this.p2WindowBoundX = (mapWidth - windowWidth / 2);
        }

        if ((this.p2WindowBoundY = tank2.getTankCenterY() - windowHeight / 2) < 0) {
            this.p2WindowBoundY = 0;
        } else if (this.p2WindowBoundY >= mapHeight - windowHeight) {
            this.p2WindowBoundY = (mapHeight - windowHeight);
        }
    }
    private void drawBackground(Graphics2D graphics) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                graphics.drawImage(this.backgrImage, this.backgrImage.getWidth() * i, this.backgrImage.getHeight() * j, this);
            }
        }
    }

    private void drawMapLayout(Graphics2D graphics) {
        walls.forEach((curr) -> {
            curr.draw(graphics);
        });
        breakableWall.forEach((curr) -> {
            curr.draw(graphics);
        });
        powerUp.forEach((curr) -> {
            curr.draw(graphics);
        });
    }

    private void drawTanks(Graphics2D graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        this.tank1.draw(g2);
        this.tank2.draw(g2);
    }
    private synchronized void drawProjectiles(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g;
        try {
            bullets.forEach((curr) -> {
                if (curr.isVisible()) {
                    curr.draw(this, g2);
                }

            });
        } catch (ConcurrentModificationException e) {
        }
    }
    private BufferedImage setImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        }
        return image;
    }
    public void setBackgroundImage(BufferedImage image) {
        this.backgrImage = image;
    }
    public void setMapObjects(ArrayList<Wall> w, ArrayList<BreakWall> b, ArrayList<PowerUp> p) {
        this.walls = w;
        this.breakableWall = b;
        this.powerUp = p;
    }
    public BufferedImage getBackgroundImage() {
        return this.backgrImage;
    }
    public void setTanks(Tank tank1, Tank tank2) {
        this.tank1 = tank1;
        this.tank2 = tank2;
    }
    public void setBullets(ArrayList<Bullet> b) {
        this.bullets = b;
    }
    public void setLifeIcons(BufferedImage image1, BufferedImage image2) {
        this.lifeIcon1 = image1;
        this.lifeIcon2 = image2;
    }
}