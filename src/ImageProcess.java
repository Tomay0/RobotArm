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
    private static final int THRESHOLD = 100;//threshold for detecting edges
    private int width,height;

    private int[][] pixels;
    private int[][] edgeValues;
    private Drawing drawing;
    private BufferedImage originalImg = null;

    /**
     * Process an image
     */
    public ImageProcess(String fileName) {
        try {
            load(fileName);
            findEdgeValues();
            drawing = createDrawing();
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Could not load an image from that file.");
        }
    }

    public BufferedImage getOriginalImg(){
        return originalImg;
    }

    /**
     * Load black and white pixels from an image to array
     */
    public void load(String fileName) throws IOException {
        originalImg = ImageIO.read(new File(fileName));
        width = originalImg.getWidth();
        height = originalImg.getHeight();

        //load pixels array of all the luminosities at each pixel
        pixels = new int[width][height];
        for(int x = 0;x<width;x++) {
            for(int y = 0;y<height;y++) {
                pixels[x][y] = getLuminosity(new Color(originalImg.getRGB(x,y)));
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
     * Find lines in the edges and make the drawing
     */
    public Drawing createDrawing() {
        Drawing drawing = new Drawing();
        try {
            //loop
            for(int y = 0;y<height;y++) {
                for(int x =0;x<width;x++) {
                    if(edgeValues[x][y]>THRESHOLD) {
                        edgeValues[x][y] = 0;//get rid of pixel

                        //start of line
                        List<Point> line = new ArrayList<>();
                        line.add(scale(x,y));

                        //keep adding points to line that surround until none are greater than the threshold
                        Point nearMaxEdge = getNearestEdgeMax(x,y);
                        while(edgeValues[(int)nearMaxEdge.getX()][(int)nearMaxEdge.getY()] > THRESHOLD) {
                            edgeValues[(int)nearMaxEdge.getX()][(int)nearMaxEdge.getY()] = 0;//get rid of pixel
                            line.add(scale(nearMaxEdge.getX(),nearMaxEdge.getY()));//add point to line
                            nearMaxEdge = getNearestEdgeMax((int)nearMaxEdge.getX(),(int)nearMaxEdge.getY());//find next pixel
                        }
                        //put the line into the list
                        drawing.addLine(line);
                    }
                }
            }
            //System.out.println("done! :) I love you.");
        }catch(Exception e) {
            drawing = null;
            e.printStackTrace();
        }
        return drawing;
    }

    public boolean save(String fileName) {
        if(drawing!=null) {
            drawing.saveLines(fileName);
            //drawing.saveLinesTest(fileName);
        }
        return drawing!=null;
    }

    /**
     * Scales the image correctly in the drawing space
     */
    private Point scale(double x, double y) {
        double xLeft = -0.4;
        double yTop = -1.4;
        double size = 1.8;

        double xScaled = x;
        double yScaled = y;

        if(width>height) {
            xScaled/=width;
            yScaled/=width;
        }else{
            xScaled/=height;
            yScaled/=height;
        }
        xScaled*=size;
        yScaled*=size;
        xScaled+=xLeft;
        yScaled+=yTop;
        return new Point(xScaled,yScaled);
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
