/*
 * Copyright (C) 2015 Basil Miller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gigamole.cutintolayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;

import com.gigamole.cutintolayout.lib.CutIntoLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int quarterHeight = getWindowManager().getDefaultDisplay().getHeight() / 5;

        final Animation slideAnimation = new TranslateAnimation(0, 0, -quarterHeight, quarterHeight);
        slideAnimation.setDuration(2000);
        slideAnimation.setInterpolator(new AnticipateOvershootInterpolator());
        slideAnimation.setRepeatCount(Animation.INFINITE);
        slideAnimation.setRepeatMode(Animation.REVERSE);

        final CutIntoLayout cutIntoLayout = (CutIntoLayout) findViewById(R.id.cut_into_layout);
        cutIntoLayout.startAnimation(slideAnimation);
    }
}
