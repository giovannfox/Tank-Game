package src;

import src.GameObject.Moveable.*;
import src.GameObject.Stationary.*;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.logging.Level;

//Main game class
public class GameWorld implements Runnable {
    //Dimensions
    private String frame_title;
    private int frame_height;
    private int frame_width;
    private int map_height;
    private int map_width;
    //Resources
    private String backgroundPath;
    private String wall_path;
    private String health_path;
    private String life_path;
    private String tank1_path;
    private String tank2_path;
    private String projectile_path;
    private String imagePath[];
    private String music_path;
    private String sound_paths[];
    private String breakablewall_path;
    //Tanks
    private static Tank tank1;
    private static Tank tank2;
    private Keyboard key1;
    private Keyboard key2;
    //Sound
    private Sound music;
    private ArrayList<Sound> soundplayer;
    //Map
    private Map map;
    private final int NUM_ROWS = 25, NUM_COLS = 25;
    private int[][] mapLayout;
    private final Observer gObserver;
    private Thread thr;
    private boolean run = false;
    private ArrayList<Wall> walls;
    private ArrayList<BreakWall> backWalls;
    private ArrayList<PowerUp> powerUp;
    private ArrayList<Bullet> bullets;
    //Window
    private JFrame frame;
    public static void main(String args[]) {
        GameWorld gameworld = new GameWorld();
        gameworld.start();
    }
    public GameWorld() {
        this.gObserver = new Observer();
    }
    @Override
    public void run() {
        init();
        try {
            while(run) {
                this.gObserver.setChanged();
                this.gObserver.notifyObservers();
                tick();
                render();

                Thread.sleep(1000/144);
            }
        } catch (InterruptedException e) {
            Logger.getLogger(GameWorld.class.getName()).log(Level.SEVERE, null, e);
        }
        stop();
    }
    private void tick() {
        this.map.setBullets(bullets);
    }
    private void render() {
        this.map.repaint();
    }
    public synchronized void start() {
        if (run)
            return;
        run = true;
        thr = new Thread(this);
        thr.start();
    }
    public synchronized void stop() {
        if (!run)
            return;
        run = false;
        try {
            thr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void init() {
        initWorldProperties();
        initResourcePaths();
        this.map = new Map(map_width, map_height, frame_width, frame_height, backgroundPath, imagePath);
        setupMap();
        setupPlayers();
        setupSounds();
        setupFrame();
    }
    private void initWorldProperties() {
        this.frame_title = "Tank War!";
        this.frame_width = 750;
        this.frame_height = 750;
        this.map_width = 1600;
        this.map_height = 1600;
    }
    private void initResourcePaths() {
        backgroundPath = "/Background.bmp";
        wall_path = "/Wall1.gif";
        breakablewall_path = "/Wall2.gif";
        health_path = "/Health.gif";
        life_path = "/Life.gif";
        tank1_path = "/Tank1.gif";
        tank2_path = "/Tank2.gif";
        projectile_path = "/Shell.gif";
        imagePath = new String[] {tank1_path, tank2_path, wall_path, breakablewall_path, health_path, life_path, projectile_path};
        music_path = "Resources/Music.wav";
        sound_paths = new String[] {"Resources/Explosion_small.wav", "Resources/Explosion_large.wav", "Resources/unbreakable.wav", "Resources/breakable.wav"};
    }
    public void setupMap() {
        setMapLayout();
        createMapObjects();
    }
    // 0 empty, 2 wall, 3 break wall, 4 health
    private void setMapLayout() {
        this.mapLayout = new int[][]
                {
                        {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 4, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 2},
                        {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 2},
                        {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 2},
                        {2, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2},
                        {2, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2},
                        {2, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 4, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                        {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                };
    }
    private void createMapObjects() {
        walls = new ArrayList<>();
        backWalls = new ArrayList<>();
        powerUp = new ArrayList<>();
        BufferedImage objectImage;
        int cell_size = 64;
        int extra = 32;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                // Wall
                if (this.mapLayout[row][col] == 2) {
                    objectImage = setImage(imagePath[2]);
                    walls.add(new Wall(col*cell_size, row*cell_size,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    walls.add(new Wall((col*cell_size)+extra, row*cell_size,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    walls.add(new Wall(col*cell_size, (row*cell_size)+extra,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    walls.add(new Wall((col*cell_size)+extra, (row*cell_size)+extra,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                }
                // Breakable Wall
                if (this.mapLayout[row][col] == 3) {
                    objectImage = setImage(imagePath[3]);
                    backWalls.add(new BreakWall(col*cell_size, row*cell_size,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    backWalls.add(new BreakWall((col*cell_size)+extra, row*cell_size,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    backWalls.add(new BreakWall(col*cell_size, (row*cell_size)+extra,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    backWalls.add(new BreakWall((col*cell_size)+extra, (row*cell_size)+extra,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                }
                if (this.mapLayout[row][col] == 4) {
                    objectImage = setImage(imagePath[4]);
                    powerUp.add(new PowerUp((col*cell_size)+(extra/2), (row*cell_size)+(extra/2),
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                }
            }
        }

        walls.forEach((current) -> {
            this.gObserver.addObserver(current);
        });
        backWalls.forEach((current) -> {
            this.gObserver.addObserver(current);
        });
        powerUp.forEach((current) -> {
            this.gObserver.addObserver(current);
        });
        this.map.setMapObjects(this.walls, this.backWalls, this.powerUp);
    }
    private void setupPlayers() {
        BufferedImage t1img = setImage(imagePath[0]);
        BufferedImage t2img = setImage(imagePath[1]);
        int tank1_x = 200,
                tank1_y = 200,
                tank2_x = 1400,
                tank2_y = 1400,
                tank2_angle = 360,
                tank_speed = 3;
        tank1 = new Tank(this, t1img, tank1_x, tank1_y, tank_speed,
                KeyEvent.VK_A, KeyEvent.VK_D,
                KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_SPACE);

        tank2 = new Tank(this, t2img, tank2_x, tank2_y, tank_speed,
                KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER);
        tank2.setAngle(tank2_angle);
        tank1.setOtherTank(tank2);
        tank2.setOtherTank(tank1);
        this.key1 = new Keyboard(tank1);
        this.key2 = new Keyboard(tank2);
        gObserver.addObserver(tank1);
        gObserver.addObserver(tank2);
        this.map.setTanks(tank1, tank2);
        this.bullets = new ArrayList<>();
    }
    //sound setup
    private void setupSounds() {
        this.music = new Sound(1, this.music_path);
        Sound small_explosion = new Sound(2, this.sound_paths[0]);
        Sound large_explosion = new Sound(2, this.sound_paths[1]);
        Sound unbreakable_hit = new Sound(2, this.sound_paths[2]);
        Sound breakable_hit = new Sound(2, this.sound_paths[3]);
        this.soundplayer = new ArrayList<>();
        this.soundplayer.add(small_explosion);
        this.soundplayer.add(large_explosion);
        this.soundplayer.add(unbreakable_hit);
        this.soundplayer.add(breakable_hit);
    }
    private void setupFrame() {
        frame = new JFrame();
        this.frame.setTitle(frame_title);
        this.frame.setSize(frame_width, frame_height);
        this.frame.setPreferredSize(new Dimension(frame_width, frame_height));
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.map);
        this.frame.setLocationRelativeTo(null);
        this.frame.addKeyListener(key1);
        this.frame.addKeyListener(key2);
        this.frame.pack();
        this.frame.setVisible(true);
    }
    public void addBulletToObservable(Bullet b) {
        this.gObserver.addObserver(b);
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
    public static Tank getTank(int number) {
        switch (number) {
            case 1:
                return tank1;
            case 2:
                return tank2;
            default:
                return null;
        }
    }
    public ArrayList<Wall> getWalls() {
        return this.walls;
    }
    public int getWallSize(){
        return walls.size();
    }
    public ArrayList<Bullet> getBullet() {
        return bullets;
    }
    public ArrayList<BreakWall> getBreakableWalls() {
        return this.backWalls;
    }
    public int getBreakableWallSize() {
        return backWalls.size();
    }
    public BufferedImage getBulletImage(){
        BufferedImage projectile = setImage(imagePath[6]);
        return projectile;
    }
    public int getMapHeight() {
        return this.map_height;
    }
    public int getMapWidth() {
        return this.map_width;
    }
    public void playSound(int sound_number) {
        if (sound_number >= 0 && sound_number <= 3)
            this.soundplayer.get(sound_number).play();
    }
    public Sound getSound(int sound_number) {
        return this.soundplayer.get(sound_number);
    }
}