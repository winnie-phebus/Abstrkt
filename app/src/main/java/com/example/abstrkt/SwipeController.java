package com.example.abstrkt;


import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

enum ButtonsState {
    GONE,
    RIGHT_VISIBLE
}

class SwipeController extends ItemTouchHelper.Callback {

    private static final float buttonWidth = 300;
    private final SwipeControllerActions buttonsActions;
    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private RectF deleteButton = null;
    private RectF addFolderButton = null;
    private RectF addToArchiveButton = null;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private RectF addTagButton = null;

    public SwipeController(SwipeControllerActions buttonsActions) {
        this.buttonsActions = buttonsActions;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE)
                    dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
            if (swipeBack) {
                if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;

                if (buttonShowedState != ButtonsState.GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    setItemsClickable(recyclerView, false);
                }
            }
            return false;
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            return false;
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener((v1, event1) -> false);
                setItemsClickable(recyclerView, true);
                swipeBack = false;

                if (buttonsActions != null && checkButtons(event)) {
                    if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                        buttonsActions.onButtonClicked(viewHolder.getAbsoluteAdapterPosition(), findPress(event));
                    }
                }
                buttonShowedState = ButtonsState.GONE;
                currentItemViewHolder = null;
            }
            return false;
        });
    }

    private String findPress(MotionEvent event) {

        String command = "";
        if (buttonClicked(event, deleteButton)) {
            command = Utils.COMM_TRASH;
        } else if (buttonClicked(event, addToArchiveButton)) {
            command = Utils.COMM_ARCHIVE;
        } else if (buttonClicked(event, addTagButton)) {
            command = Utils.COMM_ADDTAG;
        } else if (buttonClicked(event, addFolderButton)) {
            command = Utils.COMM_ADDFOLDER;
        }

        return command;
    }

    private boolean buttonClicked(MotionEvent event, RectF buttonZone) {
        return buttonZone.contains(event.getX(), event.getY());
    }

    private boolean checkButtons(MotionEvent event) {
        return (checkButton(deleteButton, event)
                || checkButton(addFolderButton, event)
                || checkButton(addTagButton, event)
                || checkButton(addToArchiveButton, event));
    }

    private boolean checkButton(RectF buttonInstance, MotionEvent event) {
        return (buttonInstance != null && buttonInstance.contains(event.getX(), event.getY()));
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private void drawButtons(@NonNull Canvas c, RecyclerView.ViewHolder viewHolder) {
        float padding = 15;
        View itemView = viewHolder.itemView;
        float buttonWidthWithoutPadding = buttonWidth - padding;
        float buttonHeight = (itemView.getHeight() / 4) + 5;
        float buttonHeightWithoutPadding = buttonHeight - padding;

        float corners = 16;

        Paint p = new Paint();

/*
        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        p.setColor(Color.BLUE);
        c.drawRoundRect(leftButton, corners, corners, p);
        drawText("EDIT", c, leftButton, p);
*/
        RectF delButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getTop() + buttonHeightWithoutPadding);
        p.setColor(Color.RED);
        c.drawRoundRect(delButton, corners, corners, p);
        drawText("DELETE", c, delButton, p);

        RectF archiveButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop() + buttonHeight, itemView.getRight(), itemView.getTop() + (buttonHeight + buttonHeightWithoutPadding));
        p.setColor(Color.RED);
        c.drawRoundRect(archiveButton, corners, corners, p);
        drawText("ARCHIVE", c, archiveButton, p);

        RectF tagButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding,
                itemView.getTop() + (2 * buttonHeight), itemView.getRight(), itemView.getTop() + (2 * buttonHeight) + buttonHeightWithoutPadding);
        p.setColor(Color.RED);
        c.drawRoundRect(tagButton, corners, corners, p);
        drawText("+ TAG", c, tagButton, p);

        RectF folderButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getBottom() - buttonHeightWithoutPadding, itemView.getRight(), itemView.getBottom());
        p.setColor(Color.RED);
        c.drawRoundRect(folderButton, corners, corners, p);
        drawText("+ FOLDER", c, folderButton, p);


        // setButtonsNull();

        if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            deleteButton = delButton;
            addToArchiveButton = archiveButton;
            addTagButton = tagButton;
            addFolderButton = folderButton;
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), p);
    }

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }
}