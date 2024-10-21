/**
 *
 * @AliciaJava 2024
 */

package calculator;

import java.awt.Graphics2D;

public class NDigitDisplay {
    int width, height;
    int numDigits;
    private LCD7Seg digits[];
    
    public NDigitDisplay(int width, int height, int numDigits) {
        this.numDigits = numDigits;
        setSize(width, height);
    }
    
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (digits == null) {
            digits = new LCD7Seg[numDigits];
            for (int n=0; n<numDigits; n++)
                digits[n] = new LCD7Seg(0, 0, 0);
        }
        
        float fgap = 0.26f;
        int digitWidth = width / (int)((numDigits + (fgap * (numDigits+1))) + 0.9f);
        if ((digitWidth*2 + 20) > height) {
            digitWidth = (height-20) / 2;
        }
        
        int space = (int)(digitWidth * fgap);
        int x = width - digitWidth*numDigits - space*(numDigits+1);
        int y = 10;
        for (int n=0; n<numDigits; n++) {
            int idx = numDigits - n - 1;
            digits[idx].setPosition(x, y, digitWidth);
            x += digitWidth + space;
        }
    }
    
    public void setValue(long value) {
        int n = 0;
        boolean minus = value < 0;
        if (minus)
            value = -value;
        if (digits == null)
            return;
        while (n < digits.length) {
            if ((value == 0) && (n > 0)) {
                if (minus) {
                    digits[n].setSegments(LCD7Seg.SEGMENT_3);
                    minus = false;
                } else
                    digits[n].setValue(-1);
            } else
                digits[n].setValue((byte)(value % 10));
            value /= 10;
            n++;
        }
        
        if ((value > 0) || minus) {
            clear();
            if (digits.length >= 5) {
                digits[4].setSegments(LCD7Seg.SEGMENT_0 | LCD7Seg.SEGMENT_1 | LCD7Seg.SEGMENT_3 | LCD7Seg.SEGMENT_4 | LCD7Seg.SEGMENT_6);
                digits[3].setSegments(LCD7Seg.SEGMENT_3 | LCD7Seg.SEGMENT_4);
                digits[2].setSegments(LCD7Seg.SEGMENT_3 | LCD7Seg.SEGMENT_4);
                digits[1].setSegments(LCD7Seg.SEGMENT_3 | LCD7Seg.SEGMENT_4 | LCD7Seg.SEGMENT_5 | LCD7Seg.SEGMENT_6);
                digits[0].setSegments(LCD7Seg.SEGMENT_3 | LCD7Seg.SEGMENT_4);
            } else if (digits.length >= 3) {
                digits[2].setSegments(LCD7Seg.SEGMENT_0 | LCD7Seg.SEGMENT_1 | LCD7Seg.SEGMENT_3 | LCD7Seg.SEGMENT_4 | LCD7Seg.SEGMENT_6);
                digits[1].setSegments(LCD7Seg.SEGMENT_3 | LCD7Seg.SEGMENT_4);
                digits[0].setSegments(LCD7Seg.SEGMENT_3 | LCD7Seg.SEGMENT_4);
            } else {
                digits[0].setSegments(LCD7Seg.SEGMENT_3);
            }
        }
    }
    
    public void clear() {
        if (digits == null)
            return;
        for (LCD7Seg d : digits)
            d.setValue(-1);
    }
    
    public void paintComponent(Graphics2D g2d) {
        if (digits == null)
            return;
        for (LCD7Seg d : digits) {
            d.paintComponent(g2d);
        }
    }
}


