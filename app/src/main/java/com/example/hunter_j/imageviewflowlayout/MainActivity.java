package com.example.hunter_j.imageviewflowlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hunter_j.imageviewflowlayout.entity.ImageDTO;
import com.example.hunter_j.imageviewflowlayout.view.SendPostImageView;
import com.example.hunter_j.imageviewflowlayout.view.ToastUtil;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private SendPostImageView mMultiImageView;


    /**
     * 删除图片的广播
     */
    public static final String BROAD_ACTION_DELETE_PIC = "delete_picture";

    /**
     * 图片获取成功获取的数组
     */
    private ArrayList<ImageDTO> photos = new ArrayList<>();
    private int[] imgs = new int[]{R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,
            R.drawable.img_4,R.drawable.img_5,R.drawable.img_6,R.drawable.img_7,
            R.drawable.img_8,R.drawable.img_9,R.drawable.img_10};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        initView();
        
    }

    private void initView() {

        mMultiImageView = (SendPostImageView) findViewById(R.id.multiImagView);

        loadFlowPhotoFirst();
    }

    /**
     * 加载初始add照片
     */
    private void loadFlowPhotoFirst() {
        ImageDTO photoInfo = new ImageDTO();
        photoInfo.setLocal_drawable(R.drawable.add_pic);
        photoInfo.setLocal(true);
        photos.add(photos.size(), photoInfo);
        addData();
    }

    /**
     * add照片
     */
    private void addData() {
        ArrayList<String> pp = null;
        if (pp == null) {
            pp = new ArrayList<>();
        } else {
            pp.clear();
        }
        for (int i = 0; i < photos.size() - 1; i++) {
            pp.add(photos.get(i).getQiniu_path());
        }
        if (photos != null && photos.size() > 0) {
            if (photos.size() == 10) {
                photos.remove(9);
            }
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMultiImageView.setList(photos);
                }
            });
            mMultiImageView.setmOnItemClickListener(new SendPostImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    if (photos.get(position).isLocal()) {
                        ImageDTO imageDTO = new ImageDTO();
                        Random random = new Random();
                        int index = random.nextInt(imgs.length);
                        imageDTO.setLocal_drawable(imgs[index]);
                        imageDTO.setLocal(false);
                        photos.add(photos.size() - 1, imageDTO);
                        addData();
                        return;
                    }

                    ArrayList<ImageDTO> pp = new ArrayList<ImageDTO>();
                    pp.addAll(photos);
                    if (pp.get(photos.size() - 1).getLocal_drawable() != 0) {
                        pp.remove(photos.size() - 1);
                    }
                    Intent intent2PicDetail = new Intent(mContext, PicPagesActivity.class);
                    Bundle bundle2PicDetail = new Bundle();
                    bundle2PicDetail.putSerializable(PicPagesActivity.PIC_DATA, pp);
                    bundle2PicDetail.putInt(PicPagesActivity.PIC_INDEX, Integer.parseInt((position + 1) + ""));
                    bundle2PicDetail.putString(PicPagesActivity.PIC_FROM, PicPagesActivity.PIC_LOCAL);
                    intent2PicDetail.putExtras(bundle2PicDetail);
                    startActivity(intent2PicDetail);
                }
            });
        }
        pp.clear();
    }

    /**
     * 接收更新列表消息的广播
     */
    BroadcastReceiver receiverDeletePic = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            switch (action) {
                case BROAD_ACTION_DELETE_PIC:
                    if (bundle != null) {
                        int index = bundle.getInt(PicPagesActivity.PIC_INDEX, -1);
                        if (index != -1) {
                            delete_index(index);
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 删除图片回调
     *
     * @param index
     */
    public void delete_index(int index) {
        photos.remove(index);
        if (photos.size() == (8) && !photos.get(7).isLocal()) {
            loadFlowPhotoFirst();
            return;
        }
        addData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_DELETE_PIC);
        registerReceiver(receiverDeletePic, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁广播
        unregisterReceiver(receiverDeletePic);

    }
}
