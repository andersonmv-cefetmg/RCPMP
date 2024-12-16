package pmpc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Grava_Arquivo {

	public static void grava(ArrayList<Double> array){
		File arquivo = new File("SOLUCAO_xx_.txt");
		try{
			if(!arquivo.exists()){
				arquivo.createNewFile();
			}
			
			FileWriter fw = new FileWriter(arquivo, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i = 0;i < array.size();i++){
				bw.write(array.get(i).toString());
				bw.write(", ");
			}
			
			bw.newLine();
                        bw.newLine();
			
			bw.close();
			fw.close();
			
			}catch(Exception e){
				System.out.println("Erro ao escrever no arquivo");
			}
		}
        public static void grava_individuo(ArrayList<Integer> array){
		File arquivo = new File("Sol_xx.txt");
		try{
			if(!arquivo.exists()){
				arquivo.createNewFile();
			}
			
			FileWriter fw = new FileWriter(arquivo, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i = 0;i < array.size();i++){
				bw.write(array.get(i).toString());
				bw.write(" ");
			}
			
			bw.newLine();
                        bw.newLine();
			
			bw.close();
			fw.close();
			
			}catch(Exception e){
				System.out.println("Erro ao escrever no arquivo");
			}
		}




    }
	
