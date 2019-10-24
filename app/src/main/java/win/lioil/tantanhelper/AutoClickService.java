package win.lioil.tantanhelper;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 辅助服务自动点击
 */
public class AutoClickService extends AccessibilityService {
    private static final String TAG = AutoClickService.class.getSimpleName();
    private static final int DELAY_PAGE = 500; // 页面切换时间
    private static final int DELAY_CLICK = 500; // 自动点击间隔

    private final Handler mHandler = new Handler();
    private final List<AccessibilityNodeInfo> mViewNodes = new ArrayList<>(); // View类节点列表
    private AccessibilityNodeInfo mLoveNode; // "喜欢"节点
    private final Runnable mAutoClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLoveNode != null)
                mLoveNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            mHandler.postDelayed(mAutoClickRunnable, DELAY_CLICK);
        }
    };

    @Override
    protected void onServiceConnected() {
        Log.d(TAG, "onServiceConnected: ");
        performGlobalAction(GLOBAL_ACTION_BACK);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                performGlobalAction(GLOBAL_ACTION_BACK);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AccessibilityUtil.jumpToTanTan(AutoClickService.this);
                    }
                }, DELAY_PAGE);
            }
        }, DELAY_PAGE);

        mHandler.postDelayed(mAutoClickRunnable, DELAY_CLICK);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        AccessibilityUtil.jumpToSetting(this);
        mHandler.removeCallbacks(mAutoClickRunnable);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: ");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo eventNode = event.getSource();
        if (eventNode == null)
            return;
        mViewNodes.clear(); // View类节点清空
        nodeTraversal(eventNode, ""); // 节点遍历
        if (mViewNodes.size() == 5) {
            mLoveNode = mViewNodes.get(3);
//            Log.d(TAG, "mLoveNode is changed");
        }
    }

    private void nodeTraversal(AccessibilityNodeInfo node, String pre) {
        if (node == null)
            return;
//        Log.d(TAG, (pre += "    ") + "nodeTraversal: " + node.getClassName());
        if (node.getClassName().equals("android.view.View") && !mViewNodes.contains(node))
            mViewNodes.add(node);
        for (int i = 0; i < node.getChildCount(); i++)
            nodeTraversal(node.getChild(i), pre);
    }
}