package taopp.com.taopiaopiaodemo;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import taopp.com.taopiaopiaodemo.widget.ClipViewPager;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.viewpager)
    ClipViewPager mViewPager;
    @BindView(R.id.container)
    LinearLayout ll;

    //屏幕宽度
    private int mScreenWidth;
    private List<View> viewlist = new ArrayList<>();
    private VpAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        for (int i = 0; i < 8; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.hualang_item_viewpager_layout, null);
            viewlist.add(view);
        }
        //屏幕宽度
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        //设置缓存数量
        mViewPager.setOffscreenPageLimit(8);
        //设置间距
        mViewPager.setPageMargin(50);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                mScreenWidth / 5, ViewPager.LayoutParams.WRAP_CONTENT);
        mViewPager.setLayoutParams(params);

        adapter = new VpAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setCurrentItem(0);



        //监听每个页面的点击，将事件分发给viewpager
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
    }

    class VpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewlist.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewlist.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(viewlist.get(position));
            return viewlist.get(position);
        }
    }

    /**
     * 切换动画
     */
    class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.9f;
        private static final float MIN_ALPHA = 0.5f;

        private float defaultScale = 0.9f;

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) {
                // This page is way off-screen to the left.
//                view.setAlpha(0);
                view.setScaleX(defaultScale);
                view.setScaleY(defaultScale);
            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
//                view.setAlpha(MIN_ALPHA +
//                        (scaleFactor - MIN_SCALE) /
//                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
//                view.setAlpha(0);
                view.setScaleX(defaultScale);
                view.setScaleY(defaultScale);
            }
        }
    }

}
