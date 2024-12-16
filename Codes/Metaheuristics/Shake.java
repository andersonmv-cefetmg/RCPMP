package pmpc;

import java.util.ArrayList;
import pmpc.Solucao.*;
import static pmpc.Movimentos.*;
import static pmpc.Principal.*;

@SuppressWarnings("unchecked")
public class Shake{

    public Solucao shake(Solucao s_shake, int nivel_pert){
        if (nivel_pert > Main.NIVEL_PERT_MAX) 
            nivel_pert = Main.NIVEL_PERT_MAX; 
            Movimentos mov = new Movimentos();
            s_shake = mov.shake(s_shake, nivel_pert);
        return s_shake;
   }

}
    
    

