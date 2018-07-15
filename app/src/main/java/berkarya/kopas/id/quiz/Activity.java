package berkarya.kopas.id.quiz;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import berkarya.kopas.id.quiz.util.AudioService;
import berkarya.kopas.id.quiz.util.AudioServiceBinder;
import berkarya.kopas.id.quiz.util.BankSoal;
import berkarya.kopas.id.quiz.util.DatabaseHelper;
import berkarya.kopas.id.quiz.util.DrawerAdapter;
import berkarya.kopas.id.quiz.util.ImageGetter;
import berkarya.kopas.id.quiz.util.MengerjakanSoal;

public class Activity extends AppCompatActivity{

    static TabLayout tabs;
    static ViewPager pages;
    DrawerLayout drawer;
    private ArrayList<MengerjakanSoal> mengerjakanSoal;
    public static ArrayList<BankSoal> bankSoal;
    private RecyclerView mRecyclerView;
    TextView waktu;
    viewPageAdapter adapter;
    static TextView nomor;


    private SQLiteDatabase sql;
    public static DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs = findViewById(R.id.tabs);
        pages = findViewById(R.id.viewPager);

        getBankSoal();
        getBankSoalJawab();

        db = new DatabaseHelper(getApplicationContext());

        tabs.setupWithViewPager(pages);
        setUpViewPager(pages,bankSoal,mengerjakanSoal);

        nomor = toolbar.findViewById(R.id.toolbarNomor);
        waktu = toolbar.findViewById(R.id.toolbarWaktu);
        nomor.setText( String.valueOf(tabs.getSelectedTabPosition()+1) );
        new TimerClass(60000 * 60, 1000).start();


        // enable pull down to refresh
        //final SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        //final ProgressBar progressBar = findViewById(R.id.progressBar);

        /*
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                progressBar.setVisibility(View.VISIBLE);
                // do something
                getBankSoal();
                adapter.notifyDataSetChanged();

                // after refresh is done, remember to call the following code
                if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);  // This hides the spinner
                    progressBar.setVisibility(View.GONE);
                }
            }
        });*/
        /*View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.tab, null, false);

        TextView linearLayoutOne = (TextView) headerView.findViewById(R.id.nomor);

        int tabCount = tabs.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            tab.setCustomView(linearLayoutOne);
            tab.setText(i);
        }*/

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        mRecyclerView = findViewById(R.id.drawerRecyclerView);

        final DrawerAdapter adapter = new DrawerAdapter(mengerjakanSoal);
        final GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 4 : 1;
            }
        });

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickLister(new DrawerAdapter.OnItemSelecteListener() {
            @Override
            public void onItemSelected(View v, int position) {

                //TODO change if the position has changed
                Toast.makeText(Activity.this, "You clicked at position: "+ position, Toast.LENGTH_SHORT).show();

                if( position == 0 ) {
                    //
                }else{
                    TabLayout.Tab tab = tabs.getTabAt(position - 1);
                    tab.select();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.END);
            }
        });
/*
        for(int x = 1; x<=40; x++) {
            View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.tab, null, false);
            LinearLayout linearLayoutOne = (LinearLayout) headerView.findViewById(R.id.linearlayoutItem);
            TextView tabNomor = (TextView) headerView.findViewById(R.id.tabNomor);

            tabNomor.setText( String.valueOf(x) );
            tabs.getTabAt(x-1).setCustomView(linearLayoutOne);
        }*/


        AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

    }



    private void getBankSoal() {
        bankSoal = new ArrayList<>();

        for(int z = 1; z<=40; z++) {


            String soal_gambar = "<img style=\"width:100%;\" src=\"https://www.murrayc.com/blog/wp-content/uploads/2014/10/screenshot_toolbar_dark_text_on_light_with_dark_theme_with_menu_cropped-300x136.png\"/><br/>"+
                    "Berdasarkan gambar mutasi yang terjadi adalah";
            if(z > 3){
                soal_gambar = "";
            }

            String soal_audio = "";
            if( z == 4 ){
                soal_audio = "http://www.dev2qa.com/demo/media/test.mp3";
            }

            bankSoal.add(new BankSoal(
                    String.valueOf(z),
                    z+" - Perhatikan gambar skema mutasi berikut ini<br/>"+soal_gambar,
                    soal_audio,
                    "Crossing over dan delesi",
                    "Delesi dan translokasi",
                    "Duplikasi dan katenasi",
                    "Delesi dan duplikasi",
                    "Katenasi dan delesi"
            ));

            //db.insertMengerjakanSoal(String.valueOf(z),"","","");
        }
    }




    private void getBankSoalJawab() {
        //Dummy Data
        mengerjakanSoal = new ArrayList<>();


        final String jawaban = "ABCDE_";
        final int jawaban_count = jawaban.length();

        final String ragu_ragu = "01";
        final int ragu_ragu_count = ragu_ragu.length();

        for(int l=0; l<=bankSoal.size()-1; l++) {
            Random r = new Random();
            String x = String.valueOf(jawaban.charAt(r.nextInt(jawaban_count)));
            String y = String.valueOf(ragu_ragu.charAt(r.nextInt(ragu_ragu_count)));

            mengerjakanSoal.add(new MengerjakanSoal(String.valueOf(l+1), x, y));
        }

        //mengerjakanSoal.addAll(db.getAllMengerjakanSoal());

/*
        for(int l=0; l<=bankSoal.size()-1; l++) {
            BankSoal s = bankSoal.get(l);
            MengerjakanSoal sj = db.getMengerjakanSoal(s.no_soal());
            mengerjakanSoal.add(new MengerjakanSoal(s.no_soal(), sj.get_jawab_soal(),sj.get_jawab_soal_ragu()));
        }*/

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_drawer) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else
                drawer.openDrawer(GravityCompat.END);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpViewPager(ViewPager viewpage,ArrayList<BankSoal> dataBankSoal,ArrayList<MengerjakanSoal> dataMengerjakanSoal){
        adapter = new viewPageAdapter(getSupportFragmentManager(),getApplicationContext());

        for(int l=0; l<=dataBankSoal.size()-1; l++) {
            adapter.addFragmentPage(String.valueOf(l+1),dataBankSoal, dataMengerjakanSoal);
        }
        //We Need Fragment class now

        viewpage.setAdapter(adapter);

    }


    @Override
    protected void onDestroy() {
        // Unbound background audio service when activity is destroyed.
        //unBoundAudioService();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();  // Always call the superclass method first

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public class viewPageAdapter extends FragmentPagerAdapter {
        private final List<String> myPageTitle = new ArrayList<>();
        ArrayList<BankSoal> dataBankSoalPages = new ArrayList<>();
        ArrayList<MengerjakanSoal> dataMengerjakanSoalPages = new ArrayList<>();
        Context ctx;

        public viewPageAdapter(FragmentManager manager, Context applicationContext){
            super(manager);
            this.ctx = applicationContext;
        }

        public void addFragmentPage(String Title, ArrayList<BankSoal> dataBankSoal, ArrayList<MengerjakanSoal> dataMengerjakanSoal){
            myPageTitle.add(Title);
            dataBankSoalPages.addAll( dataBankSoal );
            dataMengerjakanSoalPages.addAll( dataMengerjakanSoal );
        }

        @Override
        public Fragment getItem(int position) {
            return new Page().newInstance(dataBankSoalPages.get(position),dataMengerjakanSoalPages.get(position));
        }

        @Override
        public int getCount() {
            return myPageTitle.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return myPageTitle.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }


    }

    public class TimerClass extends CountDownTimer {

        TimerClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //Method ini berjalan saat waktu/timer berubah
        @Override
        public void onTick(long millisUntilFinished) {
            //Kenfigurasi Format Waktu yang digunakan
            String waktuTo = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

            //Menampilkannya pada TexView
            waktu.setText(waktuTo);
        }

        @Override
        public void onFinish() {
            ///Berjalan saat waktu telah selesai atau berhenti
            Toast.makeText(Activity.this, "Waktu Telah Berakhir", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
