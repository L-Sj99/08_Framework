package edu.kh.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Todo {
	private int listNo;
	private String listTitle;
	private String listCheck;
	private int listComplete;
	private String enrollDate;
}
