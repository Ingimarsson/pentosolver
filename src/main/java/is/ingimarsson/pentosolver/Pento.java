// Hvert tilvik af Pento stendur fyrir fimmferning (pentomino),
// þ.e. fimm samloðandi ferninga, til dæmis
//
//               #         #                                  #
//    ##### eða ### eða #### eða #### en ekki, til dæmis, ####
//               #               #
//
// Aftasta dæmið er ekki löglegur fimmferningur því hann er ekki
// samloðandi á hliðum ferninganna.
// Fyrir hvern slíkan fimmferning höfum við fimm hnit (x,y) fyrir
// ferningana sem fimmferningurinn samanstendur af. Við röðum þessum
// fimm hnitum í nokkurs konar stafrófsröð og heimtum að fyrsta
// hnitið sé (0,0).  Stafrófsröðin felst í því að 
//   (x1,y1) < (x2,y2) <==> y1 < y2 eða (y1 = y2 og x1 < x2).
// Skilyrðið að fyrsta hnitið sé (0,0) kemur ekki í veg fyrir að
// við getum táknað hvaða fimmferning sem er, heldur veldur því
// að hver fimmferningur (eða snúningur á fimmferningi eða speglun
// á fimmferningi) hefur eina og aðeins eina táknun sem fimm
// mismunandi hnit í vaxandi röð með fyrsta hnitið sem (0,0).
// Fyrir umfjöllun um fimmferninga (pentomino) má kíkja á vefsíðurnar
//   https://web.ma.utexas.edu/users/smmg/archive/1997/radin.html
// og
//   https://en.wikipedia.org/wiki/Pentomino

// Höfundur: Snorri Agnarsson, 2020

import java.util.*;
import java.awt.*;
import java.awt.print.*;

// Tilvik af Pento standa fyrir fimmferning í einhverju
// snúningsástandi eða speglunarástandi.
public class Pento
{
    private int[] x = new int[4];
    private int[] y = new int[4];
    private final char n;
    // Fastayrðing gagna.
    //  Ferningarnir í fimmferningnum hafa hnitin
    //      (0,0),(x[0],y[0]),...,(x[3],y[3])
    //  og tryggt er að y[0],...,y[3] eru >=0.
    //  Stafbreytan n inniheldur þann staf sem er
    //  rétt nafn fimmferningsins, einn af stöfunum
    //  F,I,L,P,N,T,U,V,W,X,Y,Z.
    //  Hnitin
    //      (0,0),(x[0],y[0]),...,(x[3],y[3])
    //  eru í vaxandi röð miðað við röðina sem
    //  skilgreind er að ofan.
    
    // Notkun: Pento p = new Pento(n,x,y);
    // Fyrir:  x og y skilgreina löglegan fimmferning og
    //         n er stafurinn sem er nafn þess fimmfernings.
    // Eftir:  p vísar á tilvik af Pento sem stendur fyrir
    //         viðkomandi fimmferning.
    private Pento( char n, int[] x, int[] y )
    {
        this.x = x;
        this.y = y;
        this.n = n;
    }
    
    // Notkun: Pento p = new Pento(n,s);
    // Fyrir:  Sama og fyrir new Pento(n,s,"","")
    // Eftir:  Sama og fyrir new Pento(n,s,"","")
    // Ath.:   n hlýtur að vera 'I' og s hlýtur
    //         að vera "#####", eða svipað, svo
    //         sem " ##### ".
    public Pento( char n, String s )
    {
        this(n,s,"","");
    }
    
    // Notkun: Pento p = new Pento(n,s1,s2);
    // Fyrir:  Sama og fyrir new Pento(n,s1,s2,"")
    // Eftir:  Sama og fyrir new Pento(n,s1,s2,"")
    public Pento( char n, String s1, String s2 )
    {
        this(n,s1,s2,"");
    }
    
    // Notkun: Pento p = new Pento(n,s1,s2,s3);
    // Fyrir:  n er löglegt nafn á fimmferningi og
    //         strengirnir s1,s2,s3 innihalda stafi '#'
    //         þar sem ferningar í fimmferningnum hafa
    //         hnit og innihalda stafi ' ' annars staðar.
    //         Ef strengirnir s1,s2,s3 eru skrifaðir,
    //         línu eftir línu, þá sést lögun
    //         fimmferningsins.
    // Eftir:  p vísar á tilvik af Pento sem samsvarar
    //         þeirri lögun sem s1,s2,s3 lýsa.
    public Pento( char n, String s1, String s2, String s3 )
    {
        this.n = n;
        int count = 0;
        int firstX = 0;
        for( int i=0 ; i!=s1.length() ; i++ )
        {
            if( s1.charAt(i) == ' ' ) continue;
            if( s1.charAt(i) != '#' ) throw new Error();
            if( count == 5 ) throw new Error();
            if( count == 0 )
            {
                firstX = i;
            }
            else
            {
                x[count-1] = i-firstX;
                y[count-1] = 0;
            }
            count++;
        }
        if( count == 0 ) throw new Error();
        for( int i=0 ; i!=s2.length() ; i++ )
        {
            if( s2.charAt(i) == ' ' ) continue;
            if( s2.charAt(i) != '#' ) throw new Error();
            if( count == 5 ) throw new Error();
            x[count-1] = i-firstX;
            y[count-1] = 1;
            count++;
        }
        for( int i=0 ; i!=s3.length() ; i++ )
        {
            if( s3.charAt(i) == ' ' ) continue;
            if( s3.charAt(i) != '#' ) throw new Error();
            if( count == 5 ) throw new Error();
            x[count-1] = i-firstX;
            y[count-1] = 2;
            count++;
        }
    }

    // Notkun: int x = p.getX(i);
    // Fyrir:  p vísar á löglegt tilvik af Pento,
    //         0 <= i <=4
    // Eftir:  x inniheldur x-hnit fernings i innan p.
    public int getX( int i )
    {
        if( i==0 ) return 0;
        return x[i-1];
    }
    
    // Notkun: int y = p.getY(i);
    // Fyrir:  p vísar á löglegt tilvik af Pento,
    //         0 <= i <=4
    // Eftir:  y inniheldur y-hnit fernings i innan p.
    public int getY( int i )
    {
        if( i==0 ) return 0;
        return y[i-1];
    }

    // Notkun: boolean eq = p1.equals(p2);
    // Fyrir:  p1 og p2 vísa á lögleg tilvik af Pento.
    // Eftir:  eq inniheldur true þá og því aðeins að
    //         p1 og p2 séu eins, þ.e. séu sami
    //         fimmferningur í sama snúningsástandi og
    //         speglunarástandi, þ.e. að hnit allra
    //         ferninganna í p1 og p2 séu sömu.
    public boolean equals( Object f )
    {
        if( f == this ) return true;
        if( f.getClass() != this.getClass() ) return false;
        Pento fp = (Pento)f;
        for( int i=1 ; i!=5 ; i++ )
        {
            // Búið er að staðfesta að this.getX(j)==f.getX(j)
            // og this.getY(j)==f.getY(j) fyrir j=0,...,i-1
            if( getX(i) != fp.getX(i) ) return false;
            if( getY(i) != fp.getY(i) ) return false;
        }
        return true;
    }

    // Notkun: int h = p.hashCode();
    // Fyrir:  p vísar á tilvik af Pento.
    // Eftir:  h er tætigildi sem tryggt er að uppfylli
    //         samninginn fyrir hashCode(), sem segir
    //         að ef
    //            p1.equals(p2)
    //         þá er
    //            p1.hashCode() == p2.hashCode()
    public int hashCode()
    {
        int res = 0;
        int[] mx = new int[]{3,5,7,11};
        int[] my = new int[]{13,17,19,23};
        for( int i=0 ; i!=4 ; i++ )
        {
            res += x[i]*mx[i];
            res += y[i]*my[i];
        }
        return res;
    }
    
    // Notkun: boolean l = less(a,b,i,j);
    // Fyrir:  a[i],a[j],b[i],b[j] eru til.
    // Eftir:  l er satt þá og því aðeins að
    //           (a[i],b[i]) < (a[j],b[j])
    //         miðað við röðina sem skilgreind
    //         er fyrir hnit að ofan.
    private static boolean less( int[] a, int[] b, int i, int j )
    {
        if( b[i] < b[j] ) return true;
        if( b[i] > b[j] ) return false;
        return a[i] < a[j];
    }
    
    // Notkun: boolean l = less(a,b,i,j);
    // Fyrir:  a[i],a[j],b[i],b[j] eru til.
    // Eftir:  Búið er að víxla a[i] með a[j]
    //         og b[i] með b[j].
    private static void swap( int[] a, int[] b, int i, int j )
    {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        t = b[i];
        b[i] = b[j];
        b[j] = t;
    }

    // Notkun: sort5(a,b);
    // Fyrir:  a og b eru int[] með 5 sætum.
    // Eftir:  Búið er að endurraða a og b í takt
    //         þannig að hnitasafnið
    //           {(a[0],b[0]),...,(a[4],b[4])}
    //         er óbreytt, en er nú í vaxandi röð.
    private static void sort5( int[] a, int[] b )
    {
        if( less(a,b,1,0) ) swap(a,b,1,0);
        if( less(a,b,2,0) ) swap(a,b,2,0);
        if( less(a,b,3,0) ) swap(a,b,3,0);
        if( less(a,b,4,0) ) swap(a,b,4,0);
        if( less(a,b,2,1) ) swap(a,b,2,1);
        if( less(a,b,3,1) ) swap(a,b,3,1);
        if( less(a,b,4,1) ) swap(a,b,4,1);
        if( less(a,b,3,2) ) swap(a,b,3,2);
        if( less(a,b,4,2) ) swap(a,b,4,2);
        if( less(a,b,4,3) ) swap(a,b,4,3);
    }
    
    // Notkun: normalize(a,b);
    // Fyrir:  a og b eru int[5] sem innihalda hnit
    //          (a[0],b[0]),...,(a[4],b[4])
    //         sem skilgreina löglegan fimmferning
    //         einhvers staðar í hnitakerfinu.
    // Eftir:  Búið er að færa fimmferninginn til í
    //         hnitakerfinu, ásamt því að endurraða
    //         punktunum þannig að lögunin er óbreytt,
    //         en (a[0],b[0]) == (0,0) og punktarnir
    //          (a[0],b[0]),...,(a[4],b[4])
    //         eru nú í vaxandi röð.
    private static void normalize( int[] a, int[] b )
    {
        sort5(a,b);
        int minb = b[0], mina = a[0];
        for( int i=1 ; i!=5 ; i++ )
        {
            if( b[i] < minb )
            {
                mina = a[i];
                minb = b[i];
            }
        }
        for( int i=0 ; i!=5 ; i++ )
        {
            a[i] = a[i]-mina;
            b[i] = b[i]-minb;
        }
    }

    // Notkun: Pento p2 = flip(p1);
    // Fyrir:  p1 er tilvísun á löglegan fimmferning.
    // Eftir:  p2 er tilvísun á nýjan löglegan fimmferning
    //         sem er útkoman þegar p1 er speglaður um y-ásinn.
    static Pento flip( Pento f )
    {
        int[] a = new int[5];
        int[] b = new int[5];
        for( int i=0 ; i!=5 ; i++ )
        {
            a[i] = -f.getX(i);
            b[i] = f.getY(i);
        }
        sort5(a,b);
        normalize(a,b);
        int[] ap = new int[4];
        int[] bp = new int[4];
        for( int i=0 ; i!=4 ; i++ )
        {
            ap[i] = a[i+1];
            bp[i] = b[i+1];
        }
        return new Pento(f.n,ap,bp);
    }
    
    // Notkun: Pento p2 = flip(p1);
    // Fyrir:  p1 er tilvísun á löglegan fimmferning.
    // Eftir:  p2 er tilvísun á nýjan löglegan fimmferning
    //         sem er útkoman þegar p1 er snúið 90 gráður.
    //         Snúningurinn er réttsælis sé miðað við
    //         hnitakerfi þar sem x-ásinn ´visar til hægri
    //         og y-ásinn vísar niður.
    static Pento rotate( Pento f )
    {
        int[] a = new int[5];
        int[] b = new int[5];
        for( int i=0 ; i!=5 ; i++ )
        {
            a[i] = -f.getY(i);
            b[i] = f.getX(i);
        }
        sort5(a,b);
        normalize(a,b);
        int[] ap = new int[4];
        int[] bp = new int[4];
        for( int i=0 ; i!=4 ; i++ )
        {
            ap[i] = a[i+1];
            bp[i] = b[i+1];
        }
        return new Pento(f.n,ap,bp);
    }

    // Notkun: Pento[] v = variants(f);
    // Fyrir:  f vísar á löglegan Pento hlut.
    // Eftir:  v vísar á fylki sem inniheldur löglega
    //         Pento hluti sem eru allir snúningar og
    //         allar speglanir á f, þar með talið f
    //         sjálfur.  Engir snúningar eða speglanir
    //         eru endurteknar.
    public static Pento[] variants( Pento f )
    {
        HashSet<Pento> s = new HashSet<Pento>();
        for( int i=0 ; i!=4 ; i++ )
        {
            s.add(f);
            f = rotate(f);
        }
        f = flip(f);
        for( int i=0 ; i!=4 ; i++ )
        {
            s.add(f);
            f = rotate(f);
        }
        Pento[] res = new Pento[s.size()];
        int count = 0;
        for( Pento fp: s )
        {
            res[count++] = fp;
        }
        return res;
    }
    
    // Notkun: Pento[][] a = Pento.generateAll();
    // Fyrir:  Ekkert.
    // Eftir:  a vísar á nýtt 12 staka fylki af fylkjum af Pento hlutum, sem allir
    //         eru mismunandi. Hins vegar inniheldur a[i], fyrir i=0,...,11 fylki
    //         af Pento hlutum sem eru annaðhvort snúningar eða speglanir af hvorum
    //         öðrum.  Allir mögulegir Pento hlutir eru einhvers staðar í a. Allir
    //         snúningarinir og allar speglanirnar eru mismunandi.
    public static Pento[][] generateAll()
    {
        Pento[][] f = new Pento[12][];
        f[0] = variants(new Pento('F'," ##"
                                     ,"##"
                                     ," #"
                                 )
                       );
        f[1] = variants(new Pento('I',"#####"));
        
        f[2] = variants(new Pento('L',"####"
                                     ,"#"
                                     )
                       );
        f[3] = variants(new Pento('P',"###"
                                     ,"##"
                                 )
                       );
        f[4] = variants(new Pento('N',"  ##"
                                     ,"###"
                                 )
                       );
        f[5] = variants(new Pento('T',"###"
                                     ," #"
                                     ," #"
                                 )
                       );
        f[6] = variants(new Pento('U',"###"
                                     ,"# #"
                                 )
                       );
        f[7] = variants(new Pento('V',"###"
                                     ,"#"
                                     ,"#"
                                 )
                       );
        f[8] = variants(new Pento('W',"##"
                                     ," ##"
                                     ,"  #"
                                 )
                       );
        f[9] = variants(new Pento('X'," #"
                                     ,"###"
                                     ," #"
                                 )
                       );
        f[10] = variants(new Pento('Y',"####"
                                      ," #"
                                  )
                        );
        f[11] = variants(new Pento('Z',"##"
                                      ," #"
                                      ," ##"
                                  )
                        );
        return f;
    }
    
    // Notkun: boolean oob = outOfBounds(board,x,y);
    // Fyrir:  x og y eru heiltölur, board er char[][] sem inniheldur
    //         engin null undirfylki.
    // Eftir:  oob er satt þá og því aðeins að board[x][y] sé
    //         ekki lögleg tilvísun í stafasæti.
    public static boolean outOfBounds( char[][] board, int x, int y )
    {
        if( x >= board.length ) return true;
        if( x < 0 ) return true;
        if( y >= board[x].length ) return true;
        return false;
    }

    // Notkun: boolean ok = insert(b,x,y,p);
    // Fyrir:  b er char[][] með engin null undirfylki.
    //         x og y eru heiltölur.
    //         p er tilvísun á löglegan Pento hlut.
    // Eftir:  ok er satt þá og því aðeins að sætin
    //         í b sem p tekur, ef fyrsti punktur hans
    //         er settur í (x,y) hafi öll innihaldið ' '
    //         þegar kallað var.  Þá er einnig búið að setja
    //         stafinn sem einkennir p í þau sæti.  Að öðrum
    //         kosti er ok ósatt og b er óbreytt.
    public static boolean insert( char[][] b, int x, int y, Pento p )
    {
        if( outOfBounds(b,p.getX(1)+x,p.getY(1)+y) ) return false;
        if( outOfBounds(b,p.getX(2)+x,p.getY(2)+y) ) return false;
        if( outOfBounds(b,p.getX(3)+x,p.getY(3)+y) ) return false;
        if( outOfBounds(b,p.getX(4)+x,p.getY(4)+y) ) return false;
        if( b[p.getX(1)+x][p.getY(1)+y] != ' ' ) return false;
        if( b[p.getX(2)+x][p.getY(2)+y] != ' ' ) return false;
        if( b[p.getX(3)+x][p.getY(3)+y] != ' ' ) return false;
        if( b[p.getX(4)+x][p.getY(4)+y] != ' ' ) return false;
        b[x][y] = p.n;
        b[p.getX(1)+x][p.getY(1)+y] = p.n;
        b[p.getX(2)+x][p.getY(2)+y] = p.n;
        b[p.getX(3)+x][p.getY(3)+y] = p.n;
        b[p.getX(4)+x][p.getY(4)+y] = p.n;
        return true;
    }
    
    // Notkun: remove(b,x,y,p);
    // Fyrir:  b er borð sem inniheldur p með minnstu hnit í (x,y).
    // Eftir:  Búið er að fjarlægja p af b og setja ' ' í hnitin sem
    //         áður innihéldu stafinn sem einkennir p.
    public static void remove( char[][] b, int x, int y, Pento p )
    {
        b[x][y] = ' ';
        b[p.getX(1)+x][p.getY(1)+y] = ' ';
        b[p.getX(2)+x][p.getY(2)+y] = ' ';
        b[p.getX(3)+x][p.getY(3)+y] = ' ';
        b[p.getX(4)+x][p.getY(4)+y] = ' ';
    }

    // Notkun: generateSolutions(a,board,used,iter,partial);
    // Fyrir:  a er Pento[][] sem inniheldur alla fimmferninga,
    //         eins og útkoman úr generateAll().
    //         board er char[][].
    //         used er boolean[] af stærð 12.
    //         iter vísar á flakkara af tagi MyIterator.
    //         partial er boolean gildi.
    // Eftir:  Búið er að senda allar lausnir inn í iter með
    //         put() boðinu í iter, þar sem lausnirnar uppfylla
    //         það skilyrði að búið er að fylla út í öll sæti í
    //         board sem innihalda ' ' með því að setja á borðið
    //         í mesta lagi eitt af hverjum þeim Pento hlutum (í
    //         einhverjum snúningi eða speglun) sem ekki hafa true
    //         í því sæti í used sem samsvarar hlutnum, þannig að
    //         hlutirnir þekji nákvæmlega alla þá reiti sem
    //         innihéldur ' '.
    //         Ef partial er satt (true) þá eru ekki einungis
    //         fullar lausnir sendar í flakkarann iter heldur
    //         einnig þau borð sem verða til þegar leitað er,
    //         þar sem ekki er búið að fylla í alla auða reiti.
    //         Borðið board og fylkið used eru óbreytt frá því
    //         fyrir kallið.
    public static void generateSolutions( Pento[][] a, char[][] board, boolean[] used, MyIterator iter, boolean partial )
        throws InterruptedException
    {
        int x=0,y=0;
        boolean found=false;
        for( int i=0 ; i!=board[0].length && !found ; i++ )
        {
            for( int j=0 ; j!=board.length && !found ; j++ )
            {
                if( board[j][i] == ' ' )
                {
                    found=true;
                    x = j;
                    y = i;
                }
            }
        }
        if( !found )
        {
            if( !partial ) iter.put(copy(board));
            return;
        }
        for( int i=0 ; i!=used.length ; i++ )
        {
            if( used[i] ) continue;
            for( int j=0 ; j!=a[i].length ; j++ )
            {
                if( insert(board,x,y,a[i][j]) )
                {
                    if( partial ) iter.put(copy(board));
                    used[i] = true;
                    generateSolutions(a,board,used,iter,partial);
                    remove(board,x,y,a[i][j]);
                    used[i] = false;
                }
            }
        }
    }
    
    // Notkun: String[] b = copy(board);
    // Fyrir:  board er char[][] sem samsvarar ástandi á þrautaborði.
    // Eftir:  b er String[] sem samsvarar board.
    private static String[] copy( char[][] b )
    {
        String[] b2 = new String[b.length];
        for( int i=0 ; i!=b.length ; i++ ) b2[i] = new String(b[i]);
        return b2;
    }
    
    // Notkun: char[][] b = makeBoard(board);
    // Fyrir:  board er String[] sem samsvarar fimmferningaþraut.
    // Eftir:  b er char[][] sem samsvarar sömu þraut.
    public static char[][] makeBoard( String... s )
    {
        char[][] board = new char[s.length][];
        for( int i=0 ; i!=s.length ; i++ )
        {
            board[i] = new char[s[i].length()];
            for( int j=0 ; j!=s[i].length() ; j++ )
                board[i][j] = s[i].charAt(j);
        }
        return board;
    }
    
    // Hlutir af tagi MyIterator eru flakkarar (iterators) sem
    // framleiða allar lausnir, án endurtekninga, fyrir þrautina
    // sem felst í að fylla út í alla auða reiti á borði með
    // fimmferningum án þess að nota neinn fimmferning oftar en
    // einu sinni.
    private static class MyIterator implements Iterator<String[]>
    {
        private String[] board = null;
        private boolean hasValue = false;
        private boolean done = false;
        // Fastayrðing gagna:
        //  Búið er að ræsa þráð sem framleiðir allar lausnir og
        //  setur þær í þennan hlut gegnum put() boðið.
        //
        //  Búið er að sækja núll eða fleiri slíkar lausnir.  Síðasta
        //  gildið sem framleiðsluþráðurinn sendir í put var eða verður
        //  new String[1], þ.e. fylki af tagi String sem inniheldur
        //  null í sæti 0.
        //  
        //  Búið er að flakka gegnum núll eða fleiri af lausnunum
        //  sem framleiddar eru. Ef næsta lausn á eftir er komin frá
        //  framleiðsluþræðinum þá er hún í tilviksbreytunni board
        //  og þá er hasValue satt.  Annars er hasValue ósatt.
        //  Ef búið er að flakka yfir allar lausnir þá er done satt,
        //  annars er done ósatt.
        
        // Notkun: MyIterator it = new MyIterator(board,partial);
        // Fyrir:  board er String[] sem samsvarar fimmferningarþraut.
        // Eftir:  it vísar á nýjan flakkara sem skilar öllum lausnum
        //         á þrautinni og ef partial er satt einnig þeim öðrum
        //         borðum sem verða til í leit að lausn.
        private MyIterator( String[] board, boolean partial )
        {
            Runnable r =
                ()->
                {
                    try
                    {
                        Pento[][] a = generateAll();
                        boolean[] used = new boolean[12];
                        for( int i=0 ; i!=12 ; i++ ) used[i] = false;
                        generateSolutions(a,makeBoard(board),used,MyIterator.this,partial);
                        MyIterator.this.put(new String[1]);
                    }
                    catch( InterruptedException e )
                    {
                        e.printStackTrace();
                    }
                };
            new Thread(r).start();
        }

        // Notkun: it.put(board);
        // Fyrir:  it er flakkari sem flakkað hefur yfir núll
        //         eða fleiri borð innan runu sinna lausna.
        //         board er næsta lausn sem it ætti að skila.
        // Eftir:  Flakkarinn hefur tekið við board og mun
        //         koma þeirri lausn til skila ef beðið er um.
        private synchronized void put( String[] board )
            throws InterruptedException
        {
            while( hasValue ) this.wait();
            this.board = board;
            if( board[0] == null )
                done = true;
            else
                hasValue = true;
            this.notifyAll();
        }

        // Notkun: boolean hn = it.hasNext();
        // Fyrir:  it vísar á MyIterator.
        // Eftir:  hn er satt þá og því aðeins að flakkarinn
        //         geti skilað a.m.k. einni enn lausn með
        //         kalli á next().
        public synchronized boolean hasNext()
        {
            try
            {
                while( !hasValue && !done ) wait();
                return !done;
            }
            catch( InterruptedException e )
            {
                return false;
            }
        }

        // Notkun: String[] b = it.next();
        // Fyrir:  Búið er að staðfesta með kalli á it.hasNext()
        //         að it geti skilað a.m.k. einni enn lausn.
        // Eftir:  b vísar á nýja lausn sem er næsta lausn í
        //         runu þeirra lausna sem it skilar.
        public synchronized String[] next()
        {
            if( done || !hasValue ) throw new Error();
            String[] res = board;
            hasValue = false;
            notifyAll();
            return res;
        }
    }
    
    // Tilvik af MyIterable eru flakkanleg söfn af lausnum á
    // tiltekinni fimmferningaþraut.
    public static class MyIterable implements Iterable<String[]>
    {
        private final String[] board;
        private final boolean partial;
        // Fastayrðing gagna.
        //   board inniheldur þrautina sem verið er að leysa.

        // Notkun: MyIterable i = new MyIterable(board);
        // Fyrir:  board er fimmferningaþraut.
        // Eftir:  i er flakkanlegt safn allra lausna á board.
        public MyIterable( String[] board, boolean partial )
        {
            this.board = board;
            this.partial = partial;
        }

        // Notkun: Iterator<String[]> it = i.iterator();
        // Fyrir:  i er tilvik af MyIterable.
        // Eftir:  it er flakkari sem skilar öllum lausnum
        //         á fimmferningaþrautinni sem i leysir.
        public Iterator<String[]> iterator()
        {
            return new MyIterator(board,partial);
        }
    }
    
    // Notkun: Iterable<String[]> iterable = makeSolutions(board);
    // Fyrir:  board er strengjafylki og ekkert sæti inniheldur null.
    // Eftir:  iterable er flakkanlegt safn allra lausna á þrautinni
    //         sem felst í að fylla út í öll auð sæti (sæti með
    //         bilstaf) í tvívíða fylkinu af stöfum sem board
    //         samsvarar, með fimmferningum á þann hátt að enginn
    //         fimmferningur er notaður oftar en einu sinni.
    // Ath.:   Dæmigerð notkun er í for-lykkju, svona:
    //            for( String[] b: makeSolutions(board) )
    //            {
    //                ...meðhöndla lausnina b...
    //            }
    //         Til dæmis má telja allar lausnir svona:
    //            int n=0;
    //            for( String[] b: makeSolutions(board) )
    //            {
    //                n++;
    //            }
    //            // n inniheldur nú fjölda lausna fyrir board
    //         Einnig má prenta allar lausnir svona:
    //            for( String[] b: makeSolutions(board) )
    //            {
    //                for( String s: b ) System.out.println(s);
    //                System.out.println();
    //            }
    public static MyIterable makeSolutions( String... board )
    {
        return new MyIterable(board,false);
    }
    
    // Notkun: Iterable<String[]> iterable = makePartialSolutions(board);
    // Fyrir:  board er strengjafylki og ekkert sæti inniheldur null.
    // Eftir:  iterable er flakkanlegt safn allra lausna og hlutlausna
    //         á þrautinni sem felst í að fylla út í öll auð sæti (sæti
    //         með bilstaf) í tvívíða fylkinu af stöfum sem board
    //         samsvarar, með fimmferningum á þann hátt að enginn
    //         fimmferningur er notaður oftar en einu sinni.
    public static MyIterable makePartialSolutions( String... board )
    {
        return new MyIterable(board,true);
    }
    
    public static void main( String[] args )
        throws InterruptedException
    {
        String[] board;
        /*
        board = new String[]{ "*         *"
                            , "   * * *   "
                            , " *       * "
                            , "   * * *   "
                            , " *       * "
                            , "   * * *   "
                            , "*         *"
                            };
        */
        board = new String[]{ "           "
                            , "           "
                            , "           "
                            , "   ******* "
                            , "   ******* "
                            , "   ******* "
                            , "   ******* "
                            , "           "
                            };
        /*
        board = new String[]{ "*  ***    *"
                            , "  ***      "
                            , "  ***      "
                            , "   ***     "
                            , "     ***   "
                            , "      ***  "
                            , "      ***  "
                            , "*    ***  *"
                            };
        */
        /*
        board = new String[]{ "*  ***    *"
                            , "  ***      "
                            , "  ***      "
                            , "   ***     "
                            , "     ***   "
                            , "      ***  "
                            , "      ***  "
                            , "*    ***  *"
                            };
        */
        /*
        board = new String[]{ "***   ***"
                            , "***   ***"
                            , "***   ***"
                            , "         "
                            , "         "
                            , "         "
                            , "***   ***"
                            , "***   ***"
                            , "***   ***"
                            };
        */
        //board = new String[]{"                    ","                    ","                    "};
        //board = new String[]{"**                    ","*                    *","                    **"};
        //board = new String[]{"   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   ","   "};
        for( String[] b: makeSolutions(board) )
        {
            for( String s: b ) System.out.println(s);
            System.out.println();
        }
        int n = 0;
        board = new String[]{ "        "
                            , "        "
                            , "        "
                            , "   **   "
                            , "   **   "
                            , "        "
                            , "        "
                            , "        "
                            };
        for( String[] b: makeSolutions(board) )
        {
            n++;
            if( n == 1 ) for( String s: b ) System.out.println(s);
        }
        System.out.println(n);

        /*
        board = new String[]{"          ","          ","          ","          ","          ","          "};
        n = 0;
        for( String[] b: makePartialSolutions(board) )
        {
            for( int i=0 ; i!=b[0].length() ; i++ )
            {
                for( int j=0 ; j!=b.length ; j++ )
                    System.out.print(b[j].charAt(i));
                System.out.println();
            }
            System.out.println();
            n++;
        }
        System.out.println(n);
        */
    }
}
