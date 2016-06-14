package pt.ipleiria.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> musicas;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("appmusicas", 0);
        Set<String> musicaset = sp.getStringSet("musicasKey", new HashSet<String>());


        musicas = new ArrayList<String>(musicaset);

// ve se da
        SimpleAdapter adapter = createSimpleAdapter(musicas);

//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, musicas);

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        //Quando clicarmos no item da lista de musicas
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
           public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User clicked OK button

                       musicas.remove(position);
                       SimpleAdapter adapter = createSimpleAdapter(musicas);
                       ListView listView = (ListView) findViewById(R.id.listView);
                       listView.setAdapter(adapter);

                       Toast.makeText(MainActivity.this,R.string.limp_s + position, Toast.LENGTH_SHORT).show();
                   }
               });
               builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog

                   }
               });
               // Set other dialog properties
               builder.setTitle(R.string.Aviso);
               builder.setMessage(R.string.Aviso_perg);
               builder.setIcon(R.drawable.aviso);
               // Create the AlertDialog
               AlertDialog dialog = builder.create();
               dialog.show();
               return true;
           }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.play, null));

                String str = o.toString();
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();




                // Set other dialog properties
                builder.setTitle(R.string.play);
                builder.setIcon(R.drawable.play);

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.Option, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter1);

    }
    //Botões para o layout do play

    protected void onStop() {
        super.onStop();

        Toast.makeText(MainActivity.this, "A Guardar Contactos", Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getSharedPreferences("appmusicas",0);
        SharedPreferences.Editor edit = sp.edit();
        HashSet contactsSet = new HashSet(musicas);
        edit.putStringSet("musicasKey", contactsSet);
        edit.commit();

    }

    //botão do procurar as musicas do ListView
    public void Search(View view){
        EditText text = (EditText) findViewById(R.id.editText_search);
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        ListView lv = (ListView) findViewById(R.id.listView);

        String Item = (String)sp.getSelectedItem();
        ArrayList<String> sreach_music = new ArrayList<String>();
        String term = text.getText().toString();

        if (term.equals("")){
            SimpleAdapter adapter = createSimpleAdapter(musicas);
            lv.setAdapter(adapter);
            Toast.makeText(MainActivity.this, R.string.msm, Toast.LENGTH_SHORT).show();
        }else {
            if (Item.equals("ALL")) {
                for (String c : musicas) { // : -> em
                    if (c.contains(term)) {
                        sreach_music.add(c);
                    }
                }
            } else if (Item.equals("ALBUM")) {
                for (String c : musicas) {
                    String[] s = c.split("\\|");
                    String artist = s[0];
                    artist = artist.trim();
                    if (artist.contains(term)) {
                        sreach_music.add(c);
                    }
                }
            } else if (Item.equals("ARTIST")) {
                for (String c : musicas) {
                    String[] s = c.split("\\|");
                    String album = s[1];
                    if (album.contains(term)) {
                        sreach_music.add(c);
                    }
                }
            } else if (Item.equals("YEAR")) {
                for (String c : musicas) {
                    String[] s = c.split("\\|");
                    String year = s[2];
                    if (year.contains(term)) {
                        sreach_music.add(c);
                    }
                }
            } else if (Item.equals("EDITORA")){
                for(String c : musicas){
                    String[] s = c.split("\\|");
                    String year = s[3];
                    if (year.contains(term)){
                        sreach_music.add(c);
                    }
                }
            }else if (Item.equals("RATING")) {
                for (String c : musicas) {
                    String[] s = c.split("\\|");
                    String r = s[4];
                    if (r.contains(term)) {
                        sreach_music.add(c);
                    }
                }
            }
            boolean vazia = sreach_music.isEmpty();


            if(!vazia) {
                SimpleAdapter adapter = createSimpleAdapter(musicas);
                lv.setAdapter(adapter);
                Toast.makeText(MainActivity.this, R.string.msm, Toast.LENGTH_SHORT).show();

            }else {

                SimpleAdapter adapter = createSimpleAdapter(musicas);

                lv.setAdapter(adapter);
                Toast.makeText(MainActivity.this, R.string.no_found, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //botão do adicionar musicas
    public void ADD_Album(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.song_app, null));
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                AlertDialog al = (AlertDialog) dialog;

                EditText etArtist = (EditText) al.findViewById(R.id.editText_artist);
                EditText etAlbum = (EditText) al.findViewById(R.id.editText_album);
                EditText etYear = (EditText) al.findViewById(R.id.editText_year);
                EditText etEditor = (EditText) al.findViewById(R.id.editText);
                RatingBar star = (RatingBar) al.findViewById(R.id.ratingBar);

                String artist = etArtist.getText().toString();
                String album = etAlbum.getText().toString();
                String year = etYear.getText().toString();
                String editor = etEditor.getText().toString();
                int starRating = (int)star.getRating();

                String newMusic = "★ "+ album + " ★ " + "|" + artist + " ★ "+ "|" + year + " ★ "+ "|"+ editor + " ★ "+ "|" + starRating + "stars";

                if (!artist.isEmpty() || !album.isEmpty() || !year.isEmpty() || !editor.isEmpty()) {
                    musicas.add(newMusic);
                    Toast.makeText(MainActivity.this, R.string.Criar, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.No_Criar, Toast.LENGTH_SHORT).show();
                }

                ListView lv = (ListView) findViewById(R.id.listView);
                SimpleAdapter adapter = createSimpleAdapter(musicas);
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties
        builder.setTitle(R.string.New);
        builder.setIcon(R.drawable.add);
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private SimpleAdapter createSimpleAdapter(ArrayList<String> musics) {
        List<HashMap<String, String>> simpleAdapterData = new ArrayList<HashMap<String, String>>();
        for (String c : musics) {
            HashMap<String, String> hashMap = new HashMap<>();

            String[] split = c.split("\\|");

            hashMap.put("artist", split[0]);
            hashMap.put("album", split[1]);
            hashMap.put("year", split[2]);
            hashMap.put("editor", split[3]);
            hashMap.put("starRating", split[4]);

            simpleAdapterData.add(hashMap);
        }

        String[] from = {"artist", "album", "year", "editor", "starRating"};
        int[] to = {R.id.textView_artist, R.id.textView_album, R.id.textView_year, R.id.textView_editor, R.id.textView_ratingBar};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), simpleAdapterData, R.layout.listview_img, from, to);
        return simpleAdapter;
    }

    //botão do limpar a ListView
    public void Clear_list (View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                ListView lv = (ListView) findViewById(R.id.listView);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, musicas);
                lv.setAdapter(adapter);

                adapter.clear();

                Toast.makeText(MainActivity.this,R.string.limp_s, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        // Set other dialog properties
        builder.setTitle(R.string.Aviso);
        builder.setMessage(R.string.Aviso_perg);
        builder.setIcon(R.drawable.aviso);
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void Reset (View view){
        ListView lv = (ListView) findViewById(R.id.listView);
        SimpleAdapter adapter = createSimpleAdapter(musicas);
        lv.setAdapter(adapter);
    }

    //Parte do menu da aplicação
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_pt:
                AlertDialog.Builder builder_pt = new AlertDialog.Builder(this);
                builder_pt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        Toast.makeText(MainActivity.this,"App em português", Toast.LENGTH_SHORT).show();
                    }
                });
                builder_pt.setTitle("Português");
                builder_pt.setMessage("Você alterou a app para português");
                builder_pt.setIcon(R.drawable.pt);

                AlertDialog dialog_pt = builder_pt.create();
                dialog_pt.show();
                return true;
            case R.id.action_En:
                final AlertDialog.Builder builder_en = new AlertDialog.Builder(this);
                builder_en.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        Toast.makeText(MainActivity.this,"App in english", Toast.LENGTH_SHORT).show();
                    }
                });
                builder_en.setTitle("English");
                builder_en.setMessage("You changed the app to English");
                builder_en.setIcon(R.drawable.en);

                AlertDialog dialog_en = builder_en.create();
                dialog_en.show();
                return true;
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "Option application", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_exit:
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
