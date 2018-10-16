import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents several lines separated by pen up/pen down movements.
 * Contains methods for saving to a file that can be loaded by the robot arm program
 */
public class Drawing {
    //CONSTANTS
    private static final int PEN_DOWN = 900;//motor signal for pen down
    private static final int PEN_UP = 800;//motor signal for pen up
    private static final int left0degrees = 1775;//motor signal for 0 degrees on the left arm (90 in the program)
    private static final int right0degrees = 1260;//motor signal for 0 degrees on the right arm (90 in the program)
    private static final int leftGradient = 200/22;//linear gradient for the left arm
    private static final int rightGradient = 200/20;//linear gradient for the right arm

    //THE DRAWING
    //list of lines
    private List<List<Point>> lines = new ArrayList<>();

    //FILE WRITING
    private boolean penDown;//whether the pen should be currently down
    private int leftArm;//left control signal
    private int rightArm;//right control signal
    PrintWriter writer;//file to write to

    /**
     * Initialises an empty drawing
     */
    public Drawing() {
        penDown = false;
        leftArm = 0;
        rightArm = 0;
        writer = null;
    }

    /**
     * Prints to the file the current motor signals
     */
    public void print() {
        writer.println(leftArm + "," + rightArm + "," + (penDown ? PEN_DOWN : PEN_UP));
    }


    /**
     * Changes if the pen is up or down
     */
    public void setPenDown(boolean pen) {
        this.penDown = pen;
        print();
    }

    /**
     * Moves the pen to a point
     * Angles are calculated, then motors signals are calculated
     */
    public void movePenTo(Point p) {
        double[] angles = p.calculateAngles();//calculate angles

        //calculate motor signals
        leftArm = (int) (left0degrees + Math.toDegrees(angles[0])*leftGradient);
        rightArm = (int) (right0degrees + Math.toDegrees(angles[1])*rightGradient);
        print();
    }

    /**
     * Save all control signals to a file
     */
    public void saveLines(File file) {
        try {
            writer = new PrintWriter(file);
            for(List<Point> line : lines) {//go through all lines
                for (int i = 0;i<line.size();i++) {
                    movePenTo(line.get(i));//move pen to correct location
                    if(i==0) setPenDown(true);//set pen down after moving the first time
                }
                setPenDown(false);//lift pen up
            }
            writer.flush();
            writer.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the drawing
     */
    public void clearDrawing() {
        lines.clear();
    }
    /**
     * Draws a line
     */
    public void drawLine(double x1, double y1, double x2, double y2, int subdivisions) {
        lines.add(getLine(x1,y1,x2,y2,subdivisions));
    }

    /**
     * Draws a rectangle
     */
    public void drawRect(double x1, double y1, double width, double height, int subdivisions) {
        List<Point> points = new ArrayList<>();
        points.addAll(getLine(x1,y1,x1+width,y1,subdivisions));
        points.addAll(getLine(x1+width,y1,x1+width,y1+height,subdivisions));
        points.addAll(getLine(x1+width,y1+height,x1,y1+height,subdivisions));
        points.addAll(getLine(x1,y1+height,x1,y1,subdivisions));
        lines.add(points);
    }

    /**
     * Draws a an Arc
     */
    public void drawArc(double x1, double y1, double radius, double from, double to, int subdivisions) {
        List<Point> points = new ArrayList<>();
        // getting center
        double centerX = x1 + radius;
        double centerY = y1 + radius;

        //System.out.println("center: " + centerX + ", " + centerY);

        //current position in the circle
        double currentX;
        double currentY;

        // starts from bottom and goes anti-clockwise
        double rot = from;
        for (int i = 0; i <= subdivisions; i++){
            rot += (to-from) / subdivisions;
            currentX = Math.sin(rot) * radius + centerX;
            currentY = Math.cos(rot) * radius + centerY;
            //System.out.println("point at: " + currentX + ", " + currentY);

            points.add(new Point(currentX,currentY));
        }
        lines.add(points);
    }

    /**
     * Draws a circle
     */
    public void drawCircle(double x1, double y1, double radius, int subdivisions) {
        drawArc(x1, y1, radius, 0, 2 * Math.PI, subdivisions);
    }

    /**
     * Returns a list of points that make a line with different amounts of subdivisions.
     */
    public List<Point> getLine(double x1, double y1, double x2, double y2, int subdivisions) {
        List<Point> points = new ArrayList<>();
        for(double t = 0;t<=1;t+=1.0/subdivisions) {
            points.add(new Point(x1 * (1-t) + x2 * t,y1 * (1-t) + y2 * t));
        }
        return points;
    }

    ////////GETTERS AND SETTERS///////


    /**
     * Adds a line to the list
     */
    public void addLine(List<Point> line) {
        if(line.size()>1) {
            lines.add(line);
        }
    }

    /**
     * List of all lines
     */
    public List<List<Point>> getLines() {
        return Collections.unmodifiableList(lines);
    }


    /**
     * Draws the word "SKYNET"
     * size is the width/height of letters. x,y is the top left corner
     */
    public void drawSkynet(double x, double y, double size){
        x += draw_S(x, y, size);
        x += draw_K(x, y, size);
        x += draw_Y(x, y, size);
        x += draw_N(x, y, size);
        x += draw_E(x, y, size);
        x += draw_T(x, y, size);

    }


    /**
     * draws letter and returns letter space
     */
    public double draw_S(double x, double y, double size){
        drawArc(x, y, size / 4, Math.PI * 2, Math.PI, 40);
        //drawArc(x + size / 2, y, size / 2, Math.PI, Math.PI * 2, 40);
        return size * 0.7;
    }
    public double draw_K(double x, double y, double size){
        drawLine(x, y, x, y + size, 10);
        drawLine(x, y + size / 2, x + size, y, 10);
        drawLine(x, y + size / 2, x + size, y + size, 10);
        return size * 1.2;
    }
    public double draw_I(double x, double y, double size){
        drawLine(x + size / 2, y, x + size / 2, y + size, 10);
        drawLine(x, y, x + size, y, 10);
        drawLine(x, y + size, x + size, y + size, 10);
        return size * 1.2;
    }
    public double draw_Y(double x, double y, double size){
        drawLine(x + size, y, x , y + size, 10);
        drawLine(x, y, x + size / 2, y + size / 2, 10);
        return size * 1.2;
    }
    public double draw_N(double x, double y, double size){
        drawLine(x , y, x, y + size, 10);
        drawLine(x + size, y, x + size, y + size, 10);
        drawLine(x, y, x + size, y + size, 10);
        return size * 1.2;
    }
    public double draw_E(double x, double y, double size){
        drawLine(x, y, x, y + size, 10);
        drawLine(x, y, x + size, y, 10);
        drawLine(x, y + size / 2, x + size / 2, y + size / 2, 10);
        drawLine(x, y + size, x + size, y + size, 10);
        return size * 1.2;
    }
    public double draw_T(double x, double y, double size){
        drawLine(x + size / 2, y, x + size / 2, y + size, 10);
        drawLine(x, y, x + size, y, 10);
        return size * 1.2;
    }
}
