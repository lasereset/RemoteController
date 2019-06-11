package com.dmrjkj.remotecontroller.support;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.util.SparseIntArray;

/**
 * Created by leic on 10/19/16.
 */

public class FeedbackController {

    /** 最大数量的并发声音流 */
    private static final int MAX_STREAMS = 10;

    /** 默认播放的流类型 */
    private static final int DEFAULT_STREAM = AudioManager.STREAM_MUSIC;

    private final Context mContext;

    /** 声音池 */
    private final SoundPool mSoundPool;
    private final SparseIntArray mSoundIds = new SparseIntArray();

    public FeedbackController(Context context) {
        mContext = context;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            mSoundPool = createSoundPoolApi21();
        } else {
            mSoundPool = new SoundPool(MAX_STREAMS, DEFAULT_STREAM, 0);
        }
    }

    @TargetApi(21)
    private SoundPool createSoundPoolApi21() {
        AudioAttributes aa = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        return new SoundPool.Builder()
                .setMaxStreams(MAX_STREAMS)
                .setAudioAttributes(aa)
                .build();
    }

    /**
     * 播放音效
     * @param resId  音效的资源ID
     */
    public void playAuditory(int resId) {
        playAuditory(resId, 1.0f /* rate */, 1.0f /* volume */);
    }

    /**
     * 播报音效
     * @param resId  音效的资源ID
     * @param rate   播放速度
     * @param volume 播报音量
     */
    public void playAuditory(int resId, final float rate, final float volume) {
        if (resId == 0) {
            return;
        }
        int soundId = mSoundIds.get(resId);
        if (soundId != 0) {
            new EarconsPlayTask(mSoundPool, soundId, volume, rate).execute();
        } else {
            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (sampleId != 0) {
                        new EarconsPlayTask(mSoundPool, sampleId, volume, rate).execute();
                    }
                }
            });
            mSoundIds.put(resId, mSoundPool.load(mContext, resId, 1));
        }
    }

    private static class EarconsPlayTask extends AsyncTask<Void, Integer, Boolean> {
        private SoundPool mSoundPool;
        private int mSoundId;
        private float mVolume;
        private float mRate;

        public EarconsPlayTask(SoundPool soundPool, int soundId, float volume, float rate) {
            mSoundPool = soundPool;
            mSoundId = soundId;
            mVolume = volume;
            mRate = rate;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return mSoundPool.play(mSoundId, mVolume, mVolume, 0, 0, mRate) !=0;
        }
    }
}
