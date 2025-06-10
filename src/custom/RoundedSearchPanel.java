package custom;

import java.awt.*;
import java.awt.event.*;
import java.beans.BeanProperty;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Custom rounded panel that behaves like a JTextField (search bar)
 */
public class RoundedSearchPanel extends JPanel {
    private int cornerRadius = 10;
    private Color panelColor = Color.WHITE;
    private JTextField textField;
    private SearchListener searchListener;
    private final String placeholder = "cari berdasarkan username...";
    private boolean showingPlaceholder = true;

    public interface SearchListener { void onSearch(String text); }

    public RoundedSearchPanel() {
        this(20);
    }

    public RoundedSearchPanel(int columns) {
        super(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(columns * 10 + 20, 30));
        initComponents(columns);
    }

    /**
     * Mengembalikan teks pencarian, _tanpa_ placeholder.
     */
    public String getText() {
        return showingPlaceholder ? "" : textField.getText();
    }

    /**
     * Set teks pencarian secara programatik (bila perlu).
     */
    public void setText(String text) {
        if (text == null || text.isEmpty()) {
            setPlaceholder();
        } else {
            showingPlaceholder = false;
            textField.setEditable(true);
            textField.setText(text);
            textField.setForeground(Color.BLACK);
            // Panggil listener dengan nilai sebenarnya
            if (searchListener != null) {
                searchListener.onSearch(text);
            }
        }
    }

    public void setSearchListener(SearchListener listener) {
        this.searchListener = listener;
    }

    public JTextField getTextField() {
        return textField;
    }

    private void initComponents(int columns) {
        // 1) Buat JTextField
        textField = new JTextField(columns);
        textField.setBorder(new EmptyBorder(0, 10, 0, 10));
        textField.setOpaque(false);
        textField.setFont(getFont());
        add(textField, BorderLayout.CENTER);

        // 2) Pasang placeholder di awal
        setPlaceholder();

        // 3) FocusListener: clear‐on‐focus & restore‐on‐blur
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    textField.setEditable(true);
                    showingPlaceholder = false;
                    // Begitu placeholder hilang, kirimkan onSearch("") agar
                    // filter tidak berjalan dengan placeholder
                    if (searchListener != null) {
                        searchListener.onSearch("");
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    setPlaceholder();
                    // Setelah placeholder kembali, pastikan listener mendapatkan string kosong
                    if (searchListener != null) {
                        searchListener.onSearch("");
                    }
                }
            }
        });

        // 4) DocumentListener: panggil onSearch hanya bila bukan placeholder
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                fireSearch();
            }
            public void removeUpdate(DocumentEvent e) {
                fireSearch();
            }
            public void changedUpdate(DocumentEvent e) {
                fireSearch();
            }
            private void fireSearch() {
                // Hanya jalankan onSearch jika placeholder sudah hilang
                if (!showingPlaceholder && searchListener != null) {
                    searchListener.onSearch(textField.getText());
                }
            }
        });
    }

    /**
     * Menetapkan placeholder: teks abu‐abu, tidak bisa diedit.
     */
    private void setPlaceholder() {
        showingPlaceholder = true;
        textField.setText(placeholder);
        textField.setForeground(new Color(200, 200, 200));
        textField.setEditable(false);
    }

    // ==================== setter/getter lain & paintComponent ====================
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

    @BeanProperty(description = "Font of the text in the search field")
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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(panelColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
        super.paintComponent(g);
    }
}