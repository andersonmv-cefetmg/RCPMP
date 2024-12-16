package pmpc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
	public static String INSTANCIA;	  

//    public static String INSTANCIA = "src/pmpc/instancias/lin318/lin318_005.txt";

    public static int ITERMAXSM = 500; 
    public static int NIVEL_PERT_MAX = 0;
    public static int NUM_AVALIACOES = 500;
    public static long SEED = 11235813;
    public static float ALFA = (float) 4; 
    public static int GAMMA =0;
    public static float VARIACAO = (float) 0.0; 

    public static void main(String[] args) throws IOException{
	INSTANCIA = "src/pmpc/instancias/" + args[0] + ".txt";
	GAMMA = Integer.parseInt(args[1]);
	VARIACAO = Float.parseFloat(args[2]);
        SEED = Long.parseLong(args[3]);
	
	String filePath = "resultados/" + args[0] + "-" + GAMMA + "-" + VARIACAO  + ".csv";
	
	try {

        FileWriter writer = new FileWriter(filePath);
        writer.write("fo_s0\tfo\ttime\n");
        
        for (int j=0; j<10;j++){
            Principal p = new Principal();
            Solucao s = p.gvns(INSTANCIA, ITERMAXSM, NUM_AVALIACOES);
            System.out.println(args[0] + "-" + GAMMA + "-" + VARIACAO);
        }
      
        writer.close();

    } catch (IOException e) {
        System.out.println("Ocorreu um erro ao escrever no arquivo.");
        e.printStackTrace();
    }

}

    public static void readArgs(String args[]) {
       int index = -1;

       INSTANCIA = args[++index];   
       while (index < args.length - 1) {
           String option = args[++index];

           switch (option) {
               case "--iter":
                   ITERMAXSM = Integer.parseInt(args[++index]);
                   break;
               case "--numav":
                   NUM_AVALIACOES = Integer.parseInt(args[++index]);
                   break;
                case "--seed":
                   SEED = Integer.parseInt(args[++index]);                 
                   break;

               default:
                   printUsage();
                   System.exit(-1);
           }
       }
    }
    

    public static void  printUsage() {

    }


}
