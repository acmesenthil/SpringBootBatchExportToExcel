package com.tech.krival.SpringBootBatchReadWrite.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class MyItemWriter implements  ItemWriter<String> {

	@Override
	public void write(List<? extends String> items) throws Exception {
		// TODO Auto-generated method stub
        System.out.println("ItemWriter start..");

	}

}
