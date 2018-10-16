import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Constants {
    public static File configFile = new File("config.txt");

    public static double MAX_LINE_LENGTH = 0.1;//split lines in half if above this length for lines
    public static double STRAIGHT_LINE_THRESHOLD = 0.004;//points on a line must be less than many pixels away to be considered straight
    public static int MINIMUM_LINE_POINTS = 5;//minimum number of pixels for a line to be drawn (prevents noise)
    public static int THRESHOLD = 100;//threshold for detecting edges
    public static double X_LEFT = -0.4;//minimum x the image will be drawn from
    public static double Y_TOP = -1.4;//minimum y the image will be drawn from
    public static double SIZE = 1.8;//maximum size of the image (width or height)
    public static int PEN_DOWN = 1605;//motor signal for pen down
    public static int PEN_UP = 1505;//motor signal for pen up
    public static int left0degrees = 1780;//motor signal for 0 degrees on the left arm (90 in the program)
    public static int right0degrees = 1250;//motor signal for 0 degrees on the right arm (90 in the program)
    public static double leftGradient = 10;//linear gradient for the left arm
    public static double rightGradient = 10;//linear gradient for the right arm

    /**
     * Load the constants from the config file
     */
    public static void loadConstantsFromFile() {
        try {
            Scanner scan = new Scanner(configFile);
            MAX_LINE_LENGTH = scan.nextDouble();
            STRAIGHT_LINE_THRESHOLD = scan.nextDouble();
            MINIMUM_LINE_POINTS = scan.nextInt();
            THRESHOLD = scan.nextInt();
            X_LEFT = scan.nextDouble();
            Y_TOP = scan.nextDouble();
            SIZE = scan.nextDouble();
            PEN_DOWN = scan.nextInt();
            PEN_UP = scan.nextInt();
            left0degrees = scan.nextInt();
            right0degrees = scan.nextInt();
            leftGradient = scan.nextDouble();
            rightGradient = scan.nextDouble();
            scan.close();
        }catch(Exception e){}
    }

    /**
     * Save constants to a file
     */
    public static void saveConstants() {
        try {
            PrintWriter writer = new PrintWriter(configFile);
            writer.println(MAX_LINE_LENGTH);
            writer.println(STRAIGHT_LINE_THRESHOLD);
            writer.println(MINIMUM_LINE_POINTS);
            writer.println(THRESHOLD);
            writer.println(X_LEFT);
            writer.println(Y_TOP);
            writer.println(SIZE);
            writer.println(PEN_DOWN);
            writer.println(PEN_UP);
            writer.println(left0degrees);
            writer.println(right0degrees);
            writer.println(leftGradient);
            writer.println(rightGradient);

            writer.flush();
            writer.close();
        }catch(Exception e){}
    }

}
