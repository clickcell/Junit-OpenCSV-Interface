package spreadsheettools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.rules.TestName;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class csvTools {
	private static String[] columnHeaders;
	private static CSVReader reader;
	private static List<Map<String,String>> tests = new ArrayList<Map<String, String>>();
	private String filePath;
	
	private void openfile(String filePath) throws Exception{
		this.filePath=filePath;
		File f = new File(filePath);
		Assert.assertTrue(String.format("Test Data file does not exist or is empty: %s",filePath), f.exists() && f.isFile() && f.length() > 0);
		reader = new CSVReader(new FileReader(f));
		getHeaders();
	}
	
	private static void getHeaders() throws IOException{
		columnHeaders = reader.readNext();  
	}
	
	public List<Map<String,String>> getTests(String filePath) throws Exception{
		tests.clear();
		openfile(filePath);
		String [] rowData;
		int row = 1; // the first data row in Excel is Row 2 
		while ((rowData = reader.readNext()) != null) {
			String errorMessage = String.format("ERROR: INCOMPLETE TEST DATA: row %d is missing a field", row++);
			Assert.assertEquals(errorMessage, columnHeaders.length, rowData.length);
			Map<String,String> vals = new HashMap<String,String>();
			for(int i = 0; i < columnHeaders.length; i++)
			{
				vals.put(columnHeaders[i], rowData[i]);
			}
			tests.add(vals);
		}
		reader.close();
		return tests;
	}

	public void saveData(TestName runningTest, String columnName, String newData) throws Exception{
		//ROW TO UPDATE IS DETERMINED BY INDEX OF runningTest
		//WHICH IS CONTAINED in its method name
		String index = runningTest.getMethodName().substring(runningTest.getMethodName().indexOf("[")+1, runningTest.getMethodName().indexOf("]"));
		int testIndex = Integer.parseInt(index);

		// change the appropriate values in the model
		Map<String,String> update = new HashMap<String,String>();
		update = tests.get(testIndex);
		update.put(columnName, newData);
		tests.set(testIndex, update);
		
		// create a temporary output file
		FileWriter fileWriter = new FileWriter(this.filePath + ".new", false);
		CSVWriter writer = new CSVWriter(fileWriter);
		writer.writeNext(columnHeaders);
		
		//write each row to a file without changing the values
		int row = 0;
		while (row < tests.size() ){
			List<String> rowData = new ArrayList<String>();
			
			int column= 0;
			while (column < columnHeaders.length){
				rowData.add(tests.get(row).get(columnHeaders[column]));
				column++;
			}

			// write to file
			writer.writeNext(rowData.toArray(new String[columnHeaders.length]));
			row++;
		}
		
		// replace old version of data file with temporary file
		writer.close();
		Files.move(Paths.get(this.filePath + ".new"), Paths.get(this.filePath), REPLACE_EXISTING);
		
	}
	

}

