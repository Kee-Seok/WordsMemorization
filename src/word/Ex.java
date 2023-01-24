package word;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Ex{

	static Workbook workbook;
	static WritableWorkbook write;
	static File file = new File("./temp/excel.xls");
	static ArrayList<String> words = new ArrayList<>();
	public static void save(DefaultListModel<String> wordList, DefaultListModel<String> meaningList, DefaultListModel<String> deleteWord, DefaultListModel<String> deleteMeaning) {
		if(!file.isFile()) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		try {
			WritableWorkbook wb = Workbook.createWorkbook(file);
			WritableSheet ws = wb.createSheet("단어장",0);
			WritableSheet ws2 = wb.createSheet("삭제리스트",1);
			for(int r = 0; r < wordList.size(); r++) {
			try {
				ws.addCell(new Label(0,r,wordList.get(r)));
			} catch (WriteException e) {
				e.printStackTrace();
			}
			}
			for(int r = 0; r < meaningList.size(); r++) {
			try {
				ws.addCell(new Label(1,r,meaningList.get(r)));
			} catch (WriteException e) {
				e.printStackTrace();
			}
			}
			for(int r = 0; r < deleteWord.size(); r++) {
			try {
				ws2.addCell(new Label(0,r,deleteWord.get(r)));
			} catch (WriteException e) {
				e.printStackTrace();
			}
			}
			for(int r = 0; r < deleteMeaning.size(); r++) {
			try {
				ws2.addCell(new Label(1,r,deleteMeaning.get(r)));
			} catch (WriteException e) {
				e.printStackTrace();
			}
			}
			wb.write();
			setWordList(ws);
			try {
				wb.close();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 엑셀에 세팅된 단어와 뜻 목록을 ArrayList에 갱신하여 저장하는 메서드. 
	 * @param ws (Sheet 타입 변수)
	 */
	
	public static void setWordList(Sheet s) { 
		words.removeAll(words);//배열을 비워준 후에 다시 넣기.
		for(int r = 0; r < s.getRows(); r++) {
			for(int c = 0; c < s.getColumns(); c++) {
				words.add(s.getCell(c,r).getContents());
			}
		}
		
	}
	
	/**
	 * 엑셀에서 단어 데이터 가져와서 리스트에 담기
	 * @param row
	 */
	public static void getWordsFromExcel(DefaultListModel<String> word, DefaultListModel<String> meaning,DefaultListModel<String> deleteWord, DefaultListModel<String> deleteMeaning) {
		try {
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);
			Sheet sheet2 = workbook.getSheet(1);
			int row = sheet.getRows();
			if(row>0) {//엑셀에 값이 있을때만 실행
			for(int r = 0; r < sheet.getRows(); r++) {
			word.addElement(sheet.getCell(0,r).getContents().toString());
			meaning.addElement(sheet.getCell(1,r).getContents().toString());
			}
			}
			if(deleteWord!=null&&deleteMeaning!=null){
				for(int r = 0; r < sheet2.getRows(); r++) {
					deleteWord.addElement(sheet2.getCell(0,r).getContents().toString());
					deleteMeaning.addElement(sheet2.getCell(1,r).getContents().toString());
					}
			}
			setWordList(sheet);
		} catch (BiffException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
