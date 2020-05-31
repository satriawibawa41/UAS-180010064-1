package com.bc181.wibawa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "db_beritaku";
    private final static String TABLE_BERITA = "t_berita";
    private final static String KEY_ID_BERITA = "ID_Berita";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_CAPTION = "Caption";
    private final static String KEY_PENULIS = "Penulis";
    private final static String KEY_ISI_BERITA ="Isi_Berita";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm", Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx) {
        super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BERITA = "CREATE TABLE " + TABLE_BERITA
                + "(" + KEY_ID_BERITA + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL +  " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_CAPTION + " TEXT, "
                + KEY_PENULIS + " TEXT, " + KEY_ISI_BERITA + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BERITA);
        inisialisasiBeritaAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_BERITA;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void tambahBerita(Berita dataBerita){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBerita.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBerita.getTanggal()));
        cv.put(KEY_GAMBAR,dataBerita.getGambar());
        cv.put(KEY_CAPTION,dataBerita.getCaption());
        cv.put(KEY_PENULIS,dataBerita.getPenulis());
        cv.put(KEY_ISI_BERITA,dataBerita.getIsiBerita());
        cv.put(KEY_LINK,dataBerita.getLink());

        db.insert(TABLE_BERITA, null,cv);

    }

    public void tambahBerita(Berita dataBerita,SQLiteDatabase db){
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBerita.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBerita.getTanggal()));
        cv.put(KEY_GAMBAR,dataBerita.getGambar());
        cv.put(KEY_CAPTION,dataBerita.getCaption());
        cv.put(KEY_PENULIS,dataBerita.getPenulis());
        cv.put(KEY_ISI_BERITA,dataBerita.getIsiBerita());
        cv.put(KEY_LINK,dataBerita.getLink());

        db.insert(TABLE_BERITA, null,cv);

    }
    public void editBerita(Berita dataBerita){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataBerita.getJudul());
        cv.put(KEY_TGL, sdFormat.format(dataBerita.getTanggal()));
        cv.put(KEY_GAMBAR,dataBerita.getGambar());
        cv.put(KEY_CAPTION,dataBerita.getCaption());
        cv.put(KEY_PENULIS,dataBerita.getPenulis());
        cv.put(KEY_ISI_BERITA,dataBerita.getIsiBerita());
        cv.put(KEY_LINK,dataBerita.getLink());

        db.update(TABLE_BERITA, cv, KEY_ID_BERITA + "=?" , new String[]{String.valueOf(dataBerita.getIdBerita())});
        db.close();

    }

    public void hapusBerita(int idBerita){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BERITA, KEY_ID_BERITA + "=?", new String[]{String.valueOf(idBerita)});
        db.close();
    }

    public ArrayList<Berita> getAllBerita(){
        ArrayList<Berita> dataBerita = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_BERITA;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if (csr.moveToFirst()){
            do{
                Date tempDate = new Date();
                try{
                    tempDate = sdFormat.parse(csr.getString(2));
                }catch (ParseException er){
                    er.printStackTrace();
                }

                Berita tempBerita = new Berita(
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataBerita.add(tempBerita);
            }while (csr.moveToNext());
        }
        return dataBerita;
    }

    private String storeImageFile(int id){
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(),id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return location;
    }

    private void  inisialisasiBeritaAwal(SQLiteDatabase db) {
        int idBerita = 0;
        Date tempDate = new Date();

        //Menambah data berita ke -1
        try{
            tempDate = sdFormat.parse( "20/01/2020 12:00");
        }catch(ParseException er){
            er.printStackTrace();
        }
        Berita berita1 = new Berita(
                idBerita,
                "Like Me Better",
                tempDate,
                storeImageFile(R.drawable.album1),
                "Ilustrasi Album",
                "Lauv",
                "[Verse 1]\n" +
                        "To be young and in love in New York City\n" +
                        "To not know who I am but still know that I'm good long as you're here with me\n" +
                        "To be drunk and in love in New York City\n" +
                        "Midnight into morning coffee, burning through the hours talking\n" +
                        "Damn\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "I like me better when I'm with you\n" +
                        "I like me better when I'm with you\n" +
                        "I knew from the first time, I'd stay for a long time, 'cause\n" +
                        "I like me better when, I like me better when I'm with you\n" +
                        "\n" +
                        "[Verse 2]\n" +
                        "I don't know what it is but I got that feeling\n" +
                        "Waking up in this bed next to you swear the room, yeah, it got no ceiling\n" +
                        "If we lay, let the day just pass us by\n" +
                        "I might get to too much talking, I might have to tell you something\n" +
                        "Damn\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "I like me better when I'm with you\n" +
                        "I like me better when I'm with you\n" +
                        "I knew from the first time, I'd stay for a long time, 'cause\n" +
                        "I like me better when, I like me better when I'm with you\n" +
                        "\n" +
                        "\n" +
                        "[Bridge]\n" +
                        "Stay awhile, stay awhile\n" +
                        "Stay here with me\n" +
                        "Stay awhile, stay awhile, oh\n" +
                        "Stay awhile, stay awhile\n" +
                        "Stay here with me\n" +
                        "Lay here with me, ooh\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "I like me better when I'm with you (yes I do, yes I do, babe)\n" +
                        "I like me better when I'm with you (oooh noo)\n" +
                        "I knew from the first time, I'd stay for a long time, 'cause\n" +
                        "I like me better when, I like me better when I'm with you\n" +
                        "\n" +
                        "[Outro]\n" +
                        "(I like me, I like me)\n" +
                        "(Look who you made me, made me, oh noo)\n" +
                        "Better when\n" +
                        "I like me better when I'm with you",
                "https://genius.com/Lauv-i-like-me-better-lyrics"
        );

        tambahBerita(berita1, db);
        idBerita++;

        // Membentuk obyek data berita 2
        tempDate = new Date();
        try{
            tempDate = sdFormat.parse( "01/01/2014 12:00");
        }catch(ParseException er){
            er.printStackTrace();
        }
        Berita berita2 = new Berita(
                idBerita,
                "Good Things Fall Apart",
                tempDate,
                storeImageFile(R.drawable.album2),
                "Ilustrasi Album",
                "Illenium ft. Jon Bellion",
                "[Verse 1]\n" +
                        "Did I say something wrong? Did you hear what I was thinking?\n" +
                        "Did I talk way too long when I told you all my feelings that night?\n" +
                        "Is it you? Is it me? Did you find somebody better?\n" +
                        "Someone who isn't me, 'cause I know that I was never your type\n" +
                        "Never really your type\n" +
                        "\n" +
                        "[Pre-Chorus]\n" +
                        "Overthinking's got me drinking\n" +
                        "Messing with my head, whoa\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "Tell me what you hate about me\n" +
                        "Whatever it is, I'm sorry\n" +
                        "Yeah, yeah, yeah\n" +
                        "Yeah, yeah, yeah\n" +
                        "I know I can be dramatic\n" +
                        "But everybody said we had it\n" +
                        "Yeah, yeah, yeah\n" +
                        "Yeah, yeah, yeah\n" +
                        "I'm coming to terms with a broken heart\n" +
                        "I guess that sometimes good things fall apart\n" +
                        "\n" +
                        "[Verse 2]\n" +
                        "When you said it was real, guess I really did believe you\n" +
                        "Did you fake how you feel when we parked down by the river that night? That night?\n" +
                        "That night when we fogged up the windows in your best friend's car\n" +
                        "'Cause we couldn't leave the windows down in December\n" +
                        "Whoa\n" +
                        "\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "Tell me what you hate about me\n" +
                        "Whatever it is, I'm sorry\n" +
                        "Yeah, yeah, yeah\n" +
                        "Yeah, yeah, yeah\n" +
                        "I know I can be dramatic\n" +
                        "But everybody said we had it\n" +
                        "Yeah, yeah, yeah\n" +
                        "Yeah, yeah, yeah\n" +
                        "I'm coming to terms with a broken heart\n" +
                        "I guess that sometimes good things fall apart\n" +
                        "\n" +
                        "[Instrumental Bridge]\n" +
                        "\n" +
                        "[Pre-Chorus]\n" +
                        "Overthinking's got me drinking\n" +
                        "Messing with my head, oh\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "Tell me what you hate about me (Tell me)\n" +
                        "Whatever it is, I'm sorry (Oh, I'm sorry)\n" +
                        "Yeah, yeah, yeah (Oh, I'm sorry)\n" +
                        "Yeah, yeah, yeah\n" +
                        "I know I can be dramatic (I know I can be)\n" +
                        "But everybody said we had it (Woo)\n" +
                        "Yeah, yeah, yeah\n" +
                        "Yeah, yeah, yeah\n" +
                        "I'm coming to terms with a broken heart\n" +
                        "I guess that sometimes good things fall apart",
                "https://genius.com/Illenium-and-jon-bellion-good-things-fall-apart-lyrics"
        );

        tambahBerita(berita2, db);
        idBerita++;

        // Membentuk obyek data berita 3
        tempDate = new Date();
        try{
            tempDate = sdFormat.parse( "12/03/2020 22:46");
        }catch(ParseException er){
            er.printStackTrace();
        }
        Berita berita3 = new Berita(
                idBerita,
                "All We Know",
                tempDate,
                storeImageFile(R.drawable.album3),
                "Ilustrasi Album",
                "The Chainsmokers ft. Phoebe Ryan",
                "[Verse 1]\n" +
                        "Fighting flames with fire\n" +
                        "Hang onto burning wires\n" +
                        "We don't care anymore\n" +
                        "Are we fading lovers?\n" +
                        "We keep wasting colors\n" +
                        "Maybe we should let this go\n" +
                        "\n" +
                        "[Pre-Chorus]\n" +
                        "We're falling apart, still we hold together\n" +
                        "We've passed the end, so we chase forever\n" +
                        "'Cause this is all we know\n" +
                        "This feeling's all we know\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "I'll ride my bike up to the road\n" +
                        "Down the streets right through the city\n" +
                        "I'll go everywhere you go\n" +
                        "From Chicago to the coast\n" +
                        "You tell me, \"Hit this and let's go\n" +
                        "Blow the smoke right through the window\"\n" +
                        "'Cause this is all we know\n" +
                        "\n" +
                        "[Post-Chorus]\n" +
                        "'Cause this is all we know\n" +
                        "'Cause this is all we know\n" +
                        "\n" +
                        "\n" +
                        "[Verse 2]\n" +
                        "Never face each other\n" +
                        "One bed, different covers\n" +
                        "We don't care anymore\n" +
                        "Two hearts still beating\n" +
                        "On with different rhythms\n" +
                        "Maybe we should let this go\n" +
                        "\n" +
                        "[Pre-Chorus]\n" +
                        "We're falling apart, still we hold together\n" +
                        "We've passed the end, so we chase forever\n" +
                        "'Cause this is all we know\n" +
                        "This feeling's all we know\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "I'll ride my bike up to the road\n" +
                        "Down the streets right through the city\n" +
                        "I'll go everywhere you go\n" +
                        "From Chicago to the coast\n" +
                        "You tell me, \"Hit this and let's go\n" +
                        "Blow the smoke right through the window\"\n" +
                        "'Cause this is all we know\n" +
                        "\n" +
                        "[Post-Chorus]\n" +
                        "'Cause this is all we know\n" +
                        "'Cause this is all we know",
                "https://genius.com/The-chainsmokers-all-we-know-lyrics"

        );
        tambahBerita(berita3, db);
        idBerita++;

        // Membentuk obyek data berita 4
        tempDate = new Date();
        try{
            tempDate = sdFormat.parse( "13/03/2020 05:58");
        }catch(ParseException er){
            er.printStackTrace();
        }
        Berita berita4 = new Berita(
                idBerita,
                "Stranger Things",
                tempDate,
                storeImageFile(R.drawable.album4),
                "Ilustrasi Album",
                "Kygo ft. OneRepublic",
                "[Intro]\n" +
                        "The stranger things\n" +
                        "A life, a life of stranger things (Very far)\n" +
                        "For stranger things (Dreams of places I've)\n" +
                        "'Cause that's just who we are\n" +
                        "\n" +
                        "[Verse 1]\n" +
                        "We used to run around this ghost town\n" +
                        "Always thinking out loud\n" +
                        "How we gonna get out?\n" +
                        "I remember\n" +
                        "We dream of places that we could go\n" +
                        "Castles with the strange glow\n" +
                        "People that we don't know\n" +
                        "I remember\n" +
                        "\n" +
                        "[Pre-Chorus]\n" +
                        "We left a life\n" +
                        "That's ordinary from the start\n" +
                        "We look for stranger things\n" +
                        "'Cause that's just who we are\n" +
                        "Found me the edge of something beautiful and loud\n" +
                        "Like I'm picturing now\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "Castles glitter under Spanish skies\n" +
                        "But I'm just looking out for you tonight\n" +
                        "Snow white mountains in a foreign state\n" +
                        "Tell me someday we'll get there\n" +
                        "Someday\n" +
                        "Someday\n" +
                        "Oh-oh\n" +
                        "Oh-oh\n" +
                        "\n" +
                        "\n" +
                        "[Verse 2]\n" +
                        "I see your Technicolor shadow\n" +
                        "Underneath your window\n" +
                        "Just in case you don't know\n" +
                        "I can see it\n" +
                        "You cast an unfamiliar day glow\n" +
                        "Different than what I know\n" +
                        "Shining like a halo\n" +
                        "I can feel it\n" +
                        "\n" +
                        "[Pre-Chorus]\n" +
                        "We turned our back on ordinary from the start\n" +
                        "We looked for stranger things\n" +
                        "'Cause that's just who we are\n" +
                        "Found me the edge of something beautiful and loud\n" +
                        "Show me the sky falling down\n" +
                        "\n" +
                        "[Chorus]\n" +
                        "Castles glitter under Spanish skies\n" +
                        "But I'm just looking out for you tonight\n" +
                        "Snow white mountains in an ancient place\n" +
                        "Tell me someday we'll get there\n" +
                        "Someday\n" +
                        "Someday\n" +
                        "Oh oh\n" +
                        "Oh oh\n" +
                        "\n" +
                        "[Bridge]\n" +
                        "The stranger things\n" +
                        "A life, a life of stranger things (Very far)\n" +
                        "For stranger things (Dreams of places I've)\n" +
                        "'Cause that's just who we are\n" +
                        "A stranger things (very far)\n" +
                        "The stranger things\n" +
                        "A life, a life of stranger things (Very far)\n" +
                        "For stranger things (Dreams of places I've)\n" +
                        "'Cause that's just who we are\n" +
                        "The stranger things\n" +
                        "A life, a life of stranger things (Very far)\n" +
                        "For stranger things (Dreams of places I've)\n" +
                        "'Cause that's just who we are\n" +
                        "The stranger things\n" +
                        "A life, a life of stranger things (Very far)\n" +
                        "For stranger things (Dreams of places I've)\n" +
                        "'Cause that's just who we are\n" +
                        "\n" +
                        "[Outro]\n" +
                        "The stranger things\n" +
                        "A life, a life of stranger things (Very far)\n" +
                        "For stranger things (Dreams of places I've)\n" +
                        "'Cause that's just who we are",
                "https://genius.com/Kygo-stranger-things-lyrics"
        );
        tambahBerita(berita4, db);
    }

}
