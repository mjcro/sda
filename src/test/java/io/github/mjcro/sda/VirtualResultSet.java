package io.github.mjcro.sda;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class VirtualResultSet implements ResultSet {
    private final String[] columns;
    private final Object[] data;
    private boolean wasNull = false;

    public VirtualResultSet(String[] columns, Object[] data) {
        this.columns = columns;
        this.data = data;
    }

    private Object read(int columnIndex) {
        this.wasNull = data[columnIndex - 1] == null;
        return data[columnIndex - 1];
    }

    private Object read(String columnLabel) {
        for (int i = 0; i < columns.length; i++) {
            if (columnLabel.equals(columns[i])) {
                return read(i + 1);
            }
        }
        this.wasNull = true;
        return null;
    }

    @Override
    public boolean wasNull() {
        return this.wasNull;
    }

    @Override
    public Object getObject(int columnIndex) {
        return read(columnIndex);
    }

    @Override
    public Object getObject(String columnLabel) {
        return read(columnLabel);
    }

    @Override
    public String getString(int columnIndex) {
        return (String) read(columnIndex);
    }

    @Override
    public String getString(String columnLabel) {
        return (String) read(columnLabel);
    }

    @Override
    public boolean getBoolean(int columnIndex) {
        Boolean b = (Boolean) read(columnIndex);
        return b == null ? false : b;
    }

    @Override
    public boolean getBoolean(String columnLabel) {
        Boolean b = (Boolean) read(columnLabel);
        return b == null ? false : b;
    }

    @Override
    public byte getByte(int columnIndex) {
        Byte b = (Byte) read(columnIndex);
        return b == null ? 0 : b;
    }

    @Override
    public byte getByte(String columnLabel) {
        Byte b = (Byte) read(columnLabel);
        return b == null ? 0 : b;
    }

    @Override
    public byte[] getBytes(int columnIndex) {
        return (byte[]) read(columnIndex);
    }

    @Override
    public byte[] getBytes(String columnLabel) {
        return (byte[]) read(columnLabel);
    }

    @Override
    public short getShort(int columnIndex) {
        Short s = (Short) read(columnIndex);
        return s == null ? 0 : s;
    }

    @Override
    public short getShort(String columnLabel) {
        Short s = (Short) read(columnLabel);
        return s == null ? 0 : s;
    }

    @Override
    public int getInt(int columnIndex) {
        Integer i = (Integer) read(columnIndex);
        return i == null ? 0 : i;
    }

    @Override
    public int getInt(String columnLabel) {
        Integer i = (Integer) read(columnLabel);
        return i == null ? 0 : i;
    }

    @Override
    public long getLong(int columnIndex) {
        Long l = (Long) read(columnIndex);
        return l == null ? 0 : l;
    }

    @Override
    public long getLong(String columnLabel) {
        Long l = (Long) read(columnLabel);
        return l == null ? 0 : l;
    }

    @Override
    public float getFloat(int columnIndex) {
        Float f = (Float) read(columnIndex);
        return f == null ? 0 : f;
    }

    @Override
    public float getFloat(String columnLabel) {
        Float f = (Float) read(columnLabel);
        return f == null ? 0 : f;
    }

    @Override
    public double getDouble(int columnIndex) {
        Double d = (Double) read(columnIndex);
        return d == null ? 0 : d;
    }

    @Override
    public double getDouble(String columnLabel) {
        Double d = (Double) read(columnLabel);
        return d == null ? 0 : d;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) {
        return (BigDecimal) read(columnIndex);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) {
        return (BigDecimal) read(columnLabel);
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) {
        throw new AssertionError("not implemented");
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(String columnLabel, int scale) {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean next() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void close() {
        throw new AssertionError("not implemented");
    }

    @Override
    public Date getDate(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Time getTime(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Date getDate(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Time getTime(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public SQLWarning getWarnings() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void clearWarnings() {

    }

    @Override
    public String getCursorName() {
        throw new AssertionError("not implemented");
    }

    @Override
    public ResultSetMetaData getMetaData() {
        throw new AssertionError("not implemented");
    }


    @Override
    public int findColumn(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Reader getCharacterStream(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Reader getCharacterStream(String columnLabel) {
        throw new AssertionError("not implemented");
    }


    @Override
    public boolean isBeforeFirst() {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean isAfterLast() {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean isFirst() {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean isLast() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void beforeFirst() {

    }

    @Override
    public void afterLast() {

    }

    @Override
    public boolean first() {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean last() {
        throw new AssertionError("not implemented");
    }

    @Override
    public int getRow() {
        return 0;
    }

    @Override
    public boolean absolute(int row) {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean relative(int rows) {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean previous() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void setFetchDirection(int direction) {
        throw new AssertionError("not implemented");
    }

    @Override
    public int getFetchDirection() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void setFetchSize(int rows) {
        throw new AssertionError("not implemented");
    }

    @Override
    public int getFetchSize() {
        throw new AssertionError("not implemented");
    }

    @Override
    public int getType() {
        throw new AssertionError("not implemented");
    }

    @Override
    public int getConcurrency() {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean rowUpdated() {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean rowInserted() {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean rowDeleted() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNull(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateByte(int columnIndex, byte x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateShort(int columnIndex, short x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateInt(int columnIndex, int x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateLong(int columnIndex, long x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateFloat(int columnIndex, float x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateDouble(int columnIndex, double x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateString(int columnIndex, String x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateDate(int columnIndex, Date x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateTime(int columnIndex, Time x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateObject(int columnIndex, Object x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNull(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateByte(String columnLabel, byte x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateShort(String columnLabel, short x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateInt(String columnLabel, int x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateLong(String columnLabel, long x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateFloat(String columnLabel, float x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateDouble(String columnLabel, double x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateString(String columnLabel, String x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateDate(String columnLabel, Date x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateTime(String columnLabel, Time x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateObject(String columnLabel, Object x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void insertRow() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateRow() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void deleteRow() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void refreshRow() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void cancelRowUpdates() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void moveToInsertRow() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void moveToCurrentRow() {
        throw new AssertionError("not implemented");
    }

    @Override
    public Statement getStatement() {
        throw new AssertionError("not implemented");
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Ref getRef(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Blob getBlob(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Clob getClob(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Array getArray(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Ref getRef(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Blob getBlob(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Clob getClob(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Array getArray(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) {
        throw new AssertionError("not implemented");
    }

    @Override
    public URL getURL(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public URL getURL(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateRef(int columnIndex, Ref x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateRef(String columnLabel, Ref x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateClob(int columnIndex, Clob x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateClob(String columnLabel, Clob x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateArray(int columnIndex, Array x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateArray(String columnLabel, Array x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public RowId getRowId(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public RowId getRowId(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public int getHoldability() {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean isClosed() {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNString(int columnIndex, String nString) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNString(String columnLabel, String nString) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) {
        throw new AssertionError("not implemented");
    }

    @Override
    public NClob getNClob(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public NClob getNClob(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) {
        throw new AssertionError("not implemented");
    }

    @Override
    public String getNString(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public String getNString(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) {
        throw new AssertionError("not implemented");
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) {
        throw new AssertionError("not implemented");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) {
        throw new AssertionError("not implemented");
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) {
        throw new AssertionError("not implemented");
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) {
        throw new AssertionError("not implemented");
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        throw new AssertionError("not implemented");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        throw new AssertionError("not implemented");
    }
}
