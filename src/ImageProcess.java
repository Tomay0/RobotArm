import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Represents an image processed to find edges
 */
public class ImageProcess {
    public static final double X_LEFT = -0.4;//minimum x the image will be drawn from
    public static final double Y_TOP = -1.4;//minimum y the image will be drawn from
    public static final double SIZE = 1.8;//maximum size of the image (width or height)
    public static final double MAX_LINE_LENGTH = 0.1;//split lines in half if above this length for lines
    public static final double STRAIGHT_LINE_THRESHOLD = 0.01;//points on a line must be less than many pixels away to be considered straight

    private static final int THRESHOLD = 100;//threshold for detecting edges

    private int width,height;

    private int[][] pixels;//black and white image
    private int[][] edgeValues;//image of edge values

    private BufferedImage originalImg = null;//image of the original
    private BufferedImage edgeImg = null;//image of the edges

    /**
     * Process an image
     */
    public ImageProcess(File file) {
        try {
            load(file);//load image
            findEdgeValues();//find edges
        }catch(Exception e) {
            e.printStackTrace();
            originalImg = null;
            edgeImg = null;
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
        edgeImg = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

        for(int y = 0;y<height;y++) {
            for(int x =0;x<width;x++) {
                //indices of left/right bottom/top. This is to avoid checking pixels outside edges of the screen
                /*int top = y==0 ? 0 : y-1;
                int bottom = y==height-1 ? height-1 : y+1;
                int left = x==0 ? 0 : x-1;
                int right = x==width-1 ? width-1 : x+1;
                */
                int top = y==0 ? 0 : y-1;
                int bottom = y;
                int left = x==0 ? 0 : x-1;
                int right = x;


                //vertical edges
                int vertical = -1 * pixels[left][top] + 1 * pixels[right][top] +
                        -2 * pixels[left][y] + 2 * pixels[right][y] +
                        -1 * pixels[left][bottom] + 1 * pixels[right][bottom];


                //horizontal edges
                int horizontal = 1 * pixels[left][top] + 2 * pixels[x][top] + 1 * pixels[right][top] +
                        -1 * pixels[left][bottom] + -2 * pixels[x][bottom] + -1 * pixels[right][bottom];

                //combine
                edgeValues[x][y] = (int)Math.sqrt(vertical * vertical + horizontal * horizontal);

                int edgeGrayValue = (int)Math.min(255,edgeValues[x][y]);
                edgeImg.setRGB(x,y,new Color(edgeGrayValue,edgeGrayValue,edgeGrayValue).getRGB());

                /*int edgeValue = 0;
                if(x>0) {
                    edgeValue+=Math.abs(pixels[x-1][y]-pixels[x][y])/2;

                }
                if(y>0) {
                    edgeValue+=Math.abs(pixels[x][y-1]-pixels[x][y])/2;
                }
                edgeValues[x][y] = edgeValue;
                edgeImg.setRGB(x,y,new Color(edgeValue,edgeValue,edgeValue).getRGB());*/
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
                        /*Point nearEdge = getNearEdge(x,y);
                        while(nearEdge!=null) {
                            edgeValues[(int)nearEdge.getX()][(int)nearEdge.getY()] = 0;//get rid of pixel
                            line.add(scale(nearEdge.getX(),nearEdge.getY()));//add point to line
                            nearEdge = getNearEdge((int)nearEdge.getX(),(int)nearEdge.getY());
                        }*/
                        line = optimizeLine(line);
                        //put the line into the list
                        drawing.addLine(line);
                    }
                }
            }
        }catch(Exception e) {
            drawing = null;
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
     * Get a nearby edge greater than the threshold
    *//*
    private Point getNearEdge(int x,int y) {
        for(int xoff = -1; xoff<=1;xoff++) {//loop through offsets
            for (int yoff = -1; yoff <= 1; yoff++) {
                if (x + xoff >= 0 && x + xoff < width && y + yoff >= 0 && y + yoff < height) {//check within bounds
                    int edgeValue = edgeValues[x+xoff][y+yoff];
                    if(edgeValue>THRESHOLD) {
                        return new Point(x+xoff,y+yoff);
                    }
                }
            }
        }
        return null;
    }*/

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
     * Reduce number of points that make a line
     */
    public static List<Point> optimizeLine(List<Point> line) {
        //if the line is 1 or 2 points, this is the most simple it can be
        if(line.size()<3) return line;

        //check if the line represented by the points given can be made by a straight line
        Point p1 = line.get(0);
        Point p2 = line.get(line.size()-1);
        double lineLength = p1.dist(p2);//distance from p1 to p2

        int splitIndex = -1;//where to cut the line segment in half
        if(lineLength<MAX_LINE_LENGTH) {//if the line is longer than the maximum line length, always split in half
            Point lineVec = new Point(p2.getX()-p1.getX(),p2.getY()-p1.getY());//vector from p1 to p2

            double maxDistance = 0;//calculated as the maximum

            //for all points between p1 and p2 once one that exceeds a
            for(int i = 1;i<line.size()-1;i++) {
                Point p = line.get(i);
                Point pVec = new Point(p.getX()-p1.getX(),p.getY()-p1.getY());//vector from p1 to p
                double pVecLength = p1.dist(p);//distance from p1 to p

                double dot = lineVec.getX() * pVec.getX() + lineVec.getY() * pVec.getY();//dot product
                double projectDistance = dot/lineLength;//distance from p1 to closest point on line to p

                double distanceFromLine = Math.sqrt(pVecLength * pVecLength - projectDistance * projectDistance);
                if(distanceFromLine>STRAIGHT_LINE_THRESHOLD && distanceFromLine>maxDistance) {//check that the point is less than the threshold, otherwise take note of the position and find the point with the maximum distance away from the line
                    splitIndex = i;
                    maxDistance = distanceFromLine;
                }
            }

            //If all lines are less than the threshold, return the line as a single segment
            if(splitIndex==-1) {
                List<Point> optimizedLine = new ArrayList<>();
                optimizedLine.add(p1);
                optimizedLine.add(p2);
                return optimizedLine;
            }
        }else splitIndex = line.size()/2;//set the split index as halfway through

        //Split the line segments in half at "splitIndex"
        List<Point> optimizedLine = new ArrayList<>();
        List<Point> half1 = new ArrayList<>();
        List<Point> half2 = new ArrayList<>();
        for(int i = 0;i<=splitIndex;i++) {
            half1.add(line.get(i));
        }
        for(int i = splitIndex;i<line.size();i++) {
            half2.add(line.get(i));
        }

        optimizedLine.addAll(optimizeLine(half1));
        optimizedLine.remove(optimizedLine.size()-1);
        optimizedLine.addAll(optimizeLine(half2));
        return optimizedLine;
    }

    /**
     * Returns the luminosity for a given colour
     */
    private int getLuminosity(Color color) {
        return (int)(0.2126*color.getRed() + 0.7152*color.getGreen() + 0.0722*color.getBlue());
    }

    /**Get Original Image*/
    public BufferedImage getOriginalImg(){
        return originalImg;
    }
    /**Get Edge Image*/
    public BufferedImage getEdgeImg(){
        return edgeImg;
    }

    /**Tells you if the image loaded correctly*/
    public boolean isLoaded() {return originalImg!=null;}
}
