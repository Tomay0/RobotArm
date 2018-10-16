import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

public class OptionsWindow extends JFrame implements ChangeListener, ActionListener {
    private JButton okButton = new JButton("Confirm");
    private JPanel mainPanel = new JPanel();
    private static final int OPTIONS_WINDOW_WIDTH = 600;
    private static final int OPTIONS_WINDOW_HEIGHT = 800;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 50;

    /*Sliders within the change constants sub menus*/
    private JTextField thresholdInput, xLeftInput, yTopInput, sizeInput, maxLineLengthInput, straightLineThresInput, minLinePointsInput,
            penDownInput, penUpInput, left0DegInput, right0DegInput, leftGradientInput, rightGradientInput;

    private Container container;

    public OptionsWindow(String title){
        container = getContentPane();

        /*Setting up the frame*/
        setTitle("Change Constants");
        setLayout(new BorderLayout());
        setSize(new Dimension(OPTIONS_WINDOW_WIDTH, OPTIONS_WINDOW_HEIGHT));
        setResizable(false);

        /*Setting up the mainPanel*/
        mainPanel.setBackground(Color.white);
        mainPanel.setPreferredSize(new Dimension(OPTIONS_WINDOW_WIDTH, OPTIONS_WINDOW_HEIGHT));
        mainPanel.setBorder(BorderFactory.createTitledBorder("THis is a visual representation of happiness"));
        mainPanel.setVisible(true);

        container.add(mainPanel);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        setupSliders();

        setVisible(true);

    }

    public void stateChanged(ChangeEvent event){
        //TODO: Slider changing values occurs in here
    }

    public void actionPerformed(ActionEvent event){
        //TODO: OK button goes in here

    }

    public void setupSliders() {
        /*
        JPanel thresholdPanel = new JPanel();
        thresholdSlider = new JSlider(10, 200, 10);
        JTextField thresholdEntry = new JTextField(10);
        thresholdPanel.add(thresholdSlider);
        thresholdPanel.add(thresholdEntry);
        container.add(thresholdPanel);
        */

        /*For Image Processing*/
        thresholdInput = createFormattedField("Threshold", true);
        maxLineLengthInput = createFormattedField("Max Line Length", false);
        straightLineThresInput = createFormattedField("Straight Line Threshold", false);
        minLinePointsInput = createFormattedField("Minimum Line Points", true);

        /*Motor controls*/
        xLeftInput = createFormattedField("X Left", false);
        yTopInput = createFormattedField("Y Top", false);
        sizeInput = createFormattedField("Image Size", false);

        /*For Drawing*/

    }


    public JTextField createFormattedField(String label, boolean isInteger){

        NumberFormat format = DecimalFormat.getInstance();
        if(isInteger) {
            format.setMinimumFractionDigits(0);
            format.setMaximumFractionDigits(0);
        }else{
            format.setMinimumFractionDigits(0);
            format.setMaximumFractionDigits(5);
            format.setRoundingMode(RoundingMode.HALF_UP);
        }
        JFormattedTextField newFormattedField = new JFormattedTextField(format);
        JPanel newPanel = new JPanel();
        JLabel newLabel = new JLabel(label);

        newPanel.add(newLabel);
        newPanel.add(newFormattedField);

        container.add(newPanel);

        return newFormattedField;

    }

}