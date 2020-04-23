package jp.co.stv_tech.android_12_1

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import java.io.IOException
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    private var _player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _player = MediaPlayer()

        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.mountain_stream}"
        val mediaFileUri = Uri.parse(mediaFileUriStr)

        try {
            _player?.setDataSource(applicationContext, mediaFileUri)
            _player?.setOnPreparedListener(PlayerPreparedListener())
            _player?.setOnCompletionListener (PlayerCompletionListener())
            _player?.prepareAsync()
        } catch (ex: IllegalArgumentException) {
            Log.e("Media Sample", "メディアプレイヤーの準備時の例外発生", ex)

        } catch (ex: IOException) {
            Log.e("Media Sample", "メディアプレイヤーの準備時の例外発生", ex)
        }

        //スイッチのチェックイベントリスナーを登録する
        val loopSwitch = findViewById<Switch>(R.id.swLoop)
        loopSwitch.setOnCheckedChangeListener(LoopSwitchChangedListener())
    }

    override fun onDestroy() {
        super.onDestroy()
        
        _player?.let { 
            if(it.isPlaying) {
                it.stop()
            }
            it.release()
            _player = null
        }
    }
    
    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer?) {

            Log.i("Media Sample", "PlayerPreparredListener")

            val btPlay = findViewById<Button>(R.id.btPlay)
            btPlay.isEnabled = true

            val btBack = findViewById<Button>(R.id.btBack)
            btBack.isEnabled = true

            val btForword = findViewById<Button>(R.id.btForward)
            btForword.isEnabled = true
        }
    }

    //再生完了通知イベントリスナー
    private inner class PlayerCompletionListener : MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer?) {

            _player?.let {

                if(!it.isLooping) {
                    val btPlay = findViewById<Button>(R.id.btPlay)
                    btPlay.setText(R.string.bt_play_play)
                }
            }
        }
    }
    
    fun onPlayButtonClick(view: View) {
        
        Log.i("Media Sample", "onPlayButtonClick")
        
        _player?.let { 
            val btPlay = findViewById<Button>(R.id.btPlay)
            
            if(it.isPlaying) {
                it.pause()
                btPlay.setText(R.string.bt_play_play)
            } else {
                it.start()
                btPlay.setText(R.string.bt_play_pause)
            }
        }
    }

    //戻るボタン押下時
    fun onBackButtonClick(view: View) {
        _player?.seekTo(0)
    }

    //進むボタン押下時
    fun onForwardButtonClick(view: View) {

        _player?.let {
            val duration = it.duration

            it.seekTo(duration)

            if(!it.isPlaying) {
                it.start()
            }
        }
    }

    //ループスイッチのイベントリスナー
    private inner class LoopSwitchChangedListener: CompoundButton.OnCheckedChangeListener{

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            _player?.isLooping = isChecked
        }
    }
}
