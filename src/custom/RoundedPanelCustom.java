package custom;

import kasir.manajemenakun.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.beans.BeanProperty;
import javax.swing.JPanel;

public class RoundedPanelCustom extends JPanel {
    // Radius individual untuk tiap sudut
    private int topLeftRadius = 20;
    private int topRightRadius = 20;
    private int bottomLeftRadius = 0;
    private int bottomRightRadius = 0;

    private Color panelColor = Color.LIGHT_GRAY;

    public RoundedPanelCustom() {
        super();
        setOpaque(false); // Agar background transparan dan hanya menggambar panel custom
    }

    // Setter gabungan (custom utama)
    @BeanProperty(preferred = true, description = "Set corner radius for all corners")
    public void setCornerRadius(int radius) {
        this.topLeftRadius = radius;
        this.topRightRadius = radius;
        this.bottomLeftRadius = radius;
        this.bottomRightRadius = radius;
        repaint();
    }

    // Setter individual
    public void setCornerRadius(int topLeft, int topRight, int bottomLeft, int bottomRight) {
        this.topLeftRadius = topLeft;
        this.topRightRadius = topRight;
        this.bottomLeftRadius = bottomLeft;
        this.bottomRightRadius = bottomRight;
        repaint();
    }

    public int getCornerRadius() {
        // Nilai gabungan (kamu bisa ubah sesuai preferensi — di sini dikembalikan TL)
        return topLeftRadius;
    }

    public void setPanelColor(Color color) {
        this.panelColor = color;
        repaint();
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (bg != null) {
            this.panelColor = bg;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(panelColor);

        Path2D path = new Path2D.Double();

        // Mulai dari kiri atas
        path.moveTo(0 + topLeftRadius, 0);

        // Garis atas ke kanan atas (kurva)
        path.lineTo(w - topRightRadius, 0);
        path.quadTo(w, 0, w, topRightRadius);

        // Kanan ke bawah kanan
        path.lineTo(w, h - bottomRightRadius);
        path.quadTo(w, h, w - bottomRightRadius, h);

        // Bawah ke kiri bawah
        path.lineTo(bottomLeftRadius, h);
        path.quadTo(0, h, 0, h - bottomLeftRadius);

        // Kiri ke atas kiri
        path.lineTo(0, topLeftRadius);
        path.quadTo(0, 0, topLeftRadius, 0);

        g2.fill(path);
        g2.dispose();
    }
}
