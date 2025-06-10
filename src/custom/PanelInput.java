package custom;

import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.BeanProperty;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Custom rounded panel that behaves like a JTextField (search bar)
 */
public class PanelInput extends JPanel {
    private int cornerRadius = 10;
    private Color panelColor = Color.WHITE;
    private Color borderColor = Color.GRAY; // Tambahan untuk warna border
    private int borderThickness = 1;        // Tambahan untuk ketebalan border
    private JTextField textField;

    public PanelInput() {
        this(20);
    }

    public PanelInput(int columns) {
        super(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(columns * 10 + 20, 30));
        initComponents(columns);
    }

    private void initComponents(int columns) {
        textField = new JTextField(columns);
        textField.setBorder(new EmptyBorder(0, 10, 0, 10));
        textField.setOpaque(false);
        textField.setForeground(new Color(151, 151, 151));
        textField.setFont(getFont());
        add(textField, BorderLayout.CENTER);

        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { repaint(); }
            public void removeUpdate(DocumentEvent e) { repaint(); }
            public void changedUpdate(DocumentEvent e) { repaint(); }
        });
    }

    @BeanProperty(preferred = true, description = "Set the corner radius of the panel")
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @BeanProperty(description = "Get the corner radius of the panel")
    public int getCornerRadius() {
        return cornerRadius;
    }

    @BeanProperty(preferred = true, description = "Set the background color of the panel")
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

    @BeanProperty(preferred = true, description = "Set the border color")
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

    @BeanProperty(preferred = true, description = "Set the border thickness")
    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        repaint();
    }

    public int getBorderThickness() {
        return this.borderThickness;
    }

    @BeanProperty(preferred = true, description = "Set the text in the search field")
    public void setText(String text) {
        textField.setText(text);
    }

    @BeanProperty(description = "Get the text of the search field")
    public String getText() {
        return textField.getText();
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (textField != null && font != null) {
            textField.setFont(font);
            textField.revalidate();
            textField.repaint();
        }
    }

    @Override
    public Font getFont() {
        return textField != null ? textField.getFont() : super.getFont();
    }

    public void addActionListener(ActionListener listener) {
        textField.addActionListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Panel background
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

    public JTextField getTextField() {
        return textField;
    }
} 
