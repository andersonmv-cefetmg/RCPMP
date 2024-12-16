package pmpc;

import java.util.ArrayList;

public class Avalia_fo {
    public static float avalia(ArrayList<Integer> solucao){

        ArrayList<Float> linha_matriz = new ArrayList<>(Matriz_Distancia.TAM_INSTANCIA);

        float custo = 0;
        float fo = 0;

        int indice =0;
                            
        if(solucao == null) 
            System.out.println("Solucao nula  "+solucao);

        if (solucao.size() == 0)
            System.out.println("Solucao vazia "+solucao);
 
        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++){
            
            int mediana = solucao.get(j);
            custo = Matriz_Distancia.matriz.get(mediana).get(j); 
            fo = fo + custo;
            
        }
        
        return  fo;
    }
}