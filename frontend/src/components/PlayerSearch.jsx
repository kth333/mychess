import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import PlayerService from '../services/PlayerService';

const PlayerSearch = () => {
    const [query, setQuery] = useState('');
    const [results, setResults] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [notification, setNotification] = useState('');
    const pageSize = 10;

    const role = sessionStorage.getItem('role');
    const currentPlayerId = sessionStorage.getItem('currentPlayerId');

    const handleSearch = async (page = 0) => {
        try {
            const response = await PlayerService.searchPlayers(query, page, pageSize);
            setResults(response.data.content);
            setCurrentPage(page);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('Search error:', error);
        }
    };

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            handleSearch(0);
        }
    };

    const handleNextPage = () => {
        if (currentPage < totalPages - 1) handleSearch(currentPage + 1);
    };

    const handlePreviousPage = () => {
        if (currentPage > 0) handleSearch(currentPage - 1);
    };

    const handleFollow = async (playerId) => {
        try {
            await PlayerService.followPlayer(currentPlayerId, playerId);
            setNotification('Followed successfully!');
            handleSearch(currentPage); // Refresh results to reflect follow status
            setTimeout(() => setNotification(''), 3000); // Clear notification after 3 seconds
        } catch (error) {
            console.error('Follow error:', error);
        }
    };

    const handleUnfollow = async (playerId) => {
        try {
            await PlayerService.unfollowPlayer(currentPlayerId, playerId);
            setNotification('Unfollowed successfully!');
            handleSearch(currentPage); // Refresh results to reflect unfollow status
            setTimeout(() => setNotification(''), 3000); // Clear notification after 3 seconds
        } catch (error) {
            console.error('Unfollow error:', error);
        }
    };

    return (
        <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px', backgroundColor: '#f9f9f9', borderRadius: '8px', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
            <div style={{ display: 'flex', marginBottom: '20px' }}>
                <input
                    type="text"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="Search for players"
                    style={{
                        flex: 1,
                        padding: '10px',
                        fontSize: '16px',
                        border: '1px solid #ddd',
                        borderRadius: '4px 0 0 4px',
                    }}
                />
                <button
                    onClick={() => handleSearch(0)}
                    style={{
                        padding: '10px 20px',
                        fontSize: '16px',
                        backgroundColor: '#007bff',
                        color: 'white',
                        border: 'none',
                        borderRadius: '0 4px 4px 0',
                        cursor: 'pointer',
                        transition: 'background-color 0.3s',
                    }}
                >
                    Search
                </button>
            </div>

            {notification && (
                <div style={{ marginBottom: '20px', padding: '10px', backgroundColor: '#28a745', color: 'white', borderRadius: '4px', textAlign: 'center' }}>
                    {notification}
                </div>
            )}

            <ul style={{ listStyleType: 'none', padding: 0 }}>
                {results.map(player => (
                    <li key={player.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '10px 0', borderBottom: '1px solid #eee' }}>
                        <Link
                            to={`/profile/${player.id}`}
                            style={{ color: '#007bff', textDecoration: 'underline', fontWeight: 'bold' }}
                        >
                            {player.username}
                        </Link>
                        {role !== 'ROLE_ADMIN' && (
                            <div>
                                <button
                                    onClick={() => handleFollow(player.id)}
                                    style={{
                                        padding: '6px 12px',
                                        fontSize: '14px',
                                        backgroundColor: '#28a745',
                                        color: 'white',
                                        border: 'none',
                                        borderRadius: '4px',
                                        cursor: 'pointer',
                                        marginRight: '5px',
                                        transition: 'background-color 0.3s',
                                    }}
                                >
                                    Follow
                                </button>
                                <button
                                    onClick={() => handleUnfollow(player.id)}
                                    style={{
                                        padding: '6px 12px',
                                        fontSize: '14px',
                                        backgroundColor: '#dc3545',
                                        color: 'white',
                                        border: 'none',
                                        borderRadius: '4px',
                                        cursor: 'pointer',
                                        transition: 'background-color 0.3s',
                                    }}
                                >
                                    Unfollow
                                </button>
                            </div>
                        )}
                    </li>
                ))}
            </ul>

            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '20px' }}>
                <button
                    onClick={handlePreviousPage}
                    disabled={currentPage === 0}
                    style={{
                        padding: '8px 16px',
                        fontSize: '14px',
                        backgroundColor: currentPage === 0 ? '#ccc' : '#007bff',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: currentPage === 0 ? 'not-allowed' : 'pointer',
                        transition: 'background-color 0.3s',
                    }}
                >
                    Previous
                </button>
                <span>Page {currentPage + 1} of {totalPages}</span>
                <button
                    onClick={handleNextPage}
                    disabled={currentPage === totalPages - 1}
                    style={{
                        padding: '8px 16px',
                        fontSize: '14px',
                        backgroundColor: currentPage === totalPages - 1 ? '#ccc' : '#007bff',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: currentPage === totalPages - 1 ? 'not-allowed' : 'pointer',
                        transition: 'background-color 0.3s',
                    }}
                >
                    Next
                </button>
            </div>
        </div>
    );
};

export default PlayerSearch;