package src.GameObject.Moveable;

import src.GameObject.GameObject;
import src.GameWorld;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.awt.Rectangle;
import java.util.Observer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tank extends Moveable implements Observer {
    protected int coolDown = 0;
    protected int health = 30;
    protected int life = 1;
    private int angle = 0;
    private int mapSizeX;
    private int mapSizeY;
    protected int spawnX;
    protected int spawnY;
    private int left;
    private int right;
    private int up;
    private int down;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveUp;
    private boolean moveDown;
    private int shootKey;
    private int shootCoolDown = 0;
    private boolean shoot;
    private Tank player1;
    private Tank player2;
    private GameWorld obj;
    private boolean death;
   // protected int score = 0;

    public Tank() {
    }

    public Tank(GameWorld obj, BufferedImage img, int x, int y, int speed, int left, int right, int up, int down, int shootKey) {
        super(img, x, y, speed);
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.shootKey = shootKey;
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
        this.shoot = false;
        this.death = false;
        this.spawnX = x;
        this.spawnY = y;
        this.obj = obj;
        this.setBounds(8, 10, 49, 44);
        this.mapSizeX = obj.getMapWidth();
        this.mapSizeY = obj.getMapHeight();
    }
    public void setOtherTank(Tank otherTank) {
        this.player1 = new Tank();
        this.player1 = this;
        this.player2 = new Tank();
        this.player2 = otherTank;
    }
    public void setAngle(int i) {
        this.angle = i;
    }
    public boolean collision(GameObject q) {
        r = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle r2 = new Rectangle(q.getX(), q.getY(), q.getWidth(), q.getHeight());
        if ((this.r.intersects(r2)) & (!death)) {
            return true;
        }
        return false;
    }
    public void bulletDamage(int d) {
        if (coolDown <= 0)
            this.health -= d;
    }
    public void healthUp() {
        if (this.health < 30){
            this.health = 30;
        }
    }
    public void switchUpOn() {
        this.moveUp = true;
    }
    public void switchDownOn() {
        this.moveDown = true;
    }
    public void switchRightOn() {
        this.moveRight = true;
    }
    public void switchLeftOn() {
        this.moveLeft = true;
    }
    public void switchUpOff() {
        this.moveUp = false;
    }
    public void switchDownOff() {
        this.moveDown = false;
    }
    public void switchRightOff() {
        this.moveRight = false;
    }
    public void switchLeftOff() {
        this.moveLeft = false;
    }
    public void switchShootOff() {
        this.shoot = false;
    }
    public void switchShootOn() {
        this.shoot = true;
    }
    public int getTankCenterX() {
        return x + image.getWidth(null) / 2;
    }
    public int getTankCenterY() {
        return y + image.getHeight(null) / 2;
    }
    public int getAngle() {
        return this.angle;
    }
    public int getLife() {
        return this.life;
    }
    public int getHealth() {
        return health;
    }
    public int getUp() {
        return this.up;
    }
    public int getDown() {
        return this.down;
    }
    public int getRight() {
        return this.right;
    }
    public int getLeft() {
        return this.left;
    }
    public int getFireKey() {
        return this.shootKey;
    }
    public void draw(Graphics2D g) {
        player1 = GameWorld.getTank(1);
        player2 = GameWorld.getTank(2);
        this.shootCoolDown -= 1;
        if (this.health <= 0) {
            death = true;
            if (life <= 0)
                obj.playSound(1);
        }
        if ((health > 0) && (coolDown == 0) && (life > 0)) {
            death = false;
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), image.getWidth(null) / 2, image.getHeight(null) / 2);
            g.drawImage(image, rotation, null);
            if ((player1.collision(player2))) {
                if (player1.x > x) {
                    player1.x += velocity * 5;
                    player2.x -= velocity * 5;
                } else if (player1.x < x) {
                    player1.x -= velocity * 5;
                    player2.x += velocity * 5;
                }
                if (player1.y > y) {
                    player1.y += velocity * 5;
                    player2.y -= velocity * 5;
                } else if (player1.y < y) {
                    player1.y -= velocity * 5;
                    player2.y += velocity * 5;
                }
            }
        } else if ((death == true) && (coolDown == 0) && (life > 0)) {
            coolDown = 20;
            if (life > 1) {
                obj.playSound(0);
                obj.getSound(0).getClip().setFramePosition(0);
            }
            if (--life >= 0) {
                if (life > 0)
                    health = 100;
            }
            death = false;
            x = spawnX;
            y = spawnY;
        } else {
            coolDown -= 1;
        }
    }

    @Override
    public void update(Observable observable, Object object) {
        shoot(this);
        update();
    }
    private void shoot(Tank t) {
        if (shoot && shootCoolDown <= 0 && coolDown <= 0 && life > 0) { // added coolDown check => fixes shooting when spawning
            Bullet newBullet = new Bullet(this.obj, obj.getBulletImage(), 5, this, 10);
            obj.getBullet().add(newBullet);
            obj.addBulletToObservable(newBullet);
            this.shootCoolDown = 10;
        }
    }
    private void checkLimit() {
        if (x < 0) {
            x = 0;
        }
        if (x >= mapSizeX) {
            x = mapSizeX;
        }
        if (y < 0) {
            y = 0;
        }
        if (y >= mapSizeY) {
            y = mapSizeY;
        }
    }
    public void update() {
        if (moveLeft == true){
            angle -= 3;
        }
        if (moveRight == true){
            angle += 3;
        }
        if (moveUp == true){
            x = ((int) (x + Math.round(velocity * Math.cos(Math.toRadians(angle)))));
            y = ((int) (y + Math.round(velocity * Math.sin(Math.toRadians(angle)))));
            checkLimit();
        }
        if (moveDown == true){
            x = ((int) (x - Math.round(velocity * Math.cos(Math.toRadians(angle)))));
            y = ((int) (y - Math.round(velocity * Math.sin(Math.toRadians(angle)))));
            checkLimit();
        }
        if (angle == -1) {
            angle = 359;
        } else if (angle == 361) {
            angle = 1;
        }
        if (coolDown > 0) {
            moveLeft = false;
            moveRight = false;
            moveUp = false;
            moveDown = false;
        }
    }
}