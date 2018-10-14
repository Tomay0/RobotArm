/**
 * 2D point
 *
 * Can convert to angles
 *
 */
public class Point {
    /**
     * Constants for working with coordinate space
     */
    //private static final double scRATIO = 0.68;//ratio of the coordinate space (length 1) to the distance between shoulder joints // nice
    //private static final double shRATIO = 2;//ratio between the distance between shoulder joints and the humerus
    private static final double shoulderDistance = 1;//distance between shoulders
    private static final double humerusLength = 2;//length of humerus
    private static final double maxDistance = Math.sqrt(Math.pow(humerusLength, 2) - Math.pow(humerusLength - shoulderDistance / 2, 2));//distance from the shoulders to the closest y that is allowed
    private static final double shoulderY = 1 + maxDistance;
    private static final Point leftShoulderPos = new Point(0, shoulderY);
    private static final Point rightShoulderPos = new Point(1, shoulderY);

    private double x,y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return x + ", " + y;

    }

    /**
     * Get the angles that the robot arms should be set to given a point
     */
    public double[] calculateAngles() throws IllegalArgumentException{

        // █▀▀▄ █▀▀█ █▀▀▄ ▀▀█▀▀   ▀▀█▀▀ █▀▀█ █░░█ █▀▀ █░░█ █
        // █░░█ █░░█ █░░█ ░░█░░   ░░█░░ █░░█ █░░█ █░░ █▀▀█ ▀
        // ▀▀▀░ ▀▀▀▀ ▀░░▀ ░░▀░░   ░░▀░░ ▀▀▀▀ ░▀▀▀ ▀▀▀ ▀░░▀ ▄


        double[] angles = new double[2];

        if (dist(leftShoulderPos) > 2 * humerusLength || dist(rightShoulderPos) > 2 * humerusLength){
            throw new IllegalArgumentException("The point " + this + " is out of range! OOPS!");
        }
        // finding mid points (aka 'A')
        Point midLeft = new Point((x + leftShoulderPos.x) / 2, (y + leftShoulderPos.y) / 2);
        Point midRight = new Point((x + rightShoulderPos.x) / 2, (y + rightShoulderPos.y) / 2);
        // finding distance between midpoint and joint (aka 'h')
        double midToJointLeft = Math.sqrt(Math.pow(humerusLength, 2) - Math.pow(dist(leftShoulderPos) / 2, 2));
        double midToJointRight = Math.sqrt(Math.pow(humerusLength, 2) - Math.pow(dist(rightShoulderPos) / 2, 2));

        // finding angle between hand and joint (aka 'a')
        double angleFromMidToJointLeft = Math.acos((leftShoulderPos.x-x) / dist(leftShoulderPos));
        double angleFromMidToJointRight = Math.acos((rightShoulderPos.x-x) / dist(rightShoulderPos));

        // finding joint position (aka 'x3', 'y3', 'x4', 'y4')
        Point leftJointPos = new Point(midLeft.x - midToJointLeft * Math.sin(angleFromMidToJointLeft), midLeft.y + midToJointLeft * Math.cos(angleFromMidToJointLeft));
        Point rightJointPos = new Point(midRight.x + midToJointRight * Math.sin(angleFromMidToJointRight), midRight.y - midToJointRight * Math.cos(angleFromMidToJointRight));

        // finding angle of sholder
        double LeftShoulderAngle = Math.atan2((leftJointPos.x - leftShoulderPos.x), (leftJointPos.y - leftShoulderPos.y));
        double RightShoulderAngle = Math.atan2((rightJointPos.x - rightShoulderPos.x), (rightJointPos.y - rightShoulderPos.y));

        angles[0] = -LeftShoulderAngle-Math.PI;
        angles[1] = -RightShoulderAngle-Math.PI;


        while(angles[0]<-Math.PI/2) angles[0] += 2*Math.PI;
        while(angles[1]<-Math.PI/2) angles[1] += 2*Math.PI;


        return angles;
    }

    /**
     * returns the distance between its self and anouther point
     */
    public double dist(Point otherPoint){
        return Math.sqrt(Math.pow(x - otherPoint.x, 2) + Math.pow(y - otherPoint.y, 2));
    }

    public double getX() {return x;}
    public double getY() {return y;}
}
