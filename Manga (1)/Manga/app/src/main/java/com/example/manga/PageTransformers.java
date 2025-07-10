//PageTransformers.java // tạo thêm
package com.example.manga;


import android.view.View;
import androidx.viewpager2.widget.ViewPager2;


public class PageTransformers {


    // Hiệu ứng lật sách cổ điển
    public static class BookFlipTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();


            if (position < -1) {
                page.setAlpha(0f);
            } else if (position <= 0) {
                page.setAlpha(1f);
                page.setTranslationX(0f);
                page.setRotationY(0);
            } else if (position <= 1) {
                page.setAlpha(1 - position);
                page.setTranslationX(pageWidth * -position);
                page.setRotationY(-90 * position);
            } else {
                page.setAlpha(0f);
            }
        }
    }


    // Hiệu ứng lật trang 3D sâu
    public static class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;


        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();


            if (position < -1) {
                view.setAlpha(0f);
            } else if (position <= 0) {
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setTranslationZ(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);
            } else if (position <= 1) {
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                view.setTranslationZ(-1f);


                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0f);
            }
        }
    }


    // Hiệu ứng zoom và fade
    public static class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;


        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();


            if (position < -1) {
                view.setAlpha(0f);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;


                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }


                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else {
                view.setAlpha(0f);
            }
        }
    }


    // Hiệu ứng lật trang như thật
    public static class RealisticPageFlipTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            if (position < -1) {
                page.setAlpha(0f);
            } else if (position <= 0) {
                page.setAlpha(1f);
                page.setTranslationX(0f);
                page.setRotationY(0);
                page.setScaleX(1f);
                page.setScaleY(1f);
            } else if (position <= 1) {
                page.setAlpha(1f);
                page.setTranslationX(page.getWidth() * -position);


                // Tạo hiệu ứng lật 3D
                float rotation = -180 * position;
                page.setRotationY(rotation);


                // Tạo shadow effect
                float scale = 1 - 0.1f * Math.abs(position);
                page.setScaleX(scale);
                page.setScaleY(scale);


                // Điều chỉnh độ sáng để tạo hiệu ứng shadow
                if (position > 0.5f) {
                    page.setAlpha(2 - 2 * position);
                }
            } else {
                page.setAlpha(0f);
            }
        }
    }


    // Hiệu ứng xoay cube
    public static class CubeInTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            page.setPivotX(position < 0f ? page.getWidth() : 0f);
            page.setPivotY(page.getHeight() * 0.5f);
            page.setRotationY(90f * position);
        }
    }


    // Hiệu ứng accordion
    public static class AccordionTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            page.setPivotX(position < 0 ? 0 : page.getWidth());
            page.setScaleX(position < 0 ? 1f + position : 1f - position);
        }
    }
}

