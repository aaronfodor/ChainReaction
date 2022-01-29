package com.aaronfodor.android.chain_reaction.model

import android.content.Context
import android.media.MediaPlayer
import android.preference.PreferenceManager
import com.aaronfodor.android.chain_reaction.R

/**
 * App sound handler singleton
 */
object AudioService{

    private var soundEnabled = true

    private var soundButtonClick = MediaPlayer()
    private var soundPositiveButtonClick = MediaPlayer()
    private var soundNegativeButtonClick = MediaPlayer()
    private var soundBackClick = MediaPlayer()
    private var soundOn = MediaPlayer()
    private var soundOff = MediaPlayer()
    private var soundSwipe = MediaPlayer()
    private var soundAIVictory = MediaPlayer()
    private var soundHumanVictory = MediaPlayer()
    private var soundConfetti = MediaPlayer()
    private var soundParticle = MediaPlayer()
    //private var soundDialog = MediaPlayer()
    private var soundLocked = MediaPlayer()

    /**
     * Initializes the object
     *
     * @param    context        Context where the MediaPlayer plays the sound and checks the preference sound value
     */
    fun init(context: Context){
        initSounds(context)
        refreshAudioFlag(context)
    }

    /**
     * Initializes the sounds
     *
     * @param    context        Context where the MediaPlayer plays the sound
     */
    private fun initSounds(context: Context) {
        soundButtonClick = MediaPlayer.create(context, R.raw.tap)
        soundPositiveButtonClick = MediaPlayer.create(context, R.raw.tap)
        soundNegativeButtonClick = MediaPlayer.create(context, R.raw.tap)
        soundBackClick = MediaPlayer.create(context, R.raw.notification)
        soundOn = MediaPlayer.create(context, R.raw.on)
        soundOff = MediaPlayer.create(context, R.raw.off)
        soundSwipe = MediaPlayer.create(context, R.raw.swipe)
        soundAIVictory = MediaPlayer.create(context, R.raw.victory_ai)
        soundHumanVictory = MediaPlayer.create(context, R.raw.victory_human)
        soundConfetti = MediaPlayer.create(context, R.raw.confetti)
        soundParticle = MediaPlayer.create(context, R.raw.particle)
        //soundDialog = MediaPlayer.create(context, R.raw.dialog)
        soundLocked = MediaPlayer.create(context, R.raw.locked)
    }

    /**
     * Refreshes the audioEnabled flag
     *
     * @param    context        Context where the preference sound value is queried
     */
    fun refreshAudioFlag(context: Context) {
        val settings = PreferenceManager.getDefaultSharedPreferences(context)
        soundEnabled = settings.getBoolean("sound_enabled", true)
    }

    /**
     * Prepares the MediaPlayer object to be played
     * If the MediaPlayer object is currently playing, pauses it and sets it to the start
     *
     * @param    audio      MediaPlayer object which is inspected
     */
    private fun prepareAudioPlaying(audio: MediaPlayer) {
        if(audio.isPlaying){
            audio.pause()
            audio.seekTo(0)
        }
    }

    /**
     * Plays button click sound
     */
    fun soundButtonClick(){
        if(soundEnabled){
            prepareAudioPlaying(soundButtonClick)
            soundButtonClick.start()
        }
    }

    /**
     * Plays positive button click sound
     */
    fun soundPositiveButtonClick(){
        if(soundEnabled){
            prepareAudioPlaying(soundPositiveButtonClick)
            soundPositiveButtonClick.start()
        }
    }

    /**
     * Plays negative button click sound
     */
    fun soundNegativeButtonClick(){
        if(soundEnabled){
            prepareAudioPlaying(soundNegativeButtonClick)
            soundNegativeButtonClick.start()
        }
    }

    /**
     * Plays back click sound
     */
    fun soundBackClick(){
        if(soundEnabled){
            prepareAudioPlaying(soundBackClick)
            soundBackClick.start()
        }
    }

    /**
     * Plays on sound
     */
    fun soundOn(){
        if(soundEnabled){
            prepareAudioPlaying(soundOn)
            soundOn.start()
        }
    }

    /**
     * Plays off sound
     */
    fun soundOff(){
        if(soundEnabled){
            prepareAudioPlaying(soundOff)
            soundOff.start()
        }
    }

    /**
     * Plays swipe sound
     */
    fun soundSwipe(){
        if(soundEnabled){
            prepareAudioPlaying(soundSwipe)
            soundSwipe.start()
        }
    }

    /**
     * Plays AI victory sound
     */
    fun soundAIVictory(){
        if(soundEnabled){
            prepareAudioPlaying(soundAIVictory)
            soundAIVictory.start()
        }
    }

    /**
     * Plays human victory sound
     */
    fun soundHumanVictory(){
        if(soundEnabled){
            prepareAudioPlaying(soundHumanVictory)
            soundHumanVictory.start()
        }
    }

    /**
     * Plays confetti sound
     */
    fun soundConfetti(){
        if(soundEnabled){
            prepareAudioPlaying(soundConfetti)
            soundConfetti.start()
        }
    }

    /**
     * Plays particle sound
     */
    fun soundParticle(){
        if(soundEnabled){
            prepareAudioPlaying(soundParticle)
            soundParticle.start()
        }
    }

    /**
     * Plays dialog sound
     */
    /*fun soundDialog(){
        if(soundEnabled){
            prepareAudioPlaying(soundDialog)
            soundDialog.start()
        }
    }*/

    /**
     * Plays locked sound
     */
    fun soundLocked(){
        if(soundEnabled){
            prepareAudioPlaying(soundLocked)
            soundLocked.start()
        }
    }
}