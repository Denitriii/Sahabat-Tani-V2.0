package Custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.DefaultButtonModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class Jcomboboxcustom<E> extends JComboBox<E> {

    private Color lineColor = new Color(3, 155, 216);
    private int roundRadius = 10;
    private Color borderColor = new Color(217, 217, 217);
    private int borderWidth = 1;

    @Override
    public void updateUI() {
        super.updateUI();
        installUI();
    }

    private void installUI() {
        setUI(new ComboUI(this));

    }

    public Jcomboboxcustom() {
        setBackground(Color.WHITE);
        setBorder(new LineBorder(borderColor, borderWidth, true));
        installUI();

        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean isSelected, boolean hasFocus) {
                Component com = super.getListCellRendererComponent(jlist, o, i, isSelected, hasFocus);

                // Set consistent padding for all items (top, left, bottom, right)
                setBorder(new EmptyBorder(12, 15, 12, 15));

                // Set text color and background based on selection state
                if (isSelected) {
                    com.setBackground(new Color(235, 235, 235));
                    com.setForeground(Color.BLACK);
                } else {
                    com.setBackground(Color.WHITE);
                    com.setForeground(Color.BLACK);
                }

                return com;
            }
        });
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        repaint();
    }

    public int getRoundRadius() {
        return roundRadius;
    }

    public void setRoundRadius(int roundRadius) {
        this.roundRadius = Math.max(0, roundRadius);
        setBorder(new LineBorder(borderColor, borderWidth, true));
        repaint();
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        setBorder(new LineBorder(borderColor, borderWidth, true));
        repaint();
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = Math.max(0, borderWidth);
        setBorder(new LineBorder(borderColor, borderWidth, true));
        repaint();
    }

    private class ComboUI extends BasicComboBoxUI {

        private Jcomboboxcustom combo;

        public ComboUI(Jcomboboxcustom combo) {
            this.combo = combo;
            addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                    if (arrowButton != null) {
                        arrowButton.setBackground(new Color(200, 200, 200));
                    }
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                    if (arrowButton != null) {
                        arrowButton.setBackground(new Color(150, 150, 150));
                    }
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent pme) {
                    if (arrowButton != null) {
                        arrowButton.setBackground(new Color(150, 150, 150));
                    }
                }
            });
        }

        public void layoutComboBox(java.awt.Container parent, java.awt.Rectangle bounds) {
            Insets insets = parent.getInsets();
            int width = bounds.width - (insets.left + insets.right);
            int height = bounds.height - (insets.top + insets.bottom);
            int arrowWidth = 30;
            int arrowHeight = height;
            int arrowX = bounds.x + bounds.width - insets.right - arrowWidth;
            int arrowY = bounds.y + insets.top;

            if (arrowButton != null) {
                arrowButton.setBounds(arrowX, arrowY, arrowWidth, arrowHeight);
            }

            if (editor != null) {
                editor.setBounds(bounds.x + insets.left, bounds.y + insets.top, width - arrowWidth, height);
            }
        }

        @Override
        public void paintCurrentValueBackground(Graphics grphcs, Rectangle rctngl, boolean bln) {
            // Tidak melakukan apa-apa
        }

        @Override
        protected JButton createArrowButton() {
            return new ArrowButton();
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup pop = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    list.setFixedCellHeight(36);
                    JScrollPane scroll = new JScrollPane(list);
                    scroll.setBackground(Color.WHITE);
                    scroll.setBorder(new EmptyBorder(5, 0, 5, 0));

                    if (comboBox.getItemCount() > 4) {
                        scrollBarCustom sb = new scrollBarCustom();
                        sb.setUnitIncrement(36);
                        sb.setForeground(new Color(180, 180, 180));
                        scroll.setVerticalScrollBar(sb);
                    } else {
                        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    }
                    return scroll;
                }

            };
            pop.setBorder(new LineBorder(new Color(200, 200, 200), 1));
            pop.setLightWeightPopupEnabled(false);
            return pop;
        }

        @Override
        public void paint(Graphics grphcs, JComponent jc) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            int width = getWidth();
            int height = getHeight();
            int round = roundRadius;

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, width, height, round, round);

            g2.setColor(borderColor);
            g2.setStroke(new java.awt.BasicStroke(borderWidth));
            g2.drawRoundRect(0, 0, width - 1, height - 1, round, round);

            if (isFocusOwner()) {
                g2.setColor(lineColor);
                g2.setStroke(new java.awt.BasicStroke(borderWidth + 1));
                g2.drawRoundRect(1, 1, width - 3, height - 3, round - 1, round - 1);
                g2.setStroke(new java.awt.BasicStroke(borderWidth));
            }

            super.paint(grphcs, jc);

            g2.dispose();
        }

        private class ArrowButton extends JButton {

            public ArrowButton() {
                setContentAreaFilled(false);
                setBorder(new EmptyBorder(5, 5, 5, 5));
                setBackground(new Color(150, 150, 150));
                setFocusPainted(false);
            }

            @Override
            public javax.swing.ButtonModel getModel() {
                return new DefaultButtonModel() {
                    @Override
                    public void setPressed(boolean pressed) {
                        // Mencegah perubahan state pressed
                    }
                };
            }

            @Override
            public void paint(Graphics grphcs) {
                Graphics2D g2 = (Graphics2D) grphcs;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth();
                int height = getHeight();
                int size = 10;
                int x = width - (size * 2);
                int y = (height - size) / 2 + 3;

                int arrowUpOffset = -2;

                int px[] = {x, x + size, x + size / 2};
                int py[] = {y + arrowUpOffset, y + arrowUpOffset, y + size + arrowUpOffset};

                g2.setColor(getBackground());
                g2.fillPolygon(px, py, px.length);
                g2.dispose();
            }
        }
    }
}

//public class Jcomboboxcustom<E> extends JComboBox<E> {
//
//    public String getLabeText() {
//        return labeText;
//    }
//
//    public void setLabeText(String labeText) {
//        this.labeText = labeText;
//    }
//
//    public Color getLineColor() {
//        return lineColor;
//    }
//
//    public void setLineColor(Color lineColor) {
//        this.lineColor = lineColor;
//    }
//
//    private String labeText = "Label";
//    private Color lineColor = new Color(3, 155, 216);
//    private boolean mouseOver;
//
//    @Override
//    public void updateUI() {
//        super.updateUI();
//        installUI();
//    }
//
//    private void installUI() {
//        setUI(new ComboUI(this));
//        setRenderer(new DefaultListCellRenderer() {
//            @Override
//            public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean bln, boolean bln1) {
//                Component com = super.getListCellRendererComponent(jlist, o, i, bln, bln1);
//                setBorder(new EmptyBorder(5, 5, 5, 5));
//                if (bln) {
//                    com.setBackground(new Color(240, 240, 240));
//                }
//                return com;
//            }
//        });
//    }
//
//    public Jcomboboxcustom() {
//        setBackground(Color.WHITE);
//        setBorder(new EmptyBorder(15, 3, 5, 3));
//        installUI();
//    }
//
//    private class ComboUI extends BasicComboBoxUI {
//
//        private final Animator animator;
//        private boolean animateHinText = true;
//        private float location;
//        private boolean show;
//        private Jcomboboxcustom combo;
//
//        public ComboUI(Jcomboboxcustom combo) {
//            this.combo = combo;
//            addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseEntered(MouseEvent me) {
//                    mouseOver = true;
//                    repaint();
//                }
//
//                @Override
//                public void mouseExited(MouseEvent me) {
//                    mouseOver = false;
//                    repaint();
//                }
//            });
//            addFocusListener(new FocusAdapter() {
//                @Override
//                public void focusGained(FocusEvent fe) {
//                    showing(false);
//                }
//
//                @Override
//                public void focusLost(FocusEvent fe) {
//                    showing(true);
//                }
//            });
//            addItemListener(new ItemListener() {
//                @Override
//                public void itemStateChanged(ItemEvent ie) {
//                    if (!isFocusOwner()) {
//                        if (getSelectedIndex() == -1) {
//                            showing(true);
//                        } else {
//                            showing(false);
//                        }
//                    }
//                }
//            });
//            addPopupMenuListener(new PopupMenuListener() {
//                @Override
//                public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
//                    if (arrowButton != null) {
//                        arrowButton.setBackground(new Color(200, 200, 200));
//                    }
//                }
//
//                @Override
//                public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
//                    if (arrowButton != null) {
//                        arrowButton.setBackground(new Color(150, 150, 150));
//                    }
//                }
//
//                @Override
//                public void popupMenuCanceled(PopupMenuEvent pme) {
//                    if (arrowButton != null) {
//                        arrowButton.setBackground(new Color(150, 150, 150));
//                    }
//                }
//            });
//            TimingTarget target = new TimingTargetAdapter() {
//                @Override
//                public void begin() {
//                    animateHinText = getSelectedIndex() == -1;
//                }
//
//                @Override
//                public void timingEvent(float fraction) {
//                    location = fraction;
//                    repaint();
//                }
//
//            };
//            animator = new Animator(300, target);
//            animator.setResolution(0);
//            animator.setAcceleration(0.5f);
//            animator.setDeceleration(0.5f);
//        }
//
//        @Override
//        public void paintCurrentValueBackground(Graphics grphcs, Rectangle rctngl, boolean bln) {
//
//        }
//
//        @Override
//        protected JButton createArrowButton() {
//            return new ArrowButton();
//        }
//
//        @Override
//        protected ComboPopup createPopup() {
//            BasicComboPopup pop = new BasicComboPopup(comboBox) {
//                @Override
//                protected JScrollPane createScroller() {
//                    list.setFixedCellHeight(30);
//                    JScrollPane scroll = new JScrollPane(list);
//                    scroll.setBackground(Color.WHITE);
//                    scrollBarCustom sb = new scrollBarCustom();
//                    sb.setUnitIncrement(30);
//                    sb.setForeground(new Color(180, 180, 180));
//                    scroll.setVerticalScrollBar(sb);
//                    return scroll;
//                }
//            };
//            pop.setBorder(new LineBorder(new Color(200, 200, 200), 1));
//            return pop;
//        }
//
//        @Override
//        public void paint(Graphics grphcs, JComponent jc) {
//            super.paint(grphcs, jc);
//            Graphics2D g2 = (Graphics2D) grphcs;
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
//            int width = getWidth();
//            int height = getHeight();
//            if (mouseOver) {
//                g2.setColor(lineColor);
//            } else {
//                g2.setColor(new Color(150, 150, 150));
//            }
//            g2.fillRect(2, height - 1, width - 4, 1);
//            createHintText(g2);
//            createLineStyle(g2);
//            g2.dispose();
//        }
//
//        private void createHintText(Graphics2D g2) {
//            Insets in = getInsets();
//            g2.setColor(new Color(150, 150, 150));
//            FontMetrics ft = g2.getFontMetrics();
//            Rectangle2D r2 = ft.getStringBounds(combo.getLabeText(), g2);
//            double height = getHeight() - in.top - in.bottom;
//            double textY = (height - r2.getHeight()) / 2;
//            double size;
//            if (animateHinText) {
//                if (show) {
//                    size = 18 * (1 - location);
//                } else {
//                    size = 18 * location;
//                }
//            } else {
//                size = 18;
//            }
//            g2.drawString(combo.getLabeText(), in.right, (int) (in.top + textY + ft.getAscent() - size));
//        }
//
//        private void createLineStyle(Graphics2D g2) {
//            if (isFocusOwner()) {
//                double width = getWidth() - 4;
//                int height = getHeight();
//                g2.setColor(lineColor);
//                double size;
//                if (show) {
//                    size = width * (1 - location);
//                } else {
//                    size = width * location;
//                }
//                double x = (width - size) / 2;
//                g2.fillRect((int) (x + 2), height - 2, (int) size, 2);
//            }
//        }
//
//        private void showing(boolean action) {
//            if (animator.isRunning()) {
//                animator.stop();
//            } else {
//                location = 1;
//            }
//            animator.setStartFraction(1f - location);
//            show = action;
//            location = 1f - location;
//            animator.start();
//        }
//
//        private class ArrowButton extends JButton {
//
//            public ArrowButton() {
//                setContentAreaFilled(false);
//                setBorder(new EmptyBorder(5, 5, 5, 5));
//                setBackground(new Color(150, 150, 150));
//            }
//
//            @Override
//            public void paint(Graphics grphcs) {
//                super.paint(grphcs);
//                Graphics2D g2 = (Graphics2D) grphcs;
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                int width = getWidth();
//                int height = getHeight();
//                int size = 10;
//                int x = (width - size) / 2;
//                int y = (height - size) / 2 + 5;
//                int px[] = {x, x + size, x + size / 2};
//                int py[] = {y, y, y + size};
//                g2.setColor(getBackground());
//                g2.fillPolygon(px, py, px.length);
//                g2.dispose();
//            }
//        }
//    }
//}
