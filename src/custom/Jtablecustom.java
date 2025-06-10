package Custom;

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

public class Jtablecustom extends JTable {

    private int selectedRow = -1;
    private RoundedTableHeader roundedTableHeader;
    private int headerRoundRadius = 12;

    public Jtablecustom() {
        super();
        init();
    }

    public Jtablecustom(TableModel dm) {
        super(dm);
        init();
    }

    public Jtablecustom(TableModel dm, javax.swing.table.TableColumnModel cm) {
        super(dm, cm);
        init();
    }

    public Jtablecustom(TableModel dm, javax.swing.table.TableColumnModel cm, javax.swing.ListSelectionModel sm) {
        super(dm, cm, sm);
        init();
    }

    private void init() {
        setShowHorizontalLines(false);
        setShowVerticalLines(false);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));

        roundedTableHeader = new RoundedTableHeader(this);
        setTableHeader(roundedTableHeader);
        roundedTableHeader.setBackground(new Color(241, 241, 241));

        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean blnl, int i, int column) {
                Jtableheadercustom header = new Jtableheadercustom(o + "");
                header.setHorizontalAlignment(JLabel.CENTER);
                return header;
            }
        });

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
        this.headerRoundRadius = Math.max(0, roundRadius);
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

            int width = getWidth();
            int height = getHeight();

            g2.setColor(getBackground());
            RoundRectangle2D roundedRect = new RoundRectangle2D.Double(
                    0, 0, width, height, headerRoundRadius, headerRoundRadius);
            g2.fill(roundedRect);

            super.paintComponent(g);

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
            setOpaque(true);
            setBackground(new Color(241, 241, 241));
            setFont(new Font("Inter", 1, 12));
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

//package Custom;
//
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import javax.swing.JLabel;
//import javax.swing.JTable;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//
//public class Jtablecustom extends JTable {
//
//    private int selectedRow = -1;
//    private int headerRoundRadius = 10; // Radius untuk sudut header
//
//    public Jtablecustom() {
//        setShowHorizontalLines(false);
//        setShowVerticalLines(false);
//        setGridColor(new Color(230, 230, 230));
//        setRowHeight(40);
//
//        // Membuat header tidak bisa di-drag
//        getTableHeader().setReorderingAllowed(false);
//        getTableHeader().setDefaultRenderer(new RoundedTableHeaderRenderer());
//
//        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean blnl, int row, int column) {
//                Component com = super.getTableCellRendererComponent(jtable, o, selected, blnl, row, column);
//                com.setBackground(Color.WHITE);
//                setBorder(new EmptyBorder(10, 5, 10, 5)); // Tambahkan padding ke isi sel
//                if (selected) {
//                    com.setForeground(new Color(15, 89, 140));
//                } else {
//                    com.setForeground(new Color(95, 94, 94));
//                }
//                return com;
//            }
//        });
//
//        addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                int row = getSelectedRow();
//                if (row == selectedRow) {
//                    clearSelection();
//                    selectedRow = -1;
//                } else {
//                    selectedRow = row;
//                }
//            }
//        });
//    }
//
//    public void addRow(Object[] row) {
//        DefaultTableModel model = (DefaultTableModel) getModel();
//        model.addRow(row);
//    }
//
//    public int getHeaderRoundRadius() {
//        return headerRoundRadius;
//    }
//
//    public void setHeaderRoundRadius(int headerRoundRadius) {
//        this.headerRoundRadius = Math.max(0, headerRoundRadius);
//        getTableHeader().repaint(); // Meminta header untuk digambar ulang
//    }
//
//    private class RoundedTableHeaderRenderer extends DefaultTableCellRenderer {
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            Jtableheadercustom headerLabel = new Jtableheadercustom(value + "");
//            return headerLabel;
//        }
//
//        @Override
//        public void paintComponent(Graphics g) {
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//            int width = getWidth();
//            int height = getHeight();
//
//            // Menggambar background rounded
//            g2.setColor(new Color(250, 250, 250)); // Warna background header
//            g2.fillRoundRect(0, 0, width, height, headerRoundRadius, headerRoundRadius);
//
//            // Menggambar teks header (delegasikan ke superclass)
//            super.paintComponent(g);
//
//            g2.dispose();
//        }
//    }
//
//    private class Jtableheadercustom extends JLabel {
//
//        public Jtableheadercustom(String text) {
//            super(text);
//            setOpaque(false); // Penting agar background rounded terlihat
//            setBackground(new Color(250, 250, 250));
//            setFont(new Font("Inter", 1, 12));
//            setForeground(new Color(95, 94, 94));
//            setBorder(new EmptyBorder(10, 5, 10, 5));
//            setHorizontalAlignment(CENTER); // Agar teks berada di tengah header
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            // Tidak perlu menggambar garis bawah di sini karena background header sudah kita custom
//        }
//    }
//}
//
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.FlowLayout;
//import javax.swing.JButton;
//import javax.swing.JPanel;
//import javax.swing.JTable;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//
//public class Jtablecustom extends JTable {
//
//    private DefaultTableModel tableModel;
//
//    public Jtablecustom() {
//        // Inisialisasi model tabel
//        String[] columns = {"Code", "Name", "Stock", "Status", "Unit Price", "Action"};
//        tableModel = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                // Membuat kolom Action tidak bisa diedit langsung
//                return false;
//            }
//        };
//        setModel(tableModel);
//
//        // Styling tabel
//        configureTable();
//    }
//
//    private void configureTable() {
//        // Set warna header
//        getTableHeader().setBackground(new Color(245, 245, 245)); // Lebih terang dari LIGHT_GRAY
//        getTableHeader().setForeground(Color.BLACK);
//
//        // Hilangkan garis vertikal
//        setShowVerticalLines(false);
//        setShowHorizontalLines(false);
//        // Set warna garis horizontal
//        setGridColor(new Color(220, 220, 220));
//
//        // Set ukuran kolom
//        getColumnModel().getColumn(0).setPreferredWidth(100); // Code
//        getColumnModel().getColumn(1).setPreferredWidth(200); // Name
//        getColumnModel().getColumn(2).setPreferredWidth(100); // Stock
//        getColumnModel().getColumn(3).setPreferredWidth(100); // Status
//        getColumnModel().getColumn(4).setPreferredWidth(100); // Unit Price
//        getColumnModel().getColumn(5).setPreferredWidth(100); // Action
//
//        // Set renderer untuk kolom Action
//        getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
//
//        // Set default renderer untuk sel (padding)
//        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
//        defaultRenderer.setBorder(new EmptyBorder(5, 5, 5, 5)); // Tambahkan padding
//        setDefaultRenderer(Object.class, defaultRenderer);
//        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                c.setBackground(new Color(245, 245, 245));
//                c.setForeground(Color.BLACK);
//                setBorder(new EmptyBorder(5, 5, 5, 5));
//                return c;
//            }
//        });
//
//        // Warna seleksi
//        setSelectionBackground(new Color(200, 220, 240));
//        setSelectionForeground(Color.BLACK);
//    }
//
//    // Custom renderer untuk kolom Action
//    private class ActionButtonRenderer extends DefaultTableCellRenderer {
//
//        private JPanel panel;
//        private JButton editButton;
//        private JButton deleteButton;
//
//        public ActionButtonRenderer() {
//            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
//            panel.setOpaque(false); // Make panel transparent
//
//            // Tombol Edit
//            editButton = new JButton("\u270E"); // Unicode for pen
//            editButton.setBackground(new Color(50, 150, 50));
//            editButton.setForeground(Color.WHITE);
//            editButton.setFocusPainted(false);
//            editButton.setBorder(new EmptyBorder(5, 10, 5, 10));
//
//            // Tombol Delete
//            deleteButton = new JButton("\uD83D\uDDD1"); // Unicode for trash can
//            deleteButton.setBackground(new Color(180, 50, 50));
//            deleteButton.setForeground(Color.WHITE);
//            deleteButton.setFocusPainted(false);
//            deleteButton.setBorder(new EmptyBorder(5, 10, 5, 10));
//
//            panel.add(editButton);
//            panel.add(deleteButton);
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value,
//                boolean isSelected, boolean hasFocus, int row, int column) {
//            return panel;
//        }
//    }
//
//    // Method untuk menambah data
//    public void addProduct(String code, String name, int stock, String status, double price) {
//        tableModel.addRow(new Object[]{code, name, stock, status, price, ""});
//    }
//}
//package Custom;
//
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.Dimension;
//import javax.swing.JButton;
//import javax.swing.JPanel;
//import javax.swing.JTable;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.JTableHeader;
//
//public class Jtablecustom extends JTable {
//
//    private DefaultTableModel tableModel;
//    private Color headerBackgroundColor = new Color(245, 245, 245); // Warna default header
//    private Color headerForegroundColor = Color.BLACK; // Warna default teks header
//    private Font headerFont = getTableHeader().getFont(); // Font default header
//    private Dimension headerSize = getTableHeader().getPreferredSize(); // Ukuran default header
//
//    public Jtablecustom() {
//        // Inisialisasi model tabel
//        String[] columns = {"Code", "Name", "Stock", "Status", "Unit Price", "Action"};
//        tableModel = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                // Membuat kolom Action tidak bisa diedit langsung
//                return false;
//            }
//        };
//        setModel(tableModel);
//
//        // Styling tabel
//        configureTable();
//        configureTableHeader(); // Konfigurasi header terpisah
//    }
//
//    // Getter dan Setter untuk properti header
//    public Color getHeaderBackgroundColor() {
//        return headerBackgroundColor;
//    }
//
//    public void setHeaderBackgroundColor(Color headerBackgroundColor) {
//        this.headerBackgroundColor = headerBackgroundColor;
//        configureTableHeaderRenderer(); // Perbarui renderer header
//    }
//
//    public Color getHeaderForegroundColor() {
//        return headerForegroundColor;
//    }
//
//    public void setHeaderForegroundColor(Color headerForegroundColor) {
//        this.headerForegroundColor = headerForegroundColor;
//        configureTableHeaderRenderer(); // Perbarui renderer header
//    }
//
//    public Font getHeaderFont() {
//        return headerFont;
//    }
//
//    public void setHeaderFont(Font headerFont) {
//        this.headerFont = headerFont;
//        configureTableHeaderRenderer(); // Perbarui renderer header
//    }
//
//    public Dimension getHeaderSize() {
//        return new Dimension(headerSize); // Return a copy to prevent modification
//    }
//
//    public void setHeaderSize(Dimension headerSize) {
//        this.headerSize = new Dimension(headerSize);
//        configureTableHeaderSize(); // Perbarui ukuran header
//    }
//
//    private void configureTableHeader() {
//        JTableHeader header = getTableHeader();
//        header.setOpaque(false); // Make header non-opaque to respect background color
//        configureTableHeaderRenderer();
//        configureTableHeaderSize();
//    }
//
//    private void configureTableHeaderRenderer() {
//        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                c.setBackground(headerBackgroundColor);
//                c.setForeground(headerForegroundColor);
//                setFont(headerFont);
//                setBorder(new EmptyBorder(5, 5, 5, 5));
//                return c;
//            }
//        });
//        getTableHeader().repaint(); // Repaint header to show changes
//    }
//
//    private void configureTableHeaderSize() {
//        getTableHeader().setPreferredSize(headerSize);
//        getTableHeader().revalidate(); // Revalidate header layout
//    }
//
//    private void configureTable() {
//        // Hilangkan garis vertikal
//        setShowVerticalLines(false);
//        setShowHorizontalLines(false);
//        // Set warna garis horizontal
//        setGridColor(new Color(220, 220, 220));
//
//        // Set ukuran kolom
//        getColumnModel().getColumn(0).setPreferredWidth(100); // Code
//        getColumnModel().getColumn(1).setPreferredWidth(200); // Name
//        getColumnModel().getColumn(2).setPreferredWidth(100); // Stock
//        getColumnModel().getColumn(3).setPreferredWidth(100); // Status
//        getColumnModel().getColumn(4).setPreferredWidth(100); // Unit Price
//        getColumnModel().getColumn(5).setPreferredWidth(100); // Action
//
//        // Set renderer untuk kolom Action
//        getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
//
//        // Set default renderer untuk sel (padding)
//        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
//        defaultRenderer.setBorder(new EmptyBorder(5, 7, 5, 7)); // Tambahkan padding
//        setDefaultRenderer(Object.class, defaultRenderer);
//
//        // Warna seleksi
//        setSelectionBackground(new Color(200, 220, 240));
//        setSelectionForeground(Color.BLACK);
//    }
//
//    // Custom renderer untuk kolom Action
//    private class ActionButtonRenderer extends DefaultTableCellRenderer {
//
//        private JPanel panel;
//        private JButton editButton;
//        private JButton deleteButton;
//
//        public ActionButtonRenderer() {
//            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
//            panel.setOpaque(false); // Make panel transparent
//
//            // Tombol Edit
//            editButton = new JButton("\u270E"); // Unicode for pen
//            editButton.setBackground(new Color(50, 150, 50));
//            editButton.setForeground(Color.WHITE);
//            editButton.setFocusPainted(false);
//            editButton.setBorder(new EmptyBorder(5, 10, 5, 10));
//
//            // Tombol Delete
//            deleteButton = new JButton("\uD83D\uDDD1"); // Unicode for trash can
//            deleteButton.setBackground(new Color(180, 50, 50));
//            deleteButton.setForeground(Color.WHITE);
//            deleteButton.setFocusPainted(false);
//            deleteButton.setBorder(new EmptyBorder(5, 10, 5, 10));
//
//            panel.add(editButton);
//            panel.add(deleteButton);
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value,
//                boolean isSelected, boolean hasFocus, int row, int column) {
//            return panel;
//        }
//    }
//
//    // Method untuk menambah data
//    public void addProduct(String code, String name, int stock, String status, double price) {
//        tableModel.addRow(new Object[]{code, name, stock, status, price, ""});
//    }
//}
