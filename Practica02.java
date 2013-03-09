/**2013
 * Taller de Programacion de Sistemas
 * @author Edwin Alberto Castañeda García
 */
import java.io.*;
import java.util.*;
import java.lang.*;
public class Practica02 {
	String original="",retorno="",log="";
	int nLineas,end;
	Scanner Lectora=new Scanner (System.in);
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
		return 1;
	}
	return 0;
}
/*************************/
	public String QuitarComentarios(String linea){
		if(linea.contains(";")){
			int p=linea.indexOf(';');
			linea=linea.substring(0,p);
		}
		return linea;
    }
/*************************/
	public String diferenciarTokens(String linea){
		int i,flag=0,tieneCodop=0;
		StringTokenizer st=new StringTokenizer(linea);
		char pCt,c;
		String sToken,linea2;
		sToken=linea2="";
		linea2+=("\n   "+nLineas);//Cuenta numero de lineas leidas
		while(st.hasMoreTokens()){
			sToken=st.nextToken();//Tomando token de la linea
			pCt=sToken.charAt(0);//Primer caracter del token
			if(flag==0)
				linea2+="\t";
			else //Si hay Bandera, quitarla
				flag=0;
			if(Character.isLetter(pCt) && sToken.length()<=8){
				if(pCt==linea.charAt(0)){
					for(i=1;i<sToken.length();i++){
						c=sToken.charAt(i);
						if(Character.isLetterOrDigit(c)|c=='_'){
							//es Etiqueta
						}else{
							log+="\n Caracter '"+c+"' no valido en linea "+nLineas;
						}
					}
				}else if(sToken.length()<=5 && pCt!=linea.charAt(0) && sToken.indexOf('.')==sToken.lastIndexOf('.') ){
					//es CODOP
					linea2+="\t\t ";
					tieneCodop=1;
					if(sToken.contains("End")){
						end=1;
						//break;
					}
				}else{
					log+="\n Frase No reconocida en linea "+nLineas;
				}
				flag=1;
			}else{
				//es Operando
				linea2+="\t\t ";
			}
			linea2+=sToken;
			//Aqui mandar token al arbol... instruccion pendiente
		};
		if(tieneCodop==0)
			log+="\n No tiene CODOP en linea "+nLineas;
		return linea2;
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
	public String leer(String ruta){
		ArrayList array = new ArrayList();
		String linea,extension;
		int p=ruta.lastIndexOf('.');
		linea=original=retorno=log="";
		nLineas=end=0;
		extension=ruta.substring(p,ruta.length());
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
			retorno=("#linea\tEtiqueta\tCODOP \t\tOperando");
			while ((linea = buffer.readLine()) != null){
				original+=linea+"\n";
				linea=QuitarComentarios(linea); //Quitar comentarios
				if(linea.length()>0){
					nLineas++;
					linea=diferenciarTokens(linea.toString());
					retorno+=linea;
				}
			}
			buffer.close();
			/***/
			if(end==0){
				log+="\n No finaliza con End";
			}
			/***/
		}
		catch (Exception ex){
			System.out.println("NO EXISTE ARCHIVO "+ruta);
		}
		return ruta;
	}
/*************************/
    public static void main(String a[]){
    	Scanner Lectora=new Scanner(System.in);
    	Practica02 archivo=new Practica02();
    	String ruta,extension=".asm";
		int opcion,existe;
		do{
			opcion=existe=0;
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
					archivo.escribir("log.log",archivo.log);
				}
			}else
				System.out.println("\n ERROR! Solo se admiten archivos "+extension);
			
			System.out.print("\n Desea reutilizar el programa? (0=NO,1=SI): ");
			opcion=Lectora.nextInt();
		}while(opcion==1);
    }
}