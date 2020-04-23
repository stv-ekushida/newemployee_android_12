# newemployee_android_12
メディア再生

## ①音声ファイルの追加
リソース（res)フォルダ直下に、rawフォルダを作成し、音声ファイルを追加する。


## ②メディアプレイヤーの準備

```
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

```

## ③リスナクラスの準備
### 再生準備完了イベントリスナー

```
    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer?) {

            val btPlay = findViewById<Button>(R.id.btPlay)
            btPlay.isEnabled = true

            val btBack = findViewById<Button>(R.id.btBack)
            btBack.isEnabled = true

            val btForword = findViewById<Button>(R.id.btForward)
            btForword.isEnabled = true
        }
    }
```

### 再生完了通知イベントリスナー
```
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
```

## ④再生・停止

```
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
```
