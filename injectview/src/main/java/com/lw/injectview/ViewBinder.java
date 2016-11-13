package com.lw.injectview;

/**
 * Created by lw on 2016/11/13.
 */

public interface ViewBinder<T> {
    void bind(T target);
}