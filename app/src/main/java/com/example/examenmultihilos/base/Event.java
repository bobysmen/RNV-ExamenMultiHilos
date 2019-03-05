package com.example.examenmultihilos.base;

@SuppressWarnings("unused")
public class Event<T> {

    private final T content;
    private boolean hasBeenHandled = false;

    public Event(T content) {
        this.content = content;
    }

    public boolean hasBeenHandled() {
        return hasBeenHandled;
    }

    /**
     * Returns the content and prevents its use again.
     */
    public T getContentIfNotHandled() {
        if (hasBeenHandled) return null;
        hasBeenHandled = true;
        return content;
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    public T peekContent() {
        return content;
    }

}
