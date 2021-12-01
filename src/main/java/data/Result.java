package data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLType;

class Result {
    protected final ResultSet set;
    protected final ResultSetMetaData data;
    protected Result(ResultSet set) throws SQLException{
        this.set = set;
        this.data = set.getMetaData();

    }
    public int columns() throws SQLException{
        return data.getColumnCount();
    }
    public int rows() throws SQLException{
        return -1;
    }
    protected void printSet(int baseWidth) throws SQLException{
        int columns = columns();
        int [] types = new int[columns+1];
        for(int i = 1; i<=columns; i++){
            types[i] = data.getColumnType(i);
        }
        Result setCopy = new Result(this.set);
        System.out.println();
        while(setCopy.next()){
            StringBuilder row = new StringBuilder();
            for(int i = 1; i<=columns; i++){
                switch (types[i]){
                    //varchar(string)
                    case 12:
                    default:
                        String dataStr = setCopy.getString(i);
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
                        int dataInt = setCopy.getInt(i);
                        appendWidth(row,baseWidth-getIntLength(dataInt));
                        row.append(dataInt);
                        break;
                    //bit(boolean)
                    case -7:
                        boolean dataBoolean = setCopy.getBoolean(i);
                        appendWidth(row,baseWidth-getBooleanLength(dataBoolean));
                        row.append(dataBoolean);
                        break;
                    //double
                    case 8:
                        double dataDouble = setCopy.getDouble(i);
                        appendWidth(row,baseWidth-getDoubleLength(dataDouble));
                        row.append(dataDouble);
                        break;
                }
            }
            row.append("\n");
            System.out.print(row);
        }

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
    private int getDoubleLength(double n){
        return getIntLength((int)n);
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