package custom;

import java.awt.*;
import java.awt.event.*;
import java.beans.BeanProperty;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

/**
 * Custom JPanel seperti JButton dan JLabel: bisa diberi tulisan dan diklik.
 */
public class Button extends JPanel {

    private String text = "Button";
    private Color textColor = Color.BLACK;
    private Font textFont = new Font("SansSerif", Font.PLAIN, 14);
    private int cornerRadius = 10;

    private final java.util.List<ActionListener> actionListeners = new ArrayList<>();

    public Button() {
        setOpaque(false);
        setBackground(Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Listener untuk klik
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireActionEvent();
            }
        });
    }

    // Property text
    @BeanProperty(preferred = true, description = "Text to display")
    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public String getText() {
        return text;
    }

    // Property warna teks
    @BeanProperty(preferred = true, description = "Text color")
    public void setTextColor(Color color) {
        this.textColor = color;
        repaint();
    }

    public Color getTextColor() {
        return textColor;
    }

    // Property font teks
    @BeanProperty(preferred = true, description = "Text font")
    public void setTextFont(Font font) {
        this.textFont = font;
        repaint();
    }

    public Font getTextFont() {
        return textFont;
    }

    // Property corner radius
    @BeanProperty(preferred = true, description = "Corner radius")
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    // Tambahkan ActionListener
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    private void fireActionEvent() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(event);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background dengan corner radius
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Gambar teks di tengah
        g2.setFont(textFont);
        g2.setColor(textColor);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(text, x, y);

        g2.dispose();
    }
}
