package com.example.powerhouseelectronics;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class CustomConfirmAlert {
    public interface OnDialogCloseListener {
        void onDialogClose();
    }

    private static OnDialogCloseListener dialogCloseListener;

    public static void showCustomClearCartDialog(Context context, final OnDialogCloseListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);
        layout.setScaleX(0.8f);
        layout.setScaleY(0.8f);
        layout.setAlpha(0.0f);
        layout.setBackground(context.getResources().getDrawable(R.drawable.custom_alert_background));

        TextView titleView = new TextView(context);
        titleView.setText("Confirmación");
        titleView.setTextColor(context.getResources().getColor(R.color.customAlertTitle));
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        titleView.setPadding(0, 0, 0, 10);
        titleView.setGravity(Gravity.CENTER);
        layout.addView(titleView);

        TextView messageView = new TextView(context);
        messageView.setText("¿Estás seguro de realizar esta accion?");
        messageView.setTextColor(context.getResources().getColor(R.color.customAlertMessage));
        messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        messageView.setPadding(0, 0, 0, 20);
        messageView.setGravity(Gravity.CENTER);
        layout.addView(messageView);

        Button confirmButton = new Button(context);
        confirmButton.setText("Aceptar");
        confirmButton.setBackgroundResource(R.drawable.rounded_button);
        confirmButton.setTextColor(Color.WHITE);

        Button cancelButton = new Button(context);
        cancelButton.setText("Cancelar");
        cancelButton.setBackgroundResource(R.drawable.rounded_button);
        cancelButton.setTextColor(Color.WHITE);


        AlertDialog dialog = builder.setView(layout).create();

        AnimatorSet appearingAnimation = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(layout, View.SCALE_X, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(layout, View.SCALE_Y, 1.0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(layout, View.ALPHA, 1.0f);

        appearingAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        appearingAnimation.setDuration(300);
        appearingAnimation.playTogether(scaleX, scaleY, fadeIn);
        appearingAnimation.start();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet closingAnimation = new AnimatorSet();
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(layout, View.SCALE_X, 0.8f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(layout, View.SCALE_Y, 0.8f);
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(layout, View.ALPHA, 0.0f);

                closingAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                closingAnimation.setDuration(300);
                closingAnimation.playTogether(scaleX, scaleY, fadeOut);
                closingAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onDialogClose();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                closingAnimation.start();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet closingAnimation = new AnimatorSet();
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(layout, View.SCALE_X, 0.8f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(layout, View.SCALE_Y, 0.8f);
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(layout, View.ALPHA, 0.0f);

                closingAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                closingAnimation.setDuration(300);
                closingAnimation.playTogether(scaleX, scaleY, fadeOut);
                closingAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                closingAnimation.start();
            }
        });

        layout.addView(confirmButton);
        layout.addView(cancelButton);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
