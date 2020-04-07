package is.ingimarsson.pentosolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.net.URL;

import java.io.*;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;
import net.miginfocom.swing.MigLayout;

public class App {
    // Fastayrðing gagna
    //
    // solutions er listi af þeim lausnum sem finnast þegar
    // ýtt er á solve takkann. solutionNumber er númer þeirrar
    // lausnar í listanum sem er á skjánum á þeim tíma. file er
    // tilvik af þeirri skrá sem verið er að lesa eða skrifa í,
    // eða null ef engin skrá er tilgreind. Tvö tilvik af
    // PentoComponent eru tilgreind, mainPento er borðið sem 
    // birtist á editor skjánum og inniheldur aðeins '*' eða ' '.
    // solutionPento birtist þegar ýtt er á solve og sýnir þá
    // lausn úr solutions listanum sem solutionNumber tilgreinir.
    // Að auki eru tilvik af ýmsum swing components tilgreind hér.

    ArrayList<String[]> solutions;
    int solutionNumber;

    File file;

    PentoComponent mainPento;
    PentoComponent solutionPento;

    JFrame mainFrame;
    JFrame solutionFrame;
    JButton openSolutionButton;
    JButton generateRandomButton;
    JButton resizeButton;
    JButton nextSolutionButton;
    JButton playButton;
    JTextField heightField;
    JTextField widthField;
    JLabel solutionLabel;
    JFileChooser jfc;

    // Notkun: a.handleOpen()
    // Fyrir:  a er tilvik af App
    // Eftir:  Notandi velur skrá og hún er lesin inn í mainPento
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

    // Notkun: a.handleExport()
    // Fyrir:  a er tilvik af App
    // Eftir:  Notandi velur skrá og borðið í mainPento er skrifað í hana
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

    // Notkun: a.createMenuBar()
    // Fyrir:  a er tilvik af App
    // Eftir:  mainFrame glugginn birtir menu bar með skráaraðgerðum
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
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URL("https://google.com").toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
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

    // Notkun: a = new App()
    // Fyrir:  Ekkert.
    // Eftir:  a er tilvik af App með uppsettu viðmóti.
    public App() {
        String[] board = new String[]{"        ", "        ", "        ", "   **   ", "   **   ", "        ", "        ", "        "};
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

                /*
                System.out.println(x);
                System.out.println(y);
                */

                mainPento.setBlock(x,y, mainPento.getBlock(x,y) == '*' ? ' ' : '*');
                mainPento.setBoard(mainPento.getBoard());
            }
        });

        mainFrame.setSize(800,650);
        mainFrame.setResizable(false);

        solutionFrame.setSize(800,620);
        solutionFrame.setResizable(false);

        mainFrame.setLayout(new MigLayout("", "20[]20", "20[500]20[]"));
        solutionFrame.setLayout(new MigLayout("", "20[]20", "20[500]20[]"));

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

        generateRandomButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainPento.generateRandom();
                }
            }
        );

        openSolutionButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int empty = mainPento.countEmpty();

                    if (empty != 60) {
                        JOptionPane.showMessageDialog(mainFrame, "Board needs to have exactly 60 empty blocks. Current board has "+Integer.toString(empty)+".");
                        return;
                    }

                    solutions = new ArrayList<String[]>();
                    solutionNumber = 0;

                    int n=0;
                    for( String[] b: Pento.makeSolutions(mainPento.getBoard()) ) {
                        if (n==100) break;
                        solutions.add(b);
                        n++;
                    }

                    if (solutions.size()==0) {
                        JOptionPane.showMessageDialog(mainFrame, "No solutions found");  
                    } 
                    else {
                        solutionLabel.setText("Solution 1 / "+Integer.toString(solutions.size()));
                        solutionPento.setBoard(solutions.get(0));
                        solutionFrame.setVisible(true);
                    }
                }
            }
        );

        mainFrame.add(mainPento, "cell 0 0 7 1, w 700!, h 500!, gapleft 30");
        mainFrame.add(new JLabel("Height"), "cell 0 1, gapleft 120");
        mainFrame.add(heightField, "cell 1 1");
        mainFrame.add(new JLabel("Width"), "cell 2 1");
        mainFrame.add(widthField, "cell 3 1");
        mainFrame.add(resizeButton, "cell 4 1");
        mainFrame.add(generateRandomButton, "cell 5 1");
        mainFrame.add(openSolutionButton, "cell 6 1");

        mainFrame.setJMenuBar(createMenuBar());

        // Setjum upp lausnarglugga
        nextSolutionButton = new JButton("Next Solution");

        nextSolutionButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    solutionNumber = (solutionNumber+1) % 100;
                    solutionPento.setBoard(solutions.get(solutionNumber));
                    solutionLabel.setText("Solution "+Integer.toString(solutionNumber+1)+" / "+Integer.toString(solutions.size()));
                }
            }
        );

        solutionPento = new PentoComponent(700,500);
        solutionPento.emptyBoard();

        solutionLabel = new JLabel("");

        solutionFrame.add(solutionPento, "cell 0 0 7 1, w 700!, h 500!, gapleft 30");
        solutionFrame.add(nextSolutionButton, "cell 0 1, gapleft 30");
        solutionFrame.add(solutionLabel, "cell 1 1");
 
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        App app = new App();
    }
}
