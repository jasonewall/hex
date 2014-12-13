/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Jason E. Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hex.routing;

import hex.utils.CoercionMap;
import hex.utils.maps.AbstractImmutableMap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jason on 14-11-13.
 */
public class RouteParams extends AbstractImmutableMap<String,Object> implements CoercionMap {
    private Map<String,String> params = new HashMap<>();

    public RouteParams(Map<String,String> params) {
        this.params = params;
    }

    /**
     * Build the {@code entrySet} that will be converted into an immutable implementation of the {@link java.util.Set}
     * interface.
     * <p>
     * This pattern is meant to be a convenience for creating implementations of an immutable {@link java.util.Map}
     * implementation.
     *
     * @return A {@link java.util.Set} of entries
     */
    @Override
    protected Set<Entry<String, Object>> buildEntries() {
        // The type erasure doesn't like auto converting Entry<String,String> to <String,Object>
        // Using just SimpleImmutableEntry::new assumes we're returning Entry<String,String> so it barfs on the return type
        // of #buildEntries() - go weak generics
        //noinspection Convert2MethodRef,Convert2Diamond
        return params.entrySet().stream()
                .map(e -> new SimpleImmutableEntry<String,Object>(e))
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    public Object get(Class<?> type, String name) throws IllegalArgumentException {
        if(type == int.class || type == Integer.class) {
            return getInt(name);
        } else {
            return getString(name);
        }
    }

    public int getInt(String paramName) throws IllegalArgumentException {
        try {
            return Integer.parseInt(params.get(paramName), 10);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("NumberFormatException in route parameter: %s", e.getMessage()), e);
        }
    }
}
