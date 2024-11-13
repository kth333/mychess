import React, { Component } from 'react';
import withNavigateandLocation from './withNavigateandLocation';
import PlayerService from '../services/PlayerService';

class Report extends Component {
  constructor(props) {
    super(props);
    this.state = {
      reportedPlayerUsername: '', // Username of the reported player
      reporterPlayerUsername: '', // Username of the reporter, set automatically
      reason: '',
      description: '',
      showAlert: false,
      alertType: '', // "success" or "error"
      alertMessage: '',
    };
  }

  componentDidMount() {
    // Retrieve the logged-in user's username from sessionStorage
    const reporterUsername = sessionStorage.getItem("username");
    console.log(reporterUsername);
    if (reporterUsername) {
      this.setState({ reporterPlayerUsername: reporterUsername });
    }
  }

  // Handle form submission
  handleSubmit = async (e) => {
    e.preventDefault();
    const report = {
      reportedPlayerUsername: this.state.reportedPlayerUsername,
      reporterPlayerUsername: this.state.reporterPlayerUsername,
      reason: this.state.reason,
      description: this.state.description,
    };

    console.log("Report data => ", JSON.stringify(report));

    try {
      await PlayerService.reportPlayer(report).then((res) => {
        if (res.data) {
          console.log("User reported successfully");
          this.setState({
            showAlert: true,
            alertType: 'success',
            alertMessage: 'Report submitted successfully!',
          });

          // Optional navigation after showing the alert
          setTimeout(() => {
            this.setState({ showAlert: false });
            this.props.navigate('/'); // Redirect to home or another page
          }, 3000);
        }
      });
    } catch (error) {
      const errorMessage = error.response ? error.response.data : error.message;
      console.error('Report error:', errorMessage);
      this.setState({
        showAlert: true,
        alertType: 'error',
        alertMessage: `Failed to submit report: ${errorMessage}`,
      });

      setTimeout(() => {
        this.setState({ showAlert: false });
      }, 5000); // Hide alert after 5 seconds
    }
  };

  // Handle input change
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  render() {
    const { reportedPlayerUsername, reason, description, showAlert, alertType, alertMessage } = this.state;

    return (
        <div className="flex justify-center items-center min-h-screen">
          {/* Alert Message */}
          {showAlert && (
              <div
                  className={`fixed top-4 w-96 p-4 rounded-md shadow-lg ${
                      alertType === 'success' ? 'bg-green-500 text-white' : 'bg-red-500 text-white'
                  }`}
              >
                <p className="font-semibold">{alertMessage}</p>
              </div>
          )}

          {/* Report Form */}
          <form className="p-8 rounded-lg border border-accent shadow-lg w-96 bg-secondary" onSubmit={this.handleSubmit}>
            <h2 className="text-2xl font-bold text-center mb-6">Report User</h2>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Reported Player Username</label>
              <input
                  type="text"
                  name="reportedPlayerUsername"
                  placeholder="Reported Player Username"
                  value={reportedPlayerUsername}
                  onChange={this.handleInputChange}
                  required
                  className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Report Reason</label>
              <input
                  type="text"
                  name="reason"
                  placeholder="Reason for Report"
                  value={reason}
                  onChange={this.handleInputChange}
                  required
                  className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-6">
              <label className="block text-primary text-sm font-bold mb-2">Description</label>
              <textarea
                  name="description"
                  placeholder="Additional details"
                  value={description}
                  onChange={this.handleInputChange}
                  required
                  rows="4"
                  className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <button
                type="submit"
                className="btn btn-primary w-full font-bold"
            >
              Submit Report
            </button>
          </form>
        </div>
    );
  }
}

export default withNavigateandLocation(Report);
