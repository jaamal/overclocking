package caching.doubleLinkedList;

public interface IList
{
	int size();

	// TODO may be prepend
	void appendToBegin(int key);

	void moveToBegin(int key);

	int getLastKey();

	void delete(int key);
}

// TODO
// public interface IList {
// void append(Object obj);
// Iterator getIterator();
// }
