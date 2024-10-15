import React from 'react';
import { Card, CardHeader, CardContent, CardFooter } from "./ui/card";
import { AvatarImage } from './ui/avatar';
import { Link } from 'react-router-dom';
import { Button } from './ui/button';
import withNavigateandLocation from './withNavigateandLocation';
import PlayerService from '../services/PlayerService';
import { Avatar } from '@radix-ui/react-avatar';

class Profile extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            profile: this.props.profile
        };
    }

    componentDidMount() {
        this.fetchProfileData();
    }

    fetchProfileData = async () => {
        try{
            const response = await PlayerService.getProfile();
            this.setState({ profile: response.data });
            console.log('Profile data:', response.data);
        } catch (error) {
            console.error('Error fetching profile:', error.response ? error.response.data : error.message);
        }
    }

    render() {
        const {
            playerId, fullName, bio, avatarUrl, gender, country, region, city, birthDate,
            rank, glickoRating, ratingDeviation, volatility, totalWins, totalLosses, totalDraws, isPublic, age
        } = this.state.profile || {};

        return (
            <div className="container mx-auto p-4">
                <Card className="bg-secondary shadow-md">
                    <CardHeader>
                        <div className="flex items-center space-x-4">
                            <div>
                            <Avatar>
                                <AvatarImage src={avatarUrl} alt={`${fullName}'s avatar`} size="large" className="w-36 h-36 rounded-full" />
                            </Avatar>
                                <h2 className="text-2xl font-semibold text-primary">{fullName}</h2>
                                <p className="text-sm text-secondary">Rank: {rank ? rank : 'Unranked'}</p>
                            </div>
                        </div>
                    </CardHeader>

                    <CardContent>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <p className="text-lg"><strong>Bio:</strong> {bio || "No bio available"}</p>
                                <p><strong>Country:</strong> {country}</p>
                                <p><strong>Region:</strong> {region}</p>
                                <p><strong>City:</strong> {city}</p>
                                <p><strong>Gender:</strong> {gender}</p>
                                <p><strong>Birth Date:</strong> {birthDate ? birthDate.toString() : "N/A"}</p>
                                <p><strong>Age:</strong> {age}</p>
                            </div>

                            <div>
                                <h3 className="text-lg font-semibold">Glicko Ratings</h3>
                                <p><strong>Rating:</strong> {glickoRating}</p>
                                <p><strong>Deviation:</strong> {ratingDeviation}</p>
                                <p><strong>Volatility:</strong> {volatility}</p>

                                <h3 className="mt-4 text-lg font-semibold">Game Stats</h3>
                                <p><strong>Total Wins:</strong> {totalWins}</p>
                                <p><strong>Total Losses:</strong> {totalLosses}</p>
                                <p><strong>Total Draws:</strong> {totalDraws}</p>

                                <p className="mt-4">
                                    <strong>Profile Visibility:</strong> {isPublic ? "Public" : "Private"}
                                </p>
                            </div>
                        </div>
                    </CardContent>

                    <CardFooter className="flex justify-end">
                        <Link className="btn btn-accent" to={`/profile/update/${playerId}`}>Edit Profile</Link>
                    </CardFooter>
                </Card>
            </div>
        );
    }
}

export default withNavigateandLocation(Profile);
