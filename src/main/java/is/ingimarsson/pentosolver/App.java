package is.ingimarsson.pentosolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;
import net.miginfocom.swing.MigLayout;

public class App {
    String[] board;

    JFrame mainFrame;
    JFrame solutionFrame;

    JButton openSolutionButton;
    JButton generateRandomButton;
    JButton resizeButton;

    JTextField heightField;
    JTextField widthField;

    PentoComponent mainPento;
    PentoComponent solutionPento;

    JFileChooser jfc;

    File file;

    public void handleOpen() {
        jfc.showOpenDialog(null);

        file = jfc.getSelectedFile();

        try {
            String content = new String(Files.readAllBytes(file.toPath()));

            mainPento.setBoard(content.split(System.getProperty("line.separator")));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void handleExport() {
        jfc.showSaveDialog(null);

        file = jfc.getSelectedFile();
 
        try {
            String content = String.join(System.getProperty("line.separator"), mainPento.getBoard());
            Files.write(file.toPath(), content.getBytes());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
 
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("File");

        menuItem = new JMenuItem("Open");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleOpen();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Export");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleExport();
            }
        });
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Help");
        menu.add(menuItem);

        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(menuItem);

        menuBar.add(menu);

        return menuBar;
    }

    public App() {
        board = new String[]{"   F", "  X ", "    ", "*   "};
        jfc = new JFileChooser();

        mainFrame = new JFrame("Pento Editor");
        solutionFrame = new JFrame("Pento Solver");

        mainPento = new PentoComponent(700,500);
        mainPento.setBoard(board);

        mainPento.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int x = e.getX()*mainPento.getBoard()[0].length()/700;
                int y = e.getY()*mainPento.getBoard().length/500;

                System.out.println(x);
                System.out.println(y);

                mainPento.setBlock(x,y, mainPento.getBlock(x,y) == '*' ? ' ' : '*');
                mainPento.setBoard(mainPento.getBoard());
            }
        });

        mainFrame.setSize(800,650);
        mainFrame.setResizable(false);

        solutionFrame.setSize(800,600);
        solutionFrame.setResizable(false);

        mainFrame.setLayout(new MigLayout("", "20[]20", "20[500]20[]"));

        openSolutionButton = new JButton("Solve");
        generateRandomButton = new JButton("Generate");
        resizeButton = new JButton("Resize");

        heightField = new JTextField(3);
        widthField = new JTextField(3);

        resizeButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int h = Integer.parseInt(heightField.getText());
                    int w = Integer.parseInt(widthField.getText());

                    String[] b = new String[h];

                    for (int i=0; i < h; i++) {
                        b[i] = StringUtils.repeat(" ", w);
                    }

                    mainPento.setBoard(b);
                }
            }
        );


        openSolutionButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    solutionFrame.setVisible(true);
                }
            }
        );

        mainFrame.add(mainPento, "cell 0 0 7 1, w 700!, h 500!, gapleft 30");
        mainFrame.add(new JLabel("Height"), "cell 0 1, gapleft 110");
        mainFrame.add(heightField, "cell 1 1");
        mainFrame.add(new JLabel("Width"), "cell 2 1");
        mainFrame.add(widthField, "cell 3 1");
        mainFrame.add(resizeButton, "cell 4 1");
        mainFrame.add(openSolutionButton, "cell 5 1");
        mainFrame.add(generateRandomButton, "cell 6 1");

        mainFrame.setJMenuBar(createMenuBar());

        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        App app = new App();
    }
}
