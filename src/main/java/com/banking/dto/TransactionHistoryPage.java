package com.banking.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.banking.model.Transaction;

public class TransactionHistoryPage {

	private List<TransactionResponse> transactions;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
	
	public static TransactionHistoryPage fromPage(Page<Transaction> page) {
		TransactionHistoryPage dto = new TransactionHistoryPage();
		dto.transactions = page.getContent().stream().map(TransactionResponse :: fromEntity).toList();
		dto.pageNumber = page.getNumber();
		dto.pageSize = page.getSize();
		dto.totalElements = page.getTotalElements();
		dto.totalPages = page.getTotalPages();
		dto.last = page.isLast();
		return dto;
	}

	public List<TransactionResponse> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionResponse> transactions) {
		this.transactions = transactions;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}
	
	

}
