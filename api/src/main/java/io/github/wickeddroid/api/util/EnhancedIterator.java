package io.github.wickeddroid.api.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EnhancedIterator<E> implements Iterator<E> {
    private int index = -1;
    private final List<E> object;

    public EnhancedIterator(LinkedList<E> object) {
        this.object = object;
    }

    @Override
    public boolean hasNext() {
        return index < object.size()-1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void restart() {
        index = 0;
    }

    public void setPosition(E value) {
        index = object.indexOf(value);
    }

    @Override
    public E next() {
        index++;
        return current();
    }

    public E previous() {
        index--;
        return current();
    }

    public E current() {
        return object.get(index);
    }

    @Override
    public void remove() {
        object.remove(index);
    }

}