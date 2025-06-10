package custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Bulat extends JComponent {
    private Color outlineColor = Color.BLACK;
    private Color fillColor = Color.RED;
    private int diameter = 8;
    private int strokeWidth = 2; // ketebalan outline

    // Variabel untuk drag-and-drop
    private Point mousePressedLocation;
    private Point componentLocation;

    public Bulat() {
        setPreferredSize(new Dimension(diameter + strokeWidth, diameter + strokeWidth));
        setSize(diameter + strokeWidth, diameter + strokeWidth);
        setOpaque(false); // biarkan latar belakang transparan

        // Tambahkan listener untuk drag-and-drop
        MouseAdapter dragListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressedLocation = e.getPoint();
                componentLocation = getLocation();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - mousePressedLocation.x;
                int dy = e.getY() - mousePressedLocation.y;
                setLocation(componentLocation.x + dx, componentLocation.y + dy);
            }
        };

        addMouseListener(dragListener);
        addMouseMotionListener(dragListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Aktifkan anti-aliasing agar lingkaran lebih halus
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Hitung inset agar stroke tidak terpotong
        int inset = strokeWidth / 2;
        int size = diameter; // ukuran lingkaran tanpa stroke

        // Gambar fill (ditarik ke dalam inset)
        g2.setColor(fillColor);
        g2.fillOval(inset, inset, size, size);

        // Gambar outline
        g2.setColor(outlineColor);
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.drawOval(inset, inset, size, size);

        g2.dispose();
    }

    // Setter dan getter untuk outline color
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        repaint();
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    // Setter dan getter untuk fill color
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        repaint();
    }

    public Color getFillColor() {
        return fillColor;
    }

    // Setter dan getter untuk diameter (ukuran lingkaran)
    public void setDiameter(int diameter) {
        this.diameter = diameter;
        // Perbarui preferredSize dan size juga mempertimbangkan strokeWidth
        setPreferredSize(new Dimension(diameter + strokeWidth, diameter + strokeWidth));
        setSize(diameter + strokeWidth, diameter + strokeWidth);
        revalidate();
        repaint();
    }

    public int getDiameter() {
        return diameter;
    }

    // Setter dan getter untuk ketebalan outline
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        // Perbarui ukuran komponen agar stroke ter-cover penuh
        setPreferredSize(new Dimension(diameter + strokeWidth, diameter + strokeWidth));
        setSize(diameter + strokeWidth, diameter + strokeWidth);
        revalidate();
        repaint();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    @Override
    public Dimension getPreferredSize() {
        // Pastikan ukuran komponen selalu mencakup stroke
        return new Dimension(diameter + strokeWidth, diameter + strokeWidth);
    }
}
