import React from 'react';
import { Card, CardHeader, CardContent, CardFooter } from "./ui/card";
import { Avatar } from './ui/avatar';
import { Button } from './ui/button';
import withNavigateandLocation from './withNavigateandLocation';
import PlayerService from '../services/PlayerService';
import { Avatar } from '@radix-ui/react-avatar';
import styles from './styles/styles.css';
import EloChart from './ui/EloChart'
import { testData } from './test/data';

class Profile extends React.Component {
    constructor(props) {
        super(props);
        // Destructure the data passed to the component, assuming props contains the playerProfileDTO object.
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
            username, playerId, fullName, bio, avatarUrl, gender, country, region, city, birthDate,
            rank, glickoRating, ratingDeviation, volatility, totalWins, totalLosses, totalDraws, isPublic, age
        } = this.state.profile || {};

        return (
        <div>
            <div className="image-container">
                <img
                    src="https://cdn.pixabay.com/photo/2017/09/08/20/29/chess-2730034_1280.jpg"
                    alt="Description of image"
                    className="responsive-image"
                /> {/* Need to dynamically allocate URL */}
            </div>
            <div className="container w-3/4 p-4">
                <Card className="bg-base-200 shadow-md flex-column">
                    <CardHeader className="flex-row">
                        <Card className="bg-base-200 shadow md flex-row justify-start w-1/3">
                            <div className="flex justify-start">
                                <div className="flex flex-col items-center ml-4"> {/* Center the username and rank */}
                                    <h2 className="text-2xl font-semibold text-primary text-center">
                                        <span>{username ? username : 'Unknown'}</span>
                                        <p className="text-sm text-red-500 text-secondary ml-2 inline">
                                            {rank ? rank : 'Unranked'}
                                        </p>
                                    </h2>
                                    <Avatar className="w-96 h-64 justify-center">
                                        <AvatarImage
                                            src={avatarUrl}
                                            alt={`${username}'s avatar`}
                                            size="large"
                                            className="w-full h-full"
                                        />
                                    </Avatar>
                                </div>
                            </div>
                            <CardContent>
                                <div className="mr-4 flex flex-col w-96">
                                    <div className="p-4 shadow-md rounded-md">
                                        <h3 className="text-xl font-bold border-b pb-2 mb-4">Player Information</h3>
                                        <div className="space-y-2">
                                            <p>
                                               <strong>Name:</strong> {fullName || "Unknown"}
                                            </p>
                                            <p>
                                               <strong>Bio:</strong> {bio || "No bio available"}
                                            </p>
                                       </div>
                                   </div>
                                   <div className="p-4 shadow-md rounded-md">
                                       <h4 className="text-lg font-semibold">Location Information</h4>
                                       <div className="space-y-2">
                                           <p className="text-lg">
                                               <strong>Country:</strong> {country || "Unknown"}
                                           </p>
                                           <p className="text-lg">
                                               <strong>Region:</strong> {region || "Unknown"}
                                           </p>
                                           <p className="text-lg">
                                               <strong>City:</strong> {city || "Unknown"}
                                           </p>
                                       </div>
                                   </div>
                                   <div className="p-4 shadow-md rounded-md">
                                       <h4 className="text-lg font-semibold">Demographic Information</h4>
                                       <div className="space-y-2">
                                           <p className="text-lg">
                                               <strong>Gender:</strong> {gender || "Not specified"}
                                           </p>
                                           <p className="text-lg">
                                               <strong>Birth Date:</strong> {birthDate ? birthDate.toString() : "N/A"} <br />
                                               <strong>Age:</strong> {age || "N/A"}
                                           </p>
                                       </div>
                                   </div>
                               </div>
                            </CardContent>
                        </Card>
                            <EloChart className="ml-10"/>
                    </CardHeader>
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
                    <CardFooter className="flex justify-end">
                        <Button className="btn btn-primary">Edit Profile</Button>
                    </CardFooter>
                </Card>
            </div>
            </div>
        );
    }
}

export default withNavigateandLocation(Profile);
