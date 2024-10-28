import React, { Component } from 'react';
import withNavigateandLocation from './withNavigateandLocation';
import { Link } from "react-router-dom";
import AuthService from '../services/AuthService';

class ResetPassword extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            message: '',
        };
    }

    handleSubmit = async (e) => {
        e.preventDefault();
        const { email } = this.state;

        try {
            const res =await AuthService.requestPasswordReset(email);
            console.log("Sending reset password request for:", email);
            if(res){
                this.setState({message: 'Reset link sent'})
            }
            // Simulate API call success
            this.setState({ message: 'If an account with that email exists, a reset link will be sent.' });

            this.props.navigate('/login');

        } catch (error) {
            console.error('Reset password error:', error);
            this.setState({ message: 'Error sending reset link. Please try again.' });
        }
    };

    // Handle input change
    handleInputChange = (e) => {
        this.setState({ email: e.target.value });
    };

    render() {
        const { email, message } = this.state;

        return (
            <div className="flex justify-center items-center min-h-screen">
                <form className="p-8 rounded-lg border border-accent shadow-lg w-96 bg-secondary" onSubmit={this.handleSubmit}>
                    <h2 className="text-2xl font-bold text-center mb-6">Reset Password</h2>

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

                    <button
                        type="submit"
                        className="btn btn-primary w-full font-bold"
                    >
                        Send Reset Link
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

export default withNavigateandLocation(ResetPassword);
