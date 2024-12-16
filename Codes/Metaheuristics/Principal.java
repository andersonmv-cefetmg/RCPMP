package pmpc;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import pmpc.Solucao.*;
import static pmpc.Avalia_fo.*;
import static pmpc.VND.*;

import java.util.Scanner;

@SuppressWarnings("unchecked")
public class Principal {

    public S_Star s_star;
    public float fo_VND;
    public float fo_solucao1 = 0;
    public float fo_solucao = 0;
    public ArrayList<Integer> s_VND = new ArrayList<>();

    public Principal(S_Star s_star) {
        this.s_star = s_star;
    }

    Principal() {
    }

    public Solucao gvns(String instancia, int iterMaxSm, int num_avaliacoes_vnd) throws FileNotFoundException, IOException {

        Ler_Arquivo.ler(instancia);
        Matriz_Distancia.distancia();
        Main.NIVEL_PERT_MAX = (Matriz_Distancia.NUM_MEDIANAS);
        int k = 1;
        int iterSm = 0;
        int iter = 0;
        int nivel_pert = 2;
        float fo_solucao = 0;
        float xxfo = 0;
        float fo_slinha = 0;

        Solucao s_0 = new Solucao();

        s_0.Inicializa_Arrays();
//        s_0.f_guia(); 
//        s_0.aloca_medianas(); 
//        s_0.vetor_clientes(); 

        boolean inviavel = true;

        while (inviavel == true) {
        

//            if (s_0.gera_solucao_grasp()) 
            if (s_0.gera_solucao_rand()) {

                inviavel = false;
                } else {                                
                    s_0 = new Solucao();
                    s_0.Inicializa_Arrays();
                }
        }


        this.fo_solucao = s_0.calcula_fo();
        Movimentos.aux_fo_best = this.fo_solucao;
       
               
        VND s_vnd = new VND();

        Solucao s = s_vnd.vnd(s_0);
        

        Solucao s_best = null;
        s_best = s.clone();

        
        while (iterSm < iterMaxSm){

            Shake sk = new Shake(); 
            Solucao s_linha = sk.shake(s, nivel_pert);
            Solucao s_2_linhas = s_vnd.vnd(s_linha);

            if (s_2_linhas.calcula_fo() < s_best.calcula_fo()) {


                s_best = s_2_linhas.clone();
                s = s_best.clone();
                k = 1;
                iterSm = 0;
                iter = 0;
                
            } else {
                iter = iter + 1;
                iterSm = iterSm + 1;
                k = k + 1;
                if (nivel_pert < Main.NIVEL_PERT_MAX)
                    nivel_pert++;
            }

        }

        return s_best;
    }
}
