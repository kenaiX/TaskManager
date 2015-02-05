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
import com.meizu.taskmanager.ui.HorizontalGroup;
import com.meizu.taskmanager.utils.animation.FloatEvaluator;


public class PagerStage extends Stage {


    public GestureDetector.GestureListener flickScrollListener;

    float mCameraPosition;

    HorizontalGroup mGroup;

    public PagerStage() {
        super();
        Gdx.app.debug("tt", "init");
        mCameraPosition = getCamera().position.x;
        flickScrollListener = new GestureDetector.GestureListener() {
            short focusThis;

            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                focusThis = 0;
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
                    addAction(new ScrollCameraAction(0.5f, Interpolation.linear).setToPotion(calm(mCameraPosition)));
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

    }

    public void bindGroup(HorizontalGroup group) {
        mGroup = group;
    }

    public static class Rect {
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

    private Rect[] mRect;

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
            addAction(new ScrollCameraAction(0.5f, Interpolation.linear).setToPotion(calm));
    }

    public void init() {
        addAction(new MoveCameraAction().setOffset(mRect[0].center - mCameraPosition));
    }

    public void updateCameraToPotion(float toPotion) {
        mCameraPosition = toPotion;
        getCamera().position.set(mCameraPosition, Gdx.graphics.getHeight() / 2, getCamera().position.z);
        getCamera().update();
    }

    public void updateCameraOffset(float x) {
        mCameraPosition += x;
        getCamera().position.set(mCameraPosition, Gdx.graphics.getHeight() / 2, getCamera().position.z);
        getCamera().update();
    }

    class MoveCameraAction extends Action {

        private float offset;

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

    class ScrollCameraAction extends TemporalAction {

        FloatEvaluator floatEvaluator = new FloatEvaluator();

        private float toPotion;

        private float originPotion;

        ScrollCameraAction(float duration, Interpolation interpolation) {
            super(duration, interpolation);
        }


        public ScrollCameraAction setToPotion(float toPotion) {
            this.originPotion = mCameraPosition;
            this.toPotion = toPotion;
            return this;
        }


        @Override
        protected void update(float percent) {
            updateCameraToPotion(floatEvaluator.evaluate(percent, originPotion, toPotion));
        }
    }
}
