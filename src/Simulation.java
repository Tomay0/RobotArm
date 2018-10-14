import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Simulates drawing the
 */
public class Simulation {
    private BufferedImage simulatedImage;//image of the lines being drawn

    private Drawing drawing;//drawing to simulate
    private int steps;//number of steps in the simulation
    private int step;//current step
    private int stepSpeed;//steps per update;

    /**
     * Init simulation of a drawing
     */
    public Simulation(Drawing drawing) {
       this.drawing = drawing;
       steps = 0;
       for(List<Point> line : drawing.getLines()) {//get number of lines in the image
           if(line.size()>=drawing.getMinLineLength()) steps+=line.size()-1;
       }
       step = steps-1;//start on last frame of simulation
       simulatedImage = null;
    }

    /**
     * Updates the simulation
     */
    public void update() {
        //TODO update simulatedImage so it contains a list of lines

        step+=stepSpeed;
        if(step>=steps) step = steps-1;
    }

    /**
     * Starts the simulation
     */
    public void simulate(){
        step = 0;
    }

    /**Returns the simulated image*/
    public BufferedImage getImage() {return simulatedImage;}
}
