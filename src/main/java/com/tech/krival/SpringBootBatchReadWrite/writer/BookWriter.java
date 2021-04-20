package com.tech.krival.SpringBootBatchReadWrite.writer;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.batch.item.ItemWriter;


import static java.util.Arrays.asList;

import com.tech.krival.SpringBootBatchReadWrite.model.User;


public class BookWriter implements ItemWriter<User> {
	
	private final Sheet sheet;
	
	 

    public BookWriter(Sheet sheet) {
        this.sheet = sheet;
    }
    
   

    @Override
    public void write(List<? extends User> list) {
        for (int i = 0; i < list.size(); i++) {
            writeRow(i, list.get(i));
        }
    }

    private void writeRow(int currentRowNumber, User user) {
        List<String> columns = prepareColumns(user);
        Row row = this.sheet.createRow(currentRowNumber);
        for (int i = 0; i < columns.size(); i++) {
            writeCell(row, i, columns.get(i));
        }
    }

    private List<String> prepareColumns(User user) {
        return asList(
        		user.getId().toString(),
        		user.getName()
        	
        );
    }

    private void writeCell(Row row, int currentColumnNumber, String value) {
        Cell cell = row.createCell(currentColumnNumber);
        cell.setCellValue(value);
    }

}
