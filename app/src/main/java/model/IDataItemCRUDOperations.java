package model;

import java.util.List;

public interface IDataItemCRUDOperations {

    public DataItem createDataItem(DataItem item);

    public List<DataItem> readAllDataItems();

    public DataItem readDataItem(long id);

    public boolean updateDataItem(DataItem item);

    public boolean deleteDataItem(long id);
}
