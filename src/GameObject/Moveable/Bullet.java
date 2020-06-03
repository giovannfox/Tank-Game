package src.GameObject.Moveable;

import java.awt.image.ImageObserver;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.util.Observer;
import src.GameWorld;
import java.awt.image.BufferedImage;
import src.GameObject.Stationary.Wall;
import java.util.Observable;
import src.GameObject.Stationary.BreakWall;

public class Bullet extends Moveable implements Observer {
    private final Tank p1 = GameWorld.getTank(1);
    private final Tank p2 = GameWorld.getTank(2);
    private final BufferedImage bullet;
    private GameWorld obj;
    public int xSize;
    public int ySize;
    private int theta;
    private int damage;
    public static Tank currentTank;
    public boolean visible;

    public Bullet(GameWorld gw, BufferedImage image, int velocity, Tank tk, int dmg) {
        super(image, tk.getTankCenterX(), tk.getTankCenterY(), velocity);
        bullet = image;
        damage = dmg;
        xSize = image.getWidth(null);
        ySize = image.getHeight(null);
        currentTank = tk;
        theta = currentTank.getAngle();
        visible = true;

        this.obj = gw;
    }
    public void setGameWorld(GameWorld w) {
        this.obj = w;
    }
    public static Tank getTank() {
        return currentTank;
    }
    public int getTheta() {
        return this.theta;
    }
    @Override
    public void update(Observable o, Object arg) {
        update();
    }
    public void draw(ImageObserver iobs, Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(theta), 0, 0);
        g.drawImage(bullet, rotation, iobs);
    }
    @Override
    public boolean isVisible () {
        return this.visible;
    }
    public void update() {
        y += Math.round(velocity * Math.sin(Math.toRadians(theta)));
        x += Math.round(velocity * Math.cos(Math.toRadians(theta)));

        if (p1.collision(this) && visible && currentTank != p1 && visible && p1.coolDown <= 0) {
            if (visible) {
                obj.playSound(3);// breakable collision sound
                obj.getSound(3).getClip().setFramePosition(0);
            }
            visible = false;
            p1.bulletDamage(damage);
        } else if (p2.collision(this) && visible && currentTank != p2 && visible && p2.coolDown <= 0) {
            if (visible) {
                obj.playSound(3);// breakable collision sound
                obj.getSound(3).getClip().setFramePosition(0);
            }
            visible = false;
            p2.bulletDamage(damage);
        } else
            for (int i = 0; i < obj.getWallSize(); i++){
                Wall tempWall = obj.getWalls().get(i);
                if ((tempWall.getWallRectangle().intersects(this.x, this.y, this.width, this.height)) && visible) {
                    this.visible = false;
                    obj.playSound(2);// unbreakable collision sound
                    obj.getSound(2).getClip().setFramePosition(0);
                }
                for (int j = 0; j < obj.getBreakableWallSize(); j++){
                    BreakWall tempWall2 = obj.getBreakableWalls().get(j);
                    if((tempWall2.getWallRectangle().intersects(this.x, this.y, this.width, this.height)) && visible){
                        obj.getBreakableWalls().remove(j);
                        tempWall2.breakWall();
                        this.visible = false;
                        obj.playSound(3);// breakable collision sound
                        obj.getSound(3).getClip().setFramePosition(0);
                    }
                }
            }
    }
}
