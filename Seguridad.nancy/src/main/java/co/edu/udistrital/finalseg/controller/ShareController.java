package co.edu.udistrital.finalseg.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import co.edu.udistrital.finalseg.AppProperties;
import co.edu.udistrital.finalseg.crypto.CryptoUtils;
import co.edu.udistrital.finalseg.domain.FileAttribs;
import co.edu.udistrital.finalseg.domain.FolderPathWebDTO;
import co.edu.udistrital.finalseg.utils.AppUtils;


@RestController
@RequestMapping("/")
public class ShareController {


	@Autowired
	AppProperties appProperties;
	

	
	@CrossOrigin
	@RequestMapping(value="/isAlive", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public Boolean isAlive(){		
        Boolean respuesta= true;
        return respuesta;
    }
		
	@CrossOrigin
	@RequestMapping(value="/checkExist", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkExist(@RequestParam("filename") String filename){	
		String filePathString=appProperties.getRecieve_folder()+filename;
		File f = new File(filePathString);
		String respuesta= "File not transfered";
		if(f.exists() && !f.isDirectory()) { 
			respuesta= "File transfered";
		}
        return new ResponseEntity<>(respuesta, HttpStatus.ACCEPTED);
    }
	
	@CrossOrigin
	@RequestMapping(value="/listUploadFiles", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<FileAttribs> listUploadFiles(){	
		List<FileAttribs> listaArchivos=AppUtils.obtenerFileListAttribs(appProperties.getUpload_folder());		
        return listaArchivos;
    }
	@CrossOrigin
	@RequestMapping(value="/listRecievedFiles", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<FileAttribs> listRecievedFiles(){	
		List<FileAttribs> listaArchivos=AppUtils.obtenerFileListAttribs(appProperties.getRecieve_folder());		
        return listaArchivos;
    }
	
	public RestTemplate getRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}
	
	@Test
	public void test(){
		ShareController con=new ShareController();
		con.postFile("http://127.0.0.1:7891/recieve", "file.pdf","G://share//upload//");
	}
	
	private void postFile(String url, String file, String folder){
		String uploadFilesUrl=url;
		if(fileExist(folder+file)){
			LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			RestTemplate restTemplate = getRestTemplate();	
			Date now=new Date();
			String encfilename="upload"+now.getTime();
			try{
			
			String tempFileName=folder+file;					
			Path path = Paths.get(tempFileName);																				
			
			
			File encrypfile=new File(appProperties.getEncrypt_folder()+encfilename+"."+AppUtils.getFileExtension(file));
			File orgfilename=new File(tempFileName);
			
			CryptoUtils.encrypt(AppUtils.generateKeyHash(appProperties.getEncryp_key()), orgfilename, encrypfile);
			//
			map.add("file", new FileSystemResource(appProperties.getEncrypt_folder()+encfilename+"."+AppUtils.getFileExtension(file)));
			
			HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		    HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
//		    String response = restTemplate.postForObject(uploadFilesUrl, requestEntity, String.class);
		    		    
		    try {
	            getRestTemplate().exchange(uploadFilesUrl, HttpMethod.POST, new HttpEntity<MultiValueMap<String, Object>>(map, headers), String.class);
	        }catch(HttpClientErrorException e) {
	        	e.printStackTrace();
	        }
		    
//		    System.out.println(response);
			}
			catch(Exception err){
				err.printStackTrace();
			}
		}		
	}
	
	private boolean fileExist(String url){
		return new File(url).isFile();
	}

	
	
	
	 		
	@CrossOrigin
	@RequestMapping(value="/listRecievedPath", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public FolderPathWebDTO getRecievedFolder(){	
		String folder=appProperties.getRecieve_folder();
		return new FolderPathWebDTO(folder);
    }
	@CrossOrigin
	   @RequestMapping(value="/listUploadPath", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	    public FolderPathWebDTO getUpdateFolder(){	
			String folder=appProperties.getUpload_folder();
			return new FolderPathWebDTO(folder);
	    }
	
	@CrossOrigin
	@RequestMapping(value="/sendFile", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public String moveFile(
    		@RequestParam(value = "servidor", required = true) String servidor,
    		@RequestParam(value = "accion", required = false) String accion,
    		@RequestParam(value = "archivo", required = false) String archivo
    		){	
		postFile(servidor+"/recieve",archivo, appProperties.getUpload_folder());
		return "El archivo se movio correctamente";
    }
	
	@CrossOrigin
	@RequestMapping(value="/deleteFile", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public String deleteFile(
    		@RequestParam(value = "servidor", required = true) String servidor,
    		@RequestParam(value = "accion", required = false) String accion,
    		@RequestParam(value = "archivo", required = false) String archivo
    		){	
		
		File file = new File(appProperties.getUpload_folder()+archivo);
		if(file.delete()){
			System.out.println(file.getName() + " se ha borrado!");
			return file.getName() + " se ha borrado!";
		}else{
			System.out.println("La operacion de borrado ha fallado.");
			return "La operacion de borrado ha fallado.";
		}		
    }
	
	
	@CrossOrigin
	@PostMapping("/recieve") 
    public @ResponseBody String recibirArchivos( 
            @RequestParam("file") MultipartFile file){
			System.out.println("Entra al controlador");			
            String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
            	AppUtils.convertMultipart(file, appProperties.getEncrypt_folder());
            	
            	File encrypfile=new File(appProperties.getEncrypt_folder()+file.getOriginalFilename());
    			File orgfilename=new File(appProperties.getRecieve_folder()+file.getOriginalFilename());
    			
    			CryptoUtils.decrypt(AppUtils.generateKeyHash(appProperties.getEncryp_key()), encrypfile, orgfilename);
            	
            	System.out.println("You successfully uploaded " + name + " into " + name + "-uploaded !");
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
        	System.out.println("You failed to upload " + name + " because the file was empty.");
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
	
//	@CrossOrigin
//	@PostMapping("/upload") 
//	public String singleFileUpload(@RequestParam("file") MultipartFile file,
//	                                   RedirectAttributes redirectAttributes) {
//	        if (file.isEmpty()) {
//	            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
//	            return "redirect:uploadStatus";
//	        }
//	        try {
//	            // Get the file and save it somewhere
//	            byte[] bytes = file.getBytes();
//	            Path path = Paths.get(appProperties.getUpload_folder() + file.getOriginalFilename());
////	            Files.write(path, bytes);
//	            File uploadFile=AppUtils.convertMultipart(file, appProperties.getUpload_folder());
////	            CryptoUtils cUtil=new CryptoUtils();                       
////	            File newFile = new java.io.File(appProperties.encrypt_folder + file.getOriginalFilename());           
////	            try {
////					cUtil.encrypt(CryptoUtils.DEFAULTKEY, uploadFile, newFile);
////				} catch (Exception e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}            
//	            redirectAttributes.addFlashAttribute("message",
//	                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	        return "redirect:/uploadStatus";
//	    }
	
//	public class CryptoUtilsTest {
//		public static void main(String[] args) {
//			String key = "Mary has one cat1";
//			File inputFile = new File("document.txt");
//			File encryptedFile = new File("document.encrypted");
//			File decryptedFile = new File("document.decrypted");
//			
//			try {
//				CryptoUtils.encrypt(key, inputFile, encryptedFile);
//				CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
//			} catch (CryptoException ex) {
//				System.out.println(ex.getMessage());
//				ex.printStackTrace();
//			}
//		}
//	}
	
}
