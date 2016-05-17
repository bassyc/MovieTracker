package com.androidessence.lib;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;

/**
 * Animation code for the MaterialSearchView.
 *
 * Created by adammcneilly on 3/28/16.
 */
public class AnimationUtils {
    /**
     * The length, in milliseconds, of a short duration.
     */
    public static final int ANIMATION_DURATION_SHORT = 150;

    /**
     * The length, in milliseconds, of an animation.
     */
    public static final int ANIMATION_DURATION = 400;

    /**
     * The length, in milliseconds, of a long duration.
     */
    public static final int ANIMATION_DURATION_LONG = 800;

    /**
     * Interface that is notified when an animation starts, ends, or is canceled.
     */
    public interface AnimationListener {
        /**
         * Called when an animation begins.
         * @param view The view that is being animated.
         * @return True if the listener overrides this action, false for it to be handled by the parent.
         */
        boolean onAnimationStart(View view);

        /**
         * Called when an animation ends.
         * @param view The view that is being animated.
         * @return True if the listener overrides this action, false for it to be handled by the parent.
         */
        boolean onAnimationEnd(View view);
        /**
         * Called when an animation is canceled.
         * @param view The view that is being animated.
         * @return True if the listener overrides this action, false for it to be handled by the parent.
         */
        boolean onAnimationCancel(View view);
    }

    /**
     * Shows a view and hides a view simultaneously.
     * @param showView The view to be displayed.
     * @param hideView The view to be hidden.
     */
    public static void crossFadeViews(View showView, View hideView) {
        crossFadeViews(showView, hideView, ANIMATION_DURATION);
    }

    /**
     * Shows a view and hides a view simultaneously.
     * @param showView The view to be displayed.
     * @param hideView The view to be hidden.
     * @param duration The length of the animation.
     */
    public static void crossFadeViews(View showView, View hideView, int duration) {
        fadeInView(showView, duration);
        fadeOutView(hideView, duration);
    }

    /**
     * Fades a View into site.
     * @param view The view to fade in.
     * @param duration The length of the animation, in milliseconds.
     */
    public static void fadeInView(View view, int duration) {
        fadeInView(view, duration, null);
    }

    /**
     * Fades a View into site.
     * @param view The view to fade in.
     * @param duration The length of the animation, in milliseconds.
     * @param animationListener A listener to call on the animation.
     */
    public static void fadeInView(View view, int duration, final AnimationListener animationListener) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);

        ViewCompat.animate(view).alpha(1f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                // If listener doesn't handle it,call parent
                if(animationListener != null && !animationListener.onAnimationStart(view)) {
                    view.setDrawingCacheEnabled(true);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                // If listener doesn't handle it,call parent
                if(animationListener != null && !animationListener.onAnimationEnd(view)) {
                    view.setDrawingCacheEnabled(false);
                }
            }

            @Override
            public void onAnimationCancel(View view) {
                // If listener doesn't handle it,call parent
                if(animationListener != null && !animationListener.onAnimationCancel(view)) {

                }
            }
        });
    }

    /**
     * Fades a View out of site.
     * @param view The view to fade out.
     * @param duration The length of the animation, in milliseconds.
     */
    public static void fadeOutView(View view, int duration) {
        fadeOutView(view, duration, null);
    }

    /**
     * Fades a View out of site.
     * @param view The view to fade out.
     * @param duration The length of the animation, in milliseconds.
     * @param animationListener A listener to call on the animation.
     */
    public static void fadeOutView(View view, int duration, final AnimationListener animationListener) {
        ViewCompat.animate(view).alpha(0f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                // If listener is null and not handled, call parent.
                if(animationListener == null || !animationListener.onAnimationStart(view)) {
                    view.setDrawingCacheEnabled(true);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                // If listener is null and not handled, call parent.
                if(animationListener == null || !animationListener.onAnimationEnd(view)) {
                    view.setVisibility(View.GONE);
                    view.setDrawingCacheEnabled(false);
                }
            }

            @Override
            public void onAnimationCancel(View view) {
                // If listener is null and not handled, call parent.
                if(animationListener == null || !animationListener.onAnimationCancel(view)) {

                }
            }
        });
    }
}
