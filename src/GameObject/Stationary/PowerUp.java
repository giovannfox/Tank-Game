package src.GameObject.Stationary;

import src.GameObject.Moveable.Tank;
import src.GameWorld;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.Observer;
import java.util.Observable;

public class PowerUp extends Stationary implements Observer {
    boolean healthPack = false;
    public PowerUp(int x, int y, int width, int height, BufferedImage image) {
        super(x, y, width, height, image);
    }
    public void draw(Graphics graphics) {
        if (!healthPack)
            graphics.drawImage(this.image, this.x, this.y, this);
    }
    @Override
    public void update(Observable observable, Object object) {
        update();
    }
    public void update(){
        Tank p1 = GameWorld.getTank(1);
        Tank p2 = GameWorld.getTank(2);
        if (p1.collision(this)) {
            p1.healthUp();
            healthPack = true;
        }
        else if (p2.collision(this)) {
            p2.healthUp();
            healthPack = true;
        }
    }
}
