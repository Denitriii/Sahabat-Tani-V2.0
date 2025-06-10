package custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class Jtablecustom01 extends JTable {

    private int selectedRow = -1;
    private RoundedTableHeader roundedTableHeader;
    private int headerRoundRadius = 12;

    public Jtablecustom01() {
        super();
        init();
    }

    public Jtablecustom01(TableModel dm) {
        super(dm);
        init();
    }

    public Jtablecustom01(TableModel dm, javax.swing.table.TableColumnModel cm) {
        super(dm, cm);
        init();
    }

    public Jtablecustom01(TableModel dm, javax.swing.table.TableColumnModel cm, javax.swing.ListSelectionModel sm) {
        super(dm, cm, sm);
        init();
    }

    private void init() {
        setShowHorizontalLines(false);
        setShowVerticalLines(false);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        setBorder(null);
//        setBorder(new EmptyBorder(0, 0, 0, 0));
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));


        roundedTableHeader = new RoundedTableHeader(this);
        roundedTableHeader.setRoundRadius(15);
        setTableHeader(roundedTableHeader);

        roundedTableHeader.setBackground(new Color(241, 241, 241));

        
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean blnl, int i, int column) {
                Jtableheadercustom header = new Jtableheadercustom(o + "");
                header.setHorizontalAlignment(JLabel.CENTER);
                return header;
            }
        });
        getTableHeader().setReorderingAllowed(false);
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean blnl, int row, int column) {
                Component com = super.getTableCellRendererComponent(jtable, o, selected, blnl, row, column);
                com.setBackground(Color.WHITE);
                setBorder(noFocusBorder);
                if (selected) {
                    com.setForeground(new Color(15, 89, 140));
                    com.setForeground(new Color(204, 204, 204));
                } else {
                    com.setForeground(new Color(102, 102, 102));
                }
                ((JLabel) com).setHorizontalAlignment(SwingConstants.CENTER);
                return com;
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = getSelectedRow();
                if (row == selectedRow) {
                    clearSelection();
                    selectedRow = -1;
                } else {
                    selectedRow = row;
                }
            }
        });
    }

    public int getHeaderRoundRadius() {
        return roundedTableHeader.getRoundRadius();
    }

    public void setHeaderRoundRadius(int roundRadius) {
        this.headerRoundRadius = Math.max(10, roundRadius);
        roundedTableHeader.setRoundRadius(this.headerRoundRadius);
        repaint();
    }

    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }

    private class RoundedTableHeader extends JTableHeader {

        public RoundedTableHeader(JTable table) {
            super(table.getColumnModel());
            setOpaque(false);
        }

        public int getRoundRadius() {
            return headerRoundRadius;
        }

        public void setRoundRadius(int roundRadius) {
            headerRoundRadius = Math.max(0, roundRadius);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight(), r = headerRoundRadius;
            // Buat path dengan arc hanya di top‑left & top‑right
            java.awt.geom.Path2D path = new java.awt.geom.Path2D.Double();
            path.moveTo(0, r);
            path.quadTo(0, 0, r, 0);            // sudut kiri atas
            path.lineTo(w - r, 0);
            path.quadTo(w, 0, w, r);            // sudut kanan atas
            path.lineTo(w, h);
            path.lineTo(0, h);
            path.closePath();

            // Gambar background dengan path
            g2.setColor(getBackground());
            g2.fill(path);

            super.paintComponent(g2);

            g2.dispose();
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            repaint();
        }
    }

    private class Jtableheadercustom extends JLabel {

        public Jtableheadercustom(String text) {
            super(text);
            setOpaque(false);
//            setBackground(new Color(241, 241, 241));
            setFont(new Font("tahoma", Font.BOLD, 14));
            setForeground(new Color(95, 94, 94));
            setBorder(new EmptyBorder(10, 5, 10, 5));
            setHorizontalAlignment(CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
}
