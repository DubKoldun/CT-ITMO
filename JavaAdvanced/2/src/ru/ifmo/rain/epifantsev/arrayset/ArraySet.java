package ru.ifmo.rain.epifantsev.arrayset;

import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {
    private final List<E> array;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this(Collections.emptyList(), null);
    }

    public ArraySet(Collection<? extends E> array) {
        this(array, null);
    }

    public ArraySet(Collection<? extends E> array, Comparator<? super E> comparator) {
        var treeSetWrapper = new TreeSet<E>(comparator);
        treeSetWrapper.addAll(array);
        this.array = new ArrayList<>(treeSetWrapper);
        this.comparator = comparator;
    }

    private ArraySet(List<E> array, Comparator<? super E> comparator) {
        this.array = array;
        this.comparator = comparator;
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    private SortedSet<E> subSet(E fromElement, E toElement, boolean inclusive) {
        var from = Collections.binarySearch(array, Objects.requireNonNull(fromElement), comparator);
        var to = Collections.binarySearch(array, Objects.requireNonNull(toElement), comparator);
        if (from < 0) from = -from - 1;
        if (to < 0) to = -to - 1;
        if (inclusive) ++to;
        if (from > to)
            throw new IllegalArgumentException("The left border should be less or equal than right");
        return new ArraySet<>(array.subList(from, to), comparator);
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        if (isEmpty()) return new ArraySet<>(Collections.emptyList(), comparator);
        return subSet(fromElement, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        if (isEmpty()) return new ArraySet<>(Collections.emptyList(), comparator);
        return subSet(first(), toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        if (isEmpty()) return new ArraySet<>(Collections.emptyList(), comparator);
        return subSet(fromElement, last(), true);
    }

    @Override
    public E first() {
        if (isEmpty()) throw new NoSuchElementException();
        return array.get(0);
    }

    @Override
    public E last() {
        if (isEmpty()) throw new NoSuchElementException();
        return array.get(size() - 1);
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        return Collections.binarySearch(array, (E) Objects.requireNonNull(o), comparator) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return Collections.unmodifiableList(array).iterator();
    }
}
