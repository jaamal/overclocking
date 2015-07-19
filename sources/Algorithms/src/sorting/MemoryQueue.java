package sorting;

import java.util.ArrayList;

public class MemoryQueue<T> implements IQueue<T>
{
    private final ArrayList<T> arrayList;
    private final int capacity;
    private int head, tail, count;

    public MemoryQueue(int capacity)
    {
        this.capacity = capacity;
        arrayList = new ArrayList<T>(capacity + 1);
        for (int i = 0; i <= capacity; ++i)
            arrayList.add(null);
        head = tail = count = 0;
    }

    @Override
    public void push(T obj)
    {
        if (count == capacity)
            throw new RuntimeException("Queue is full!");
        arrayList.set(tail, obj);
        ++count;
        tail = (tail + 1) % arrayList.size();
    }

    @Override
    public T pop()
    {
        T obj = front();
        --count;
        head = (head + 1) % arrayList.size();
        return obj;
    }

    @Override
    public T front()
    {
        return arrayList.get(head);
    }

    @Override
    public long count()
    {
        return count;
    }

    @Override
    public void copyFrom(IQueue<T> source, long count)
    {
        for (int i = 0; i < count; ++i)
            push(source.pop());
    }
}
