import React, { Component } from 'react';
import { Card } from '../ui/card';
import { Button } from '../ui/button';
import { useParams } from 'react-router-dom';
import withNavigateandLocation from '../withNavigateandLocation';
import TournamentService from '../../services/TournamentService';

class TournamentDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tournament: null, // will store the tournament details
        };
    }

    async componentDidMount() {
        await this.fetchData();
    }

    signUp = async () => {
        const { id } = this.state.tournament;
        try {
            await TournamentService.signUp(id);
            // Redirect to player profile after successful sign up
            
        } catch (error) {
            console.error("Failed to sign up for tournament", error);
            alert("Failed to sign up for tournament\nReason: " + error.response.data);
        }
        // this.props.navigate('/profile');
    };

    fetchData = async () => {
        const { name } = this.props.params;
    
        try {
          const res = await TournamentService.getTournamentByName(name);
          console.log("Tournament data:", res.data);
          this.setState({ tournament: res.data });
        } catch (error) {
          console.error("Failed to fetch tournament", error);
        }
    };

    render() {
        const { tournament } = this.state;
        const userRole = sessionStorage.getItem("role");
        const isPlayer = userRole === 'ROLE_PLAYER';

        if (!tournament) {
            return <p>Loading...</p>; // Display loading if tournament data is not yet fetched
        }

        const { 
            name, 
            description, 
            startDateTime, 
            endDateTime, 
            registrationStartDate, 
            registrationEndDate, 
            format, 
            status, 
            minRating, 
            maxRating, 
            affectsRating, 
            minAge, 
            maxAge, 
            requiredGender, 
            city, 
            region, 
            country, 
            timeControl 
        } = tournament;

        return (
            <div className="p-6 max-w-4xl mx-auto">
                <Card className="p-4 bg-base-200">
                    <h2 className="text-2xl font-bold text-primary">
                        {name}
                    </h2>
                    <p className="my-2 text-accent">
                        {description}
                    </p>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 my-4">
                        <div>
                            <h5 className="text-xl font-semibold text-primary">
                                Tournament Details
                            </h5>
                            <ul className="list-disc ml-6">
                                <li>Start: {new Date(startDateTime).toLocaleString()}</li>
                                <li>End: {new Date(endDateTime).toLocaleString()}</li>
                                <li>Format: {format}</li>
                                <li>Status: {status}</li>
                            </ul>
                        </div>

                        <div>
                            <h5 className="text-xl font-semibold text-primary">
                                Requirements
                            </h5>
                            <ul className="list-disc ml-6">
                                <li>Min Rating: {minRating}</li>
                                <li>Max Rating: {maxRating}</li>
                                <li>Affects Rating: {affectsRating ? 'Yes' : 'No'}</li>
                                <li>Min Age: {minAge}</li>
                                <li>Max Age: {maxAge}</li>
                                <li>Required Gender: {requiredGender}</li>
                                <li>Location: {city}, {region}, {country}</li>
                            </ul>
                        </div>
                    </div>

                    <div className="my-4">
                        <h5 className="text-xl font-semibold text-primary">
                            Time Control
                        </h5>
                        <p className="text-secondary">
                            Base Time: {timeControl.baseTimeMinutes} minutes, Increment: {timeControl.incrementSeconds} seconds, Type: {timeControl.timeControlType}
                        </p>
                        <p className="text-secondary">
                            Registration from {new Date(registrationStartDate).toLocaleString()} to {new Date(registrationEndDate).toLocaleString()}
                        </p>
                    </div>

                    <Button className="btn btn-primary mt-6" onClick={this.signUp} disabled={!isPlayer}>
                        Sign Up
                    </Button>
                </Card>
            </div>
        );
    }
}

// Wrap the component with `withRouter` to access `this.props.params` in class components
export default withNavigateandLocation((props) => (
    <TournamentDetails {...props} params={useParams()} />
));
