import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

/**
 * Represents an image processed to find edges
 */
public class ImageProcess {
    private static final double X_LEFT = -0.4;//minimum x the image will be drawn from
    private static final double Y_TOP = -1.4;//minimum y the image will be drawn from
    private static final double SIZE = 1.8;//maximum size of the image (width or height)

    private static final int THRESHOLD = 100;//threshold for detecting edges

    private int width,height;

    private int[][] pixels;//black and white image
    private int[][] edgeValues;//image of edge values
    private Drawing drawing;//drawing with list of lines that can be converted to control signals

    private BufferedImage originalImg = null;//image of the original
    private BufferedImage edgeImg = null;//image of the edges

    /**
     * Process an image
     */
    public ImageProcess(File file) {
        try {
            load(file);//load image
            findEdgeValues();//find edges
            drawing = createDrawing();//find lines and create drawing
        }catch(Exception e) {
            e.printStackTrace();
            drawing = null;
            System.out.println("Could not load an image from that file.");
        }
    }

    /**
     * Loads an image from the file
     * Puts luminosity values of all the pixels into the pixels[][] array.
     */
    public void load(File file) throws IOException {
        originalImg = ImageIO.read(file);
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
     * Go through all pixels in the image and get the edge values
     * The higher the number the sharper the edge
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
     * Find lines in the image and make a drawing
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


    /**
     * Scales the image correctly in the drawing space
     */
    private Point scale(double x, double y) {
        double xScaled = x;
        double yScaled = y;

        if(width>height) {
            xScaled/=width;
            yScaled/=width;
        }else{
            xScaled/=height;
            yScaled/=height;
        }
        xScaled*=SIZE;
        yScaled*=SIZE;
        xScaled+=X_LEFT;
        yScaled+=Y_TOP;
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

    /**
     * Saves the drawing
     */
    public boolean save(File file) {
        if(drawing!=null) {
            drawing.saveLines(file);
        }
        return drawing!=null;
    }

    /*
    Testing save drawing
     */
    public boolean saveTest(File file){
        if(drawing!=null) {
            drawing.saveLinesTest(file);
        }
        return drawing!=null;

    }

    /**Get Original Image*/
    public BufferedImage getOriginalImg(){
        return originalImg;
    }
    /**Get drawing*/
    public Drawing getDrawing(){
        return drawing;
    }
}
