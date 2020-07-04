import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class fileIO {
    private final int bufferSIZE=360;
    private dictionaryIO dio;
    private FileChannel fc;
    private long currentFilePos=0;
    private ByteBuffer bf;
    private static String fileLocation=".txt";

    public fileIO()
    {
        bf=ByteBuffer.allocate(bufferSIZE);
    }

    public boolean readFromDB(String prefix)
    {
        dio=null;
        bf.clear();
        currentFilePos=0;
        fileLocation=prefix+".txt";
        try(FileInputStream fis=new FileInputStream(fileLocation);)
        {
            int j=0, toIterate;
            fc=fis.getChannel();
            bf.clear();
            while (true)
            {
                fc.position(currentFilePos);
                int read=fc.read(bf);
                if(read < 0) return false;
                if(read < bufferSIZE)
                {
                    read=0;
                    toIterate=bf.position();
                }
                else
                {
                    int cliff=adj(bf);
                    bf.position(bf.position()-cliff-1);
                    fc.position(fc.position()-cliff-1);
                    toIterate=bf.position();
                }

                bf.flip();
                dictionaryIO dio;
                for(byte i=0; bf.position()<toIterate; i++)
                {
                    dio=dictionaryIO.dictionRead(bf);
                    if(dio == null)
                    {
                        fc.position(fc.position()-(long)(toIterate-bf.position()));
                        break;
                    }
                    System.out.println("ID:["+j+"]"+dio);
                }
                bf.clear();

                if(read == 0) return true;
                currentFilePos=fc.position();
            }
        }
        catch (IOException e)
        {
            System.out.println("dictionaryIO readFromDB failed."+e);
        }

        bf.clear();
        return false;
    }

    public boolean addToDB(HashMap<String,String> entries)
    {
        dio=null;
        bf.clear();
        currentFilePos=0;
        dio=null;
        for(Map.Entry<String,String> k:entries.entrySet())
        {
            bf.clear();
            fileLocation=k.getKey().charAt(0)+".txt";
            try(FileOutputStream fos=new FileOutputStream(fileLocation,true))
            {
                fc=fos.getChannel();
                dio=dictionaryIO.createDictionEntry(k.getKey().toCharArray(),k.getValue().toCharArray());
                if(dio == null)
                {
                    bf.clear();
                    return false;
                }

                if(!dio.dictionWrite(bf))
                {
                    System.out.println("Failed addToDB in ["+k.getKey()+"]");
                    bf.clear();
                    return false;
                }
                bf.flip();
                fc.write(bf);
            }
            catch (IOException e)
            {
                System.out.println("addToDB Failure at ["+k.getKey()+"]"+e);
            }
        }
        bf.clear();
        return true;
    }

    public String searchFromDB(String word)
    {
        bf.clear();
        dio=null;
        currentFilePos=0;
        fileLocation=word.charAt(0)+".txt";
        try(FileInputStream fis=new FileInputStream(fileLocation);)
        {
            int j=0, toIterate;
            fc=fis.getChannel();
            bf.clear();
            while (true)
            {
                fc.position(currentFilePos);
                int read=fc.read(bf);
                if(read < 0) return "NOT FOUND";
                if(read < bufferSIZE) read=0;
                else
                {
                    int cliff=adj(bf);
                    bf.position(bf.position()-cliff-1);
                    fc.position(fc.position()-cliff-1);
                }
                toIterate=bf.position();

                bf.flip();
                for(byte i=0; bf.position()<toIterate; i++)
                {
                    dio=dictionaryIO.getWord(bf,word.toCharArray());
                    if(dio == null)
                    {
                        fc.position(fc.position()-(long)(toIterate-bf.position()));
                        break;
                    }
                    else
                        {
                            System.out.println("ID:[" + j + "]" + dio);
                            return dio.toString();
                    }
                }

                bf.clear();

                if(read == 0) return "NOT FOUND";
                currentFilePos=fc.position();
            }
        }
        catch (IOException e)
        {
            System.out.println("dictionaryIO readFromDB failed."+e);
        }

        return "NOT FOUND";
    }

    private int adj(ByteBuffer bf)
    {
        int currentPos=bf.position()-1;
        int indx=currentPos-1;
        byte x=bf.get(indx--);
        while(bf.get(indx) != dictionaryIO.iToken)
        {
            indx--;
        }

        return currentPos-indx;
    }
}
