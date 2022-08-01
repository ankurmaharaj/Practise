package utility;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import com.opencsv.*;


// Healper class to Read data from CSV files.

public class ReadData {
public Path filePath;
Reader reader = null;
CSVReader csvReader = null;
CSVParser parser = null;
List<String[]> readData = new ArrayList<String[]>();

/**
+ Reades the give file with delimiter.
* @param filePath
* @param delimiter
*/

//constructor
public ReadData(Path filePath, char delimiter) {
this.filePath = filePath;
try {
reader = Files.newBufferedReader(filePath);
} catch (IOException e) {
e.printStackTrace();
}

//List<String[]> allData = new ArrayList<String[]>();

try {
parser = new CSVParserBuilder().withSeparator(delimiter).build();
csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();

readData = csvReader.readAll();


//for (String[] row : alldata) {
//for (String cell : row) {
//System.out.print(cell + "\t");
// }
//System.out.println();

}
 catch (Exception e) {
e.printStackTrace();
} finally {
try {
reader.close();
} catch (IOException e) {
e.printStackTrace();
}
}
}

//public static void main(String[] args) throws IOException
//final String home = System.getProperty("user.dir");
//Path filePat = Paths.get(home, "src", "test", "resources", "support", "h
//ReadData rd = new ReadData(filepat);
//List<String> rowswithLegacy = rd.getAllValues FromColumn("Legacy ID");

//for(String rowwith:rowswithLegacy) {

//System.out.println(rowwith+"\t");

// }
// }


/**
* Return the complete Row values.
* @param rowNum
@return
+ @throws IOException
*/


public List<String> getValuesFromSpecificRow(int rowNum)
{
List<String> lineValues = new LinkedList<String>();
for (String cell : readData.get(rowNum)) {
lineValues.add(cell);
}
return lineValues;
}


/**
* Returns the matching 2nd keyword (column) & values for maching 1st column name  & value
@param Keyword
@param value
@param seckeyword
* @param secValue
* @return
* @throws IOException
*/

public List<HashMap<String, String>> getValuesFromSpecificRow(String Keyword,String Value,String secKeyword,String secValue) throws IOException
{
List<HashMap<String, String>> linesWithCriteria = new LinkedList<>();
List <Integer> primRows = new LinkedList<Integer>();
try {
	primRows = getRowNums (Keyword, Value);
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
int colSeckey = getColNum(secKeyword);

for (int row: primRows) {
if (getValuesFromSpecificRow(row).get(colSeckey).equals(secValue)) {
HashMap<String, String> hashMap = new HashMap<>();
Iterator<String> iterator = readHeader().iterator();
for (String eachvalueinRow:getValuesFromSpecificRow(row)) {
hashMap.put(iterator.next(), eachvalueinRow);
//lineswithCriteria.add(getvaluesFromSpecificRow(row));
}
linesWithCriteria.add(hashMap);
}
}
return linesWithCriteria;
}

/**
Returns all values from eaven Column
* @param caseName
@return
* @throws IOException
*/

public List<String> getAllValuesFromColumn(String caseName) throws IOException {
List<String> allValues =new LinkedList<String>();
int columnNum = getColNum( caseName);
int numCols = readData.size() -1;
for (int i = 1; i <= numCols; i++) {
allValues.add(getValueFromCell(i, columnNum));
}
return allValues;
}
// Retruns value from a given cell co-ordiantes (row, col).

// @param row
//* @param col
//* @return
//* @throws IOException

public String getValueFromCell(int row, int col) throws IOException {
String cellValue = getValuesFromSpecificRow(row).get(col);
//System.out.println(cellvalue);
return cellValue;
}
/**
* Returns the list of header column names.
* @return
+ @throws IOException
*/
public List<String> readHeader() throws IOException {
List<String> header = new LinkedList<String>();
header = getValuesFromSpecificRow(0);
return header;
}

/**
* Returns the column number of given column name
* @param keyword
* @return
* @throws IOException
*/
public int getColNum(String keyword) throws IOException {
int colNum = 0;
if (readHeader().contains(keyword)) {
colNum = readHeader().indexOf(keyword);
} else if (readHeader().contains(keyword.toLowerCase())) {
colNum = readHeader().indexOf(keyword.toLowerCase());
} else if (readHeader().contains(keyword. toUpperCase())) {
colNum = readHeader().indexOf(keyword. toUpperCase());
}
return colNum;
}
//@param keyword
//@param caseName
//@return
//@throws IOException

public List<Integer> getRowNums (String keyWord, String caseName) throws IOException {
List<Integer> rowNums = new LinkedList <Integer>();
int colNumkeyword = getColNum(keyWord);
//int colNum_colName = getColNum(colName);
List<String[]> lines = readData;
int row = 0;
for (String[] line : lines) {
if (line [colNumkeyword].equals(caseName)) {
rowNums.add(row);
}
row++;
}
return rowNums;
}
/**
* Keyword = Column Header name of the first condition; caseName = Value of the keyword
* @param Keyword
* @param caseName
* @return

throws IOException
*/
public List<String> getData(String Keyword, String caseName) throws IOException{
List<String> Data = new LinkedList<String>();
int colNum=getColNum(caseName);
for (int rowNum: getRowNums (Keyword, caseName)) {
Data.add(getValueFromCell(rowNum, colNum));
}
return Data;
}
/* Returns the size of List
* @return */
public int getSize() {
return readData.size();
}
/*Returns the 2nd keyword (column) values for machine 1st column name & value.
* @param Keyword
* @param KeyValue
* @param colName
Sreturn
@throws IOException */


public List<String> getValuesWithKeyword(String Keyword, String KeyValue, String colName) throws IOException {
List<Integer> rows = new ArrayList<Integer>();
List<String> values = new ArrayList<String>();
rows.addAll(getRowNums (Keyword, KeyValue));
int col = getColNum(colName);
for (int row : rows) {
 values.add(getValue (row, col));
}

return values;
}
// Returns the 2nd keyword (column) value for maching ist column name & value.
/*@param Keyword
@param KeyValue
@param colName
@return
@throws IOException
*/
public String getValueWithkeyword(String Keyword, String KeyValue, String colName) throws IOException {
return getValuesWithKeyword(Keyword, KeyValue, colName).get(0);
}
/**
@param row
@param col
@return
@throws IOException
*/
public String getValue(int row, int col) throws IOException {
String value = readData.get(row)[col];
return value;
}
/**
* @return
@throws IOException
*/
public List<String[]> ReadcSVFile() throws IOException {
Reader reader = Files.newBufferedReader(filePath);
CSVReader csReader = null;

List<String[]> allData = new ArrayList<String[]>();
try {
CSVParser parser = new CSVParserBuilder().withSeparator('|').build();
csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();
allData = csReader.readAll();

//for (String[] row: allData) {
//for (String cell: row) {
//System.out.print(cell + "\t");
//System.out.printin();
} catch (Exception e) {
e.printStackTrace();
} finally {
reader.close();
}
readData = allData;
return allData;
}
/**
* @throws IOException
*/
public void WriteCSVFile() throws IOException {
	
}

/*
* Delete give row from the data.
* @param rowNum
*/
public void deleteRow(int rowNum) {
readData.remove(rowNum);
}
}
