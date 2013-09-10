package mainUI;

import javax.swing.table.AbstractTableModel;

public class PassengerTableModel extends AbstractTableModel {

	private String[] columnNames = {"Passenger","Floor","Dest.","Status"};
    private Object[][] data;
    private int dataCount;
    
    public PassengerTableModel() {
    	dataCount = 0;
    }
    
    public PassengerTableModel(int c) {
    	dataCount = c;
    	data = new Object[c][4];
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return dataCount;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

}
