/**
 *
 * @AliciaJava 2024
 */
package calculator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

/**
 *
 * @AliciaJava
 * 
 */
public class KeyboardPanel extends JPanel {
    private static final char[] keyLabels = {
        'L', 'O', 'C', '/',
        '7', '8', '9', '*',
        '4', '5', '6', '-',
        '1', '2', '3', '+',
        '0', '.', '='
    };
    
    public KeyboardPanel(KeyListener listener, CalcLookAndFeel lookAndFeel) {
        super();
        setBackground(lookAndFeel.getKeyboardBackgroundColor());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                lookAndFeel.setKeyFont(null, null);
            }
        });

        GridBagLayout gl = new GridBagLayout();
        setLayout(gl);

        GridBagConstraints constraints = new GridBagConstraints();
        for (int r=0; r<5; r++) {
            for (int c=0; c<4; c++) {
                if (r==4 && c==3)
                    break;
                constraints.gridx = c;
                constraints.gridy = r;
                constraints.weightx = 1.0;
                constraints.weighty = 1.0;
                constraints.gridwidth = 1;
                constraints.gridheight = 1;
                constraints.fill = GridBagConstraints.BOTH;
                char chLabel = keyLabels[r*4 + c];
                CalcKey key = new CalcKey(chLabel, listener, lookAndFeel);
                if (chLabel == 'O')
                    key.setKeyColor(lookAndFeel.getKeyColor2());
                else if (chLabel == '=')
                    key.setKeyColor(lookAndFeel.getKeyColor3());
                else if ((chLabel == '+') || (chLabel == '-') || (chLabel == '*') || (chLabel == '/'))
                    key.setKeyColor(lookAndFeel.getKeyColor4());
                if (r==3 && c==3) {
                    key.setVerticalKey();
                    constraints.gridheight = 2;
                }
                add(key, constraints);
            }
        }
    }
}


