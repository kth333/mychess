import React, { Component } from 'react';
import { Link } from "react-router-dom";
import withNavigateandLocation from './withNavigateandLocation';
import AuthService from '../services/AuthService';

class PasswordReset extends Component {
    constructor(props) {
        super(props);
        this.state = {
            newPassword: '',
            confirmPassword: '',
            message: '',
            token: '',
        };
    }

    async componentDidMount() {
        await this.fetchToken();
    }

    fetchToken = () => {   
        const { token } = this.props.params;
        console.log("Token:", token);   
        this.setState({ token });
    };

    handleSubmit = async (e) => {
        e.preventDefault();
        const { newPassword, confirmPassword, token } = this.state;

        // Simple validation for password match
        if (newPassword !== confirmPassword) {
            this.setState({ message: 'Passwords do not match!' });
            return;
        }

        let newPasswordAndToken = {
            resetToken: token,
            newPassword: newPassword,
            
        };
        console.log(newPasswordAndToken);

        try {
           
            const res = await AuthService.resetPassword(newPasswordAndToken);

            console.log("Resetting password for:", newPassword);
            console.log("Using token:", token);
            
            // On success, show a success message
            this.setState({ message: 'Password successfully reset!' });

            // Navigate to login page after successful password reset
            this.props.navigate('/login');

        } catch (error) {
            console.error('Error resetting password:', error);
            this.setState({ message: 'Error resetting password. Please try again.' });
        }
    };

    // Handle input changes
    handleInputChange = (e) => {
        this.setState({ [e.target.name]: e.target.value });
    };

    render() {
        const { newPassword, confirmPassword, message } = this.state;
        console.log(this.state);

        return (
            <div className="flex justify-center items-center min-h-screen">
                <form className="p-8 rounded-lg border border-accent shadow-lg w-96 bg-secondary" onSubmit={this.handleSubmit}>
                    <h2 className="text-2xl font-bold text-center mb-6">Reset Your Password</h2>

                    <div className="mb-4">
                        <label className="block text-primary text-sm font-bold mb-2">New Password</label>
                        <input
                            type="password"
                            name="newPassword"
                            value={newPassword}
                            onChange={this.handleInputChange}
                            required
                            className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    <div className="mb-4">
                        <label className="block text-primary text-sm font-bold mb-2">Confirm Password</label>
                        <input
                            type="password"
                            name="confirmPassword"
                            value={confirmPassword}
                            onChange={this.handleInputChange}
                            required
                            className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    <button
                        type="submit"
                        className="btn btn-primary w-full font-bold"
                    >
                        Reset Password
                    </button>

                    {message && <p className="text-center text-sm text-green-500 mt-4">{message}</p>}

                    <div className="flex justify-between mt-4">
                        <Link to="/login" className="text-blue-500">
                            Back to Login
                        </Link>
                    </div>
                </form>
            </div>
        );
    }
}

export default withNavigateandLocation(PasswordReset);
