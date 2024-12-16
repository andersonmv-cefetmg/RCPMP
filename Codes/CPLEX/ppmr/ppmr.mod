/********************************************
 * OPL 12.10.0.0 Model
 * Author: anderson
 * Creation Date: 27 de abr de 2020 at 14:33:20
 *********************************************/
execute
{
  thisOplModel.settings.run_engineLog="LogCPLEX_724_p10_0_0_Aggregate.txt";
}

//execute timeTermination {
    //cplex.tilim = 86400;   // set time model stop (second)
  //  }
   
int n =...; // depositos
int m =...; //clientes
int p = ...;
int Q=...;
int gamma=...;
float variacao=...;
range N = 1..n;
range M = 1..m;
float q[M]=...;

dvar boolean x[N][M];
dvar boolean y[N];
dvar float beta[N];
dvar float rho[N][M];


float d[N][M] =...;

float mySeed;
execute{
mySeed = Opl.srand(300);
}


float q_dn[j in M] = q[j]-(q[j]*variacao);
float q_up[j in M] = q[j]+(q[j]*variacao);

float q_ch[j in M]= ((q_dn[j] + q_up[j])/2);
 
float r[j in M]= q_up[j]-q_ch[j];


minimize sum(i in N, j in M) d[i][j]*x[i][j];

subject to{

forall (j in M) sum (i in N) x[i][j]==1;
sum (i in N) y[i]==p;
forall (i in M) sum (j in M) q_ch[j]*x[i][j] + gamma*beta[i] + sum(j in M)rho[i][j] <= Q*y[i];

//forall (i in M, j in M) x[i][j] <= y[i];

forall (i in N, j in M) beta[i] + rho[i][j] >= r[j]*x[i][j];
forall (i in N, j in M) rho[i][j]>=0;
forall (i in N) beta[i] >= 0;
}


main{


  thisOplModel.generate();
  var RCPMP = thisOplModel;
  var def = RCPMP.modelDefinition;
  var data = RCPMP.dataElements;
 
  // Primeira rodada - usando gamma e variacao do arquivo de dados

 
var ofile = new IloOplOutputFile("saida.txt");
  if(cplex.solve()){
  ofile.writeln("Solve successful; solve status="+cplex.getCplexStatus());
  ofile.writeln("Dados: gamma="+data.gamma);
    ofile.writeln("Dados: variacao="+data.variacao);
    ofile.writeln("Dados: p="+data.p);
  ofile.writeln("Objective value="+cplex.getObjValue());

  }

    if ( RCPMP!=thisOplModel) {
      RCPMP.end();
    }
    RCPMP = new IloOplModel(def,cplex);
    data.p = 5; // Mudança no valor de p
    data.gamma = 56; // Mudança no valor de gamma
    data.variacao = 0.10; // Mudança no valor de variacao
    data.Q = 3710; // Mudança no valor da demanda
    RCPMP.addDataSource(data);
    RCPMP.generate();
   
  if(cplex.solve()){
  ofile.writeln("Solve successful; solve status="+cplex.getCplexStatus());
  ofile.writeln("Dados: gamma="+data.gamma);
    ofile.writeln("Dados: variacao="+data.variacao);
    ofile.writeln("Dados: p="+data.p);
    ofile.writeln("Dados: Q="+data.Q);
  ofile.writeln("Objective value="+cplex.getObjValue());
  }
 
 
   if ( RCPMP!=thisOplModel) {
      RCPMP.end();
    }
    RCPMP = new IloOplModel(def,cplex);
    data.p = 5; // Mudança no valor de p
    data.gamma = 56; // Mudança no valor de gamma
    data.variacao = 0.15; // Mudança no valor de variacao
    data.Q = 3710; // Mudança no valor da demanda
    RCPMP.addDataSource(data);
    RCPMP.generate();
   
  if(cplex.solve()){
  ofile.writeln("Solve successful; solve status="+cplex.getCplexStatus());
  ofile.writeln("Dados: gamma="+data.gamma);
    ofile.writeln("Dados: variacao="+data.variacao);
    ofile.writeln("Dados: p="+data.p);
    ofile.writeln("Dados: Q="+data.Q);
  ofile.writeln("Objective value="+cplex.getObjValue());
  }
 
    //ofile.close();
   
}






