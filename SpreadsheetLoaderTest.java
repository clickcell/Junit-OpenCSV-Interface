package nondigital;

import java.util.Collection;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import spreadsheettools.csvTools;

@RunWith(Parameterized.class)
public class SpreadsheetLoaderTest {

	private static csvTools csvTools = new csvTools();
	@Parameter public Map<String, String> testDataRow;
	@Rule public TestName currentlyRunningTest = new TestName();

	@Parameterized.Parameters
	public static Collection<Map<String,String>> testData() throws Exception {
		return csvTools.getTests("C:\\Automation\\Data\\testspreadsheet.csv");
	}
	
	@Test
	public void reading() {
		String data = String.format("%s %s %s %s", 
				testDataRow.get("columna"),
				testDataRow.get("columnb"),
				testDataRow.get("columnc"),
				testDataRow.get("columnd"));
		System.out.println(data);
	}
	
	@Test
	public void writing() throws Exception {
		
		int newDataA = Integer.parseInt(testDataRow.get("columna")) + 1;
		int newDataB = Integer.parseInt(testDataRow.get("columnb")) + 1;
		int newDataC = Integer.parseInt(testDataRow.get("columnc")) + 1;
		int newDataD = Integer.parseInt(testDataRow.get("columnd")) + 1;

		csvTools.saveData(currentlyRunningTest, "columna", String.valueOf(newDataA));
		csvTools.saveData(currentlyRunningTest, "columnb", String.valueOf(newDataB));
		csvTools.saveData(currentlyRunningTest, "columnc", String.valueOf(newDataC));
		csvTools.saveData(currentlyRunningTest, "columnd", String.valueOf(newDataD));
	
	}
	
}
