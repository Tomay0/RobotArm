
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UI extends JFrame{
    JFrame mainFrame = new JFrame();
    JPanel menuPanel; //holds buttons and
    //JSlider thresholdSlider;
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private static final int MENU_PANEL_WIDTH = FRAME_WIDTH / 5;
    private static final int MENU_PANEL_HEIGHT = FRAME_HEIGHT;
    private ImageProcess currentImage;
    public UI(){
        currentImage  = null;
        Drawing drawing = new Drawing();
        drawing.drawRect(-0.4,-1.4,1.8,1.8);
        drawing.saveLines("testUno.txt");

        Container container = getContentPane();
        setLayout(new BorderLayout());
        setTitle("Image Processing");
        setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /*menuPanel setup*/
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(MENU_PANEL_WIDTH, MENU_PANEL_HEIGHT));
        menuPanel.setVisible(true);
        menuPanel.setBackground(Color.white);
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));

        /*Adding the menuPanel to mainFrame - menuPanel will hold most of the interaction*/
        container.add(menuPanel, BorderLayout.WEST); //Menu panel to the left of window


        setupMenuPanel();
        setVisible(true);
    }

    public void setupMenuPanel(){

        /*JButton objects*/
        JButton openFileButton = new JButton("Open File");
        JButton saveFileButton = new JButton("Save File");

        /*Adding listeners to the buttons*/
        openFileButton.addActionListener(new OpenButtonListener());
        saveFileButton.addActionListener(new SaveButtonListener());

        /*Adding to menuPanel*/
        menuPanel.add(openFileButton);
        menuPanel.add(saveFileButton);

        JSlider thresholdSlider = new JSlider();
        menuPanel.add(thresholdSlider);

    }

    private class OpenButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            JFileChooser openFileChooser = new JFileChooser();
            int status = openFileChooser.showOpenDialog(null); //Prompting user to open a file
            /*Checks if a file was selected*/
            if(status != openFileChooser.APPROVE_OPTION){
                System.out.println("No file selected");

            }else{
                String fileName = openFileChooser.getName(openFileChooser.getSelectedFile());
                currentImage = new ImageProcess(fileName);
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
                if(!currentImage.save(fileNameSave)) {
                    System.out.println("Could not save");
                }


            }
        }
    }

    public static void main(String[] args) {
        UI ui = new UI();
    }

}
