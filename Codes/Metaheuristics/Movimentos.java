package pmpc;
import java.util.Set;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import pmpc.Solucao.*;
import pmpc.Matriz_Distancia.*;
import pmpc.Principal.*;
import pmpc.Ler_Arquivo.*;
import pmpc.Movimentos.*;

@SuppressWarnings("unchecked")

public class Movimentos {

    public static String metodo;
    static float aux_fo_best=0;
    private static int opcao = 0;
    private static int opcao_shake = 0;
    public static Solucao s_aux_aux = null;
    private ArrayList<Integer> solucao_shake = new ArrayList<>();
    private ArrayList<Integer> medianas_shake = new ArrayList<>();
    private ArrayList<Integer> instalacoes_shake = new ArrayList<>(); 
    private ArrayList<Integer> vt_clientes_shake = new ArrayList<>();
    private ArrayList<Integer> solucoes = new ArrayList<>();
    private  ArrayList<Float> s_qj_xij_shake = new ArrayList<>();
    private  ArrayList<Float> s_rj_xij_shake = new ArrayList<>();
    private  ArrayList<ArrayList<Float>> S_shake = new ArrayList<>();
    private  ArrayList<ArrayList<Integer>> S2_shake = new ArrayList<>();
    private  ArrayList<Integer> vetor_clientes_shake = new ArrayList<>();
    private  ArrayList<Integer> demandas_clientes_shake = new ArrayList<>();
    private  ArrayList<Float> demandas_shake = new ArrayList<>();
    private  ArrayList<Float> q_shake = new ArrayList<>();
    private  ArrayList<Float> q_ch_shake = new ArrayList<>();
    private  ArrayList<Float> q_dn_shake = new ArrayList<>();
    private  ArrayList<Float> q_up_shake = new ArrayList<>();
    private  ArrayList<Float> r_shake = new ArrayList<>();
    private  ArrayList<Float> r_max_shake = new ArrayList<>();
    private  ArrayList<Integer> clientes_r_max_shake = new ArrayList<>();
    private  ArrayList<Float> aux_soma_acumulada_ordenada_shake = new ArrayList<>();
    private  int mediana_que_sai_shake =0;
    private  int mediana_shake =0;
    private  int cliente_shake =0;
    private  int mediana_que_entra_shake=0;
    private  float func_guia_shake=0;
    private  float s_a_o_shake=0; 
    private  float raio_cliente_shake=0;
    private  float r_t_i_shake = 0; 
    private  float  d_a_i_shake = 0; 
    private  float fo_solucao_shake =0;
    private  float fo_best_shake =0;
    private boolean solucao_viavel_shake = false;
    private int num_trocas = 0;

    
 public Solucao vnd_realoc(Solucao s_realoc) {
        if (s_realoc == null){
            s_realoc = s_aux_aux.clone();
 
        }    
        float fo_realoc = s_realoc.calcula_fo(); 
        s_realoc.setFo_solucao(fo_realoc);
        Solucao aux_s_realoc1 = s_realoc.clone();
        Solucao aux_s_realoc2 = aux_s_realoc1.clone();

        aux_s_realoc2.setSolucao_viavel(false);
        Random rnd = new Random();

        int inst_candidata = 0;
        int cliente_candidato = 0;
        int inst_q_sai = 0;

        Solucao s_aux = s_realoc.clone();
        int index_inst_q_sai = 0;
        
        ArrayList<Integer> clientes_disponiveis = new ArrayList<>();
            for (int j=0; j< Ler_Arquivo.TAM_INSTANCIA;j++){
                clientes_disponiveis.add(j);
            }
        
        for(int j=0; j< Ler_Arquivo.TAM_INSTANCIA; j++){ 
            
            Collections.shuffle (clientes_disponiveis);
            cliente_candidato = clientes_disponiveis.get(rnd.nextInt(clientes_disponiveis.size()));

            clientes_disponiveis.remove(clientes_disponiveis.indexOf(cliente_candidato));
            inst_q_sai = aux_s_realoc1.getSolucao().get(cliente_candidato);

            index_inst_q_sai = aux_s_realoc1.getMedianas().indexOf(aux_s_realoc1.getSolucao().get(cliente_candidato)); // cliente atual
            
            ArrayList<Integer> inst_disponiveis = (ArrayList<Integer>) aux_s_realoc1.getMedianas().clone();
            inst_disponiveis.remove(aux_s_realoc1.getMedianas().indexOf(inst_q_sai));

            while(!inst_disponiveis.isEmpty()){
                
                inst_candidata = inst_disponiveis.get(0);
               
                inst_disponiveis.remove(0);

                if (verifica_fo_realoc(aux_s_realoc1, cliente_candidato, inst_candidata)) {

                    if (verifica_demandas_realoc_troca(aux_s_realoc1, cliente_candidato, inst_candidata, inst_q_sai)) {

                        atualiza_s_linha(aux_s_realoc1, cliente_candidato, inst_candidata);
                        remove_soma_raio_dem_sai_realoc(aux_s_realoc1, cliente_candidato, inst_candidata, inst_q_sai);
                        readiciona_soma_raio_dem_sai_realoc(aux_s_realoc1, cliente_candidato, inst_candidata, inst_q_sai);

                        float fo_teste = aux_s_realoc1.calcula_fo();

                            if(fo_teste < aux_s_realoc1.getFo_best()){

                                float fo_best = fo_teste;
                                aux_s_realoc1.setFo_best(fo_best);
                                aux_s_realoc1.setFo_solucao(fo_best);
                                aux_s_realoc1.setSolucao_viavel(true);

                                aux_s_realoc2 = aux_s_realoc1.clone();
                                s_realoc.setFo_best(fo_best);
                                s_aux_aux = aux_s_realoc1.clone();
                                s_aux_aux.setFo_best(fo_best);

                                inst_q_sai = aux_s_realoc1.getSolucao().get(cliente_candidato);

                            }
                       }
                }

            }
        }
        
        
        if(aux_s_realoc2.getSolucao_viavel() == true){
            s_realoc = aux_s_realoc2.clone();
            s_aux = s_realoc.clone();
            s_aux_aux = s_aux.clone();

            aux_s_realoc2.setSolucao_viavel(false);
        }else{
            s_aux_aux = s_realoc.clone();
            s_aux = null;
        }
    
return s_aux;
    }

  public Solucao vnd_troca(Solucao s_troca) {
       
        if (s_troca == null){
            s_troca = s_aux_aux.clone();
        }
        
        float fo_realoc = s_troca.calcula_fo(); 
        s_troca.setFo_solucao(fo_realoc);
        Solucao aux_s_troca1 = s_troca.clone();
        Solucao aux_s_troca2 = aux_s_troca1.clone();

        aux_s_troca2.setSolucao_viavel(false);
        Random rnd = new Random();

        int inst_candidata1 = 0;
        int inst_candidata2 = 0;
        
        int cliente_candidato1 = 0;
        int cliente_candidato2 = 0;
        
        int inst_q_sai1 = 0;
        int inst_q_sai2 = 0;

        Solucao s_aux = s_troca.clone();
        int index_inst_q_sai1 = 0;
        int index_inst_q_sai2 = 0;
        
        ArrayList<Integer> clientes_disponiveis = new ArrayList<>();
        
        for (int j=0; j< Ler_Arquivo.TAM_INSTANCIA;j++){
            if(!aux_s_troca1.getMedianas().contains(j)){
                clientes_disponiveis.add(j);
            }
        }

        for(int j=0; j< clientes_disponiveis.size(); j++){ 
            
            cliente_candidato1 = clientes_disponiveis.get(j);
            inst_q_sai1 = aux_s_troca1.getSolucao().get(cliente_candidato1);

            for(int i=1; i< clientes_disponiveis.size(); i++){
                cliente_candidato2 = clientes_disponiveis.get(i);
                inst_candidata1 = aux_s_troca1.getSolucao().get(cliente_candidato2);

                inst_candidata2 = inst_q_sai1;
                inst_q_sai2 = inst_candidata1;

                if (verifica_fo_realoc(aux_s_troca1, cliente_candidato1, inst_candidata1)) {
                     if (verifica_fo_realoc(aux_s_troca1, cliente_candidato2, inst_candidata2)) {
                        if (verifica_demandas_realoc_troca(aux_s_troca1, cliente_candidato1, inst_candidata1, inst_q_sai1)) {
                            if (verifica_demandas_realoc_troca(aux_s_troca1, cliente_candidato2, inst_candidata2, inst_q_sai2)) {

                                atualiza_s_linha(aux_s_troca1, cliente_candidato1, inst_candidata1);
                                remove_soma_raio_dem_sai_realoc(aux_s_troca1, cliente_candidato1, inst_candidata1, inst_q_sai1);
                                readiciona_soma_raio_dem_sai_realoc(aux_s_troca1, cliente_candidato1, inst_candidata1, inst_q_sai1);


                                atualiza_s_linha(aux_s_troca1, cliente_candidato2, inst_candidata2);
                                remove_soma_raio_dem_sai_realoc(aux_s_troca1, cliente_candidato2, inst_candidata2, inst_q_sai2);
                                readiciona_soma_raio_dem_sai_realoc(aux_s_troca1, cliente_candidato2, inst_candidata2, inst_q_sai2);

                                float fo_teste = aux_s_troca1.calcula_fo();

                                if(fo_teste < aux_s_troca1.getFo_best()){

                                    float fo_best = fo_teste;
                                    aux_s_troca1.setFo_best(fo_best);
                                    aux_s_troca1.setFo_solucao(fo_best);
                                    aux_s_troca1.setSolucao_viavel(true);

                                    aux_s_troca2 = aux_s_troca1.clone();
                                    s_troca.setFo_best(fo_best);
                                    s_aux_aux = aux_s_troca1.clone();
                                    s_aux_aux.setFo_best(fo_best);

                                    inst_q_sai1 = aux_s_troca1.getSolucao().get(cliente_candidato1);
                                    inst_q_sai2 = aux_s_troca1.getSolucao().get(cliente_candidato2);
                                }
                            }
                        }
                    }           
                }
            }
        }

        if(aux_s_troca2.getSolucao_viavel() == true){
            s_troca = aux_s_troca2.clone();
            s_aux = s_troca.clone();
            s_aux_aux = s_aux.clone();
            aux_s_troca2.setSolucao_viavel(false);
            confirma_demandas(s_aux);

        }else{
            s_aux = null;
        }
        return s_aux;
    }

    public Solucao vnd_subst_realoc(Solucao s_subst) {
        Random rnd = new Random();
        if (s_subst == null){
            s_subst = s_aux_aux.clone();
        }
        float fo_subst = s_subst.calcula_fo(); 
        s_subst.setFo_solucao(fo_subst);

        Solucao aux_s_subst1 = s_aux_aux.clone();

        Solucao aux_s_subst2 = aux_s_subst1.clone();
        Solucao s_aux = null;

        boolean melhorou = false;
        aux_s_subst2.setSolucao_viavel(false);
        int inst_candidata = 0;
        int inst_q_atende_inst_candidata = 0;
        int index_inst_q_atende_inst_candidata = 0;
        int inst_q_sai = 0;
        int indice_inst_q_sai = 0;
        int substituicoes = (Ler_Arquivo.TAM_INSTANCIA - Ler_Arquivo.NUM_MEDIANAS);;
         
        ArrayList<Integer> aux_medianas = (ArrayList<Integer>) aux_s_subst1.getMedianas().clone();
        
        ArrayList<Integer> vt_clientes = clientes_shake(aux_s_subst1);
        int p =0;
        for(int i =0; i <= substituicoes; i++){   
                inst_q_sai = aux_s_subst1.getSolucao().get(i); 
                indice_inst_q_sai = aux_s_subst1.getMedianas().indexOf(inst_q_sai);

                if (vt_clientes.isEmpty())
                    vt_clientes = clientes_shake(aux_s_subst1);

                for(int j=0;j < vt_clientes.size(); j++){

                    inst_candidata = vt_clientes.get(j);
                    inst_q_atende_inst_candidata = aux_s_subst1.getSolucao().get(inst_candidata);
                    
                    index_inst_q_atende_inst_candidata = aux_s_subst1.getMedianas().indexOf(inst_q_atende_inst_candidata);

                    if(inst_q_atende_inst_candidata == inst_q_sai){
                        if (verifica_fo_subst(aux_s_subst1, inst_candidata, inst_q_sai)){
                            if (verifica_demandas_subst(aux_s_subst1, inst_candidata, inst_q_sai)) {
                                
                                aux_s_subst1 = substitui(aux_s_subst1, inst_candidata, inst_q_sai);
                                float fo_teste = aux_s_subst1.calcula_fo();
                                    
                                    
                                if(fo_teste < aux_s_subst1.getFo_best()){

                                    Solucao aux_s_subst3 = vnd_realoc(aux_s_subst1);
                                    if(aux_s_subst3 != null){
                                        fo_teste = aux_s_subst3.calcula_fo();
                                        aux_s_subst1 = aux_s_subst3.clone();
                                        
                                    }
                                    float fo_best = fo_teste;
                                    aux_s_subst1.setFo_best(fo_best);
                                    aux_s_subst1.setSolucao_viavel(true);

                                    aux_s_subst2 = aux_s_subst1.clone();
                                    aux_s_subst2.setFo_best(fo_best);
                                    s_aux_aux = aux_s_subst1.clone();
                                    s_aux_aux.setFo_best(fo_best);
                                    s_subst.setFo_best(fo_best);
                                }
                            }
                        }
                    }else{
                        if (verifica_fo_subst(aux_s_subst1, inst_candidata, inst_q_sai)){

                            aux_s_subst1 = verifica_demandas_subst_diferente(aux_s_subst1, inst_candidata, inst_q_sai, indice_inst_q_sai, inst_q_atende_inst_candidata, index_inst_q_atende_inst_candidata );
                            if(aux_s_subst1.getSolucao_viavel()==true){
                                    
                                if(aux_s_subst1.calcula_fo() < aux_s_subst1.getFo_best()){
                                    float fo_best = aux_s_subst1.calcula_fo();
                                    
                                    Solucao aux_s_subst3 = vnd_realoc(aux_s_subst1);
                                    if(aux_s_subst3 != null){
                                        fo_best = aux_s_subst3.calcula_fo();
                                        aux_s_subst1 = aux_s_subst3.clone();
                                        
                                    }
                                    aux_s_subst1.setFo_solucao(fo_best);
                                    aux_s_subst1.setSolucao_viavel(true);

                                    aux_s_subst2 = aux_s_subst1.clone();
                                }
                            }
                        }
                    }
                aux_s_subst1 = s_subst.clone();
                }
        }

        if(aux_s_subst2.getSolucao_viavel() == true){
            s_subst = aux_s_subst2.clone();
            s_aux = s_subst.clone();
            s_aux_aux = s_aux.clone();

            aux_s_subst2.setSolucao_viavel(false);
        }else{
            s_aux = null;
        }
        return s_aux;
    }

   
    private static ArrayList<Integer> clientes_realoc(Solucao s_realoc) {
        ArrayList<Integer> vt_clientes = new ArrayList<>();

        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) {

            if (!s_realoc.getSolucao().contains(j)) {
                vt_clientes.add(j);
            }
        }
        return vt_clientes;
    }
        
        
    private static ArrayList<Integer> clientes_shake(Solucao s_shake) {
        ArrayList<Integer> vt_clientes = new ArrayList<>();

        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) {

            if (!s_shake.getSolucao().contains(j)) {
                vt_clientes.add(j);
            }
        }
        
        return vt_clientes;
    }
    
        private static ArrayList<Integer> clientes_troca(Solucao s_troca) {
        ArrayList<Integer> vt_clientes = new ArrayList<>();

        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) {

            if (!s_troca.getSolucao().contains(j)) {
                vt_clientes.add(j);
                
            }
        }
        return vt_clientes;
    }

    private static boolean verifica_demandas_subst(Solucao s_shake, int inst_candidata, int inst_q_sai) {

        Random rnd = new Random();
        s_shake.setSolucao_viavel(false);
        boolean alocacao_viavel = false;
        float d_a_i = 0;
        float r_t_i = 0;
        int index = 0;
        float fo_aux_shake= 0;
        float maior_raio=0;
        
        Solucao aux_s_shake = s_shake.clone();
        ArrayList<Float> raios = new ArrayList<>();
        ArrayList<Integer> clientes = new ArrayList<>();
        ArrayList<Float> aux_S = new ArrayList<>();
        ArrayList<Integer> aux_S2 = new ArrayList<>();
        int indice_inst_q_sai= aux_s_shake.getMedianas().indexOf(inst_q_sai);
        
        for(int j=0; j<aux_s_shake.getSolucao().size(); j++){
            if(aux_s_shake.getSolucao().get(j) == inst_q_sai){
                aux_s_shake.getSolucao().set(j, inst_candidata);
                raios.add(s_shake.getR().get(j));
                clientes.add(j);
                d_a_i = d_a_i + s_shake.getQ_ch().get(j);
            }
        }
        aux_s_shake.setSolucao(inst_candidata, inst_candidata);
       
        if(d_a_i <= Ler_Arquivo.capacidade.get(inst_candidata)){
            
            int  indice_maior_raio= 0;
            float soma_raios = 0;
            if(Main.GAMMA!=0){
                for (int j = 0; j < Main.GAMMA; j++) {
                    if(!raios.isEmpty()){
                        maior_raio = Collections.max(raios);
                        indice_maior_raio= raios.indexOf(maior_raio);
                        aux_S.add(maior_raio);
                        aux_S2.add(clientes.get(indice_maior_raio));
                        clientes.remove(clientes.get(indice_maior_raio));
                        raios.remove(maior_raio);

                        r_t_i = r_t_i + aux_S.get(j);
                    }
                }
            }
            
            if(d_a_i + r_t_i <= Ler_Arquivo.capacidade.get(inst_candidata)){
                if (!aux_S.isEmpty()){
                    aux_s_shake.setS(indice_inst_q_sai, aux_S);
                    aux_s_shake.setS2(indice_inst_q_sai, aux_S2);
                }
           
                aux_s_shake.setS_qj_xij(indice_inst_q_sai, d_a_i);
                aux_s_shake.setS_rj_xij(indice_inst_q_sai, r_t_i); 
                aux_s_shake.setMedianas(indice_inst_q_sai, inst_candidata);
                fo_aux_shake = aux_s_shake.calcula_fo(); 
                aux_s_shake.setFo_solucao(fo_aux_shake);
                s_shake = aux_s_shake.clone();
                s_shake.setSolucao_viavel(true);
                alocacao_viavel = true;
            
            
            }

        }
        if (alocacao_viavel == true) {
            return true;
        } else {
            return false;
        }
    }
                                                                    

    private static Solucao verifica_demandas_subst_diferente(Solucao aux_s_shake1, int inst_candidata, int inst_q_sai, int indice_inst_q_sai,int inst_q_atende_inst_candidata, int index_inst_q_atende_inst_candidata) {

        Random rnd = new Random();
        
        float d_a_i = 0;
        boolean alocacao_viavel = false;
        float r_t_i = 0;
        float maior_raio =0;
        float fo_aux_shake= 0;
        Solucao aux_s_shake = aux_s_shake1.clone();
        ArrayList<Float> raios = new ArrayList<>();
        ArrayList<Integer> clientes = new ArrayList<>();
        ArrayList<Float> gamma_maiores_raios = new ArrayList<>();
        ArrayList<Float> aux_S = new ArrayList<>();
        ArrayList<Integer> aux_S2 = new ArrayList<>();
        aux_s_shake.setSolucao(inst_candidata, inst_candidata);
        raios.add(aux_s_shake.getR().get(inst_candidata));
        clientes.add(inst_candidata);

        while (aux_s_shake.getSolucao().contains(inst_q_sai)) {
            for (int j = 0; j < aux_s_shake.getSolucao().size(); j++) {
                if (aux_s_shake.getSolucao().get(j) == inst_q_sai) {
                    aux_s_shake.setSolucao(j, inst_candidata);
                    raios.add(aux_s_shake1.getR().get(j));
                    clientes.add(j);
                    d_a_i = d_a_i + aux_s_shake1.getQ_ch().get(j);
                }
            }
        }
        
        d_a_i = d_a_i + aux_s_shake.getQ_ch().get(inst_candidata);
        
        if(d_a_i <= Ler_Arquivo.capacidade.get(inst_candidata)){
            
            int  indice_maior_raio= 0;
            float soma_raios = 0;
            if(Main.GAMMA!=0){
                for (int j = 0; j < Main.GAMMA; j++) {
                    if(!raios.isEmpty()){
                        maior_raio = Collections.max(raios);
                        indice_maior_raio= raios.indexOf(maior_raio);
                        aux_S.add(maior_raio);
                        aux_S2.add(clientes.get(indice_maior_raio));
                        clientes.remove(clientes.get(indice_maior_raio));
                        raios.remove(maior_raio);

                        r_t_i = r_t_i + aux_S.get(j);
                    }
                }

            }
            
            if(d_a_i + r_t_i <= Ler_Arquivo.capacidade.get(inst_candidata)){
                if (!aux_S.isEmpty()){
                    aux_s_shake.setS(indice_inst_q_sai, aux_S);
                    aux_s_shake.setS2(indice_inst_q_sai, aux_S2);
                }
           
                aux_s_shake.setS_qj_xij(indice_inst_q_sai, d_a_i);
                aux_s_shake.setS_rj_xij(indice_inst_q_sai, r_t_i); 
                aux_s_shake.setMedianas(indice_inst_q_sai, inst_candidata);

   
        
            aux_s_shake1 = demandas_q_saem(aux_s_shake, inst_q_atende_inst_candidata, index_inst_q_atende_inst_candidata) ;

            fo_aux_shake = aux_s_shake.calcula_fo(); 
            aux_s_shake1.setFo_solucao(fo_aux_shake);
            aux_s_shake1 = aux_s_shake.clone();
            aux_s_shake1.setSolucao_viavel(true);
            }
            

        }else{
             aux_s_shake1.setSolucao_viavel(false);
        }
        
        
        return aux_s_shake1;
    }
    
    private static Solucao demandas_q_saem(Solucao aux_s_shake, int inst_q_atende_inst_candidata, int index_inst_q_atende_inst_candidata){
        
        float d_a_i = 0;
        boolean alocacao_viavel = false;
        float r_t_i = 0;
        float maior_raio =0;
        float fo_aux_shake= 0;
        int indice_maior_raio=0;
        
        ArrayList<Float> raios = new ArrayList<>();
        ArrayList<Integer> clientes = new ArrayList<>();
        ArrayList<Float> gamma_maiores_raios = new ArrayList<>();
        ArrayList<Float> aux_S = new ArrayList<>();
        ArrayList<Integer> aux_S2 = new ArrayList<>();
        
        raios.clear();
        clientes.clear();
        aux_S.clear();
        aux_S2.clear();
        r_t_i=0;

        
        for(int j = 0; j< aux_s_shake.getSolucao().size(); j++){
            if(aux_s_shake.getSolucao().get(j) == inst_q_atende_inst_candidata){
                raios.add(aux_s_shake.getR().get(j));
                clientes.add(j);
                d_a_i = d_a_i + aux_s_shake.getQ_ch().get(j);
            }
        }
        if(Main.GAMMA!=0){
            for (int j = 0; j < Main.GAMMA; j++) {
                if(!raios.isEmpty()){
                    maior_raio = Collections.max(raios);
                    indice_maior_raio= raios.indexOf(maior_raio);
                    aux_S.add(maior_raio);
                    aux_S2.add(clientes.get(indice_maior_raio));
                    clientes.remove(clientes.get(indice_maior_raio));
                    raios.remove(maior_raio);
                    r_t_i = r_t_i + aux_S.get(j);
                }
            }
        }

        if (!aux_S.isEmpty()){
            aux_s_shake.setS(index_inst_q_atende_inst_candidata, aux_S);
            aux_s_shake.setS2(index_inst_q_atende_inst_candidata, aux_S2);
        }
        
        aux_s_shake.setS_qj_xij(index_inst_q_atende_inst_candidata, d_a_i);
        aux_s_shake.setS_rj_xij(index_inst_q_atende_inst_candidata, r_t_i);

        return aux_s_shake;
    }
 
public boolean verifica_demandas_realoc_troca(Solucao s_realoc, int cliente_candidato, int inst_candidata, int inst_q_sai) {

        Random rnd = new Random();
        boolean alocacao_viavel = false;
        ArrayList<Integer> S2_clientes = new ArrayList<>();
        ArrayList<Float> S_raios = new ArrayList<>();
//        int tam = 0;
        float minimo = 0;
       

        int tam = s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).size();

        if (tam < Main.GAMMA) {
            opcao = 1;            
            float d_a_i = (s_realoc.getQ_ch().get(cliente_candidato) + (s_realoc.getS_qj_xij().get(s_realoc.getMedianas().indexOf(inst_candidata))));  // d_a_i = demanda dos clientes acumulada na instalação..
            
            float soma_raios = 0;
            for(int i = 0; i < (s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).size()); i++){
                soma_raios = soma_raios + s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).get(i);   
            }
            
            float r_t_i = soma_raios + s_realoc.getR().get(cliente_candidato);  // r_t_i = Somatório dos gammas raios da instalação.
            if ((d_a_i + r_t_i) <= Ler_Arquivo.capacidade.get(inst_candidata)) { // 14025
                
                s_realoc.getS_rj_xij().set(s_realoc.getMedianas().indexOf(inst_candidata), r_t_i);
                s_realoc.getS_qj_xij().set(s_realoc.getMedianas().indexOf(inst_candidata), d_a_i);
                s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_candidata)).add(cliente_candidato);
                s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).add(s_realoc.getR().get(cliente_candidato));
                alocacao_viavel = true;
            } else {
                alocacao_viavel = false;
            }
        }
        if (tam >= Main.GAMMA) {
            opcao = 2;             
            if (Main.GAMMA!=0){
                minimo = Collections.min(s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)));
                int i_minimo = s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).indexOf(Collections.min(s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata))));
            }    

    
            if ((s_realoc.getR().get(cliente_candidato)) > minimo) {
                opcao = 21; 

                float d_a_i = (s_realoc.getQ_ch().get(cliente_candidato) + s_realoc.getS_qj_xij().get(s_realoc.getMedianas().indexOf(inst_candidata)));
                float soma_raios1 = 0;
                for(int i = 0; i < (s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).size()); i++){
                    soma_raios1 = soma_raios1 + s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).get(i);   
                }
                float r_t_i = (soma_raios1 + (s_realoc.getR().get(cliente_candidato))) - minimo;
                float total_d_r = r_t_i + d_a_i;
                if (total_d_r <= Ler_Arquivo.capacidade.get(inst_candidata)) {

                    opcao = 211;

                    s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_candidata)).remove(s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).indexOf(Collections.min(s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)))));
                    s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_candidata)).add(cliente_candidato);

                    s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).remove(s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).indexOf(Collections.min(s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)))));
                    s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_candidata)).add((s_realoc.getR().get(cliente_candidato)));  
                    
                    s_realoc.getS_rj_xij().set(s_realoc.getMedianas().indexOf(inst_candidata), r_t_i);

                    s_realoc.getS_qj_xij().set(s_realoc.getMedianas().indexOf(inst_candidata), d_a_i);

                    alocacao_viavel = true;
                } else {
                    alocacao_viavel = false;
                }
            } else {
                opcao = 22;
                float demanda_atendida = s_realoc.getS_qj_xij().get(s_realoc.getMedianas().indexOf(inst_candidata));
                float d_cliente_candidato = s_realoc.getQ_ch().get(cliente_candidato);
                float demanda_total = demanda_atendida + d_cliente_candidato;
                if (demanda_total <= Ler_Arquivo.capacidade.get(inst_candidata)) {
                    s_realoc.getS_qj_xij().set(s_realoc.getMedianas().indexOf(inst_candidata), demanda_total);
                    alocacao_viavel = true;
                } else {
                    alocacao_viavel = false;
                }
            }
        }

        if (opcao == 0) {
            alocacao_viavel = false;
        }

        if (alocacao_viavel == true) {
            return true;
        } else {
            return false;
        }
    }
    public static void atualiza_s_linha(Solucao s_realoc, int cliente_candidato, int inst_candidata) {

        s_realoc.setSolucao(cliente_candidato, inst_candidata);

    }

    public Solucao substitui(Solucao s_shake, int candidato_nova_mediana, int inst_q_sai) {

        s_shake.setSolucao(candidato_nova_mediana, candidato_nova_mediana);
        while (s_shake.getSolucao().contains(inst_q_sai)) {

            for (int j = 0; j < s_shake.getSolucao().size(); j++) {

                if (s_shake.getSolucao().get(j) == inst_q_sai) {
                    s_shake.setSolucao(j, candidato_nova_mediana);
                }
            }
        }

        s_shake.setMedianas(s_shake.getMedianas().indexOf(inst_q_sai), candidato_nova_mediana);
        return s_shake;
    }

    public boolean verifica_fo_realoc(Solucao s_realoc, int cliente_candidato, int inst_candidata) {

        ArrayList<Integer> s_linha_aux = (ArrayList<Integer>) s_realoc.getSolucao().clone();
        s_linha_aux.set(cliente_candidato, inst_candidata);

        float nova_fo = avalia(s_linha_aux);
        float fo_realoc = s_realoc.calcula_fo();
        if (nova_fo < s_realoc.getFo_best()) {
            return true;
        } else {
            return false;
        }
    }
    
        private boolean verifica_fo_troca(Solucao aux_s_troca1, int cliente_candidato1, int cliente_candidato2, int inst_candidata1, int inst_candidata2) {
           
            ArrayList<Integer> aux_1 = new ArrayList<>();
            Collections.swap(aux_s_troca1.getSolucao(), cliente_candidato1, cliente_candidato2);
            float nova_fo = aux_s_troca1.calcula_fo(); 
            if (nova_fo < aux_s_troca1.getFo_best()) {
                return true;
            } else {
                return false;
            }
        }
        

    public boolean verifica_fo_subst(Solucao s_subst, int inst_candidata, int inst_q_sai) {
        float custo_atual = 0;
        float novo_custo = 0;
        float novo_custo_atual = 0;
        float subtrai_custo = 0;
        ArrayList<Integer> aux_1 = (ArrayList<Integer>) s_subst.getSolucao().clone();
        aux_1.set(inst_candidata, inst_candidata);
        
        while (aux_1.contains(inst_q_sai)) {

            for (int j = 0; j < aux_1.size(); j++) {

                if (aux_1.get(j) == inst_q_sai) {
                    aux_1.set(j, inst_candidata);
                }
            }
        }
        float fo_aux1 = avalia(aux_1);
        if (fo_aux1 < s_subst.getFo_best()) {
            return true;
        } else {
            return false;
        }
    }

    public float avalia(ArrayList<Integer> solucao) {

        ArrayList<Float> linha_matriz = new ArrayList<>(Matriz_Distancia.TAM_INSTANCIA);

        float custo = 0;
        float fo = 0;

        int indice = 0;

        if (solucao == null) {
            System.out.println("Solucao nula  " + solucao);
        }

        if (solucao.size() == 0) {
            System.out.println("Solucao vazia " + solucao);
        }

        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) {

            int mediana = solucao.get(j);
            custo = Matriz_Distancia.matriz.get(mediana).get(j); 
            fo = fo + custo;

        }

        return fo;
    }

    public Solucao remove_soma_raio_dem_sai_realoc(Solucao s_realoc, int cliente_candidato, int inst_candidata, int inst_q_sai) {
       
        if (s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_q_sai)).contains(cliente_candidato)) {
            
            s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_q_sai)).remove(s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_q_sai)).indexOf(cliente_candidato));
            
            float soma_raios = 0;
            for(int i = 0; i < (s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_q_sai)).size()); i++){
                soma_raios = soma_raios + s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_q_sai)).get(i);   
            }
          
            s_realoc.setS_rj_xij(s_realoc.getMedianas().indexOf(inst_q_sai), soma_raios);

            s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_q_sai)).remove(s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_q_sai)).indexOf(cliente_candidato));
        }
        
        float d_a_i =  s_realoc.getS_qj_xij().get(s_realoc.getMedianas().indexOf(inst_q_sai)) - s_realoc.getQ_ch().get(cliente_candidato);
            
        s_realoc.setS_qj_xij(s_realoc.getMedianas().indexOf(inst_q_sai), d_a_i);

        return s_realoc;
    }
    
    
    public Solucao readiciona_soma_raio_dem_sai_realoc(Solucao s_realoc, int cliente_candidato, int inst_candidata, int inst_q_sai) {

        ArrayList<Integer> clientes_remanescentes = new ArrayList<>();
        ArrayList<Float> raios_remanescentes = new ArrayList<>();
        ArrayList<Integer> clientes_maiores_raios_inst = new ArrayList<>();
        ArrayList<Float> maiores_raios_inst = new ArrayList<>(); 
        
        if(s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_q_sai)).size() < Main.GAMMA){

            for (int j = 0; j < Ler_Arquivo.TAM_INSTANCIA; j++) {
                if (s_realoc.getSolucao().get(j) == inst_q_sai) {
                    if(!s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_q_sai)).contains(j)){
                        clientes_remanescentes.add(j); 
                        raios_remanescentes.add(s_realoc.getR().get(j));
                    }
                }
            }

            if (clientes_remanescentes.size() != 0) {
                
                float maior_raio_cliente_remanescente = Collections.max(raios_remanescentes);
                int   cliente_remanescente_maior_raio = clientes_remanescentes.get(raios_remanescentes.indexOf(Collections.max(raios_remanescentes)));
                      
                float soma_raios = 0;
                for(int i = 0; i < (s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_q_sai)).size()); i++){
                    soma_raios = soma_raios + s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_q_sai)).get(i);   
                }    
                 
                float total_dem_mais_raio = s_realoc.getS_qj_xij().get(s_realoc.getMedianas().indexOf(inst_q_sai)) + maior_raio_cliente_remanescente + soma_raios;

                if (total_dem_mais_raio <= Ler_Arquivo.capacidade.get(inst_q_sai)){
                    float r_t_i =  soma_raios + maior_raio_cliente_remanescente;
                    s_realoc.setS_rj_xij(s_realoc.getMedianas().indexOf(inst_q_sai), r_t_i);
                    
                    s_realoc.getS2().get(s_realoc.getMedianas().indexOf(inst_q_sai)).add(cliente_remanescente_maior_raio);
                    s_realoc.getS().get(s_realoc.getMedianas().indexOf(inst_q_sai)).add(maior_raio_cliente_remanescente);


                }
            }
            

        }

        return s_realoc;
    }
    
 
    public Solucao subst_retira_dem_raio_inst_q_sai(Solucao s_subst, int candidato_nova_mediana, int inst_q_sai) {
        
        ArrayList<Integer> clientes_inst_q_sai = new ArrayList<>();
        clientes_inst_q_sai = s_subst.getS2().get(s_subst.getMedianas().indexOf(candidato_nova_mediana));
        
        if (clientes_inst_q_sai.contains(inst_q_sai)) {
            clientes_inst_q_sai.remove(clientes_inst_q_sai.indexOf(inst_q_sai));
        }
        float d_a_i = (s_subst.getS_qj_xij().get(s_subst.getMedianas().indexOf(inst_q_sai)) - s_subst.getQ_ch().get(candidato_nova_mediana));
        s_subst.getS_qj_xij().set(s_subst.getMedianas().indexOf(inst_q_sai), d_a_i);

        return s_subst;
    }
    
    public void confirma_demandas(Solucao s_aux){  
        int mediana = 0;
        float demanda = 0;
        ArrayList<Integer> demandas_s_aux = new ArrayList<>();
        for(int i = 0; i< Ler_Arquivo.NUM_MEDIANAS; i++){
           mediana= s_aux.getMedianas().get(i);
            demanda = 0;
            for(int j=0; j<Ler_Arquivo.TAM_INSTANCIA;j++){
                if(s_aux.getSolucao().get(j) == mediana){
                    demanda = (demanda + s_aux.getQ_ch().get(j));
                }
            }
        demandas_s_aux.add(i, (int) demanda);
        }
        demandas_s_aux.clear();
    }


    public Solucao shake(Solucao s_shake, int nivel_pert) {
        Solucao aux_s_subst1 = s_shake.clone();

        Random rnd = new Random();
        float d_mim = 0;
        float d_max=0;
        int ok=0;
        boolean  resposta = true;
        medianas_shake = aux_s_subst1.getMedianas();
        solucao_shake = aux_s_subst1.getSolucao();
        S_shake = aux_s_subst1.getS();
        S2_shake = aux_s_subst1.getS2();
        r_shake = aux_s_subst1.getR();
        s_qj_xij_shake = aux_s_subst1.getS_qj_xij();
        s_rj_xij_shake = aux_s_subst1.getS_rj_xij();
        q_ch_shake= aux_s_subst1.getQ_ch();
        aux_s_subst1.setSolucao_viavel(false);

        int substituicoes = 1;
        while (substituicoes < nivel_pert) {  
            int num_trocas = 0;
            while (aux_s_subst1.getSolucao_viavel()== false) {

                resposta = true;
                
                prepara_shake(aux_s_subst1, nivel_pert);
                
                while(resposta == true){       

                    mediana_shake = medianas_shake.get(rnd.nextInt(Matriz_Distancia.NUM_MEDIANAS)); 
                    cliente_shake = vt_clientes_shake.get(rnd.nextInt(vt_clientes_shake.size())); 
                    vt_clientes_shake.remove(vt_clientes_shake.indexOf(cliente_shake)); 
                        if(verifica_demandas_shake()){
                            solucao_shake.set(cliente_shake,mediana_shake);
                        }else{
                            for(int j=0;j< Matriz_Distancia.NUM_MEDIANAS;j++){
                                mediana_shake = medianas_shake.get(j); 
                                if(verifica_demandas_shake()){
                                    solucao_shake.set(cliente_shake,mediana_shake);
                                    j = Matriz_Distancia.NUM_MEDIANAS;
                                }else{
                                    if(j == Matriz_Distancia.NUM_MEDIANAS - 1){
                                        resposta=false;
                                    }
                                }
                            }
                        }
                        if(solucao_shake.contains(-2)){
                            resposta = true;
                        }else{
                            resposta = false;
                        }

                        if (vt_clientes_shake.size() == 0){
                            resposta = false;

                        }
                }
                if(!aux_s_subst1.getSolucao().contains(-2)){
                    aux_s_subst1.setSolucao_viavel(true);
                    aux_s_subst1.getSolucao();
                }   
            }
                
        substituicoes++;        
        }            
                
        return aux_s_subst1;
    }    

    public Solucao prepara_shake(Solucao aux_s_subst1, int nivel_pert) {
        Random rnd = new Random();  
        num_trocas=0;

        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) {

            if (!aux_s_subst1.getSolucao().contains(j)) {
                vt_clientes_shake.add(j);
                
            }
        }
        
        while (num_trocas < nivel_pert) {
            mediana_que_sai_shake = medianas_shake.get(rnd.nextInt(Matriz_Distancia.NUM_MEDIANAS));
            mediana_que_entra_shake = vt_clientes_shake.get(rnd.nextInt(vt_clientes_shake.size()));
            medianas_shake.set(medianas_shake.indexOf(mediana_que_sai_shake), mediana_que_entra_shake);
            vt_clientes_shake.set(vt_clientes_shake.indexOf(mediana_que_entra_shake), mediana_que_sai_shake);
            num_trocas++;
        }

            
        for(int j=0;j< Matriz_Distancia.TAM_INSTANCIA;j++){
            solucao_shake.set(j, -2);
        }

        aloca_medianas_shake();
        
        return aux_s_subst1;
    }

    public boolean verifica_demandas_shake(){

        Random rnd = new Random();
        boolean alocacao_viavel = false;
        int ok=1;
        float zero =0;
        int tam = S_shake.get(medianas_shake.indexOf(mediana_shake)).size();
        int instalacao =0;
        float demanda_instalacao =0;
        
        for(int j=0;j< Matriz_Distancia.NUM_MEDIANAS;j++){
            s_qj_xij_shake.set(j, zero);
        }
        
        int median =0;

        for(int j=0;j< Matriz_Distancia.TAM_INSTANCIA;j++){
            if (solucao_shake.get(j)!= -2){
                float demanda_total_instalacao = s_qj_xij_shake.get(medianas_shake.indexOf(solucao_shake.get(j))) + q_ch_shake.get(solucao_shake.get(j));
                s_qj_xij_shake.set(medianas_shake.indexOf(solucao_shake.get(j)),demanda_total_instalacao); 
            }
            
        }
        
        raio_cliente_shake = 0;

        opcao_shake = 0;
        
        if (tam < Main.GAMMA){
            opcao = 1;
            d_a_i_shake =  (q_ch_shake.get(mediana_shake) + s_qj_xij_shake.get(medianas_shake.indexOf(mediana_shake)));  
            
            r_t_i_shake =  (r_shake.get(mediana_shake)  + s_rj_xij_shake.get(medianas_shake.indexOf(mediana_shake)));
            
            if ((d_a_i_shake + r_t_i_shake) <= Ler_Arquivo.capacidade.get(mediana_shake)){ 
               
                s_rj_xij_shake.set(medianas_shake.indexOf(mediana_shake), r_t_i_shake);
                s_qj_xij_shake.set(medianas_shake.indexOf(mediana_shake), d_a_i_shake);
                
                S2_shake.get(medianas_shake.indexOf(mediana_shake)).add(mediana_shake);
                S_shake.get(medianas_shake.indexOf(mediana_shake)).add(r_shake.get(mediana_shake));
                
                alocacao_viavel = true;
            }else{ 
                alocacao_viavel = false;  
            }
        }else{
            
            opcao_shake=2;
            double minimo =0;
            int i_minimo =0;
           
            if (Main.GAMMA!=0){
                minimo = Collections.min(S_shake.get(medianas_shake.indexOf(mediana_shake)));
                i_minimo = S_shake.get(medianas_shake.indexOf(mediana_shake)).indexOf(Collections.min(S_shake.get(medianas_shake.indexOf(mediana_shake))));
            }
            if (r_shake.get(mediana_shake) > minimo){
                opcao_shake=21;
                   
                        float verifica_s_raios =  (float) ((s_rj_xij_shake.get(medianas_shake.indexOf(mediana_shake)) - minimo) + r_shake.get(mediana_shake));
                    
                        float verifica_s_demandas = s_qj_xij_shake.get(medianas_shake.indexOf(mediana_shake)) + q_ch_shake.get(mediana_shake);
                        
                        if ((verifica_s_raios + verifica_s_demandas) <= Ler_Arquivo.capacidade.get(mediana_shake)){
                            opcao_shake = 211;

                            d_a_i_shake = verifica_s_demandas;
                            
                            
                            S2_shake.get(medianas_shake.indexOf(mediana_shake)).remove(S_shake.get(medianas_shake.indexOf(mediana_shake)).indexOf(Collections.min(S_shake.get(medianas_shake.indexOf(mediana_shake)))));
                            S2_shake.get(medianas_shake.indexOf(mediana_shake)).add(mediana_shake);


                            S_shake.get(medianas_shake.indexOf(mediana_shake)).remove(S_shake.get(medianas_shake.indexOf(mediana_shake)).indexOf(Collections.min(S_shake.get(medianas_shake.indexOf(mediana_shake)))));
                            S_shake.get(medianas_shake.indexOf(mediana_shake)).add(r_shake.get(mediana_shake));  

                            float soma =0;
                            for(int j=0; j < S_shake.get(medianas_shake.indexOf(mediana_shake)).size(); j++){
                                soma = soma + S_shake.get(medianas_shake.indexOf(mediana_shake)).get(j);
                            }
                            s_rj_xij_shake.set(medianas_shake.indexOf(mediana_shake), soma);
                            s_qj_xij_shake.set(medianas_shake.indexOf(mediana_shake), d_a_i_shake);
            
                                alocacao_viavel = true;
                        }else{ 
                                alocacao_viavel=false; 
                        }
            }else{
                    opcao_shake=212;
                    float verifica_s_demandas = s_qj_xij_shake.get(medianas_shake.indexOf(mediana_shake)) + q_ch_shake.get(mediana_shake); 
                    d_a_i_shake =  verifica_s_demandas;
                    if(verifica_s_demandas <= Ler_Arquivo.capacidade.get(mediana_shake)){
                        s_qj_xij_shake.set(medianas_shake.indexOf(mediana_shake),  d_a_i_shake);
                        alocacao_viavel = true;
                    }else{
                        alocacao_viavel = false;
                    }
            }
        }

        if (alocacao_viavel == true){
            return true;
        }else{
            return false;
        } 
    }

    public void aloca_medianas_shake(){
       float zero =0;
        for(int i=0;i< pmpc.Matriz_Distancia.NUM_MEDIANAS;i++){
            s_qj_xij_shake.set(i, zero);
       }
        for(int i=0;i< pmpc.Matriz_Distancia.NUM_MEDIANAS;i++){
            solucao_shake.set(medianas_shake.get(i),medianas_shake.get(i));
            s_qj_xij_shake.set(medianas_shake.indexOf(medianas_shake.get(i)), q_ch_shake.get(medianas_shake.get(i))); 
            if(Main.GAMMA!=0){
                s_rj_xij_shake.set(medianas_shake.indexOf(medianas_shake.get(i)), r_shake.get(medianas_shake.get(i)));
                S2_shake.get(medianas_shake.indexOf(medianas_shake.get(i))).add(medianas_shake.get(i));
                S_shake.get(medianas_shake.indexOf(medianas_shake.get(i))).add(r_shake.get(medianas_shake.get(i))); 
            }
        }
    }
}


