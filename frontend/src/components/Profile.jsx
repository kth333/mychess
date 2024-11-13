import React from 'react';
import { Card, CardHeader, CardContent, CardFooter } from "./ui/card";
import { AvatarImage } from './ui/avatar';
import { Link, useParams } from 'react-router-dom';
import { Button } from './ui/button';
import withNavigateandLocation from './withNavigateandLocation';
import PlayerService from '../services/PlayerService';
import { Avatar } from '@radix-ui/react-avatar';
import EloChart from './ui/EloChart';

class Profile extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            profile: null,
            ratingHistory: [],
            followers: [],
            following: [],
            isCurrentUser: false,
            isFollowing: false,
            showModal: false,
            modalContent: 'followers',
            currentPage: 0,
            totalPages: 0,
        };
    }

    componentDidMount() {
        this.fetchProfileData();
    }

    componentDidUpdate(prevProps) {
        // Check if the playerId URL parameter has changed
        if (prevProps.params.playerId !== this.props.params.playerId) {
            this.fetchProfileData(); // Re-fetch data if the playerId has changed
        }
    }

    fetchProfileData = async () => {
        const { playerId } = this.props.params;
        const currentPlayerId = parseInt(sessionStorage.getItem("currentPlayerId"));

        try {
            const response = await PlayerService.getProfile(playerId || null);
            const profileData = response.data;

            if (!profileData.isPublic && profileData.playerId !== currentPlayerId) {
                alert("This profile is private.");
                this.setState({ profile: null });
            } else {
                this.setState({
                    profile: profileData,
                    isCurrentUser: profileData.playerId === currentPlayerId,
                }, () => {
                    this.fetchPlayerRatingHistory(profileData.playerId);
                    this.fetchFollowersAndFollowing(profileData.playerId, 0);
                    if (!this.state.isCurrentUser) this.checkIfFollowing(profileData.playerId);
                });
            }
        } catch (error) {
            console.error('Error fetching profile:', error.response ? error.response.data : error.message);
        }
    };

    fetchPlayerRatingHistory = async (playerId) => {
        try {
            const response = await PlayerService.getPlayerRatingHistory(playerId);
            this.setState({ ratingHistory: response.data });
        } catch (error) {
            console.error('Error fetching rating history:', error.response ? error.response.data : error.message);
        }
    }

    fetchFollowersAndFollowing = async (playerId, page) => {
        const pageSize = 5; // Define number of entries per page
        try {
            const followersResponse = await PlayerService.getFollowers(playerId, page, pageSize);
            const followingResponse = await PlayerService.getFollowing(playerId, page, pageSize);
            const totalPages = Math.max(followersResponse.data.totalPages, followingResponse.data.totalPages);

            this.setState({
                followers: followersResponse.data.content,
                following: followingResponse.data.content,
                totalPages,
                currentPage: page,
            });
        } catch (error) {
            console.error('Error fetching followers/following:', error);
        }
    };

    checkIfFollowing = async (profileId) => {
        const currentPlayerId = parseInt(sessionStorage.getItem("currentPlayerId"));
        try {
            const followingList = await PlayerService.getFollowing(currentPlayerId);

            // Access the 'content' array within the response
            if (Array.isArray(followingList.data.content)) {
                const isFollowing = followingList.data.content.some(followed => followed.id === profileId);
                this.setState({ isFollowing });
            } else {
                console.error('Expected followingList.data.content to be an array but got:', followingList.data.content);
                this.setState({ isFollowing: false }); // Default to not following if data is unexpected
            }
        } catch (error) {
            console.error('Error checking if following:', error);
        }
    };

    handleFollow = async () => {
        const { playerId } = this.props.params;
        const currentPlayerId = parseInt(sessionStorage.getItem("currentPlayerId"));
        try {
            await PlayerService.followPlayer(currentPlayerId, playerId);
            this.setState({ isFollowing: true });
            this.fetchFollowersAndFollowing(playerId, this.state.currentPage);
        } catch (error) {
            console.error("Error following player:", error);
        }
    };

    handleUnfollow = async () => {
        const { playerId } = this.props.params;
        const currentPlayerId = parseInt(sessionStorage.getItem("currentPlayerId"));
        try {
            await PlayerService.unfollowPlayer(currentPlayerId, playerId);
            this.setState({ isFollowing: false });
            this.fetchFollowersAndFollowing(playerId, this.state.currentPage);
        } catch (error) {
            console.error("Error unfollowing player:", error);
        }
    };

    toggleModal = (content) => {
        this.setState((prevState) => ({
            showModal: !prevState.showModal,
            modalContent: content,
            currentPage: 0
        }), () => {
            const { profile } = this.state;
            this.fetchFollowersAndFollowing(profile.playerId, 0);
        });
    };

    handlePageChange = (direction) => {
        const newPage = this.state.currentPage + direction;
        if (newPage >= 0 && newPage < this.state.totalPages) {
            this.fetchFollowersAndFollowing(this.state.profile.playerId, newPage);
        }
    };

    render() {
        const { profile, ratingHistory, followers, following, isCurrentUser, isFollowing, showModal, modalContent, currentPage, totalPages } = this.state;

        if (!profile) {
            return <div>Loading...</div>;
        }

        const {
            username, playerId, fullName, bio, avatarUrl, gender, country, region, city, birthDate,
            rank, glickoRating, ratingDeviation, volatility, totalWins, totalLosses, totalDraws, isPublic, age
        } = profile;

        const displayList = modalContent === 'followers' ? followers : following;

        return (
            <div>
                {/* Profile Background Image */}
                <div className="image-container" style={{ overflow: 'hidden', maxHeight: '300px' }}>
                    <img
                        src="https://cdn.pixabay.com/photo/2017/09/08/20/29/chess-2730034_1280.jpg"
                        alt="Background image"
                        className="responsive-image"
                        style={{ width: '100%', objectFit: 'cover', height: '100%' }}
                    />
                </div>
                <div className="container w-3/4 p-4">
                    <Card className="bg-base-200 shadow-md flex-column">
                        <CardHeader className="flex-row">
                            <Card className="bg-base-200 shadow md flex-row justify-start w-1/3">
                                <div className="flex justify-start">
                                    <div className="flex flex-col items-center ml-4">
                                        <h2 className="text-2xl font-semibold text-primary text-center">
                                            {username || 'Unknown'}
                                            <p className="text-sm text-red-500 text-secondary ml-2 inline">
                                                {rank || 'Unranked'}
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
                                            <p><strong>Name:</strong> {fullName || "Unknown"}</p>
                                            <p><strong>Bio:</strong> {bio || "No bio available"}</p>
                                        </div>
                                        <div className="p-4 shadow-md rounded-md">
                                            <h4 className="text-lg font-semibold">Location Information</h4>
                                            <p><strong>Country:</strong> {country || "Unknown"}</p>
                                            <p><strong>Region:</strong> {region || "Unknown"}</p>
                                            <p><strong>City:</strong> {city || "Unknown"}</p>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                            <EloChart ratingHistory={ratingHistory} />
                        </CardHeader>
                        <div className="mt-4 flex justify-between">
                            <Button onClick={() => this.toggleModal('followers')}>Followers ({followers.length})</Button>
                            <Button onClick={() => this.toggleModal('following')}>Following ({following.length})</Button>
                        </div>

                        {/* Follow/Unfollow and Edit Buttons */}
                        {!isCurrentUser && (
                            <CardFooter className="flex justify-end space-x-4">
                                {isFollowing ? (
                                    <Button className="btn btn-danger" onClick={this.handleUnfollow}>Unfollow</Button>
                                ) : (
                                    <Button className="btn btn-primary" onClick={this.handleFollow}>Follow</Button>
                                )}
                            </CardFooter>
                        )}
                        {isCurrentUser && (
                            <CardFooter className="flex justify-end">
                                <Link className="btn btn-accent" to={`/profile/update/${playerId}`}>Edit Profile</Link>
                            </CardFooter>
                        )}
                    </Card>
                </div>

                {/* Modal for Followers/Following with Pagination */}
                {showModal && (
                    <div style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%',
                        backgroundColor: 'rgba(0, 0, 0, 0.5)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        zIndex: 1000
                    }}>
                        <div style={{
                            backgroundColor: 'white',
                            padding: '20px',
                            borderRadius: '8px',
                            width: '300px',
                            maxHeight: '400px',
                            overflowY: 'auto',
                        }}>
                            <h3 className="text-lg font-semibold">{modalContent === 'followers' ? 'Followers' : 'Following'}</h3>
                            <ul>
                                {displayList.length > 0 ? (
                                    displayList.map((user) => (
                                        <li key={user.id}>{user.username}</li>
                                    ))
                                ) : (
                                    <p>No {modalContent} yet</p>
                                )}
                            </ul>
                            <div className="flex justify-between mt-4">
                                <Button onClick={() => this.handlePageChange(-1)} disabled={currentPage === 0}>Previous</Button>
                                <span>Page {currentPage + 1} of {totalPages}</span>
                                <Button onClick={() => this.handlePageChange(1)} disabled={currentPage === totalPages - 1}>Next</Button>
                            </div>
                            <Button onClick={() => this.toggleModal('')} className="mt-4 btn btn-primary">Close</Button>
                        </div>
                    </div>
                )}
            </div>
        );
    }
}

const ProfileWithParams = (props) => (
    <Profile {...props} params={useParams()} />
);

export default withNavigateandLocation(ProfileWithParams);