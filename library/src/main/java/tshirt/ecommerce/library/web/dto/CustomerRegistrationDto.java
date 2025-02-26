package tshirt.ecommerce.library.web.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationDto {
	@NotEmpty(message = "First name can't be empty!")
	private String firstName;

	@NotEmpty(message = "Last name can't be empty!")
	private String lastName;

	@NotEmpty(message = "Email can't be empty!")
	@Email(message = "*Please provide a valid Email")
	private String username;

	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	private String password;

	@NotEmpty(message = "*Please provide your phone")
	@Pattern(regexp = "^(\\+84|0)[3|5|7|8|9][0-9]{8}$", message = "Vui lòng nhập đúng định dạng số điện thoại")
	private String phone;

	@NotEmpty(message = "Confirm Password is mandatory")
	private String confirm;

	private String company;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String country;
	private String postalCode;
}
