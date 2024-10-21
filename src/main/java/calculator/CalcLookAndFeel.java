/**
 *
 * @author 2023
 */

package calculator;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

/**
 *
 * @AliciaJava 2024
 * 
 */
public class CalcLookAndFeel {
    private Color screenBackgroundColor = new Color(0xc3cdc8);          // Fondo pantalla lcd
    private Color screenLightBackgroundColor = new Color(0x03f705);     // Fondo fosforescente
    private Color keyboardBackgroundColor = new Color(100, 100, 100);   // Fondo calculadora
    
    private Color keyColor = new Color(0x4a587f);   // Color de teclas
    private Color keyColor2 = new Color(196, 0, 0); // Tecla OFF
    private Color keyColor3 = new Color(0xdba400);  // Tecla =
    private Color keyColor4 = new Color(0xf47c47);  // Teclas + - * /
    private Color keyTxtColor = Color.WHITE; // Color del texto de la tecla
    private Color keyShadowColor = new Color(30, 30, 30, 100); // Color de la sobra de una tecla.
    private Color keyMarginColor = Color.BLACK;   // Margen de la tecla.
    private Color keyOutline = new Color(255, 255, 255, 100); // Para resaltar contorno
    
    private Font keyFont;
    private FontMetrics keyFontMetrics;
    
    public Color getScreenBackgroundColor() {
        return screenBackgroundColor;
    }
    
    public Color getScreenLightBackgroundColor() {
        return screenLightBackgroundColor;
    }
    
    public Color getKeyboardBackgroundColor() {
        return keyboardBackgroundColor;
    }
    
    public Color getKeyColor() {
        return keyColor;
    }
    
    public Color getKeyColor2() {
        return keyColor2;
    }
    
    public Color getKeyColor3() {
        return keyColor3;
    }
    
    public Color getKeyColor4() {
        return keyColor4;
    }
    
    public Color getKeyTxtColor() {
        return keyTxtColor;
    }
    
    public Font getKeyFont() {
        return keyFont;
    }
    
    public FontMetrics getKeyFontMetrics() {
        return keyFontMetrics;
    }
    
    public void setKeyFont(Font keyFont, FontMetrics keyFontMetrics) {
        this.keyFont = keyFont;
        this.keyFontMetrics = keyFontMetrics;
    }
    
    public Color getKeyShadowColor() {
        return keyShadowColor;
    }
    
    public Color getKeyMarginColor() {
        return keyMarginColor;
    }
    
    public Color getKeyOutline() {
        return keyOutline;
    }
}

