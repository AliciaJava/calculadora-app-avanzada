/**
 *
 * @AliciaJava 2024
 */
package calculator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class LCD7Seg {
    public static final int SEGMENT_0 = 0x1;
    public static final int SEGMENT_1 = 0x2;
    public static final int SEGMENT_2 = 0x4;
    public static final int SEGMENT_3 = 0x8;
    public static final int SEGMENT_4 = 0x10;
    public static final int SEGMENT_5 = 0x20;
    public static final int SEGMENT_6 = 0x40;
    
    private Color segmentColor = new Color(0x000000);
    private Color shadowColor = new Color(0, 0, 0, 64);
    private Color disabledColor = new Color(0, 0, 0, 16);

    private byte segmentState;
    private int x;
    private int y;
    private int digitWidth;
    private int segmentWidth;
    private Polygon segments[];
    private Polygon segmentShadows[];
    
    public LCD7Seg(int x, int y, int digitWidth) {
        this.segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_2 | SEGMENT_3 | SEGMENT_4 | SEGMENT_5 | SEGMENT_6;
        this.x = x;
        this.y = y;
        this.digitWidth = digitWidth;
        initSegments();
    }
    
    public void setPosition(int x, int y, int digitWidth) {
        this.x = x;
        this.y = y;
        this.digitWidth = digitWidth;
        initSegments();
    }
    
    public int getXPosition() {
        return x;
    }
    
    public int getYPosition() {
        return y;
    }
    
    public int getDigitWith() {
        return digitWidth;
    }
    
    public static int getSegmentWidth(int digitWidth) {
        return (int)((digitWidth * 0.13f)*2);
    }
    
    private void initSegments() {
        segments = new Polygon[7];
        segmentShadows = null;
        
        Rectangle r = new Rectangle(x, y, digitWidth, digitWidth*2);
        
        segmentWidth = getSegmentWidth(r.width);
        int w = segmentWidth/2;
        int gap = (int)(r.width * 0.03f);
        if (gap == 0)
            gap = 1;
        Point points[] = new Point[] {
            new Point(r.x + w*2, r.y),
            new Point(r.x + r.width - w*2, r.y),
            new Point(r.x + w, r.y + w),
            new Point(r.x + r.width - w, r.y + w),
            new Point(r.x + w*2, r.y + w*2),
            new Point(r.x + r.width - w*2, r.y + w*2)
        };
        
        Polygon p;
        p = new Polygon();
        p.addPoint(points[0].x+gap, points[0].y);
        p.addPoint(points[1].x-gap, points[1].y);
        p.addPoint(points[3].x-gap, points[3].y);
        p.addPoint(points[5].x-gap, points[5].y);
        p.addPoint(points[4].x+gap, points[4].y);
        p.addPoint(points[2].x+gap, points[2].y);
        segments[0] = p;
        
        int xPoints[] = new int[p.npoints];
        int yPoints[] = new int[p.npoints];
        for (int n=0; n<xPoints.length; n++) {
            xPoints[n] = p.xpoints[n];
            yPoints[n] = p.ypoints[n] + r.height/2 - w;
        }
        p = new Polygon(xPoints, yPoints, xPoints.length);
        segments[3] = p;
        
        for (int n=0; n<xPoints.length; n++) {
            yPoints[n] = p.ypoints[n] + r.height/2 - w;
        }
        p = new Polygon(xPoints, yPoints, xPoints.length);
        segments[6] = p;
        
        // Segmentos verticales
        points = new Point[] {
            new Point(r.x + w, r.y + w),
            new Point(r.x, r.y + w*2),
            new Point(r.x + w*2, r.y + w*2),
            new Point(r.x, r.y + r.height/2 - w),
            new Point(r.x + w*2, r.y + r.height/2 - w),
            new Point(r.x + w, r.y + r.height/2)
        };
        p = new Polygon();
        p.addPoint(points[0].x, points[0].y+gap);
        p.addPoint(points[2].x, points[2].y+gap);
        p.addPoint(points[4].x, points[4].y-gap);
        p.addPoint(points[5].x, points[5].y-gap);
        p.addPoint(points[3].x, points[3].y-gap);
        p.addPoint(points[1].x, points[1].y+gap);
        segments[1] = p;
        
        for (int n=0; n<xPoints.length; n++) {
            xPoints[n] = p.xpoints[n] + r.width - w*2;
            yPoints[n] = p.ypoints[n];
        }
        p = new Polygon(xPoints, yPoints, xPoints.length);
        segments[2] = p;

        p = segments[1];
        for (int n=0; n<xPoints.length; n++) {
            xPoints[n] = p.xpoints[n];
            yPoints[n] = p.ypoints[n] + r.height/2 - w;
        }
        p = new Polygon(xPoints, yPoints, xPoints.length);
        segments[4] = p;

        p = segments[2];
        for (int n=0; n<xPoints.length; n++) {
            xPoints[n] = p.xpoints[n];
            yPoints[n] = p.ypoints[n] + r.height/2 - w;
        }
        p = new Polygon(xPoints, yPoints, xPoints.length);
        segments[5] = p;
        
        if (digitWidth >= 32) {
            // Incluir sombras
            int xoffset = (digitWidth/32) * 2;
            int yoffset = xoffset;
            segmentShadows = new Polygon[segments.length];
            for (int nPoly=0; nPoly<segmentShadows.length; nPoly++) {
                for (int n=0; n<xPoints.length; n++) {
                    xPoints[n] = segments[nPoly].xpoints[n] + xoffset;
                    yPoints[n] = segments[nPoly].ypoints[n] + yoffset;
                }
                p = new Polygon(xPoints, yPoints, xPoints.length);
                segmentShadows[nPoly] = p;
            }
        }
    }
    
    public void setSegmentColor(Color segmentColor) {
        this.segmentColor = segmentColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }
   
    public int getSegmentWidth() {
        return segmentWidth;
    }
    
    public void setValue(int value) {
        switch (value) {
            case 0:
                segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_2 | SEGMENT_4 | SEGMENT_5 | SEGMENT_6;
                break;
            case 1:
                segmentState = SEGMENT_2 | SEGMENT_5;
                break;
            case 2:
                segmentState = SEGMENT_0 | SEGMENT_2 | SEGMENT_3 | SEGMENT_4 | SEGMENT_6;
                break;
            case 3:
                segmentState = SEGMENT_0 | SEGMENT_2 | SEGMENT_3 | SEGMENT_5 | SEGMENT_6;
                break;
            case 4:
                segmentState = SEGMENT_1 | SEGMENT_2 | SEGMENT_3 | SEGMENT_5;
                break;
            case 5:
                segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_3 | SEGMENT_5 | SEGMENT_6;
                break;
            case 6:
                segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_3 | SEGMENT_4 | SEGMENT_5 | SEGMENT_6;
                break;
            case 7:
                segmentState = SEGMENT_0 | SEGMENT_2 | SEGMENT_5;
                break;
            case 8:
                segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_2 | SEGMENT_3 | SEGMENT_4 | SEGMENT_5 | SEGMENT_6;
                break;
            case 9:
                segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_2 | SEGMENT_3 | SEGMENT_5 | SEGMENT_6;
                break;
            case 10:
                segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_2 | SEGMENT_3 | SEGMENT_4 | SEGMENT_5;
                break;
            case 11:
                segmentState = SEGMENT_1 | SEGMENT_3 | SEGMENT_4 | SEGMENT_5 | SEGMENT_6;
                break;
            case 12:
                segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_4 | SEGMENT_6;
                break;
            case 13:
                segmentState = SEGMENT_2 | SEGMENT_3 | SEGMENT_4 | SEGMENT_5 | SEGMENT_6;
                break;
            case 14:
                segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_3 | SEGMENT_4 | SEGMENT_6;
                break;
            case 15:
                segmentState = SEGMENT_0 | SEGMENT_1 | SEGMENT_3 | SEGMENT_4;
                break;
            case -1:
                segmentState = 0;
                break;
            default:
                segmentState = SEGMENT_3;
        }
    }
    
    public void setSegments(int mask) {
        segmentState = (byte) mask;
    }
    
    public void paintComponent(Graphics2D g2d) {
        byte mask = 1;
        g2d.setColor(segmentColor);
        for (int n=0; n<segments.length; n++) {
            if ((segmentState & mask) != 0) {
                if (segmentShadows != null) {
                    g2d.setColor(shadowColor);
                    g2d.fillPolygon(segmentShadows[n]);
                }
                g2d.setColor(segmentColor);
                g2d.fillPolygon(segments[n]);
            } else {
                g2d.setColor(disabledColor);
                g2d.fillPolygon(segments[n]);
            }
            mask *= 2;
        }
    }

}





