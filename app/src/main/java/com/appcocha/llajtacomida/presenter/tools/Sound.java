package com.appcocha.llajtacomida.presenter.tools;

import android.content.Context;
import android.media.MediaPlayer;

import com.appcocha.llajtacomida.R;

import java.io.Serializable;

/**
 * Contiene los efectos de sonido de la aplicación
 */
public class Sound implements Serializable {

    private static float volume;
    private static MediaPlayer mediaPlayerStart;
    private static MediaPlayer mediaPlayerClick;
    private static MediaPlayer mediaPlayerThrow;
//    private static MediaPlayer mediaPlayerAlert;
    private static MediaPlayer mediaPlayerLocation;
    private static MediaPlayer mediaPlayerLocationEditor;
    private static MediaPlayer mediaPlayerMagic;
    private static MediaPlayer mediaPlayerLoad;
    private static MediaPlayer mediaPlayerError;
    private static MediaPlayer mediaPlayerGPS;
    private static MediaPlayer mediaPlayerSuccess;
    private static MediaPlayer mediaPlayerDrop;

    /**
     * Contructor, inicializa todos los efectos de sonido
     * @param context
     */
    public Sound(Context context){
        volume = 0.08F;
        mediaPlayerStart = MediaPlayer.create(context, R.raw.start_3);
        mediaPlayerClick = MediaPlayer.create(context, R.raw.click_2);
//        mediaPlayerAlert = MediaPlayer.create(context, R.raw.alert);
        mediaPlayerThrow = MediaPlayer.create(context, R.raw.sound_1);
        mediaPlayerLocation = MediaPlayer.create(context, R.raw.flash_camera); //digital
        mediaPlayerLocationEditor = MediaPlayer.create(context, R.raw.alien_1);
        mediaPlayerMagic = MediaPlayer.create(context, R.raw.magic);
        mediaPlayerLoad = MediaPlayer.create(context, R.raw.load);
        mediaPlayerError = MediaPlayer.create(context, R.raw.error_1);
        mediaPlayerGPS = MediaPlayer.create(context, R.raw.gps);
        mediaPlayerSuccess = MediaPlayer.create(context, R.raw.success);
        mediaPlayerDrop = MediaPlayer.create(context, R.raw.drop);
        volume(volume);
    }

    /**
     * Establece el estado on / of del volumen
     * @param on
     */
    public static void setVolumeOff(boolean on){
        if(on) volume(0.08f);
        else volume(0f);
    }

    /**
     * Establece el nivel del volumen
     * @param VOLUME
     */
    private static void volume(final float VOLUME){
        mediaPlayerStart.setVolume(VOLUME, VOLUME);
        mediaPlayerClick.setVolume(VOLUME, VOLUME);
        mediaPlayerMagic.setVolume(VOLUME, VOLUME);
        mediaPlayerLoad.setVolume(VOLUME, VOLUME);
        mediaPlayerThrow.setVolume(VOLUME, VOLUME);
//        mediaPlayerAlert.setVolume(VOLUME, VOLUME);
        mediaPlayerLocation.setVolume(VOLUME, VOLUME);
        mediaPlayerLocationEditor.setVolume(VOLUME, VOLUME);
        mediaPlayerGPS.setVolume(VOLUME, VOLUME);
        mediaPlayerError.setVolume(VOLUME, VOLUME);
        mediaPlayerSuccess.setVolume(VOLUME, VOLUME);
        mediaPlayerDrop.setVolume(VOLUME, VOLUME);
    }

    /**
     * Si el sonido está activado
     * reproduce sonido start
     */
    public static void playStart(){
        if(volume > 0) mediaPlayerStart.start();
    }

    /**
     * Si el sonido está activado
     * reproduce sonido click
     */
    public static void playClick(){
        if(volume > 0) mediaPlayerClick.start();
    }

    /**
     * Si el sonido está activado
     * reproduce sonido magic
     */
    public static void playMagic(){
        if(volume > 0) mediaPlayerMagic.start();
    }

//    /**
//     * Si el sonido está activado
//     * reproduce sonido alert
//     */
//    public static void playAlert(){
//        if(volume > 0) mediaPlayerAlert.start();
//    }

    /**
     * Si el sonido está activado
     * reproduce sonido throw
     */
    public static void playThrow(){
        if(volume > 0) mediaPlayerThrow.start();
    }

    /**
     * Si el sonido está activado
     * reproduce sonido load
     */
    public static void playLoad(){
        if(volume > 0) mediaPlayerLoad.start();
    }

    /**
     * Si el sonido está activado
     * reproduce sonido location
     */
    public static void playLocation(){
        if(volume > 0) mediaPlayerLocation.start();
    }

    /**
     * Si el sonido está activado
     * reproduce sonido locationEditor
     */
    public static void playLocationEditor(){
        if(volume > 0) mediaPlayerLocationEditor.start();
    }

    /**
     * Si el sonido está activado
     * reproduce sonido error
     */
    public static void playError(){
        if(volume > 0) mediaPlayerError.start();
    }

    /**
     * Si el sonido está activado
     * reproduce sonido success
     */
    public static void playSuccess(){
        if(volume > 0) mediaPlayerSuccess.start();
    }

    /**
     * Si el sonido está asctivado
     * reproduce sonido drop
     */
    public static void playDrop(){
        if(volume > 0){
            mediaPlayerDrop.start();
        }
    }

    /**
     * Si el sonido está asctivado
     * reproduce sonido GPS
     */
    public static void playGPS(){
        if(volume > 0){
            mediaPlayerGPS.start();
        }
    }
}
