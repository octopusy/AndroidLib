package com.axl.android.frameworkbase.view;

import android.view.View;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/10/9
 * Time: 17:39
 */

public interface OnRecyclerViewItemClickListener<T> {
    void onItemClick(View v, T t, int position);
}
