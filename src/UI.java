import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UI extends JFrame{
    JFrame mainFrame = new JFrame();
    JPanel menuPanel; //holds buttons and sliders
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private static final int MENU_PANEL_WIDTH = FRAME_WIDTH / 5;
    private static final int MENU_PANEL_HEIGHT = FRAME_HEIGHT;
    public UI(){

        Container container = getContentPane();
        setLayout(new BorderLayout());
        setTitle("Image Processing");
        setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /*menuPanel setup*/
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(MENU_PANEL_WIDTH, MENU_PANEL_HEIGHT));
        menuPanel.setVisible(true);
        menuPanel.setBackground(Color.white);
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));

        /*Adding the menuPanel to mainFrame - menuPanel will hold most of the interaction*/
        container.add(menuPanel, BorderLayout.WEST); //Menu panel to the left of window

        createButtons();
    }

    public void createButtons(){

        /*JButton objects*/
        JButton openFileButton = new JButton("Open File");
        JButton saveFileButton = new JButton("Save File");

        /*Adding listeners to the buttons*/
        openFileButton.addActionListener(new OpenButtonListener());
        saveFileButton.addActionListener(new SaveButtonListener());

        /*Adding to menuPanel*/
        menuPanel.add(openFileButton);
        menuPanel.add(saveFileButton);
    }

    private class OpenButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            JFileChooser openFileChooser = new JFileChooser();
            int status = openFileChooser.showOpenDialog(null); //Prompting user to open a file
            /*Checks if a file was selected*/
            if(status != openFileChooser.APPROVE_OPTION){
                System.out.println("No file selected");
            }else{
                try {
                    ImageProcess.load();//Need to make load static?

                }catch(IOException e){
                    e.printStackTrace();
                    System.out.println("Could not load image");
                }
            }
        }
    }

    private class SaveButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JFileChooser saveFileChooser = new JFileChooser();
            int status = saveFileChooser.showSaveDialog(null); //Prompting user to open a file
            if(status != saveFileChooser.APPROVE_OPTION){
                System.out.println("No file selected");
            }else{
                try{
                    ImageProcess.saveLines(); //Need to make saveLines static?
                }catch(IOException e){
                    e.printStackTrace();
                    System.out.println("Unable to save!");

                }

            }
        }
    }


}
