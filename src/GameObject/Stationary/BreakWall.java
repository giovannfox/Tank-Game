package src.GameObject.Stationary;

import src.GameObject.Moveable.Tank;
import src.GameWorld;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import java.awt.image.ImageObserver;

public class BreakWall extends Wall implements Observer {
    private int height;
    private int width;
    Rectangle wallRectangle;
    private boolean demolish = false;

    public BreakWall(int x, int y, int width, int height, BufferedImage image) {
        super(x, y, width, height, image);
        this.width = image.getWidth();
        this.height = image.getHeight();
        wallRectangle = new Rectangle(x, y, width, height);
    }
    public void prod(Graphics graphics, ImageObserver observer) {
        graphics.drawImage(this.image, this.x, this.y, observer);
    }
    public void breakWall(){
        demolish = true;
    }
    @Override
    public void update(Observable observable, Object object) {
        update();
    }
    @Override
    public void update(){
        if(!demolish){
            Tank p1 = GameWorld.getTank(1);
            Tank p2 = GameWorld.getTank(2);
            if (p1.collision(this)) {
                if (p1.x > (x)) {
                    p1.x += 3;
                } else if (p1.x < (this.x)) {
                    p1.x -= 3;
                }
                if (p1.y > (this.y)) {
                    p1.y += 3;
                } else if (p1.y < this.y) {
                    p1.y -= 3;
                }
            }
            if (p2.collision(this)) {
                if (p2.x > (x)) {
                    p2.x += 3;
                } else if (p2.x < (this.x)) {
                    p2.x -= 3;
                }
                if (p2.y > (this.y)) {
                    p2.y += 3;
                } else if (p2.y < this.y) {
                    p2.y -= 3;
                }
            }
        }
    }

}
