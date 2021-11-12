// FractalDrawer class draws a fractal of a shape indicated by user input
import java.awt.*;
import java.util.Scanner; // for user input

public class FractalDrawer {
    private double totalArea = 0;  // member variable for tracking the total area
    public FractalDrawer() {
        // constructor
    }

    // drawFractal creates a new Canvas object and determines which shapes to draw a fractal by calling appropriate helper function, returning the calculated total area
    public double drawFractal(String type) {
        Canvas myCanvas = new Canvas();
        if (type.equals("Triangle") || type.equals("triangle")){
            drawTriangleFractal(100, 100, 400, 400, Color.RED, myCanvas, 7);
        }
        else if (type.equals("Circle") || type.equals("circle")){
            drawCircleFractal(100, 400, 400, Color.RED, myCanvas, 7);
        }
        else if (type.equals("Rectangle") || type.equals("rectangle")){
            drawRectangleFractal(200, 100, 400, 400, Color.RED, myCanvas, 7);
        }
        else {
            System.out.println("Invalid shape.");
        }
        return totalArea;
    }

    // drawTriangleFractal draws a triangle fractal using recursive techniques
    public void drawTriangleFractal(double width, double height, double x, double y, Color c, Canvas can, int level) {
        if (level == 0) {
            return;
        }
        else {
            Triangle myTriangle = new Triangle(x, y, width, height);
            myTriangle.setColor(c);
            can.drawShape(myTriangle);
            totalArea += myTriangle.calculateArea();
            if (c.equals(Color.RED)){
                c = Color.BLUE;
            }
            else if (c.equals(Color.BLUE)){
                c=Color.GREEN;
            }
            else if (c.equals(Color.GREEN)){
            c=Color.RED;
            }
            drawTriangleFractal(width / 2, height / 2, x - width / 2, y, c, can, level-1); // left
            drawTriangleFractal(width / 2, height / 2, x + width / 4, y - height, c, can, level-1);// upper middle
            drawTriangleFractal(width / 2, height / 2, x + width, y, c, can, level-1); //right
        }

    }
    // drawCircleFractal: draws a circle fractal using recursive techniques
    public void drawCircleFractal(double radius, double x, double y, Color c, Canvas can, int level) {
        if (level == 0) {
            return;
        } else {
            Circle myCircle = new Circle(x, y, radius);
            myCircle.setColor(c);
            can.drawShape(myCircle);
            totalArea += myCircle.calculateArea();
            if (c.equals(Color.RED)) {
                c = Color.BLUE;
            } else if (c.equals(Color.BLUE)) {
                c = Color.GREEN;
            } else if (c.equals(Color.GREEN)) {
                c = Color.RED;
            }
        }
        drawCircleFractal(radius / 2, x - 1.5 * radius, y + 1.5 * radius, c, can, level - 1); // lower left
        drawCircleFractal(radius / 2, x - 1.5 * radius, y - 1.5 * radius, c, can, level - 1); //upper left
        drawCircleFractal(radius / 2, x + 1.5 * radius, y + 1.5 * radius, c, can, level - 1); //upper right
        drawCircleFractal(radius / 2, x + 1.5 * radius, y - 1.5 * radius, c, can, level - 1); //lower right
    }
    // drawRectangleFractal: draws a rectangle fractal using recursive techniques
    public void drawRectangleFractal(double width, double height, double x, double y, Color c, Canvas can, int level) {
        if (level == 0) {
            return;
        }
        else {
            Rectangle myRectangle = new Rectangle(x, y, width, height);
            myRectangle.setColor(c);
            can.drawShape(myRectangle);
            totalArea += myRectangle.calculateArea();
            if (c.equals(Color.RED)){
            c = Color.BLUE;
            }
            else if (c.equals(Color.BLUE)){
            c=Color.GREEN;
            }
            else if (c.equals(Color.GREEN)){
            c=Color.RED;
            }
            drawRectangleFractal(width / 2, height / 2, x - width / 2, y + height, c, can, level-1); // lower left
            drawRectangleFractal(width / 2, height / 2, x - width / 2, y - height / 2, c, can, level-1); // upper left
            drawRectangleFractal(width / 2, height / 2, x + width, y + height, c, can, level-1); // lower right
            drawRectangleFractal(width / 2, height / 2, x + width, y - height / 2, c, can, level-1); // upper right
        }
    }

    // main: asks user for shape input, and then draw the corresponding fractal.
    public  static void main(String[] args){
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter a shape (triangle, circle, or rectangle): ");
        String user_shape = myScanner.nextLine();
        FractalDrawer myFractalDrawer = new FractalDrawer(); //creating new fractal drawer object
        myFractalDrawer.drawFractal(user_shape);
        System.out.println("Area is: " + myFractalDrawer.totalArea);
    }

}
