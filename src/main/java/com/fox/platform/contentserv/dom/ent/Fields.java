package com.fox.platform.contentserv.dom.ent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * POJO class to match the Json object
 * @author diego.chavarria
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {

    @JsonProperty(value="Thumbnail")
    private String thumbnail;

    @JsonProperty(value="Image")
    private String image1;

    @JsonProperty(value="EpisodeId")
    private String episodeId;

    @JsonProperty(value="Image620x240")
    private String image620x2401;

    @JsonProperty(value="Shortname")
    private String shortName1;

    @JsonProperty(value="ProductionId")
    private String productionId;

    @JsonProperty(value="Image145x105")
    private String image145x105;

    @JsonProperty(value="Description")
    private String description;

    private String tuneInDate;

    private String tuneInHour;

    private String eventDuration;

    private String statisticsId;

    @JsonProperty(value="header-image")
    private String headerImage;

    private String validation;

    private String metakeywords;

    private String eventName;

    @JsonProperty(value="embed_code")
    private String embedCode;

    private String theme;

    private String hashtag;

    @JsonProperty(value="main-image")
    private String mainImage;

    @JsonProperty(value="auth_level_cl")
    private String authLevelCl;

    private String competitionName;

    private String sport;

    private String signal;

    @JsonProperty(value="auth_level")
    private String authLevel;

    private String eventDate;

    private String deck;

    @JsonProperty(value="streams_deprecated")
    private String streamsDeprecated;

    private String channel;

    private String picture;

    @JsonProperty(value="stats_id")
    private String statsId;

    @JsonProperty(value="picture_national")
    private String pictureNational;

    @JsonProperty(value="datafactory_id")
    private String datafactoryId;

    @JsonProperty(value="full_name")
    private String fullName;

    @JsonProperty(value="playlist-description")
    private String playlistDescription;

    private String thumb;

    private String image;

    @JsonProperty(value="short-name")
    private String shortName;

    private String image200x200;

    private String mrss;

    private String tags;

    @JsonProperty(value="Character")
    private String character;

    @JsonProperty(value="Actor")
    private String actor;

    private String name;

    private String vendor;

    private String url;

    @JsonProperty(value="match_type")
    private String matchType;

    @JsonProperty(value="team_b")
    private String teamB;

    private String competition;

    private String poll;

    private String date;

    @JsonProperty(value="team_a")
    private String teamA;

    private String time;

    private String unbundled;

    private String image620x240;

    private String eventEnd;

    @JsonProperty(value="intellicore_id")
    private String intellicoreId;

    private String programmingSystem;

    private String logo;




}
