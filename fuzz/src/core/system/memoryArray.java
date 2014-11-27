package core.system;

import java.lang.reflect.Array;

public class memoryArray<T> {
    private T[] m_Array;

    public memoryArray(Class<T[]> type, int size) {
        m_Array = (T[]) Array.newInstance(type.getComponentType(), size);
    }

    public T[] GetArray() {
        return m_Array;
    }
}
