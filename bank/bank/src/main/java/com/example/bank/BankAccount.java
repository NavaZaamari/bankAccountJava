package com.example.bank;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name="BANK_ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
	@GeneratedValue
	@Id
	private Long id;

	@Column(name="HOLDER")
	private String accountHolder;

	@Column(name="PASSWORD")
	private String password;

	@Column(name="BALANCE")
	private double balance;

	@Column(name="DELETED")
	private boolean deleted = false;




	public BankAccount(String accountHolder, String password) {
		this.accountHolder = accountHolder;
		this.password = password;

	}

	public void deposit(double amount) {
		if (amount > 0) {
			balance += amount;
			System.out.println("Deposited: " + amount);

		}
		else {
			System.out.println("Invalid Amount");
		}
	}

	public void withdraw(double amount) {
		if (amount <= balance) {
			balance -= amount;
			System.out.println("Withdrawn: " + amount);
		}
		else {
			throw new InsufficientBalanceException("Not enough money!");
		}
	}

	public void displayBalance() {
		System.out.println("Current balance: " + balance);
	}
}