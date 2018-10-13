
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

public class UI extends JFrame{
    JFrame mainFrame = new JFrame();
    private JPanel menuPanel; //holds buttons and other stuff (tbd)
    private JPanel displayPanel; //Will display simulation and other things (tbd)
    private JTextArea textOutputArea; //Will display text output to indicate that an action has been performed
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenu customizeMenu = new JMenu("Customize");
    private JMenu simMenu = new JMenu("Simulation");
    private JMenuItem openMenuItem, saveMenuItem, saveTestMenuItem, darkThemeMenuItem, lightThemeMenuItem, runSimMenuItem = new JMenuItem();
    private JScrollPane textOutputAreaScroll;
    private Simulation sim;

    /*Border titles*/
    String menuBorderTitle = "Menu";
    String displayBorderTitle = "Happiness is an illusion";
    String textOutBorderTitle = "Text output";

    String openedImgFileName = "";
    private Set<JPanel> panelSet = new HashSet<>();

    JSlider thresholdSlider;

    /*Sizes of the components - All are related back to FRAME_WIDTH and FRAME_HEIGHT in some way*/
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private static final int MENU_PANEL_WIDTH = FRAME_WIDTH / 5;
    private static final int MENU_PANEL_HEIGHT = FRAME_HEIGHT;
    private static final int DISPLAY_PANEL_WIDTH = FRAME_WIDTH - MENU_PANEL_WIDTH - 20;
    private static final int DISPLAY_PANEL_HEIGHT = FRAME_HEIGHT;
    private static final int TEXT_OUT_PANEL_WIDTH = DISPLAY_PANEL_WIDTH;
    private static final int TEXT_OUT_PANEL_HEIGHT = DISPLAY_PANEL_HEIGHT/5;

    private ImageProcess currentImage;
    private Drawing drawing;
    public UI(){
        currentImage  = null;
        Drawing drawing = new Drawing();
        drawing.drawRect(-0.4,-1.4,1.8,1.8);
        drawing.saveLines("testUno.txt");

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
        setupMenuBarItems();
        setVisible(true);
    }

    public void setupMenuBarItems(){

        /*For file menu*/
        openMenuItem = new JMenuItem("Open");
        saveMenuItem = new JMenuItem("Save");
        saveTestMenuItem = new JMenuItem("Save Test");

        /*For useless functionality*/
        darkThemeMenuItem = new JMenuItem("Dark theme :)");
        lightThemeMenuItem = new JMenuItem("Light Theme :)");

        /*For sim menu*/
        runSimMenuItem = new JMenuItem("Run Simulation");

        /*Adding listeners to the buttons*/
        openMenuItem.addActionListener(new OpenButtonListener());
        saveMenuItem.addActionListener(new SaveButtonListener());
        saveTestMenuItem.addActionListener(new SaveTestButtonListener());

        /*Adding to fileMenu*/
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveTestMenuItem);

        /*Adding the useless things into the thing*/
        customizeMenu.add(darkThemeMenuItem);
        customizeMenu.add(lightThemeMenuItem);

        /*Adding listeners to the useless things*/
        darkThemeMenuItem.addActionListener(new DarkThemeButtonListener());
        lightThemeMenuItem.addActionListener(new LightThemeButtonListener());

        /*Adding to sim menu*/
        simMenu.add(runSimMenuItem);

        /*Adding listeners to the sim menu items*/
        runSimMenuItem.addActionListener(new RunSimListener());
    }

    public void setupMenuPanel(){

    }

    public void setupDisplayScreen(){



    }

    private class OpenButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){

            JFileChooser openFileChooser = new JFileChooser();
            int status = openFileChooser.showOpenDialog(null); //Prompting user to open a file
            /*Checks if a file was selected*/
            if(status != openFileChooser.APPROVE_OPTION){
                System.out.println("No file selected");

            }else{
                String fileName = openFileChooser.getName(openFileChooser.getSelectedFile());
                openedImgFileName = fileName;
                currentImage = new ImageProcess(fileName); //Processing the opened image
                textOutputArea.append("\nloaded file: " + fileName);

                /*Getting image to display onto menuPanel*/
                try {
                    BufferedImage openedImg = ImageIO.read(new File(fileName));
                    JLabel pic = new JLabel(new ImageIcon(openedImg));
                    menuPanel.add(pic);

                }catch(IOException e){
                    e.printStackTrace();
                    System.out.println("Could not load that file!");

                }
            }
        }
    }

    private class SaveButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if(currentImage==null) {
                System.out.println("you haven't opened an image.");
            }
            JFileChooser saveFileChooser = new JFileChooser();
            int status = saveFileChooser.showSaveDialog(null); //Prompting user to open a file
            if(status != saveFileChooser.APPROVE_OPTION){
                System.out.println("No file selected");
            }else{
                String fileNameSave = saveFileChooser.getName(saveFileChooser.getSelectedFile());
                currentImage.save(fileNameSave);
                if(!currentImage.save(fileNameSave)) {
                    System.out.println("Could not save");
                }
            }
        }
    }

    private class SaveTestButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if(currentImage == null){
                System.out.println("You have not opened an image");
            }
            JFileChooser saveTestFileChooser = new JFileChooser();
            int status = saveTestFileChooser.showSaveDialog(null);
            if(status != saveTestFileChooser.APPROVE_OPTION){
                System.out.println("Save cancelled");
            }else{
                String fileNameSaveTest = saveTestFileChooser.getName(saveTestFileChooser.getSelectedFile());
                currentImage.saveTest(fileNameSaveTest);
                if(!currentImage.saveTest(fileNameSaveTest)){
                    System.out.println("Could not save test file");

                }
            }
        }
    }

    /*For the sensitive eyes :) (This was not a waste of time)*/
    private class DarkThemeButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){

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
    }
    /*(This was not a waste of time)*/
    private class LightThemeButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
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
    }

    private class RunSimListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            if(currentImage == null){
                textOutputArea.append("\nCan not run simulation: no image has been opened");
            }
            sim = new Simulation(openedImgFileName);
            sim.commenceSim();
        }
    }

    public static void main(String[] args) {
        UI ui = new UI();
        System.out.println("help");
    }
}
