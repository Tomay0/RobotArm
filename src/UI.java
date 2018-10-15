
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.Color;

public class UI extends JFrame implements ActionListener{
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

    //UI STUFF
    private JPanel menuPanel; //holds buttons and other stuff (tbd)
    private JPanel displayPanel; //Will display simulation and other things (tbd)
    private JTextArea textOutputArea; //Will display text output to indicate that an action has been performed
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenu customizeMenu = new JMenu("Customize");
    private JMenu simMenu = new JMenu("Simulation");
    private JMenu drawMenu = new JMenu("Draw");
    private JMenuItem openMenuItem, saveMenuItem, saveTestMenuItem, darkThemeMenuItem, lightThemeMenuItem, socialistThemeMenuItem
            ,runSimMenuItem, drawCircle, drawHorizLine, drawVertLine, drawWord;
    private JScrollPane textOutputAreaScroll;
    private Set<JPanel> panelSet = new HashSet<>();
    private JSlider thresholdSlider;

    //MAIN ROBOT ARM STUFF
    private ImageProcess currentImage;
    private Drawing drawing;
    private Simulation simulation;

    /**
     * Initialise the UI
     */
    public UI(){
        currentImage  = null;
        drawing = null;
        simulation = null;
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
        textOutputArea.setText("H E L P  M E");
        textOutputAreaScroll = new JScrollPane(textOutputArea); //Adds the panel to the scroll pane - enables scrolling of the panel
        textOutputAreaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); //Only allows scrolling when needed

        /*Adding components to container*/
        container.add(menuPanel, BorderLayout.WEST); //Menu panel to the left of window
        container.add(menuBar, BorderLayout.NORTH); //Menu bar on top
        container.add(displayPanel, BorderLayout.EAST);
        container.add(textOutputAreaScroll, BorderLayout.SOUTH);

        /*Adding panels to set*/
        panelSet.add(menuPanel);
        panelSet.add(displayPanel);

        /*Doing the stuff in the menu*/
        menuBar.add(fileMenu);
        menuBar.add(customizeMenu);
        menuBar.add(simMenu);
        menuBar.add(drawMenu);
        setupMenuBarItems();
        setVisible(true);
    }

    /**
     * Init menu
     */
    public void setupMenuBarItems(){

        /*For file menu*/
        openMenuItem = new JMenuItem("Open");
        saveMenuItem = new JMenuItem("Save");
        saveTestMenuItem = new JMenuItem("Save Test");

        /*For useless functionality*/
        darkThemeMenuItem = new JMenuItem("Dark theme :)");
        lightThemeMenuItem = new JMenuItem("Light Theme :)");
        socialistThemeMenuItem = new JMenuItem("Socialist Theme :D");

        /*For sim menu*/
        runSimMenuItem = new JMenuItem("Run Simulation");

        drawWord = new JMenuItem("Draw Word");
        drawCircle = new JMenuItem("Draw Circle");
        drawVertLine = new JMenuItem("Draw Vertical Line");
        drawHorizLine = new JMenuItem("Draw Horizontal Line");

        /*Adding action commands to file menu itmes*/
        openMenuItem.setActionCommand("Open");
        saveMenuItem.setActionCommand("Save");
        saveTestMenuItem.setActionCommand("Save Test");

        /*Adding to fileMenu*/
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveTestMenuItem);

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
        drawMenu.add(drawHorizLine);
        drawMenu.add(drawWord);
        drawMenu.add(drawVertLine);

        drawCircle.setActionCommand("Draw Circle"); //then in actionPerformed - event.getActionCommand()
        drawHorizLine.setActionCommand("Draw Horizontal Line");
        drawVertLine.setActionCommand("Draw Vertical Line");
        drawWord.setActionCommand("Draw Word");

        /*Adding action listeners to all menu items*/
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        saveTestMenuItem.addActionListener(this);
        darkThemeMenuItem.addActionListener(this);
        lightThemeMenuItem.addActionListener(this);
        socialistThemeMenuItem.addActionListener(this);
        runSimMenuItem.addActionListener(this);
        //TODO: Add action listeners to all the draw menu items
    }

    public void setupMenuPanel(){

    }

    public void setupDisplayScreen(){

    }

    public void actionPerformed(ActionEvent event){

        if(event.getActionCommand().equals("Open")){
            openFile();

        }else if(event.getActionCommand().equals("Save")){
            saveFile();

        }else if(event.getActionCommand().equals("Save Test")){
            saveTest();

        }else if(event.getActionCommand().equals("Enable Dark Theme")){
            System.out.println("Dark theme");
            enableDarkTheme();

        }else if(event.getActionCommand().equals("Enable Light Theme")){
            enableLightTheme();

        }else if(event.getActionCommand().equals("Run Sim")){
            runSim();

        }else if(event.getActionCommand().equals("Draw Circle")){


        }else if(event.getActionCommand().equals("Draw Horizontal Line")){


        }else if(event.getActionCommand().equals("Draw Vertical Line")){


        }else if(event.getActionCommand().equals("Draw Word")){


        }else if(event.getActionCommand().equals("Enable Socialist Theme")){
            enableSocialistTheme();

        }



    }

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
            if(currentImage.getDrawing()==null) {//check open of the image was ok
                currentImage = null;
                textOutputArea.append("Unable to read image data from the file you picked.\n");
            }else{
                //Load successful
                drawing = currentImage.getDrawing();
                textOutputArea.append("loaded file: " + file.getName() + "\n");

                /*Getting image to display onto menuPanel*/
                menuPanel.removeAll(); //Clears the panel of the image
                BufferedImage openedImg = currentImage.getOriginalImg();
                    /* Potential scaling stuff
                    int scaleX = MENU_PANEL_WIDTH/openedImg.getWidth();
                    int scaleY = MENU_PANEL_HEIGHT/openedImg.getHeight();
                    int newWidth = openedImg.getWidth() * scaleX;
                    System.out.println(newWidth);
                    int newHeight = openedImg.getHeight() * scaleY;
                    System.out.println(newHeight);
                    JLabel pic = new JLabel(new ImageIcon(openedImg.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST)));
                    */
                JLabel pic = new JLabel(new ImageIcon(openedImg.getScaledInstance(MENU_PANEL_WIDTH - 10, 200, Image.SCALE_FAST))); //Temporary forced scaling

                /*Setting the orientation of the label below the icon*/
                pic.setHorizontalTextPosition(JLabel.CENTER);
                pic.setVerticalTextPosition(JLabel.BOTTOM);
                pic.setText("happiness is a joke"); //Label

                menuPanel.add(pic);
                menuPanel.updateUI(); //Reshowing panel components
                revalidate();
            }
        }
    }

    /*Saving a file*/
    public void saveFile(){
        if(currentImage==null) {
            textOutputArea.append("you haven't opened an image.\n");
        }
        JFileChooser saveFileChooser = new JFileChooser();
        saveFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int status = saveFileChooser.showSaveDialog(null); //Prompting user to open a file
        if(status != saveFileChooser.APPROVE_OPTION){
            textOutputArea.append("No file selected\n");
        }else{
            //Save
            File f = saveFileChooser.getSelectedFile();
            if(!currentImage.save(f)) {
                textOutputArea.append("Could not save\n");
            }
        }
    }

    public void saveTest(){

        if(currentImage == null){
            textOutputArea.append("You have not opened an image\n");
        }
        JFileChooser saveTestFileChooser = new JFileChooser();
        saveTestFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        int status = saveTestFileChooser.showSaveDialog(null);
        if(status != saveTestFileChooser.APPROVE_OPTION){
            textOutputArea.append("Save cancelled\n");
        }else{
            //Save
            File f = saveTestFileChooser.getSelectedFile();
            if(!currentImage.saveTest(f)){
                textOutputArea.append("Could not save test file\n");
            }
        }
    }

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


    }

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

    }

    public void runSim(){


    }



    public static void main(String[] args) {
        UI ui = new UI();
    }
}
