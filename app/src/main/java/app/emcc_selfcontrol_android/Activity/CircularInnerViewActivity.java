/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Omada Health, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package app.emcc_selfcontrol_android.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.emcc_selfcontrol_android.Interface.UpdateState;
import app.emcc_selfcontrol_android.R;
/**
 * Created by oliviergoutay on 12/8/14.
 */
public class CircularInnerViewActivity extends LinearLayout implements UpdateState{
    /**
     * TAG for logging
     */
    private TextView user_top_textview;
    private TextView value_info_textview;
    private TextView user_bottom_textview;
    private static final String TAG = "HomeUserView";

    public CircularInnerViewActivity(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout mainV = (LinearLayout) inflater.inflate(R.layout.view_user_info, this);
        user_top_textview=(TextView)mainV.findViewById(R.id.user_top_textview);
        value_info_textview=(TextView)mainV.findViewById(R.id.value_info_textview);
        user_bottom_textview=(TextView)mainV.findViewById(R.id.user_bottom_textview);
        //TODO init view
    }

    @Override
    public void updateDreamName(String name) {
        user_top_textview.setText(name);
    }

    @Override
    public void updateDreamProgress(String name) {
        value_info_textview.setText(name);
    }

    @Override
    public void updateDreamToole(String name) {
        user_bottom_textview.setText(name);
    }
}
