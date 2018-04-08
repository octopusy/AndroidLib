package com.axl.android.frameworkbase.view;

import android.content.Context;
import android.view.View;

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2017/10/24
 * Time: 09:57
 */

public interface IVaryViewHelper {

    View getCurrentLayout();

    void restoreView();

    void showLayout(View view);

    View inflate(int layoutId);

    Context getContext();

    View getView();

}