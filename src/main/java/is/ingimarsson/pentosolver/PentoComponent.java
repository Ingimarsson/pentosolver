package is.ingimarsson.pentosolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.Random;

public class PentoComponent extends JComponent {
    String[] board;

    int height = 0;
    int width = 0;

    // Notkun: PentoComponent c = new PentoComponent();
    // Fyrir:  Ekkert.
    // Eftir:  c er afbrigði af JComponent sem teiknar fimmferning,
    //         fimmferningurinn er upphafsstilltur sem tómur
    public PentoComponent(int w, int h) {
        this.width = w;
        this.height = h;

        this.setBoard(new String[] {""});

        PentoComponent thisPento = this;
        this.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                if("board".equals(propertyName)) {
                    thisPento.repaint();
                }
   
            }
        });
    }

    // Notkun: c.setBoard(String[] b)
    // Fyrir:  c er PentoComponent og b er borð framsett sem fylki
    //         strengja þar sem hver strengur er hver lína í borðinu,
    //         strengur má innihalda bilstreng fyrir autt bil, stjörnu
    //         fyrir frátekið bil eða stafina FILPNTUVWXYZ sem tákna
    //         einhvern af 12 mismunandi fimmferninguum
    // Eftir:  PentoComponent c birtir nýja borðið b.
    public void setBoard(String[] b) {
        this.firePropertyChange("board", this.board, b);
        this.board = b;
    }

    // Notkun: String[] b = c.getBoard();
    // Fyrir:  c er PentoComponent
    // Eftir:  b er núverandi borð
    public String[] getBoard() {
        return this.board;
    }

    public char getBlock(int x, int y) {
        return this.board[y].charAt(x);
    }

    public void setBlock(int x, int y, char block) {
        String[] b = this.getBoard();

        char[] changedLine = b[y].toCharArray();
        changedLine[x] = block;

        b[y] = String.valueOf(changedLine);

        this.setBoard(b);
        this.repaint();

        for (int i=0; i<b.length; i++) {
            System.out.println(b[i]);
        }
    }

    @Override
    public void paintComponent( Graphics g ) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        int x = 0;
        int y = 0;

        int blockWidth = this.width/this.board[0].length();
        int blockHeight = this.height/this.board.length;

        for (String line : this.getBoard()) {
          for (char ch : line.toCharArray()) {
              // Hér skilgreinum við ólíka liti fyrir hvern fimmferning
              switch (ch) {
                  case 'F':
                      g.setColor(new Color(50,100,150));
                      break;
                  case 'I':
                      g.setColor(new Color(0,0,80));
                      break;
                  case 'L':
                      g.setColor(new Color(0,100,80));
                      break;
                  case 'P':
                      g.setColor(new Color(0,100,200));
                      break;
                  case 'N':
                      g.setColor(new Color(0,200,150));
                      break;
                  case 'T':
                      g.setColor(new Color(80,100,0));
                      break;
                  case 'U':
                      g.setColor(new Color(80,100,150));
                      break;
                  case 'V':
                      g.setColor(new Color(80,200,50));
                      break;
                  case 'W':
                      g.setColor(new Color(80,200,250));
                      break;
                  case 'X':
                      g.setColor(new Color(160,0,150));
                      break;
                  case 'Y':
                      g.setColor(new Color(160,0,0));
                      break;
                  case 'Z':
                      g.setColor(new Color(160,100,200));
                      break;
                  case '*':
                      g.setColor(Color.GRAY);
                      break;
                  default:
                      g.setColor(Color.WHITE);
              }
              g.fillRect(x,y,blockWidth,blockHeight);
              g.draw3DRect(x,y,blockWidth,blockHeight,false);
              x = x+blockWidth;
          }
          x = 0;
          y = y+blockHeight;
        }

        // Teiknum ramma utan um borðið
        g.setColor(Color.BLACK);
        g.draw3DRect(0, 0, this.width-1, this.height-1, false);
    }
}
