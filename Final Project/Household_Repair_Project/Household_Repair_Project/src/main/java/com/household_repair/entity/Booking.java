package com.household_repair.entity;

import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
@Entity
@Table(name = "BookingDetails")
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BookingId")
	private Integer bookingId;
	
	@Column(name = "CustomerName")
	private String name;
	
	
	@Column(name = "PhoneNo")
	@Size(min = 10, max = 10)
	private Long phoneNo;
	
	@Column(name = "Address")
	private String address;
	
	@Column(name = "BookingDate")
	private Date  bookingDate;
	
	@Column(name = "BookingTime")
	private LocalTime bookingTime;
	
	@Column(name = "ServiceType")
	private String serviceType;
	
	
	public Booking() {
		// TODO Auto-generated constructor stub
	}


	public Booking(Integer bookingId, String name, Long phoneNo, String address, Date bookingDate,
			LocalTime bookingTime, String serviceType) {
		super();
		this.bookingId = bookingId;
		this.name = name;
		this.phoneNo = phoneNo;
		this.address = address;
		this.bookingDate = bookingDate;
		this.bookingTime = bookingTime;
		this.serviceType = serviceType;
	}


	public Integer getBookingId() {
		return bookingId;
	}


	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getPhoneNo() {
		return phoneNo;
	}


	public void setPhoneNo(Long phoneNo) {
		this.phoneNo = phoneNo;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public Date getBookingDate() {
		return bookingDate;
	}


	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}


	public LocalTime getBookingTime() {
		return bookingTime;
	}


	public void setBookingTime(LocalTime bookingTime) {
		this.bookingTime = bookingTime;
	}


	public String getServiceType() {
		return serviceType;
	}


	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}


	@Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", name=" + name + ", phoneNo=" + phoneNo + ", address=" + address
				+ ", bookingDate=" + bookingDate + ", bookingTime=" + bookingTime + ", serviceType=" + serviceType
				+ "]";
	}
	
	

}
