/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KasirTransaki;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;
/**
 *
 * @author user
 */
public class customToggle extends JToggleButton {
    public customToggle(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("SansSerif", Font.BOLD, 12));
        setForeground(Color.WHITE);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bgColor = isSelected() ? Color.decode("#26B876") : Color.decode("#469B7E");
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        String text = getText();
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        g2.setColor(Color.WHITE);
        g2.setFont(getFont());
        g2.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3);

        g2.dispose();
    }

    // Override mouse event supaya bisa toggle off sendiri
    @Override
    protected void processMouseEvent(MouseEvent e) {
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            if (isSelected()) {
                // Jika tombol sudah selected dan diklik lagi, maka unselect
                setSelected(false);

                // Kirim event perubahan state supaya ButtonGroup tahu perubahan
                fireItemStateChanged(new java.awt.event.ItemEvent(this,
                    java.awt.event.ItemEvent.ITEM_STATE_CHANGED,
                    this,
                    java.awt.event.ItemEvent.DESELECTED));

                // Consume event supaya toggle default tidak dijalankan
                e.consume();
                return;
            }
        }
        super.processMouseEvent(e);
    }
}