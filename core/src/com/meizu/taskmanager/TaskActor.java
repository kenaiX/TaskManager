package com.meizu.taskmanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.meizu.taskmanager.utils.action.CancelableAction;

public class TaskActor extends Actor {
    private Sprite mSprite;

    private CancelableAction mMoveAction;

    public TaskActor(TaskItem item) {
        addCaptureListener(actorGestureListener);

        final float zoom = Gdx.graphics.getWidth() * 0.6f / item.getScreenShot().getWidth();

        mSprite = new Sprite(item.getScreenShot());
        mSprite.setSize(mSprite.getWidth() * zoom, mSprite.getHeight() * zoom);
        setWidth(mSprite.getWidth());
        setHeight(mSprite.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        mSprite.setPosition(getX(), getY());
        mSprite.draw(batch);
    }

    private ActorGestureListener actorGestureListener = new ActorGestureListener() {
        boolean catchFocus;
        float speed;

        @Override
        public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
            Stage stage = getStage();
            if (!catchFocus && Math.abs(deltaX) < Math.abs(deltaY)) {
                catchFocus = true;
                if (stage != null)
                    stage.cancelTouchFocusExcept(actorGestureListener, TaskActor.this);
            }
            if (catchFocus) {
                MoveByAction moveByAction = new MoveByAction();
                moveByAction.setAmountY(deltaY);
                addAction(moveByAction);
            }
        }

        @Override
        public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
            speed = 0;
            cancelMoveIfNeed();
            if (getOriginY() == 0) {
                setOriginY(getY());
            }
            super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            if ((Math.abs(speed) <= 150 && speed != 0) || (speed == 0 && getY() != getOriginY())) {
                MoveToOringinAction moveToAction = new MoveToOringinAction();
                moveToAction.setPosition(getX(), getOriginY());
                moveToAction.setDuration(0.5f);
                addAction(moveToAction);
            }
            super.touchUp(event, x, y, pointer, button);
        }

        @Override
        public void fling(InputEvent event, float velocityX, float velocityY, int button) {
            speed = velocityY;

            if (Math.abs(speed) > 150) {
                addAction(new RemoveTaskAction());
            } else {
                MoveToOringinAction moveToAction = new MoveToOringinAction();
                moveToAction.setPosition(getX(), getOriginY());
                moveToAction.setDuration(0.3f);
                addAction(moveToAction);
            }
            super.fling(event, velocityX, velocityY, button);
        }
    };


    private void cancelMoveIfNeed(){
        if (mMoveAction != null) {
            mMoveAction.cancel();
        }
    }

    private class MoveToOringinAction extends MoveToAction implements CancelableAction {
        boolean alive = true;

        public MoveToOringinAction() {
            cancelMoveIfNeed();
            mMoveAction = this;
        }

        @Override
        protected void end() {
            if (mMoveAction == this) mMoveAction = null;
        }

        @Override
        protected void update(float percent) {
            if (!alive) return;
            super.update(percent);
        }

        @Override
        public void cancel() {
            alive = false;
        }
    }

    private class RemoveTaskAction extends Action {


        public boolean act(float delta) {
            target.remove();
            return true;
        }

    }
}
