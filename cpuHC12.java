/**2013
 * Taller de Programacion de Sistemas
 * @author Edwin Alberto Castañeda García
 */
import java.io.*;
import java.util.*;
import java.util.Vector.*;
import java.lang.*;
public class cpuHC12 {
	static final int SI=1,NO=0;
	String original,retorno,log;
	int nLineas,end;
	Scanner Lectora=new Scanner (System.in);
	Stack<String> etiquetas=new Stack<String>();
	Stack<String> codops=new Stack<String>();
	Stack<String> operandos=new Stack<String>();
/*************************/
	public String QuitarComentarios(String linea){
		if(linea.contains(";")){
			int p=linea.indexOf(';');
			linea=linea.substring(0,p);
		}
		return linea;
    }
/*************************/
	public int mostrar(String ruta){
		File archivo = new File(ruta);
		if(archivo.exists()){
			System.out.println("\n\t---> Original de "+ruta+"\n"+original);
			System.out.println("\n\t---> Retorno en .INST \n"+retorno);
			if(log.length()>0)
				System.out.println("\n\t---> ERRORES! \n"+log);
			else
				System.out.println(log="\n\t Completado Sin Errores");
			return SI;
		}
		return NO;
	}
/*************************/
	public void escribir(String nuevaRuta,String contenido){
		File archivo = new File(nuevaRuta);
		if(archivo.exists()){
			if(archivo.delete()){
			    System.out.println("\n Reescribiendo "+nuevaRuta+"...");
			}else
			    System.out.println("\n Error al reescribir "+nuevaRuta+"...");
		}else
			System.out.println("\n Creando "+nuevaRuta+"...");
		try {
			FileWriter escribirArchivo = new FileWriter(archivo, true);
			BufferedWriter buffer = new BufferedWriter(escribirArchivo);
			buffer.write(contenido);
			buffer.newLine();
			buffer.close();
		}
		catch (Exception ex) {
		}
	}
/*************************/
	public int esEtiqueta(String sToken){
		char c;
		int tieneEtiqueta=NO;
		for(int i=1;i<sToken.length();i++){
			c=sToken.charAt(i);
			if(Character.isLetterOrDigit(c)|c=='_'){
				tieneEtiqueta=SI;
			}else
				log+="\n Caracter '"+c+"' no valido en linea "+nLineas;
		}
		if(tieneEtiqueta==SI){
			etiquetas.add(sToken); //es Etiqueta
		}
		return tieneEtiqueta;
	}
/*************************/
	public int esCODOP(String sToken,int tieneEtiqueta){
		sToken=sToken.toUpperCase();
		if(sToken.length()<=5 && sToken.indexOf('.')==sToken.lastIndexOf('.') ){
			/* A continuacion, comparar si el token es algun CODOP existente
			y efectuar validacion a prueba de errores */
			if(sToken.contains("END")){
				end=SI;
			}
			codops.add(sToken); //es Codop
			if(tieneEtiqueta==NO)
				etiquetas.add("NULL");
			return SI;
		}
		return NO;
	}
/*************************/
	public void esOperando(String sToken, int tieneCodop){		
		if(tieneCodop==NO){
			codops.add("NULL");
			log+="\n No tiene CODOP en linea "+nLineas;
		}else if(end==SI)
			codops.add(" ");
		while(codops.size()>(operandos.size()+1))
			operandos.add("NULL");
		operandos.add(sToken); //es Operando
	}
/*************************/
	public String diferenciarTokens(String linea){
		int tieneCodop=NO,tieneEtiqueta=NO;
		String sToken;
		char pct,pcl=linea.charAt(0);
		StringTokenizer st=new StringTokenizer(linea);
		while(st.hasMoreTokens()){
			sToken=st.nextToken();//Tomando token de la linea
			pct=sToken.charAt(0);//Primer caracter del token
			if(Character.isLetter(pct) && sToken.length()<=8 && tieneCodop==NO){
				if(tieneEtiqueta==NO && pcl!=' '){
					tieneEtiqueta=esEtiqueta(sToken.toUpperCase());
				}else if(tieneCodop==NO){
					boolean i=st.hasMoreTokens();
					tieneCodop=esCODOP(sToken.toUpperCase(),tieneEtiqueta);
				}else
					log+="\n Token "+sToken+" NO reconocido de linea "+nLineas;
			}else if(end==NO)
				esOperando(sToken,tieneCodop);
		};
		return linea;
    }
/*************************/
	public void retornoLOG(){
		//codops.get(lista.size()-1).toString();//esta no hace nada aqui
		if(end==0){
			log+="\n No finaliza con End";
		}
	}
/*************************/
	//Esta funcion retornará el archivo INST
	public String retornoINST(){
		int i,j;
		String linea;
		while(codops.size()>(operandos.size()+1))
			operandos.add("NULL");
		retorno="#linea Etiqueta  CODOP  Operando";
		for(j=0;j<nLineas;j++){
			linea="\n   "+j+"\t"+etiquetas.get(j).toString();
			for(i=16-linea.length();i>0;i--)
				linea+=" ";
			linea+=(codops.get(j).toString());
			if(j!=nLineas-1){
				for(i=23-linea.length();i>0;i--)
					linea+=" ";
				linea+=(operandos.get(j).toString());
			}
			retorno+=linea;
		}
		return retorno;
	}
/*************************/
    public String leer(String ruta){
		String linea,extension;
		int p=ruta.lastIndexOf('.'),existe=NO;
		extension=ruta.substring(p,ruta.length());
		linea=original="";
		nLineas=0;
		File archivo = new File(ruta);
		if(!archivo.exists()){
			if(ruta.contains(extension.toUpperCase()) ){
				ruta=ruta.substring(0,p)+extension.toLowerCase();
				archivo = new File(ruta);
			}else if(ruta.contains(ruta.toLowerCase()) ){
				ruta=ruta.substring(0,p)+extension.toUpperCase();
				archivo = new File(ruta);
			}
		}
		try {
			FileReader leerArchivo = new FileReader(archivo);
			BufferedReader buffer = new BufferedReader(leerArchivo);
			existe=SI;
			while ((linea = buffer.readLine()) != null){
				original+=linea+"\n";
				linea=QuitarComentarios(linea); //Quitar comentarios
				if(linea.length()>0){ //Si no es linea vacia
					diferenciarTokens(linea.toString());
					nLineas++;
				}
			}
			buffer.close();
			retornoINST();
			retornoLOG();
		}
		catch (Exception ex){
			if(existe==NO)
				System.out.println("NO EXISTE ARCHIVO "+ruta);
		}
		return ruta;
	}
/*************************/
    public static void main(String a[]){
    	Scanner Lectora=new Scanner(System.in);
    	cpuHC12 archivo=new cpuHC12();
    	String ruta,extension=".asm";
		int opcion,existe;
		do{
			opcion=existe=0;
			archivo.log="";
			archivo.codops.clear();   //limpia lista de codops
			archivo.etiquetas.clear();//limpia lista de etiquetas
			archivo.operandos.clear();//limpia lista de operandos
		    System.out.print("\n Por Favor, escriba ruta con archivo:   ");
		    ruta=Lectora.next();
			if(!ruta.contains(".")){
				System.out.println("\n No hay punto \".\" en el nombre, se abrira "+ruta+extension);
				ruta+=extension;
			}
			if(ruta.contains(extension.toUpperCase())|ruta.contains(extension.toLowerCase()) ){
				ruta=archivo.leer(ruta);
				existe=archivo.mostrar(ruta);
				if(existe==1){
					archivo.escribir(ruta.substring(0,ruta.lastIndexOf('.'))+".INST",archivo.retorno);
					archivo.escribir("errores.log",archivo.log);
				}
			}else
				System.out.println("\n ERROR! Solo se admiten archivos "+extension);
			System.out.print("\n Desea reutilizar el programa? (0=NO,1=SI): ");
			opcion=Lectora.nextInt();
		}while(opcion==1);
    }
}