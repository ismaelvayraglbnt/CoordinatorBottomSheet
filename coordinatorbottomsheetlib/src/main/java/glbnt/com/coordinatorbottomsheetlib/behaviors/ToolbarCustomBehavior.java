package glbnt.com.coordinatorbottomsheetlib.behaviors;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import glbnt.com.coordinatorbottomsheetlib.R;
import glbnt.com.coordinatorbottomsheetlib.utils.BottomSheetUtils;
import glbnt.com.coordinatorbottomsheetlib.views.BottomCollapsibleActionBar;
import glbnt.com.coordinatorbottomsheetlib.views.BottomToolbar;

/**
 * Created by ismaelvayra on 11/12/15.
 */
@SuppressWarnings("unused")
public class ToolbarCustomBehavior extends AppBarLayout.ScrollingViewBehavior {

    private Context ctx;
    private float screenSizeHeight;
    private boolean mNestedScrollStarted = false;
    private float startPoint;
    private float endPoint;
    private ValueAnimator mAnimator;
    private float parallaxTranslation;
    private float finalHeightToolbar;
    private float initialHeightToolbar;

    public ToolbarCustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        initBehavior();
    }

    private void initBehavior() {
        screenSizeHeight = ctx.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {

        boolean isBottomCollapsibleChild = dependency instanceof BottomCollapsibleActionBar;
        if (isBottomCollapsibleChild && (startPoint == 0 || endPoint == 0)) {
            BottomCollapsibleActionBar appBar = (BottomCollapsibleActionBar) dependency;
            BottomToolbar toolbar = (BottomToolbar) child.findViewById(R.id.toolbar_bottom_panel);
            FrameLayout toolbarContainer = (FrameLayout) child.findViewById(R.id.toolbar_container);
            startPoint = appBar.getAnchorPoint();
            endPoint = appBar.getEndAnimationPoint();
            parallaxTranslation = toolbar.getParallaxTranslation();
            finalHeightToolbar = toolbar.getFinalHeight();
            initialHeightToolbar = ((FrameLayout.LayoutParams) toolbar.getLayoutParams()).height;

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbarContainer.getLayoutParams();
            params.height = (int) initialHeightToolbar;
            toolbarContainer.setLayoutParams(params);
        }

        return isBottomCollapsibleChild || (dependency instanceof FrameLayout);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof BottomCollapsibleActionBar) {
            float dependencyY = Math.abs(dependency.getY());
            View fakeToolbar = child.findViewById(R.id.fake_toolbar);
            fakeToolbar.setAlpha(BottomSheetUtils.getScaledAlpha(dependencyY, startPoint, endPoint));
            View bottomToolbar = child.findViewById(R.id.toolbar_bottom_panel);

            if (parallaxTranslation != 0) {
                float toolbarTranslation = BottomSheetUtils.getParallaxPosition(dependencyY, startPoint, endPoint,
                        parallaxTranslation, bottomToolbar.getTranslationY());
                bottomToolbar.setTranslationY(toolbarTranslation);
                fakeToolbar.setTranslationY(toolbarTranslation);

                View toolbarContainer = child.findViewById(R.id.toolbar_container);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbarContainer.getLayoutParams();
                params.height = (int) (initialHeightToolbar - toolbarTranslation);
                toolbarContainer.setLayoutParams(params);

            }

            if (finalHeightToolbar != 0) {
                FrameLayout.LayoutParams bottomToolbarLp = (FrameLayout.LayoutParams) bottomToolbar.getLayoutParams();
                FrameLayout.LayoutParams fakeToolbarLp = (FrameLayout.LayoutParams) fakeToolbar.getLayoutParams();
                float toolbarHeightFloat = BottomSheetUtils.getScrollingHeight(dependencyY, startPoint, endPoint,
                        finalHeightToolbar, initialHeightToolbar);
                bottomToolbarLp.height = (int) (toolbarHeightFloat);
                fakeToolbarLp.height = (int) (toolbarHeightFloat);

                bottomToolbar.setLayoutParams(bottomToolbarLp);
                fakeToolbar.setLayoutParams(fakeToolbarLp);

                View toolbarContainer = child.findViewById(R.id.toolbar_container);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbarContainer.getLayoutParams();
                params.height += (toolbarHeightFloat - initialHeightToolbar);
                toolbarContainer.setLayoutParams(params);
            }

        }

        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return true;
    }

    //TODO: fix appbar drag
}
