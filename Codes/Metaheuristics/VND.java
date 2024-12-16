package pmpc;
import static pmpc.Movimentos.*;
import java.util.ArrayList;
@SuppressWarnings("unchecked")
public class VND {

    public Solucao vnd(Solucao s_vnd){        
        
        boolean melhora_movimentos=true;
        
        Movimentos mov = new Movimentos();
        
        while(melhora_movimentos==true){

            s_vnd = mov.vnd_realoc(s_vnd);
            while (s_vnd != null){
                s_vnd = mov.vnd_realoc(s_vnd);
 
            }
            s_vnd = mov.vnd_troca(s_vnd);
            if (s_vnd != null){
                     melhora_movimentos=true;
            }else{
                s_vnd = mov.vnd_subst_realoc(s_vnd);
                if(s_vnd != null){
                         melhora_movimentos=true;
                }else{
                    melhora_movimentos=false;
                }      
            }
        }
               
        return s_vnd;
    }
}


