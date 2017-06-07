package co.edu.udistrital.finalseg.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import co.edu.udistrital.finalseg.domain.FileAttribs;

public class AppUtils {

	
	public static byte[] generateKeyHash(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		byte[] bytesOfMessage = key.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] md5b = md.digest(bytesOfMessage);					
		byte[] b =Arrays.copyOf(md5b, 16);
		return b;
	}
	
	 public static File convertMultipart(MultipartFile file, String folder) throws IOException
	    {        	
	        File convFile = new java.io.File(folder + file.getOriginalFilename());
	        convFile.createNewFile(); 
	        FileOutputStream fos = new FileOutputStream(convFile); 
	        fos.write(file.getBytes());
	        fos.close(); 
	        return convFile;
	    }
	 
	   public static List<File> obtenerListArchivos(String dir){    
		try {
				List<File> filesInFolder = Files.walk(Paths.get(dir))
				        .filter(Files::isRegularFile)
				        .map(Path::toFile)
				        .collect(Collectors.toList());				
				return filesInFolder;
			} catch (IOException e) {
				return null;
			}	
	   }
	   public static List<FileAttribs> obtenerFileListAttribs(String dir){
		   
		   List<File> listaArchivos=obtenerListArchivos(dir);
		   List<FileAttribs> listaFileatt=new ArrayList<>();
		   for (File archivo : listaArchivos) {
			   FileAttribs fileatrib=new FileAttribs(archivo.getName(), getApropiatedFileSize(archivo.length()),getFileExtension(archivo));			   
			   listaFileatt.add(fileatrib);
		   }
		   return listaFileatt;
	   }
	
	   public static String getApropiatedFileSize(long size){
		   	long fileSizeInBytes = size;
			// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
			long fileSizeInKB = fileSizeInBytes / 1024;
			// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
			long fileSizeInMB = fileSizeInKB / 1024;
			if(fileSizeInMB>0){
				return fileSizeInMB + " Mb";
			}
			else{
				return fileSizeInKB + " Kb";
			}		
	   }
	   
	   public static String getFileExtension(File file) {
		    String name = file.getName();
		    try {
		        return name.substring(name.lastIndexOf(".") + 1);
		    } catch (Exception e) {
		        return "";
		    }
		}
	   public static String getFileExtension(String filename) {
		    String name = filename;
		    try {
		        return name.substring(name.lastIndexOf(".") + 1);
		    } catch (Exception e) {
		        return "";
		    }
		}
}
