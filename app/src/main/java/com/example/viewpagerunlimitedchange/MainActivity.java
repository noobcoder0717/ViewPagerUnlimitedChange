package com.example.viewpagerunlimitedchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int curposition=0;//记录当前位置
    public ViewPager mViewPager;
    public TextView tv;
    public List<ImageView> mImageList;//轮播的图片集合
    public String[] mImageTitles;//标题集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    //初始化控件
    public void init(){
        mViewPager=findViewById(R.id.viewpager);
        tv=findViewById(R.id.tv_pager_title);
        initData();//初始化数据
        initView();//初始化View,设置适配器
    }

    //初始化数据(图片、标题、点击事件)
    public void initData(){
        mImageList=new ArrayList<>();
        mImageTitles=new String[]{"title4","title1","title2","title3","title4","title1"};
        int ImageRes[]=new int[]{R.drawable.apple_pic,R.drawable.banana_pic,R.drawable.cherry_pic,R.drawable.grape_pic};
        //加入最后一张图片，以形成无限循环
        ImageView iv;
        iv=new ImageView(this);
        iv.setBackgroundResource(ImageRes[3]);
        iv.setId(R.id.pager_image1);
        iv.setOnClickListener(new PagerImageOnClick());
        mImageList.add(iv);
        for(int i=0;i<4;i++){
            iv=new ImageView(this);
            iv.setBackgroundResource(ImageRes[i]);
            iv.setId(R.id.pager_image1);
            iv.setOnClickListener(new PagerImageOnClick());
            mImageList.add(iv);
        }
        iv=new ImageView(this);
        iv.setBackgroundResource(ImageRes[0]);
        iv.setId(R.id.pager_image1);
        iv.setOnClickListener(new PagerImageOnClick());
        mImageList.add(iv);
    }

    //给ViewPager设置适配器
    public void initView(){
        MyViewPagerAdapter viewPagerAdapter=new MyViewPagerAdapter(mImageList,mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            //每次切换页面都会调用这个方法
            //不论是用户手动切换或者是代码导致的切换(setCurrentItem)
            @Override
            public void onPageSelected(int position) {
                //这里的position是当前page对应的下标
                System.out.println(1);
                curposition=position;
                tv.setText(mImageTitles[position]);

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                //验证当前的滑动是否结束
                System.out.println(2);
                if(state==ViewPager.SCROLL_STATE_IDLE){
                    if(curposition==0){
                        mViewPager.setCurrentItem(4,false);//false表示不要动画效果
                    }else if(curposition==5){
                        mViewPager.setCurrentItem(1,false);
                    }
                }
            }
        });
        mViewPager.setCurrentItem(1);
    }




    private class PagerImageOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.pager_image1:
                    Toast.makeText(MainActivity.this,"图片1被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image2:
                    Toast.makeText(MainActivity.this,"图片2被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image3:
                    Toast.makeText(MainActivity.this,"图片3被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image4:
                    Toast.makeText(MainActivity.this,"图片4被点击",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    class MyViewPagerAdapter extends PagerAdapter{
        private List<ImageView> imageViewList;
        private ViewPager viewPager;

        public MyViewPagerAdapter(List<ImageView> imageViewList,ViewPager viewPager){
            this.viewPager=viewPager;
            this.imageViewList=imageViewList;
        }
        @Override
        //获取要滑动的控件的数量
        //position的最大数目
        public int getCount(){
            return mImageList.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object){
            return view==object;
        }
        @Override
        //一个page在切换完成后会调用该方法去加载下一个可能将被展示的page
        //如page1切换到page2，则会调用该方法加载page3
        //而从page3切换到page1，则会调用该方法加载page1
        public Object instantiateItem(ViewGroup container,int position){

            ImageView iv=mImageList.get(position);
            if(iv.getParent()!=null){
                ((ViewPager)iv.getParent()).removeView(iv);
            }
            container.addView(mImageList.get(position));
            return mImageList.get(position);
        }

        //ViewPager只会保留当前页和当前页左右两页
        //如从page2滑到page3，则page1会被删除
        @Override
        public void destroyItem(ViewGroup container,int position,Object object){
            viewPager.removeView(imageViewList.get(position));
        }
    }
}

