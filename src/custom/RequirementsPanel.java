
package custom;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Predicate;

public class RequirementsPanel extends JPanel {
    private final JLabel lengthLabel;
    private final JLabel uppercaseLabel;
    private final JLabel digitLabel;

    // Predikat (kondisi) untuk masing‐masing requirement:
    private final Predicate<String> lengthTest    = pwd -> pwd.length() >= 8;
    private final Predicate<String> uppercaseTest = pwd -> pwd.matches(".*[A-Z].*");
    private final Predicate<String> digitTest     = pwd -> pwd.matches(".*\\d.*");

    /**
     * Constructor default.
     * Anda harus memanggil bindTo(...) agar panel dapat mendeteksi perubahan di JPasswordField.
     */
    public RequirementsPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false); // transparan jika ingin
        lengthLabel    = createRequirementLabel("Minimal 8 karakter");
        uppercaseLabel = createRequirementLabel("Minimal 1 huruf kapital (A–Z)");
        digitLabel     = createRequirementLabel("Minimal 1 angka (0–9)");

        // Tambahkan ketiganya dalam urutan:
        add(lengthLabel);
        add(uppercaseLabel);
        add(digitLabel);
    }

    public RequirementsPanel(JPasswordField pwdField) {
        this();
        bindTo(pwdField);
    }

    private JLabel createRequirementLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Tahoma", Font.PLAIN, 10));
        lbl.setForeground(new Color(255,102,102)); // merah gelap
        lbl.setAlignmentX(LEFT_ALIGNMENT);         // agar kiri‐rata
        return lbl;
    }

    public void bindTo(JPasswordField pwdField) {
        // Tambahkan listener pada dokument field:
        pwdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> updateRequirements(pwdField));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> updateRequirements(pwdField));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> updateRequirements(pwdField));
            }
        });

        // Jalankan sekali saat inisialisasi agar keadaan awal (field kosong) muncul semua:
        updateRequirements(pwdField);
    }

    private void updateRequirements(JPasswordField pwdField) {
        String pwd = new String(pwdField.getPassword());

        if (pwd.isEmpty()) {
            this.setVisible(false);
        } else {
            this.setVisible(true);
            lengthLabel.setVisible(!lengthTest.test(pwd));
            uppercaseLabel.setVisible(!uppercaseTest.test(pwd));
            digitLabel.setVisible(!digitTest.test(pwd));
        }
        revalidate();
        repaint();
    }

    public JLabel getLengthLabel() {
        return lengthLabel;
    }

    /** Kembalikan label “Minimal 1 huruf kapital” */
    public JLabel getUppercaseLabel() {
        return uppercaseLabel;
    }

    /** Kembalikan label “Minimal 1 angka” */
    public JLabel getDigitLabel() {
        return digitLabel;
    }

    /**
     * Set font untuk salah satu requirement label.
     * @param lbl   Label (harus salah satu dari getLengthLabel, getUppercaseLabel, atau getDigitLabel)
     * @param font  Font baru
     */
    public void setRequirementFont(JLabel lbl, Font font) {
        lbl.setFont(font);
    }

    /**
     * Set warna font untuk semua requirement label.
     * @param c  Warna (misal Color.RED atau apa saja)
     */
    public void setRequirementColor(Color c) {
        lengthLabel   .setForeground(c);
        uppercaseLabel.setForeground(c);
        digitLabel    .setForeground(c);
    }
}
