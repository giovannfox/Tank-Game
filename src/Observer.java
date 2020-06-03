package src;

import src.GameObject.GameObject;
import java.util.Observable;
import java.awt.event.KeyEvent;

public class Observer extends Observable{
    private Object target;
    private Object caller;
    private int type;

    public void Bump(GameObject call, GameObject targ) {
        type = 1;
        this.caller = call;
        this.target = targ;
        setChanged();
        this.notifyObservers(this);
    }
    public void setKeys(KeyEvent k) {
        type = 2;
        this.target = k;
        setChanged();
        notifyObservers(this);
    }
    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }
    public void setType(int t) {
        this.type = t;
    }
    public Object getTarget() {
        return target;
    }
    public Object getCall() {
        return caller;
    }
    public int getType() {
        return type;
    }
}
