package jp.isao.recognizetest;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 音声認識アプリをIntentで呼び出す
 * @author utsumiisao
 *
 */
public class RecognizeTestActivity extends Activity {
    
	/**
	 * 音声認識アプリからのリクエストコード
	 */
	private static final int __REQUEST_RECOGNIZE = 100;
	
	/**
	 * レイアウトに認識した文字を表示する
	 */
	private TextView tv;
	
	/**
	 * 音声認識のintent
	 */
	private Intent _recognizerIntent;
	
	/**
	 * 音声認識機能がユーザーに尋ねる文言
	 */
	private static final String __PROMPT= "Tell Me Your Name";
	
	/**
	 * 音声認識アプリがなかったときGooglePlayからダウンロードを問いかけるダイアログのタイトル
	 */
	private static final String __DIALOG_TITLE = "Not Available";
	
	private static final String __DIALOG_MESSAGE = "There is currently no recognition application installed " +
			"Would you like to download one?";
	
	private static final String __RECOGNIZER_APP_URI = "market://details?id=com.google.android.voicesearch";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        setContentView(tv);
        makeRecognizerIntent();
        sendRecognizerIntent();
    }
    
    /**
     * 音声認識機能の呼び出すintentを作る
     */
    private void makeRecognizerIntent() {
    	_recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    	_recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    	_recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, __PROMPT);
    }
    
    /**
     * 音声認識機能にintentを送信する
     */
    private void sendRecognizerIntent() {
    	try {
    		startActivityForResult(_recognizerIntent, __REQUEST_RECOGNIZE);	
		} catch (ActivityNotFoundException e) {
			makeRecognitionAppAlertDialog();
		}
    }
 
    /**
     * 音声認識アプリが無いとき、ユーザーにダウンロードするか問いかけるダイアログ
     */
    private void makeRecognitionAppAlertDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(__DIALOG_TITLE);
    	builder.setMessage(__DIALOG_MESSAGE);
    	builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				makeGooglePlayIntent();
			}
		});
    	builder.setNegativeButton("No", null);
    	builder.create().show();
    }
    
    /**
     * GooglePlayの音声認識アプリのサイトに飛ぶIntent
     */
    private void makeGooglePlayIntent() {
    	Intent googlePlayIntent = new Intent(Intent.ACTION_VIEW);
    	googlePlayIntent.setData(Uri.parse(__RECOGNIZER_APP_URI));
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == __REQUEST_RECOGNIZE && resultCode == Activity.RESULT_OK) {
    		ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    		tv.setText(matches.get(0));
    	} else {
    		Toast.makeText(this, "Operation Canceled", Toast.LENGTH_SHORT).show();
    	}
    }
}