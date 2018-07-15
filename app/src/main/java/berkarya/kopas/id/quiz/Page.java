package berkarya.kopas.id.quiz;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import berkarya.kopas.id.quiz.util.BankSoal;
import berkarya.kopas.id.quiz.util.ImageGetter;
import berkarya.kopas.id.quiz.util.MengerjakanSoal;

public class Page extends Fragment {

    boolean complete = false;
    MediaPlayer mediaPlayer;
    Handler audioProgressUpdateHandler = null;
    ProgressBar backgroundAudioProgress;
    public final int UPDATE_AUDIO_PROGRESS_BAR = 1;

    private int stateMediaPlayer;
    private final int STATE_Idle = 0;
    private final int STATE_Initialized = 1;
    private final int STATE_Preparing = 2;
    private final int STATE_Prepared = 3;
    private final int STATE_Started = 4;
    private final int STATE_Paused = 5;
    private final int STATE_Stopped = 6;
    private final int STATE_PlaybackCompleted = 7;
    private final int STATE_End = 8;
    private final int STATE_Error = 9;

    TextView playerState, playerDuration;
    Activity act;
    Context ctx;
    TextView tanya1;
    String no_soal, tanya_soal, tanya_soal_audio, jawab_a, jawab_b, jawab_c, jawab_d, jawab_e, jawab_soal, jawab_soal_ragu;

    public Fragment newInstance(BankSoal dataBankSoal, MengerjakanSoal mengerjakanSoal) {
        Page f = new Page();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("no_soal", dataBankSoal.no_soal());
        args.putString("tanya_soal", dataBankSoal.tanya_soal());
        args.putString("tanya_soal_audio", dataBankSoal.tanya_soal_audio());
        args.putString("jawab_a", dataBankSoal.jawab_a());
        args.putString("jawab_b", dataBankSoal.jawab_b());
        args.putString("jawab_c", dataBankSoal.jawab_c());
        args.putString("jawab_d", dataBankSoal.jawab_d());
        args.putString("jawab_e", dataBankSoal.jawab_e());
        args.putString("jawab_soal", mengerjakanSoal.get_jawab_soal());
        args.putString("jawab_soal_ragu", mengerjakanSoal.get_jawab_soal_ragu());
        f.setArguments(args);

        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        no_soal = getArguments().getString("no_soal");
        tanya_soal = getArguments().getString("tanya_soal");
        tanya_soal_audio = getArguments().getString("tanya_soal_audio");
        //tanya_gambar = getArguments().getString("tanya_gambar");
        //tanya_soal2 = getArguments().getString("tanya_soal2");
        jawab_a = getArguments().getString("jawab_a");
        jawab_b = getArguments().getString("jawab_b");
        jawab_c = getArguments().getString("jawab_c");
        jawab_d = getArguments().getString("jawab_d");
        jawab_e = getArguments().getString("jawab_e");
        jawab_soal = getArguments().getString("jawab_soal");
        jawab_soal_ragu = getArguments().getString("jawab_soal_ragu");

        ctx = getContext();
        act = new Activity();

        initMediaPlayer();

    }

    private void initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.test);
        setPlayerState(STATE_Prepared);
    }

    private void setPlayerState(int st){
        stateMediaPlayer = st;
        //String stringState = getPlayerState();
    }

    private String getPlayerState(){
        String strSt;
        switch(stateMediaPlayer){
            case STATE_Idle: strSt = "Idle";
                break;
            case STATE_Initialized: strSt = "Initialized";
                break;
            case STATE_Preparing: strSt = "Preparing";
                break;
            case STATE_Prepared: strSt = "Prepared";
                break;
            case STATE_Started: strSt = "Started";
                break;
            case STATE_Paused: strSt = "Paused";
                break;
            case STATE_Stopped: strSt = "Stopped";
                break;
            case STATE_PlaybackCompleted: strSt = "PlaybackCompleted";
                break;
            case STATE_End: strSt = "End";
                break;
            case STATE_Error: strSt = "Error";
                break;
            default: strSt = "unknown...";
        }
        return strSt;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View PageTree = inflater.inflate(R.layout.page, container, false);

        tanya1 = PageTree.findViewById(R.id.txtSoal);

        RadioButton jawabA = PageTree.findViewById(R.id.rb_A);
        RadioButton jawabB = PageTree.findViewById(R.id.rb_B);
        RadioButton jawabC = PageTree.findViewById(R.id.rb_C);
        RadioButton jawabD = PageTree.findViewById(R.id.rb_D);
        RadioButton jawabE = PageTree.findViewById(R.id.rb_E);

        jawabA.setText(jawab_a);
        jawabB.setText(jawab_b);
        jawabC.setText(jawab_c);
        jawabD.setText(jawab_d);
        jawabE.setText(jawab_e);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            Spanned spanned = Html.fromHtml(tanya_soal, new ImageGetter(getResources(),tanya1), null);
            tanya1.setText(spanned);
            //tanya1.setText(Html.fromHtml(tanya_soal, new ImageGetter(), null));
        }else {
            Spanned spanned = Html.fromHtml(tanya_soal, Html.FROM_HTML_MODE_COMPACT, new ImageGetter(getResources(), tanya1), null);
            tanya1.setText(spanned);
            //tanya1.setText(Html.fromHtml(tanya_soal, Html.FROM_HTML_MODE_COMPACT, new ImageGetter(), null));
        }

        CheckBox ragu_ragu = PageTree.findViewById(R.id.btn_belum_terjawab);
        RadioGroup jawabGroup = PageTree.findViewById(R.id.rgAnswer);

        if(jawab_soal_ragu.equals("1")) {
            ragu_ragu.setChecked(true);
        }else{
            ragu_ragu.setChecked(false);
        }

        //int radioButtonID = jawabGroup.getCheckedRadioButtonId();
        //jawabGroup.findViewById(radioButtonID);

        switch (jawab_soal){
            case "A":
                ((RadioButton)jawabGroup.findViewById(R.id.rb_A)).setChecked(true);
                break;
            case "B":
                ((RadioButton)jawabGroup.findViewById(R.id.rb_B)).setChecked(true);
                break;
            case "C":
                ((RadioButton)jawabGroup.findViewById(R.id.rb_C)).setChecked(true);
                break;
            case "D":
                ((RadioButton)jawabGroup.findViewById(R.id.rb_D)).setChecked(true);
                break;
            case "E":
                ((RadioButton)jawabGroup.findViewById(R.id.rb_E)).setChecked(true);
                break;
        }


        ragu_ragu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked ) {
                    Toast.makeText(ctx, "isChecked", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ctx, "unChecked", Toast.LENGTH_SHORT).show();
                }

            }});

        jawabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                String jawab = "";
                switch(checkedId){
                    case R.id.rb_A:
                        jawab = "A";
                        break;
                    case R.id.rb_B:
                        jawab = "B";
                        break;
                    case R.id.rb_C:
                        jawab = "C";
                        break;
                    case R.id.rb_D:
                        jawab = "D";
                        break;
                    case R.id.rb_E:
                        jawab = "E";
                        break;
                }
                Toast.makeText(ctx,jawab,Toast.LENGTH_SHORT).show();

            }

        });

        final ImageView play = PageTree.findViewById(R.id.action_play);
        final ImageView pause = PageTree.findViewById(R.id.action_pause);

        final ImageView prev = PageTree.findViewById(R.id.btnPrev);
        final ImageView next = PageTree.findViewById(R.id.btnNext);

        backgroundAudioProgress = PageTree.findViewById(R.id.play_audio_in_background_service_progressbar);

        play.setVisibility(View.GONE);
        pause.setVisibility(View.GONE);
        if(!tanya_soal_audio.equals("")) {

            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
            play.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);

                    backgroundAudioProgress.setVisibility(ProgressBar.VISIBLE);

                    createAudioProgressbarUpdater();
                    setAudioProgressUpdateHandler(audioProgressUpdateHandler);

                    Toast.makeText(getActivity().getApplicationContext(), tanya_soal_audio, Toast.LENGTH_SHORT).show();

                    if(stateMediaPlayer==STATE_Prepared || stateMediaPlayer==STATE_Started || stateMediaPlayer==STATE_Paused || stateMediaPlayer==STATE_PlaybackCompleted){
                        mediaPlayer.start();
                        setPlayerState(STATE_Started);


                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                setPlayerState(STATE_Prepared);
                                //displayDurationPosition();
                            }});

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                setPlayerState(STATE_PlaybackCompleted);
                                //displayDurationPosition();

                                setComplete(true);

                                play.setVisibility(View.VISIBLE);
                                pause.setVisibility(View.GONE);



                                if(stateMediaPlayer==STATE_Prepared
                                        || stateMediaPlayer==STATE_Started
                                        || stateMediaPlayer==STATE_Stopped
                                        || stateMediaPlayer==STATE_Paused
                                        || stateMediaPlayer==STATE_PlaybackCompleted){

                                    //Stop
                                    mediaPlayer.stop();
                                    setPlayerState(STATE_Stopped);

                                    //then parepare in background thread
                                    mediaPlayer.prepareAsync();
                                    setPlayerState(STATE_Preparing);

                                }else{
                                    Toast.makeText(getActivity(),
                                            "Stop at Invalid state!",
                                            Toast.LENGTH_LONG).show();
                                }
                            }});
                    }else{
                        Toast.makeText(getActivity(), "Play at Invalid state!", Toast.LENGTH_LONG).show();
                    }

                }
            });
            pause.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    play.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.GONE);

                    if(stateMediaPlayer==STATE_Started || stateMediaPlayer==STATE_Paused || stateMediaPlayer==STATE_PlaybackCompleted){
                        mediaPlayer.pause();
                        setPlayerState(STATE_Paused);
                        //displayDurationPosition();
                    }else{
                        Toast.makeText(getActivity(), "Pause at Invalid state!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        ragu_ragu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        /*
        showPrevNext(prev,next,act.pages.getCurrentItem());
        prev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                act.pages.setCurrentItem(act.pages.getCurrentItem()-1, true); //getItem(-1) for previous
            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                act.pages.setCurrentItem(act.pages.getCurrentItem()+1, true); //getItem(-1) for previous
            }
        });*/


        act.pages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                act.nomor.setText( String.valueOf(act.tabs.getSelectedTabPosition()+1) );

                //showPrevNext(prev,next,act.pages.getCurrentItem());

                //mediaPlayer.pause();
                //setPlayerState(STATE_Paused);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        return PageTree;
    }

    public void setAudioProgressUpdateHandler(Handler audioProgressUpdateHandler) {
        this.audioProgressUpdateHandler = audioProgressUpdateHandler;
    }

    private void createAudioProgressbarUpdater() {
        /* Initialize audio progress handler. */
        if (audioProgressUpdateHandler == null) {
            audioProgressUpdateHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // The update process message is sent from AudioServiceBinder class's thread object.
                    if (msg.what == UPDATE_AUDIO_PROGRESS_BAR) {

                        backgroundAudioProgress.setMax(mediaPlayer.getDuration());
                        // Update progressbar. Make the value 10 times to show more clear UI change.
                        backgroundAudioProgress.setProgress(mediaPlayer.getCurrentPosition());

                        if (getComplete()) {
                            backgroundAudioProgress.setVisibility(ProgressBar.INVISIBLE);
                        } else {
                            backgroundAudioProgress.setVisibility(ProgressBar.VISIBLE);
                        }
                    }
                }
            };
        }

    }

    public void setComplete(boolean x){
        this.complete = x;
    }

    public boolean getComplete(){
        return complete;
    }

    @Override
    public void onDestroy() {

        Toast.makeText(getActivity(), "release mediaPlayer", Toast.LENGTH_LONG).show();
        mediaPlayer.release();
        mediaPlayer = null;
        setPlayerState(STATE_End);

        super.onDestroy();
    }

    private void showPrevNext(ImageView prev, ImageView next, int currentItem) {
        if( currentItem == 0 ){
            prev.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        }else if( currentItem == act.bankSoal.size()-1 ){
            prev.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);
        }else{
            prev.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }
    }

}