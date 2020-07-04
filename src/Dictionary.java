import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Dictionary {
    public static void main(String arg[]) throws IOException {
        Scanner scn=new Scanner(System.in);
        fileIO fio=new fileIO();
        int input=0, number=0;
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        while(input != 5)
        {
            System.out.println("+++++++++++++ S T A R T ++++++++++++++++");
            System.out.println("\n1. To Add Entries.\n2. To Print All Entries\n3. To Print Entries Of a Letter\n4. To Search\n5. Exit\nEnter:");
            input=scn.nextInt();

            if(input == 1)
            {
                System.out.println("---- P I C K E D [1]----\nInput Number of Entries:");
                number=scn.nextInt();
                HashMap<String,String> kv=new HashMap<>(number);
                //BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                //String str;
                String[] d=new String[2];
                for(int i=0; i<number; i++)
                {
                    System.out.println("ID"+i+":");
                    d=br.readLine().toLowerCase().trim().split(" ");
                    kv.put(d[0],d[1]);
                }
                fio.addToDB(kv);
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
            }
            else if(input == 2)
            {
                System.out.println("---- P I C K E D [2]----\nPRINT ALL\n");
                char[] lt=("abcdefghijklmnopqrstuvwxyz").toCharArray();
                for(char x:lt)
                    fio.readFromDB(x+"");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
            }
            else if(input == 3)
            {
                System.out.println("---- P I C K E D [3]----\nPrint All Entries Of Letter:");
                String l=br.readLine();
                fio.readFromDB(l);
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
            }
            else if(input == 4)
            {
                System.out.println("---- P I C K E D [4]----\nTo Search\nEnter Key to Search:");
                String sch=br.readLine();
                String result=fio.searchFromDB(sch);
                System.out.println("SEARCH RESULT\n------------\n"+result);
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
            }
            else if(input == 5)
                System.out.println("************ E X I T ****************");

        }
    }
}
