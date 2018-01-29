package com.example.hunter_j.imageviewflowlayout.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hunter_j.imageviewflowlayout.R;
import com.example.hunter_j.imageviewflowlayout.entity.ImageDTO;
import com.example.hunter_j.imageviewflowlayout.utils.DensityUtil;

import java.util.List;

/**
 * Created by hunter_J on 2017/11/2.
 */

public class SendPostImageView extends LinearLayout {

    public static int MAX_WIDTH = 0;

    //照片的Url列表
    private List<ImageDTO> imageList;

    /** 长度 单位为Pixel **/
    private int pxMoreMaxWandH; //多张图的宽高
    private int pxImagePadding = DensityUtil.dip2px(getContext(), 4); //图片间的间距

    private int MAX_PER_ROW_COUNT = 4; //每行显示最大数

    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;

    private OnItemClickListener mOnItemClickListener;
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public SendPostImageView(Context context) {
        super(context);
    }

    public SendPostImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setList(List<ImageDTO> lists) throws IllegalArgumentException {
        if(lists==null){
            throw new IllegalArgumentException("imageList is null...");
        }
        imageList = lists;
        if (MAX_WIDTH > 0){
            pxMoreMaxWandH = (MAX_WIDTH - pxImagePadding*3)/4; //解决右侧图片与内容对不齐的问题
            initImageLayoutParams();
        }
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0){
            int width = measurewWidth(widthMeasureSpec);
            if (width>0){
                MAX_WIDTH = width;
                if (imageList != null && imageList.size() > 0){
                    setList(imageList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;

        moreParaColumnFirst = new LayoutParams(pxMoreMaxWandH,pxMoreMaxWandH);
        morePara = new LayoutParams(pxMoreMaxWandH, pxMoreMaxWandH);
        morePara.setMargins(pxImagePadding,0,0,0);

        rowPara = new LayoutParams(match,wrap);
    }


    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0){
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imageList == null || imageList.size() == 0){
            return;
        }

        if (imageList.size() == 1){
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(rowPara);
            addView(rowLayout);
            rowLayout.addView(createImageView(0, true));
            rowLayout.addView(createTextHint());
        }else {
            int allCount = imageList.size();

            int rowCount = allCount / MAX_PER_ROW_COUNT + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0); //行数
            for (int rowCursor = 0; rowCursor < rowCount ; rowCursor++){
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                rowLayout.setLayoutParams(rowPara);
                if (rowCursor != 0){
                    rowLayout.setPadding(0,pxImagePadding,0,0);
                }

                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ?
                        MAX_PER_ROW_COUNT : allCount % MAX_PER_ROW_COUNT; //每行的列数
                if (rowCursor != rowCount -1){
                    columnCount = MAX_PER_ROW_COUNT;
                }
                addView(rowLayout);

                int rowOffset = rowCursor * MAX_PER_ROW_COUNT; //行偏
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++){
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(createImageView(position, true));
                }
            }
        }
    }

    private View createImageView(int position, final boolean isMultiImage) {
        final ImageDTO photoInfo = imageList.get(position);
        final ImageView imageView = new ColorFilterImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);

        imageView.setOnClickListener(new ImageOnClickListener(position));
        if (position != imageList.size()-1) {
            imageView.setBackgroundColor(getResources().getColor(R.color.im_font_color_text_hint));
        }else {
            imageView.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (photoInfo.getLocal_drawable() != 0){
            Glide.with(getContext()).load(photoInfo.getLocal_drawable()).into(imageView);
        }else {
            Glide.with(getContext()).load(photoInfo.getPath()).centerCrop().into(imageView);
        }
        return imageView;
    }

    private View createTextHint(){
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.f_24));
        textView.setTextColor(getResources().getColor(R.color.c_gray_aebdcd));
        textView.setText("* 添加图片将会有更高浏览量哦^_^");
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, pxMoreMaxWandH);
        params.setMargins(pxImagePadding,0,0,pxImagePadding);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.BOTTOM);
        return textView;
    }

    private int measurewWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    private class ImageOnClickListener implements OnClickListener{

        private int position;

        public ImageOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(view, position);
            }
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }


}
