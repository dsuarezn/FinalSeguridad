package co.edu.udistrital.finalseg;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component

@ConfigurationProperties("app")
public class AppProperties {
    private String upload_folder;
    private String recieve_folder;
    private String encrypt_folder;
    
    private String encryp_key;

	public String getUpload_folder() {
		return upload_folder;
	}

	public void setUpload_folder(String upload_folder) {
		this.upload_folder = upload_folder;
	}

	public String getRecieve_folder() {
		return recieve_folder;
	}

	public void setRecieve_folder(String recieve_folder) {
		this.recieve_folder = recieve_folder;
	}

	public String getEncrypt_folder() {
		return encrypt_folder;
	}

	public void setEncrypt_folder(String encrypt_folder) {
		this.encrypt_folder = encrypt_folder;
	}

	public String getEncryp_key() {
		return encryp_key;
	}

	public void setEncryp_key(String encryp_key) {
		this.encryp_key = encryp_key;
	}
    
    
		
}
