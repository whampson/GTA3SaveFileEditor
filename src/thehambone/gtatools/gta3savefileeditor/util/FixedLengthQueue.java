
package thehambone.gtatools.gta3savefileeditor.util;

import java.util.Iterator;


/**
 * A {@code FixedLengthQueue} represents a first-in-first-out (FIFO) collection
 * of objects whose maximum capacity does not change. The class implements the
 * {@code Iterable} interface so items in the queue can be iterated.
 * <p>
 * Created on Jan 27, 2016.
 *
 * @author thehambone
 * @param <E> the type of each element in the queue
 */
public class FixedLengthQueue<E> implements Iterable<E>
{
    private final E[] queue;
    
    private int front;
    private int rear;
    private int itemCount;
    
    /**
     * Creates an empty {@code Queue} with the specified capacity.
     * 
     * @param capacity the maximum number of elements the queue can hold
     */
    @SuppressWarnings("unchecked") // Safe to cast an empty Object array to E
    public FixedLengthQueue(int capacity)
    {
        if (capacity < 1) {
            throw new IllegalArgumentException(
                    "capacity must be a postive integer");
        }
        
        queue = (E[])new Object[capacity];
        front = 0;
        rear = -1;
        itemCount = 0;
    }
    
    /**
     * Checks whether the queue is empty.
     * 
     * @return {@code true} if the queue is empty, {@code false} otherwise
     */
    public boolean isEmpty()
    {
        return itemCount == 0;
    }
    
    /**
     * Checks whether the maximum capacity of the queue has been reached.
     * 
     * @return {@code true} if the queue is full, {@code false} otherwise
     */
    public boolean isFull()
    {
        return itemCount == queue.length;
    }
    
    /**
     * Returns the current number of items in the queue.
     * 
     * @return the number of items in the queue
     */
    public int getItemCount()
    {
        return itemCount;
    }
    
    /**
     * Gets the item at the front of the queue.
     * 
     * @return the item at the front of the queue
     * @throws QueueException if the queue is empty
     */
    public E peek()
    {
        if (isEmpty()) {
            throw new QueueException("queue is empty");
        }
        
        return queue[front];
    }
    
    /**
     * Adds an item to the rear of the queue.
     * 
     * @param item the item to be added
     * @throws QueueException if the queue is full
     */
    public void insert(E item)
    {
        if (isFull()) {
            throw new QueueException("queue is full");
        }
        
        if (rear == queue.length - 1) {
            rear = -1;
        }
        
        queue[++rear] = item;
        itemCount++;
    }
    
    /**
     * Removes the item at the front of the queue.
     * 
     * @return the item removed
     * @throws QueueException if the queue is empty
     */
    public E remove()
    {
        if (isEmpty()) {
            throw new QueueException("queue is empty");
        }
        
        if (front == queue.length) {
            front = 0;
        }
        itemCount--;
        return queue[front++];
    }
    
    /**
     * Returns an Iterator object that iterates the queue from the last item to
     * the first item.
     * 
     * @return a reverse-order Iterator object
     */
    public Iterator<E> reverseOrderIterator()
    {
        return new Iterator<E>()
        {
            int itemsIterated = 0;
            int index = rear;
            
            @Override
            public boolean hasNext()
            {
                return itemsIterated != itemCount;
            }

            @Override
            public E next()
            {
                if (index == -1) {
                    index = queue.length - 1;
                }
                itemsIterated++;
                return queue[index--];
            }
        };
    }
    
    @Override
    public Iterator<E> iterator()
    {
        return new Iterator<E>()
        {
            int itemsIterated = 0;
            int index = front;
            
            @Override
            public boolean hasNext()
            {
                return itemsIterated != itemCount;
            }

            @Override
            public E next()
            {
                if (index == queue.length) {
                    index = 0;
                }
                itemsIterated++;
                return queue[index++];
            }
        };
    }
}
