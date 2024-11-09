package com.g1.mychess.match.mapper;

import com.g1.mychess.match.dto.MatchDTO;
import com.g1.mychess.match.model.Match;

import java.util.List;
import java.util.stream.Collectors;

public class MatchMapper {

    public static MatchDTO toDTO(Match match) {
        return MatchDTO.fromEntity(match);
    }

    public static List<MatchDTO> toDTOList(List<Match> matches) {
        return matches.stream()
                .map(MatchMapper::toDTO)
                .collect(Collectors.toList());
    }
}