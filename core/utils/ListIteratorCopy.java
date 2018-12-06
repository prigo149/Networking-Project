package com.websystem.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListIteratorCopy {

    public static <T> List<T> copyList ( Collection<T> copy ) {
        List<T> copied = new ArrayList();
        copy.iterator().forEachRemaining( t -> copied.add( t ) );
        return copied;
    }
}