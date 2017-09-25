package com.garydty.a10366827.models;

import com.garydty.a10366827.utility.GsonRequest;
import com.garydty.a10366827.utility.RiotRequestHelper;

/**
 * Created by Gary Doherty on 25/09/2017.
 */

public class Summoner {
    public String name;
    public int profileIconId;
    public long id;
    public long revisionDate;
    public long accountId;
    public long summonerLevel;

    public static final String PATH = "/lol/summoner/v3/summoners/by-name/";
}
