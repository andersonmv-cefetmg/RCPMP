//24/10/2022
package pmpc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import static pmpc.Matriz_Distancia.*;
import static pmpc.Principal.*;
import static pmpc.Movimentos.*;
@SuppressWarnings("unchecked")
public class Solucao {

    private ArrayList<Integer> solucao = new ArrayList<>();
    private ArrayList<Integer> medianas = new ArrayList<>();
    private  ArrayList<Integer> vetor_clientes = new ArrayList<>();
    private ArrayList<Integer> instalacoes = new ArrayList<>(); 

    private  ArrayList<Float> s_qj_xij = new ArrayList<>();
    private  ArrayList<Float> s_rj_xij = new ArrayList<>();

    private  ArrayList<ArrayList<Float>> S = new ArrayList<>();
    private  ArrayList<ArrayList<Integer>> S2 = new ArrayList<>();
    

    private  ArrayList<Integer> demandas_clientes = new ArrayList<>();
    private  ArrayList<Float> demandas = new ArrayList<>();
    private  ArrayList<Float> q = new ArrayList<>();
    private  ArrayList<Float> q_ch = new ArrayList<>();
    private  ArrayList<Float> q_dn = new ArrayList<>();
    private  ArrayList<Float> q_up = new ArrayList<>();
    private  ArrayList<Float> r = new ArrayList<>();
    private  ArrayList<Float> r_max = new ArrayList<>();
    private  ArrayList<Integer> clientes_r_max = new ArrayList<>();
    private  ArrayList<Float> aux_soma_acumulada_ordenada = new ArrayList<>();

    private  ArrayList<Integer> LRC = new ArrayList<>();


    private  int mediana =0;
    private  int cliente=0;
    private  float func_guia=0;
    private  float s_a_o=0; 
    private  float raio_cliente=0;
   
    private  float r_t_i = 0; 
    private  float  d_a_i = 0; 
    private  int opcao = 0;
    private  float fo_solucao =0;
    private  float fo_best =0;
    private boolean solucao_viavel = false;
    

    
    public ArrayList<Integer> getMedianas() {
        return medianas;
    }
     public ArrayList<Integer> getVetor_clientes(int par) {
        return vetor_clientes;
    }
    
    public ArrayList<Integer> getSolucao() {
        return solucao;
    }

    public ArrayList<Float> getS_qj_xij() {
        return s_qj_xij;
    }

    public ArrayList<Float> getS_rj_xij() {
        return s_rj_xij;
    }

    public ArrayList<ArrayList<Float>> getS() {
        return S;
    }

    public ArrayList<ArrayList<Integer>> getS2() {
        return S2;
    }

    public ArrayList<Float> getQ_ch() {
        return q_ch;
    }

    public ArrayList<Float> getR() {
        return r;
    }


    
    public float getFo_solucao() {
        return fo_solucao;
    }
    
    public float getFo_best() {
        return fo_best;
    }

    public boolean getSolucao_viavel() {
        return solucao_viavel;
    }

    public boolean isSolucao_viavel() {
        return solucao_viavel;
    }


    public void setS_qj_xij(int j, Float elem) {
        this.s_qj_xij.set(j, elem);
    }
    
    
    public void setSolucao(int j, int elem) {
        this.solucao.set(j, elem);
    }

    public void setMedianas(int j, int elem) {
        this.medianas.set(j, elem);
    }
    
    public void setVetor_clientes(int j, int elem) {
        this.vetor_clientes.set(j, elem);
    }

    public void setS_rj_xij(int j, Float elem) {
        this.s_rj_xij.set(j, elem);
    }

    public void setS(int j, ArrayList<Float> S) {
        this.S.set(j, S);
    }

    public void setS2(int j, ArrayList<Integer> S) {
        this.S2.set(j, S);
    }
    
    public void setFo_solucao(float fo_solucao) {
        this.fo_solucao = fo_solucao;
    }
    
    public void setFo_best(float fo_best) {
        this.fo_best = fo_best;
    }

    public void setSolucao_viavel(boolean solucao_viavel) {
        this.solucao_viavel = solucao_viavel;
    }
    

    @SuppressWarnings("empty-statement")
    //INICIALIZA ESTRUTURAS
    public void Inicializa_Arrays(){  
        aux_soma_acumulada_ordenada = (ArrayList<Float>) Matriz_Distancia.soma_acumulada_ordenada.clone(); 

        Random rnd = new Random();
        int rnd_q_dn = 0;
        float zero = 0;
        
        solucao =  new ArrayList<>();
        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) { 
             solucao.add(-2);
        }
        
        vetor_clientes =  new ArrayList<>();
        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) { 
             vetor_clientes.add(0);
        }
 
        s_qj_xij=  new ArrayList<>();
        for (int j = 0; j < Matriz_Distancia.NUM_MEDIANAS; j++) { 
             s_qj_xij.add(zero);
        }
        
        s_rj_xij=  new ArrayList<>();
        for (int j = 0; j < Matriz_Distancia.NUM_MEDIANAS; j++) { 
             s_rj_xij.add(zero);
        }

        q =  new ArrayList<>();
        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) { 
             q.add(zero);
        }
        
        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) { 
            q.set(j, (float)Matriz_Distancia.demanda.get(j));
        }
        q_dn=  new ArrayList<>();
        for (int j = 1; j <= Matriz_Distancia.TAM_INSTANCIA; j++) { 
             q_dn.add(zero);
        }
        
        q_up=  new ArrayList<>();
        for (int j = 1; j <= Matriz_Distancia.TAM_INSTANCIA; j++) { 
             q_up.add(zero);;
        }

        q_ch=  new ArrayList<>();
        for (int j = 1; j <= Matriz_Distancia.TAM_INSTANCIA; j++) { 
             q_ch.add(zero);
        }
        r =  new ArrayList<>();
        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) { 
             r.add(zero);;
        }
        
        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) { 
            
           q_dn.set(j, (float)Matriz_Distancia.demanda.get(j)-(Matriz_Distancia.demanda.get(j)*Main.VARIACAO) );
	}
        
        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) { 
            
           q_up.set(j, (float)Matriz_Distancia.demanda.get(j)+(Matriz_Distancia.demanda.get(j)*Main.VARIACAO) );
	}

        for(int j=0;j< pmpc.Matriz_Distancia.TAM_INSTANCIA;j++)
            q_ch.set(j,((q_dn.get(j)+q_up.get(j))/2));
        
        for(int j=0;j< pmpc.Matriz_Distancia.TAM_INSTANCIA;j++)
            r.set(j,(q_up.get(j)-q_ch.get(j)));
        
        for(int i=0; i<Matriz_Distancia.NUM_MEDIANAS;i++){
            S.add(r_max);
            r_max = new  ArrayList<Float>();
        }
        for(int i=0; i<Matriz_Distancia.NUM_MEDIANAS;i++){
            S2.add(clientes_r_max);
            clientes_r_max = new  ArrayList<Integer>();
        }

    }
    
     public boolean gera_solucao_grasp(){ 
        float d_mim = 0;
        float d_max=0;
        Random rnd = new Random();
        int ok=0;
        boolean  resposta = true;
        int periodicidade=0;        
        
        while(resposta == true){       

            mediana = medianas.get(rnd.nextInt(Matriz_Distancia.NUM_MEDIANAS)); 
            cliente = vetor_clientes.get(rnd.nextInt(vetor_clientes.size())); 
            vetor_clientes.remove(vetor_clientes.indexOf(cliente)); 
//                 solucao_versao1(); 
//                 solucao_versao2(); 
//                 solucao_versao3(); 
//                 solucao_versao4(); 
                if(verifica_demandas()){
                    solucao.set(cliente,mediana);
                    if(solucao.contains(-2)){
                        resposta = true;
                    }else{
                        resposta = false;
                    }
                }else{
                    for(int j=0;j< Matriz_Distancia.NUM_MEDIANAS;j++){
                       mediana = medianas.get(j); 
                       if(verifica_demandas()){
                           solucao.set(cliente,mediana);
                           j = Matriz_Distancia.NUM_MEDIANAS;
                        }else{
                           if(j == Matriz_Distancia.NUM_MEDIANAS - 1){
                               resposta=false;
                           }
                        }
                    }
                }
                if (vetor_clientes.size() == 0)
                    resposta = false;

        }       
 
       if (!solucao.contains(-2)){
           fo_solucao = Avalia_fo.avalia(solucao);
           fo_best = Avalia_fo.avalia(solucao);
           System.out.println(solucao);
           System.out.println("Solução Viável");
            return true;
        }else{
           System.out.println(solucao);
           System.out.println("Solução  Inviável");
            return false;
        }       
    }    


   
    
    public void gera_medianas(){ 
        Random rnd = new Random();
         while(medianas.size() < Matriz_Distancia.NUM_MEDIANAS) {
                mediana = rnd.nextInt(Matriz_Distancia.TAM_INSTANCIA);
                if(!medianas.contains(mediana) )
                     medianas.add(mediana);
         }
    }
    
        public void f_guia(){
        
        for (int j = 0; j < Matriz_Distancia.NUM_MEDIANAS; j++) { 
            float min = Collections.min(Matriz_Distancia.soma_acumulada_ordenada);
            float max = Collections.max(Matriz_Distancia.soma_acumulada_ordenada);
            func_guia = (min+(Main.ALFA*(max-min)));
            gera_medianas_grasp();
            aux_soma_acumulada_ordenada.remove(s_a_o);

        }
    }
    
    public void gera_medianas_grasp(){ 
        float dif=-1000000;

        int index = 0;        
        for(int i = 0; i < aux_soma_acumulada_ordenada.size(); i++){
                s_a_o = aux_soma_acumulada_ordenada.get(i);
                dif = s_a_o -func_guia;
                if(dif >=0){
                    index = i;
                    break;
                }
        }
     
        LRC.add(Matriz_Distancia.soma_acumulada_dist.indexOf(s_a_o));
        int median = LRC.size()-1;
        medianas.add(LRC.get(median));
        
            
    }
    
    public void vetor_clientes(){ 
            vetor_clientes.clear();
            for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++) { 
                    if(!medianas.contains(j)){
                          vetor_clientes.add(j);
                          demandas_clientes.add(demanda.get(j));
                    }
            }
    }
    
    
    public void aloca_medianas(){

        for(int i=0;i< pmpc.Matriz_Distancia.NUM_MEDIANAS;i++){
           
            solucao.set(medianas.get(i),medianas.get(i));

            s_qj_xij.set(medianas.indexOf(medianas.get(i)), q_ch.get(medianas.get(i))); 
            
            if(Main.GAMMA!=0){
                s_rj_xij.set(medianas.indexOf(medianas.get(i)), r.get(medianas.get(i))); 
            
                S2.get(medianas.indexOf(medianas.get(i))).add(medianas.get(i));
                S.get(medianas.indexOf(medianas.get(i))).add(r.get(medianas.get(i))); 
            }
        }
    }

    public boolean verifica_demandas(){
        Random rnd = new Random();
        boolean alocacao_viavel = false;
        int ok=1;

        int tam = S.get(medianas.indexOf(mediana)).size();

        raio_cliente = 0;

        opcao = 0;
        
        if (tam < Main.GAMMA){
            opcao = 1;
            d_a_i =  (q_ch.get(cliente) + s_qj_xij.get(medianas.indexOf(mediana)));  
            
            r_t_i =  (r.get(cliente)  + s_rj_xij.get(medianas.indexOf(mediana)));  
            
            if ((d_a_i + r_t_i) <= Ler_Arquivo.capacidade.get(mediana)){ 
               
                s_rj_xij.set(medianas.indexOf(mediana), r_t_i);
                s_qj_xij.set(medianas.indexOf(mediana), d_a_i);
                
                S2.get(medianas.indexOf(mediana)).add(cliente);
                S.get(medianas.indexOf(mediana)).add(r.get(cliente));
                
                alocacao_viavel = true;
            }else{ 
                alocacao_viavel = false;  
            }
        }else{
            
            opcao=2;
            double minimo =0;
            int i_minimo =0;
           
            if (Main.GAMMA!=0){
                minimo = Collections.min(S.get(medianas.indexOf(mediana)));
                i_minimo = S.get(medianas.indexOf(mediana)).indexOf(Collections.min(S.get(medianas.indexOf(mediana))));
            }
            if (r.get(cliente) > minimo){
                opcao=21;
                    
                        float verifica_s_raios =  (float) ((s_rj_xij.get(medianas.indexOf(mediana)) - minimo) + r.get(cliente));
                    
                        float verifica_s_demandas = s_qj_xij.get(medianas.indexOf(mediana)) + q_ch.get(cliente);
                        
                        if ((verifica_s_raios + verifica_s_demandas) <= Ler_Arquivo.capacidade.get(mediana)){
                            opcao = 211;
                            d_a_i = verifica_s_demandas;
                            
                            S2.get(medianas.indexOf(mediana)).remove(S.get(medianas.indexOf(mediana)).indexOf(Collections.min(S.get(medianas.indexOf(mediana)))));
                            S2.get(medianas.indexOf(mediana)).add(cliente);


                            S.get(medianas.indexOf(mediana)).remove(S.get(medianas.indexOf(mediana)).indexOf(Collections.min(S.get(medianas.indexOf(mediana)))));
                            S.get(medianas.indexOf(mediana)).add(r.get(cliente));   

                            float soma =0;
                            for(int j=0; j < S.get(medianas.indexOf(mediana)).size(); j++){
                                soma = soma + S.get(medianas.indexOf(mediana)).get(j);
                            }
                            s_rj_xij.set(medianas.indexOf(mediana), soma);
                            s_qj_xij.set(medianas.indexOf(mediana), d_a_i);
            
                                alocacao_viavel = true;
                        }else{ 
                                alocacao_viavel=false; 
                        }
//                    }
            }else{
                    opcao=212;
                    float verifica_s_demandas = s_qj_xij.get(medianas.indexOf(mediana)) + q_ch.get(cliente);  
                    d_a_i =  verifica_s_demandas;
                    if(verifica_s_demandas <= Ler_Arquivo.capacidade.get(mediana)){
                        
                        s_qj_xij.set(medianas.indexOf(mediana),  d_a_i);
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
    
    
    public void aloca_somas_raio_dem(){
    }
    
    public float calcula_fo(){

        float custo = 0;
        float fo = 0;
 
        for (int j = 0; j < Matriz_Distancia.TAM_INSTANCIA; j++){
            int mediana = solucao.get(j);
            custo = Matriz_Distancia.matriz.get(mediana).get(j); 
            fo = fo + custo;   
        }
        
        return  fo;
    }
    
    public Solucao clone(){
    
        Solucao solucao_copia = new Solucao();
        
        for(int i = 0; i < solucao.size();i++){
            solucao_copia.getSolucao().add(solucao.get(i));
            solucao_copia.getQ_ch().add(q_ch.get(i));
            solucao_copia.getR().add(r.get(i));
        }
        
        for (int i = 0; i < medianas.size(); i++) {
            solucao_copia.getMedianas().add(medianas.get(i));
            
            solucao_copia.getS_qj_xij().add(s_qj_xij.get(i));
            solucao_copia.getS_rj_xij().add(s_rj_xij.get(i));
        }

        
        for (int i = 0; i < medianas.size(); i++) {
              
                ArrayList<Float> a = S.get(i);
                ArrayList<Integer> b = S2.get(i);
                
                solucao_copia.getS().add(i, a);
                solucao_copia.getS2().add(i, b);

        }
        
        solucao_copia.setFo_solucao(this.getFo_solucao());
        
        solucao_copia.setFo_best(this.getFo_best());

        solucao_copia.setSolucao_viavel(this.getSolucao_viavel());
        
        return solucao_copia;
    }

    int getSolucao(int cliente_candidato) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
   
    
   public void aloca_medianas_rand(){
       Random rnd = new Random(); 
       
       medianas.clear();
       for(int i=0; i<pmpc.Matriz_Distancia.NUM_MEDIANAS;i++){
           mediana =  rnd.nextInt(Matriz_Distancia.TAM_INSTANCIA);
           if (!medianas.contains(mediana)){
                medianas.add(i,mediana);
            }else{
               i=i-1;
           }
        }
  
       for(int i=0;i< pmpc.Matriz_Distancia.NUM_MEDIANAS;i++){
           
            solucao.set(medianas.get(i),medianas.get(i));

            s_qj_xij.set(medianas.indexOf(medianas.get(i)), q_ch.get(medianas.get(i)));
            
            if(Main.GAMMA!=0){
                s_rj_xij.set(medianas.indexOf(medianas.get(i)), r.get(medianas.get(i)));
            
                S2.get(medianas.indexOf(medianas.get(i))).add(medianas.get(i));
                S.get(medianas.indexOf(medianas.get(i))).add(r.get(medianas.get(i))); 
            }
        }
    } 
     
    public boolean gera_solucao_rand(){ 

        Random rnd = new Random();
        boolean  resposta = true;
        aloca_medianas_rand();
        vetor_clientes();
        int mesma_mediana = 0;
        while(resposta == true){       
                cliente = vetor_clientes.get(rnd.nextInt(vetor_clientes.size()));
                vetor_clientes.remove(vetor_clientes.indexOf(cliente));
                mediana = medianas.get(rnd.nextInt(Matriz_Distancia.NUM_MEDIANAS)); 
//                 solucao_versao1(); 
//                 solucao_versao2(); 
//                 solucao_versao3(); 
//                 solucao_versao4(); 
              
                if(verifica_demandas()){
                    solucao.set(cliente,mediana);
                    if(solucao.contains(-2)){
                        resposta = true;
                    }else{
                        resposta = false;
                    }
                }else{
                    for(int j=0;j< Matriz_Distancia.NUM_MEDIANAS;j++){
                       mediana = medianas.get(j); 
                       if(verifica_demandas()){

                           solucao.set(cliente,mediana);
                           j = Matriz_Distancia.NUM_MEDIANAS;
                        }else{
                           if(j == Matriz_Distancia.NUM_MEDIANAS - 1){
                               resposta=false;
                           }
                        }
                    }
                }
                if (vetor_clientes.size() == 0)
                    resposta = false;

        }       

       if (!solucao.contains(-2)){
           fo_solucao = Avalia_fo.avalia(solucao);
           fo_best = Avalia_fo.avalia(solucao);
            return true;
        }else{
            return false;
        }

    }  

    public void versao_randomica(){
        Random rnd = new Random();
        mediana = medianas.get(rnd.nextInt(Matriz_Distancia.NUM_MEDIANAS)); 
        cliente = vetor_clientes.get(rnd.nextInt(vetor_clientes.size())); 
        vetor_clientes.remove(vetor_clientes.indexOf(cliente)); 
    }

    public void solucao_versao1(){ 

        ArrayList<Float> distancias_das_instalacoes_candidatas = new ArrayList<>();
        Random rnd = new Random();

        float distancia=0;

        
        int instalação_mais_proxima =0;
        
        float distancia_da_instalacao_mais_proxima = 0;
        
        cliente = vetor_clientes.get(rnd.nextInt(vetor_clientes.size())); 
        vetor_clientes.remove(vetor_clientes.indexOf(cliente)); 
        
        for (int i = 0; i< Matriz_Distancia.NUM_MEDIANAS; i++){ 
            distancia = matriz.get(cliente).get(medianas.get(i));
            distancias_das_instalacoes_candidatas.add(distancia);
            
        }
        distancia_da_instalacao_mais_proxima = Collections.min(distancias_das_instalacoes_candidatas);
        mediana = medianas.get(distancias_das_instalacoes_candidatas.indexOf(distancia_da_instalacao_mais_proxima));

    }

    public void solucao_versao2(){

        Random rnd = new Random();
        int indice_cliente_maior_demanda=0;
        int maior_demanda = 0;
        
        mediana = medianas.get(rnd.nextInt(Matriz_Distancia.NUM_MEDIANAS)); 

        maior_demanda = Collections.max(demandas_clientes); 
        cliente = vetor_clientes.get(demandas_clientes.indexOf(maior_demanda)); 
        vetor_clientes.remove(vetor_clientes.indexOf(cliente)); 
        demandas_clientes.remove(demandas_clientes.indexOf(maior_demanda)); 

    }
    
    public boolean gera_solucao_maior_demanda(){ 

        Random rnd = new Random();
        float maior_demanda = 0;
        float medianax =0;
        boolean  resposta = true;
  
        for (int i = 0; i< Matriz_Distancia.TAM_INSTANCIA; i++) { 
            instalacoes.add(i);
            demandas.add(q.get(i));
        }
        for (int i = 0; i< Matriz_Distancia.NUM_MEDIANAS; i++) { 
            maior_demanda = Collections.max(demandas); 
            mediana = (int) demandas.indexOf(maior_demanda);
            medianas.add(mediana);
            demandas.remove(mediana);
        }
  
        aloca_medianas();
        vetor_clientes();
        
        while(resposta == true){       

            cliente = vetor_clientes.get(rnd.nextInt(vetor_clientes.size())); 
            vetor_clientes.remove(vetor_clientes.indexOf(cliente)); 

            if(verifica_demandas()){
                aloca_somas_raio_dem();
                solucao.set(cliente,mediana);
                if(solucao.contains(-2)){
                    resposta = true;
                }else{
                    resposta = false;
                }
            }else{
                for(int j=0;j< Matriz_Distancia.NUM_MEDIANAS;j++){
                   mediana = medianas.get(j); 
                   if(verifica_demandas()){
                        aloca_somas_raio_dem();
                        solucao.set(cliente,mediana);
                        j = Matriz_Distancia.NUM_MEDIANAS;
                    }else{
                        if(j == Matriz_Distancia.NUM_MEDIANAS - 1){
                               resposta=false;
                        }
                    }
                }
            }
                if (vetor_clientes.size() == 0)
                    resposta = false;

        }       
      
       
       if (!solucao.contains(-2)){
           fo_solucao = Avalia_fo.avalia(solucao);
           fo_best = Avalia_fo.avalia(solucao);
           System.out.println(solucao);
           System.out.println("Solução Viável");
            return true;
        }else{
           System.out.println(solucao);
           System.out.println("Solução  Inviável");
            return false;
        }    
    
    }
 
    public boolean solucao_versao3(){
        // Cliente Maior demanda.
        // Instalação mais próxima.

        Random rnd = new Random();
        int indice_cliente_maior_demanda=0;
        int maior_demanda = 0;
        boolean  resposta = true;
        float distancia=0;
        int instalação_mais_proxima =0;
        float distancia_da_instalacao_mais_proxima = 0;
        

        while(resposta == true ){
            
            maior_demanda = Collections.max(demandas_clientes); 
            cliente = vetor_clientes.get(demandas_clientes.indexOf(maior_demanda)); 
            vetor_clientes.remove(vetor_clientes.indexOf(cliente)); 
            demandas_clientes.remove(demandas_clientes.indexOf(maior_demanda)); 
            
            ArrayList<Float> distancias_das_instalacoes_candidatas = new ArrayList<>();
            
            for (int i = 0; i< Matriz_Distancia.NUM_MEDIANAS; i++){ 
                distancia = matriz.get(cliente).get(medianas.get(i));
                distancias_das_instalacoes_candidatas.add(distancia);

            }
            distancia_da_instalacao_mais_proxima = Collections.min(distancias_das_instalacoes_candidatas);
            mediana = medianas.get(distancias_das_instalacoes_candidatas.indexOf(distancia_da_instalacao_mais_proxima));
            distancias_das_instalacoes_candidatas.clear();
            
            if(verifica_demandas()){
                aloca_somas_raio_dem();
                solucao.set(cliente,mediana);
                if(solucao.contains(-2)){
                    resposta = true;
                }else{
                    resposta = false;
                }
            }else{
                for(int j=0;j< Matriz_Distancia.NUM_MEDIANAS;j++){
                    mediana = medianas.get(j); 
                    
                    if(verifica_demandas()){
                        solucao.set(cliente,mediana);
                        if(solucao.contains(-2)){
                            resposta = true;
                        }else{
                            resposta = false;
                        }       
                        j = Matriz_Distancia.NUM_MEDIANAS;
                    }else{
                        if(j == Matriz_Distancia.NUM_MEDIANAS - 1){
                            resposta=false;
                        }
                    }
                }
            }
        }    
        if (!solucao.contains(-2)){
           fo_solucao = Avalia_fo.avalia(solucao);
           fo_best = Avalia_fo.avalia(solucao);
           System.out.println(solucao);
           System.out.println("Solução Viável");
            return true;
        }else{
           System.out.println(solucao);
           System.out.println("Solução  Inviável");
            return false;
        }    
    }
        public void solucao_versao4(){

        Random rnd = new Random();
        int indice_cliente_maior_demanda=0;
        int maior_demanda = 0;
        
        mediana = medianas.get(rnd.nextInt(Matriz_Distancia.NUM_MEDIANAS)); 

        maior_demanda = Collections.max(demandas_clientes); 
        cliente = vetor_clientes.get(demandas_clientes.indexOf(maior_demanda)); 
        vetor_clientes.remove(vetor_clientes.indexOf(cliente));
        demandas_clientes.remove(demandas_clientes.indexOf(maior_demanda)); 
    } 
} 
    
