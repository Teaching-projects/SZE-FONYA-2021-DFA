package it.slyce.messaging;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;



import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.slyce.messaging.listeners.LoadMoreMessagesListener;
import it.slyce.messaging.listeners.UserClicksAvatarPictureListener;
import it.slyce.messaging.listeners.UserSendsMessageListener;
import it.slyce.messaging.message.MediaMessage;
import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.SpinnerMessage;
import it.slyce.messaging.message.TextMessage;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageRecyclerAdapter;
import it.slyce.messaging.utils.CustomSettings;
import it.slyce.messaging.utils.DateUtils;
import it.slyce.messaging.utils.Refresher;
import it.slyce.messaging.utils.ScrollUtils;
import it.slyce.messaging.utils.asyncTasks.AddNewMessageTask;
import it.slyce.messaging.utils.asyncTasks.ReplaceMessagesTask;
import it.slyce.messaging.view.ViewUtils;

/**
 * Created by John C. Hunchar on 1/12/16.
 */
public class SlyceMessagingFragment extends Fragment implements OnClickListener {

    private static final int START_RELOADING_DATA_AT_SCROLL_VALUE = 5000; // TODO: maybe change this? make it customizable?

    private EditText mEntryField;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Message> mMessages;
    private List<MessageItem> mMessageItems;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private View rootView;
    private ImageView mSendButton;

    private LoadMoreMessagesListener loadMoreMessagesListener;
    private UserSendsMessageListener listener;
    private CustomSettings customSettings;
    private Refresher mRefresher;

    private String defaultAvatarUrl;
    private String defaultDisplayName;
    private String defaultUserId;
    private int startHereWhenUpdate;
    private long recentUpdatedTime;
    private boolean moreMessagesExist;
    private FragmentActivity context;
//    public static boolean otherAvatarEnable = true;
    public static boolean ownAvatarEnable = true;


    public void setSendButton(int drawableResId){
        mSendButton.setImageResource(drawableResId);
    }

    public void setPictureButtonVisible(final boolean bool) {
        if (context != null)
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = (ImageView) rootView.findViewById(R.id.slyce_messaging_image_view_snap);
                    imageView.setVisibility(bool ? View.VISIBLE : View.GONE);
                }
            });
    }

    private void addSpinner() {
        mMessages.add(0, new SpinnerMessage());
        replaceMessages(mMessages, -1);
    }

    private void removeSpinner() {
        if (mMessages == null || mMessages.isEmpty()) return;
        if (mMessages.get(0) instanceof SpinnerMessage) {
            mMessages.remove(0);
            mMessageItems.remove(0);
            mRecyclerAdapter.notifyItemRemoved(0);
        }
    }

    public void setMoreMessagesExist(boolean moreMessagesExist) {
        if (this.moreMessagesExist == moreMessagesExist)
            return;
        this.moreMessagesExist = moreMessagesExist;
        if (moreMessagesExist)
            addSpinner();
        else
            removeSpinner();
        loadMoreMessagesIfNecessary();
    }

    public void setLoadMoreMessagesListener(LoadMoreMessagesListener loadMoreMessagesListener) {
        this.loadMoreMessagesListener = loadMoreMessagesListener;
        loadMoreMessagesIfNecessary();
    }

    public void setUserClicksAvatarPictureListener(UserClicksAvatarPictureListener userClicksAvatarPictureListener) {
        this.customSettings.userClicksAvatarPictureListener = userClicksAvatarPictureListener;
    }

    public void setDefaultAvatarUrl(String defaultAvatarUrl) {
        this.defaultAvatarUrl = defaultAvatarUrl;
    }

    public void setDefaultDisplayName(String defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    public void setDefaultUserId(String defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

//    public void setOtherAvatarEnable(boolean otherAvatarEnable) {this.otherAvatarEnable = otherAvatarEnable;}
    public void setOwnAvatarEnable(boolean ownAvatarEnable) {this.ownAvatarEnable = ownAvatarEnable;}

    public void setStyle(int style) {
        TypedArray ta = context.obtainStyledAttributes(style, R.styleable.SlyceMessagingTheme);
        this.customSettings.backgroudColor = ta.getColor(R.styleable.SlyceMessagingTheme_backgroundColor, Color.GRAY);
        rootView.setBackgroundColor(this.customSettings.backgroudColor); // the background color
        this.customSettings.timestampColor = ta.getColor(R.styleable.SlyceMessagingTheme_timestampTextColor, Color.BLACK);
        this.customSettings.externalBubbleTextColor = ta.getColor(R.styleable.SlyceMessagingTheme_externalBubbleTextColor, Color.WHITE);
        this.customSettings.externalBubbleBackgroundColor = ta.getColor(R.styleable.SlyceMessagingTheme_externalBubbleBackground, Color.WHITE);
        this.customSettings.localBubbleBackgroundColor = ta.getColor(R.styleable.SlyceMessagingTheme_localBubbleBackground, Color.WHITE);
        this.customSettings.localBubbleTextColor = ta.getColor(R.styleable.SlyceMessagingTheme_localBubbleTextColor, Color.WHITE);
        this.customSettings.snackbarBackground = ta.getColor(R.styleable.SlyceMessagingTheme_snackbarBackground, Color.WHITE);
        this.customSettings.snackbarButtonColor = ta.getColor(R.styleable.SlyceMessagingTheme_snackbarButtonColor, Color.WHITE);
        this.customSettings.snackbarTitleColor = ta.getColor(R.styleable.SlyceMessagingTheme_snackbarTitleColor, Color.WHITE);
    }

    public void addNewMessages(List<Message> messages, @Nullable AddNewMessageTask.MessageAdditionCompletedListener listener) {
        mMessages.addAll(messages);
        new AddNewMessageTask(messages, mMessageItems, mRecyclerAdapter, mRecyclerView, context, customSettings, listener).execute();
    }

    public void addNewMessage(Message message) {
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        addNewMessages(messages,null);
    }

    public void setOnSendMessageListener(UserSendsMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = context.getLayoutInflater();
        final View v = inflater.inflate(R.layout.fragment_slyce_messaging, null);
        this.rootView = v;
        this.customSettings = new CustomSettings();

        // Setup views
        mEntryField = (EditText) rootView.findViewById(R.id.slyce_messaging_edit_text_entry_field);
        mSendButton = (ImageView) rootView.findViewById(R.id.slyce_messaging_image_view_send);
        ImageView mSnapButton = (ImageView) rootView.findViewById(R.id.slyce_messaging_image_view_snap);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.slyce_messaging_recycler_view);

        // Add interfaces
        mSendButton.setOnClickListener(this);
        mSnapButton.setOnClickListener(this);
        initScrolling();
        fixRandomScrolling();

        // Init variables for recycler view
        mMessages = new ArrayList<>();
        mMessageItems = new ArrayList<>();
        mRecyclerAdapter = new MessageRecyclerAdapter(mMessageItems, customSettings);
        mLinearLayoutManager = new LinearLayoutManager(this.context.getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return !mRefresher.isRefreshing();
            }

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                }
            }
        };
        mLinearLayoutManager.setStackFromEnd(true);

        // Setup recycler view
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mRefresher.isRefreshing();
                    }
                }
        );

        startUpdateTimestampsThread();
        startHereWhenUpdate = 0;
        recentUpdatedTime = 0;
        mRefresher = new Refresher(false);
        setStyle(R.style.MyTheme);

        loadMoreMessagesIfNecessary();
        startLoadMoreMessagesListener();
//
//        nekünk nem kell, mert csak szüveges üzit küldünk
//        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
//                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                        ContextCompat.checkSelfPermission(context.getApplicationContext(),
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 232);

        return rootView;
    }

    private void startUpdateTimestampsThread() {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (int i = startHereWhenUpdate; i < mMessages.size() && i < mMessageItems.size(); i++) {
                    try {
                        MessageItem messageItem = mMessageItems.get(i);
                        Message message = messageItem.getMessage();
                        if (DateUtils.dateNeedsUpdated(context, message.getDate(), messageItem.getDate())) {
                            messageItem.updateDate(context, message.getDate());
                            updateTimestampAtValue(i);
                        } else if (i == startHereWhenUpdate) {
                            i++;
                        }
                    } catch (Exception exception) {
                        Log.d("debug", exception.getMessage());
                        exception.printStackTrace();
                    }
                }
            }
        }, 0, 62, TimeUnit.SECONDS);
    }

    private void startLoadMoreMessagesListener() {
        if (Build.VERSION.SDK_INT >= 23)
            mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    loadMoreMessagesIfNecessary();
                }
            });
        else
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    loadMoreMessagesIfNecessary();
                }
            });
    }

    private void loadMoreMessagesIfNecessary() {
        if (shouldReloadData()) {
            recentUpdatedTime = new Date().getTime();
            loadMoreMessages();
        }
    }

    private void loadMoreMessages() {
        new AsyncTask<Void, Void, Void>() {
            private boolean spinnerExists;
            private List<Message> messages;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mRefresher.setIsRefreshing(true);
                if (mMessages == null || mMessages.isEmpty()) return;
                spinnerExists = moreMessagesExist && mMessages.get(0) instanceof SpinnerMessage;
                if (spinnerExists) {
                    mMessages.remove(0);
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                messages = loadMoreMessagesListener.loadMoreMessages();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                int upTo = messages.size();
                for (int i = messages.size() - 1; i >= 0; i--) {
                    Message message = messages.get(i);
                    mMessages.add(0, message);
                }
                if (spinnerExists && moreMessagesExist)
                    mMessages.add(0, new SpinnerMessage());
                mRefresher.setIsRefreshing(false);
                replaceMessages(mMessages, upTo);
            }
        }.execute();
    }

    public void replaceMessages(List<Message> messages) {
        replaceMessages(messages, -1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = ((FragmentActivity) context);
    }

    private void replaceMessages(List<Message> messages, int upTo) {
        if (context != null) {
            new ReplaceMessagesTask(messages, mMessageItems, mRecyclerAdapter, context.getApplicationContext(), mRefresher, upTo).execute();
        }
    }

    private boolean shouldReloadData() {
        int scrollOffset = mRecyclerView.computeVerticalScrollOffset();
        if (loadMoreMessagesListener == null || !moreMessagesExist) {
            return false;
        } else {
            return scrollOffset < START_RELOADING_DATA_AT_SCROLL_VALUE &&
                    recentUpdatedTime + 1000 < new Date().getTime();
        }
    }

    private void updateTimestampAtValue(final int i) {
        if (context != null) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecyclerAdapter.notifyItemChanged(i);
                }
            });
        }
    }

    private File file;
    private Uri outputFileUri;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.slyce_messaging_image_view_send) {
            sendUserTextMessage();
        } else if (v.getId() == R.id.slyce_messaging_image_view_snap) {
            mEntryField.setText("");
            final File mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            final File root = new File(mediaStorageDir, "SlyceMessaging");
            root.mkdirs();
            final String fname = "img_" + System.currentTimeMillis() + ".jpg";
            file = new File(root, fname);
            outputFileUri = Uri.fromFile(file);
            //TODO replace camera lib
//            Intent takePhotoIntent = new CameraActivity.IntentBuilder(context.getApplicationContext())
//                    .skipConfirm()
//                    .to(this.file)
//                    .zoomStyle(ZoomStyle.SEEKBAR)
//                    .updateMediaStore()
//                    .build();
//            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            pickPhotoIntent.setType("image/*");
//            Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Take a photo or select one from your device");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {takePhotoIntent});
//            try {
//                startActivityForResult(chooserIntent, 1);
//            } catch (RuntimeException exception) {
//                Log.d("debug", exception.getMessage());
//                exception.printStackTrace();
//            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 232 || (data == null && this.file.exists())) {
            return;
        }
        try {
            if (requestCode == 1 && resultCode == context.RESULT_OK) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera && data != null) { // if there is no picture
                    return;
                }
                if (isCamera || data == null || data.getData() == null) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                MediaMessage message = new MediaMessage();
                message.setUrl(selectedImageUri.toString());
                message.setDate(System.currentTimeMillis());
                message.setDisplayName(this.defaultDisplayName);
                message.setSource(MessageSource.LOCAL_USER);
                message.setAvatarUrl(this.defaultAvatarUrl);
                message.setUserId(this.defaultUserId);
                addNewMessage(message);
                ScrollUtils.scrollToBottomAfterDelay(mRecyclerView, mRecyclerAdapter);
                if (listener != null)
                    listener.onUserSendsMediaMessage(selectedImageUri);
            }
        } catch (RuntimeException exception) {
            Log.d("debug", exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void sendUserTextMessage() {
        String text = ViewUtils.getStringFromEditText(mEntryField);
        if (TextUtils.isEmpty(text))
            return;
        mEntryField.setText("");

        if (!text.replaceAll("\\s+","").equals("")) { //csak whitespace-t nem küldhet
            text = text.trim();

            // Build messageData object
            TextMessage message = new TextMessage();
            message.setDate(System.currentTimeMillis());
            message.setAvatarUrl(defaultAvatarUrl);
            message.setSource(MessageSource.LOCAL_USER);
            message.setDisplayName(defaultDisplayName);
            message.setText(text);
            message.setUserId(defaultUserId);
            addNewMessage(message);

            ScrollUtils.scrollToBottomAfterDelay(mRecyclerView, mRecyclerAdapter);
            if (listener != null)
                listener.onUserSendsTextMessage(message.getText());
        }
    }

    private boolean scrollingToBottom = false;

    public void initScrolling(){
        final View contentView = mRecyclerView;
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    if (!scrollingToBottom) {
                        scrollingToBottom = true;
                        scrollRecyclerViewToBottom(false);
                    }
                }
                else {
                    // keyboard is closed
                    scrollingToBottom = false;
                }
            }
        });
    }

    public void scrollRecyclerViewToBottom(boolean force) {
        if (force){
            RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
            mRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }else{
            RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
            if (adapter != null && adapter.getItemCount() > 0) {
                boolean isAtBottom = mLinearLayoutManager.findLastVisibleItemPosition() > adapter.getItemCount()-7;
                if (isAtBottom){
                    mRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }else{
                    mRecyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                }
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (onStartListener != null){
            onStartListener.onStart();
        }
    }

    private OnStartListener onStartListener;

    public void setOnStartListener(OnStartListener onStartListener) {
        this.onStartListener = onStartListener;
    }

    public interface OnStartListener{
        void onStart();
    }

    private void fixRandomScrolling(){
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.getItemAnimator().setChangeDuration(0);
    }

}
