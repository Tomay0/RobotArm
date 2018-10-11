public class Point {
    /**
     * basically a PVector with extra features
     */
    private double x,y;
    private static final double humerusLength = 1;
    private static final double shoulderDistance = humerusLength / 2;
    private static final double shoulderY = 1 + Math.sqrt(Math.pow(humerusLength, 2) - Math.pow(humerusLength - shoulderDistance / 2, 2));
    private static final Point leftShoulderPos = new Point(0, shoulderY);
    private static final Point rightShoulderPos = new Point(shoulderDistance, shoulderY);

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double[] calculateAngle(){
        double[] angles = new double[2];

        if (dist(leftShoulderPos) > 2 * humerusLength || dist(rightShoulderPos) > 2 * humerusLength){
            System.out.println("not in range", 10, 40);
            return angles;
        }
        // finding mid points (aka 'A')
        Point midLeft = new Point((x + leftShoulderPos.x) / 2, (y + leftShoulderPos.y) / 2);
        Point midRight = new Point((x + rightShoulderPos.x) / 2, (y + rightShoulderPos.y) / 2);
        //line(midLeft.x, midLeft.y, midRight.x, midRight.y); // debugging

        // finding distance betwen midpoint and joint (aka 'h')
        float midToJointLeft = Math.sqrt(Math.pow(humerusLength, 2) - Math.pow(dist(leftShoulderPos) / 2, 2));
        float midToJointRight = Math.sqrt(Math.pow(humerusLength, 2) - Math.pow(dist(rightShoulderPos) / 2, 2));

        // finding angle between hand and joint (aka 'a')
        float angleFromMidToJointLeft = Math.acos((leftShoulderPos.x - pos.x) / dist(leftShoulderPos));
        float angleFromMidToJointRight = Math.acos((rightShoulderPos.x - pos.x) / dist(rightShoulderPos));

        // finding joint position (aka 'x3', 'y3', 'x4', 'y4')
        Point leftJointPos = new Point(midLeft.x - midToJointLeft * sin(angleFromMidToJointLeft), midLeft.y + midToJointLeft * cos(angleFromMidToJointLeft));
        Point rightJointPos = new Point(midRight.x + midToJointRight * sin(angleFromMidToJointRight), midRight.y - midToJointRight * cos(angleFromMidToJointRight));

        //line(leftJointPos.x, leftJointPos.y, rightJointPos.x, rightJointPos.y); // debugging

        // finding angle of sholder
        float LeftShoulderAngle = Math.atan2((leftJointPos.x - leftShoulderPos.x), (leftJointPos.y - leftShoulderPos.y));
        float RightShoulderAngle = Math.atan2((rightJointPos.x - rightShoulderPos.x), (rightJointPos.y - rightShoulderPos.y));

        angles[0] = LeftShoulderAngle;
        angles[1] = RightShoulderAngle;

        return angles;
    }

    /**
     * returns the distance between its self and anouther point
     * @param otherPoint
     * @return
     */
    public double dist(Point otherPoint){
        return Math.sqrt(Math.pow(x - otherPoint.x, 2) + Math.pow(x - otherPoint.x, 2));
    }

    public double getX() {return x;}
    public double getY() {return y;}
}
