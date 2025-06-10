package custom;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PanelPasswordInput extends JPanel {
    private int cornerRadius = 10;
    private Color panelColor = Color.WHITE;
    private Color borderColor = Color.GRAY;
    private int borderThickness = 1;

    private JPasswordField passwordField;
    private JButton toggleButton;

    // Warna icon saat password terlihat
    private Color visibleIconColor = Color.RED;

    public PanelPasswordInput() {
        this(20);
    }

    public PanelPasswordInput(int columns) {
        super(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(columns * 10 + 40, 30)); // Tambah space buat tombol
        initComponents(columns);
    }

    private void initComponents(int columns) {
        passwordField = new JPasswordField(columns);
        passwordField.setBorder(new EmptyBorder(0, 10, 0, 10));
        passwordField.setOpaque(false);
        passwordField.setForeground(new Color(151, 151, 151));
        passwordField.setFont(getFont());

        // Tombol toggle show/hide password
        toggleButton = new JButton();
        toggleButton.setPreferredSize(new Dimension(30, 30));
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(true);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        toggleButton.setOpaque(false);
        toggleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleButton.setToolTipText("Show/Hide Password");

        // Icon lingkaran (bulat)
        toggleButton.setIcon(createCircleIcon(Color.WHITE));

        toggleButton.addActionListener(e -> togglePasswordVisibility());

        add(passwordField, BorderLayout.CENTER);
        add(toggleButton, BorderLayout.EAST);

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateButtonVisibility(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateButtonVisibility(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateButtonVisibility(); }
        });

        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
    char[] pwd = passwordField.getPassword();
    boolean isEmpty = pwd.length == 0;
        if (isEmpty) {
            toggleButton.setVisible(false);
        } else {
            toggleButton.setVisible(true);
            if (passwordField.getEchoChar() == (char) 0) { // visible
                toggleButton.setIcon(createCircleIcon(visibleIconColor)); // isi merah
                toggleButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // border putih
            } else { // hidden
                toggleButton.setIcon(createCircleIcon(new Color(255, 255, 255, 200)));
                toggleButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // border putih juga
            }
        }
    }


    private void togglePasswordVisibility() {
        if (passwordField.getEchoChar() != (char) 0) {
            // Tampilkan password
            passwordField.setEchoChar((char) 0);
        } else {
            // Sembunyikan password, default echo char
            passwordField.setEchoChar('\u2022'); // bullet char
        }
        updateButtonVisibility();
    }

    private Icon createCircleIcon(Color color) {
        int size = 8;
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Lingkaran isi
                g2.setColor(color);
                g2.fillOval(x, y, size, size);

                // Border lingkaran
                g2.setColor(color.darker());
                g2.setStroke(new BasicStroke(1f));
                g2.drawOval(x, y, size, size);

                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    // Setter warna icon merah
    public void setVisibleIconColor(Color color) {
        this.visibleIconColor = color;
        updateButtonVisibility();
    }

    public Color getVisibleIconColor() {
        return visibleIconColor;
    }

    public String getText() {
        return new String(passwordField.getPassword());
    }

    public void setText(String text) {
        passwordField.setText(text);
        updateButtonVisibility();
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (passwordField != null && font != null) {
            passwordField.setFont(font);
            passwordField.revalidate();
            passwordField.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background panel
        g2.setColor(panelColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Border
        if (borderThickness > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.drawRoundRect(
                borderThickness / 2,
                borderThickness / 2,
                getWidth() - borderThickness,
                getHeight() - borderThickness,
                cornerRadius, cornerRadius
            );
        }
        g2.dispose();
        super.paintComponent(g);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    @Override
    public void setBackground(Color bg) {
        this.panelColor = bg;
        super.setBackground(bg);
        repaint();
    }

    @Override
    public Color getBackground() {
        return panelColor;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        repaint();
    }

    public int getBorderThickness() {
        return this.borderThickness;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getToggleButton() {
        return toggleButton;
    }
}
