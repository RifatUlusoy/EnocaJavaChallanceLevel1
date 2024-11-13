import java.util.ArrayList;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {

        //Java'da 100 adet random sayıya sahip bir liste oluşturun
        //Boş liste oluşturma
        ArrayList<Integer> randomList = new ArrayList<Integer>();
        //100 tane random integer oluşturma ve boş listeye ekleme
        for(int i = 0 ; i<100; i++){
            int randomNum = (int)(Math.random()*101);
            randomList.add(randomNum);
        }
        //Daha sonra bu listenin bir kopyasını oluşturma
        ArrayList randomList2 = (ArrayList) randomList.clone();
        System.out.println("Birinci Liste");
        System.out.println(randomList);
        System.out.println(randomList.size());
        System.out.println("İkinci Liste");
        System.out.println(randomList2);
        System.out.println(randomList2.size());

        //Daha sonra random bir sayı oluşturulur
        int random =  (int)(Math.random()*101);
        System.out.println(random);
        //Random oluşturulan sayı neyse 2.listede o index'e karşılık gelir ve indexdeki değeri siler
        randomList2.remove(random);

        System.out.println(randomList2);
        System.out.println(randomList2.size());
        //Hangi değerin eksik olduğunu bulan method
        for(int i=0;i<randomList.size()+1;i++){
            if(randomList2.get(i)==randomList.get(i)){
                continue;
            }else{
                System.out.println(randomList.get(i));
                break;

            }
        }
    }

}