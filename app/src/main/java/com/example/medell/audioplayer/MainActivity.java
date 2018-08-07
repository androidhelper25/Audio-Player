package com.example.medell.audioplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static MediaPlayer mp;
    SeekBar sb;
    Button btnply,btnnxt,btnprv,btnfw,btnbck;
    Uri ui;
    Thread updateseekbar;
    ListView lv;
    String []items;
    int pos=0;
    int currentdu,totaldu;
    ArrayList<File> mysong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView)findViewById(R.id.lvPlaylist);
        btnply=(Button)findViewById(R.id.btnplay);
        btnbck=(Button)findViewById(R.id.btnbck);
        btnnxt=(Button)findViewById(R.id.btnnxt);
        btnprv=(Button)findViewById(R.id.btnprv);
        btnfw=(Button)findViewById(R.id.btnfwd);
        sb=(SeekBar)findViewById(R.id.seekBar);
        btnply.setOnClickListener(this);
        btnfw.setOnClickListener(this);
        btnbck.setOnClickListener(this);
        btnnxt.setOnClickListener(this);
        btnprv.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)  {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.songmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.song:
            {
                mysong=findsong(Environment.getExternalStorageDirectory());
                items=new String[mysong.size()];
                for(int i=0;i<mysong.size();i++)
                {
                    // tost(mysong.get(i).getName().toString());
                    items[i]=mysong.get(i).getName().toString().replace(".mp3","");

                }
                Toast.makeText(getApplicationContext(),String.valueOf(mysong.size()),Toast.LENGTH_LONG).show();
                ArrayAdapter<String> adp= new ArrayAdapter<>(getApplicationContext(), R.layout.song_layout,R.id.textView, items);
                lv.setAdapter(adp);



                updateseekbar=new Thread(){
                    public void run()
                    {
                        totaldu=mp.getDuration();

                        while (currentdu<totaldu)
                        {
                            try {
                                sleep(500);
                                currentdu=mp.getCurrentPosition();
                                sb.setProgress(currentdu);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                };

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(mp!=null)
                        {
                            mp.stop();
                            mp.release();
                            ui= Uri.parse(mysong.get(position).toString());
                            mp= MediaPlayer.create(getApplicationContext(), ui);
                            mp.start();
                            sb.setMax(mp.getDuration());
                            currentdu=0;
                            updateseekbar.start();
                            pos=position;
                            Toast.makeText(getApplicationContext(),String.valueOf(pos),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            ui= Uri.parse(mysong.get(position).toString());
                            mp= MediaPlayer.create(getApplicationContext(), ui);
                            mp.start();
                            sb.setMax(mp.getDuration());
                            currentdu=0;
                            updateseekbar.start();
                            pos=position;
                            Toast.makeText(getApplicationContext(),String.valueOf(pos),Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public ArrayList<File> findsong(File root)
    {
        ArrayList<File> f1=new ArrayList<File>();
        File [] f=root.listFiles();
        for(File singlefiles:f)
        {
            if(singlefiles.isDirectory() && !singlefiles.isHidden())
            {
                f1.addAll(findsong(singlefiles));
            }else
            {
                if(singlefiles.getName().endsWith(".mp3"))
                {
                    f1.add(singlefiles);
                }
            }
        }
        return  f1;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.btnplay:
            {
                if(mp.isPlaying())
                {
                    mp.pause();
                    btnply.setText(">");
                }
                else {
                    mp.start();
                    btnply.setText("||");
                }
                break;
            }
            case R.id.btnfwd:
            {
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            }
            case R.id.btnbck:
            {
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            }
            case R.id.btnnxt:
            {
                if(pos==mysong.size()-1)
                {
                   /* pos=0;
                    mp.stop();
                    mp.release();
                    ui= Uri.parse(mysong.get(pos+1).toString());
                    mp= MediaPlayer.create(getApplicationContext(), ui);
                    mp.start();
                    sb.setMax(mp.getDuration());
                    updateseekbar.start();
                    pos=pos+1;
                    break;  */
                   pos=0;
                    Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();

                }
                else
                {

                mp.stop();
                mp.release();
                ui= Uri.parse(mysong.get(pos+1).toString());
                mp= MediaPlayer.create(getApplicationContext(), ui);
                mp.start();
                sb.setMax(mp.getDuration());
                currentdu=0;
                updateseekbar.start();
                pos=pos+1;
                Toast.makeText(getApplicationContext(),String.valueOf(pos),Toast.LENGTH_LONG).show();
                break;
                }
            }
            case R.id.btnprv: {
                if (pos == 0)
                {
                /*--    pos=mysong.size()-1;
                    mp.stop();
                    mp.release();
                    ui= Uri.parse(mysong.get(pos-1).toString());
                    mp= MediaPlayer.create(getApplicationContext(), ui);
                    mp.start();
                    sb.setMax(mp.getDuration());
                    updateseekbar.start();
                    pos=pos-1;
                    break;  --*/
                }
                else
                {
                mp.stop();
                mp.release();
                ui= Uri.parse(mysong.get(pos-1).toString());
                mp= MediaPlayer.create(getApplicationContext(), ui);
                mp.start();
                sb.setMax(mp.getDuration());
                    currentdu=0;
                updateseekbar.start();
                pos=pos-1;
                    Toast.makeText(getApplicationContext(),String.valueOf(pos),Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
