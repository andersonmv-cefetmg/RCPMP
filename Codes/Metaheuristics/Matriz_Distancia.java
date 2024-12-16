package pmpc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

@SuppressWarnings("unchecked")
public class Matriz_Distancia {

    public static int NUM_MEDIANAS = 0;
    public static int TAM_INSTANCIA = 0;
    public static ArrayList<Float> coord_x = new ArrayList<>();
    public static ArrayList<Float> coord_y = new ArrayList<>();
    public static ArrayList<Integer> capacidade = new ArrayList<>();
    public static ArrayList<Integer> demanda = new ArrayList<>();
    public static ArrayList<ArrayList<Float>> matriz = new ArrayList<>();
    public static ArrayList<Float> soma_acumulada_dist = new ArrayList<>();
    public static ArrayList<Float> soma_acumulada_ordenada = new ArrayList<>();
    public static ArrayList<Integer> i_soma_acumulada_ordenada = new ArrayList<>();
    public static ArrayList<ArrayList<Float>> distancia(){


    TAM_INSTANCIA =  pmpc.Ler_Arquivo.TAM_INSTANCIA;
    NUM_MEDIANAS =  pmpc.Ler_Arquivo.NUM_MEDIANAS;
    demanda = pmpc.Ler_Arquivo.demanda;
    capacidade = pmpc.Ler_Arquivo.capacidade;
    soma_acumulada_dist.clear();
            
            for(int i=0; i<pmpc.Ler_Arquivo.TAM_INSTANCIA; i++){
                ArrayList<Float> linha_matriz = new ArrayList<>();
                for(int j=0; j<pmpc.Ler_Arquivo.TAM_INSTANCIA; j++){
                    float valor = (float)  Math.sqrt((Math.pow((Ler_Arquivo.coord_x.get(j)-Ler_Arquivo.coord_x.get(i)),2))+(Math.pow((Ler_Arquivo.coord_y.get(j)-Ler_Arquivo.coord_y.get(i)),2)));
                    linha_matriz.add(valor);
                    
                }
                
                matriz.add(linha_matriz);
                
                float aux_acumulada_dist=0;
                for (int p=0; p<Matriz_Distancia.TAM_INSTANCIA; p++){
                    aux_acumulada_dist = aux_acumulada_dist + linha_matriz.get(p);
                }
            soma_acumulada_dist.add(aux_acumulada_dist);
            }
            soma_acumulada_ordenada = (ArrayList<Float>) soma_acumulada_dist.clone();

           Collections.sort(soma_acumulada_ordenada);
           
            float aux = 0;
            
            for (int j=0; j< Matriz_Distancia.NUM_MEDIANAS; j++)
                    i_soma_acumulada_ordenada.add(0);
            
            for (int i=0; i< Matriz_Distancia.NUM_MEDIANAS;i++){
                
                aux = soma_acumulada_ordenada.get(i);
                for (int j=0; j< Matriz_Distancia.TAM_INSTANCIA;j++ ){
                    if(soma_acumulada_dist.get(j) == aux){
                        i_soma_acumulada_ordenada.set(i, j);
                    }
                }
            }
            return matriz;

    }
       
        public static void distancias() throws FileNotFoundException, IOException {
            
            ArrayList<Float> linha_matriz = new ArrayList<>();
            soma_acumulada_dist.clear();
            
            InputStream is = new FileInputStream("instancias_ccpx/ccpx1.dat");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String linha = null;
            StringTokenizer st;

            br.readLine();
            linha = br.readLine();

            st = new StringTokenizer(linha, " ");

            TAM_INSTANCIA = Integer.parseInt(st.nextToken());
            int tamanho_instancia_y = Integer.parseInt(st.nextToken());
            NUM_MEDIANAS = Integer.parseInt(st.nextToken());
            int numero_medianas_y = Integer.parseInt(st.nextToken());
            int valor_otimo = Integer.parseInt(st.nextToken());
            
            int cap = Integer.parseInt(st.nextToken());
            
            for(int x = 0; x < TAM_INSTANCIA;x++){
                capacidade.add(cap);
            }
            
            
            boolean teste = true;
            int valor_1 = -1;
            int valor_2 = -1;
            int fator = (int) Math.floor(TAM_INSTANCIA / 20);
            while(teste){
                linha = br.readLine();
                st = new StringTokenizer(linha, " ");
                valor_1 =  Integer.parseInt(st.nextToken());
                valor_2 =  Integer.parseInt(st.nextToken());
                if(valor_1 == 0){
                    if(valor_1 != valor_2){
                        teste = false;
                    }
                }
                else {
                	demanda.add(valor_1);
                	for(int f = 0;f < fator;f++) {
                		br.readLine();
                	}
                }
            }
            
            demanda.remove(0);
            
            st = new StringTokenizer(linha, " ");
            
            for(int i = 0;i < TAM_INSTANCIA;i++){

                float  value = 0;
                ArrayList<Float> Linha = new ArrayList<>(TAM_INSTANCIA);

                while(Linha.size() < TAM_INSTANCIA){
                    
                    
                    
                    while(st.hasMoreTokens()){
                        value = Float.parseFloat(st.nextToken());
                        Linha.add(value);
                    }
                    linha = br.readLine();
                    st = new StringTokenizer(linha, " ");         
                }
                matriz.add(Linha);
                
                linha_matriz.clear() ;
                linha_matriz.addAll(Linha);
                
                
                float aux_acumulada=0;
                for (int p=0; p<TAM_INSTANCIA; p++){
                    aux_acumulada = aux_acumulada + linha_matriz.get(p);
                }
                
                soma_acumulada_dist.add(aux_acumulada);
            }
            soma_acumulada_ordenada = (ArrayList<Float>) soma_acumulada_dist.clone();
            
            Collections.sort(soma_acumulada_ordenada);
            
            float aux = 0;
            
            for (int j=0; j< Ler_Arquivo.NUM_MEDIANAS; j++)
                i_soma_acumulada_ordenada.add(0);
            
            for (int i=0; i< Ler_Arquivo.NUM_MEDIANAS;i++){
                
                aux = soma_acumulada_ordenada.get(i);
                for (int j=0; j< Ler_Arquivo.TAM_INSTANCIA;j++ ){
                    if(soma_acumulada_dist.get(j) == aux){
                        i_soma_acumulada_ordenada.set(i, j);
                    }
                }
            }
        }

    static Object get(int inst_candidata) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}