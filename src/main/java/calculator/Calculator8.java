/**
 *
 * @AliciaJava 2024
 */
package calculator;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class Calculator8 implements KeyListener {
    private JFrame mainFrame;
    private int numDigits = 9;
    private LCDPanel screen;
    private KeyboardPanel keyPad;
    private StringBuilder lastValue;
    private StringBuilder edValue;
    private char op;

    public Calculator8() {
        lastValue = new StringBuilder();
        edValue = new StringBuilder();
        op = 0;
    }
    
    private void calculate() {
        try {
            boolean errorFlag = false;
            double d0 = Double.parseDouble(lastValue.toString());
            double d1 = Double.parseDouble(edValue.toString());
            double res;
            switch (op) {
                case '+':
                    res = d0 + d1;
                    break;
                case '-':
                    res = d0 - d1;
                    break;
                case '*':
                    res = d0 * d1;
                    break;
                case '/':
                    res = d0 / d1;
                    if (Double.isNaN(res) || Double.isInfinite(res)) {
                        errorFlag = true;
                    } else if (Math.abs(res) < Double.MIN_NORMAL)
                        res = 0;
                    break;
                default:
                    res = 0;
            }
            String strRes;
            if (!errorFlag)
                strRes = String.format("%.9f", res);
            else
                strRes = "";
            int extra = strRes.length()-1;
            while ((extra > 0) && strRes.charAt(extra)=='0') {
                extra--;
            }
            if (extra < strRes.length() - 1)
                strRes = strRes.substring(0, extra+1);
            
            int idxDot = strRes.indexOf('.');
            int strResLen = strRes.length();
            if (idxDot >= 0)
                strResLen--;
            if (strResLen > numDigits) {
                // La parte fraccionaria se trunca
                strRes = strRes.substring(0, numDigits + (idxDot >= 0 ? 1 : 0));
                if (idxDot >= strRes.length()) {
                    // Se ha truncado el punto decimal
                    screen.displayError();
                    strRes = "";
                }
            }
            
            lastValue.setLength(0);
            edValue.setLength(0);
            edValue.append(strRes);
            op = 0;
            
            if (errorFlag)
                screen.displayError();
        } catch (NumberFormatException e) {
            System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
        }
    }
    
    @Override
    public void keyPressed(char code) {
        if (code == 'C') {
            if (edValue.length() > 0) {
                // clear
                edValue.setLength(0);
                screen.setValue(0);
            } else if (lastValue.length() > 0) {
                // clear all
                lastValue.setLength(0);
                screen.setValue(0);
                screen.repaint();
                op = 0;
            } else {
                screen.setValue(0);
                screen.repaint();
                op = 0;
            }
        } else if (code == 'O') {
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mainFrame.dispose();
                }
            }, 400);
        } else if (code == 'L') {
            screen.setLight();
        } else if ((code == '+') || (code == '-') || (code == '*') || (code == '/')) {
            if (op != 0) {
                calculate();
                if (edValue.length() > 0)
                    screen.setValue(edValue);
            } else if ((code == '-') && (lastValue.length()==0) && (edValue.length()==0)) {
                edValue.append(code);
                return;
            }
            op = code;
            lastValue.setLength(0);
            lastValue.append(edValue);
            edValue.setLength(0);
        } else if (code == '=') {
            if ((op == 0) || (lastValue.length() == 0) || (edValue.length() == 0))
                return;
            calculate();
            if (edValue.length() > 0)
                screen.setValue(edValue);
        } else {
            int decimalIndex = edValue.indexOf(".");
            int edValueLen = edValue.length() - (decimalIndex >= 0 ? 1 : 0);
            if (edValueLen < numDigits) {
                if (code == '.') {
                    if (decimalIndex >= 0) {
                        return;
                    }
                    if ((edValue.length() == 0) || ("-".equals(edValue.toString())))
                        edValue.append('0');
                }
                edValue.append(code);
                screen.setValue(edValue);
            }
        }
    }
    
    private void start() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame();
            mainFrame.setUndecorated(true);
            mainFrame.setTitle("Calculator 8 - Raul Cosio");
            Dimension minSize = new Dimension(385, (int)(385*1.6));
            mainFrame.setMinimumSize(minSize);
            mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            CalcLookAndFeel lookAndFeel = new CalcLookAndFeel();
            
            mainFrame.setBackground(lookAndFeel.getKeyboardBackgroundColor());
            CalcMainPanel globalPanel = new CalcMainPanel(lookAndFeel, this);
            globalPanel.setBackground(lookAndFeel.getKeyboardBackgroundColor());
            LayoutManager layout = new BoxLayout(globalPanel, BoxLayout.PAGE_AXIS);
            globalPanel.setLayout(layout);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                Point pt = new Point();
                Dimension size = new Dimension();
                boolean moveOrResize;
                
                @Override
                public void mousePressed(MouseEvent e) {
                    pt.x = e.getX();
                    pt.y = e.getY();
                    size.setSize(mainFrame.getSize());
                    moveOrResize = !( (pt.x >= (size.width-40)) && (pt.y >= (size.height-40)) );
                }
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (moveOrResize) {
                        mainFrame.setLocation(e.getXOnScreen()-pt.x, e.getYOnScreen()-pt.y);
                    } else {
                        mainFrame.setSize(size.width + e.getX()-pt.x, size.height + e.getY()-pt.y);
                    }
                }
            };
            globalPanel.addMouseListener(mouseAdapter);
            globalPanel.addMouseMotionListener(mouseAdapter);
            globalPanel.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Rectangle bounds = e.getComponent().getBounds();
                    screen.setPreferredSize(new Dimension(bounds.width, bounds.height/5));
                    keyPad.setPreferredSize(new Dimension(bounds.width, bounds.height*4/5));
                    e.getComponent().validate();

                    RoundRectangle2D rr2d = new RoundRectangle2D.Double(0, 0, bounds.width, bounds.height, 24, 24);
                    mainFrame.setShape(rr2d);
                }
            });

            screen = new LCDPanel(numDigits, lookAndFeel);
            screen.setPreferredSize(new Dimension(minSize.width, minSize.height/5));
            keyPad = new KeyboardPanel(this, lookAndFeel);
            keyPad.setPreferredSize(new Dimension(minSize.width, minSize.height*4/5));
            
            globalPanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 20, 20));
            globalPanel.add(screen);
            globalPanel.add(keyPad);
            
            mainFrame.setContentPane(globalPanel);
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            mainFrame.setVisible(true);
            globalPanel.requestFocus();
        });
    }
    
    public static void main(String[] args) {
        new Calculator8().start();
    }
}


