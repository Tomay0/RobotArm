import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class ImageProcess {
    private static final int THRESHOLD = 150;//threshold for detecting edges
    private int width,height;

    private int[][] pixels;
    private int[][] edgeValues;
    private List<List<Point>> lines;

    /**
     * Process an image
     */
    public ImageProcess() {
        System.out.print("Enter file: ");
        Scanner scan = new Scanner(System.in);
        try {
            load(scan.next());
            findEdgeValues();
            findLines();
            saveLines("lineSimulation/data/lines.txt");
        }catch(Exception e) {
            System.out.println("Could not load an image from that file.");
        }
    }

    /**
     * Load black and white pixels from an image to array
     */
    public void load(String fileName) throws IOException {
        BufferedImage image = ImageIO.read(new File(fileName));
        width = image.getWidth();
        height = image.getHeight();

        //load pixels array of all the luminosities at each pixel
        pixels = new int[width][height];
        for(int x = 0;x<width;x++) {
            for(int y = 0;y<height;y++) {
                pixels[x][y] = getLuminosity(new Color(image.getRGB(x,y)));
            }
        }
    }

    /**
     * Find edges in the image
     */
    public void findEdgeValues() {
        //find edges
        edgeValues = new int[width][height];
        for(int y = 0;y<height;y++) {
            for(int x =0;x<width;x++) {
                //indices of left/right bottom/top. This is to avoid checking pixels outside edges of the screen
                int top = y==0 ? 0 : y-1;
                int bottom = y==height-1 ? height-1 : y+1;
                int left = x==0 ? 0 : x-1;
                int right = x==width-1 ? width-1 : x+1;

                //vertical edges
                int vertical = -1 * pixels[left][top] + 1 * pixels[right][top] +
                        -2 * pixels[left][y] + 2 * pixels[right][y] +
                        -1 * pixels[left][bottom] + 1 * pixels[right][bottom];

                //horizontal edges
                int horizontal = 1 * pixels[left][top] + 2 * pixels[x][top] + 1 * pixels[right][top] +
                        -1 * pixels[left][bottom] + -2 * pixels[x][bottom] + -1 * pixels[right][bottom];

                //combine
                edgeValues[x][y] = (int)Math.sqrt(vertical * vertical + horizontal * horizontal);
            }
        }
    }

    /**
     * Find lines in the edges
     */
    public void findLines() {
        lines = new ArrayList<>();
        //loop
        for(int y = 0;y<height;y++) {
            for(int x =0;x<width;x++) {
                if(edgeValues[x][y]>THRESHOLD) {
                    edgeValues[x][y] = 0;//get rid of pixel

                    //start of line
                    List<Point> line = new ArrayList<>();
                    line.add(new Point(x,y));

                    //keep adding points to line that surround until none are greater than the threshold
                    Point nearMaxEdge = getNearestEdgeMax(x,y);
                    while(edgeValues[(int)nearMaxEdge.getX()][(int)nearMaxEdge.getY()] > THRESHOLD) {
                        edgeValues[(int)nearMaxEdge.getX()][(int)nearMaxEdge.getY()] = 0;//get rid of pixel
                        line.add(nearMaxEdge);//add point to line
                        nearMaxEdge = getNearestEdgeMax((int)nearMaxEdge.getX(),(int)nearMaxEdge.getY());//find next pixel
                    }
                    //put the line into the list
                    lines.add(line);
                }
            }
        }
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

    /**
     * Find pixel surrounding x,y that has the largest edge value
     */
    private Point getNearestEdgeMax(int x, int y) {
        Point point = null;
        int maxValue = 0;

        for(int xoff = -1; xoff<=1;xoff++) {//loop through offsets
            for(int yoff = -1; yoff<=1;yoff++) {
                if(x+xoff >=0 && x+xoff<width && y+yoff>=0 && y+yoff<height) {//check within bounds
                    int edgeValue = edgeValues[x+xoff][y+yoff];
                    if(edgeValue>=maxValue) {
                        maxValue = edgeValue;
                        point = new Point(x+xoff,y+yoff);
                    }
                }
            }
        }
        return point;
    }

    /**
     * Returns the luminosity for a given colour
     */
    private int getLuminosity(Color color) {
        return (int)(0.2126*color.getRed() + 0.7152*color.getGreen() + 0.0722*color.getBlue());
    }
}
