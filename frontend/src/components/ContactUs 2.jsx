import React, { Component } from 'react';
import withNavigateandLocation from './withNavigateandLocation';
import { Link } from "react-router-dom";
import EmailService from '../services/EmailService';  

class ContactUs extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            email: '',
            message: '',
            responseMessage: '',
        };
    }

    handleSubmit = async (e) => {
        e.preventDefault();
        const { name, email, message } = this.state;
        const contactData = { name, email, message };

        console.log(JSON.stringify(contactData));
        
        try {
            const res = await EmailService.sendContactUsEmail(contactData);
            console.log(res);
            if (res) {
                this.setState({ responseMessage: 'Your message has been sent.' });
            }
        } catch (error) {
            console.error('Contact form error:', error);
            this.setState({ responseMessage: 'Error sending your message. Please try again.' });
        }
    };

    // Handle input changes
    handleInputChange = (e) => {
        this.setState({ [e.target.name]: e.target.value });
    };

    render() {
        const { name, email, message, responseMessage } = this.state;

        return (
            <div className="flex justify-center items-center min-h-screen">
                <form className="p-8 rounded-lg border border-accent shadow-lg w-96 bg-secondary" onSubmit={this.handleSubmit}>
                    <h2 className="text-2xl font-bold text-center mb-6">Contact Us</h2>

                    {/* Name Input */}
                    <div className="mb-4">
                        <label className="block text-primary text-sm font-bold mb-2">Name</label>
                        <input
                            type="text"
                            name="name"
                            value={name}
                            onChange={this.handleInputChange}
                            required
                            className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    {/* Email Input */}
                    <div className="mb-4">
                        <label className="block text-primary text-sm font-bold mb-2">Email</label>
                        <input
                            type="email"
                            name="email"
                            value={email}
                            onChange={this.handleInputChange}
                            required
                            className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    {/* Message Input */}
                    <div className="mb-4">
                        <label className="block text-primary text-sm font-bold mb-2">Message</label>
                        <textarea
                            name="message"
                            value={message}
                            onChange={this.handleInputChange}
                            required
                            rows="4"
                            className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    {/* Submit Button */}
                    <button
                        type="submit"
                        className="btn btn-primary w-full font-bold"
                    >
                        Send Message
                    </button>

                    {/* Response Message */}
                    {responseMessage && <p className="text-center text-sm text-green-500 mt-4">{responseMessage}</p>}

                    <div className="flex justify-between mt-4">
                        <Link to="/home" className="text-blue-500">
                            Back to Home
                        </Link>
                    </div>
                </form>
            </div>
        );
    }
}

export default withNavigateandLocation(ContactUs);
