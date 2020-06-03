package src.GameObject;

import java.awt.Rectangle;
import java.awt.Point;
import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.image.ImageObserver;
import java.awt.Image;

public abstract class GameObject extends JComponent {
    protected Rectangle r;
    protected BufferedImage image;
    public int x;
    public int y;
    protected int width;
    protected int height;

    public GameObject( int x, int y, int w, int h, BufferedImage image){
        this.image = image;
        this.r = new Rectangle( x, y, this.width, this.height);
        this.width = w;
        this.height = h;
        this.x = x;
        this.y = y;
    }
    public GameObject(BufferedImage i, ImageObserver observer){
        this.image = i;
        this.x = 0;
        this.y = 0;
        try {
            this.width = image.getWidth(observer);
            this.height = image.getHeight(observer);
        } catch (NullPointerException e) {
            System.out.println("Error\n");
            this.width = 0;
            this.height = 0;
        }
        this.r.setBounds(this.x, this.y, this.width, this.height);
    }
    public GameObject(int x, int y, BufferedImage i, ImageObserver observer){
        this.x = x;
        this.y = y;
        this.image = i;
        try {
            this.width = image.getWidth(observer);
            this.height = image.getHeight(observer);
        } catch (NullPointerException e) {
            System.out.println("Error\n");
            this.width = 0;
            this.height = 0;
        }
        this.r = new Rectangle( x, y, this.width, this.height);
    }
    @Override
    public int getHeight(){
        return this.height;
    }
    @Override
    public int getWidth(){
        return this.width;
    }
    @Override
    public int getX() {
        return this.x;
    }
    @Override
    public int getY() {
        return this.y;
    }
    //Location
    @Override
    public void setLocation(int nextX, int nextY){
        this.x = nextX;
        this.y = nextY;
        this.r = new Rectangle(nextX, nextY);
    }
    @Override
    public void setLocation(Point nextLocation){
        this.x = nextLocation.x;
        this.y = nextLocation.y;
        this.r.setLocation(nextLocation);
    }
    public void setX(int newX) {
        this.x = newX;
        this.r.setLocation(newX, this.y);
    }
    public void setY(int newY) {
        this.y = newY;
        this.r.setLocation(this.x, newY);
    }
    public void setWidth(int newWidth) {
        this.width = newWidth;
        this.r.setSize(newWidth, this.height);
    }
    public void setHeight(int newHeight) {
        this.height = newHeight;
        this.r.setSize(this.width, newHeight);
    }
    public void setObjectRectangle(int x, int y, int width, int height) {
        this.r = new Rectangle(x, y, width, height);
    }
    public void setImage(BufferedImage img) {
        this.image = img;
    }
    public void setImage(BufferedImage image, ImageObserver observer){
        this.image = image;
        try {
            this.height = image.getWidth(observer);
            this.width = image.getHeight(observer);
        } catch (NullPointerException e) {
            this.height = 0;
            this.width = 0;
        }
        this.r.setSize(this.width, this.height);
    }
    @Override
    public Point getLocation(){
        return new Point(this.x, this.y);
    }
    //Size
    @Override
    public void setSize(Dimension nextSize){
        this.r.setSize(nextSize);
    }
    @Override
    public void setSize(int nextWidth, int nextHeight){
        this.width = nextWidth;
        this.height = nextHeight;
        this.r.setSize( nextWidth, nextHeight);
    }
    @Override
    public Dimension getSize() {
        return this.r.getSize();
    }
    //Default const
    public GameObject(){
    }
    public Rectangle getObjectRectangle() {
        return this.r;
    }
    public Image getImage() {
        return this.image;
    }
}
