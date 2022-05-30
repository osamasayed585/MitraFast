package com.visionstech.whatsappview;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.visionstech.whatsappview.databinding.WhatsAppViewBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Varun John on 4 Dec, 2018
 * Github : https://github.com/varunjohn
 */
public class WhatsAppView {


    boolean isLayoutDirectionRightToLeft;
    int screenWidth, screenHeight;
    WhatsAppViewBinding binding;
    private String TAG = "WhatsAppViewView";


    private Animation animBlink, animJump, animJumpFast;

    private boolean isDeleting;
    private boolean stopTrackingAction;
    private Handler handler;

    private int audioTotalTime;
    private TimerTask timerTask;
    private Timer audioTimer;
    private SimpleDateFormat timeFormatter;

    private float lastX, lastY;
    private float firstX, firstY;

    private float directionOffset, cancelOffset, lockOffset;
    private float dp = 0;
    private boolean isLocked = false;

    private UserBehaviour userBehaviour = UserBehaviour.NONE;
    private RecordingListener recordingListener;
    private ReplayListener replayListener;


    private List<AttachmentOption> attachmentOptionList;
    private AttachmentOptionsListener attachmentOptionsListener;
    private List<LinearLayout> layoutAttachments;
    private Context context;
    private boolean showCameraIcon = true, showAttachmentIcon = true, showEmojiIcon = true;
    private boolean removeAttachmentOptionAnimation;

    public void initView(ViewGroup view) {

        if (view == null) {
            showErrorLog("initView ViewGroup can't be NULL");
            return;
        }

        context = view.getContext();

        view.removeAllViews();

        binding = WhatsAppViewBinding.inflate(LayoutInflater.from(view.getContext()));

        view.addView(binding.getRoot());

        timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());

        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        isLayoutDirectionRightToLeft = view.getContext().getResources().getBoolean(R.bool.is_right_to_left);


        handler = new Handler(Looper.getMainLooper());

        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, view.getContext().getResources().getDisplayMetrics());

        animBlink = AnimationUtils.loadAnimation(view.getContext(),
                R.anim.blink);
        animJump = AnimationUtils.loadAnimation(view.getContext(),
                R.anim.jump);
        animJumpFast = AnimationUtils.loadAnimation(view.getContext(),
                R.anim.jump_fast);

        setupRecording();
        setupAttachmentOptions();

        binding.deleteReply.setOnClickListener(this::onDeleteReplyClick);


    }


    public void changeSlideToCancelText(int textResourceId) {
        binding.textViewSlide.setText(textResourceId);
    }

    public boolean isShowCameraIcon() {
        return showCameraIcon;
    }

    public void showCameraIcon(boolean showCameraIcon) {
        this.showCameraIcon = showCameraIcon;

        if (showCameraIcon) {
            binding.imageViewCamera.setVisibility(View.VISIBLE);
        } else {
            binding.imageViewCamera.setVisibility(View.GONE);
        }
    }

    public boolean isShowAttachmentIcon() {
        return showAttachmentIcon;
    }

    public void showAttachmentIcon(boolean showAttachmentIcon) {
        this.showAttachmentIcon = showAttachmentIcon;

        if (showAttachmentIcon) {
            binding.imageViewAttachment.setVisibility(View.VISIBLE);
        } else {
            binding.imageViewAttachment.setVisibility(View.INVISIBLE);
        }
    }

    public boolean isShowEmojiIcon() {
        return showEmojiIcon;
    }

    public void showEmojiIcon(boolean showEmojiIcon) {
        this.showEmojiIcon = showEmojiIcon;

        if (showEmojiIcon) {
            binding.imageViewEmoji.setVisibility(View.VISIBLE);
        } else {
            binding.imageViewEmoji.setVisibility(View.INVISIBLE);
        }
    }

    public void setAttachmentOptions(List<AttachmentOption> attachmentOptionList, final AttachmentOptionsListener attachmentOptionsListener) {

        this.attachmentOptionList = attachmentOptionList;
        this.attachmentOptionsListener = attachmentOptionsListener;

        if (this.attachmentOptionList != null && !this.attachmentOptionList.isEmpty()) {
            binding.layoutAttachmentOptions.removeAllViews();
            int count = 0;
            LinearLayout linearLayoutMain = null;
            layoutAttachments = new ArrayList<>();

            for (final AttachmentOption attachmentOption : this.attachmentOptionList) {

                if (count == 6) {
                    break;
                }

                if (count == 0 || count == 3) {
                    linearLayoutMain = new LinearLayout(context);
                    linearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayoutMain.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayoutMain.setGravity(Gravity.CENTER);

                    binding.layoutAttachmentOptions.addView(linearLayoutMain);
                }

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams((int) (dp * 84), LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setPadding((int) (dp * 4), (int) (dp * 12), (int) (dp * 4), (int) (dp * 0));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);

                layoutAttachments.add(linearLayout);

                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(new LinearLayout.LayoutParams((int) (dp * 48), (int) (dp * 48)));
                imageView.setImageResource(attachmentOption.getResourceImage());

                TextView textView = new TextView(context);
                TextViewCompat.setTextAppearance(textView, R.style.TextAttachmentOptions);
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                textView.setPadding((int) (dp * 4), (int) (dp * 4), (int) (dp * 4), (int) (dp * 0));
                textView.setMaxLines(1);
                textView.setText(attachmentOption.getTitle());

                linearLayout.addView(imageView);
                linearLayout.addView(textView);

                linearLayoutMain.addView(linearLayout);

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideAttachmentOptionView();
                        WhatsAppView.this.attachmentOptionsListener.onClick(attachmentOption);
                    }
                });

                count++;
            }
        }
    }

    public void hideAttachmentOptionView() {
        if (binding.layoutAttachment.getVisibility() == View.VISIBLE) {
            binding.imageViewAttachment.performClick();
        }
    }

    public void showAttachmentOptionView() {
        if (binding.layoutAttachment.getVisibility() != View.VISIBLE) {
            binding.imageViewAttachment.performClick();
        }
    }

    private void setupAttachmentOptions() {
        binding.imageViewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.layoutAttachment.getVisibility() == View.VISIBLE) {
                    int x = isLayoutDirectionRightToLeft ? (int) (dp * (18 + 40 + 4 + 56)) : (int) (screenWidth - (dp * (18 + 40 + 4 + 56)));
                    int y = (int) (dp * 220);

                    int startRadius = 0;
                    int endRadius = (int) Math.hypot(screenWidth - (dp * (8 + 8)), (dp * 220));

                    Animator anim = ViewAnimationUtils.createCircularReveal(binding.layoutAttachment, x, y, endRadius, startRadius);
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            binding.layoutAttachment.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    anim.start();

                } else {

                    if (!removeAttachmentOptionAnimation) {
                        int count = 0;
                        if (layoutAttachments != null && !layoutAttachments.isEmpty()) {

                            int[] arr = new int[]{5, 4, 2, 3, 1, 0};

                            if (isLayoutDirectionRightToLeft) {
                                arr = new int[]{3, 4, 0, 5, 1, 2};
                            }

                            for (int i = 0; i < layoutAttachments.size(); i++) {
                                if (arr[i] < layoutAttachments.size()) {
                                    final LinearLayout layout = layoutAttachments.get(arr[i]);
                                    layout.setScaleX(0.4f);
                                    layout.setAlpha(0f);
                                    layout.setScaleY(0.4f);
                                    layout.setTranslationY(dp * 48 * 2);
                                    layout.setVisibility(View.INVISIBLE);

                                    layout.animate().scaleX(1f).scaleY(1f).alpha(1f).translationY(0).setStartDelay((count * 25) + 50).setDuration(300).setInterpolator(new OvershootInterpolator()).start();
                                    layout.setVisibility(View.VISIBLE);

                                    count++;
                                }
                            }
                        }
                    }

                    int x = isLayoutDirectionRightToLeft ? (int) (dp * (18 + 40 + 4 + 56)) : (int) (screenWidth - (dp * (18 + 40 + 4 + 56)));
                    int y = (int) (dp * 220);

                    int startRadius = 0;
                    int endRadius = (int) Math.hypot(screenWidth - (dp * (8 + 8)), (dp * 220));

                    Animator anim = ViewAnimationUtils.createCircularReveal(binding.layoutAttachment, x, y, startRadius, endRadius);
                    anim.setDuration(500);
                    binding.layoutAttachment.setVisibility(View.VISIBLE);
                    anim.start();
                }
            }
        });
    }

    public void removeAttachmentOptionAnimation(boolean removeAttachmentOptionAnimation) {
        this.removeAttachmentOptionAnimation = removeAttachmentOptionAnimation;
    }

    public void setAudioRecordButtonImage(int imageResource) {
        binding.imageAudio.setImageResource(imageResource);
    }

    public void setStopButtonImage(int imageResource) {
        binding.imageStop.setImageResource(imageResource);
    }

    public void setSendButtonImage(int imageResource) {
        binding.imageSend.setImageResource(imageResource);
    }

    //    public View setContainerView(int layoutResourceID) {
//        View view = LayoutInflater.from(viewContainer.getContext()).inflate(layoutResourceID, null);
//
//        if (view == null) {
//            showErrorLog("Unable to create the Container View from the layoutResourceID");
//            return null;
//        }
//
//        viewContainer.removeAllViews();
//        viewContainer.addView(view);
//        return view;
//    }

    public RecordingListener getRecordingListener() {
        return recordingListener;
    }

    public void setRecordingListener(RecordingListener recordingListener) {
        this.recordingListener = recordingListener;
    }

    public View getSendView() {
        return binding.imageViewSend;
    }

    public View getAttachmentView() {
        return binding.imageViewAttachment;
    }

    public View getEmojiView() {
        return binding.imageViewEmoji;
    }

    public View getCameraView() {
        return binding.imageViewCamera;
    }

    public EditText getMessageView() {
        return binding.editTextMessage;
    }

    public RecyclerView getChatRecyclerView() {
        return binding.chat;
    }

    private void setupRecording() {

        binding.imageViewSend.animate().scaleX(0f).scaleY(0f).setDuration(100).setInterpolator(new LinearInterpolator()).start();

        binding.editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    if (binding.imageViewSend.getVisibility() != View.GONE) {
                        binding.imageViewSend.setVisibility(View.GONE);
                        binding.imageViewSend.animate().scaleX(0f).scaleY(0f).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                    }

                    if (showCameraIcon) {
                        if (binding.imageViewCamera.getVisibility() != View.VISIBLE && !isLocked) {
                            binding.imageViewCamera.setVisibility(View.VISIBLE);
                            binding.imageViewCamera.animate().scaleX(1f).scaleY(1f).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                        }
                    }

                } else {
                    if (binding.imageViewSend.getVisibility() != View.VISIBLE && !isLocked) {
                        binding.imageViewSend.setVisibility(View.VISIBLE);
                        binding.imageViewSend.animate().scaleX(1f).scaleY(1f).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                    }

                    if (showCameraIcon) {
                        if (binding.imageViewCamera.getVisibility() != View.GONE) {
                            binding.imageViewCamera.setVisibility(View.GONE);
                            binding.imageViewCamera.animate().scaleX(0f).scaleY(0f).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                        }
                    }
                }
            }
        });

        binding.imageViewAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (isDeleting) {
                    return true;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    cancelOffset = (float) (screenWidth / 2.8);
                    lockOffset = (float) (screenWidth / 2.5);

                    if (firstX == 0) {
                        firstX = motionEvent.getRawX();
                    }

                    if (firstY == 0) {
                        firstY = motionEvent.getRawY();
                    }

                    startRecord();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        stopRecording(RecordingBehaviour.RELEASED);
                    }

                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                    if (stopTrackingAction) {
                        return true;
                    }

                    UserBehaviour direction = UserBehaviour.NONE;

                    float motionX = Math.abs(firstX - motionEvent.getRawX());
                    float motionY = Math.abs(firstY - motionEvent.getRawY());

                    if (isLayoutDirectionRightToLeft ? (motionX > directionOffset && lastX > firstX && lastY > firstY) : (motionX > directionOffset && lastX < firstX && lastY < firstY)) {

                        if (isLayoutDirectionRightToLeft ? (motionX > motionY && lastX > firstX) : (motionX > motionY && lastX < firstX)) {
                            direction = UserBehaviour.CANCELING;

                        } else if (motionY > motionX && lastY < firstY) {
                            direction = UserBehaviour.LOCKING;
                        }

                    } else if (isLayoutDirectionRightToLeft ? (motionX > motionY && motionX > directionOffset && lastX > firstX) : (motionX > motionY && motionX > directionOffset && lastX < firstX)) {
                        direction = UserBehaviour.CANCELING;
                    } else if (motionY > motionX && motionY > directionOffset && lastY < firstY) {
                        direction = UserBehaviour.LOCKING;
                    }

                    if (direction == UserBehaviour.CANCELING) {
                        if (userBehaviour == UserBehaviour.NONE || motionEvent.getRawY() + binding.imageViewAudio.getWidth() / 2 > firstY) {
                            userBehaviour = UserBehaviour.CANCELING;
                        }

                        if (userBehaviour == UserBehaviour.CANCELING) {
                            translateX(-(firstX - motionEvent.getRawX()));
                        }
                    } else if (direction == UserBehaviour.LOCKING) {
                        if (userBehaviour == UserBehaviour.NONE || motionEvent.getRawX() + binding.imageViewAudio.getWidth() / 2 > firstX) {
                            userBehaviour = UserBehaviour.LOCKING;
                        }

                        if (userBehaviour == UserBehaviour.LOCKING) {
                            translateY(-(firstY - motionEvent.getRawY()));
                        }
                    }

                    lastX = motionEvent.getRawX();
                    lastY = motionEvent.getRawY();
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });

        binding.imageViewStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocked = false;
                stopRecording(RecordingBehaviour.LOCK_DONE);
            }
        });
    }

    private void translateY(float y) {
        if (y < -lockOffset) {
            locked();
            binding.imageViewAudio.setTranslationY(0);
            return;
        }

        if (binding.layoutLock.getVisibility() != View.VISIBLE) {
            binding.layoutLock.setVisibility(View.VISIBLE);
        }

        binding.imageViewAudio.setTranslationY(y);
        binding.layoutLock.setTranslationY(y / 2);
        binding.imageViewAudio.setTranslationX(0);
    }

    private void translateX(float x) {

        if (isLayoutDirectionRightToLeft ? x > cancelOffset : x < -cancelOffset) {
            canceled();
            binding.imageViewAudio.setTranslationX(0);
            binding.layoutSlideCancel.setTranslationX(0);
            return;
        }

        binding.imageViewAudio.setTranslationX(x);
        binding.layoutSlideCancel.setTranslationX(x);
        binding.layoutLock.setTranslationY(0);
        binding.imageViewAudio.setTranslationY(0);

        if (Math.abs(x) < binding.imageViewMic.getWidth() / 2) {
            if (binding.layoutLock.getVisibility() != View.VISIBLE) {
                binding.layoutLock.setVisibility(View.VISIBLE);
            }
        } else {
            if (binding.layoutLock.getVisibility() != View.GONE) {
                binding.layoutLock.setVisibility(View.GONE);
            }
        }
    }

    private void locked() {
        stopTrackingAction = true;
        stopRecording(RecordingBehaviour.LOCKED);
        isLocked = true;
    }

    private void canceled() {
        stopTrackingAction = true;
        stopRecording(RecordingBehaviour.CANCELED);
    }

    private void stopRecording(RecordingBehaviour recordingBehaviour) {

        stopTrackingAction = true;
        firstX = 0;
        firstY = 0;
        lastX = 0;
        lastY = 0;

        userBehaviour = UserBehaviour.NONE;

        binding.imageViewAudio.animate().scaleX(1f).scaleY(1f).translationX(0).translationY(0).setDuration(100).setInterpolator(new LinearInterpolator()).start();
        binding.layoutSlideCancel.setTranslationX(0);
        binding.layoutSlideCancel.setVisibility(View.GONE);

        binding.layoutLock.setVisibility(View.GONE);
        binding.layoutLock.setTranslationY(0);
        binding.imageViewLockArrow.clearAnimation();
        binding.imageViewLock.clearAnimation();

        if (isLocked) {
            return;
        }

        if (recordingBehaviour == RecordingBehaviour.LOCKED) {
            binding.imageViewStop.setVisibility(View.VISIBLE);

            if (recordingListener != null)
                recordingListener.onRecordingLocked();

        } else if (recordingBehaviour == RecordingBehaviour.CANCELED) {
            binding.textViewTime.clearAnimation();
            binding.textViewTime.setVisibility(View.INVISIBLE);
            binding.imageViewMic.setVisibility(View.INVISIBLE);
            binding.imageViewStop.setVisibility(View.GONE);
            binding.layoutEffect2.setVisibility(View.GONE);
            binding.layoutEffect1.setVisibility(View.GONE);

            timerTask.cancel();
            delete();

            if (recordingListener != null)
                recordingListener.onRecordingCanceled();

        } else if (recordingBehaviour == RecordingBehaviour.RELEASED || recordingBehaviour == RecordingBehaviour.LOCK_DONE) {
            binding.textViewTime.clearAnimation();
            binding.textViewTime.setVisibility(View.INVISIBLE);
            binding.imageViewMic.setVisibility(View.INVISIBLE);
            binding.editTextMessage.setVisibility(View.VISIBLE);
            if (showAttachmentIcon) {
                binding.imageViewAttachment.setVisibility(View.VISIBLE);
            }
            if (showCameraIcon) {
                binding.imageViewCamera.setVisibility(View.VISIBLE);
            }
            if (showEmojiIcon) {
                binding.imageViewEmoji.setVisibility(View.VISIBLE);
            }
            binding.imageViewStop.setVisibility(View.GONE);
            binding.editTextMessage.requestFocus();
            binding.layoutEffect2.setVisibility(View.GONE);
            binding.layoutEffect1.setVisibility(View.GONE);

            timerTask.cancel();

            if (recordingListener != null)
                recordingListener.onRecordingCompleted(audioTotalTime);
        }
    }

    private void startRecord() {
        if (recordingListener != null)
            recordingListener.onRecordingStarted();

        hideAttachmentOptionView();

        stopTrackingAction = false;
        binding.editTextMessage.setVisibility(View.INVISIBLE);
        binding.imageViewAttachment.setVisibility(View.INVISIBLE);
        binding.imageViewCamera.setVisibility(View.INVISIBLE);
        binding.imageViewEmoji.setVisibility(View.INVISIBLE);
        binding.imageViewAudio.animate().scaleXBy(1f).scaleYBy(1f).setDuration(200).setInterpolator(new OvershootInterpolator()).start();
        binding.textViewTime.setVisibility(View.VISIBLE);
        binding.layoutLock.setVisibility(View.VISIBLE);
        binding.layoutSlideCancel.setVisibility(View.VISIBLE);
        binding.imageViewMic.setVisibility(View.VISIBLE);
        binding.layoutEffect2.setVisibility(View.VISIBLE);
        binding.layoutEffect1.setVisibility(View.VISIBLE);

        binding.textViewTime.startAnimation(animBlink);
        binding.imageViewLockArrow.clearAnimation();
        binding.imageViewLock.clearAnimation();
        binding.imageViewLockArrow.startAnimation(animJumpFast);
        binding.imageViewLock.startAnimation(animJump);

        if (audioTimer == null) {
            audioTimer = new Timer();
            timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.textViewTime.setText(timeFormatter.format(new Date(audioTotalTime * 1000)));
                        audioTotalTime++;
                    }
                });
            }
        };

        audioTotalTime = 0;
        audioTimer.schedule(timerTask, 0, 1000);
    }

    private void delete() {
        binding.imageViewMic.setVisibility(View.VISIBLE);
        binding.imageViewMic.setRotation(0);
        isDeleting = true;
        binding.imageViewAudio.setEnabled(false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isDeleting = false;
                binding.imageViewAudio.setEnabled(true);

                if (showAttachmentIcon) {
                    binding.imageViewAttachment.setVisibility(View.VISIBLE);
                }
                if (showCameraIcon) {
                    binding.imageViewCamera.setVisibility(View.VISIBLE);
                }
                if (showEmojiIcon) {
                    binding.imageViewEmoji.setVisibility(View.VISIBLE);
                }
            }
        }, 1250);

        binding.imageViewMic.animate().translationY(-dp * 150).rotation(180).scaleXBy(0.6f).scaleYBy(0.6f).setDuration(500).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

                float displacement = 0;

                if (isLayoutDirectionRightToLeft) {
                    displacement = dp * 40;
                } else {
                    displacement = -dp * 40;
                }

                binding.dustin.setTranslationX(displacement);
                binding.dustinCover.setTranslationX(displacement);

                binding.dustinCover.animate().translationX(0).rotation(-120).setDuration(350).setInterpolator(new DecelerateInterpolator()).start();

                binding.dustin.animate().translationX(0).setDuration(350).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        binding.dustin.setVisibility(View.VISIBLE);
                        binding.dustinCover.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                binding.imageViewMic.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).setInterpolator(new LinearInterpolator()).setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.imageViewMic.setVisibility(View.INVISIBLE);
                                binding.imageViewMic.setRotation(0);

                                float displacement = 0;

                                if (isLayoutDirectionRightToLeft) {
                                    displacement = dp * 40;
                                } else {
                                    displacement = -dp * 40;
                                }

                                binding.dustinCover.animate().rotation(0).setDuration(150).setStartDelay(50).start();
                                binding.dustin.animate().translationX(displacement).setDuration(200).setStartDelay(250).setInterpolator(new DecelerateInterpolator()).start();
                                binding.dustinCover.animate().translationX(displacement).setDuration(200).setStartDelay(250).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        binding.editTextMessage.setVisibility(View.VISIBLE);
                                        binding.editTextMessage.requestFocus();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                }).start();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }
                ).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void showErrorLog(String s) {
        Log.e(TAG, s);
    }

    public void showReplayContainer(String message, boolean isText, boolean isImage, boolean isVoice) {
        binding.replyContainer.setVisibility(View.VISIBLE);
        binding.replayImage.setVisibility(View.GONE);
        if (isText) {
            binding.replayContent.setText(message);
        } else if (isImage) {
            binding.replayImage.setVisibility(View.VISIBLE);
            binding.replayContent.setText(context.getString(R.string.photo));
            Glide.with(context).load(message).placeholder(R.drawable.main_logo).error(R.drawable.main_logo).into(binding.replayImage);
        } else if (isVoice) {
            binding.replayContent.setText(context.getString(R.string.voice_message));
        }
    }

    private void onDeleteReplyClick(View view) {
        deleteReply();
        if (replayListener != null)
            replayListener.onReplyCanceled();

    }

    public void deleteReply() {
        binding.replyContainer.setVisibility(View.GONE);
        binding.replayContent.setText("");

    }

    public void setMessageSwipeController(MessageSwipeController messageSwipeController) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(messageSwipeController);
        itemTouchHelper.attachToRecyclerView(binding.chat);
    }

    public ReplayListener getReplayListener() {
        return replayListener;
    }

    public void setReplayListener(ReplayListener replayListener) {
        this.replayListener = replayListener;
    }

    public enum UserBehaviour {
        CANCELING,
        LOCKING,
        NONE
    }

    public enum RecordingBehaviour {
        CANCELED,
        LOCKED,
        LOCK_DONE,
        RELEASED
    }

    public interface RecordingListener {

        void onRecordingStarted();

        void onRecordingLocked();

        void onRecordingCompleted(int recordTime);

        void onRecordingCanceled();

    }

    public interface ReplayListener {

        void onReplyCanceled();

    }
}
