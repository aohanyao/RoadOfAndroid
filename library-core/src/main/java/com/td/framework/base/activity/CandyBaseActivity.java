package com.td.framework.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.jaeger.library.StatusBarUtil;
import com.road.android.core.R;
import com.td.framework.ui.swipebacklayout.SwipeBackLayout;
import com.td.framework.ui.swipebacklayout.app.SwipeBackActivity;
import com.td.framework.utils.T;
import com.td.framework.utils.anim.ActivityAnimUtils;
import com.td.framework.utils.data.IntentUtils;

/**
 * 最基本的Activity
 * -------------------------------
 * v0.1 2018年10月04日11:44:26
 * ①showToast
 * ②startActivity
 * ③跳转动画
 * ④initToolbar
 * -------------------------------
 */
public class CandyBaseActivity extends SwipeBackActivity {

    protected Activity mActivity;

    protected Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        initSwipeActivity();
    }

    /**
     * 是否初始化了toolbar
     */
    private boolean isInitToolbar = false;


    @Override
    protected void onStart() {
        super.onStart();
        if (!isInitToolbar) {
            initToolbar();
        }
    }

    private void initSwipeActivity() {
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        mToolBar = findViewById(R.id.base_toolbar);
        if (null != mToolBar) {
            // 设置为透明色
            mToolBar.setBackgroundColor(0x00000000);
            // 设置全透明
            mToolBar.getBackground().setAlpha(0);
            // 清除标题
            mToolBar.setTitle("");
            setSupportActionBar(mToolBar);
//            mToolBar.setBackgroundColor(getToolbarBackground());
            //设置返回按钮
            mToolBar.setNavigationIcon(getNavigationIcon());
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationOnClickListener();
                }
            });
            isInitToolbar = true;
            //返回文字按钮
            View navText = findViewById(R.id.toolbar_nav_text);
            if (null != navText) {
                navText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNavigationOnClickListener();
                    }
                });
            }
        }

        // appbar
        AppBarLayout mAppBarLayout = findViewById(R.id.base_appbar);
        // 状态栏高度 getStatusBarHeight只是一个获取高度的方法
        int statusBarHeight = getStatusBarHeight(mActivity);
        //大于 19  设置沉浸和padding
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mAppBarLayout != null) {
                ViewGroup.MarginLayoutParams appbarLayoutParam = (ViewGroup.MarginLayoutParams) mAppBarLayout.getLayoutParams();
                // 更改高度 toolbar_height 的高度是可配置的
                appbarLayoutParam.height = (int) (getResources().getDimension(R.dimen.toolbar_height) + statusBarHeight);
                // 设置padding
                mAppBarLayout.setPadding(mAppBarLayout.getPaddingLeft(),
                        statusBarHeight,
                        mAppBarLayout.getPaddingRight(),
                        mAppBarLayout.getPaddingBottom());

                //重新设置回去
                mAppBarLayout.setLayoutParams(appbarLayoutParam);
            }
        }
        // 设置沉浸和状态栏的颜色为透明
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }
    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取toolbar的背景
     *
     * @return
     */
    private int getToolbarBackground() {
        return getResources().getColor(R.color.colorPrimary);
    }

    /**
     * 返回按钮点击
     */
    protected void onNavigationOnClickListener() {
        finish();
        slideLeftOut();
    }

    /**
     * 返回按钮
     *
     * @return
     */
    protected int getNavigationIcon() {
        return R.drawable.ic_chevron_left_write_24dp;
    }

    /**
     * 显示文本信息
     *
     * @param text 文本信息
     */
    public void showToast(String text) {
        T.showToast(mActivity, text);
    }

    /**
     * 显示文本信息
     *
     * @param resId 文本资源id信息
     */
    public void showToast(int resId) {
        T.showToast(mActivity, resId);
    }


    /**
     * 右边划出
     */
    protected void slideLeftOut() {
        ActivityAnimUtils.out(mActivity);
    }

    /**
     * 进入
     */
    protected void slideRightIn() {
        ActivityAnimUtils.to(mActivity);
    }

    @Override
    public void finish() {
        super.finish();
        slideLeftOut();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        slideLeftOut();
    }

    /**
     * 打开 Activity
     *
     * @param activity
     */
    protected void launchActivity(Class<? extends Activity> activity) {
        startActivity(new Intent(mActivity, activity));
        // 加上动画
        slideRightIn();
    }

    /**
     * 打开 Activity
     *
     * @param activity
     */
    protected void launchActivityForResult(Class<? extends Activity> activity, int requestCode) {
        startActivityForResult(new Intent(mActivity, activity), requestCode);
        // 加上动画
        slideRightIn();
    }

    /**
     * 打开新的 Activity
     *
     * @param activity 目标Activity
     * @param pairs    键值对
     */
    protected void launchActivity(Class<? extends Activity> activity, Pair<String, Object>... pairs) {
        Intent intent = new Intent(mActivity, activity);
        // 填充数据
        IntentUtils.fillIntent(intent, pairs);
        startActivity(intent);
        // 加上动画
        slideRightIn();
    }

    /**
     * 打开新的 Activity
     *
     * @param activity 目标Activity
     * @param pairs    键值对
     */
    protected void launchActivityForResult(Class<? extends Activity> activity, int requestCode, Pair<String, Object>... pairs) {
        Intent intent = new Intent(mActivity, activity);
        // 填充数据
        IntentUtils.fillIntent(intent, pairs);
        startActivityForResult(intent, requestCode);
        // 加上动画
        slideRightIn();
    }


}
