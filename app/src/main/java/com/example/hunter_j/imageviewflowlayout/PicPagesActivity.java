package com.example.hunter_j.imageviewflowlayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hunter_j.imageviewflowlayout.entity.ImageDTO;

import java.util.ArrayList;

public class PicPagesActivity extends AppCompatActivity implements View.OnClickListener{

    public final static String PIC_DATA = "pic_datas";
    public final static String PIC_INDEX = "pic_index";
    public final static String PIC_FROM = "pic_from";
    public final static String PIC_LOCAL = "pic_from_sendPost";
    private ArrayList<ImageDTO> picDatas = new ArrayList<>();

    private TextView mTvBack,mTvDel;
    private ViewPager mVp;
    private RelativeLayout mRlActionBar;

    private Context mContext;
    private int page_index = 0;
    private String page_from = PIC_LOCAL;

    //ViewPager适配器与监听
    private PicPageListener mPicPageListener;
    private PicPageAdapter mPicPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pic_pages);

        Bundle bundle = getIntent().getExtras();
        onReceiveArguments(bundle != null ? bundle : new Bundle());
        mContext = this;
        initView();

    }

    protected void onReceiveArguments(Bundle bundle) {
        picDatas = (ArrayList<ImageDTO>) bundle.getSerializable(PIC_DATA);
        page_index = bundle.getInt(PIC_INDEX,0) -1;
        page_from = bundle.getString(PIC_FROM, PIC_LOCAL);
    }



    private void initView() {

        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvDel = (TextView) findViewById(R.id.tv_del);
        mVp = (ViewPager) findViewById(R.id.vp_pic);
        mRlActionBar = (RelativeLayout) findViewById(R.id.rl_actionbar);

        mTvBack.setText(String.format("返回      %s / %s",page_index+1, picDatas.size()));

        mPicPageAdapter = new PicPageAdapter(picDatas);
        mVp.setAdapter(mPicPageAdapter);
        mVp.setCurrentItem(page_index);

        mPicPageListener = new PicPageListener();
        mTvBack.setOnClickListener(this);
        mTvDel.setOnClickListener(this);
        mVp.addOnPageChangeListener(mPicPageListener);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_del:
                if (picDatas.size() == 0) return;
                //通知发帖界面删除图片
                sendDeletePicBroadcast(page_index);
                //删除ViewPager中的图片
                picDatas.remove(page_index);
                mTvBack.setText(String.format("返回      %s / %s" ,page_index+1 ,picDatas.size()));
                if (picDatas.size() != 0) {
                    mPicPageAdapter.notifyDataSetChanged();
                } else {
                    finish();
                }
                break;
        }
    }

    /**
     * 通知发帖界面删除图片
     * @param index 图片下标
     */
    private void sendDeletePicBroadcast(int index) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(PIC_INDEX, index);
        intent.putExtras(bundle);
        intent.setAction(MainActivity.BROAD_ACTION_DELETE_PIC);
        sendBroadcast(intent);
    }

    /**
     * 实现VierPager监听器接口
     */
    class PicPageListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageSelected(int position) {
            page_index = position;
            mTvBack.setText(String.format("返回      %s / %s",position+1,picDatas.size()));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    /**
     * ViewPager的适配器
     */
    class PicPageAdapter extends PagerAdapter {

        private ArrayList<ImageDTO> picData = new ArrayList<>();

        public PicPageAdapter(ArrayList<ImageDTO> picDatas) {
            this.picData = picDatas;
        }

        @Override
        public int getCount() {
            //选取超大值，实现无限循环
            return picData.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object ;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageview = new ImageView(mContext);
            imageview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT));
            Glide.with(mContext).load(picData.get(position).getLocal_drawable()).into(imageview);
            container.addView(imageview);
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRlActionBar.getVisibility() == View.VISIBLE){
                        mRlActionBar.setVisibility(View.GONE);
                    }else {
                        mRlActionBar.setVisibility(View.VISIBLE);
                    }
                }
            });
            return imageview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView(container.getChildAt(position%mList.size()));
            container.removeView((View) object);
        }
    }


}
