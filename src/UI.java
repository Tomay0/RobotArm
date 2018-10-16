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
    private static final String menuBorderTitle = "Image";
    private static final String displayBorderTitle = "Drawing";
    private static final String textOutBorderTitle = "Text output";
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private static final int MENU_PANEL_WIDTH = FRAME_WIDTH / 5;
    private static final int MENU_PANEL_HEIGHT = FRAME_HEIGHT;
    private static final int DISPLAY_PANEL_WIDTH = FRAME_WIDTH - MENU_PANEL_WIDTH - 20;
    private static final int DISPLAY_PANEL_HEIGHT = FRAME_HEIGHT;
    private static final int TEXT_OUT_PANEL_WIDTH = DISPLAY_PANEL_WIDTH;
    private static final int TEXT_OUT_PANEL_HEIGHT = DISPLAY_PANEL_HEIGHT/5;
    private static final int SIMULATION_SIZE = 500;

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
        Constants.loadConstantsFromFile();
        currentImage  = null;
        drawing = new Drawing();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e) {
            e.printStackTrace();
        }
        Container container = getContentPane();

        /*Setup of window*/
        setLayout(new BorderLayout());
        setTitle("Robot Arm Image Processor");
        setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        /*menuPanel setup - Panel to the left*/
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(MENU_PANEL_WIDTH, MENU_PANEL_HEIGHT));
        menuPanel.setBackground(Color.white);
        menuPanel.setBorder(BorderFactory.createTitledBorder(menuBorderTitle));

        /*Display panel setup - Big panel at the center*/
        displayPanel = new JPanel();
        displayPanel.setPreferredSize(new Dimension(DISPLAY_PANEL_WIDTH, DISPLAY_PANEL_HEIGHT));
        displayPanel.setBackground(Color.white);
        displayPanel.setBorder(BorderFactory.createTitledBorder(displayBorderTitle));

        /*Text output panel setup - Panel at the bottom*/
        textOutputArea = new JTextArea();
        textOutputArea.setEditable(false); //Prevents user from typing into text area
        textOutputAreaScroll = new JScrollPane(textOutputArea); //Adds the panel to the scroll pane - enables scrolling of the panel
        textOutputAreaScroll.setBackground(Color.white);
        textOutputAreaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); //Only allows scrolling when needed
        textOutputAreaScroll.setPreferredSize(new Dimension(TEXT_OUT_PANEL_WIDTH, TEXT_OUT_PANEL_HEIGHT));
        textOutputAreaScroll.setBorder(BorderFactory.createTitledBorder(textOutBorderTitle));

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
        menuBar.add(drawMenu);
        menuBar.add(simMenu);
        menuBar.add(optionsMenu);
        menuBar.add(customizeMenu);
        setupMenuBarItems();
        setVisible(true);
    }

    /**
     * Init menu
     */
    public void setupMenuBarItems(){

        /*For file menu*/
        /*JMenuItem openMenuItem = new JMenuItem("Open Image");
        JMenuItem saveMenuItem = new JMenuItem("Save Drawing");

        openMenuItem.setActionCommand("Open");
        saveMenuItem.setActionCommand("Save");
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);

        /*For useless functionality
        JMenuItem darkThemeMenuItem = new JMenuItem("Dark theme :)");
        JMenuItem lightThemeMenuItem = new JMenuItem("Light Theme :)");
        JMenuItem socialistThemeMenuItem = new JMenuItem("Socialist Theme :D");

        customizeMenu.add(darkThemeMenuItem);
        customizeMenu.add(lightThemeMenuItem);
        customizeMenu.add(socialistThemeMenuItem);
        darkThemeMenuItem.setActionCommand("Enable Dark Theme");
        lightThemeMenuItem.setActionCommand("Enable Light Theme");
        socialistThemeMenuItem.setActionCommand("Enable Socialist Theme");
        darkThemeMenuItem.addActionListener(this);
        lightThemeMenuItem.addActionListener(this);
        socialistThemeMenuItem.addActionListener(this);

        /*For sim menu
        JMenuItem runSimMenuItem = new JMenuItem("Run Simulation");
        simMenu.add(runSimMenuItem);
        runSimMenuItem.setActionCommand("Run Sim");
        runSimMenuItem.addActionListener(this);

        //Draw menu
        JMenuItem drawWord = new JMenuItem("Draw Word");
        JMenuItem drawCircle = new JMenuItem("Draw Circle");
        JMenuItem drawVertLine = new JMenuItem("Draw Vertical Line");
        JMenuItem drawHorLine = new JMenuItem("Draw Horizontal Line");
        JMenuItem drawRect = new JMenuItem("Draw Rectangle");

        drawMenu.add(drawCircle);
        drawMenu.add(drawLine);
        drawMenu.add(drawWord);

        drawCircle.setActionCommand("Draw Circle"); //then in actionPerformed - event.getActionCommand()
        drawVertLine.setActionCommand("Draw Vertical Line");
        drawWord.setActionCommand("Draw Word");
        drawHorLine.setActionCommand("Draw Horizontal Line");
        drawRect.setActionCommand("Draw Rectangle");

        drawLine.addActionListener(this);
        drawCircle.addActionListener(this);
        drawWord.addActionListener(this);

        //Options menu
        JMenuItem changeSimConstants = new JMenuItem("Image Processing Constants");
        JMenuItem changeMotorConstants = new JMenuItem("Motor Constants");
        changeSimConstants.setActionCommand("Set Image Processing Constants");
        changeMotorConstants.setActionCommand("Set Motor Constants");
        changeSimConstants.addActionListener(this);
        changeMotorConstants.addActionListener(this);
        optionsMenu.add(changeSimConstants);
        optionsMenu.add(changeMotorConstants);*/
        addMenuItem(fileMenu,"Open Image","Open");
        addMenuItem(fileMenu,"Save Image","Save");

        addMenuItem(drawMenu,"Draw Vertical Line","Draw Vertical Line");
        addMenuItem(drawMenu,"Draw Horizontal Line","Draw Horizontal Line");
        addMenuItem(drawMenu,"Draw Rectangle","Draw Rectangle");
        addMenuItem(drawMenu,"Draw Circle","Draw Circle");
        addMenuItem(drawMenu,"Draw Word","Draw Word");

        addMenuItem(simMenu,"Start Simulation","Run Sim");

        addMenuItem(optionsMenu,"Image Processing Constants","Set Image Processing Constants");
        addMenuItem(optionsMenu,"Motor Constants","Set Motor Constants");

        addMenuItem(customizeMenu,"Dark theme :)","Enable Dark Theme");
        addMenuItem(customizeMenu,"Light Theme :)","Enable Light Theme");
        addMenuItem(customizeMenu,"Socialist Theme :D","Enable Socialist Theme");
    }

    /**
     * Adds a menu option
     */
    private void addMenuItem(JMenu menu, String text, String action) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setActionCommand(action);
        menuItem.addActionListener(this);
        menu.add(menuItem);
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
            updateDrawing();
        }
        else if(command.equals("Draw Vertical Line")){
            drawing = new Drawing();
            drawing.drawLine(0.3, -1.3, 0.3, 0.3, 50);
            updateDrawing();
        }
        else if(command.equals("Draw Horizontal Line")){
            drawing = new Drawing();
            drawing.drawLine(-0.3, -1, 1.3, -1, 50);
            updateDrawing();
        }
        else if(command.equals("Draw Rectangle")){
            drawing = new Drawing();
            drawing.drawRect(-0.3, -1.3, 1.7, 1.7, 50);
            updateDrawing();
        }
        else if(command.equals("Draw Word")){
            drawing = new Drawing();
            drawing.drawSkynet(-0.3,-1.3,0.25);
            updateDrawing();
        }
        else if(command.equals("Set Image Processing Constants"))new OptionsWindow(this,"Image Processing Constants");
        else if(command.equals("Set Motor Constants"))new OptionsWindow(this,"Motor Constants");
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
                textOutputArea.append("loaded file: " + file.getName() + "\n");
                //Load successful
                updateImage();

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
                pic.setText("Original image"); //Label

                edgePic.setHorizontalTextPosition(JLabel.CENTER);
                edgePic.setVerticalTextPosition(JLabel.BOTTOM);
                edgePic.setText("Edges"); //Label

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
        textOutputAreaScroll.setBackground(Color.black);
        TitledBorder newTextAreaBorder = (TitledBorder) textOutputAreaScroll.getBorder();
        newTextAreaBorder.setTitleColor(Color.white);
        textOutputAreaScroll.setBorder(newTextAreaBorder);
        textOutputAreaScroll.setForeground(Color.white); //Changes text color

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
        textOutputAreaScroll.setBackground(Color.white);
        TitledBorder newTextAreaBorder = (TitledBorder) textOutputAreaScroll.getBorder();
        newTextAreaBorder.setTitleColor(Color.black);
        textOutputAreaScroll.setBorder(newTextAreaBorder);
        textOutputAreaScroll.setForeground(Color.black); //Changes text color

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
        textOutputAreaScroll.setBackground(Color.red);
        TitledBorder newTextAreaBorder = (TitledBorder) textOutputAreaScroll.getBorder();
        newTextAreaBorder.setTitleColor(Color.yellow);
        textOutputAreaScroll.setBorder(newTextAreaBorder);
        textOutputAreaScroll.setForeground(Color.yellow); //Changes text color

        theme = 3;
        simulation.redraw();

    }

    /**
     * Create a drawing from the loaded image and update the simulation
     */
    public void updateImage() {
        if(currentImage!=null) {
            drawing = currentImage.createDrawing();
            updateDrawing();
        }
    }

    /**
     * Updates drawing in the simulation and tells you number of lines, number of control signals and estimated drawing time
     */
    public void updateDrawing() {
        int controlCount = drawing.getMotorSignalCount();
        int s = (int)Math.round(controlCount/5.0);
        int m = (int)(s/60);
        s = s%60;
        simulation.setDrawing(drawing);
        textOutputArea.append("Created drawing from image with " + simulation.getNumberOfLines() + " lines and " + controlCount + " control signals.\nEstimated drawing time of at least least " + m + ":" + s + "\n");
    }

    /*Getters*/
    public int getTheme(){
        return theme;

    }

    public static void main(String[] args) {
        UI ui = new UI();
    }
}


