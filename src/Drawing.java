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
        writer.println(leftArm + "," + rightArm + "," + (penDown ? Constants.PEN_DOWN : Constants.PEN_UP));
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
        leftArm = (int) (Constants.left0degrees + Math.toDegrees(angles[0])*Constants.leftGradient);
        rightArm = (int) (Constants.right0degrees + Math.toDegrees(angles[1])*Constants.rightGradient);
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
     * Gets an Arc
     */
    public List<Point> getArc(double x1, double y1, double radius, double from, double to, int subdivisions) {
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
        return points;
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
        List<Point> pointsPart1 = new ArrayList<>();
        pointsPart1.addAll(getArc(x, y + size / 2, size / 4, -Math.PI/4,Math.PI, 50));
        Collections.reverse(pointsPart1);
        List<Point> pointsPart2 = new ArrayList<>();
        pointsPart2.addAll(getArc(x, y, size / 4, Math.PI * 0.75,Math.PI * 2, 50));
        pointsPart2.addAll(pointsPart1);
        lines.add(pointsPart2);
        return size * 0.7;
    }
    public double draw_K(double x, double y, double size){
        drawLine(x, y, x, y + size, 50);
        drawLine(x, y + size / 2, x + size / 2, y, 50);
        drawLine(x, y + size / 2, x + size / 2, y + size, 50);
        return size * 0.7;
    }
    /*public double draw_I(double x, double y, double size){
        drawLine(x + size / 2, y, x + size / 2, y + size, 50);
        drawLine(x, y, x + size, y, 50);
        drawLine(x, y + size, x + size, y + size, 50);
        return size * 1.2;
    }*/
    public double draw_Y(double x, double y, double size){
        drawLine(x + size * 0.75, y, x , y + size, 50);
        drawLine(x, y, x + size * 0.75 / 2, y + size / 2, 50);
        return size * 0.95;
    }
    public double draw_N(double x, double y, double size){
        drawLine(x , y, x, y + size, 50);
        drawLine(x + size * 0.75, y, x + size * 0.75, y + size, 50);
        drawLine(x, y, x + size * 0.75, y + size, 50);
        return size * 0.95;
    }
    public double draw_E(double x, double y, double size){
        drawLine(x, y, x, y + size, 50);
        drawLine(x, y, x + size / 2, y, 50);
        drawLine(x, y + size / 2, x + size / 4, y + size / 2, 50);
        drawLine(x, y + size, x + size / 2, y + size, 50);
        return size * 0.7;
    }
    public double draw_T(double x, double y, double size){
        drawLine(x + size / 2, y, x + size / 2, y + size, 50);
        drawLine(x, y, x + size, y, 50);
        return size * 1.2;
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
     * Get the number of motor controls in the drawing
     */
    public int getMotorSignalCount() {
        int motorSignalCount = 0;
        for(List<Point> line : lines) {
            motorSignalCount+=2 + line.size();
        }
        return motorSignalCount;
    }
}
