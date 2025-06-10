package custom;

import java.awt.*;
import java.awt.event.*;
import java.beans.BeanProperty;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ButtonPhotos extends JPanel {
    private int cornerRadius = 10;
    private Color panelColor = Color.LIGHT_GRAY;

    private JLabel label;

    // JLabel custom yang bisa scale gambar otomatis
    private class ScaledIconLabel extends JLabel {
        private Image image;

        public void setImage(Image img) {
            this.image = img;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int w = getWidth();
                int h = getHeight();
                g.drawImage(image, 0, 0, w, h, this);
            }
        }
    }

    private ScaledIconLabel iconLabel;
    private Icon icon;

    private final List<ActionListener> actionListeners = new ArrayList<>();

    public ButtonPhotos() {
        super();
        setOpaque(false);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.CENTER;

        iconLabel = new ScaledIconLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        add(iconLabel, gbc);

        label = new JLabel();
        label.setFont(new Font("Tahoma", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        gbc.gridy = 1;
        add(label, gbc);

        // Interaksi klik
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireActionEvent();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
        
    iconLabel.addComponentListener(new ComponentAdapter() {
    @Override
    public void componentResized(ComponentEvent e) {
        if (icon == null) return;

        Image img = null;

        if (icon instanceof ImageIcon) {
            img = ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            if (w > 0 && h > 0) {
                java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.getGraphics();
                icon.paintIcon(null, g, 0, 0);
                g.dispose();
                img = bi;
            }
        }

        if (img != null) {
            int w = iconLabel.getWidth();
            int h = iconLabel.getHeight();
            if (w > 0 && h > 0) {
                Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(scaled));
            }
        }
    }
});

        
    }

    public void setHorizontalTextPosition(int position) {
        label.setHorizontalTextPosition(position);
    }

    public int getHorizontalTextPosition() {
        return label.getHorizontalTextPosition();
    }

    public void setVerticalTextPosition(int position) {
        label.setVerticalTextPosition(position);
    }

    public int getVerticalTextPosition() {
        return label.getVerticalTextPosition();
    }

    @BeanProperty(preferred = true, description = "The text displayed on the button")
    public void setText(String text) {
        label.setText(text);
    }

    @BeanProperty(description = "Get the text displayed on the button")
    public String getText() {
        return label.getText();
    }

    @BeanProperty(preferred = true, description = "The icon displayed on the button")
    public void setIcon(Icon icon) {
        this.icon = icon;

        if (icon != null && iconLabel.getWidth() > 0 && iconLabel.getHeight() > 0) {
            Image img = null;

            // Paksa ambil image meskipun bukan ImageIcon langsung
            if (icon instanceof ImageIcon) {
                img = ((ImageIcon) icon).getImage();
            } else {
                // Buat BufferedImage baru dari icon biasa
                int w = icon.getIconWidth();
                int h = icon.getIconHeight();
                if (w > 0 && h > 0) {
                    java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                    Graphics g = bi.getGraphics();
                    icon.paintIcon(null, g, 0, 0);
                    g.dispose();
                    img = bi;
                }
            }

            if (img != null) {
                int w = iconLabel.getWidth();
                int h = iconLabel.getHeight();
                Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(scaled));
            } else {
                iconLabel.setIcon(icon); // fallback
            }

        } else {
            iconLabel.setIcon(icon); // fallback
        }

        revalidate();
        repaint();
    }

 

    @BeanProperty(description = "Get the icon displayed on the button")
    public Icon getIcon() {
        return this.icon;
    }

    public void setTextColor(Color color) {
        label.setForeground(color);
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    private void fireActionEvent() {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "RoundedPanelClicked");
        for (ActionListener l : actionListeners) {
            l.actionPerformed(evt);
        }
    }

    @BeanProperty(preferred = true, description = "Set the corner radius of the panel")
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public int getCornerRadius() {
        return this.cornerRadius;
    }

    @BeanProperty(preferred = true, description = "Set the panel color")
    public void setPanelColor(Color color) {
        this.panelColor = color;
        repaint();
    }

    public Color getPanelColor() {
        return this.panelColor;
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
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(panelColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
    }
}
