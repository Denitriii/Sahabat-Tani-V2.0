/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package custom;


import java.awt.*;
import java.awt.event.*;
import java.beans.BeanProperty;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class RoundedPanelButton extends JPanel {
    private int cornerRadius = 10;
    private Color panelColor = Color.LIGHT_GRAY;
    private JLabel label;
    private final List<ActionListener> actionListeners = new ArrayList<>();

    public RoundedPanelButton() {
        super();
        setOpaque(false);
        setLayout(new GridBagLayout()); // Supaya teks di tengah

        label = new JLabel();
        label.setFont(new Font("Tahoma", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        add(label);

        // Interaksi klik
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireActionEvent();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                // Tambahan efek hover jika kamu ingin (opsional)
                // setBackground(panelColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
                // setBackground(panelColor); // reset warna jika ada efek hover
            }
        });
    }

    // Untuk menyimpan tulisan di panel
    @BeanProperty(preferred = true, description = "The text displayed on the button")
    public void setText(String text) {
        label.setText(text);
    }

    @BeanProperty(description = "Get the text displayed on the button")
    public String getText() {
        return label.getText();
    }

    public void setTextColor(Color color) {
        label.setForeground(color);
    }

    // Action listener seperti JButton
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
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(panelColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
    }
}

