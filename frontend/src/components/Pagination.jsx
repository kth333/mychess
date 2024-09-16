// src/components/Pagination.js
import React from 'react';
import { Button } from "./ui/button";

const Pagination = ({ currentPage, totalItems, itemsPerPage, onPageChange }) => {
  const totalPages = Math.ceil(totalItems / itemsPerPage);

  const handlePageChange = (pageNumber) => {
    onPageChange(pageNumber);
  };

  return (
    <div className="flex justify-center mt-8">
      {Array.from({ length: totalPages }, (_, index) => index + 1).map(pageNumber => (
        <Button
          key={pageNumber}
          className={`mx-1 ${currentPage === pageNumber ? 'bg-blue-600 text-white' : 'bg-white text-blue-600'}`}
          onClick={() => handlePageChange(pageNumber)}
        >
          {pageNumber}
        </Button>
      ))}
    </div>
  );
};

export default Pagination;
