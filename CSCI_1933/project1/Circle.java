import java.awt.Color;

public class Circle{
    private double PI  = 3.14159; // should we import math?
    private double x_pos;
    private double y_pos;
    private double radius;
    private Color color;
    public Circle(double x, double y, double r){
        x_pos = x;
        y_pos = y;
        radius = r;
    }
    public double calculatePerimeter(){
        return 2 * PI * radius;
    }

    public double calculateArea(){
        return PI * radius * radius;
    }

    public void setColor(Color c){
        color = c;
    }

    public void setPos(double x, double y){
        x_pos = x;
        y_pos = y;
    }

    public void setRadius(double r){
        radius = r;
    }

    public Color getColor(){
        return color;
    }

    public double getXPos(){
        return x_pos;

    }

    public double getYPos(){

        return y_pos;
    }

    public double getRadius(){

        return radius;
    }


}