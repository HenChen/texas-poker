import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 单例模式策略
 * 
 * @decription 该类提供不同的决策表，单例模式，每种策略都存在一个副本
 * @author Haibin Chen
 * @create 2015-5-19
 * @update 2015-5-19
 */
public class Strategy {
    public enum StrategyName{
	CON_LEVEL_1,
	CON_LEVEL_2,
	NORMAL,
	RADICAL_LEVEL_1,
	RADICAL_LEVEL_2;
    }
    private static String CON_LEVEL_1_D = "0.00 0.00 0.00 0.00 1.00\n"
	    + "0.05 0.00 0.00 0.00 0.95\n" + "0.10 0.00 0.00 0.00 0.90\n"
	    + "0.15 0.00 0.00 0.00 0.85\n" + "0.20 0.00 0.00 0.00 0.80\n"
	    + "0.25 0.00 0.00 0.00 0.75\n" + "0.30 0.00 0.00 0.00 0.70\n"
	    + "0.35 0.00 0.00 0.00 0.65\n" + "0.40 0.00 0.00 0.00 0.60\n"
	    + "0.45 0.00 0.00 0.00 0.55\n" + "0.50 0.00 0.00 0.00 0.50\n"
	    + "0.60 0.00 0.00 0.00 0.40\n" + "0.70 0.00 0.00 0.00 0.30\n"
	    + "0.70 0.00 0.10 0.00 0.20\n" + "0.80 0.00 0.10 0.00 0.10\n"
	    + "0.75 0.00 0.25 0.00 0.00\n" + "0.60 0.00 0.40 0.00 0.00\n"
	    + "0.50 0.00 0.50 0.00 0.00\n" + "0.40 0.00 0.40 0.20 0.00\n"
	    + "0.30 0.00 0.40 0.30 0.00\n";
	    
    private static String CON_LEVEL_2_D = "0.00 0.00 0.00 0.00 1.00\n"
	    + "0.10 0.00 0.00 0.00 0.90\n" + "0.15 0.00 0.00 0.00 0.85\n"
	    + "0.20 0.00 0.00 0.00 0.80\n" + "0.25 0.00 0.00 0.00 0.75\n"
	    + "0.30 0.00 0.00 0.00 0.70\n" + "0.35 0.00 0.00 0.00 0.65\n"
	    + "0.40 0.00 0.00 0.00 0.60\n" + "0.45 0.00 0.00 0.00 0.55\n"
	    + "0.50 0.00 0.00 0.00 0.50\n" + "0.60 0.00 0.00 0.00 0.40\n"
	    + "0.65 0.00 0.00 0.00 0.35\n" + "0.70 0.00 0.00 0.00 0.30\n"
	    + "0.70 0.00 0.10 0.00 0.20\n" + "0.80 0.00 0.10 0.00 0.10\n"
	    + "0.70 0.00 0.25 0.00 0.05\n" + "0.60 0.00 0.40 0.00 0.00\n"
	    + "0.50 0.00 0.30 0.20 0.00\n" + "0.40 0.00 0.30 0.30 0.00\n"
	    + "0.30 0.00 0.30 0.40 0.00";
    
    private static String NORMAL_D= "0.0 0.0 0.0 0.0 1.0\n"
	    + "0.0 0.0 0.0 0.0 1.0\n" + "0.1 0.0 0.0 0.0 0.9\n"
	    + "0.2 0.0 0.0 0.0 0.8\n" + "0.3 0.0 0.0 0.0 0.7\n"
	    + "0.4 0.0 0.0 0.0 0.6\n" + "0.5 0.0 0.0 0.0 0.5\n"
	    + "0.6 0.0 0.0 0.0 0.4\n" + "0.6 0.0 0.1 0.0 0.3\n"
	    + "0.6 0.0 0.2 0.0 0.2\n" + "0.5 0.0 0.3 0.0 0.2\n"
	    + "0.5 0.0 0.4 0.0 0.1\n" + "0.4 0.0 0.5 0.0 0.1\n"
	    + "0.3 0.0 0.6 0.0 0.1\n" + "0.3 0.0 0.5 0.1 0.1\n"
	    + "0.3 0.0 0.4 0.2 0.1\n" + "0.4 0.0 0.4 0.2 0.0\n"
	    + "0.3 0.0 0.5 0.2 0.0\n" + "0.0 0.0 0.3 0.7 0.0\n"
	    + "0.0 0.0 0.1 0.9 0.0";
    
    private static final String RADICAL_LEVEL_1_D = "0.0 0.0 0.0 0.0 1.0\n"
	    + "0.0 0.0 0.0 0.0 1.0\n" + "0.1 0.0 0.0 0.0 0.9\n"
	    + "0.2 0.0 0.0 0.0 0.8\n" + "0.3 0.0 0.0 0.0 0.7\n"
	    + "0.4 0.0 0.0 0.0 0.6\n" + "0.4 0.0 0.1 0.0 0.5\n"
	    + "0.4 0.0 0.2 0.0 0.4\n" + "0.4 0.0 0.3 0.0 0.3\n"
	    + "0.4 0.0 0.4 0.0 0.2\n" + "0.3 0.0 0.5 0.0 0.2\n"
	    + "0.4 0.0 0.5 0.0 0.1\n" + "0.4 0.0 0.4 0.1 0.1\n"
	    + "0.4 0.0 0.3 0.2 0.1\n" + "0.3 0.0 0.3 0.3 0.1\n"
	    + "0.3 0.0 0.3 0.4 0.0\n" + "0.3 0.0 0.3 0.4 0.0\n"
	    + "0.2 0.0 0.4 0.4 0.0\n" + "0.1 0.0 0.5 0.4 0.0\n"
	    + "0.0 0.0 0.6 0.4 0.0";
   
    private static String RADICAL_LEVEL_2_D = "0.00 0.00 0.00 0.00 1.00\n"
	    + "0.20 0.00 0.00 0.00 0.80\n" + "0.30 0.00 0.00 0.00 0.70\n"
	    + "0.60 0.00 0.00 0.00 0.40\n" + "0.90 0.00 0.00 0.00 0.10\n"
	    + "0.80 0.00 0.20 0.00 0.00\n" + "0.70 0.00 0.30 0.00 0.00\n"
	    + "0.60 0.00 0.40 0.00 0.00\n" + "0.50 0.00 0.50 0.00 0.00\n"
	    + "0.40 0.00 0.60 0.00 0.00\n" + "0.30 0.00 0.70 0.00 0.00\n"
	    + "0.20 0.00 0.70 0.10 0.00\n" + "0.10 0.00 0.70 0.20 0.00\n"
	    + "0.10 0.00 0.60 0.30 0.00\n" + "0.10 0.00 0.50 0.40 0.00\n"
	    + "0.20 0.00 0.30 0.50 0.00\n" + "0.20 0.00 0.30 0.50 0.00\n"
	    + "0.10 0.00 0.40 0.50 0.00\n" + "0.10 0.00 0.40 0.50 0.00\n"
	    + "0.05 0.00 0.45 0.50 0.00";

    private static HashMap<StrategyName, Strategy> strategySet = new HashMap<StrategyName, Strategy>();
    
  
    
    /**
     * @function 单例模式实例化指定策略名的策略
     * @param: 策略名
     * @return:Strategy
     * @create:2015-5-19
     * @update:
     */
    public static Strategy instanceOf(StrategyName strategyName){
	if (!strategySet.containsKey(strategyName)) {
               strategySet.put(strategyName, new Strategy(strategyName));
	}
	return strategySet.get(strategyName);
    }

    /**
     * @function 测试
     * @param: Strategy
     * @return:void
     * @create:2015-5-19
     * @update:
     *
    public static void main(String args[]) {
	
	    Strategy s = Strategy.instanceOf(StrategyName.CON_LEVEL_1);
	 
    }   
     */
     
    /*
     * 决策表
     */
    private List<ArrayList<Double>> decisionTable;
    
    /**
     * construct decision table with specified strategy file
     * @param strategyName
     * @throws FileNotFoundException
     */
    private Strategy(StrategyName strategyName){
        String strategyData = null;
        switch(strategyName){
        case CON_LEVEL_1:
              strategyData = CON_LEVEL_1_D;
              break;
        case CON_LEVEL_2:
              strategyData = CON_LEVEL_2_D;
              break;
        case NORMAL:
              strategyData = NORMAL_D;
              break;
        case RADICAL_LEVEL_1:
              strategyData = RADICAL_LEVEL_1_D;
              break;
        case RADICAL_LEVEL_2:
              strategyData = RADICAL_LEVEL_2_D;
              break;
        }
        InputStream is = new ByteArrayInputStream(strategyData.getBytes());
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
	decisionTable = new ArrayList<ArrayList<Double>>();
	try {
	    String deStr = null;
	    while ((deStr = br.readLine()) != null) {
		ArrayList<Double> de = new ArrayList<Double>();
		String[] doubleStr = deStr.split(" ");
		for (int i = 0; i < doubleStr.length; i++) {
		    de.add(Double.valueOf(doubleStr[i].trim()));
		}
		decisionTable.add(de);
	    }
	    
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		br.close();
		is.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    @SuppressWarnings("unchecked")
    public ArrayList<Double> getDecisionbyStrengh(double strength) {
	int index = (int) Math.floor(strength*20);
	index = index>=20? 19:index;
        System.out.println("index: "+index + "---decision:"+this.decisionTable.get(index).toString());
	return (ArrayList<Double>)this.decisionTable.get(index).clone();
    }
 
}
