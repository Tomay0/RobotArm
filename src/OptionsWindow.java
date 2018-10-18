import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

public class OptionsWindow extends JFrame implements ActionListener {
    private boolean imageProcess;//what type of constants to modify. True for imageProcess, false for motor constants

    private UI ui;
    private JButton okButton,closeButton;
    private JPanel mainPanel;
    private static final int OPTIONS_WINDOW_WIDTH = 600;
    private static final int OPTIONS_WINDOW_HEIGHT = 400;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 30;

    /*Sliders within the change constants sub menus*/
    private JFormattedTextField thresholdInput, xLeftInput, yTopInput, sizeInput, maxLineLengthInput, straightLineThresInput, minLinePointsInput,
            penDownInput, penUpInput, left0DegInput, right0DegInput, leftGradientInput, rightGradientInput;

    /**
     * Initialize image process window
     */
    public OptionsWindow(UI ui, String title){
        mainPanel  = new JPanel();
        this.ui = ui;
        /*Setting up the frame*/
        setTitle(title);
        setLayout(new BorderLayout());
        setSize(new Dimension(OPTIONS_WINDOW_WIDTH, OPTIONS_WINDOW_HEIGHT));
        setResizable(false);

        /*Setting up the mainPanel*/
        mainPanel.setPreferredSize(new Dimension(OPTIONS_WINDOW_WIDTH, OPTIONS_WINDOW_HEIGHT));
        mainPanel.setLayout(new FlowLayout());

        //either add image processing constants or arm motor constants
        imageProcess = title.equals("Image Processing Constants");
        if(imageProcess) imageProcessingFields();
        else armMotorFields();

        //buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(OPTIONS_WINDOW_WIDTH,BUTTON_HEIGHT+20));
        okButton = new JButton("Apply");
        okButton.setActionCommand("Apply");
        okButton.addActionListener(this);
        okButton.setPreferredSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
        closeButton = new JButton("Close");
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(this);
        closeButton.setPreferredSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));

        buttonPanel.add(okButton);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel);
        add(mainPanel);
        setVisible(true);
    }

    /**
     * Button click event
     */
    public void actionPerformed(ActionEvent event){
        if(event.getActionCommand().equals("Close")) {
            dispose();//exit
        }else if(event.getActionCommand().equals("Apply")) {
            try {
                if(imageProcess) {
                    //change image process constants
                    Constants.THRESHOLD = ((Number)thresholdInput.getValue()).intValue();
                    Constants.MAX_LINE_LENGTH = ((Number)maxLineLengthInput.getValue()).doubleValue();
                    Constants.STRAIGHT_LINE_THRESHOLD = ((Number)straightLineThresInput.getValue()).doubleValue();
                    Constants.MINIMUM_LINE_POINTS = ((Number)minLinePointsInput.getValue()).intValue();
                }else{
                    //change motor constants
                    Constants.X_LEFT = ((Number)xLeftInput.getValue()).doubleValue();
                    Constants.Y_TOP = ((Number)yTopInput.getValue()).doubleValue();
                    Constants.SIZE = ((Number)sizeInput.getValue()).doubleValue();
                    Constants.PEN_UP = ((Number)penUpInput.getValue()).intValue();
                    Constants.PEN_DOWN = ((Number)penDownInput.getValue()).intValue();
                    Constants.left0degrees = ((Number)left0DegInput.getValue()).intValue();
                    Constants.right0degrees = ((Number)right0DegInput.getValue()).intValue();
                    Constants.leftGradient = ((Number)leftGradientInput.getValue()).doubleValue();
                    Constants.rightGradient = ((Number)rightGradientInput.getValue()).doubleValue();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
            ui.updateImage();
            Constants.saveConstants();
            //dispose();
        }
    }

    /**
     * Fields for Image Processing constants
     * */
    public void imageProcessingFields() {
        thresholdInput = createFormattedField("Edge detection threshold:", true,Constants.THRESHOLD);
        maxLineLengthInput = createFormattedField("Maximum line length:", false,Constants.MAX_LINE_LENGTH);
        straightLineThresInput = createFormattedField("Straight line detection threshold:", false,Constants.STRAIGHT_LINE_THRESHOLD);
        minLinePointsInput = createFormattedField("Minimum line points:", false,Constants.MINIMUM_LINE_POINTS);
    }
    /**
     * Fields for Motor constants
     * */
    public void armMotorFields() {
        xLeftInput = createFormattedField("X Left", false,Constants.X_LEFT);
        yTopInput = createFormattedField("Y Top", false,Constants.Y_TOP);
        sizeInput = createFormattedField("Image Size", false,Constants.SIZE);
        penUpInput = createFormattedField("Pen Up", false,Constants.PEN_UP);
        penDownInput = createFormattedField("Pen Down", false,Constants.PEN_DOWN);
        left0DegInput = createFormattedField("Left 90 degrees", false,Constants.left0degrees);
        right0DegInput = createFormattedField("Right 90 degrees", false,Constants.right0degrees);
        leftGradientInput = createFormattedField("Left gradient", false,Constants.leftGradient);
        rightGradientInput = createFormattedField("Right gradient", false,Constants.rightGradient);
    }

    /**
     * Creates a formatted integer/double field
     * Also creates a label and places in a panel that stretches across the length of the window
     */
    public JFormattedTextField createFormattedField(String label, boolean isInteger,Object value){
        //main panel
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(OPTIONS_WINDOW_WIDTH,30));
        //label
        JLabel newLabel = new JLabel(label);

        //number formatting
        NumberFormat format = DecimalFormat.getInstance();
        if(isInteger) {
            format.setMinimumFractionDigits(0);
            format.setMaximumFractionDigits(0);
        }else{
            format.setMinimumFractionDigits(0);
            format.setMaximumFractionDigits(5);
        }
        format.setRoundingMode(RoundingMode.HALF_UP);
        JFormattedTextField field = new JFormattedTextField(format);//create field
        field.setPreferredSize(new Dimension(120,20));
        field.setValue(value);

        //add everything
        panel.add(newLabel);
        panel.add(field);
        mainPanel.add(panel);
        return field;

    }
}