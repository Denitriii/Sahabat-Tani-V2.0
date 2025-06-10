

package custom;

import kasir.manajemenakun.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.BeanProperty;
import javax.swing.JPanel;

public class RoundedPanel01 extends JPanel {
    private int cornerRadius = 10; // Radius sudut
    private Color panelColor = Color.LIGHT_GRAY;
    
    public RoundedPanel01() {
        super();
        setOpaque(false); // Agar background transparan dan hanya menggambar panel custom
    }
    
    @BeanProperty(preferred = true, description = "Set the corner radius of the panel")
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public int getCornerRadius() {
        return this.cornerRadius;
    }
    
    public void setPanelColor(Color color) {
        this.panelColor = color;
        repaint();
    }
    
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (bg != null) {
            this.panelColor = bg; // Pastikan warna latar belakang tersimpan
            repaint();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(panelColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
    }
}
