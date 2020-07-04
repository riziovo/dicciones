import java.nio.ByteBuffer;

public class dictionaryIO {
    public final static int SIZE=1+(1+Dictionari.wordSIZE*2)+1+(1+Dictionari.meaningSIZE*2);
    public static final byte iToken=(byte)19;
    private static final byte meanToken=(byte)39;
    private Dictionari di;

    private dictionaryIO(char[] word,char[] meaning)
    {
        this.di=new Dictionari(word,meaning);
    }

    public static dictionaryIO createDictionEntry(char[] word, char[] meaning)
    {
        if(word.length < 1 || meaning.length< 1) return null;

        return new dictionaryIO(word,meaning);
    }

    public static dictionaryIO dictionRead(ByteBuffer bf)
    {
        if(bf.limit()-bf.position() < 1+(1+Dictionari.wordSIZE)+1+(1+Dictionari.meaningSIZE)) return null;

        if(bf.get() != iToken)
        {
            bf.position(bf.position()-1);
            return null;
        }

        byte wLen=bf.get();
        char[] word=new char[wLen];
        for(byte i=0; i< wLen; i++) word[i]=bf.getChar();
        bf.position(bf.position()+Dictionari.wordSIZE*2-wLen*2);

        if(bf.get()!= meanToken)
        {
            bf.position(bf.position()-1-Dictionari.wordSIZE*2-1-1);
            return null;
        }

        byte mLen=bf.get();
        char[] meaning=new char[mLen];
        for(byte i=0; i<mLen; i++) meaning[i]=bf.getChar();

        bf.position(bf.position()+Dictionari.meaningSIZE*2-mLen*2);
        return createDictionEntry(word, meaning);
    }

    public boolean dictionWrite(ByteBuffer bf)
    {
        if(bf.limit()-bf.position() < 1+(1+Dictionari.wordSIZE*2)+1+(1+Dictionari.meaningSIZE*2)) return false;

        bf.put(iToken);
        bf.put(di.wordLen);
        for(byte i=0; i< Dictionari.wordSIZE; i++) bf.putChar(di.word[i]);

        bf.put(meanToken);
        bf.put(di.meaningLen);
        for(byte i=0; i< Dictionari.meaningSIZE; i++) bf.putChar(di.meaning[i]);

        return true;
    }

    public static dictionaryIO getWord(ByteBuffer bf, char[] w)
    {
        int turns=(bf.limit()-bf.position())/SIZE;
        if(turns == 0 && bf.limit()-bf.position()>SIZE) turns = 1;
        if(turns < 1) return null;

        while(true)
        {
            int entryStart=bf.position();
            //turns--;
            if(bf.get() != iToken)
            {
                bf.position(bf.position()-1);
                return null;
            }
            //int entryStart=bf.position()-1;
            byte wLen=bf.get();
            if(w.length != wLen)
            {
                turns--;
                if(turns < 1)
                {
                    bf.position(bf.limit());
                    //bf.position(bf.limit());
                    //bf.position(entryStart+Dictionari.wordSIZE*2+1+1+Dictionari.meaningSIZE*2);
                    return null;
                }
                bf.position(entryStart+SIZE);
            }
            else {
                int initPos = bf.position()-1;
                byte i = 0;
                for (byte j=0; j < wLen; j++)
                {
                    if (w[j] != bf.getChar()) break;
                    i++;
                    System.out.println(w[j]);
                }

                if (i == w.length)
                {
                    skipWord_Buffer(bf,wLen);

                    if (bf.get() != meanToken)
                    {
                        //bf.position(bf.position() - Dictionari.wordSIZE*2-1-1);
                        //bf.position(bf.limit());
                        return null;
                    }

                    byte mLen = bf.get();
                    char[] meaning = new char[mLen];
                    for (byte j = 0; j < mLen; j++) meaning[j] = bf.getChar();

                    skipMean_Buffer(bf,mLen);
                    //bf.position(bf.position()+Dictionari.meaningSIZE*2-mLen*2);
                    return createDictionEntry(w,meaning);
                }
                else
                {
                    turns--;
                    if(turns < 1)
                    {
                        //bf.position(initPos+Dictionari.wordSIZE*2+1+1+Dictionari.meaningSIZE);
                        bf.position(bf.limit());
                        return null;
                    }
                    bf.position(entryStart+SIZE);
                }
            }
        }
    }


    private static void skipWord_Buffer(ByteBuffer bf, int wLen)
    {
        bf.position(bf.position()+Dictionari.wordSIZE*2-wLen*2);
    }
    private static void skipMean_Buffer(ByteBuffer bf, int wLen)
    {
        bf.position(bf.position()+Dictionari.meaningSIZE*2-wLen*2);
    }

    public String toString()
    {
        return this.di.getAll();
    }
}