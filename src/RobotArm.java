import java.io.IOException;
import java.io.PrintWriter;

public class RobotArm {
    public RobotArm() {
        try {
            horizontalLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void horizontalLine() throws IOException {
        PrintWriter writer = new PrintWriter("horizontalLine.txt");

        writer.println("1450,1550,1200");
        writer.println("1450,1550,1800");
        writer.println("1000,1200,1800");
        writer.println("1000,1200,1200");

        writer.flush();
        writer.close();
    }


    public static void main(String[] args) {
        new RobotArm();
    }
}
