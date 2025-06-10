package custom;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;


public class ComboBox<E> extends JComboBox<E> {
    private int cornerRadius = 15;
    private Color borderColor = new Color(230, 230, 230);
    private Color arrowButtonColor = new Color(100, 149, 237); // Cornflower Blue
    private String placeholder = " "
            + "Pilih Role";

    public ComboBox() {
        setUI(new RoundedComboBoxUI());
        setOpaque(false);
        setBackground(Color.WHITE);
        setBorder(null);
    }
    
    
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public int getCornerRadius() {
        return this.cornerRadius;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public Color getBorderColor() {
        return this.borderColor;
    }

    public void setArrowButtonColor(Color color) {
        this.arrowButtonColor = color;
        repaint();
    }

    public Color getArrowButtonColor() {
        return this.arrowButtonColor;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Gambar background putih berbentuk bulat (round rect)
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground()); // putih
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        g2.dispose();

        // Biarkan BasicComboBoxUI melakukan paintCurrentValue (teks/placeholder & arrow)
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(borderColor);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        g2.dispose();
    }

    private class RoundedComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Draw transparent background (no box)
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Draw arrow only
                    g2.setComposite(AlphaComposite.SrcOver);
                    g2.setColor(arrowButtonColor);
                    int size = 6;
                    int x = getWidth() / 2;
                    int y = getHeight() / 2;
                    int[] xPoints = {x - size, x, x + size};
                    int[] yPoints = {y - 2, y + 3, y - 2};
                    g2.fillPolygon(xPoints, yPoints, 3);

                    g2.dispose();
                }
            };
            button.setPreferredSize(new Dimension(24, 0)); // <-- lebih ramping
            button.setFocusable(false);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            return button;
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            comboBox.setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            // Isi area teks dengan putih (menghapus highlight abu-abu)
            g2.setColor(comboBox.getBackground());
            g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

            // 2) Periksa apakah ada item terpilih
            if (comboBox.getSelectedItem() != null) {
                // Jika sudah dipilih, biarkan super yang menggambar teksnya,
                // di atas area yang sudah di‐fill putih:
                super.paintCurrentValue(g2, bounds, hasFocus);

            } else {
                // Belum ada item terpilih → gambar placeholder

                // Ambil renderer default
                @SuppressWarnings("unchecked")
                ListCellRenderer<? super E> renderer = (ListCellRenderer<? super E>) comboBox.getRenderer();
                // Kita perlu JList dummy untuk melewatkan ke renderer
                JList<E> dummyList = new JList<>();

                @SuppressWarnings("unchecked")
                E ph = (E) placeholder;
                
                // Minta renderer membuat komponen untuk placeholder
                Component c = renderer.getListCellRendererComponent(
                    dummyList,
                    ph,    // teks placeholder
                    -1,             // index = -1 (bukan bagian model)
                    false,          // isSelected = false
                    false           // cellHasFocus = false
                );

                // Sekarang kita override font & warna placeholder pada komponen tersebut:
                c.setFont(new Font("Tahoma", Font.PLAIN, comboBox.getFont().getSize()));
                c.setForeground(Color.GRAY);
                c.setBackground(comboBox.getBackground());
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(false);
                }
                // Hitung posisi (x, y) agar baseline placeholder sejajar persis dengan item terpilih nanti
                Insets insets = comboBox.getInsets();
                int textX = bounds.x + insets.left;

                // Ukuran renderer (agar tahu tinggi baris)
                Dimension pref = c.getPreferredSize();
                int textHeight = pref.height;

                // Vertikal: center di dalam bounds
                int textY = bounds.y + (bounds.height - textHeight) / 2 ;

                // Set bounds pada komponen renderer kemudian paint di lokasi tersebut
                c.setBounds(textX, textY,
                            bounds.width - insets.left - insets.right,
                            textHeight);
                c.paint(g2);
            }

            g2.dispose();
//            if (comboBox.getSelectedItem() == null && !placeholder.isEmpty()) {
//                Graphics2D g2 = (Graphics2D) g.create();
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2.setColor(comboBox.getBackground());
//                g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
//                // Gunakan Tahoma Italic, ukuran disamakan dengan font combobox default
//                g2.setColor(Color.BLACK);
//                Font tahomaPlain = new Font("Tahoma", Font.PLAIN, comboBox.getFont().getSize());
//                g2.setFont(tahomaPlain);
//                
//                FontMetrics fm = g2.getFontMetrics();
//                int textWidth = fm.stringWidth(placeholder);
//                int textHeight = fm.getHeight();
//                
//                Insets insets = comboBox.getInsets();
//                int textX = bounds.x + insets.left;
//                int ascent = fm.getAscent();
//                int textY = bounds.y + (bounds.height - textHeight) / 2 + ascent;
//                g2.drawString(placeholder, textX, textY);
//                g2.dispose();
////                Graphics2D g2 = (Graphics2D) g.create();
////                g2.setColor(Color.GRAY);
////                g2.setFont(comboBox.getFont().deriveFont(Font.ITALIC));
////                Insets insets = comboBox.getInsets();
////                int textX = bounds.x + insets.left;
////                int textY = bounds.y + g.getFontMetrics().getAscent();
////                g2.drawString(placeholder, textX, textY);
////                g2.dispose();
//            } else {
//                super.paintCurrentValue(g, bounds, hasFocus);
//            }
        }
        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox);

            // Dapatkan JList dari popup, atur background dan cell renderer:
            JList<?> list = popup.getList();
            list.setBackground( new Color(70,155,126));
            list.setSelectionBackground(new Color(230, 230, 230)); // highlight tanpa border
            list.setBorder(null);

            // Custom cell renderer: hilangkan border pada item terpilih
            list.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list,
                                                              Object value,
                                                              int index,
                                                              boolean isSelected,
                                                              boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setBorder(null);            // tidak ada border di tiap cell
                    label.setOpaque(true);
                    // Atur teks berwarna hitam (atau warna default), 
                    // dan atur background highlight bila isSelected
                    if (isSelected) {
                        label.setBackground(new Color(230, 230, 230));
                        label.setForeground(Color.BLACK);
                    } else {
                        label.setBackground(Color.WHITE);
                        label.setForeground(Color.BLACK);
                    }
                    return label;
                }
            });

            // Border hitam rounded pada kotak popup/dropdown
            popup.setBorder(new LineBorder(Color.BLACK, 1, true));
            return popup;
        }
    }
}
