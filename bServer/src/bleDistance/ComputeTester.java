package bleDistance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
public class ComputeTester {
	static String TESTMODE_BENCHSET="benchsettest";
	static String TESTMODE_TXT="txttest";
	
	public ComputeTester() {
		this.mSpline=null;
	}
	MakeSpline mSpline;
	public MakeSpline getMSpline() {
		return this.mSpline;
	}
	public void startTest(String modeName/*�׽�Ʈ ���*/, String filePath/*�׽�Ʈ ��� ������ csv ���� ���*/) throws IOException {
		System.out.println("hellow world");
		String TestMode=modeName;
		HashMap<IDPAIR,SIGINF> resultMap = new HashMap<IDPAIR, SIGINF>();
		if(TestMode.equals(TESTMODE_BENCHSET)) {	// bench table�� ����ϴ� �׽�Ʈ ���
			
			DataRemoteConnector_BleBenchTestSet dataConn= new DataRemoteConnector_BleBenchTestSet();
			
			//*********************************************************
			//---------------------------------------------------------
			//Spline curve Test :
			
			mSpline= new MakeSpline();
			
			//---------------------------------------------------------
			//*********************************************************
			
			try {
				dataConn.connectANDquery(resultMap);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(resultMap!=null) {
				CsvWriter tw= new CsvWriter(filePath);
				tw.WriteLine("Date,sTime,Bench,myid,yourid,realDist,N,aveFRSSI,txPower,signalSize");
				for(int y=0; y<dataConn.getPairSize(); y++) {
					System.out.println("Log - id pair size : "+dataConn.getPairSize());
					IDPAIR temp_idpair=dataConn.getPairs()[y];
					SIGINF temp_siginf = resultMap.get(dataConn.getPairs()[y]);
					String thisstime=temp_idpair.startTime.toString();
					ComputingMethods.valueFilteredRssi(temp_siginf);
					ComputingMethods.computeAverageFRSSI(temp_siginf);
					ComputingMethods.computeN(temp_siginf, temp_idpair.realDistance);
					ComputingMethods.valueFilter(temp_siginf, temp_siginf.findN);
					double cut_n=ComputingMethods.cutDecimalPoint(temp_siginf.findN);
					double cut_rssi=ComputingMethods.cutDecimalPoint(temp_siginf.averageFrssi);
					tw.WriteLine(temp_idpair.dateStr+","+thisstime+","+temp_idpair.bench+","+temp_idpair.myid+","+temp_idpair.yourid+","+temp_idpair.realDistance+","+cut_n+","+cut_rssi+","+temp_siginf.txP+","+temp_siginf.fSize);
		
					//*********************************************************
					//---------------------------------------------------------
					//Spline curve Test :
					if(temp_idpair.realDistance!=1) {
					mSpline.checkIDandInsertSIG(temp_idpair.myid, temp_idpair.yourid, temp_idpair.realDistance, cut_rssi, temp_siginf.fSize);
					}
					//---------------------------------------------------------
					//*********************************************************
				}
				//*********************************************************
				//---------------------------------------------------------
				//Spline curve Test :
				System.out.println("Log - spline : "+mSpline.size);
				mSpline.makeSpline();
				for(int k=0; k<mSpline.size; k++) {
					System.out.println("================="+k+"==================");
					System.out.println("spline id pair : "+mSpline.myids[k]+" "+mSpline.yourids[k]+" input X : "+mSpline.splines[k].distMap.size());
					// X = rssi, Y = distance(m);
					System.out.println("spline output (t=0.5)=" + mSpline.insertMiddleT(k));
				}
				//---------------------------------------------------------
				//*********************************************************
				tw.Close();
			}
			dataConn=null;
			System.out.println("Log - bench test finish.");
		}else if(TestMode.equals(TESTMODE_TXT)) {	//�ؽ�Ʈ ���Ͽ��� ��ȣ�� �о �׽�Ʈ�ϴ� ���
			System.out.println("hellow world");
			SIGINF temp= new SIGINF(); //��ȣ ���� ��ü
			this.ReadFile(temp);//�ؽ�Ʈ ���� �о�ͼ� ��ȣ ��ü�� ����
			ComputingMethods.valueFilter(temp,2);		//����� ��ȣ ���͸�
			System.out.println("DISTANCE - "+temp.getDistance());	//���� ����� ����� distance�� ���
		}
	}
	public void ReadFile(SIGINF sig) throws IOException {
		// ***** �ؽ�Ʈ ���Ͽ��� ��ȣ�� �о ��ü�� ������
		
		String location = "./data.txt";
		File file = new File(location);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while((line = br.readLine()) != null){
			System.out.println(line);
			sig.stackRSSI(Integer.parseInt(line));	//rssi���� �ϳ��� �׾Ƽ� ������
		}
		br.close();
}
}
