package com.example.bank;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name="BANK_ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount implements UserDetails {
	@GeneratedValue
	@Id
	private Long id;

	@Column(name="HOLDER", unique=true)
	private String accountHolder;

	@Column(name="PASSWORD")
	private String password;

	@Column(name="BALANCE")
	private double balance;

	@Column(name="CARD_NUMBER")
	private String cardNumber;

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


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getUsername() {
		return "";
	}
}