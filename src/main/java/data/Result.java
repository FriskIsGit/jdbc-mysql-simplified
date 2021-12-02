package data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

//simplified Wrapper
class Result {
    protected ResultSet set;
    protected ResultSetMetaData data;
    final int DOUBLE_PRECISION = 5;
    Result(ResultSet set) throws SQLException{
        this.set = set;
        this.data = set.getMetaData();
    }
    public int columns() throws SQLException{
        return data.getColumnCount();
    }
    public int rows() throws SQLException{
        int rows = 0;
        if(set.getType() != ResultSet.TYPE_FORWARD_ONLY){
            while(this.next()){
                rows++;
            }
        }else{
            return -1;
        }
        this.beforeFirst();
        return rows;
    }
    public void printSet(int baseWidth) throws SQLException{
        int columns = columns();
        int [] types = new int[columns+1];
        for(int i = 1; i<=columns; i++){
            types[i] = data.getColumnType(i);
        }

        while(this.next()){
            StringBuilder row = new StringBuilder();
            for(int i = 1; i<=columns; i++){
                switch (types[i]){
                    //varchar(string)
                    case 12:
                    default:
                        String dataStr = this.getString(i);
                        if(dataStr == null) {
                            appendWidth(row,baseWidth-4);
                            row.append("NULL");
                            break;
                        }
                        appendWidth(row,baseWidth-dataStr.length());
                        row.append(dataStr);
                        break;
                    case 4:
                    //int
                        int dataInt = this.getInt(i);
                        appendWidth(row,baseWidth-getIntLength(dataInt));
                        row.append(dataInt);
                        break;
                    //bit(boolean)
                    case -7:
                        boolean dataBoolean = this.getBoolean(i);
                        appendWidth(row,baseWidth-getBooleanLength(dataBoolean));
                        row.append(dataBoolean);
                        break;
                    //double
                    case 8:
                        double dataDouble = this.getDouble(i);
                        appendWidth(row,baseWidth-getIntLength((int)dataDouble));
                        row.append(dataDouble);
                        appendWidth(row,DOUBLE_PRECISION-decimalPointLength(dataDouble));
                        break;
                }
            }
            row.append("\n");
            System.out.print(row);
        }
        this.beforeFirst();
    }

    public void first() throws SQLException{
        this.set.first();
    }
    public void beforeFirst() throws SQLException{
        this.set.beforeFirst();
    }

    private void appendWidth(StringBuilder row, int spaces){
        for(int i = 0; i<spaces; i++){
            row.append(' ');
        }
    }
    private int getBooleanLength(boolean flag){
        return flag ? 4 : 5;
    }
    private int getIntLength(int num){
        if(num == 0) return 1;
        int length = 0;
        while(num != 0){
            num/=10;
            length++;
        }
        return length;
    }
    private int decimalPointLength(double n){
        //decimal point issue
        String doubleStr = String.valueOf(n);
        int dotIndex = doubleStr.indexOf('.');
        if(dotIndex==-1) return 1;
        return doubleStr.length() - dotIndex - 1;
    }

    public boolean next() throws SQLException{
        return set.next();
    }

    public String getString(int colIndex) throws SQLException{
        return set.getString(colIndex);
    }

    public int getInt(int colIndex) throws SQLException{
        return set.getInt(colIndex);
    }

    public byte getByte(int colIndex) throws SQLException{
        return set.getByte(colIndex);
    }

    public double getDouble(int colIndex) throws SQLException{
        return set.getDouble(colIndex);
    }

    public boolean getBoolean(int colIndex) throws SQLException{
        return set.getBoolean(colIndex);
    }
}