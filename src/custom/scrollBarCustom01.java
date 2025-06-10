package custom;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class scrollBarCustom01 extends JScrollBar {

    public scrollBarCustom01() {
        setUI(new ModernScrollBarUI01());
        setPreferredSize(new Dimension(8, 8));
        setForeground(new Color(70, 155, 126));
        setBackground(Color.WHITE);
    }
}
