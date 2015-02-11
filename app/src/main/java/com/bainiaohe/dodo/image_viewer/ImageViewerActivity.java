package com.bainiaohe.dodo.image_viewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bainiaohe.dodo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageViewerActivity extends ActionBarActivity {

    public static final String TAG = "ImageViewerActivity";
    /**
     * 启动image viewer是需要传入的参数名
     */
    public static final String PARAM_IMAGE_URLS = "image_urls";
    /**
     * 启动image viewer是需要传入的参数名
     */
    public static final String PARAM_SELECTED_IMAGE_INDEX = "selected_image_index";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter = null;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager = null;

    private ArrayList<String> imageUrls = null;

    /**
     * Indicates the current page
     */
    private TextView indicator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取参数
        imageUrls = getIntent().getStringArrayListExtra(PARAM_IMAGE_URLS);
        int selected_index = getIntent().getIntExtra(PARAM_SELECTED_IMAGE_INDEX, 0);
        for (String url : imageUrls)
            Log.e(TAG, "image url:" + url);
        Log.e(TAG, "Selected Index:" + selected_index);

        //不显示ActionBar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_image_viewer);

        initViews(selected_index);
    }

    /**
     * 初始化
     *
     * @param selectedIndex
     */
    private void initViews(int selectedIndex) {
        indicator = (TextView) findViewById(R.id.indicator);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        Log.e(TAG, "view pager == null ? " + (mViewPager == null));

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                indicator.setText((i + 1) + "/" + mSectionsPagerAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //TODO 设置当前page
        mViewPager.setCurrentItem(selectedIndex, false);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String PARAM_IMAGE_URL = "image_url";
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private String imageUrl = null;

        public PlaceholderFragment() {
            this.imageUrl = "";
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String imageUrl, int sectionNumber) {

            Bundle bundle = new Bundle();
            bundle.putString(PARAM_IMAGE_URL, imageUrl);

            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setImageUrl(bundle);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public void setImageUrl(Bundle bundle) {
            this.imageUrl = bundle.getString(PARAM_IMAGE_URL);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_image_viewer, container, false);

            Picasso.with(getActivity()).load(imageUrl)
                    .placeholder(R.drawable.picture_holder)
                    .error(R.drawable.picture_load_failed)
                    .into((android.widget.ImageView) rootView.findViewById(R.id.image));

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(imageUrls.get(position), position);
        }

        @Override
        public int getCount() {

            return imageUrls == null ? 0 : imageUrls.size();
        }
    }
}
