package customComponen ;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import javax.swing.border.EmptyBorder;

public class RoundedScrollPane extends JScrollPane {

    private int roundTopLeft = 0;
    private int roundTopRight = 0;
    private int roundBottomLeft = 0;
    private int roundBottomRight = 0;

    public RoundedScrollPane() {
        setOpaque(false);
        getViewport().setOpaque(false);

        // Set these properties to remove the border line
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setViewportBorder(null);

        // Use custom scrollbars
        setVerticalScrollBar(new scrollBarCustom());
        setHorizontalScrollBar(new scrollBarCustom());
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Set transparent background
        setBackground(new Color(0, 0, 0, 0));
    }

    public RoundedScrollPane(JComponent view) {
        super(view);
        setOpaque(false);
        getViewport().setOpaque(false);

        // Same border removal for this constructor
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setViewportBorder(null);

        // Use custom scrollbars
        setVerticalScrollBar(new scrollBarCustom());
        setHorizontalScrollBar(new scrollBarCustom());

        // Set transparent background
        setBackground(new Color(0, 0, 0, 0));
    }

    // Getter and setter methods (unchanged)
    public int getRoundTopLeft() {
        return roundTopLeft;
    }

    public void setRoundTopLeft(int roundTopLeft) {
        this.roundTopLeft = Math.max(0, roundTopLeft);
        repaint();
    }

    public int getRoundTopRight() {
        return roundTopRight;
    }

    public void setRoundTopRight(int roundTopRight) {
        this.roundTopRight = Math.max(0, roundTopRight);
        repaint();
    }

    public int getRoundBottomLeft() {
        return roundBottomLeft;
    }

    public void setRoundBottomLeft(int roundBottomLeft) {
        this.roundBottomLeft = Math.max(0, roundBottomLeft);
        repaint();
    }

    public int getRoundBottomRight() {
        return roundBottomRight;
    }

    public void setRoundBottomRight(int roundBottomRight) {
        this.roundBottomRight = Math.max(0, roundBottomRight);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Create a clean graphics context
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clear the background with transparency
        g2.setColor(new Color(0, 0, 0, 0));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Draw the rounded background
        g2.setColor(getBackground());

        // Simplified approach for rounded corners
        int maxRound = Math.max(Math.max(roundTopLeft, roundTopRight),
                Math.max(roundBottomLeft, roundBottomRight));

        RoundRectangle2D.Double shape = new RoundRectangle2D.Double(
                0, 0, getWidth() - 1, getHeight() - 1, maxRound, maxRound);
        g2.fill(shape);

        g2.dispose();

        // Call the parent's paintComponent without painting the border
        super.paintComponent(g);
    }

    // Override UI settings that might add borders
    @Override
    public void updateUI() {
        super.updateUI();
        // Make sure we reset borders after UI update
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setViewportBorder(null);
    }

    // Allow only EmptyBorder to be set
    @Override
    public void setBorder(javax.swing.border.Border border) {
        // Only allow EmptyBorder or null
        super.setBorder(new EmptyBorder(0, 0, 0, 0));
    }
}
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.geom.RoundRectangle2D;
//import javax.swing.JComponent;
//import javax.swing.JScrollPane;
//import javax.swing.border.EmptyBorder;
//
//public class RoundedScrollPane extends JScrollPane {
//
//    private int roundTopLeft = 0;
//    private int roundTopRight = 0;
//    private int roundBottomLeft = 0;
//    private int roundBottomRight = 0;
//
//    public RoundedScrollPane() {
//        setOpaque(false);
//        getViewport().setOpaque(false);
//        getViewport().setBackground(getBackground()); // Ini mungkin masih background default
//        setVerticalScrollBar(new scrollBarCustom());
//        setHorizontalScrollBar(new scrollBarCustom());
//        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        setBackground(new Color(0, 0, 0, 0)); // Transparan
//        setBorder(new EmptyBorder(0, 0, 0, 0));
//    }
//
//    public RoundedScrollPane(JComponent view) {
//        super(view);
//        setOpaque(false);
//        getViewport().setOpaque(false);
//    }
//
//    public int getRoundTopLeft() {
//        return roundTopLeft;
//    }
//
//    public void setRoundTopLeft(int roundTopLeft) {
//        this.roundTopLeft = Math.max(0, roundTopLeft);
//        repaint();
//    }
//
//    public int getRoundTopRight() {
//        return roundTopRight;
//    }
//
//    public void setRoundTopRight(int roundTopRight) {
//        this.roundTopRight = Math.max(0, roundTopRight);
//        repaint();
//    }
//
//    public int getRoundBottomLeft() {
//        return roundBottomLeft;
//    }
//
//    public void setRoundBottomLeft(int roundBottomLeft) {
//        this.roundBottomLeft = Math.max(0, roundBottomLeft);
//        repaint();
//    }
//
//    public int getRoundBottomRight() {
//        return roundBottomRight;
//    }
//
//    public void setRoundBottomRight(int roundBottomRight) {
//        this.roundBottomRight = Math.max(0, roundBottomRight);
//        repaint();
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g.create();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setColor(getBackground());
//
//        java.awt.geom.Area roundedArea = new java.awt.geom.Area(new RoundRectangle2D.Double(
//                0, 0, getWidth(), getHeight(), roundTopLeft, roundTopLeft));
//        roundedArea.intersect(new java.awt.geom.Area(new RoundRectangle2D.Double(
//                0, 0, getWidth(), getHeight(), roundTopRight, roundTopRight)));
//        roundedArea.intersect(new java.awt.geom.Area(new RoundRectangle2D.Double(
//                0, 0, getWidth(), getHeight(), roundBottomLeft, roundBottomLeft)));
//        roundedArea.intersect(new java.awt.geom.Area(new RoundRectangle2D.Double(
//                0, 0, getWidth(), getHeight(), roundBottomRight, roundBottomRight)));
//
//        g2.fill(roundedArea);
//
//        g2.dispose();
//        super.paintComponent(g);
//    }
//
//    @Override
//    public void setBorder(javax.swing.border.Border border) {
//        if (border instanceof EmptyBorder) {
//            super.setBorder(border);
//        }
//    }
//}
