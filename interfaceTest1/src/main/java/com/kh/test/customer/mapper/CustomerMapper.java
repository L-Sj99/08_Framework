package com.kh.test.customer.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kh.test.customer.dto.Customer;

@Mapper
public interface CustomerMapper {

	/**
	 * @param customer
	 * @return result
	 */
	int insertCustomer(Customer customer);

}
