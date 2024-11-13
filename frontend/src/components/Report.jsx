import React, { Component } from 'react';
import withNavigateandLocation from './withNavigateandLocation';
import PlayerService from '../services/PlayerService';

class Report extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      reason: '',
      description: '',
    };
  }

  // Handle form submission
  handleSubmit = async (e) => {
    e.preventDefault();
    const report = {
      username: this.state.username,
      reason: this.state.reason,
      description: this.state.description,
    };

    console.log("Report data => " + JSON.stringify(report));

    try {
      await PlayerService.reportPlayer(report).then((res) => {
        if (res.data) {
          console.log("User reported successfully");
          // Navigate or display success message here if needed
        }
      });
    } catch (error) {
      console.error('Report error:', error.response ? error.response.data : error.message);
    }
  };

  // Handle input change
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  render() {
    const { username, reason, description } = this.state;

    return (
      <div className="flex justify-center items-center min-h-screen">
        <form className="p-8 rounded-lg border border-accent shadow-lg w-96 bg-secondary">
          <h2 className="text-2xl font-bold text-center mb-6">Report User</h2>

          <div className="mb-4">
            <label className="block text-primary text-sm font-bold mb-2">Username</label>
            <input
              type="text"
              name="username"
              value={username}
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
            onClick={this.handleSubmit}
          >
            Submit Report
          </button>
        </form>
      </div>
    );
  }
}

export default withNavigateandLocation(Report);
