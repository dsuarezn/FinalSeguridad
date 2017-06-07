package co.edu.udistrital.finalseg.domain;

public class FileAttribs {

	private String filename;
	private String filesize;
	private String filetype;
	
	
	
	public FileAttribs(String filename, String filesize, String filetype) {
		super();
		this.filename = filename;
		this.filesize = filesize;
		this.filetype = filetype;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	
	
	
}
