import java.io.IOException;
import java.io.PrintWriter;

public class RobotArm {
    private static final int PEN_DOWN = 1800;
    private static final int PEN_UP = 1200;
    private static final double D_OVER_R = 0.5;

    private boolean penDown;
    private int leftArm;//control signal values
    private int rightArm;
    PrintWriter writer;

    public RobotArm() {
        try {
            writer = new PrintWriter("horizontalLine.txt");
            leftArm = 1450;
            rightArm = 1550;
            penDown = false;
            print();
            horizontalLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPenDown(boolean pen) {
        this.penDown = pen;
        print();
    }

    public void print() {
        writer.println(leftArm + "," + rightArm + "," + (penDown ? PEN_DOWN : PEN_UP));
    }

    public void horizontalLine() throws IOException {
        setPenDown(true);
        setArmDegrees(120, 50);
        setPenDown(false);
    }

    public void movePenTo(double x, double y) {
        leftArm = (int)x;
        rightArm = (int)y;
        print();
    }

    /**
     * Test - we can change this to reverse order
     */
    private void calculateXY(double left, double right) {
        double rx = Math.cos(Math.toRadians(right));
        double ry = Math.sin(Math.toRadians(right));

        double lx = Math.cos(Math.toRadians(left)) - D_OVER_R;
        double ly = Math.sin(Math.toRadians(left));

        double dx = rx-lx;//x distance between 2 arms
        double dy = ry-ly;//y distance between 2 arms
        double d = Math.sqrt(dx*dx + dy*dy);
        if(d>0.5) System.out.println("Arms are too far away");

        //midpoint of the shoulders
        double shoulderMidX = (lx+rx)*0.5;
        double shoulderMidY = (ly+ry)*0.5;

        double shoulderSlant = Math.atan2(dy,dx);

        double midShoulderToPointDist = 1-d*d/4;

        double y2 = Math.cos(shoulderSlant) * midShoulderToPointDist;
        double x2 = -Math.sin(shoulderSlant) * midShoulderToPointDist;



        //double angle = Math.acos((d/2)/1);
        double x = x2+shoulderMidX;
        double y = y2+shoulderMidY;
    }

    public void setArmDegrees(double left, double right) {
        //double leftDeg = Math.toDegrees(left);
        //double rightDeg = Math.toDegrees(right);
        leftArm = (int)(2350-left*10);
        rightArm = (int)(2450-right*10);
        print();
    }


    public static void main(String[] args) {
        new RobotArm();
    }
}
