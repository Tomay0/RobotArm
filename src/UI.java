import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.Color;
import java.util.List;

public class UI extends JFrame implements ActionListener, ChangeListener {
    //SOME CONSTANTS
    private static final String menuBorderTitle = "Menu";
    private static final String displayBorderTitle = "Happiness is an illusion";
    private static final String textOutBorderTitle = "Text output";
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private static final int MENU_PANEL_WIDTH = FRAME_WIDTH / 5;
    private static final int MENU_PANEL_HEIGHT = FRAME_HEIGHT;
    private static final int DISPLAY_PANEL_WIDTH = FRAME_WIDTH - MENU_PANEL_WIDTH - 20;
    private static final int DISPLAY_PANEL_HEIGHT = FRAME_HEIGHT;
    private static final int TEXT_OUT_PANEL_WIDTH = DISPLAY_PANEL_WIDTH;
    private static final int TEXT_OUT_PANEL_HEIGHT = DISPLAY_PANEL_HEIGHT/5;
    private static final int SIMULATION_SIZE = 600;

    //UI STUFF
    private JPanel menuPanel; //holds buttons and other stuff (tbd)
    private JPanel displayPanel; //Will display simulation and other things (tbd)
    private JTextArea textOutputArea; //Will display text output to indicate that an action has been performed
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenu customizeMenu = new JMenu("Customize");
    private JMenu simMenu = new JMenu("Simulation");
    private JMenu drawMenu = new JMenu("Draw");
    private JMenu optionsMenu = new JMenu("Options");

    private JMenuItem openMenuItem, saveMenuItem, darkThemeMenuItem, lightThemeMenuItem, socialistThemeMenuItem
            ,runSimMenuItem, drawCircle, drawLine, drawWord, changeSimConstants, changeMotorConstants;

    /*Sliders within the change constants sub menus*/
    private JSlider thresholdSlider, minLinePointSlider, straightLineThresholdSlider, maxLineLengthSlider,
            penDownSlider, penUpSlider, left0DegSlider, right0DegSlider, leftGradientSlider, rightGradientSlider;

    private JScrollPane textOutputAreaScroll;
    private Set<JPanel> panelSet = new HashSet<>();
    private int theme = 1; //1 = Light Theme, 2 = Dark Theme, 3 = Socialist Theme (Potentially Redundant)
    /*TODO: Make sliders for the following:
    ---------- For Simulation Class --------
    * THRESHOLD
    * MINIMUM_LINE_POINTS
    * STRAIGHT_LINE_THRESHOLD
    * MAX_LINE_LENGTH
    * ---------For Drawing Class --------
    * PEN_DOWN
    * PEN_UP
    * left0Degrees
    * right0degrees
    * leftGradient
    * rightGradient*/

    //MAIN ROBOT ARM STUFF
    private ImageProcess currentImage;
    private Drawing drawing;
    private Simulation simulation;

    /**
     * Initialise the UI
     */
    public UI(){
        currentImage  = null;
        drawing = new Drawing();
        //drawing.drawRect(-0.3,-1.3,1,1,50);
        //drawing.drawCircle(-0.3,-1.3,0.5,50);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e) {
            e.printStackTrace();
        }
        Container container = getContentPane();

        /*Setup of window*/
        setLayout(new BorderLayout());
        setTitle("Image Processing");
        setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        /*menuPanel setup - Panel to the left*/
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(MENU_PANEL_WIDTH, MENU_PANEL_HEIGHT));
        menuPanel.setVisible(true);
        menuPanel.setBackground(Color.white);
        menuPanel.setBorder(BorderFactory.createTitledBorder(menuBorderTitle));

        /*Display panel setup - Big panel at the center*/
        displayPanel = new JPanel();
        displayPanel.setPreferredSize(new Dimension(DISPLAY_PANEL_WIDTH, DISPLAY_PANEL_HEIGHT));
        displayPanel.setVisible(true);
        displayPanel.setBackground(Color.white);
        displayPanel.setBorder(BorderFactory.createTitledBorder(displayBorderTitle));

        /*Text output panel setup - Panel at the bottom*/
        textOutputArea = new JTextArea();
        textOutputArea.setPreferredSize(new Dimension(TEXT_OUT_PANEL_WIDTH, TEXT_OUT_PANEL_HEIGHT));
        textOutputArea.setVisible(true);
        textOutputArea.setBackground(Color.white);
        textOutputArea.setBorder(BorderFactory.createTitledBorder(textOutBorderTitle));
        textOutputArea.setEditable(false); //Prevents user from typing into text area
        textOutputAreaScroll = new JScrollPane(textOutputArea); //Adds the panel to the scroll pane - enables scrolling of the panel
        textOutputAreaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); //Only allows scrolling when needed

        /*Adding components to container*/
        container.add(menuPanel, BorderLayout.WEST); //Menu panel to the left of window
        container.add(menuBar, BorderLayout.NORTH); //Menu bar on top
        container.add(displayPanel, BorderLayout.EAST);
        container.add(textOutputAreaScroll, BorderLayout.SOUTH);

        //Simulation
        simulation = new Simulation(this,SIMULATION_SIZE);
        JLabel simLabel = new JLabel(new ImageIcon(simulation.getImage()));
        displayPanel.add(simLabel);
        new Thread(simulation).start();
        simulation.setDrawing(drawing);

        /*Adding panels to set*/
        panelSet.add(menuPanel);
        panelSet.add(displayPanel);

        /*Doing the stuff in the menu*/
        menuBar.add(fileMenu);
        menuBar.add(customizeMenu);
        menuBar.add(simMenu);
        menuBar.add(drawMenu);
        menuBar.add(optionsMenu);
        setupMenuBarItems();
        setVisible(true);
    }

    /**
     * Init menu
     */
    public void setupMenuBarItems(){

        /*For file menu*/
        openMenuItem = new JMenuItem("Open Image");
        saveMenuItem = new JMenuItem("Save Drawing");

        /*For useless functionality*/
        darkThemeMenuItem = new JMenuItem("Dark theme :)");
        lightThemeMenuItem = new JMenuItem("Light Theme :)");
        socialistThemeMenuItem = new JMenuItem("Socialist Theme :D");

        /*For sim menu*/
        runSimMenuItem = new JMenuItem("Run Simulation");

        drawWord = new JMenuItem("Draw Word");
        drawCircle = new JMenuItem("Draw Circle");
        drawLine = new JMenuItem("Draw Line");

        /*Adding action commands to file menu itmes*/
        openMenuItem.setActionCommand("Open");
        saveMenuItem.setActionCommand("Save");

        /*Adding to fileMenu*/
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);

        /*Adding the useless things into the thing*/
        customizeMenu.add(darkThemeMenuItem);
        customizeMenu.add(lightThemeMenuItem);
        customizeMenu.add(socialistThemeMenuItem);

        darkThemeMenuItem.setActionCommand("Enable Dark Theme");
        lightThemeMenuItem.setActionCommand("Enable Light Theme");
        socialistThemeMenuItem.setActionCommand("Enable Socialist Theme");

        /*Adding to sim menu*/
        simMenu.add(runSimMenuItem);

        /*Adding listeners to the sim menu items*/
        runSimMenuItem.setActionCommand("Run Sim");

        /*Adding menu items to drawMenu*/
        drawMenu.add(drawCircle);
        drawMenu.add(drawLine);
        drawMenu.add(drawWord);

        drawCircle.setActionCommand("Draw Circle"); //then in actionPerformed - event.getActionCommand()
        drawLine.setActionCommand("Draw Line");
        drawWord.setActionCommand("Draw Word");

        changeSimConstants = new JMenuItem("Simulation Constants");
        changeMotorConstants = new JMenuItem("Motor Constants");
        changeSimConstants.setActionCommand("Set Simulation Constants");
        changeMotorConstants.setActionCommand("Set Motor Constants");
        optionsMenu.add(changeSimConstants);
        optionsMenu.add(changeMotorConstants);

        /*Adding action listeners to all menu items*/
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        darkThemeMenuItem.addActionListener(this);
        lightThemeMenuItem.addActionListener(this);
        socialistThemeMenuItem.addActionListener(this);
        runSimMenuItem.addActionListener(this);
        drawLine.addActionListener(this);
        drawCircle.addActionListener(this);
        drawWord.addActionListener(this);
        changeSimConstants.addActionListener(this);
        changeMotorConstants.addActionListener(this);
    }

    public void setupMenuPanel(){

    }

    public void setupDisplayScreen(){

    }

    /**
     * Click event
     */
    public void actionPerformed(ActionEvent event){
        String command = event.getActionCommand();
        if(command.equals("Open"))openFile();
        else if(command.equals("Save"))saveFile();
        else if(command.equals("Enable Dark Theme"))enableDarkTheme();
        else if(command.equals("Enable Light Theme")) enableLightTheme();
        else if(command.equals("Enable Socialist Theme"))enableSocialistTheme();
        else if(command.equals("Run Sim"))runSim();
        else if(command.equals("Draw Circle")){
            drawing = new Drawing();
            drawing.drawCircle(-0.3,-1.3,0.5,50);
            updateSim();
        }
        else if(command.equals("Draw Line")){
            drawing = new Drawing();
            drawing.drawLine(0.3, -1.3, 0.3, 0.3, 50);
            updateSim();
        }
        else if(command.equals("Draw Word")){/*drawing.drawWord()*/}
        else if(command.equals("Set Simulation Constants"))new OptionsWindow("Simulation Constants");
        else if(command.equals("Set Motor Constants"))new OptionsWindow("Motor Constants");
    }

    public void stateChanged(ChangeEvent event){
        //TODO: Event handling for sliders
    }

    /**
     * Opens an image
     */
    public void openFile(){
        JFileChooser openFileChooser = new JFileChooser();
        openFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int status = openFileChooser.showOpenDialog(null); //Prompting user to open a file
        /*Checks if a file was selected*/
        if(status != openFileChooser.APPROVE_OPTION){
            textOutputArea.append("No file selected\n");

        }else{
            //Open
            File file = openFileChooser.getSelectedFile();
            currentImage = new ImageProcess(file); //Processing the opened image
            if(!currentImage.isLoaded()) {//check open of the image was ok
                currentImage = null;
                textOutputArea.append("Unable to read image data from the file you picked.\n");
            }else{
                //Load successful
                drawing = currentImage.createDrawing();//turn into drawing
                simulation.setDrawing(drawing);
                textOutputArea.append("loaded file: " + file.getName() + "\n");
                int numOfLines = simulation.getNumberOfLines();
                int nunberOfSeconds = (int)((double)numOfLines * 0.257);
                int numberOfMins = nunberOfSeconds / 60;
                nunberOfSeconds %= 60;
                textOutputArea.append("Number of lines: " + numOfLines + ". this will take: " + numberOfMins + "mins and " + nunberOfSeconds + " seconds\n");

                /*Getting image to display onto menuPanel*/
                menuPanel.removeAll(); //Clears the panel of the image
                BufferedImage openedImg = currentImage.getOriginalImg();
                BufferedImage edgeImg = currentImage.getEdgeImg();

                double width = openedImg.getWidth();
                double height = openedImg.getHeight();
                double scale;
                double maxSize = MENU_PANEL_WIDTH-10;//Maximum width/height

                //Calculate scale so that the longest side is equal to the maximum size
                if(width>height) scale = maxSize/width;
                else scale = maxSize/height;

                //scaled picture
                JLabel pic = new JLabel(new ImageIcon(openedImg.getScaledInstance((int)(width*scale), (int)(height*scale), Image.SCALE_FAST)));
                JLabel edgePic = new JLabel(new ImageIcon(edgeImg.getScaledInstance((int)(width*scale), (int)(height*scale), Image.SCALE_FAST)));

                /*Setting the orientation of the label below the icon*/
                pic.setHorizontalTextPosition(JLabel.CENTER);
                pic.setVerticalTextPosition(JLabel.BOTTOM);
                pic.setText("happiness is a joke"); //Label

                menuPanel.add(pic);
                menuPanel.add(edgePic);
                menuPanel.updateUI(); //Reshowing panel components
                revalidate();
            }
        }
    }

    /**
     * Saves a drawing
     * */
    public void saveFile(){
        if(drawing==null) {
            textOutputArea.append("You haven't created a drawing.\n");
            return;
        }
        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int status = saveFileChooser.showSaveDialog(null); //Prompting user to open a file
        if(status != saveFileChooser.APPROVE_OPTION){
            textOutputArea.append("No file selected\n");
        }else{
            //Save
            File file = saveFileChooser.getSelectedFile();
            textOutputArea.append("No drawing created\n");
        }
    }

    /**Starts simulation*/
    public void runSim(){
        simulation.simulate();
    }

    /**
     * Changes theme to dark theme
     */
    public void enableDarkTheme(){

        for(JPanel panel : panelSet) {
            panel.setBackground(Color.black);
            TitledBorder newBorder = (TitledBorder) panel.getBorder();//panel.getBorder() returns Border type - need to cast to TitledBorder type
            newBorder.setTitleColor(Color.white);
            panel.setBorder(newBorder);
        }

        /*Changing the color of the text area*/
        textOutputArea.setBackground(Color.black);
        TitledBorder newTextAreaBorder = (TitledBorder) textOutputArea.getBorder();
        newTextAreaBorder.setTitleColor(Color.white);
        textOutputArea.setBorder(newTextAreaBorder);
        textOutputArea.setForeground(Color.white); //Changes text color

        theme = 2;
        simulation.redraw();
    }

    /**
     * Changes theme to light theme
     */
    public void enableLightTheme(){

        for(JPanel panel : panelSet){
            panel.setBackground(Color.white);

            /*Turning border title color white*/
            TitledBorder newBorder = (TitledBorder) panel.getBorder(); //panel.getBorder() returns Border type - need to cast to TitledBorder type
            newBorder.setTitleColor(Color.black);
            panel.setBorder(newBorder);

        }

        /*Changing the color of the text area*/
        textOutputArea.setBackground(Color.white);
        TitledBorder newTextAreaBorder = (TitledBorder) textOutputArea.getBorder();
        newTextAreaBorder.setTitleColor(Color.black);
        textOutputArea.setBorder(newTextAreaBorder);
        textOutputArea.setForeground(Color.black); //Changes text color

        theme = 1;
        simulation.redraw();

    }

    /*Potentially redundant*/
    public void enableSocialistTheme(){

        for(JPanel panel : panelSet){
            panel.setBackground(Color.red);

            /*Turning border title color white*/
            TitledBorder newBorder = (TitledBorder) panel.getBorder(); //panel.getBorder() returns Border type - need to cast to TitledBorder type
            newBorder.setTitleColor(Color.yellow);
            panel.setBorder(newBorder);
            textOutputArea.setText("OUR UI");
        }

        /*Changing the color of the text area*/
        textOutputArea.setBackground(Color.red);
        TitledBorder newTextAreaBorder = (TitledBorder) textOutputArea.getBorder();
        newTextAreaBorder.setTitleColor(Color.yellow);
        textOutputArea.setBorder(newTextAreaBorder);
        textOutputArea.setForeground(Color.yellow); //Changes text color

        theme = 3;
        simulation.redraw();

    }

    public void updateSim(){
        simulation.setDrawing(drawing);
    }

    /*Getters*/
    public int getTheme(){
        return theme;

    }

    public static void main(String[] args) {
        UI ui = new UI();
    }
}


