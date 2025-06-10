package custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class Jbuttoncustom extends JButton {

    private boolean mousePress;
    private int round = 7; // Radius default
    private Color hoverBackgroundColor;
    private Color pressBackgroundColor; // Menyimpan warna latar belakang asli saat tidak di-press
    private Color borderColor;
    private int borderWidth = 0;

    public Jbuttoncustom() {
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(7, 15, 7, 15));
        pressBackgroundColor = getBackground(); // Inisialisasi warna press dengan warna default
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    mousePress = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    mousePress = false;
                    repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if (hoverBackgroundColor != null) {
                    setBackground(hoverBackgroundColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                setBackground(pressBackgroundColor); // Kembalikan ke warna sebelum hover/press
            }
        });
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = Math.max(0, round);
        repaint();
    }

    public Color getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(Color hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = Math.max(0, borderWidth);
        repaint();
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        pressBackgroundColor = bg; // Update warna press saat warna latar belakang diubah
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphic) {
        Graphics2D g2 = (Graphics2D) graphic.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color currentBackground = getBackground();
        if (mousePress) {
            currentBackground = currentBackground.darker();
        }

        int arc = Math.min(getWidth(), getHeight()); // Default ke bentuk bulat penuh
        if (round > 0) {
            arc = round;
        }

        g2.setColor(currentBackground);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));

        g2.dispose();
        super.paintComponent(graphic);
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (borderColor != null && borderWidth > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth));
            int arc = Math.min(getWidth(), getHeight());
            if (round > 0) {
                arc = round;
            }
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - borderWidth, getHeight() - borderWidth, arc, arc));
            g2.dispose();
        }
    }
}
