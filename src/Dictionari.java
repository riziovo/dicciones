public class Dictionari {
    public static final byte wordSIZE=21;
    public static final byte meaningSIZE=40;
    public char[] word;
    public byte wordLen;
    public char[] meaning;
    public byte meaningLen;

    Dictionari(char[] word,char[] meaning)
    {
        this.word=new char[wordSIZE];
        this.wordLen=(byte)word.length;
        for(byte i=0; i< this.wordLen; i++) this.word[i]=word[i];

        this.meaning=new char[meaningSIZE];
        this.meaningLen=(byte)meaning.length;
        for(byte i=0; i< this.meaningLen; i++) this.meaning[i]=meaning[i];
    }

    public String getWord()
    {
        return new String(word);
    }

    public String getMeaning()
    {
        return new String(meaning);
    }
    public String getAll()
    {
        return new String(getWord()+"$"+getMeaning());
    }
}
