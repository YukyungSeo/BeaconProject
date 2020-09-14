package pack;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class csvWriter {	//계산 결과들을 csv파일로 추출하기 위함

	
	File file;
	FileWriter fileW;
	String filePath;
	String txtLine;
	
	public csvWriter(String path) throws IOException {
		filePath=path;
		file=new File(filePath);
		fileW=new FileWriter(file);
		txtLine="";
		
	}
	
	public void WriteLine(String ln) throws IOException {
		txtLine=ln;
		fileW.write(String.format("%s",txtLine));
		fileW.write(System.lineSeparator());
		
	}
	public void Close() throws IOException {
		fileW.close();
	}


}
