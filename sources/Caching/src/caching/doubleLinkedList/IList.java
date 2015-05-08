package caching.doubleLinkedList;

public interface IList
{
    void prepend(int key);
    void moveToBegin(int key);
    int getLastKey();
}