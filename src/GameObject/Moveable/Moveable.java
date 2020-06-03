package src.GameObject.Moveable;

import java.awt.image.BufferedImage;
import src.GameObject.GameObject;

public class Moveable extends GameObject {
    protected int velocity;
    public Moveable(BufferedImage image, int x, int y, int velocity){
        super(x, y, image, null);
        this.velocity = velocity;
    }
    public Moveable(){
    }
}
