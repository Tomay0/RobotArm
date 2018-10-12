import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Drawing {
    private List<List<Point>> lines = new ArrayList<>();
    private static final int PEN_DOWN = 900;
    private static final int PEN_UP = 800;

    private static final int left0degrees = 1775;
    private static final int right0degrees = 1260;
    private static final int leftGradient = 200/22;
    private static final int rightGradient = 200/20;

    private boolean penDown;
    private int leftArm;//control signal values
    private int rightArm;
    PrintWriter writer;

    public Drawing() {
        penDown = false;
        leftArm = 0;
        rightArm = 0;
        writer = null;
    }

    public void print() {
        writer.println(leftArm + "," + rightArm + "," + (penDown ? PEN_DOWN : PEN_UP));
    }



    public void setPenDown(boolean pen) {
        this.penDown = pen;
        print();
    }

    public void movePenTo(Point p) {
        double[] angles = p.calculateAngles();
        leftArm = (int) (left0degrees + Math.toDegrees(angles[0])*leftGradient);
        rightArm = (int) (right0degrees + Math.toDegrees(angles[1])*rightGradient);
        print();
    }

    /**
     * Adds an arbitrary line to the list
     */
    public void addLine(List<Point> line) {
        if(line.size()>1) {
            lines.add(line);
        }
    }
    /**
     * Adds a horizontal line to the list
     */
    public void drawLine(double x1, double y1, double x2, double y2) {
        lines.add(getLine(x1,y1,x2,y2,10));
    }

    /**
     * Returns a list of points that make a line.
     */
    public List<Point> getLine(double x1, double y1, double x2, double y2, int subdivisions) {
        List<Point> points = new ArrayList<>();
        for(double t = 0;t<=1;t+=1.0/subdivisions) {
            points.add(new Point(x1 * (1-t) + x2 * t,y1 * (1-t) + y2 * t));
        }
        return points;
    }

    /**
     * Draws a rectangle
     */
    public void drawRect(double x1, double y1, double width, double height) {
        List<Point> points = new ArrayList<>();
        points.addAll(getLine(x1,y1,x1+width,y1,30));
        points.addAll(getLine(x1+width,y1,x1+width,y1+height,30));
        points.addAll(getLine(x1+width,y1+height,x1,y1+height,30));
        points.addAll(getLine(x1,y1+height,x1,y1,30));
        lines.add(points);
    }

    public void randomBounds(double x1, double y1, double width, double height) {
        List<Point> points = new ArrayList<>();
        for(int i = 0;i<1000;i++) {
            points.add(new Point(x1 + width * Math.random(), y1 + height*Math.random()));
        }
        lines.add(points);
    }

    /**
     * Save all lines to a file
     */
    public void saveLines(String fileName) {
        try {
            writer = new PrintWriter(new File(fileName));
        /*for(List<Point> line : lines) {
            for(Point p : line) {
                writer.print(p.getX() + "," + p.getY() + " ");
            }
            writer.println();
        }*/
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
}
