package pack;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {	//계산 결과들을 csv파일로 추출하기 위함

	
	File file;
	FileWriter fileW;
	String filePath;
	String txtLine;
	
	public CsvWriter(String path) throws IOException {
		filePath=path;
		file=new File(filePath);
		fileW=new FileWriter(file);
		//fileW=new FileWriter(file,true); //이어쓰기 모드 true
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
