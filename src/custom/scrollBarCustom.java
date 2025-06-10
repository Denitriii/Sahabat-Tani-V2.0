package Custom;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class scrollBarCustom extends JScrollBar {

    public scrollBarCustom() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(8, 8));
        setForeground(new Color(241, 241, 241));
        setBackground(Color.WHITE);
    }
}
