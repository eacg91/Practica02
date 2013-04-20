import java.io.*;
import java.util.*;
import java.lang.*;
public class comparar_codop {
	static final int SI=1,NO=0;
	Scanner Lectora=new Scanner (System.in);
	Stack<String> modosdir=new Stack<String>();

	
	public String modos_direccionamiento(String nombre){
		String linea,ruta="TABOP.txt",sToken="";
		int i,l,s,existe=NO,encontrado=NO;
		String[] tokensLinea=new String[6];
		File archivo = new File(ruta);
		if(!archivo.exists()){
		}
		try {
			FileReader leerArchivo = new FileReader(archivo);
			BufferedReader buffer = new BufferedReader(leerArchivo);
			existe=SI;
			while ((linea = buffer.readLine()) != null){
				encontrado=i=NO;
				StringTokenizer st=new StringTokenizer(linea.toString());
				do{
					sToken=st.nextToken();//Tomando token de la linea
					if(sToken.contains(nombre)&&nombre.length()==sToken.length())
						encontrado=SI;
					if(encontrado==SI){
						tokensLinea[i]=sToken;
						//System.out.print("\t"+tokensLinea[i]);
						i++;
					}
				}while(st.hasMoreTokens()&&encontrado==SI);
				if(encontrado==SI){
					modosdir.add(tokensLinea[1]);
					tokensLinea[4]=tokensLinea[3];//pasa pos3 a pos4
					l=tokensLinea[2].length();//calcula ancho de pos2
					s=(tokensLinea[4].charAt(0)-48)+l;
					tokensLinea[3]=Integer.toString(l);;//guardar "l" en pos3
					tokensLinea[5]=Integer.toString(s);;//guardar "s" en pos5
					for(i=0;i<6;i++)
						System.out.print("\t"+tokensLinea[i]);
					System.out.println("");
				}
			}
			System.out.println("Modos de direccionamiento: "+modosdir.toString());
			buffer.close();
		}
		catch (Exception ex){
			if(existe==NO)
				System.out.println("NO EXISTE ARCHIVO ");
		}
		return nombre;
	}
	
    public static void main(String a[]){
		String nombre;
    	Scanner Lectora=new Scanner(System.in);
		comparar_codop objeto=new comparar_codop();
		System.out.print("\t¿Cual CODOP estas buscando? : ");
		nombre=Lectora.next();
		objeto.modos_direccionamiento(nombre);
	}
}