import java.awt.Color;
public class Rectangle{
    private double x_pos;
    private double y_pos;
    private double width;
    private double height;
    private Color color;

    public Rectangle(double x, double y, double w, double h){
        x_pos = x;
        y_pos = y;
        width = w;
        height = h;
    }
    public double calculatePerimeter(){
        return width * 2 + height * 2;
    }
    public double calculateArea(){
        return width * height;
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



