/**
 * 2D point
 *
 * Can convert to angles for motor control
 */
public class Point {
    //CONSTANTS FOR WORKING WITH THE COORDINATE SPACE
    private static final double armDistance = 1;//distance between motors
    private static final double armLength = 2;//length of arms
    private static final double maxDistance = Math.sqrt(Math.pow(armLength, 2) - Math.pow(armLength - armDistance / 2, 2));//distance from the motors to the closest y that is allowed
    private static final double motorY = 1 + maxDistance;//Y at which the motors are located at
    private static final Point leftMotorPos = new Point(0, motorY);//left motor
    private static final Point rightMotorPos = new Point(1, motorY);//right motor


    //x,y
    private double x,y;

    /**
     * Init
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**x*/
    public double getX() {return x;}
    /**y*/
    public double getY() {return y;}

    /**
     * Point as a string
     */
    public String toString() {
        return x + ", " + y;

    }

    /**
     * returns the distance between its self and another point
     */
    public double dist(Point otherPoint){
        return Math.sqrt(Math.pow(x - otherPoint.x, 2) + Math.pow(y - otherPoint.y, 2));
    }


    /**
     * Get the angles that the robot arms should be set to given a point
     */
    public double[] calculateAngles() throws IllegalArgumentException{
        double[] angles = new double[2];

        if (dist(leftMotorPos) > 2 * armLength || dist(rightMotorPos) > 2 * armLength){
            throw new IllegalArgumentException("The point " + this + " is out of range! OOPS!");
        }
        // finding mid points (aka 'A')
        Point midLeft = new Point((x + leftMotorPos.x) / 2, (y + leftMotorPos.y) / 2);
        Point midRight = new Point((x + rightMotorPos.x) / 2, (y + rightMotorPos.y) / 2);
        // finding distance between midpoint and joint (aka 'h')
        double midToJointLeft = Math.sqrt(Math.pow(armLength, 2) - Math.pow(dist(leftMotorPos) / 2, 2));
        double midToJointRight = Math.sqrt(Math.pow(armLength, 2) - Math.pow(dist(rightMotorPos) / 2, 2));

        // finding angle between hand and joint (aka 'a')
        double angleFromMidToJointLeft = Math.acos((leftMotorPos.x-x) / dist(leftMotorPos));
        double angleFromMidToJointRight = Math.acos((rightMotorPos.x-x) / dist(rightMotorPos));

        // finding joint position (aka 'x3', 'y3', 'x4', 'y4')
        Point leftJointPos = new Point(midLeft.x - midToJointLeft * Math.sin(angleFromMidToJointLeft), midLeft.y + midToJointLeft * Math.cos(angleFromMidToJointLeft));
        Point rightJointPos = new Point(midRight.x + midToJointRight * Math.sin(angleFromMidToJointRight), midRight.y - midToJointRight * Math.cos(angleFromMidToJointRight));

        // finding angle of sholder
        double LeftShoulderAngle = Math.atan2((leftJointPos.x - leftMotorPos.x), (leftJointPos.y - leftMotorPos.y));
        double RightShoulderAngle = Math.atan2((rightJointPos.x - rightMotorPos.x), (rightJointPos.y - rightMotorPos.y));

        angles[0] = -LeftShoulderAngle-Math.PI;
        angles[1] = -RightShoulderAngle-Math.PI;


        while(angles[0]<-Math.PI/2) angles[0] += 2*Math.PI;
        while(angles[1]<-Math.PI/2) angles[1] += 2*Math.PI;


        return angles;
    }
}
