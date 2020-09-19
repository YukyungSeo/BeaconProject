package pack;

import java.sql.Time;

public class IDPAIR {	// 어플리케이션 id들의 짝 (bench, receiver=myid, sender=yourid)
	String bench;
	String myid;
	String yourid;
	
	int realDistance;
	
	Time startTime;
	Time finishTime;
	
	String dateStr;
	
	
	public void setTime(Time start, Time finish) {
		this.startTime=start;
		this.finishTime=finish;
	}
	public void setDistance(int dist) {
		this.realDistance=dist;
	}
	
	public IDPAIR(String bench, String my, String your) {
		this.myid=my;
		this.yourid=your;
		
		//test:
		this.bench= bench;
		this.realDistance=44;
		this.startTime=Time.valueOf("01:01:01");
		this.finishTime=Time.valueOf("09:09:09");
		dateStr="";
		
	}
	public boolean isSame(String b, String m, String y, Time start_t) {
		
		String timetxt=start_t.toString();
		if(this.myid.equals(m)&&this.yourid.equals(y)&this.bench.equals(b)&&this.startTime.equals(Time.valueOf(timetxt))) {
			return true;
		}
		else{return false;
		}
		
	}
	public void setDate(String date) {
		// TODO Auto-generated method stub
		dateStr=date;
		
	}

}
