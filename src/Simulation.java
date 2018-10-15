import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulates drawing the image
 */
public class Simulation implements Runnable{
    private static final int FRAME_RATE = 60;

    private BufferedImage image;//image of the lines being drawn
    private List<double[]> lines;//lines to draw
    private int step;//current step
    private int prevStep;//previous step
    private int stepSpeed;//steps per update
    private int size;//width and height of the image to draw to
    private UI ui;//the ui to update
    /**
     * Init the simulation with a blank image
     */
    public Simulation(UI ui, int size) {
        lines = new ArrayList<>();
        step = 0;
        prevStep = 0;
        stepSpeed = 1;
        this.ui = ui;
        this.size = size;
        image = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
        clearImage();
    }

    /**
     * Set the image to a blank white square
     */
    public void clearImage(){
        for(int x = 0;x<size;x++) {
            for(int y = 0;y<size;y++) {
                image.setRGB(x,y, Color.WHITE.getRGB());//set image to blank white
            }
        }
    }


    /**
     * Converts the drawing into an array of lines for simulating
     */
    public void setDrawing(Drawing drawing) {
        lines.clear();//resets any previous drawing
        clearImage();

        if (drawing == null) return;//removing the drawing clears the simulation
        for(List<Point> line : drawing.getLines()) {//get number of lines in the image
            if(line.size()>=drawing.getMinLineLength()) {
                for(int i = 0;i<line.size()-1;i++) {
                    double[] lineDoubles = new double[4];
                    //scale points to be within the window
                    lineDoubles[0] = size * (line.get(i).getX()-ImageProcess.X_LEFT)/ImageProcess.SIZE;//x1
                    lineDoubles[1] = size * (line.get(i).getY()-ImageProcess.Y_TOP)/ImageProcess.SIZE;//y1
                    lineDoubles[2] = size * (line.get(i+1).getX()-ImageProcess.X_LEFT)/ImageProcess.SIZE;//x2
                    lineDoubles[3] = size * (line.get(i+1).getY()-ImageProcess.Y_TOP)/ImageProcess.SIZE;//y2

                    lines.add(lineDoubles);
                }
            }
        }
        prevStep = 0;
        step = lines.size();//start with the simulation complete
        stepSpeed = (int)Math.ceil(lines.size()/(FRAME_RATE * 60.0));//maximum of 60 seconds to complete the simulation
    }

    /**
     * Starts the simulation from the beginning
     */
    public void simulate(){
        clearImage();
        step = 0;
        prevStep = 0;
    }

    /**Returns the simulated image*/
    public BufferedImage getImage() {return image;}

    /**
     * Method for running the simulation
     */
    @Override
    public void run() {
        while(true) {
            try{
                //increase step counter
                step+=stepSpeed;
                if(step>lines.size()) step = lines.size();

                //prepare graphics to draw on
                Graphics2D g = image.createGraphics();
                g.setColor(Color.RED);
                for(int i = prevStep;i<step;i++) {
                    //draw line
                    if(i>=0 && i<lines.size()) {
                        double[] points = lines.get(i);
                        g.drawLine((int)points[0],(int)points[1],(int)points[2],(int)points[3]);
                    }
                }

                //update
                ui.repaint();

                //next step
                prevStep = step;
                Thread.sleep(1000/FRAME_RATE);//60fps
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
