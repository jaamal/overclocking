package storage;

public interface IArrayItemsWriter<T>
{
    public void add(T item);
    
    public void addAll(T[] items);

    public void done();
}
