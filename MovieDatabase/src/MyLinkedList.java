
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<T extends Comparable<T>> implements ListInterface<T> {
	// dummy head
	Node<T> head;
	int numItems;

	public MyLinkedList() {
		head = new Node<T>(null);
	}

    /**
     * {@code Iterable<T>}를 구현하여 iterator() 메소드를 제공하는 클래스의 인스턴스는
     * 다음과 같은 자바 for-each 문법의 혜택을 볼 수 있다.
     * 
     * <pre>
     *  for (T item: iterable) {
     *  	item.someMethod();
     *  }
     * </pre>
     * 
     * @see PrintCmd#apply(MovieDB)
     * @see SearchCmd#apply(MovieDB)
     * @see java.lang.Iterable#iterator()
     */
    public final Iterator<T> iterator() {
    	return new MyLinkedListIterator<T>(this);
    }

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public T first() {
		return head.getNext().getItem();
	}

	
	//insert item in sorted position. We can keep the list sorted easily.
	//this is possible because we used T extended Comparable<T>
	public void insertSorted(T item) {
		Node<T> last = head;
		Node<T> prev = head;
		if( numItems == 0 ){
			last.insertNext(item);
			numItems += 1;
			return;
		}
		
		last = last.getNext();
		//go to proper position. new item will be added between prev and next.
		while ( last.getItem().compareTo( item ) < 0 ) {
			prev = last;
			last = last.getNext();
			if( last == null ) break;
		}
		prev.insertNext(item);
		numItems += 1;
	}
	
	//Simply add new item to end of list
	@Override
	public void add(T item){
		Node<T> last = head;
		while (last.getNext() != null) {
			last = last.getNext();
		}
		last.insertNext(item);
		numItems += 1;
	}
	
	
	@Override
	public void removeAll() {
		head.setNext(null);
	}
	
	
	//iterate every item in this, return the item if found
	//return null if not found
	public T find( T item ){
		for(T iter : this){ 
			if( iter.equals(item)) return iter;
		}
		return null;
	}
	
	//delete the given item.
	//if not found, do nothing
	public void delete( T item ){
		Node<T> last = head;
		Node<T> prev = head;
		if( numItems == 0 ) return;
		last = last.getNext();
		while( last != null && !last.getItem().equals(item)  ){
			prev = last;
			last = last.getNext();
		}
		
		if( last == null ) return;
		
		prev.removeNext();
		numItems -= 1;
	}
	
}

class MyLinkedListIterator<T extends Comparable<T>> implements Iterator<T> {
	private MyLinkedList<T> list;
	private Node<T> curr;
	private Node<T> prev;

	public MyLinkedListIterator(MyLinkedList<T> list) {
		this.list = list;
		this.curr = list.head;
		this.prev = null;
	}

	@Override
	public boolean hasNext() {
		return curr.getNext() != null;
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		prev = curr;
		curr = curr.getNext();

		return curr.getItem();
	}

	@Override
	public void remove() {
		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();
		prev.removeNext();
		list.numItems -= 1;
		curr = prev;
		prev = null;
	}
}