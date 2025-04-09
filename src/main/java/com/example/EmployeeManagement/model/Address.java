// package com.example.EmployeeManagement.model;

// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Table(name = "addresses")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor

// public class Address {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(nullable = false)
//     private String address;

//     @Column(name = "street")
// 	private String street;

// 	@Column(name = "city")
// 	private String city;

// 	@Column(name = "state")
// 	private String state;

// 	@Column(name = "country")
// 	private String country;

//     @ManyToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
//     @JoinColumn(name ="user_id" , referencedColumnName = "id")
//     private User user;
// }
