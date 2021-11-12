import java.awt.Color;

public class Triangle{
    private double x_pos;
    private double y_pos;
    private double width;
    private double height;
    private Color color;

    public Triangle(double x, double y, double w, double h) {
        x_pos = x;
        y_pos = y;
        width = w;
        height = h;
    }

    public double calculatePerimeter(){
        //return x_pos + y_pos + width; wrong
        //all the triangle objects are isoceles (two sides are the same and bottom is diff) or equilateral
        double p = ((width / 2) ** 2 + (height) ** 2)** 0.5;
        return p * 2 + width;
    }

    public double calculateArea(){
        return width * height * 0.5;
    }

    public void setColor(Color c){
        color = c;
    }

    public void setPos(double x, double y){
        x_pos = x;
        y_pos = y;
    }

    public void setHeight(double h){
        height = h;
    }

    public void setWidth(double w){
        width = w;
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

    public double getHeight(){
        return height;
    }

    public double getWidth(){
        return width;
    }
}

