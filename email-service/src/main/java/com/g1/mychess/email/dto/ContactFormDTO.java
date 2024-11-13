package com.g1.mychess.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for capturing contact form information.
 * This class is used to encapsulate the details submitted via the contact form, such as the user's name, email, and message.
 * It also includes validation annotations to ensure that the data meets the required constraints.
 */
public class ContactFormDTO {

    /**
     * The name of the person submitting the contact form.
     * This field is required and cannot exceed 50 characters.
     */
    @NotBlank(message = "Name is required.")
    @Size(max = 50, message = "Name must not exceed 50 characters.")
    private String name;

    /**
     * The email address of the person submitting the contact form.
     * This field is required and must be a valid email address.
     */
    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    /**
     * The message that the person is submitting through the contact form.
     * This field is required and cannot exceed 500 characters.
     */
    @NotBlank(message = "Message is required.")
    @Size(max = 500, message = "Message must not exceed 500 characters.")
    private String message;

    /**
     * Gets the name of the person submitting the contact form.
     *
     * @return The name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person submitting the contact form.
     *
     * @param name The name of the person.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the person submitting the contact form.
     *
     * @return The email address of the person.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the person submitting the contact form.
     *
     * @param email The email address of the person.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the message that the person is submitting through the contact form.
     *
     * @return The message from the person.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message that the person is submitting through the contact form.
     *
     * @param message The message from the person.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
