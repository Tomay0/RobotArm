import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Drawing {
    private List<List<Point>> lines = new ArrayList<>();

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
    public void addHorizontalLine() {
        
    }

    /**
     * Save all lines to a file
     */
    public void saveLines(String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(fileName));
        for(List<Point> line : lines) {
            for(Point p : line) {
                writer.print(p.getX() + "," + p.getY() + " ");
            }
            writer.println();
        }
        writer.flush();
        writer.close();
    }
}
