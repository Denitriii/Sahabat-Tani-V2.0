/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponen;

/**
 *
 * @author user
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.List;

public class TableRowTransferHandler extends TransferHandler {
    private final JTable table;
    private int[] selectedRows;

    private final DataFlavor localObjectFlavor;

    public TableRowTransferHandler(JTable table) {
        this.table = table;
        localObjectFlavor = new DataFlavor(Object[][].class, "Array of rows");
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        selectedRows = table.getSelectedRows();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object[][] data = new Object[selectedRows.length][model.getColumnCount()];

        for (int i = 0; i < selectedRows.length; i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                data[i][j] = model.getValueAt(selectedRows[i], j);
            }
        }

        return new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{localObjectFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return localObjectFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
                return data;
            }
        };
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        return info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        if (!canImport(info)) return false;

        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int row = dl.getRow();

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        try {
            Object[][] data = (Object[][]) info.getTransferable().getTransferData(localObjectFlavor);

            for (Object[] rowData : data) {
                model.insertRow(row++, rowData);
            }

            return true;
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if (action == MOVE && selectedRows != null) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                model.removeRow(selectedRows[i]);
            }
        }
        selectedRows = null;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }
}

