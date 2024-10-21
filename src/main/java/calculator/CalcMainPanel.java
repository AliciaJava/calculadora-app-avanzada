/**
 *
 * @AliciaJava 2024
 */
package calculator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

/**
 *
 * @AliciaJava 2024
 * 
 */
public class CalcMainPanel extends JPanel {
    private CalcLookAndFeel lookAndFeel;
    private KeyListener keyListener;

    public CalcMainPanel(CalcLookAndFeel lookAndFeel, KeyListener keyListener) {
        super();
        this.lookAndFeel = lookAndFeel;
        this.keyListener = keyListener;
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (keyListener != null) {
                    char ch = e.getKeyChar();
                    if ( ((ch >= '0') && (ch <= '9')) || (ch == '+') || (ch == '-') ||
                         (ch == '*') || (ch == '/') || (ch == '=') || (ch == '.') ||
                         (ch == '\n') || (ch == 8)) {
                        if (ch == '\n')
                            ch = '=';
                        else if (ch == 8)
                            ch = 'C';
                        keyListener.keyPressed(ch);
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        Color c = lookAndFeel.getKeyboardBackgroundColor();
        
        Stroke saveStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(3));
        for (int n=0; n<16; n++) {
            g2d.setColor(new Color(n*(c.getRed()/16), n*(c.getGreen()/16), n*(c.getBlue()/16)));
            g2d.drawRoundRect(n, n, getWidth()-(n*2), getHeight()-(n*2), 24, 24);
        }
        g2d.setStroke(saveStroke);
    }
}


