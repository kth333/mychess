import React, { Component } from 'react';
import withNavigateandLocation from './withNavigateandLocation';
import { Mail } from 'lucide-react';
import AuthService from '../services/AuthService';

class VerifyEmail extends Component {
    constructor(props) {
        super(props);
        this.state = {
            responseMessage: '',
            email: 'Email not found.',  // Placeholder email; replace with actual data as needed.
        };
    }

    componentDidMount() {  
        const { email } = this.props.params;
        this.setState({ email });
    }

    handleResendEmail = async () => {
        const { email } = this.state;
        try {
            await AuthService.resendVerificationEmail(email).then((res) => {
                this.setState({ responseMessage: 'A verification email has been sent.' });
            });
            
        } catch (error) {
            console.error('Resend email error:', error);
            this.setState({ responseMessage: 'Failed to resend email. Please try again.' });
        }
    };

    render() {
        const { responseMessage, email } = this.state;

        return (
            <div className="flex justify-center items-center min-h-screen bg-primary">
                <div className="p-8 rounded-lg shadow-lg w-full max-w-md bg-primary text-center">
                    <div className="flex justify-center mb-4">
                        <div className="bg-green-100 p-4 rounded-full flex items-center justify-center shadow-md">
                            <Mail className="w-8 h-8 text-green-500" />
                        </div>
                    </div>
                    <h2 className="text-2xl font-bold text-primary mb-2">Please verify your email</h2>
                    <p className="text-primary mb-2">You're almost there! We sent an email to</p>
                    <p className="font-semibold text-primary">{email}</p>
                    <p className="text-primary mt-4">Just click on the link in that email to complete your signup. If you don’t see it, you may need to <span className="font-semibold text-primary">check your spam folder</span>.</p>
                    <p className="text-primary mt-4">Still can’t find the email? No problem.</p>

                    {/* Resend Verification Button */}
                    <button
                        onClick={this.handleResendEmail}
                        className="btn btn-primary w-full mt-6"
                    >
                        Resend Verification Email
                    </button>

                    {/* Response Message */}
                    {responseMessage && <p className="text-center text-sm text-primary mt-4">{responseMessage}</p>}
                </div>
            </div>
        );
    }
}

export default withNavigateandLocation(VerifyEmail);
