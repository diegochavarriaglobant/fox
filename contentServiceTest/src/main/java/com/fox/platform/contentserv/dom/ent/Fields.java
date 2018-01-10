package com.fox.platform.contentserv.dom.ent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {
    public String Thumbnail;
    public String Image;
    public String EpisodeId;
    public String Image620x240;
    public String Shortname;
    public String ProductionId;
    public String Image145x105;
    public String Description;


    public String tuneInDate;
    public String tuneInHour;
    public String eventDuration;
    public String statisticsId;

    @JsonProperty(value="header-image")
    public String headerImage;

    public String validation;
    public String metakeywords;
    public String eventName;
    public String embed_code;
    public String theme;
    public String hashtag;

    @JsonProperty(value="main-image")
    public String mainImage;

    public String auth_level_cl;
    public String competitionName;
    public String sport;
    public String signal;
    public String auth_level;
    public String eventDate;
    public String deck;
    public String streams_deprecated;
    public String channel;

    public String picture;
    public String stats_id;
    public String picture_national;
    public String datafactory_id;
    public String full_name;

    @JsonProperty(value="playlist-description")
    public String playlistDescription;

    public String thumb;
    public String image;

    @JsonProperty(value="short-name")
    public String shortName;
    public String image200x200;
    public String mrss;
    public String tags;
    public String Character;
    public String Actor;
    public String name;
    public String vendor;
    public String url;
    public String match_type;
    public String team_b;
    public String competition;
    public String poll;
    public String date;
    public String team_a;
    public String time;

    public String unbundled;
    public String image620x240;
    public String eventEnd;
    public String intellicore_id;
    public String programmingSystem;
    public String logo;




}
