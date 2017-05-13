package com.example.yuanmengzeng.hexagonblock;

import java.lang.ref.WeakReference;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import yuanmengzeng.donwload.DownloadEntity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuanmengzeng.hexagonblock.CustomView.DiamondView;
import com.example.yuanmengzeng.hexagonblock.CustomView.HexagonHeap;
import com.example.yuanmengzeng.hexagonblock.CustomView.HexagonView;
import com.example.yuanmengzeng.hexagonblock.CustomView.HorizontalLineBlock;
import com.example.yuanmengzeng.hexagonblock.Http.DataManager;
import com.example.yuanmengzeng.hexagonblock.QQ.QQUiListener;
import com.example.yuanmengzeng.hexagonblock.RankList.RankDialog;
import com.example.yuanmengzeng.hexagonblock.Share.DiamondDialog;
import com.example.yuanmengzeng.hexagonblock.Share.MenuPopWindow;
import com.example.yuanmengzeng.hexagonblock.Share.ShareDialog;
import com.example.yuanmengzeng.hexagonblock.download.DownloadDialog;
import com.example.yuanmengzeng.hexagonblock.download.DownloadService;
import com.example.yuanmengzeng.hexagonblock.download.DownloadTestAcitivity;
import com.example.yuanmengzeng.hexagonblock.model.UpgradeModel;
import com.example.yuanmengzeng.hexagonblock.util.FileUtil;
import com.tencent.tauth.Tencent;

/**
 * Created by yuanmengzeng on 2016/5/17.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener
{

    private final static String HIGHTEST_SCORE = "hexagon_block_hightest_score";

    private HexagonHeap hexagonHeap;

    private HexagonView hexblock;

    private HorizontalLineBlock leftBlock, centerBlock, rightBlock;

    private ArrayList<HorizontalLineBlock> blocks = new ArrayList<>();

    private EditText editText;

    private TextView topScoreTx;

    private HexagonPositinHandler positinHandler;

    private ScoreManager scoreManager;

    private SoundManager soundManager;

    private MediaPlayer mediaPlayer;

    private int topScore;

    private ImageView coverImg;

    private View cover;

    private View loadingCircle;

    private View menuList;

    private DiamondView diamondView;

    private AnimatorSet loadingCircleAnim;

    private ShareDialog shareDialog;

    private ExitDialog exitDialog;

    private ArrayList<Dialog> dialogs = new ArrayList<>();

    private MenuPopWindow menuPopWindow;

    private DiamondDialog diamondDialog;

    private RankDialog rankDialog;

    private DownloadDialog downloadDialog;

    private int diamondScore; // 加钻石的门槛分数

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ZYMLog.info(" ----------------------------- app start");
        setContentView(R.layout.main_canvas_layout);
        initCoverView();
        // initGameView();
    }

    private void initGameView()
    {

        ZYMLog.info("ZYM width is " + getResources().getDisplayMetrics().widthPixels);
        ZYMLog.info("ZYM height is " + getResources().getDisplayMetrics().heightPixels);
        hexblock = (HexagonView) findViewById(R.id.hexBlock);
        editText = (EditText) findViewById(R.id.editScore);

        topScoreTx = (TextView) findViewById(R.id.hightest_socre);
        TextView sumScoreTx = (TextView) findViewById(R.id.sum_score_tx);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/f.ttf"); // 设置显示总分的字体
        sumScoreTx.setTypeface(typeface);
        topScore = CommonUtils.readPrefsInt(this, HIGHTEST_SCORE);
        String text = "" + topScore;
        topScoreTx.setText(text);

        scoreManager = new ScoreManager(MainActivity.this, (TextView) findViewById(R.id.sum_score_tx),
                (TextView) findViewById(R.id.step_score_tx));
        scoreManager.setTopScoreInfo(topScoreTx, topScore);
        positinHandler.setScoreManager(scoreManager);

        leftBlock.setOnTouchListener(this);
        centerBlock.setOnTouchListener(this);
        rightBlock.setOnTouchListener(this);

        menuList = findViewById(R.id.menu_list);
        menuList.setOnClickListener(this);
        hexblock.setHexContentColor(getResources().getColor(R.color.yellow));

        /**** 随机数测验 *****/
        // for (int i = 0; i < positinHandler.getRandomTypeProducer().getSum()
        // * 100; i++)
        // {
        // positinHandler.changeBlockTypeRandomly(leftBlock);
        // }
        // ZYMLog.info("counter type is " + positinHandler.counter);
        /******************/

        findViewById(R.id.animatorTest).setOnClickListener(this);
        diamondView = (DiamondView) findViewById(R.id.diamond);
        diamondView.setOnClickListener(this);

        diamondScore = CommonData.STATE_SCORE_LEVEl;

    }

    private void initCoverView()
    {
        coverImg = (ImageView) findViewById(R.id.cover_image);
        cover = findViewById(R.id.cover);
        cover.setVisibility(View.VISIBLE);
        loadingCircle = findViewById(R.id.loading_circle);
        loadingCircleAnim = new AnimatorSet();

        hexagonHeap = (HexagonHeap) findViewById(R.id.hexagonHeap);
        // hexagonHeap.setHeapBgResourse(R.drawable.bruce_bg);
        leftBlock = (HorizontalLineBlock) findViewById(R.id.left_bottom_block);
        centerBlock = (HorizontalLineBlock) findViewById(R.id.center_bottom_block);
        rightBlock = (HorizontalLineBlock) findViewById(R.id.right_bottom_block);

        blocks.add(leftBlock);
        blocks.add(centerBlock);
        blocks.add(rightBlock);

        positinHandler = new HexagonPositinHandler(this);
        positinHandler.setHexagonHeap(hexagonHeap);
        positinHandler.changeBlockTypeRandomly(leftBlock);
        positinHandler.changeBlockTypeRandomly(centerBlock);
        positinHandler.changeBlockTypeRandomly(rightBlock);

        findViewById(R.id.skim_btn).setOnClickListener(this);

        final MyHandler myHandler = new MyHandler(new WeakReference<>(this));
        myHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                ZYMLog.info("postdelay^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                scoreManager = new ScoreManager(MainActivity.this, (TextView) findViewById(R.id.sum_score_tx),
                        (TextView) findViewById(R.id.step_score_tx));
                scoreManager.setTopScoreInfo(topScoreTx, topScore);
                if (soundManager == null)
                {
                    soundManager = new SoundManager(MainActivity.this, new SoundManager.SoundCallBack()
                    {
                        @Override
                        public void onBgSoundLoadSuc(MediaPlayer mp)
                        {
                            ZYMLog.info("onBgSoundLoadSuc^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                            mediaPlayer = mp;
                            if (isNormalDay)
                            { // 正常日子 非生日
                                skimCover();
                            }
                            else
                            { // 生日那天
                                mp.start();
                                if (cover.getVisibility() == View.VISIBLE)
                                {
                                    coverImg.setImageResource(R.drawable.cover_birthday);
                                    findViewById(R.id.loading_hint).setVisibility(View.GONE);
                                    findViewById(R.id.skim_btn).setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
                ReqTimeThread reqTimeThread = new ReqTimeThread(myHandler);
                reqTimeThread.start();
            }
        }, 500);
        coverTimerTask();
        DataManager.getInstance().reqUpdateInfo(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.animatorTest:

                for (int i = 0; i < hexagonHeap.getChildCount(); i++)
                {
                    ((HexagonView) hexagonHeap.getChildAt(i)).setHexContentColor(
                            getResources().getColor(R.color.ver3_dark_gray));
                }
                String text = editText.getText().toString();
                int order = Integer.valueOf(text);
                HexagonView hexagonView = (HexagonView) hexagonHeap.getChildAt(order);
                hexagonView.setHexContentColor(Color.RED);
                positinHandler.changeBlockType(leftBlock, order);
                for (int i = 0; i <= 5; i++)
                {
                    HexagonView hex = hexagonView.getAdjacentHexagon(i);
                    if (hex != null)
                    {
                        hex.setHexContentColor(Color.RED);
                    }
                }
                // int score = Integer.valueOf(text);
                // scoreManager.addScore(score);
                // playScoreSound(score);
                // startScaleAnim(loadingCircle);
                // soundManager.playBlockExpandSound();
                break;
            case R.id.buzzer_3d:
                if (v.getTag() != null && v.getTag() instanceof Boolean)
                {
                    boolean isEnable = (boolean) v.getTag();
                    if (isEnable)
                    {
                        ((ImageView) v).setImageResource(R.drawable.sound_off_64);
                        v.setTag(false);
                        soundManager.setBgEnable(false);
                    }
                    else
                    {
                        ((ImageView) v).setImageResource(R.drawable.sound_on_64);
                        v.setTag(true);
                        soundManager.setBgEnable(true);
                    }
                }
                break;
            case R.id.share_3d:
                shareScore();
                break;
            case R.id.reboot_game_3d:
                // Intent intent = new Intent(MainActivity.this,
                // DownloadService.class);
                // String url =
                // "http://res.wx.qq.com/voice/getvoice?mediaid=MzA4MTAxMzcxNl8yNjQ5NTkzNjI1";
                // String url =
                // "http://sqdd.myapp.com/myapp/qqteam/Androidlite/qqlite_3.5.0.660_android_r108360_GuanWang_537047121_release_10000484.apk";
                // String url = "http://120.24.93.248/app/HexagonBlock.apk";
                // intent.putExtra(DownloadService.URL, url);
                // MainActivity.this.startService(intent);
                reStartGame();
                break;
            case R.id.menu_list:
                soundManager.playMenuSound();
                v.setBackgroundResource(R.drawable.solid_t_radius_gray_btn_normal);
                showMenuPopWindow();
            case R.id.skim_btn:
                skimCover();
                break;
            case R.id.diamond:
                showDiamondDialog();
                break;
            case R.id.rank_list:
                showRankDialog();
                break;
            case R.id.download:
                showDownloadDialog();
                // Intent intent2 = new Intent(MainActivity.this,
                // DownloadTestAcitivity.class);
                // startActivity(intent2);
                break;
        }
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event)
    {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                // ZYMLog.info("---------------action down");
                moveToPosition(v, (int) rawX, (int) rawY);
                positinHandler.expandBlockSize((HorizontalLineBlock) v);
                soundManager.playBlockExpandSound();
                break;
            case MotionEvent.ACTION_MOVE:
                // ZYMLog.info("---------------action move");
                moveToPosition(v, (int) rawX, (int) rawY);
                boolean isRefBlockChanged = positinHandler.matchBlock((HorizontalLineBlock) v);
                if (isRefBlockChanged)
                {
                    soundManager.playBlockMatchSound();
                }
                break;
            case MotionEvent.ACTION_UP:
                // v.setVisibility(View.INVISIBLE);
                boolean isMatched = positinHandler.checkBlockGroupMatched((HorizontalLineBlock) v); // 检测当前block是否匹配上了
                if (isMatched)
                {
                    positinHandler.fixBlockWithAnim((HorizontalLineBlock) v, new Animation.AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                            v.setVisibility(View.VISIBLE);
                            soundManager.playBlockPlaceSound();
                        }

                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            v.setVisibility(View.INVISIBLE);
                            positinHandler.revertBlockSize((HorizontalLineBlock) v); // 回复初始大小
                            positinHandler.setHexagonViewMatched((HorizontalLineBlock) v);
                            positinHandler.changeBlockTypeRandomly((HorizontalLineBlock) v);
                            boolean isClearable = positinHandler.checkClearLine(); // 若匹配上了,则检测是否有整行需要消除，并加分
                            if (isClearable)
                            {
                                playScoreSound(scoreManager.getStepScore());
                            }
                            moveToInitPosition(v);
                            new Handler().postDelayed(new Runnable() //
                            {
                                @Override
                                public void run()
                                {
                                    if (checkGameOver())
                                    {
                                        showExitDialog(CommonData.TYPE_DIAMOND);
                                    }
                                }
                            }, 150);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        }
                    });
                }
                else
                {
                    positinHandler.revertBlockSize((HorizontalLineBlock) v); // 回复初始大小
                    moveToInitPosition(v);
                    soundManager.playBlockRevertSound();
                }
                if (scoreManager.getSumScore() > CommonData.THIRD_STAGE_SOCRE)
                { // 超过20000分，屌，虐个狗（《不能说的秘密》四手联弹）
                    soundManager.playBgSound(2);
                    positinHandler.setClearAlpha(0x05);
                }
                else if (scoreManager.getSumScore() > CommonData.SECOND_STAGE_SOCRE)
                {// 超过8000分，高手，换个有意思的音乐
                    soundManager.playBgSound(1);
                    positinHandler.setClearAlpha(0x10);
                }

                if (scoreManager.getSumScore() >= diamondScore)
                {
                    diamondScore += CommonData.STATE_SCORE_LEVEl;
                    diamondView.addDiamond(1);
                }
                // ZYMLog.info("----------------action up");
                break;

        }
        return true;
    }

    /**
     * 弹出菜单窗口
     */
    private void showMenuPopWindow()
    {
        if (menuPopWindow == null)
        {
            menuPopWindow = new MenuPopWindow(this, this);
            menuPopWindow.setOnDismissListner(new MenuPopWindow.onDismissListner()
            {
                @Override
                public void onDismiss()
                {
                    menuList.setBackground(null);
                }
            });
        }
        menuPopWindow.showAsDropDown(menuList, 0, 0);
    }

    private DiamondDialog.OnDiamondCsmListner onDiamondCsmListner = new DiamondDialog.OnDiamondCsmListner()
    {
        @Override
        public void onBlockTransform(int whickBlock, int newForm)
        {
            diamondView.addDiamond(-1);
            ZYMLog.info("diamond count is " + diamondView.getDiamondCount());
            switch (whickBlock)
            {
                case CommonData.BLOCK_LEFT:
                    positinHandler.changeBlockType(leftBlock, newForm);
                    moveToInitPosition(leftBlock);
                    break;
                case CommonData.BLOCK_CENTER:
                    positinHandler.changeBlockType(centerBlock, newForm);
                    moveToInitPosition(centerBlock);
                    break;
                case CommonData.BLOCK_RIGHT:
                    positinHandler.changeBlockType(rightBlock, newForm);
                    moveToInitPosition(rightBlock);
                    break;
            }
        }
    };

    public void showDiamondDialog()
    {
        if (diamondDialog == null)
        {
            diamondDialog = new DiamondDialog(MainActivity.this, diamondView.getDiamondCount(),
                    leftBlock.getBlockType(), centerBlock.getBlockType(), rightBlock.getBlockType());
            diamondDialog.setOnDiamondCsmListner(onDiamondCsmListner);
        }
        else
        {
            diamondDialog.refreshData(diamondView.getDiamondCount(), leftBlock.getBlockType(),
                    centerBlock.getBlockType(), rightBlock.getBlockType());
        }
        diamondDialog.show();

    }

    private void showRankDialog()
    {
        rankDialog = new RankDialog();
        rankDialog.setScore(scoreManager.getSumScore());
        rankDialog.show(getSupportFragmentManager(), "RankDialog");
    }

    private void showDownloadDialog()
    {
        downloadDialog = new DownloadDialog();
        // downloadDialog.setShowsDialog(true);
        downloadDialog.show(getSupportFragmentManager(), "DownloadDialog");
    }

    private void startScaleAnim(View view)
    {
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.5f, 1.0f, 0.5f, 1.0f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.5f, 1.0f, 0.5f, 1.0f);
        scaleXAnim.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnim.setRepeatCount(ValueAnimator.INFINITE);

        if (loadingCircleAnim.isRunning())
            return;
        loadingCircleAnim.setInterpolator(new DecelerateInterpolator());
        loadingCircleAnim.setDuration(1000);
        loadingCircleAnim.playTogether(scaleXAnim, scaleYAnim);
        loadingCircleAnim.start();
    }

    private boolean isNormalDay = true;

    private void coverTimerTask()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (isNormalDay && cover.getVisibility() != View.GONE)
                {
                    cover.setVisibility(View.GONE);
                    initGameView();
                    if (soundManager != null)
                    {
                        soundManager.stopBgSound();
                    }
                    ZYMLog.info("ZYM coverTimerTask run");
                }
            }
        }, 5000);
    }

    boolean firstDraw = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        ZYMLog.info(" ----------------------------- app onWindowFocusChanged");
        if (cover.getVisibility() == View.VISIBLE)
        {
            startScaleAnim(loadingCircle);
        }

        if (positinHandler != null && firstDraw) // 只有第一次进入界面的时候才定位各个棋子的位置
        {
            firstDraw = false;
            positinHandler.setInitPositionInfo(leftBlock, 0);
            positinHandler.setInitPositionInfo(centerBlock, 1);
            positinHandler.setInitPositionInfo(rightBlock, 2);
        }
    }

    private void moveToPosition(View view, int rawX, int rawY)
    {
        int left = rawX - view.getMeasuredWidth() / 2;
        int bottom = rawY - CommonUtils.getStatusHeight(this) - ((ViewGroup) view).getChildAt(0).getMeasuredHeight();
        int top = bottom - view.getMeasuredHeight();
        if (top <= 0)
            top = 0;
        if (left <= 0)
            left = 0;
        if (left + view.getMeasuredWidth() > getResources().getDisplayMetrics().widthPixels)
        {
            left = getResources().getDisplayMetrics().widthPixels - view.getMeasuredWidth();
        }
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (lp != null)
        {
            lp.leftMargin = left;
            lp.topMargin = top;
            lp.gravity = Gravity.LEFT | Gravity.TOP;
            view.setLayoutParams(lp);
            // ZYMLog.info("leftMargin is "+left+" topMargin is "+top);
        }
        else
        { // 如果不能使用margin方式设置view的位置，则使用layout方式
            view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
        }
    }

    private void moveToInitPosition(View v)
    {
        switch (v.getId())
        {
            case R.id.left_bottom_block:
                positinHandler.moveToInitPosition(v, 0);
                break;
            case R.id.center_bottom_block:
                positinHandler.moveToInitPosition(v, 1);
                break;
            case R.id.right_bottom_block:
                positinHandler.moveToInitPosition(v, 2);
                break;
        }
    }

    /**
     * 根据得分使用不同的音效
     * 
     * @param score 得分
     */
    private void playScoreSound(int score)
    {
        ZYMLog.info("step score is " + score);
        double rootScore = Math.sqrt(score);
        for (int i = 9; i >= 0; i--)
        {
            if (rootScore >= ScoreManager.SCORE_LEVEL[i])
            {
                soundManager.playRowClearSound(i);
                break;
            }
        }
    }

    /**
     * 检测日子是否对
     * 
     * @param date date
     */
    private void calibreBirthDay(Date date)
    {
        int month, day;
        if (date != null)
        {
            month = date.getMonth() + 1;
            day = date.getDate();
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DATE);
        }
        if (month == 7 && day == 30)
        { // 7.30
            isNormalDay = false;
            soundManager.playBgSound(3);
        }
        else
        {
            skimCover();
        }
        ZYMLog.info("ZYM month is " + month + "  day is " + day);
    }

    private int skimTimes = 0;

    private void skimCover()
    {
        skimTimes++;
        if (skimTimes > 1)
        {
            if (!mediaPlayer.isPlaying())
            {
                mediaPlayer.start();
            }
            if (loadingCircleAnim.isRunning())
            {
                loadingCircleAnim.cancel();
            }
            if (cover.getVisibility() != View.GONE)
            {
                cover.setVisibility(View.GONE);
                initGameView();
            }
            isNormalDay = true;
            // skimTimes = 0;
        }
    }

    private void shareScore()
    {
        if (shareDialog == null)
        {
            shareDialog = new ShareDialog(this, scoreManager.getSumScore());
            dialogs.add(shareDialog);
        }
        else
        {
            shareDialog.setScore(scoreManager.getSumScore());
        }
        shareDialog.show();
    }

    /**
     * 检测游戏是否结束
     * 
     * @return 是否结束
     */
    private boolean checkGameOver()
    {
        boolean isGameOver = true;
        int i = 0;
        while (isGameOver && i < blocks.size())
        {
            isGameOver = !positinHandler.isRoom2Place(blocks.get(i++));
        }
        return isGameOver;

    }

    @Override
    public void onBackPressed()
    {
        showExitDialog(CommonData.TYPE_EXIT);
    }

    private void showExitDialog(int type)
    {
        if (exitDialog == null)
        {
            exitDialog = new ExitDialog(this, new ExitDialog.DismissListner()
            {
                @Override
                public void onDismiss(boolean exit)
                {
                    if (!exit)
                    {
                        soundManager.resumeSound();
                    }
                    else
                    {
                        soundManager.stopBgSound();
                    }
                }
            });
            dialogs.add(exitDialog);
        }
        exitDialog.setType(type);
        exitDialog.show();
        if (soundManager != null)
        {
            soundManager.pauseSound();
        }
        writeRecord();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        ZYMLog.info("ZYM onPause");
        if (soundManager != null)
        {
            soundManager.pauseSound();
        }
        writeRecord();
    }

    @Override
    protected void onDestroy()
    {
        ZYMLog.info("ZYM onDestroy");
        if (soundManager != null)
        {
            soundManager.releaseAll();
        }
        dismissAllDialog();
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        ZYMLog.info(" ----------------------------- app display");
        super.onResume();
        if (soundManager != null)
        {
            soundManager.resumeSound();
        }
    }

    private static class MyHandler extends Handler
    {

        private WeakReference<MainActivity> mainActivityRefs;

        public MyHandler(WeakReference<MainActivity> mainActivityRefs)
        {
            this.mainActivityRefs = mainActivityRefs;
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case CommonData.LOAD_TIME_DATA_SUCS: // 加载网络时间成功
                    Date date = (Date) msg.obj;
                    if (date != null)
                    {
                        mainActivityRefs.get().calibreBirthDay(date);
                    }
                    else
                    {
                        mainActivityRefs.get().calibreBirthDay(null);
                    }
                    break;
                case CommonData.LOAD_TIME_DATA_FAIL:
                    mainActivityRefs.get().calibreBirthDay(null);
                    break;
            }
        }
    }

    private void writeRecord()
    {
        if (topScoreTx != null)
        {
            String text = topScoreTx.getText().toString();
            topScore = Integer.valueOf(text);
            CommonUtils.writePrefsInt(this, HIGHTEST_SCORE, topScore);
        }
    }

    private void reStartGame()
    {
        diamondView.setDiamondCount(1);
        positinHandler.setClearAlpha(0);
        soundManager.playBgSound(0);
        hexagonHeap.reset();
        scoreManager.reset();
    }

    /**
     * 隐去所有的dialog
     */
    private void dismissAllDialog()
    {
        for (Dialog dialog : dialogs)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CommonData.QQ_LOGIN_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Tencent.onActivityResultData(requestCode, resultCode, data, new QQUiListener());
        }
    }
}
