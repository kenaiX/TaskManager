package com.meizu.taskmanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.SnapshotArray;
import com.google.common.eventbus.Subscribe;
import com.meizu.taskmanager.ui.HorizontalGroup;
import com.meizu.taskmanager.utils.action.CancelableAction;
import com.meizu.taskmanager.utils.animation.FloatEvaluator;
import com.meizu.taskmanager.utils.eventbus.EventBusUtil;


public class PagerStage extends Stage {

    public final static class UpdateRectEvent {
    }

    public final static class Rect {
        public final float x;
        public final float y;
        public final float width;
        public final float height;
        public final float center;

        public Rect(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.center = x + width / 2;
        }

        @Override
        public String toString() {
            return "Rect{" +
                    "center=" + center +
                    '}';
        }
    }

    public GestureDetector.GestureListener flickScrollListener;

    private Rect[] mRect;
    private float mCameraPosition;
    private HorizontalGroup mGroup;
    private CancelableAction mMoveCameraAction;

    public PagerStage() {
        super();
        mCameraPosition = getCamera().position.x;
        flickScrollListener = new GestureDetector.GestureListener() {
            short focusThis;

            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                focusThis = 0;
                cancelMoveIfNeed();
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                if (focusThis == 1) {
                    addAction(new ScrollCameraAction(0.5f, Interpolation.linear).setToPotion(calm(mCameraPosition - velocityX)));
                    return true;
                }
                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                if (focusThis == 0) {
                    if (Math.abs(deltaX) > Math.abs(deltaY)) {
                        focusThis = 1;
                        cancelTouchFocus();
                        return true;
                    } else {
                        focusThis = -1;
                        return false;
                    }

                } else if (focusThis == 1) {
                    addAction(new MoveCameraAction().setOffset(-deltaX));
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                if (focusThis == 1) {
                    addAction(new ScrollCameraAction(0.2f, Interpolation.linear).setToPotion(calm(mCameraPosition)));
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }
        };

        EventBusUtil.getDefaultEventBus().register(this);

    }

    @Override
    public void dispose() {
        super.dispose();
        EventBusUtil.getDefaultEventBus().unregister(this);
    }

    public void bindGroup(HorizontalGroup group) {
        mGroup = group;
    }

    public float calm(float toPotion) {
        float reFloat = 0;
        float distance = Float.MAX_VALUE;
        for (int i = 0, n = mRect.length; i < n; i++) {
            final float dis = Math.abs(mRect[i].center - toPotion);
            if (dis < distance) {
                distance = dis;
                reFloat = mRect[i].center;
            }
        }
        return reFloat;
    }

    @Subscribe
    public void updateRect(UpdateRectEvent event) {
        updateRect();
    }

    public void updateRect() {
        SnapshotArray<Actor> children = mGroup.getChildren();
        PagerStage.Rect[] rects = new PagerStage.Rect[children.size];
        for (int i = 0, n = rects.length; i < n; i++) {
            Actor actor = children.get(i);

            rects[i] = new PagerStage.Rect(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
        }
        mRect = rects;


        float calm = calm(mCameraPosition);
        if (calm != mCameraPosition)
            addAction(new ScrollCameraAction(0.2f, Interpolation.linear).setToPotion(calm));
    }

    public void init() {
        addAction(new MoveCameraAction().setOffset(mRect[0].center - mCameraPosition));
    }

    private void updateCameraToPotion(float toPotion) {
        mCameraPosition = toPotion;
        getCamera().position.set(mCameraPosition, Gdx.graphics.getHeight() / 2, getCamera().position.z);
        getCamera().update();
    }

    private void updateCameraOffset(float x) {
        mCameraPosition += x;
        getCamera().position.set(mCameraPosition, Gdx.graphics.getHeight() / 2, getCamera().position.z);
        getCamera().update();
    }


    private void cancelMoveIfNeed(){
        if (mMoveCameraAction != null) {
            mMoveCameraAction.cancel();
        }
    }

    private class MoveCameraAction extends Action {

        private float offset;

        public MoveCameraAction() {
            super();
            cancelMoveIfNeed();
        }

        public MoveCameraAction setOffset(float offset) {
            this.offset = offset;
            return this;
        }

        @Override
        public boolean act(float delta) {
            updateCameraOffset(offset);
            return true;
        }

    }

    private class ScrollCameraAction extends TemporalAction implements CancelableAction {

        FloatEvaluator floatEvaluator = new FloatEvaluator();

        @Override
        protected void end() {
            if (mMoveCameraAction == this) mMoveCameraAction = null;
        }

        private boolean alive = true;

        private float toPotion;

        private float originPotion;

        ScrollCameraAction(float duration, Interpolation interpolation) {
            super(duration, interpolation);
            cancelMoveIfNeed();
            mMoveCameraAction = this;
        }


        public ScrollCameraAction setToPotion(float toPotion) {
            this.originPotion = mCameraPosition;
            this.toPotion = toPotion;
            return this;
        }


        @Override
        protected void update(float percent) {
            if (!alive) return;
            updateCameraToPotion(floatEvaluator.evaluate(percent, originPotion, toPotion));
        }

        @Override
        public void cancel() {
            alive = false;
        }
    }
}
