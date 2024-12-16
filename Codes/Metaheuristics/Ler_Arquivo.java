package pmpc;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Ler_Arquivo{
    
    public static int NUM_MEDIANAS=0;
    public static int TAM_INSTANCIA = 0;
    public static int CAPACIDADE_FIXA = 0;
    public static ArrayList<Float> coord_x = new ArrayList<>();
    public static ArrayList<Float> coord_y = new ArrayList<>();
    public static ArrayList<Integer> capacidade = new ArrayList<>();
    public static ArrayList<Integer> demanda = new ArrayList<>();
    
    public static void ler(String instancia) throws FileNotFoundException, IOException {

    InputStream is = new FileInputStream(instancia);    
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String linha = null;
        StringTokenizer st;

        linha = br.readLine();

        st = new StringTokenizer(linha, "\t");

        int tamanho_instancia = Integer.parseInt(st.nextToken());
        int numero_medianas = Integer.parseInt(st.nextToken());

        float coordx[] = new float[tamanho_instancia];
        float coordy[] = new float[tamanho_instancia];
        int x_capacidade[] = new int[tamanho_instancia];
        int x_demanda[] = new int[tamanho_instancia];

        for (int i = 0; i < tamanho_instancia; i++) {
            linha = br.readLine();
            st = new StringTokenizer(linha,"\t"); 
            
            coordx[i] = Float.parseFloat(st.nextToken()); 
            coordy[i] = Float.parseFloat(st.nextToken());

            x_capacidade[i] = Integer.parseInt(st.nextToken());
            x_demanda[i] = Integer.parseInt(st.nextToken());

        }

        br.close();
        
        TAM_INSTANCIA = tamanho_instancia;
        NUM_MEDIANAS = numero_medianas;
        
         
        for (int j=0; j< tamanho_instancia; j++){
            coord_x.add(j, Float.NaN);
            coord_y.add(j, Float.NaN);
            capacidade.add(0);
            demanda.add(0);
        }
        for(int j=0; j< tamanho_instancia; j++){
            coord_x.set(j, coordx[j]);
            coord_y.set(j, coordy[j]);
            capacidade.set(j,x_capacidade[j]);
            demanda.set(j,x_demanda[j]);
        }


    }
}
