package src;

import src.GameObject.Moveable.Tank;
import java.util.Observable;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Keyboard extends Observable implements KeyListener {
    private final Tank tank;
    private final int fire;
    private final int up;
    private final int down;
    private final int right;
    private final int left;

    public Keyboard(Tank tank) {
        this.tank = tank;
        this.fire = tank.getFireKey();
        this.up = tank.getUp();
        this.down = tank.getDown();
        this.right = tank.getRight();
        this.left = tank.getLeft();
    }
    @Override
    public void keyTyped(KeyEvent event) {
    }
    @Override
    public void keyPressed(KeyEvent event)
    {
        int key = event.getKeyCode();
        if (key == this.fire) {
            this.tank.switchShootOn();
        }
        if (key == this.up) {
            this.tank.switchUpOn();
        }
        if (key == this.down) {
            this.tank.switchDownOn();
        }
        if (key == this.right) {
            this.tank.switchRightOn();
        }
        if (key == this.left) {
            this.tank.switchLeftOn();
        }
    }
    @Override
    public void keyReleased(KeyEvent event)
    {
        int key = event.getKeyCode();
        if (key == this.fire) {
            this.tank.switchShootOff();
        }
        if (key == this.up) {
            this.tank.switchUpOff();
        }
        if (key == this.down) {
            this.tank.switchDownOff();
        }
        if (key == this.right) {
            this.tank.switchRightOff();
        }
        if (key == this.left) {
            this.tank.switchLeftOff();
        }
    }
}
